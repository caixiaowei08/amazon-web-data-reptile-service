/**
 * Created by User on 2017/8/2.
 */
$(function () {
    $('#authorListTable').bootstrapTable({
        url: "/adminAuthorController.admin?dataGrid",
        sidePagination: "server",
        dataType: "json",
        search: false,
        pageNumber: 1,
        pageSize: 20,
        sortName: "id",
        sortOrder: 'desc',
        pageList: [10, 20, 30, 50, 100],
        pagination: true,
        height: 700,
        clickToSelect: true,
        uniqueId: "id",
        locale: "zh-CN",
        showColumns: false,
        singleSelect: true,
        onLoadError: function () {
            setTimeout("window.location='/adminSystemController.admin?goAdminLogin'", 1000);
        },
        columns: [[
            {
                title: '编号',
                field: "id",
                width: "8%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '账号',
                field: "account",
                width: "8%",//宽度
                align: "center",//水平
                valign: "middle"
            },
            {
                title: '状态',
                field: "status",
                width: "8%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    if (value == 1) {
                        return "<span class='label label-success'>使用</span>"
                    } else if (value == 2) {
                        return "<span class='label label-default'>冻结</span>"
                    }
                    return "";
                }
            },
            {
                title: '最近登录时间',
                field: "loginTime",
                width: "8%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '创建时间',
                field: "createTime",
                width: "8%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '操作',
                field: "id",
                width: "10%",//宽度
                formatter: function (value, row, index) {
                    return "&nbsp;&nbsp;<a onclick='loadAuthorInfo(" + value + ")' href='#' title='修改账户' data-target='#updateAuthorModel'  data-toggle='modal'><i class='fa fa-pencil'></i></a>&nbsp;&nbsp;&nbsp;&nbsp;<a onclick='loadDeleteAuthorInfo(" + row.id +");' title='删除账号' href='#' data-target='#deleteAuthorModel' data-toggle='modal'><i class='fa fa-trash'></i></a>";
                }
            }
        ]],
        queryParams: function (params) {
            params.account = $("#account").val().trim();
            return params;
        }
    });
    addNewAuthorValidator();//初始化
    updateAuthorValidator();
    ko.applyBindings(viewModel);
});

var viewModel = {
    account: ko.observable(),
    pwd: ko.observable(),
    updateAccount: ko.observable(),
    updateId: ko.observable(),
    updateStatus: ko.observable(),
    deleteId: ko.observable(),
    deleteAccount: ko.observable()
}

//搜索
function doAuthorSearch() {
    $('#authorListTable').bootstrapTable("refresh");
}

//新建普通用户校验
function addNewAuthorValidator() {
    $('#formAddNewAuthorModel').bootstrapValidator({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            account: {
                validators: {
                    notEmpty: {
                        message: '请输入普通管理员账号'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_\.]+$/,
                        message: '账号由数字字母下划线和.组成'
                    }
                }
            },
            pwd: {
                validators: {
                    notEmpty: {
                        message: '请输普通管理员入密码'
                    },
                    stringLength: {
                        min: 6,
                        max: 30,
                        message: '密码长度必须在6到30之间'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_\.]+$/,
                        message: '密码由数字字母下划线和.组成'
                    }
                }
            },
        }
    }).on('success.form.bv', function (e) {
        var form = $('#formAddNewAuthorModel');
        e.preventDefault(); // 阻止默认事件提交
        $.post(form.attr('action'), form.serialize(), function (result) {
            if (result.success === "success") {
                toastr.success(result.msg);
                $('#addNewAuthorModel').modal('hide');
            } else if (result.success === "fail") {
                toastr.warning(result.msg);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin';
            }
            viewModel.account("");
            viewModel.pwd("");
            form.bootstrapValidator('disableSubmitButtons', false);
            form.data('bootstrapValidator').resetForm();
            $('#authorListTable').bootstrapTable("refresh");
        }, 'json');
    });
}

function loadAuthorInfo(authorId) {
    $.ajax({
        url: "/adminAuthorController.admin?doGet",
        type: 'post',
        data: {id: authorId},
        success: function (data) {
            if (data.success === "success") {
                viewModel.updateId(data.content.id);
                viewModel.updateAccount(data.content.account);
                viewModel.updateStatus(data.content.status);
                $("#selectStatus option[value='" + data.content.status + "']").attr('selected', 'selected');
            } else if (data.success === "fail") {
                toastr.warning(data.msg);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin'
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.error("服务器异常,请联系管理员！");
        }
    });
}

function updateAuthorValidator() {
    $('#formUpdateAuthorModel').bootstrapValidator({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {}
    }).on('success.form.bv', function (e) {
        var form = $('#formUpdateAuthorModel');
        e.preventDefault(); // 阻止默认事件提交
        $.post(form.attr('action'), form.serialize(), function (result) {
            if (result.success === "success") {
                toastr.success(result.msg);
                viewModel.updateId("");
                viewModel.updateAccount("");
                viewModel.updateStatus("");
                $('#updateAuthorModel').modal('hide');
            } else if (result.success === "fail") {
                toastr.warning(result.msg);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin';
            }
            form.bootstrapValidator('disableSubmitButtons', false);
            form.data('bootstrapValidator').resetForm();
            $('#authorListTable').bootstrapTable("refresh");
        }, 'json');
    });
}

function loadDeleteAuthorInfo(id) {
    $.ajax({
        url: "/adminAuthorController.admin?doGet",
        type: 'post',
        data: {id: id},
        success: function (data) {
            if (data.success === "success") {
                viewModel.deleteId(data.content.id);
                viewModel.deleteAccount(data.content.account);
            } else if (data.success === "fail") {
                toastr.warning(data.msg);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin'
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.error("服务器异常,请联系管理员！");
        }
    });
}

function deleteAuthorById() {
    var authorId = $("#deleteId").val();

    $.ajax({
        url: "/adminAuthorController.admin?doDel",
        type: 'post',
        data: {id: authorId},
        success: function (data) {
            if (data.success === "success") {
                viewModel.deleteId("");
                viewModel.deleteAccount("");
            } else if (data.success === "fail") {
                toastr.warning(data.msg);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin'
            }
            $('#deleteAuthorModel').modal('hide');
            $('#authorListTable').bootstrapTable("refresh");
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.error("服务器异常,请联系管理员！");
        }
    });

}

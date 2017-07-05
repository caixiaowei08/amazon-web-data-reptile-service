/**
 * Created by User on 2017/7/4.
 */
$(function () {
    $('#userListTable').bootstrapTable({
        url: "/adminUserMangeController.admin?dataGrid",
        sidePagination: "server",
        dataType: "json",
        search: false,
        pageNumber: 1,
        pageSize: 20,
        sortName: "updateTime",
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
            toastr.warning("请重新登录！");
            setTimeout("window.location='/adminSystemController.admin?goAdminLogin'", 1000);
        },
        columns: [[
            {
                title: '卖家编号',
                field: "id",
                width: "8%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '邮箱',
                field: "account",
                width: "8%",//宽度
                align: "center",//水平
                valign: "middle"
            },
            {
                title: '状态',
                field: "state",
                width: "8%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    if (value == 1) {
                        return "<span class='label label-success'>正常</span>"
                    } else if (value == 2) {
                        return "<span class='label label-default'>禁用</span>"
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
                    return "&nbsp;&nbsp;&nbsp;&nbsp;<a onclick='loadUserVipInfo(" + value + ")' href='#' title='VIP充值' data-target='#vipCharge'  data-toggle='modal'><i class='fa fa-vimeo'></i></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a onclick='loadUserFundInfo(" + value + ");' title='资金充值' href='#' data-target='#fundCharge' data-toggle='modal'><i class='fa fa-money'></i></a>";
                }
            }
        ]],
        queryParams: function (params) {
            params.account = $("#account").val().trim();
            return params;
        }
    });
    ko.applyBindings(viewModel);
    formValidator();
});

function doUserSearch() {
    $('#userListTable').bootstrapTable("refresh");
}

function loadUserVipInfo(id) {
    $.ajax({
        url: "/adminUserMangeController.admin?doGetVipInfo",
        type: 'post',
        data: {sellerId: id},
        success: function (data) {
            if (data.success === "success") {
                viewModel.sellerId(data.content.sellerId);
                viewModel.account(data.content.account);
                viewModel.membershipEndTime(data.content.membershipEndTime);
                viewModel.memberShipMonth(1);
            } else if (data.success === "fail") {
                toastr.warning(data.msg);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin'
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.warning("服务器异常,请联系管理员！");
        }
    });
}

function loadUserFundInfo(id) {
    $.ajax({
        url: "/adminUserMangeController.admin?doGetFundInfo",
        type: 'post',
        data: {sellerId: id},
        success: function (data) {
            if (data.success === "success") {
                viewModel.sellerId(data.content.sellerId);
                viewModel.account(data.content.account);
                viewModel.totalFund(data.content.totalFund);
                viewModel.usableFund(data.content.usableFund);
                viewModel.freezeFund(data.content.freezeFund);
                viewModel.chargeFund("");
            } else if (data.success === "fail") {
                toastr.warning(data.msg);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin'
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.warning("服务器异常,请联系管理员！");
        }
    });
}

function formValidator() {
    $('#formObj_submitMemberShipMonth').bootstrapValidator({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            memberShipMonth: {
                validators: {
                    notEmpty: {
                        message: '请输入会员充值月份！'
                    },
                    regexp: {
                        regexp: /^[1-9]\d*$/,
                        message: '充值月份必须为正整数！'
                    }
                }
            }
        }
    }).on('success.form.bv', function (e) {
        var form = $('#formObj_submitMemberShipMonth');
        $.post(form.attr('action'), form.serialize(), function (result) {
            if (result.success === "success") {
                toastr.success(result.msg);
                $('#userListTable').bootstrapTable("refresh");
                $('#vipCharge').modal('hide');
                form.bootstrapValidator('disableSubmitButtons', false);
            } else if (result.success === "fail") {
                toastr.warning(result.msg);
                form.bootstrapValidator('disableSubmitButtons', false);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin';
            }
        }, 'json');
    });

    $('#formObj_submitFundCharge').bootstrapValidator({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            chargeFund: {
                validators: {
                    notEmpty: {
                        message: '请输入充值金额！'
                    },
                    regexp: {
                        regexp: /^[0-9]+([.]{1}[0-9]{1,2})?$/,
                        message: '金额至多保留两位小数的正数!'
                    }
                }
            }
        }
    }).on('success.form.bv', function (e) {
        var form = $('#formObj_submitFundCharge');
        $.post(form.attr('action'), form.serialize(), function (result) {
            if (result.success === "success") {
                toastr.success(result.msg);
                $('#userListTable').bootstrapTable("refresh");
                $('#fundCharge').modal('hide');
                form.bootstrapValidator('disableSubmitButtons', false);
            } else if (result.success === "fail") {
                toastr.warning(result.msg);
                form.bootstrapValidator('disableSubmitButtons', false);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin';
            }
        }, 'json');
    });


}

function submitMemberShipMonth() {
    $('#formObj_submitMemberShipMonth').submit();
    return false;
}

function submitFundCharge(){
    $('#formObj_submitFundCharge').submit();
    return false;
}

var viewModel = {
    sellerId: ko.observable(),
    account: ko.observable(),
    membershipEndTime: ko.observable(),
    memberShipMonth:ko.observable(),
    totalFund:ko.observable(),
    usableFund:ko.observable(),
    freezeFund:ko.observable(),
    chargeFund:ko.observable()
}
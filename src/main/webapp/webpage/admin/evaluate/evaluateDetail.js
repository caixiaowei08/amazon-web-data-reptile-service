/**
 * Created by User on 2017/6/23.
 */
$(function () {
    $('#createTime_begin').datetimepicker({
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        endDate: new Date(),
        forceParse: 0
    }).on('changeDate', function (ev) {
        var startTime = $("#createTime_begin_value").val();
        $("#createTime_end").datetimepicker('setStartDate', startTime);
        $('#createTime_begin').datetimepicker('hide');
    });

    $('#createTime_end').datetimepicker({
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        endDate: new Date(),
        forceParse: 0
    }).on('changeDate', function (ev) {
        var endTime = $("#createTime_end_value").val();
        $("#createTime_begin").datetimepicker('setEndDate', endTime);
        $('#createTime_end').datetimepicker('hide');
    });

    $('#formAddEvaluateUrlModel').bootstrapValidator({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            reviewUrl: {
                validators: {
                    notEmpty: {
                        message: '评价链接不能为空！'
                    }
                }
            }
        }
    }).on('success.form.bv', function (e) {
        var form = $('#formAddEvaluateUrlModel');
        $.post(form.attr('action'), form.serialize(), function (result) {
            if (result.success === "success") {
                toastr.success(result.msg);
                $('#addEvaluateUrlModel').modal('hide');
                $('#evaluateListTable').bootstrapTable("refresh");
            } else if (result.success === "fail") {
                toastr.warning(result.msg);
                $('#addEvaluateUrlModel').modal('hide');
                form.bootstrapValidator('disableSubmitButtons', false);
            } else {
                toastr.warning("请重新登录！");
                $('#addEvaluateUrlModel').modal('hide');
                setTimeout("window.location='/adminSystemController.admin?goAdminLogin'", 500);
            }
        }, 'json');
    });

    $('#formModifyAmOrderNoModel').bootstrapValidator({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            amzOrderId: {
                validators: {
                    notEmpty: {
                        message: '亚马逊单号不能为空！'
                    },
                    regexp: {
                        regexp: /^[\d-]*$/,
                        message: '亚马逊订单号只能是数字和-组合！'
                    }
                }
            }
        }
    }).on('success.form.bv', function (e) {
        var form = $('#formModifyAmOrderNoModel');
        $.post(form.attr('action'), form.serialize(), function (result) {
            if (result.success === "success") {
                toastr.success(result.msg);
                $('#modifyAmOrderNoModel').modal('hide');
                $('#evaluateListTable').bootstrapTable("refresh");
            } else if (result.success === "fail") {
                toastr.warning(result.msg);
                $('#modifyAmOrderNoModel').modal('hide');
            } else {
                toastr.warning("请重新登录！");
                $('#modifyAmOrderNoModel').modal('hide');
                setTimeout("window.location='/adminSystemController.admin?goAdminLogin'", 500);
            }
            form.bootstrapValidator('disableSubmitButtons', false);
        }, 'json');
    });


    $('#evaluateListTable').bootstrapTable({
        url: "/evaluateController.admin?dataGrid",
        sidePagination: "server",
        dataType: "json",
        search: false,
        pageNumber: 1,
        pageSize: 20,
        sortName: "createTime",
        sortOrder: 'desc',
        pageList: [10, 20, 30, 50, 100],
        pagination: true,
        height: 650,
        queryParams: function (params) {
            params.amzOrderId = $("#amazon_amzOrderId").val().trim();
            params.asinId = $("#amazon_asin").val().trim();
            params.state = $("#amazon_state").val().trim();
            params.promotId = $("#amazon_promoteId").val().trim();
            params.createTime_begin = $("#createTime_begin_value").val().trim();
            params.createTime_end = $("#createTime_end_value").val().trim();
            return params;
        },
        onLoadError: function () {
            toastr.warning("请重新登录！");
            setTimeout("window.location='/adminSystemController.admin?goAdminLogin'", 1000);
        },
        clickToSelect: true,//是否启用点击选中行
        uniqueId: "id",
        locale: "zh-CN",
        showColumns: false,
        singleSelect: true,
        idField: "id",
        columns: [[
            {
                title: "评论编号",
                field: "id",
                width: "8%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: "状态",
                field: "state",
                width: "7%",//宽度
                align: "center",//水平
                valign: "middle",
                formatter: function (value, row, index) {
                    if (value == 1) {
                        return "<span class='label label-warning'>pending</span>"
                    } else if (value == 2) {
                        return "<span class='label label-success'>review</span>"
                    }
                    return "";
                }
            },
            {
                title: 'ASIN',
                field: "asinId",
                width: "8%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '亚马逊订单号',
                field: "amzOrderId",
                width: "15%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '单号提交时间',
                field: "createTime",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    var html = ('<span>' + value.substr(0, 10) + '</span>');
                    return html;
                }
            },
            {
                title: '评价内容',
                field: "reviewContent",
                width: "20%",//宽度
                align: "center",//水平
                valign: "middle", //垂直
                formatter: function (value, row, index) {
                    if (value === undefined) {
                        return "-"
                    }
                    return "<a href='" + row.reviewUrl + "' target='_blank'>" + value + "</a>";
                }
            },
            {
                title: '评价星级',
                field: "reviewStar",
                sortable: true,
                width: "5%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '评价时间',
                field: "reviewDate",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '操作',
                field: "id",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    if (row.state === 1) {
                        return "<a onclick='clickModifyAmOrderNoModel(" + value + ");' title='修改亚马逊订单号' data-target='#modifyAmOrderNoModel' data-toggle='modal'><i class='fa fa-pencil-square'></i></a>&nbsp;&nbsp;<a onclick='clickEvaluateModel(" + value + ");' title='删除评价' data-target='#deleteEvaluateModel' data-toggle='modal'><i class='fa fa-window-close'></i></a>&nbsp;&nbsp;<a onclick='clickAddEvaluateUrlModel(" + row.id + ")'; title='追加评论链接' data-target='#addEvaluateUrlModel' data-toggle='modal'><i class='fa fa-plus-square'></i></a>";
                    } else if (row.state === 2) {
                        return "<a onclick='clickModifyAmOrderNoModel(" + value + ");' title='修改亚马逊订单号' data-target='#modifyAmOrderNoModel' data-toggle='modal'><i class='fa fa-pencil-square'></i></a>&nbsp;&nbsp;<a onclick='clickEvaluateModel(" + value + ");' title='删除评价' data-target='#deleteEvaluateModel' data-toggle='modal'><i class='fa fa-window-close'></i></a>";
                    } else {
                        return "";
                    }
                }
            }
        ]]
    });

    $("#btn_sub").click(function () {
        $('#formObj').submit();
    });

    $('#addNewReview').on('hide.bs.modal', function () {
        $("#asinId").val("");
        $("#amzOrderId").val("");
        $("#reviewUrl").val("");
        $("#formObj").data('bootstrapValidator').destroy();
        $('#formObj').data('bootstrapValidator', null);
        formValidator();
    })
    formValidator();
    ko.applyBindings(viewModel);
});

var viewModel = {
    deleteId: ko.observable(),
    eid: ko.observable(),
    asinId: ko.observable(),
    amzOrderId: ko.observable(),
    reviewUrl: ko.observable()
}

function clickEvaluateModel(id) {
    viewModel.deleteId(id);
}

function submitAddReviewUrl() {
    $('#formAddEvaluateUrlModel').submit();
}

function submitModifyAmOrderNo() {
    $('#formModifyAmOrderNoModel').submit();
}

function clickAddEvaluateUrlModel(id) {
    $.ajax({
        url: "/evaluateController.admin?doFindComment",
        type: 'post',
        data: {id: id},
        success: function (data) {
            if (data.success === "success") {
                viewModel.eid(data.content.id);
                viewModel.asinId(data.content.asinId);
                viewModel.amzOrderId(data.content.amzOrderId);
                viewModel.reviewUrl("");
            } else if (data.success === "fail") {
                toastr.warning(data.msg);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin';
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.success("服务器异常,请联系管理员！");
        }
    })
}

function clickModifyAmOrderNoModel(id) {
    $.ajax({
        url: "/evaluateController.admin?doFindComment",
        type: 'post',
        data: {id: id},
        success: function (data) {
            if (data.success === "success") {
                viewModel.eid(data.content.id);
                viewModel.asinId(data.content.asinId);
                viewModel.amzOrderId(data.content.amzOrderId);
                viewModel.reviewUrl(data.content.reviewUrl);
            } else if (data.success === "fail") {
                toastr.warning(data.msg);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin';
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.success("服务器异常,请联系管理员！");
        }
    })
}

function beforeSend() {
    $("#deleteEvaluateByIdBtn").addClass("disabled"); // Disables visually
    $("#deleteEvaluateByIdBtn").prop("disabled", true); // Disables visually + functionally
}

function SendComplete() {
    $("#deleteEvaluateByIdBtn").removeClass("disabled"); // Disables visually
    $("#deleteEvaluateByIdBtn").prop("disabled", false); // Disables visually + functionally
}

function deleteEvaluateById() {
    var deleteId = $("#deleteId").val();
    $.ajax({
        url: "/evaluateController.admin?doDel",
        type: 'post',
        beforeSend: beforeSend,
        data: {id: deleteId},
        success: function (data) {
            if (data.success === "success") {
                $('#evaluateListTable').bootstrapTable("refresh");
                toastr.success("关闭成功！");
            } else if (data.success === "fail") {
                toastr.warning(data.msg);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin';
            }
        },
        complete: function () {
            $('#deleteEvaluateModel').modal('hide');
            SendComplete();
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.success("服务器异常,请联系管理员！");
        }
    })
}

function formValidator() {
    $('#formObj').bootstrapValidator({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            asinId: {
                validators: {
                    notEmpty: {
                        message: '请输入ASIN编号!'
                    }
                }
            },
            amzOrderId: {
                validators: {
                    notEmpty: {
                        message: '请输入亚马逊订单号!'
                    },
                    regexp: {
                        regexp: /^[\d-]*$/,
                        message: '亚马逊订单号只能是数字和-组合！'
                    }

                }
            }
        }
    });
}

function downEvaluateSearch() {
    $('#evaluateListTable').bootstrapTable("refresh");
}

function beforeSend() {
    $("#btn_sub").addClass("disabled"); // Disables visually
    $("#btn_sub").prop("disabled", true); // Disables visually + functionally
}

function SendComplete() {
    $("#btn_sub").removeClass("disabled"); // Disables visually
    $("#btn_sub").prop("disabled", false); // Disables visually + functionally
}

function doSubmitEvaluate() {
    var asinId = $("#asinId").val();
    var amzOrderId = $("#amzOrderId").val();
    var reviewUrl = $("#reviewUrl").val();
    if (
        asinId === null || asinId === undefined || asinId === '' ||
        amzOrderId === null || amzOrderId === undefined || amzOrderId === ''
    ) {
        return;
    }
    $.ajax({
        url: "/evaluateController.admin?doAdd",
        type: 'post',
        beforeSend: beforeSend,
        data: $('#formObj').serialize(),
        success: function (data) {
            if (data.success === "success") {
                toastr.success(data.msg);
                $("#asinId").val("");
                $("#amzOrderId").val("");
                $("#reviewUrl").val("");
                $("#formObj").data('bootstrapValidator').destroy();
                $('#formObj').data('bootstrapValidator', null);
                formValidator();
            } else if (data.success === "fail") {
                toastr.warning(data.msg);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin'
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.success("服务器异常,请联系管理员！");
        },
        complete: function () {
            $('#evaluateListTable').bootstrapTable("refresh");
            SendComplete();
        }
    });
}

function downEvaluateExcel() {
    var params = new Object();
    params.amzOrderId = $("#amazon_amzOrderId").val().trim();
    params.asinId = $("#amazon_asin").val().trim();
    params.state = $("#amazon_state").val().trim();
    params.createTime_begin = $("#createTime_begin_value").val().trim();
    params.createTime_end = $("#createTime_end_value").val().trim();
    if ((params.createTime_end === "") ^ (params.createTime_begin === "")) {
        toastr.warning("若填写查询时间，开始时间和结束时间需要同时填写！");
        return;
    }
    $.ajax({
        url: "/adminSystemController.admin?doCheckLogin",
        type: 'post',
        success: function (data) {
            if (data.success === "success") {
                window.open(
                    "/evaluateController.admin?downEvaluateExcel&asinId=" + params.asinId
                    + "&state=" + params.state
                    + "&amzOrderId=" + params.amzOrderId
                    + "&createTime_begin=" + params.createTime_begin
                    + "&createTime_end=" + params.createTime_end
                )
            } else if (data.success === "fail") {
                toastr.warning(data.msg);
                return;
            } else {
                setTimeout("window.location='/adminSystemController.admin?goAdminLogin'", 1000);
                return;
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.success("服务器异常,请联系管理员！");
        }
    });
}






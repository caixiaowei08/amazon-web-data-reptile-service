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
                        regexp: /^[0-9]{3}[-]{1}[0-9]{7}[-]{1}[0-9]{7}$/,
                        message: '由数字和-组合成，必须满足格式xxx-xxxxxxx-xxxxxxx！'
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
        onLoadSuccess: function () {
            $('.rating').rating('create');
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
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    if (value === undefined) {
                        return "-"
                    }
                    return "<input value='" + value + "' type='text' class='rating' data-min=0 data-max=5 data-step=0.1 data-size='xs' title='评价星级'>";
                }
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

    $("#addEvaluateUrlBtn").click(function () {
        $('#formAddEvaluateUrlModel').submit();
    });

    formValidator();
    formAddEvaluateUrlModelValidator();
    ko.applyBindings(viewModel);
});

var viewModel = {
    deleteId: ko.observable(),
    eid: ko.observable(),
    asinId: ko.observable(),
    state: ko.observable(),
    amzOrderId: ko.observable(),
    reviewUrl: ko.observable()
}

function clickEvaluateModel(id) {
    viewModel.deleteId(id);
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
                viewModel.state(data.content.state==2);
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
                        regexp: /^[0-9]{3}[-]{1}[0-9]{7}[-]{1}[0-9]{7}$/,
                        message: '由数字和-组合成，必须满足格式xxx-xxxxxxx-xxxxxxx！'
                    }
                }
            },
            weChat: {
                validators: {
                    regexp: {
                        regexp: /^[0-9]+([.]{1}[0-9]{1,2})?$/,
                        message: '金额至多保留两位小数的正数!'
                    }
                }
            },
            zfb: {
                validators: {
                    regexp: {
                        regexp: /^[0-9]+([.]{1}[0-9]{1,2})?$/,
                        message: '金额至多保留两位小数的正数!'
                    }
                }
            },
            payPal: {
                validators: {
                    regexp: {
                        regexp: /^[0-9]+([.]{1}[0-9]{1,2})?$/,
                        message: '金额至多保留两位小数的正数!'
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
    if (
        asinId === null || asinId === undefined || asinId === '' ||
        amzOrderId === null || amzOrderId === undefined || amzOrderId === ''
    ) {
        return;
    }
    var reviewUrl = $("#reviewUrl").val();
    if (!(reviewUrl === null || reviewUrl === undefined || reviewUrl === '')) {
        var weChat = $("#weChat").val();
        var zfb = $("#zfb").val();
        var payPal = $("#payPal").val();
        if (
            (weChat === null || weChat === undefined || weChat === '') &&
            (zfb === null || zfb === undefined || zfb === '') &&
            (payPal === null || payPal === undefined || payPal === '')
        ) {
            toastr.info("请填写微信、支付宝、PayPal其中之一的金额！");
            return;
        }
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
                $("#weChat").val("");
                $("#zfb").val("");
                $("#payPal").val("");
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
    params.promotId = $("#amazon_promoteId").val().trim();
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
                    + "&promotId=" + params.promotId
                    + "&amzOrderId=" + params.amzOrderId
                    + "&createTime_begin=" + params.createTime_begin
                    + "&createTime_end=" + params.createTime_end
                )
            } else if (data.success === "fail") {
                toastr.error(data.msg);
                return;
            } else {
                window.location='/adminSystemController.admin?goAdminLogin';
                return;
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.success("服务器异常,请联系管理员！");
        }
    });
}

function doSubmitEvaluate() {
    var asinId = $("#asinId").val();
    var amzOrderId = $("#amzOrderId").val();
    if (
        asinId === null || asinId === undefined || asinId === '' ||
        amzOrderId === null || amzOrderId === undefined || amzOrderId === ''
    ) {
        return;
    }
    var reviewUrl = $("#reviewUrl").val();
    if (!(reviewUrl === null || reviewUrl === undefined || reviewUrl === '')) {
        var weChat = $("#weChat").val();
        var zfb = $("#zfb").val();
        var payPal = $("#payPal").val();
        if (
            (weChat === null || weChat === undefined || weChat === '') &&
            (zfb === null || zfb === undefined || zfb === '') &&
            (payPal === null || payPal === undefined || payPal === '')
        ) {
            toastr.info("请填写微信、支付宝、PayPal其中之一的金额！");
            return;
        }
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
                $("#weChat").val("");
                $("#zfb").val("");
                $("#payPal").val("");
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

function formAddEvaluateUrlModelValidator() {
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
            },
            weChat: {
                validators: {
                    regexp: {
                        regexp: /^[0-9]+([.]{1}[0-9]{1,2})?$/,
                        message: '金额至多保留两位小数的正数!'
                    }
                }
            },
            zfb: {
                validators: {
                    regexp: {
                        regexp: /^[0-9]+([.]{1}[0-9]{1,2})?$/,
                        message: '金额至多保留两位小数的正数!'
                    }
                }
            },
            payPal: {
                validators: {
                    regexp: {
                        regexp: /^[0-9]+([.]{1}[0-9]{1,2})?$/,
                        message: '金额至多保留两位小数的正数!'
                    }
                }
            }
        }
    })
}

function submitAddReviewUrl() {
    console.log("submitAddReviewUrl");
    var reviewUrl = $("#addReviewUrl").val();
    if (reviewUrl === null || reviewUrl === undefined || reviewUrl === '') {
        return;
    }
    var weChat = $("#weChat-url").val();
    var zfb = $("#zfb-url").val();
    var payPal = $("#payPal-url").val();
    if (
        (weChat === null || weChat === undefined || weChat === '') &&
        (zfb === null || zfb === undefined || zfb === '') &&
        (payPal === null || payPal === undefined || payPal === '')
    ) {
        toastr.info("请填写微信、支付宝、PayPal其中之一的金额！");
        return;
    }

    $.ajax({
        url: "/evaluateController.admin?doAddReviewUrl",
        type: 'post',
        beforeSend: beforeSendAddEvaluateUrlBtn,
        data: $('#formAddEvaluateUrlModel').serialize(),
        success: function (data) {
            if (data.success === "success") {
                toastr.success(data.msg);
                $('#addEvaluateUrlModel').modal('hide');
                $("#weChat-url").val("");
                $("#zfb-url").val("");
                $("#payPal-url").val("");
            } else if (data.success === "fail") {
                toastr.error(data.msg);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin'
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.success("服务器异常,请联系管理员！");
        },
        complete: function () {
            $('#evaluateListTable').bootstrapTable("refresh");
            $("#formAddEvaluateUrlModel").data('bootstrapValidator').destroy();
            $('#formAddEvaluateUrlModel').data('bootstrapValidator', null);
            formAddEvaluateUrlModelValidator();
            sendCompleteAddEvaluateUrlBtn();
        }
    });
}

function beforeSendAddEvaluateUrlBtn() {
    $("#addEvaluateUrlBtn").addClass("disabled"); // Disables visually
    $("#addEvaluateUrlBtn").prop("disabled", true); // Disables visually + functionally
}

function sendCompleteAddEvaluateUrlBtn() {
    $("#addEvaluateUrlBtn").removeClass("disabled"); // Disables visually
    $("#addEvaluateUrlBtn").prop("disabled", false); // Disables visually + functionally
}
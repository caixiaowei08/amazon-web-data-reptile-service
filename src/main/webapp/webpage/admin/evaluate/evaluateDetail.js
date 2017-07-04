/**
 * Created by User on 2017/6/23.
 */
$(function () {
    $('#evaluateListTable').bootstrapTable({
        url: "/evaluateController.admin?dataGrid",
        sidePagination: "server",
        dataType: "json",
        search: false,
        pageNumber: 1,
        pageSize: 20,
        sortName: "updateTime",
        sortOrder: 'desc',
        pageList: [10, 20, 30, 50, 100],
        pagination: true,
        height: 650,
        onLoadError:function () {
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
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: "状态",
                field: "state",
                width: "10%",//宽度
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
                title: '亚马逊订单号',
                field: "amzOrderId",
                width: "20%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '评价内容',
                field: "reviewContent",
                width: "30%",//宽度
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
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '评价时间',
                field: "reviewDate",
                width: "20%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
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
});

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
                        message: '请输入亚马逊订单ID!'
                    }
                }
            }
        }
    });
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
            } else if (data.success ===  "fail") {
                toastr.warning(data.msg);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin'
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.success("服务器异常,请联系管理员！");
        },
        complete: function () {
            $('#addNewReview').modal('hide');
            $('#evaluateListTable').bootstrapTable("refresh");
            SendComplete();
        }
    });
}






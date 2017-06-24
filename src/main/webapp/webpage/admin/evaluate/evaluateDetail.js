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
        pageList: [10, 20, 30, 50, 100],
        pagination: true,
        height: tableHeight(),
        clickToSelect: true,//是否启用点击选中行
        uniqueId: "id",
        locale: "zh-CN",
        showColumns: false,
        singleSelect: true,
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
                valign: "middle"
            },
            {
                title: '亚马逊订单号',
                field: "amzOrderId",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '评价内容',
                field: "reviewContent",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
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
                title: '举报',
                field: "id",
                width: "10%",//宽度
                formatter: function (value, row, index) {
                    if (row.state === 1) {//开启状态
                        return "<a onclick='loadPromotOrder(" + value + ")' title='查看详情' data-target='#orderDetailModal'style='cursor:pointer;' data-toggle='modal'><i class='fa fa-search'></i></a>&nbsp;&nbsp;&nbsp;&nbsp;" +
                                "<a onclick='clickEvaluateModel("+value+");' title='评论管理' style='cursor:pointer;'><i class='fa fa-hand-o-right'></i></a>"
                    } else if (row.state === 2) { //关闭状态
                        return "<a onclick='loadPromotOrder(" + value + ")'title='查看详情' data-target='#orderDetailModal'style='cursor:pointer;' data-toggle='modal'><i class='fa fa-search'></i></a>"
                    } else {
                        return "";
                    }
                }
            }
        ]]
    });
    $(window).resize(function () {
        $('#evaluateListTable').bootstrapTable('resetView', {
            height: tableHeight()
        })
    })
    function tableHeight() {
        return $(window).height() - 300;
    }

    $("#btn_sub").click(function () {
        console.log("btn_sub");
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
    var dayReviewNum = $("#dayReviewNum").val();
    var needReviewNum = $("#needReviewNum").val();
    var finishDate = $("#finishDate").val();
    if (
        dayReviewNum === null || dayReviewNum === undefined || dayReviewNum === ''||
        needReviewNum === null || needReviewNum === undefined || needReviewNum === ''||
        finishDate === null || finishDate === undefined || finishDate === ''
    ) {
        return;
    }

    $.ajax({
        url:"/promotOrderController.admin?doAdd",
        type:'post',
        beforeSend:beforeSend,
        data:$('#formObj').serialize(),
        success:function(data){
            if(data.success === "success"){
                toastr.success(data.msg);
            }else{
                toastr.error(data.msg);
            }
        },
        error:function(jqxhr,textStatus,errorThrow){
            toastr.success("服务器异常,请联系管理员！");
        },
        complete:function () {
            $('#addNewReview').modal('hide');
            SendComplete();
        },
        statusCode:{
            404:function(){console.log('not found');},
            500:function(){console.log('error by server');},
        }
    });



}






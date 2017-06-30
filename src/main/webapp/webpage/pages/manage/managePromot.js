/**
 * Created by User on 2017/6/18.
 */
$(function () {
    $('#addDate_begin').datetimepicker({
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        endDate: new Date(),
        forceParse: 0
    }).on('changeDate', function (ev) {
        var startTime = $("#addDate_begin_value").val();
        $("#addDate_end").datetimepicker('setStartDate', startTime);
        $('#addDate_begin').datetimepicker('hide');
    });

    $('#addDate_end').datetimepicker({
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        endDate: new Date(),
        forceParse: 0
    }).on('changeDate', function (ev) {
        var endTime = $("#addDate_end_value").val();
        $("#addDate_begin").datetimepicker('setEndDate', endTime);
        $('#addDate_end').datetimepicker('hide');
    });

    $('#promotListTable').bootstrapTable({
        url: "/promotOrderController.do?dataGrid",
        sidePagination: "server",
        dataType: "json",
        search: false,
        sortName: "updateTime",
        sortOrder: 'desc',
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
                title: '订单编号',
                field: "id",
                sortable: true,
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: 'ASIN',
                field: "asinId",
                sortable: true,
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    return "<a href='" + row.productUrl + "' target='_blank'>" + value + "</a>";
                }
            },
            {
                title: '店铺名称',
                field: "brand",
                sortable: true,
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '状态',
                field: "state",
                sortable: true,
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    if (value == 1) {
                        return "<span class='label label-success'>开启</span>"
                    } else if (value == 2) {
                        return "<span class='label label-default'>关闭</span>"
                    }
                    return "";
                }
            },
            {
                title: '联系买家',
                field: "buyerNum",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '获得评论',
                field: "evaluateNum",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    return "<a href='/redirectionController.do?goToEvaluateDetail&promotId=" + row.id + "' target='_parent' title='查看评价详情'>"+value+"</a>";
                }
            },
            {
                title: '费用(美元)',
                field: "consumption",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '创建时间',
                field: "addDate",
                sortable: true,
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '结束日期',
                field: "finishDate",
                sortable: true,
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '操作',
                field: "id",
                width: "10%",//宽度
                formatter: function (value, row, index) {
                    if (row.state === 1) {//开启状态
                        return "<a onclick='loadPromotOrder(" + value + ")' title='查看详情' data-target='#orderDetailModal' data-toggle='modal'><i class='fa fa-search'></i></a>&nbsp;&nbsp;<a onclick='clickDeleteModel("+value+");' title='关闭推广' data-target='#deleteOrderModel' data-toggle='modal'><i class='fa fa-window-close'></i></a>";
                    } else if (row.state === 2) { //关闭状态
                        return "<a onclick='loadPromotOrder(" + value + ")'title='查看详情' data-target='#orderDetailModal' data-toggle='modal'><i class='fa fa-search'></i></a>";
                    } else {
                        return "";
                    }
                }
            }
        ]],
        queryParams: function (params) {
            params.asinId = $("#amazon_asin").val().trim();
            params.state = $("#amazon_state").val().trim();
            params.addDate_begin = $("#addDate_begin_value").val().trim();
            params.addDate_end = $("#addDate_end_value").val().trim();
            return params;
        }
    });
    $(window).resize(function () {
        $('#promotListTable').bootstrapTable('resetView', {
            height: tableHeight()
        })
    })
    function tableHeight() {
        return $(window).height() - 350;
    }
})

function doPromotSearch() {
    var addDate_begin = $("#addDate_begin_value").val().trim();
    var addDate_end = $("#addDate_end_value").val().trim();
    if ((addDate_end === "") ^ (addDate_begin === "")) {
        toastr.warning("若填写查询时间，开始时间和结束时间需要同时填写！");
        return;
    }
    $('#promotListTable').bootstrapTable("refresh");
}

var viewModel = {
    id:ko.observable(),
    asinId: ko.observable(),
    productUrl: ko.observable(),
    productTitle: ko.observable(),
    brand: ko.observable(),
    landingImage: ko.observable(),
    salePrice: ko.observable(),
    state: ko.observable(),
    addDate: ko.observable(),
    finishDate: ko.observable(),
    guaranteeFund: ko.observable(),
    consumption: ko.observable(),
    needReviewNum: ko.observable(),
    dayReviewNum: ko.observable(),
    buyerNum: ko.observable(),
    evaluateNum: ko.observable(),
    reviewPrice: ko.observable()
};

ko.applyBindings(viewModel);

function loadPromotOrder(promotId) {
    $.ajax({
        url: "/promotOrderController.do?doGet",
        type: 'post',
        data: {id: promotId},
        success: function (data) {
            if (data.success === "success") {
                    viewModel.id(data.content.id);
                    viewModel.asinId(data.content.asinId);
                    viewModel.productUrl(data.content.productUrl);
                    viewModel.productTitle(data.content.productTitle);
                    viewModel.brand(data.content.brand);
                    viewModel.landingImage(data.content.thumbnail);
                    viewModel.salePrice(data.content.salePrice);
                    viewModel.state(data.content.state);
                    viewModel.addDate(data.content.addDate);
                    viewModel.finishDate(data.content.finishDate);
                    viewModel.guaranteeFund(data.content.guaranteeFund);
                    viewModel.consumption(data.content.consumption);
                    viewModel.needReviewNum(data.content.needReviewNum);
                    viewModel.dayReviewNum(data.content.dayReviewNum);
                    viewModel.buyerNum(data.content.buyerNum);
                    viewModel.evaluateNum(data.content.evaluateNum);
                    viewModel.reviewPrice(data.content.reviewPrice);
            } else {
                window.location = '/loginController.do?login'
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.success("服务器异常,请联系管理员！");
        },
        statusCode: {
            404: function () {
                console.log('not found');
            },
            500: function () {
                console.log('error by server');
            },
        }
    });
}

function clickDeleteModel(id){
    viewModel.id(id);
}


function beforeSend() {
    $("#deleteOrderByIdBtn").addClass("disabled"); // Disables visually
    $("#deleteOrderByIdBtn").prop("disabled", true); // Disables visually + functionally
}

function SendComplete() {
    $("#deleteOrderByIdBtn").removeClass("disabled"); // Disables visually
    $("#deleteOrderByIdBtn").prop("disabled", false); // Disables visually + functionally
}

function deleteOrderById(){
    var promotId = $("#deleteId").val();
    $.ajax({
        url: "/promotOrderController.do?doCloseById",
        type: 'post',
        beforeSend:beforeSend,
        data: {id: promotId},
        success: function (data) {
            if(data.success==="success"){
                $('#promotListTable').bootstrapTable("refresh");
                toastr.success("关闭成功！");
            }else{
                toastr.warning("关闭失败！");
            }
        },
        complete:function () {
            $('#deleteOrderModel').modal('hide');
            SendComplete();
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.success("服务器异常,请联系管理员！");
        }
    })
}
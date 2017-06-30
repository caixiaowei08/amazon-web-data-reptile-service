/**
 * Created by User on 2017/6/22.
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
        url: "/adminPromotController.admin?dataGrid",
        sidePagination: "server",
        dataType: "json",
        search: false,
        pageNumber: 1,
        pageSize: 20,
        sortName: "updateTime",
        sortOrder: 'desc',
        pageList: [10, 20, 30, 50, 100],
        pagination: true,
        height: tableHeight(),
        clickToSelect: true,
        uniqueId: "id",
        locale: "zh-CN",
        showColumns: false,
        singleSelect: true,
        columns: [[
            {
                title: '订单编号',
                field: "id",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: 'ASIN',
                field: "asinId",
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
                sortable: true,
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '获得评论',
                field: "evaluateNum",
                sortable: true,
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '费用(美元)',
                field: "consumption",
                sortable: true,
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
                        return "<a onclick='loadPromotOrder(" + value + ")' title='查看详情' data-target='#orderDetailModal'style='cursor:pointer;' data-toggle='modal'><i class='fa fa-search'></i></a>";
                }
            }
        ]],
        queryParams: function (params) {
            params.id = $("#amazon_id").val().trim();
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
    ko.applyBindings(viewModel);
});

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


function loadPromotOrder(promotId) {
    $.ajax({
        url: "/adminPromotController.admin?doGet",
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

/*function clickEvaluateModel(promotId) {
    window.open("/evaluateController.admin?goEvaluateDetail&promotId="+promotId);
}*/
function downPromotOrderExcel(){
    var params = new Object();
    params.id = $("#amazon_id").val().trim();
    params.asinId = $("#amazon_asin").val().trim();
    params.state = $("#amazon_state").val().trim();
    params.addDate_begin = $("#addDate_begin_value").val().trim();
    params.addDate_end = $("#addDate_end_value").val().trim();

    if ((params.addDate_end === "") ^ (params.addDate_begin === "")) {
        toastr.warning("若填写查询时间，开始时间和结束时间需要同时填写！");
        return;
    }
    var json = JSON.stringify(params);

    window.open(
        "adminPromotController.admin?downPromotOrderExcel&id="
        +params.id+"&asinId=" +params.asinId
        +"&state=" +params.state
        +"&addDate_begin=" +params.addDate_begin
        +"&addDate_end=" +params.addDate_end
    )
}


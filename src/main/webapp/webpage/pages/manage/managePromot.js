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

    $('#formUpdatePromotModel').bootstrapValidator({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            sequence: {
                validators: {
                    between: {
                        min: 1,
                        max: 20,
                        message: '请输入大于1小于20的数字！'
                    }
                }
            }
        }
    }).on('success.form.bv', function (e) {
        var form = $('#formUpdatePromotModel');
        $.post(form.attr('action'), form.serialize(), function (result) {
            if (result.success === "success") {
                toastr.success(result.msg);
                $('#updatePromotModel').modal('hide');
                $('#promotListTable').bootstrapTable("refresh");
            } else if (result.success === "fail") {
                toastr.warning(result.msg);
                $('#updatePromotModel').modal('hide');
            } else {
                toastr.warning("请重新登录！");
                $('#updatePromotModel').modal('hide');
                setTimeout("window.location='/adminSystemController.admin?goAdminLogin'", 500);
            }
            viewModel.keyword("");
            viewModel.sequence("");
        }, 'json');
    });

    $('#promotListTable').bootstrapTable({
        url: "/promotOrderController.do?dataGrid",
        sidePagination: "server",
        dataType: "json",
        search: false,
        sortName: "addDate",
        sortOrder: 'desc',
        pageNumber: 1,
        pageSize: 10,
        pageList: [10, 20, 30, 50, 100],
        pagination: true,
        onLoadError:function () {
            toastr.warning("请重新登录！");
            setTimeout("window.location='/loginController.do?login'", 1000);
        },
        height: 700,
        clickToSelect: true,//是否启用点击选中行
        uniqueId: "id",
        locale: "zh-CN",
        showColumns: false,
        singleSelect: true,
        columns: [[
            {
                title: '订单编号',
                field: "id",
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: 'ASIN',
                field: "asinId",
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    return "<a href='" + row.productUrl + "' target='_blank'>" + value + "</a>";
                }
            },
            {
                title: '店铺名称',
                field: "brand",
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    if(value===undefined||value===""){
                        return "-";
                    }else{
                        return '<input type="text" style="border:none;background:none;"  size="10" readonly="readonly" value="' + value + '"/>';
                    }
                }
            },
            {
                title: '状态',
                field: "state",
                sortable: true,
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
                title: '目标评论',
                field: "needReviewNum",
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '每日评论',
                field: "dayReviewNum",
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '已下订单',
                field: "buyerNum",
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    return "<a href='/redirectionController.do?goToEvaluateDetail&promotId=" + row.id + "' target='_parent' title='查看评价详情'>" + value + "</a>";
                }
            },
            {
                title: '获得评论',
                field: "evaluateNum",
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    return "<a href='/redirectionController.do?goToEvaluateDetail&promotId=" + row.id + "' target='_parent' title='查看评价详情'>" + value + "</a>";
                }
            },
            {
                title: '费用(美元)',
                field: "consumption",
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '创建时间',
                field: "addDate",
                sortable: true,
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '结束日期',
                field: "finishDate",
                sortable: true,
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '操作',
                field: "id",
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    return "<a onclick='loadPromotOrder(" + value + ")'title='查看详情' data-target='#orderDetailModal' data-toggle='modal'><i class='fa fa-search'></i></a>&nbsp;&nbsp;<a onclick='loadPromotOrder(" + value + ");' title='修改推广' data-target='#updatePromotModel' data-toggle='modal'><i class='fa fa-pencil-square'></i></a>";
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

function submitUpdatePromot(){
    $('#formUpdatePromotModel').submit();
}

var viewModel = {
    id: ko.observable(),
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
    cashBackConsumption: ko.observable(),
    needReviewNum: ko.observable(),
    dayReviewNum: ko.observable(),
    buyerNum: ko.observable(),
    evaluateNum: ko.observable(),
    cashback: ko.observable(),
    keyword: ko.observable(),
    sequence: ko.observable(),
    reviewPrice: ko.observable(),
    remark: ko.observable()
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
                viewModel.cashBackConsumption(data.content.cashBackConsumption);
                viewModel.needReviewNum(data.content.needReviewNum);
                viewModel.dayReviewNum(data.content.dayReviewNum);
                viewModel.buyerNum(data.content.buyerNum);
                viewModel.evaluateNum(data.content.evaluateNum);
                viewModel.cashback(data.content.cashback);
                viewModel.reviewPrice(data.content.reviewPrice);
                viewModel.keyword(data.content.keyword);
                viewModel.sequence(data.content.sequence);
                viewModel.remark(data.content.remark);
            } else if (data.success === "fail") {
                toastr.warning(data.msg);
            } else {
                window.location = '/loginController.do?login';
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.success("服务器异常,请联系管理员！");
        }
    });
}

function downEvaluateExcel() {
    var params = new Object();
    params.asinId = $("#amazon_asin").val().trim();
    params.state = $("#amazon_state").val().trim();
    params.addDate_begin = $("#addDate_begin_value").val().trim();
    params.addDate_end = $("#addDate_end_value").val().trim();
    if ((params.addDate_end === "") ^ (params.addDate_begin === "")) {
        toastr.warning("若填写查询时间，开始时间和结束时间需要同时填写！");
        return;
    }
    $.ajax({
        url: "/userController.do?doCheckLogin",
        type: 'post',
        success: function (data) {
            if (data.success === "success") {
                window.open(
                    "/promotOrderController.do?downEvaluateExcel&asinId=" + params.asinId
                    + "&state=" + params.state
                    + "&addDate_begin=" + params.addDate_begin
                    + "&addDate_end=" + params.addDate_end
                )
            } else if (data.success === "fail") {
                toastr.warning(data.msg);
                setTimeout("window.location='/loginController.do?login'", 1000);
                return;
            } else {
                window.location = '/loginController.do?login';
                return;
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.success("服务器异常,请联系管理员！");
        }
    });
}

function downPromotOrderExcel() {
    var params = new Object();
    params.asinId = $("#amazon_asin").val().trim();
    params.state = $("#amazon_state").val().trim();
    params.addDate_begin = $("#addDate_begin_value").val().trim();
    params.addDate_end = $("#addDate_end_value").val().trim();
    if ((params.addDate_end === "") ^ (params.addDate_begin === "")) {
        toastr.warning("若填写查询时间，开始时间和结束时间需要同时填写！");
        return;
    }
    $.ajax({
        url: "/userController.do?doCheckLogin",
        type: 'post',
        success: function (data) {
            if (data.success === "success") {
                window.open(
                    "/promotOrderController.do?downPromotOrderExcel&asinId=" + params.asinId
                    + "&state=" + params.state
                    + "&addDate_begin=" + params.addDate_begin
                    + "&addDate_end=" + params.addDate_end
                )
            } else if (data.success === "fail") {
                toastr.warning(data.msg);
                setTimeout("window.location='/loginController.do?login'", 1000);
                return;
            } else {
                window.location = '/loginController.do?login';
                return;
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.success("服务器异常,请联系管理员！");
        }
    });
}





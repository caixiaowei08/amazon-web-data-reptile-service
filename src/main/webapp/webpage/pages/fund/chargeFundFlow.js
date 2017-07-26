/**
 * Created by User on 2017/6/29.
 */
$(function () {
    $('#startTime_begin').datetimepicker({
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        endDate: new Date(),
        forceParse: 0
    }).on('changeDate', function (ev) {
        var startTime = $("#startTime_begin_value").val();
        $("#startTime_end").datetimepicker('setStartDate', startTime);
        $('#startTime_begin').datetimepicker('hide');
    });

    $('#startTime_end').datetimepicker({
        weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        endDate: new Date(),
        forceParse: 0
    }).on('changeDate', function (ev) {
        var endTime = $("#startTime_end_value").val();
        $("#startTime_begin").datetimepicker('setEndDate', endTime);
        $('#startTime_end').datetimepicker('hide');
    });

    $('#chargeFundFlowListTable').bootstrapTable({
        url: "/userRechargeFundController.do?dataGrid",
        sidePagination: "server",
        dataType: "json",
        sortName: "createTime",
        sortOrder: 'desc',
        search: false,
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 50, 100],
        pagination: true,
        height: 700,
        onLoadError:function () {
            toastr.warning("请重新登录！");
            setTimeout("window.location='/loginController.do?login'", 1000);
        },
        clickToSelect: true,//是否启用点击选中行
        uniqueId: "id",
        locale: "zh-CN",
        showColumns: false,
        singleSelect: true,
        columns: [[
            {
                title: '平台充值流水号',
                field: "platformOrderNum",
                width: "8%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '充值类型',
                field: "chargeType",
                width: "8%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    if (value == 1) {
                        return "<span class='label label-primary'>余额充值</span>"
                    } else if (value == 2) {
                        return "<span class='label label-danger'>购买会员</span>"
                    }
                    return "";
                }
            },
            {
                title: '金额($)',
                field: "chargeFund",
                width: "7%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
            },
            {
                title: '金额(￥)',
                field: "chargeFundRmb",
                width: "7%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
            },
            {
                title: '会员月份',
                field: "memberShipMonth",
                width: "7%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '支付方式',
                field: "chargeSource",
                sortable: true,
                width: "7%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    if (value == 1) {
                        return "<span class='label label-info'>支付宝</span>"
                    } else if (value == 2) {
                        return "<span class='label label-warning'>微信</span>"
                    }
                    return "";
                }
            },
            {
                title: '状态',
                field: "state",
                sortable: true,
                width: "8%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    if (value == 1) {
                        return "<span class='label label-default'>未支付</span>"
                    } else if (value == 2) {
                        return "<span class='label label-success'>成功</span>"
                    } else if (value == 3) {
                        return "<span class='label label-warning'>失败</span>"
                    }
                    return "";
                }
            },
            {
                title: 'ASIN',
                field: "asinId",
                width: "8%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
            },
            {
                title: '店铺',
                field: "brand",
                width: "7%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    if(value===undefined||value===""){
                        return "-";
                    }else{
                        return '<input type="text" style="border:none;background:none;"  size="8" readonly="readonly" value="' + value + '"/>';
                    }
                }
            },
            {
                title: '备注',
                field: "remark",
                width: "8%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
            },
            {
                title: '创建时间',
                field: "createTime",
                sortable: true,
                width: "15%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            }

        ]],
        queryParams: function (params) {
            params.chargeType = $("#amazon_chargeType").val().trim();
            params.chargeSource = $("#amazon_chargeSource").val().trim();
            params.state = $("#amazon_state").val().trim();
            params.startTime_begin = $("#startTime_begin_value").val().trim();
            params.startTime_end = $("#startTime_end_value").val().trim();
            return params;
        }
    });
});

function doChargeFundFlowSearch() {
    var addDate_begin = $("#startTime_begin_value").val().trim();
    var addDate_end = $("#startTime_end_value").val().trim();
    if ((addDate_end === "") ^ (addDate_begin === "")) {
        toastr.warning("若填写查询时间，开始时间和结束时间需要同时填写！");
        return;
    }
    $('#chargeFundFlowListTable').bootstrapTable("refresh");
}

function downChargeFundFlowExcel() {
    var params = new Object();
    params.chargeType = $("#amazon_chargeType").val().trim();
    params.chargeSource = $("#amazon_chargeSource").val().trim();
    params.state = $("#amazon_state").val().trim();
    params.startTime_begin = $("#startTime_begin_value").val().trim();
    params.startTime_end = $("#startTime_end_value").val().trim();
    if ((params.startTime_begin === "") ^ (params.startTime_begin === "")) {
        toastr.warning("若填写查询时间，开始时间和结束时间需要同时填写！");
        return;
    }
    $.ajax({
        url: "/userController.do?doCheckLogin",
        type: 'post',
        success: function (data) {
            if (data.success === "success") {
                window.open(
                    "/userRechargeFundController.do?downChargeFundFlowExcel&chargeType=" + params.chargeType
                    + "&chargeSource=" + params.chargeSource
                    + "&state=" + params.state
                    + "&startTime_begin=" + params.startTime_begin
                    + "&startTime_end=" + params.startTime_end
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
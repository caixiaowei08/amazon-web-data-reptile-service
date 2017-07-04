/**
 * Created by User on 2017/6/29.
 */
$(function () {
    $('#chargeFundFlowListTable').bootstrapTable({
        url: "/userRechargeFundController.do?dataGrid",
        sidePagination: "server",
        dataType: "json",
        sortName: "updateTime",
        sortOrder: 'desc',
        search: false,
        pageNumber: 1,
        pageSize: 20,
        pageList: [10, 20, 30, 50, 100],
        pagination: true,
        height: 700,
        clickToSelect: true,//是否启用点击选中行
        uniqueId: "id",
        locale: "zh-CN",
        showColumns: false,
        singleSelect: true,
        columns: [[
            {
                title: '平台充值流水号',
                field: "platformOrderNum",
                width: "10%",//宽度
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
                title: '充值金额($)',
                field: "chargeFund",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
            },
            {
                title: '充值金额(￥)',
                field: "chargeFundRmb",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
            },
            {
                title: '购买会员月份',
                field: "memberShipMonth",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '支付方式',
                field: "chargeSource",
                sortable: true,
                width: "10%",//宽度
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
                width: "10%",//宽度
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
                title: '时间',
                field: "createTime",
                sortable: true,
                width: "15%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            }

        ]],
        queryParams: function (params) {
            return params;
        }
    });
});
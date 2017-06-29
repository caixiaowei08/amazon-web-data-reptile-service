/**
 * Created by User on 2017/6/29.
 */
$(function () {
    $('#evaluateDetailListTable').bootstrapTable({
        url: "/promotOrderEvaluateFlowController.do?dataGrid",
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
                title: '评论编号',
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
            },
            {
                title: '状态',
                field: "state",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    if (value == 1) {
                        return "<span class='label label-info'>pending</span>"
                    } else if (value == 2) {
                        return "<span class='label label-success'>review</span>"
                    }
                    return "";
                }
            },
            {
                title: '亚马逊订单号',
                field: "amzOrderId",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
            },
            {
                title: '评价内容',
                field: "reviewContent",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    if(value === undefined){
                        return "-"
                    }
                    return "<a href='" + row.reviewUrl + "' target='_blank'>" + value + "</a>";
                }
            },
            {
                title: '评价日期',
                field: "reviewDate",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '投诉',
                field: "complaint",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            }
        ]],
        queryParams: function (params) {
            return params;
        }
    });
    $(window).resize(function () {
        $('#evaluateDetailListTable').bootstrapTable('resetView', {
            height: tableHeight()
        })
    })
    function tableHeight() {
        return $(window).height() - 350;
    }

});
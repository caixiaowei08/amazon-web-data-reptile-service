/**
 * Created by User on 2017/6/23.
 */
$(function () {
    var promotId = getQueryString("promotId");
    loadPromotInfo(promotId);
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
        ]],
        queryParams: function (params) {
            params.promotId = promotId;
            return params;
        }
    });

    $(window).resize(function () {
        $('#evaluateListTable').bootstrapTable('resetView', {
            height: tableHeight()
        })
    })
    function tableHeight() {
        return $(window).height() - 300;
    }

    ko.applyBindings(viewModel);

});

function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}

var viewModel = {
    promotId:ko.observable(),
    asinId: ko.observable()
}

function loadPromotInfo(promotId) {
    $.ajax({
        url: "/evaluateController.admin?doGet",
        type: 'post',
        data: {id: promotId},
        success: function (data) {
            if (data.success === "success") {
                viewModel.promotId(data.content.id);
                viewModel.asinId(data.content.asinId);
            } else {
                toastr.warning(data.msg);
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
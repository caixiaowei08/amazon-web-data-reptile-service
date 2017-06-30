$(function () {
    $('#evaluateDetailListTable').bootstrapTable({
        url: "/evaluateController.admin?dataGrid",
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
                title: '评论编号',
                field: "id",
                width: "5%",//宽度
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
                width: "5%",//宽度
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
                width: "25%",//宽度
                align: "center",//水平
                valign: "middle",//垂直
                formatter: function (value, row, index) {
                    if (value === undefined) {
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
            }
        ]],
        queryParams: function (params) {
            params.promotId = getQueryString("promotId").trim();
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

    loadData();
    ko.applyBindings(viewModel);
});

var viewModel = {
    promotId: ko.observable(),
    asinId: ko.observable()
}

function loadData() {
    var promotId = getQueryString("promotId").trim();
    $.ajax({
        url: "/adminPromotController.admin?doGet",
        type: 'post',
        data: {id: promotId},
        success: function (data) {
            if (data.success === "success") {
                viewModel.promotId(data.content.id);
                viewModel.asinId(data.content.asinId);
            } else if (data.success === "RELOGIN") {
                window.location = '/adminSystemController.admin?goAdminLogin';
            } else if (data.success == "fail") {
                toastr.warning(result.msg);
                form.bootstrapValidator('disableSubmitButtons', false);
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.warning("服务器异常,请联系管理员！");
        }
    });

}

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}

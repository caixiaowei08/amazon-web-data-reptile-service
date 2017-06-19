/**
 * Created by User on 2017/6/18.
 */
$(function () {
    $('#promotListTable').bootstrapTable({
        url: "/promotOrderController.do?dataGrid",
        sidePagination: "server",
        dataType: "json",
        search:true,
        pageNumber:1,
        pageSize: 20,
        pageList: [10, 20, 30, 50,100],
        pagination: true,
        searchPlaceholder:"根据ASIN进行搜索！",
        height: tableHeight(),
        clickToSelect: true,//是否启用点击选中行
        uniqueId: "id",
        locale: "zh-CN",
        showColumns: true,
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
                valign: "middle"//垂直
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
                formatter:function(value,row,index){
                    console.log(value);
                    if(value == 1){
                        return "<span class='label label-success'>开启</span>"
                    }else if(value == 2){
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
                field: "",
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            },
            {
                title: '费用',
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
                field: "",
                sortable: true,
                width: "10%",//宽度
                align: "center",//水平
                valign: "middle"//垂直
            }
        ]]
    });

    $(window).resize(function () {
        $('#promotListTable').bootstrapTable('resetView', {
            height: tableHeight()
        })
    })

    function tableHeight() {
        return $(window).height() - 250;
    }
})
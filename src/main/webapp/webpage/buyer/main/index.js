/**
 * Created by User on 2017/7/9.
 */
var total = 0;
var sort = "updateTime";
var order = "desc";
var pageSize = 20;
var offset = 0;
var limit = 20;
var loading = false;

$(function () {
    getDate();
    $(window).scroll(function () {
        var scrollTop = $(this).scrollTop();    //滚动条距离顶部的高度
        var scrollHeight = $(document).height();   //当前页面的总高度
        var clientHeight = $(this).height();    //当前可视的页面高度
        if ((scrollTop + clientHeight) === scrollHeight) {
            if(!loading){
                getDate();
                loading = true;
            }
        }
    });
    window.onresize = function () {
        var clientWidth = $(this).width();
        var clientHeight = $(this).height();
        console.log("clientHeight:" + clientHeight + ",clientWidth:" + clientWidth);
    }
});

function getDate() {
    var req = new Object();
    req.sort = sort;
    req.order = order;
    req.offset = offset;
    req.limit = limit;
    $.ajax({
        url: "/mainBuyerController.buyer?dataGrid",
        type: 'post',
        data: req,
        dataType: "json",
        success: function (data) {
            console.log(data.rows.length);
            total = total + data.rows.length;
            offset = offset + data.rows.length;
            limit = offset + pageSize;
            dealWithData(data.rows);
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.warning("loading error!");
        },
        complete:function () {
            loading = false;
        }
    });
}

function dealWithData(rows) {
    if(rows.length === 0 && $("#noMoreId").hasClass("hidden")){
        $("#noMoreId").removeClass("hidden");
    }

    for (i = 0; i < rows.length; i++) {
        console.log(rows[i]);
        var item =
            "<div class='col-sm-6'>"+
            "<div class='media media-bg'>"+
            "<div class='media-left'>"+
            "<div class='img-container'>"+
            "<a href='"+rows[i].productUrl+"'>"+
            "<img class='img-responsive' src='"+rows[i].thumbnail+"' alt=''>"+
            "</a>"+
            "</div>"+
            "</div>"+
            "<div class='media-body'>"+
            "<div class='media-heading '>"+
            "<p class='title'>"+rows[i].productTitle+"</p>"+
            "<div class='price-box'>"+
            "<span class='price'>"+rows[i].salePrice+"</span>"+
            "<span class='currency'>USD&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>"+
            "<span class='product-brand'>"+rows[i].brand+"</span>"+
            "</div>"+
            "<div class='amazon-info'>"+
            "<span class='product-num'>"+rows[i].stockNum+"</span>"+
            "<span class='currency'>&nbsp;&nbsp;in stock,today</span>"+
            "<span class='repay-note'>just repay 1 time</span>"+
            "</div>"+
            "<div class='btn-buyer'>"+
            "<a href='"+rows[i].productUrl+"' class='pull-right btn btn-warning btn-xs'>Buy now</a>"+
            "</div>"+
            "</div>"+
            "</div>"+
            "</div>"+
            "</div>";
        $("#goods-data").append(item);
    }
}





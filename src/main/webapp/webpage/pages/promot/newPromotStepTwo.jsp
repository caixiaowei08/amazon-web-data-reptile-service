<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/webpage/context/baseTags.jsp" %>
<html>
<head>
    <title>Review Tracker</title>
    <meta charset="utf-8"/>
    <link rel="shortcut icon" type="image/x-icon" href="/webpage/plug-in/imgs/favicon.ico" media="screen"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0,user-scalable=no"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/font-awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/toastr/toastr.css"/>
    <link rel="stylesheet" href="/webpage/pages/main/index.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrapvalidator/dist/css/bootstrapValidator.min.css"/>
    <link href="/webpage/plug-in/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet"
          media="screen">
    <!--[if lte IE 9]>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <script src="https://cdn.bootcss.com/html5shiv/r29/html5.js"></script>
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <![endif]-->
    <script type="text/javascript" src="/webpage/plug-in/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrapvalidator/dist/js/language/zh_CN.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js"
            charset="UTF-8"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"
            charset="UTF-8"></script>
    <script type="text/javascript" src="/webpage/plug-in/toastr/toastr.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/knockoutjs/dist/knockout.js"></script>
</head>
<body style="overflow-y:auto;">
<div class="main-container">
    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Review Tracker</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a href="/mainController.do?index" target="_parent">
                    <div class="pull-left " style="width: 50px;height: 50px;margin:3px 6px; ">
                        <img alt="Brand" class="img-responsive img-rounded" src="/webpage/plug-in/imgs/logo.jpg">
                    </div>
                    <div class="navbar-brand font-color">
                        Review Tracker
                    </div>
                </a>
            </div>
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle " data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">客服咨询<span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li>
                                <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=282208271&site=qq&menu=yes">
                                    客服1&nbsp;<img border="0" src="http://wpa.qq.com/pa?p=2:282208271:51" alt="客服1"
                                                  title="客服1"/>
                                </a>
                            </li>
                            <li>
                                <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3170047818&site=qq&menu=yes">
                                    客服2&nbsp;<img border="0" src="http://wpa.qq.com/pa?p=2:3170047818:51" alt="客服2"
                                                  title="客服2"/>
                                </a>
                            </li>
                        </ul>
                    </li>
                    <li><a href="/promotOrderController.do?goNewPromotOne" target="_parent">新建推广活动</a></li>
                    <li><a href="/redirectionController.do?goManagePromot" target="_parent">推广活动管理</a></li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle " data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">账户充值<span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="/redirectionController.do?goToChargeFund" target="_parent">余额充值</a></li>
                            <li><a href="/redirectionController.do?goToChargeMemberShip" target="_parent">购买会员</a></li>
                            <li><a href="/redirectionController.do?chargeFundFlow" target="_parent">充值记录</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle " data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">我的账户<span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="redirectionController.do?goUserDetailInfo" target="_parent">账户详情</a></li>
                            <li><a href="redirectionController.do?goUserChangePwd" target="_parent">修改密码</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="/userController.do?doLogOff" target="_parent">
                            退出
                            <i class="fa fa-power-off" style="color: red" aria-hidden="true"></i>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    <div class="main-content">
        <div style="height: 20px;"></div>
        <div class="container">
            <div class="panel panel-warning">
                    <form id="promotOrderSubmitForm" action="/promotOrderController.do?doAdd" onsubmit="return false;">
                    <div class="panel-heading">
                        <h3 class="panel-title">新建推广活动</h3>
                    </div>
                    <div class="panel-body">
                        <h5>
                            <strong>第二步</strong>:&nbsp;&nbsp;填写推广活动订单信息
                        </h5>
                        <div style="height: 25px;">
                        </div>
                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-addon" id="pageUrl-addon">
                                    <div class="text-right" style="width: 80px;">产品URL链接</div>
                                </span>
                                <input data-bind="value: pageUrl" type="text" class="form-control" readonly="readonly"
                                       id="asinOrUrl" name="productUrl" placeholder="请输入亚马逊ASIN或者商品主页链接！"
                                       aria-describedby="pageUrl-addon">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group">
                        <span class="input-group-addon" id="asin-addon">
                            <div class="text-right" style="width: 80px;">ASIN编码</div>
                        </span>
                                <input data-bind="value: asin" type="text" readonly="readonly" class="form-control"
                                       id="asin"
                                       name="asinId"
                                       aria-describedby="asin-addon">
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group">
                        <span class="input-group-addon" id="productTitle-addon">
                            <div class="text-right" style="width: 80px;">商品标题</div>
                        </span>
                                <input data-bind="value: productTitle" type="text" readonly="readonly"
                                       class="form-control"
                                       id="productTitle"
                                       name="productTitle" aria-describedby="productTitle-addon">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-7">
                                <div class="form-group">
                                    <div class="input-group">
                                <span class="input-group-addon" id="priceblockSaleprice-addon">
                                    <div class="text-right" style="width: 80px;">亚马逊价格</div>
                                </span>
                                        <input data-bind="value: priceblockSaleprice" type="text" readonly="readonly"
                                               class="form-control"
                                               id="priceblockSaleprice" name="salePrice"
                                               placeholder="亚马逊价格"
                                               aria-describedby="priceblockSaleprice-addon">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="input-group">
                                        <span class="input-group-addon" id="brand-addon">
                                            <div class="text-right" style="width: 80px; ">店铺名称</div>
                                        </span>
                                        <input data-bind="value:brand" type="text" readonly="readonly"
                                               class="form-control" id="brand"
                                               name="brand"
                                               aria-describedby="brand-addon">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="input-group">
                                        <span class="input-group-addon" id="needReviewNum-addon">
                                            <div class="text-right" style="width: 80px; ">目标好评数*</div>
                                        </span>
                                        <input type="number" data-bind="value:needReviewNum" class="form-control"
                                               id="needReviewNum"
                                               onkeydown="if(event.keyCode==13){event.keyCode=0;event.returnValue=false;}"
                                               name="needReviewNum"
                                               placeholder="请输入目标好评数！"
                                               aria-describedby="needReviewNum-addon">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="input-group">
                                        <span class="input-group-addon" id="reviewPrice-addon">
                                            <div class="text-right" style="width: 80px; ">评价扣现单价</div>
                                        </span>
                                        <input type="text" data-bind="value:reviewPrice" class="form-control"
                                               id="reviewPrice"
                                               readonly="readonly" aria-describedby="reviewPrice-addon">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="input-group">
                                        <span class="input-group-addon" id="dayReviewNum-addon">
                                            <div class="text-right" style="width: 80px; ">每天好评数*</div>
                                        </span>
                                        <input type="number" class="form-control" id="dayReviewNum" name="dayReviewNum"
                                               onkeydown="if(event.keyCode==13){event.keyCode=0;event.returnValue=false;}"
                                               placeholder="请输入每天好评数！"
                                               data-bind='value: dayReviewNum'
                                               aria-describedby="dayReviewNum-addon" value="10">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="input-group">
                                            <span class="input-group-addon" id="guaranteeFund-addon">
                                                <div class="text-right" style="width: 80px; ">所需保证金</div>
                                            </span>
                                        <input type="text" data-bind='value: guaranteeFund' class="form-control"
                                               readonly="readonly"
                                               id="guaranteeFund"
                                               aria-describedby="guaranteeFund-addon">
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-5">
                                <img src="" data-bind="attr:{src:landingImage}" alt="产品主图" style="height:250px; "
                                     class="img-responsive center-block">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <div class="input-group date form_date" data-date="" data-date-format="yyyy-mm-dd"
                                         data-link-field="dtp_input2" data-link-format="yyyy-mm-dd">
                                        <span class="input-group-addon">
                                            <div class="text-right" style="width: 80px;">结束日期*</div>
                                        </span>
                                        <input class="form-control" name="finishDate" data-bind="value:finishDate"
                                               id="finishDate" placeholder="订单最少时间为三天！" readonly="readonly"
                                               size="16" type="text" value=""
                                               onkeydown="if(event.keyCode==13){event.keyCode=0;event.returnValue=false;}"
                                        >
                                        <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <div class="input-group">
                                        <span class="input-group-addon" id="keyword-addon">
                                            <div class="text-right" style="width: 80px; ">搜索关键词</div>
                                        </span>
                                        <input data-bind="value:keyword" type="text"
                                               class="form-control" id="keyword"
                                               name="keyword"
                                               aria-describedby="brand-addon">
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-4">
                                <div class="form-group">
                                    <div class="input-group">
                                        <span class="input-group-addon" id="sort-addon">
                                            <div class="text-right" style="width: 80px; ">关键字排名</div>
                                        </span>
                                        <input type="number" data-bind="value:sequence" class="form-control"
                                               id="sequence"
                                               onkeydown="if(event.keyCode==13){event.keyCode=0;event.returnValue=false;}"
                                               name="sequence"
                                               placeholder="请输入排位"
                                               aria-describedby="sort-addon">
                                        <span class="input-group-addon" id="basic-addon2">页</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-group">
                                <span class="input-group-addon" id="remark-addon">
                                    <div class="text-right" style="width: 80px; ">备注：</div>
                                </span>
                                <input type="text" name="remark" class="form-control" data-bind="value:remark"
                                       placeholder="写下您的备注信息！"
                                       id="remark"
                                       aria-describedby="remark-addon">
                            </div>
                        </div>
                    </div>
                    <div style="height: 20px;"></div>
                    <div class="panel-footer">
                        <div class="container">
                            <div class="row">
                                <div class="col-md-1 col-md-offset-6">
                                    <input type="button" onclick="goToNewPromotOne();" class="btn btn-default" value="上一步"/>
                                </div>
                                <div class="col-md-1">
                                    <input type="button" onclick="submitOrder();" class="btn btn-primary" value="提&nbsp;交">
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <div style="height: 100px;"></div>
    </div>
    <div class="modal fade bs-example-modal-sm" id="chargeFundModel" tabindex="-1" role="dialog"
         aria-labelledby="chargeFundModel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title">
                        提示：可用余额不足，请充值！
                    </h4>
                </div>
                <div class="modal-body">
                    <input type="hidden" id="chargeFund" name="chargeFund">
                    请先补充余额差额<span id="chargeFundShow"></span>美元，并重新提交订单！
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="deleteEvaluateByIdBtn"
                            onclick="chargeFundNow();">马上充值
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<nav class="navbar navbar-default navbar-fixed-bottom">
    <div class="container">
        <div class="row">
            <div class="col-sm-8 col-sm-offset-2">
                <div style="text-align:center;line-height: 28px;">
                    Copyright&copy;Viscal Technology Services Ltd All Rights Reserved&reg;ICP备17013383号
                </div>
            </div>
        </div>
    </div>
</nav>
</body>
</html>
<script>
    $(function () {
        loadData();
        formValidator();
        $('.form_date').datetimepicker({
            weekStart: 1,
            todayBtn: 1,
            autoclose: 1,
            todayHighlight: 1,
            startView: 2,
            minView: 2,
            startDate: new Date(Date.parse(new Date().toString()) + 86400000 * 3),
            forceParse: 0
        }).on('hide',function(e) {
            $('#promotOrderSubmitForm').data('bootstrapValidator')
                .updateStatus('finishDate', 'NOT_VALIDATED',null)
                .validateField('finishDate');
        });
    });

    var ViewModel = function (pageUrl, asin, productTitle, priceblockSaleprice,
                              brand, reviewPrice, needReviewNum, landingImage,
                              dayReviewNum, finishDate, remark, sequence, keyword) {
        this.pageUrl = ko.observable(pageUrl);
        this.asin = ko.observable(asin);
        this.productTitle = ko.observable(productTitle);
        this.priceblockSaleprice = ko.observable(priceblockSaleprice);
        this.brand = ko.observable(brand);
        this.reviewPrice = ko.observable(reviewPrice);
        this.needReviewNum = ko.observable(needReviewNum);
        this.dayReviewNum = ko.observable(dayReviewNum);
        this.landingImage = ko.observable(landingImage);
        this.finishDate = ko.observable(finishDate);
        this.remark = ko.observable(remark);
        this.guaranteeFund = ko.computed(function () {
            var m = 0,
                s1 = this.reviewPrice().toString(),
                s2 = this.needReviewNum().toString();
            s3 = this.priceblockSaleprice().toString();
            s1 = s1.substring(1, s1.length);
            s3 = s3.substring(1, s3.length);
            console.log(s1);
            console.log(s3);
            console.log(Number(s1) + Number(s3));
            s4 = (Number(s1) + Number(s3)).toString();
            try {
                m += s4.split(".")[1].length
            } catch (e) {
            }
            try {
                m += s2.split(".")[1].length
            } catch (e) {
            }
            return "$" + (Math.ceil((Number(s4.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)) * 100)) / Math.pow(10, 2);
        }, this);
        this.sequence = ko.observable(sequence);
        this.keyword = ko.observable(keyword);
    };
    function loadData() {
        $.ajax({
            url: "/promotOrderController.do?doGetPromotOrderTemp",
            type: 'post',
            success: function (data) {
                if (data.success === "success") {
                    ko.applyBindings(
                        new ViewModel(data.content.pageUrl,
                            data.content.asin,
                            data.content.productTitle,
                            data.content.priceblockSaleprice,
                            data.content.brand,
                            data.content.reviewPrice,
                            data.content.needReviewNum,
                            data.content.landingImage,
                            data.content.dayReviewNum,
                            data.content.finishDate,
                            data.content.remark,
                            data.content.sequence,
                            data.content.keyword
                        )
                    );
                } else if (data.success === "fail") {
                    window.location = '/mainController.do?index';
                } else {
                    window.location = '/loginController.do?login';
                }
            },
            error: function (jqxhr, textStatus, errorThrow) {
                toastr.error("服务器异常,请联系管理员！");
            }
        });
    }
    
    function submitOrder() {
        $('#promotOrderSubmitForm').submit();
        return false;
    }

    function chargeFundNow() {
        window.location = '/redirectionController.do?goToChargeFund&chargeFund=' + $("#chargeFund").val();
    }

    function goToNewPromotOne() {
        window.location = '/promotOrderController.do?goNewPromotOne';
    }

    function formValidator() {
        $('#promotOrderSubmitForm').bootstrapValidator({
            framework: 'bootstrap',
            icon: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                needReviewNum: {
                    validators: {
                        notEmpty: {
                            message: '请输入目标好评数'
                        },
                        regexp: {
                            regexp: /^[1-9]\d*$/,
                            message: '请输入大于0的数字！'
                        }
                    }
                },
                dayReviewNum: {
                    validators: {
                        notEmpty: {
                            message: '请输入每天好评数'
                        },
                        regexp: {
                            regexp: /^[1-9]\d*$/,
                            message: '请输入大于0的数字！'
                        }
                    }
                },
                finishDate: {
                    validators: {
                        notEmpty: {
                            message: '请输入推广活动结束日期！'
                        }
                    }
                },
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
            console.log("-----success.form.bv------");
            var form = $('#promotOrderSubmitForm');
            beforeSend();
            $.post(form.attr('action'), form.serialize(), function (data) {
                if (data.success === "success") {
                    window.location = '/redirectionController.do?goManagePromot';
                } else if (data.success === "fail") {
                    toastr.error(data.msg);
                } else if (data.success === "charge") {
                    $("#chargeFund").val(data.content);
                    $("#chargeFundShow").text(data.content);
                    $('#chargeFundModel').modal('show');
                } else {
                    window.location = '/loginController.do?login';
                }
                console.log("-----success.form.bv---end-----")
                sendComplete();
            }, 'json');
        });
    }


    function beforeSend() {
        $("#deleteEvaluateByIdBtn").addClass("disabled"); // Disables visually
        $("#deleteEvaluateByIdBtn").prop("disabled", true); // Disables visually + functionally
    }

    function sendComplete() {
        $("#deleteEvaluateByIdBtn").removeClass("disabled"); // Disables visually
        $("#deleteEvaluateByIdBtn").prop("disabled", false); // Disables visually + functionally
    }
</script>

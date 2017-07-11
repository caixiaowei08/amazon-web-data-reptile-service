<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/webpage/context/baseTags.jsp" %>
<html>
<head >
    <title>Seller Assistant</title>
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
    <script type="text/javascript" src="/webpage/plug-in/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/toastr/toastr.min.js"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body style="overflow-y:auto;">
<div class="main-container">
    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Seller Assistant</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand font-color" href="/mainController.do?index" target="_parent">Seller Assistant</a>
            </div>
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle " data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">客服咨询<span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li>
                                <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=282208271&site=qq&menu=yes">
                                    客服1&nbsp;<img border="0" src="http://wpa.qq.com/pa?p=2:282208271:51" alt="客服1" title="客服1"/>
                                </a>
                            </li>
                            <li>
                                <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3170047818&site=qq&menu=yes">
                                    客服2&nbsp;<img border="0" src="http://wpa.qq.com/pa?p=2:3170047818:51" alt="客服2" title="客服2"/>
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
            <div style="height: 20px;"></div>
            <div class="panel panel-warning">
                <div class="panel-heading">
                    <h3 class="panel-title">新建推广活动</h3>
                </div>
                <div class="panel-body">
                    <form id="formObj" onsubmit="return doDealAsinOrUrl()">
                        <h5>
                            <strong>第一步</strong>:&nbsp;&nbsp;输入亚马逊商品ASIN编码
                        </h5>
                        <div style="height: 25px;">
                            <div class="row">
                                <div id="loading" class="hidden col-lg-6 col-lg-offset-3">正在处理信息，请稍候....</div>
                            </div>
                        </div>
                        <div class="form-group" style="height:40px;">
                            <div class="input-group">
                                <span class="input-group-addon" id="basic-addon1">ASIN编码：</span>
                                <input type="text" class="form-control" id="asinOrUrl"  onkeydown="if(event.keyCode==13){event.keyCode=0;event.returnValue=false;}" name="asinOrUrl" placeholder="请输入亚马逊ASIN或者商品主页链接！"
                                       aria-describedby="basic-addon1">
                            </div>
                        </div>
                    </form>
                </div>
                <div style="height: 20px;"></div>
                <div class="panel-footer">
                    <div class="container">
                        <div class="row">

                            <div class="col-md-2 col-md-offset-2">
                                <a href="/mainController.do?index" class="btn btn-default">返回主页</a>
                            </div>

                            <div class="col-md-2 col-md-offset-4">
                                <div class="btn-group" role="group">
                                    <button type="button" id="btn_sub" class="btn btn-default">下一步</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div style="height: 100px;"></div>
    </div>
</div>
<nav class="navbar navbar-default navbar-fixed-bottom">
    <div class="container">
        <div class="row">
            <div class="col-sm-8 col-sm-offset-2">
                <div style="text-align:center;line-height: 28px;">
                    Copyright&copy;Vascal Technology Services Ltd  All Rights Reserved&reg;鄂ICP备17013383号
                </div>
            </div>
        </div>
    </div>
</nav>
</body>
</html>
<script>
    $(function () {
        $('#formObj').bootstrapValidator({
            framework: 'bootstrap',
            icon: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                asinOrUrl: {
                    validators: {
                        notEmpty: {
                            message: '请输入亚马逊ASIN或者商品主页链接！'
                        }
                    }
                }
            }
        });
    });

    $("#btn_sub").click(function(){
        $('#formObj').submit();
    });

    function doDealAsinOrUrl() {
        var asinOrUrl = $("#asinOrUrl").val();
        if (asinOrUrl === null || asinOrUrl === undefined || asinOrUrl === '') {
            return;
        }
        
        function beforeSend() {
            $("#loading").removeClass("hidden");
            $("#btn_sub").addClass("disabled"); // Disables visually
            $("#btn_sub").prop("disabled", true); // Disables visually + functionally
        }

        function SendComplete() {
            $("#loading").addClass("hidden");
            $("#btn_sub").removeClass("disabled"); // Disables visually
            $("#btn_sub").prop("disabled", false); // Disables visually + functionally
        }
        
        $.ajax({
            url:"/promotOrderController.do?doDealAsinOrUrl",
            type:'post',
            beforeSend:beforeSend,
            data:$('#formObj').serialize(),
            success:function(data){
                if(data.success === "success"){
                    window.location='/promotOrderController.do?goNewPromotTwo'
                }else if (data.success === "fail") {
                    toastr.error(data.msg);
                } else {
                    window.location = '/loginController.do?login';
                }
            },
            error:function(jqxhr,textStatus,errorThrow){
                toastr.error("服务器异常,请联系管理员！");
            },
            complete:function () {
                SendComplete();
            }
        });
    }
</script>

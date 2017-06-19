<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/webpage/context/baseTags.jsp" %>
<html>
<head>
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
<body>
<div class="main-container">
    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand font-color" href="#">Seller Assistant</a>
            </div>
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle " data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">我的推广 <span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="promotOrderController.do?goNewPromotOne" target="main-content-name">新建推广</a></li>
                            <li><a href="redirectionController.do?goManagePromot">管理推广</a></li>
                        </ul>
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
                                <div id="loading" class="hidden col-lg-6 col-lg-offset-3">爬虫正在抓取网页数据。。。</div>
                            </div>
                        </div>
                        <div class="form-group" style="height:40px;">
                            <div class="input-group">
                                <span class="input-group-addon" id="basic-addon1">ASIN编码：</span>
                                <input type="text" class="form-control" id="asinOrUrl" name="asinOrUrl" placeholder="请输入亚马逊ASIN或者商品主页链接！"
                                       aria-describedby="basic-addon1">
                            </div>
                        </div>
                    </form>
                </div>
                <div style="height: 20px;"></div>
                <div class="panel-footer">
                    <div class="container">
                        <div class="row">
                            <div class="col-md-2 col-md-offset-8">
                                <div class="btn-group" role="group">
                                    <button type="button" id="btn_sub" class="btn btn-default">下一步</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
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
            console.log("true");
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
                }else{
                    toastr.error(data.msg);
                }
            },
            error:function(jqxhr,textStatus,errorThrow){
                toastr.success("服务器异常,请联系管理员！");
            },
            complete:function () {
                SendComplete();
            },
            statusCode:{
                404:function(){console.log('not found');},
                500:function(){console.log('error by server');},
            }
        });
    }
</script>

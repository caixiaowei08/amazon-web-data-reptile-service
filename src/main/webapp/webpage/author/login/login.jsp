<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/webpage/context/baseTags.jsp" %>
<!DOCTYPE html>
<head>
    <title>Review Tracker</title>
    <meta charset="utf-8"/>
    <link rel="shortcut icon" type="image/x-icon" href="/webpage/plug-in/imgs/favicon.ico" media="screen"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta http-equiv="X-UA-Compatible" content="IE=9"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0,user-scalable=no"/>
    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/font-awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/toastr/toastr.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/common/css/main.css"/>
    <link rel="stylesheet" href="/webpage/pages/login/login.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrapvalidator/dist/css/bootstrapValidator.min.css"/>
    <!--[if lte IE 9]>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <script src="https://cdn.bootcss.com/html5shiv/r29/html5.js"></script>
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <![endif]-->
    <script type="text/javascript" src="/webpage/plug-in/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/toastr/toastr.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>
    <script type="text/javascript" src="/webpage/author/login/login.js"></script>
</head>
<body class="container-fluid" style="overflow-y:auto;">
<div style="height: 100px;"></div>
<div class="row">
    <div class="col-lg-4 col-lg-offset-4" >
        <div class="thumbnail">
            <div class="page-header text-center text-primary">
                <h3>Review Tracker订单管理登录</h3>
            </div>
            <p>&nbsp;</p>
            <form id="formObj" class="form-horizontal" action="/author/userController.author?doLogin" onsubmit="return false;">
                <div class="form-group" style="height: 54px;">
                    <label for="account" class="col-sm-3 control-label">账号:</label>
                    <div class="col-sm-7">
                        <input type="text" name="account" onkeydown="if(event.keyCode==13){event.keyCode=0;event.returnValue=false;}" id="account" class="form-control" placeholder="请输入登录账号">
                    </div>
                </div>
                <div class="form-group" style="height: 54px;">
                    <label for="pwd" class="col-sm-3 control-label">密码:</label>
                    <div class="col-sm-7">
                        <input type="password" name="pwd" id="pwd"  onkeydown="if(event.keyCode==13){event.keyCode=0;event.returnValue=false;}" class="form-control" placeholder="请输入登录密码">
                    </div>
                </div>
                <div class="form-group" >
                    <div class="col-sm-7  col-sm-offset-3">
                        <input type="button" id="authorLoginBtn" onclick="loginAuthorSubmit();" value="登录" class="btn btn-primary btn-block">
                    </div>
                </div>
            </form>
            <div style="height:30px;"></div>
        </div>
    </div>
</div>
<nav class="navbar navbar-default navbar-fixed-bottom">
    <div class="container">
        <div class="row">
            <div class="col-sm-8 col-sm-offset-2">
                <div style="text-align:center;line-height: 28px;">
                    Copyright&copy;Viscal Technology Services Ltd  All Rights Reserved&reg;ICP备17013383号
                </div>
            </div>
        </div>
    </div>
</nav>
</body>
</html>
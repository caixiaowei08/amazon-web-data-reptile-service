<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/webpage/context/baseTags.jsp" %>
<!DOCTYPE html>
<head>
    <title>Seller Assistant</title>
    <meta charset="utf-8"/>
    <link rel="shortcut icon" type="image/x-icon" href="/webpage/plug-in/imgs/favicon.ico" media="screen"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0,user-scalable=no"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="/webpage/buyer/user/sendEmailResetPwd.css"/>
    <!--[if lte IE 9]>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <script src="https://cdn.bootcss.com/html5shiv/r29/html5.js"></script>
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <![endif]-->
    <script type="text/javascript" src="/webpage/plug-in/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrapvalidator/dist/js/language/zh_CN.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/webpage/buyer/user/sendEmailResetPwd.js"></script>
</head>
<body>
<div class="bg-amazon-login">
    <div style="height: 100px;"></div>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-4 col-md-offset-4">
                <div class="panel-login">
                    <div class="page-header text-center text-warning">
                        <h3>Reset your password</h3>
                    </div>
                    <form id="formObj" class="form-horizontal" onsubmit="return doAdminLogin()">
                        <div class="form-group" style="height: 54px;">
                            <label for="account" class="pull-left">Enter your email address and we will send you a link to reset your password.</label>
                            <div class="input-group-lg">
                                <input type="text" name="account" placeholder="Enter your email address"
                                       onkeydown="if(event.keyCode==13){event.keyCode=0;event.returnValue=false;}"
                                       id="account" class="form-control">
                            </div>
                        </div>
                        <div style="height:30px;"></div>
                        <div class="form-group">
                            <input type="button" id="loginSubmit" onclick="loginAdmin();" value="Send Password reset email"
                                   class="btn btn-block btn-success btn-lg">
                        </div>
                        <div style="height:10px;"></div>
                    </form>
                    <div class="login-bottom">
                        <div style="height: 10px;"></div>
                        <div class="container-fluid">
                            <div class="text-center">
                                <label>I remember the password ?</label>
                                <a href="/userPageController.buyer?login" target="_parent" class="">Sign in</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div style="height:50px;"></div>
</div>
</body>
</html>

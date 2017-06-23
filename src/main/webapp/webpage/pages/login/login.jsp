<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/webpage/context/baseTags.jsp" %>
<html>
<head>
    <title>Seller Assistant</title>
    <meta charset="utf-8"/>
    <link rel="shortcut icon" type="image/x-icon" href="/webpage/plug-in/imgs/favicon.ico" media="screen"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta http-equiv="X-UA-Compatible" content="IE=9" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0,user-scalable=no"/>
    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/font-awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/toastr/toastr.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/common/css/main.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/validform/css/metro/style.css"/>
    <link rel="stylesheet" href="/webpage/pages/login/login.css"/>
    <script type="text/javascript" src="/webpage/plug-in/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/validform/js/Validform_v5.3.1_min_zh-cn.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/toastr/toastr.min.js"></script>

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body class="container-fluid">
<div class="row">
    <div style="margin-top: 100px;">
        <div class="col-lg-10 col-lg-offset-1">
            <div class="row">
                <div class="col-md-4 col-md-offset-1">
                    <h2>欢迎使用Seller Assistant</h2>
                    <p>&nbsp;</p>
                    <p class="sign-left">您唯一需要做的就是在我们的平台上设置需要推广的产品，其他事情将由我们处理。只有当为您带来亚马逊评论时，我们才会收取少许费用。</p>
                    <p class="sign-left">现在开始行动，加入我们吧！</p>
                </div>
                <div class="col-md-4 col-md-offset-1">
                    <div class="thumbnail">
                        <div class="page-header text-center text-primary">
                            <h3>欢迎登录Seller Assistant</h3>
                        </div>
                        <p>&nbsp;</p>
                        <form id="formobj" class="form-horizontal" action="userController.do?doLogin">
                            <div class="form-group" style="height: 54px;">
                                <label for="email" class="col-sm-3 control-label">电子邮箱:</label>
                                <div class="col-sm-7">
                                    <input type="email" name="account" id="email" datatype="e" class="form-control" placeholder="请输入邮箱账号!"
                                           sucmsg="用户名验证通过！" nullmsg="请输入用户名！" errormsg="请输入登录邮箱！">
                                </div>
                            </div>
                            <div class="form-group" style="height: 54px;">
                                <label for="password" class="col-sm-3 control-label">密&nbsp;&nbsp;&nbsp;&nbsp;码:</label>
                                <div class="col-sm-7">
                                    <input type="password" name="pwd" id="password" datatype="*" class="form-control" placeholder="请输入密码！"
                                           sucmsg="密码通过验证！" nullmsg="请输入密码！" errormsg="请输入密码！">
                                </div>
                            </div>
                            <div class="form-group" >
                                <div class="col-sm-7  col-sm-offset-3">
                                    <input type="button" id="login_submit" value="登录" class="btn btn-primary btn-block">
                                </div>
                            </div>
                        </form>
                        <p>&nbsp;</p>
                        <div class="row" style="border-top: 1px solid #e4e5e6">
                            <div class="col-sm-5 col-sm-offset-1">
                                <a href="/loginController.do?register" style="text-decoration: none;color:#03a9f4">没有账号？注册</a>
                            </div>
                            <div class="col-sm-4 col-sm-offset-2">
                                <a href="/loginController.do?lookForPwd" style="text-decoration: none;color:#78808a">忘记密码？</a>
                            </div>
                            <p>&nbsp;</p>
                        </div>
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
                        版权声明：&copy; 归属某某有限公司 2017-2020 该网站不支持IE浏览器 建议使用chrome
                    </div>
                </div>
            </div>
        </div>
    </nav>
</div>
</body>
</html>
<script>
    $(function() {
        $("#formobj").Validform({
            tiptype:4,
            btnSubmit:"#login_submit",
            ajaxPost: true,
            callback: function(data){
                if (data.success =="success") {
                    toastr.info(data.msg);
                    setTimeout("window.location='/mainController.do?index'", 1000);
                } else if(data.success =="fail"){
                    toastr.warning(data.msg);
                } else {
                    toastr.error("服务器宕机或者网络问题！");
                    return false;
                }
            }
        })
    })
</script>

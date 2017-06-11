<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/webpage/context/baseTags.jsp" %>
<html>
<head>
    <title>Seller Assistant</title>
    <meta charset="utf-8"/>
    <link rel="shortcut icon" type="image/x-icon" href="/webpage/plug-in/imgs/favicon.ico" media="screen"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0,user-scalable=no"/>
    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/font-awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/toastr/toastr.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/common/css/main.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/validform/css/metro/style.css"/>
    <link rel="stylesheet" href="/webpage/pages/login/login.css"/>
    <script type="text/javascript" src="/webpage/context/GlobalConstant.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/validform/js/Validform_v5.3.1_min_zh-cn.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/toastr/toastr.min.js"></script>
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
                <div class="col-md-5 col-md-offset-1">
                    <div class="thumbnail">
                        <div class="page-header text-center text-primary">
                            <h3>找回密码Seller Assistant</h3>
                        </div>
                        <p>&nbsp;</p>
                        <form id="formobj" class="form-horizontal" action="userController.do?doLookForPwd">
                            <div class="form-group" style="height: 54px;">
                                <label for="email" class="col-sm-3 control-label">电子邮箱:</label>
                                <div class="col-sm-6">
                                    <input type="email" name="account" id="email" datatype="e" class="form-control"
                                           placeholder="请输入邮箱账号!"
                                           sucmsg="用户名验证通过！" nullmsg="请输入邮箱用户名！" errormsg="请输入登录邮箱！">
                                </div>
                                <div class="col-sm-3">
                                    <input type="button" id="getEmailCode"  value="获取验证码" class="btn btn-info btn-block">
                                </div>
                            </div>
                            <div class="form-group" style="height: 54px;">
                                <label for="email" class="col-sm-3 control-label">邮件验证码:</label>
                                <div class="col-sm-6">
                                    <input type="text" name="emailCode" id="emailCode" datatype="/^\d{4}$/" class="form-control"
                                           placeholder="请输入邮箱所收到的验证码"
                                           sucmsg="已输入验证码" nullmsg="请输入验证码！" errormsg="请输入数字验证码">
                                </div>
                            </div>
                            <div class="form-group" style="height: 54px;">
                                <label for="password" class="col-sm-3 control-label">新密码:</label>
                                <div class="col-sm-6">
                                    <input type="password" name="pwd" id="password" datatype="*6-18"
                                           class="form-control" placeholder="请输入新密码！"
                                           sucmsg="密码通过验证！" nullmsg="请输入6-18位密码！" errormsg="请输入6-18位密码！">
                                </div>
                            </div>
                            <div class="form-group" style="height: 54px;">
                                <label for="repassword"
                                       class="col-sm-3 control-label">确认新密码:</label>
                                <div class="col-sm-6">
                                    <input type="password" recheck="pwd" id="repassword" datatype="*"
                                           class="form-control" placeholder="请再次输入密码！"
                                           sucmsg="密码通过验证！" nullmsg="请再次输入密码！" errormsg="您两次输入的账号密码不一致！">
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-6  col-sm-offset-3">
                                    <input type="button" id="login_register" value="修改密码"
                                           class="btn btn-primary btn-block">
                                </div>
                            </div>
                        </form>
                        <p>&nbsp;</p>
                        <div class="row" style="border-top: 1px solid #e4e5e6">
                            <div class="col-sm-5 col-sm-offset-1">
                                <a href="/loginController.do?login" style="text-decoration: none;color:#03a9f4">已有账号？登录</a>
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
                        版权声明：&copy; 蔡晓伟 2017-2020
                    </div>
                </div>
            </div>
        </div>
    </nav>
</div>
</body>
</html>
<script>
    $(function () {
        $("#getEmailCode").click(function () {
            var email = $("#email").val();

            if(!isEmail(email)){
                toastr.warning("请输入正确的邮箱！");
                return;
            }

            $.ajax({
                type:"post",
                url:"userController.do?sendEmailCode",
                async:true,
                timeout:10000,
                data:{account:email},
                success: function(res){
                    if(res.success === "success"){
                        toastr.success("发送邮箱验证码成功！");
                    }else{
                        toastr.warning(res.msg);
                    }
                },
                error:function () {
                    toastr.warning("服务器异常，请联系维护人员！");
                },
                complete:function (XMLHttpRequest, textStatus) {
                    if(status=='timeout'){//超时,status还有success,error等值的情况
                        toastr.warning("获取数据超时,请检查网络问题！");
                    }
                }
            },'json');

            var i = 60;
            $("#getEmailCode").addClass('disabled');
            var flag = setInterval(function(){
                $("#getEmailCode").val(i+"s");
                i--;
                if(i < 0){
                    $("#getEmailCode").removeClass('disabled');
                    $("#getEmailCode").val("获取验证码");
                    clearInterval(flag);
                }
            },1000);
        });

        $("#formobj").Validform({
            tiptype: 4,
            btnSubmit: "#login_register",
            postonce: true,
            ajaxPost: true,
            callback: function (data) {
                if (data.success == "success") {
                    toastr.info(data.msg);
                    setTimeout("window.location='/loginController.do?login'", 2000);
                } else if (data.success == "fail") {
                    toastr.info(data.msg);
                } else {
                    toastr.warning("服务器宕机或者网络问题！");
                    return false;
                }
            }
        });
    });

    /**
     * 邮箱正则表达式
     * @param str
     * @returns {boolean}
     */
    function isEmail(str){
        var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
        return reg.test(str);
    }
</script>

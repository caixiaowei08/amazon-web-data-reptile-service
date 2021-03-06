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
    <link rel="stylesheet" href="/webpage/plug-in/bootstrapvalidator/dist/css/bootstrapValidator.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap-table/dist/bootstrap-table.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/toastr/toastr.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/ace/css/ace.css"/>
    <link rel="stylesheet" href="/webpage/pages/main/index.css"/>
    <link rel="stylesheet" href="/webpage/admin/evaluate/evaluateDetail.css"/>
    <!--[if lte IE 9]>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <script src="https://cdn.bootcss.com/html5shiv/r29/html5.js"></script>
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <![endif]-->
    <script type="text/javascript" src="/webpage/plug-in/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrapvalidator/dist/js/language/zh_CN.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/toastr/toastr.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/knockoutjs/dist/knockout.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap-table/dist/bootstrap-table.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap-table/dist/bootstrap-table-locale-all.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap-table/dist/locale/bootstrap-table-zh-CN.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="/webpage/author/userInfo/changePwd.js"></script>
</head>
<body style="overflow-y:auto;">
<div class="main-container">
    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Review Tracker订单管理端</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand font-color" href="/author/pageController.author?main" target="_parent">Review Tracker订单管理端</a>
            </div>
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li><a href="/author/pageController.author?main" target="_parent">推广订单管理</a></li>
                    <li><a href="/author/pageController.author?evaluate" target="_parent">订单评价管理</a></li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle " data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">我的账号<span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="/author/pageController.author?changePwd" target="_parent">修改密码</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="/author/userController.author?doLoginOff">
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
                    <h3 class="panel-title">密码修改</h3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-2 col-md-offset-2">
                            <div style="height: 30px;"></div>
                            <a href="#" class="thumbnail">
                                <img src="/webpage/plug-in/imgs/profile-pic.jpg" alt="头像">
                            </a>
                        </div>
                        <div class="col-md-6">
                            <div class="jumbotron">
                                <form id="formChangeAuthorPwd" action="/author/userController.author?doChangeAuthorPwdByPwd">
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-xs-6">
                                                <lable class="control-label" style="color:#8a6d3b;">
                                                    账户名称：
                                                </lable>
                                            </div>
                                            <div class="col-xs-6">
                                                <span class="control-label" data-bind="text:account"></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-xs-6">
                                                <lable class="control-label" style="color:#8a6d3b;">
                                                    输入原始密码：
                                                </lable>
                                            </div>
                                            <div class="col-xs-6">
                                                <input type="password" class="control-label" name="oldPwd">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-xs-6">
                                                <lable class="control-label" style="color:#8a6d3b;">
                                                    输入新密码：
                                                </lable>
                                            </div>
                                            <div class="col-xs-6">
                                                <input type="password" class="control-label" name="newPwd">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-xs-6">
                                                <lable class="control-label" style="color:#8a6d3b;">
                                                    重新输入新密码：
                                                </lable>
                                            </div>
                                            <div class="col-xs-6">
                                                <input type="password" class="control-label" name="reRwd">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-xs-6">
                                            </div>
                                            <div class="col-xs-6">
                                                <input type="submit" class="btn btn-primary" value="确认修改">
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">
                    <div class="row">
                        <div class="col-md-2 col-md-offset-1">
                            <div class="btn-group">
                                <a href="/author/pageController.author?main" target="_parent" class="btn btn-default">返回主页</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div style="height: 100px;"></div>
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
</div>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 2017/6/28
  Time: 21:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/webpage/context/baseTags.jsp" %>
<html>
<head>
    <title>Seller Assistant</title>
    <meta charset="utf-8"/>
    <link rel="shortcut icon" type="image/x-icon" href="/webpage/plug-in/imgs/favicon.ico" media="screen"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta http-equiv="X-UA-Compatible" content="IE=9"/>
    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/font-awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/toastr/toastr.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrapvalidator/dist/css/bootstrapValidator.min.css"/>
    <link rel="stylesheet" href="/webpage/pages/main/index.css"/>
    <!--[if lte IE 9]>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <script src="https://cdn.bootcss.com/html5shiv/r29/html5.js"></script>
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <![endif]-->
    <script type="text/javascript" src="/webpage/plug-in/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/toastr/toastr.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrapvalidator/dist/js/language/zh_CN.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/toastr/toastr.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/knockoutjs/dist/knockout.js"></script>
    <script type="text/javascript" src="/webpage/pages/user/changePwd.js"></script>
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
                                    业务客服&nbsp;<img border="0" src="http://wpa.qq.com/pa?p=2:282208271:51" alt="客服1" title="客服1"/>
                                </a>
                            </li>
                            <li>
                                <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=331219046&site=qq&menu=yes">
                                    技术支持&nbsp;<img border="0" src="http://wpa.qq.com/pa?p=2:331219046:51" alt="技术支持" title="技术支持"/>
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
                                <form id="formobj" action="/userController.do?doChangeUserPwdByPwd" onsubmit="return false;">
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
                                                <input type="password" class="control-label" name="pwd">
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
                                                <input type="password" class="control-label" name="reRwd" id="reRwd">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-xs-6">
                                            </div>
                                            <div class="col-xs-6">
                                                <input type="submit" onclick="changeUserPwd();" class="btn btn-primary" value="确认修改">
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
                                <a href="/mainController.do?index" class="btn btn-default">返回主页</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div style="height: 100px;"></div>
    </div>
</div>
</body>
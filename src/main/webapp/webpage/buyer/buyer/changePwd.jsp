<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/webpage/context/baseTags.jsp" %>
<html>
<head>
    <title>Review Tracker</title>
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
    <script type="text/javascript" src="/webpage/plug-in/bootstrapvalidator/dist/js/language/en_US.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/toastr/toastr.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/knockoutjs/dist/knockout.js"></script>
    <script type="text/javascript" src="/webpage/buyer/buyer/changePwd.js"></script>
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
                <a class="navbar-brand font-color" href="/buyerUserController.buyer?index" target="_parent">Review Tracker</a>
            </div>
            <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle " data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">My account<span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="/userPageController.buyer?goAccountDetails" target="_parent">Account details</a></li>
                            <li><a href="/userPageController.buyer?goChangePwd" target="_parent">Reset password</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="/buyerUserController.buyer?doLogOff" target="_parent">
                            Log off
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
                    <h3 class="panel-title">Reset password</h3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-2 col-md-offset-2">
                            <div style="height: 30px;"></div>
                            <a href="#" class="thumbnail">
                                <img src="/webpage/plug-in/imgs/profile-pic.jpg" alt="thumbnail">
                            </a>
                        </div>
                        <div class="col-md-6">
                            <div class="jumbotron">
                                <form id="formObj" action="/buyerUserController.buyer?doChangePwd" onsubmit="return false;">
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <lable class="control-label" style="color:#8a6d3b;">
                                                    Email address：
                                                </lable>
                                            </div>
                                            <div class="col-md-4">
                                                <span class="control-label" data-bind="text:account"></span>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <lable class="control-label" style="color:#8a6d3b;">
                                                    Old password ：
                                                </lable>
                                            </div>
                                            <div class="col-md-4">
                                                <input type="password" class="control-label" name="oldPwd">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-md-4">
                                                <lable class="control-label" style="color:#8a6d3b;">
                                                    New password：
                                                </lable>
                                            </div>
                                            <div class="col-md-4">
                                                <input type="password" class="control-label" name="newPwd">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-md-4">
                                            </div>
                                            <div class="col-md-4">
                                                <input type="submit" onclick="changeUserPwd();" class="btn btn-primary" value="Submit">
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
                                <a href="/buyerUserController.buyer?index" class="btn btn-default">Go to homepage</a>
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
                        Copyright&copy;Viscal Technology Services Ltd All Rights Reserved&reg;鄂ICP备17013383号
                    </div>
                </div>
            </div>
        </div>
    </nav>
</div>
</body>
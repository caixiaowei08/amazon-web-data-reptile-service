<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/webpage/context/baseTags.jsp" %>
<!DOCTYPE html>
<head>
    <title>Seller Assistant</title>
    <meta charset="utf-8"/>
    <link rel="shortcut icon" type="image/x-icon" href="/webpage/plug-in/imgs/favicon.ico" media="screen"/>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta http-equiv="X-UA-Compatible" content="IE=9"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0,user-scalable=no"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/font-awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/toastr/toastr.css"/>
    <link rel="stylesheet" href="/webpage/pages/main/index.css"/>
    <link rel="stylesheet" href="/webpage/admin/main/main.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrapvalidator/dist/css/bootstrapValidator.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap-table/dist/bootstrap-table.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/toastr/toastr.css"/>
    <script type="text/javascript" src="/webpage/plug-in/jquery/jquery.min.js"></script>
    <!--[if lte IE 9]>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <script src="https://cdn.bootcss.com/html5shiv/r29/html5.js"></script>
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <![endif]-->
    <script type="text/javascript" src="/webpage/plug-in/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>
    <script type="text/javascript"
            src="/webpage/plug-in/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap-table/dist/bootstrap-table.min.js"></script>
    <script type="text/javascript"
            src="/webpage/plug-in/bootstrap-table/dist/bootstrap-table-locale-all.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap-table/dist/locale/bootstrap-table-zh-CN.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/knockoutjs/dist/knockout.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/toastr/toastr.min.js"></script>
    <script type="text/javascript" src="/webpage/admin/systemConfig/userManage.js"></script>
</head>
<body class="main-container" style="overflow-y:auto;">
<nav class="navbar navbar-default navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only">Seller Assistant管理端</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand font-color" href="/skipController.admin?goToAdminMain" target="_parent">Seller Assistant管理端</a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="/skipController.admin?goToAdminMain" target="_parent">推广订单管理</a></li>
                <li><a href="/evaluateController.admin?goEvaluateDetail" target="_parent">订单评价管理</a></li>
                <li><a href="/skipController.admin?goUserManage" target="_parent">卖家管理</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle " data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">系统设置<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="/skipController.admin?goPriceExchange" target="_parent">价格汇率</a></li>
                        <%--<li><a href="/skipController.admin?goQQContacts" target="_parent">联系人</a></li>--%>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle " data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">我的账号<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="/skipController.admin?goAdminChangePwd" target="_parent">修改密码</a></li>
                    </ul>
                </li>
                <li>
                    <a href="/adminSystemController.admin?doLogOff">
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
            <div class="panel-heading">
                <h3 class="panel-title">卖家管理</h3>
            </div>
            <div class="panel-body">
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <label class="col-xs-4 control-label amazon-label">邮箱账号</label>
                            <div class="col-xs-8">
                                <input class="form-control" id="account" type="text"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="panel-footer">
                <div class="row">
                    <div class="col-sm-4 col-sm-offset-8">
                        <div class="btn-group" role="group">
                            <button type="button" onclick="doUserSearch();" class="btn btn-default"
                                    style="width: 104px;">查询
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <table id="userListTable" class="table table-hover">
        </table>
    </div>
    <div style="height: 100px;"></div>
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
<div class="modal fade" id="vipCharge" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="formObj_submitMemberShipMonth" action="/adminUserMangeController.admin?doChargeVip" onsubmit="return false;">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="myModalLabel">
                        会员充值
                    </h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;" id="asin-addon">卖家编号</span>
                            <input type="text" class="form-control" data-bind="value:sellerId" id="sellerIdVip" name="sellerId" readonly="readonly"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;" id="amzOrderId-addon">邮箱</span>
                            <input type="text" class="form-control"  data-bind="value:account" name="account" placeholder="邮箱" readonly="readonly"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;" id="membershipEndTime-addon">会员到期时间</span>
                            <input type="text" class="form-control" data-bind="value:membershipEndTime" readonly="readonly"  id="membershipEndTime" placeholder="会员到期时间"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;" id="memberShipMonth-addon">充值月数</span>
                            <input type="number" min="1" step="1" class="form-control" data-bind="value:memberShipMonth" id="memberShipMonth" name="memberShipMonth" placeholder="请填写充值月数"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" id="btn_sub_memberShipMonth" onclick="submitMemberShipMonth();" class="btn btn-primary">确定
                    </button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="modal fade" id="fundCharge" tabindex="-1" role="dialog" aria-labelledby=""
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="formObj_submitFundCharge" action="/adminUserMangeController.admin?doFundCharge" onsubmit="return false;">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title">
                        资金充值
                    </h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;">卖家编号</span>
                            <input type="text" class="form-control" data-bind="value:sellerId" name="sellerId" readonly="readonly"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;">邮箱</span>
                            <input type="text" class="form-control"  data-bind="value:account" name="account" placeholder="邮箱" readonly="readonly"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;">账户总额(美元)</span>
                            <input type="text" class="form-control" data-bind="value:totalFund" readonly="readonly" placeholder="账户总额(美元)"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;">可用金额(美元)</span>
                            <input type="text" class="form-control" data-bind="value:usableFund" readonly="readonly"  placeholder="可用金额(美元)"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;">冻结金额(美元)</span>
                            <input type="text" class="form-control" data-bind="value:freezeFund" readonly="readonly"  placeholder="冻结金额(美元)"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;">充值金额(美元)</span>
                            <input type="number" step="1" class="form-control"  data-bind="value:chargeFund" name="chargeFund" placeholder="充值金额(美元)"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" id="btn_sub_fundCharge" onclick="submitFundCharge();" class="btn btn-primary">确定
                    </button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>

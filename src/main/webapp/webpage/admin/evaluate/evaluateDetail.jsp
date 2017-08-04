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
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/font-awesome/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrapvalidator/dist/css/bootstrapValidator.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap-table/dist/bootstrap-table.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/toastr/toastr.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/ace/css/ace.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/rating/css/star-rating.css"/>
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
    <script type="text/javascript"
            src="/webpage/plug-in/bootstrap-table/dist/bootstrap-table-locale-all.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap-table/dist/locale/bootstrap-table-zh-CN.js"></script>
    <script type="text/javascript"
            src="/webpage/plug-in/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript"
            src="/webpage/plug-in/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/rating/js/star-rating.js"></script>
    <script type="text/javascript" src="/webpage/admin/evaluate/evaluateDetail.js"></script>
</head>
<body style="overflow-y:auto;">
<div class="main-container">
    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                        data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                    <span class="sr-only">Review Tracker管理端</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand font-color" href="/skipController.admin?goToAdminMain" target="_parent">
                    Review Tracker管理端</a>
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
                            <li><a href="/skipController.admin?goGeneralManager" target="_parent">普通管理员</a></li>
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
                    <h3 class="panel-title">订单评价管理</h3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-sm-4">
                            <div class="form-group">
                                <label class="col-xs-4 control-label amazon-label" for="amazon_asin">ASIN</label>
                                <div class="col-xs-7">
                                    <input class="form-control" id="amazon_asin" type="text"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4">
                            <div class="form-group">
                                <label class="col-xs-4 control-label amazon-label" for="amazon_amzOrderId">亚马逊单号</label>
                                <div class="col-xs-7">
                                    <input class="form-control" id="amazon_amzOrderId" type="text"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4">
                            <div class="form-group">
                                <label class="col-xs-4 control-label amazon-label" for="amazon_asin">状态</label>
                                <div class="col-xs-7">
                                    <select id="amazon_state" class="form-control">
                                        <option value="">--选择--</option>
                                        <option value="1">pending</option>
                                        <option value="2">review</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-4">
                            <div class="form-group">
                                <label class="col-xs-4 control-label amazon-label" for="amazon_promoteId">订单编号</label>
                                <div class="col-xs-7">
                                    <input class="form-control" id="amazon_promoteId" type="text"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-8">
                            <div class="form-group">
                                <label class="col-xs-2 control-label amazon-label" for="amazon_state">
                                    <nobr>单号提交时间</nobr>
                                </label>
                                <div class="col-xs-4">
                                    <div class="input-group date form_date" id="createTime_begin"
                                         data-date-format="yyyy-mm-dd"
                                         data-link-field="createTime_begin_input" data-link-format="yyyy-mm-dd">
                                        <input class="form-control" name="createTime_begin" id="createTime_begin_value"
                                               size="16" type="text" value="" readonly placeholder="开始时间">
                                        <span class="input-group-addon"><span
                                                class="glyphicon glyphicon-remove"></span></span>
                                        <span class="input-group-addon"><span
                                                class="glyphicon glyphicon-calendar"></span></span>
                                    </div>
                                    <input type="hidden" id="createTime_begin_input" value=""/>
                                </div>
                                <div class="col-xs-4">
                                    <div class="input-group date form_date" id="createTime_end"
                                         data-date-format="yyyy-mm-dd"
                                         data-link-field="createTime_end_input" data-link-format="yyyy-mm-dd">
                                        <input class="form-control" name="createTime_end" id="createTime_end_value"
                                               size="16"
                                               type="text" value="" readonly placeholder="结束时间">
                                        <span class="input-group-addon"><span
                                                class="glyphicon glyphicon-remove"></span></span>
                                        <span class="input-group-addon"><span
                                                class="glyphicon glyphicon-calendar"></span></span>
                                    </div>
                                    <input type="hidden" id="createTime_end_input" value=""/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel-footer">
                    <div class="row">
                        <div class="col-sm-4 col-sm-offset-8">
                            <div class="btn-group" role="group">
                                <a onclick="downEvaluateExcel();" class="btn btn-default" style="width: 104px;">评论导出
                                </a>
                                <a type="button" data-target='#addNewReview' class="btn btn-default" data-toggle='modal'
                                   style="width: 104px;">评论录入
                                </a>
                                <button type="button" onclick="downEvaluateSearch();" class="btn btn-default"
                                        style="width: 104px;">查询
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <table id="evaluateListTable" class="table table-hover">
            </table>
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
<div class="modal fade" id="addNewReview" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="formObj" onsubmit="return doSubmitEvaluate()">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <h4 class="modal-title" id="myModalLabel">
                        评论录入
                    </h4>
                </div>
                <div class="modal-body">
                    <div class="form-group" style="height: 50px;">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;" id="asin-addon">ASIN编号*</span>
                            <input type="text" class="form-control" id="asinId" name="asinId" placeholder="ASIN编号"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group" style="height: 50px;">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;" id="amzOrderId-addon">亚马逊订单号*</span>
                            <input type="text" class="form-control" id="amzOrderId" name="amzOrderId"
                                   placeholder="亚马逊订单号"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group has-warning" style="height: 50px;">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;" id="reviewUrl-addon">亚马逊评论链接</span>
                            <input type="text" class="form-control" id="reviewUrl" name="reviewUrl"
                                   placeholder="亚马逊评论链接"
                                   aria-describedby="basic-addon1">
                        </div>
                        <small class="help-block">若填写亚马逊评论链接 则必填 微信 支付宝 PayPal其中之一</small>
                    </div>
                    <div class="form-group" style="height: 50px;">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;"
                                  id="weChat-addon">微信&nbsp;&nbsp;￥</span>
                            <input type="number" class="form-control" id="weChat" name="weChat"
                                   placeholder="微信&nbsp;&nbsp;￥"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group" style="height: 50px;">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;" id="zfb-addon">支付宝&nbsp;&nbsp;￥</span>
                            <input type="number" class="form-control" id="zfb" name="zfb"
                                   placeholder="支付宝￥"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;"
                                  id="PayPal-addon">PayPal&nbsp;&nbsp;$</span>
                            <input type="number" class="form-control" id="payPal" name="payPal"
                                   placeholder="PayPal$"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" id="btn_sub" class="btn btn-primary">录入
                    </button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="modal fade bs-example-modal-sm" id="deleteEvaluateModel" tabindex="-1" role="dialog"
     aria-labelledby="deleteEvaluateModel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close"
                        data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title">
                    删除评价
                </h4>
            </div>
            <div class="modal-body">
                <input type="hidden" id="deleteId" data-bind="value:deleteId">
                确认删除评价 <span data-bind="text:deleteId"></span>吗？
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="deleteEvaluateByIdBtn"
                        onclick="deleteEvaluateById();">确定
                </button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="addEvaluateUrlModel" tabindex="-1" role="dialog" aria-labelledby="addEvaluateUrlModel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="formAddEvaluateUrlModel" onsubmit="return submitAddReviewUrl();">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <input type="hidden" name="id" data-bind="value:eid">
                    <h4 class="modal-title">
                        追加评论链接
                    </h4>
                </div>
                <div class="modal-body">
                    <div class="form-group" style="height: 50px;">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;">ASIN编号</span>
                            <input type="text" class="form-control" readonly="readonly" data-bind="value:asinId"
                                   placeholder="ASIN编号"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group" style="height: 50px;">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;">亚马逊订单号</span>
                            <input type="text" class="form-control" readonly="readonly" data-bind="value:amzOrderId"
                                   placeholder="亚马逊订单号"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group" style="height: 50px;">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;">亚马逊评论链接*</span>
                            <input type="text" class="form-control" id="addReviewUrl" name="reviewUrl"
                                   data-bind="value:reviewUrl"
                                   placeholder="亚马逊评论链接"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group" style="height: 50px;">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;"
                                  id="weChat-url-addon">微信&nbsp;&nbsp;￥</span>
                            <input type="number" class="form-control" id="weChat-url" name="weChat"
                                   placeholder="微信&nbsp;&nbsp;￥"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group" style="height: 50px;">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;" id="zfb-url-addon">支付宝&nbsp;&nbsp;￥</span>
                            <input type="number" class="form-control" id="zfb-url" name="zfb"
                                   placeholder="支付宝&nbsp;&nbsp;￥"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;"
                                  id="payPal-url-addon">PayPal&nbsp;&nbsp;$</span>
                            <input type="number" class="form-control" id="payPal-url" name="payPal"
                                   placeholder="PayPal&nbsp;&nbsp;$"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group has-warning">
                        <small class="help-block">必填 微信 支付宝 PayPal 其中之一</small>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button"  id="addEvaluateUrlBtn" class="btn btn-primary">录入
                    </button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="modal fade" id="modifyAmOrderNoModel" tabindex="-1" role="dialog" aria-labelledby="modifyAmOrderNoModel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="formModifyAmOrderNoModel" action="/evaluateController.admin?doOrderNoUpdate"
                  onsubmit="return false;">
                <div class="modal-header">
                    <button type="button" class="close"
                            data-dismiss="modal" aria-hidden="true">
                        &times;
                    </button>
                    <input type="hidden" name="id" data-bind="value:eid">
                    <h4 class="modal-title">
                        评论亚马逊订单号修改
                    </h4>
                </div>
                <div class="modal-body">
                    <div class="form-group" style="height: 50px;">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;">ASIN编号</span>
                            <input type="text" class="form-control" readonly="readonly" data-bind="value:asinId"
                                   placeholder="ASIN编号"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group" style="height: 50px;">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;">亚马逊订单号</span>
                            <input type="text" class="form-control" name="amzOrderId" data-bind="value:amzOrderId"
                                   placeholder="亚马逊订单号"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="input-group" style="width: 100%;">
                            <span class="input-group-addon" style="width:150px;">亚马逊评论链接*</span>
                            <input type="text" class="form-control" readonly="readonly" name="reviewUrl"
                                   data-bind="value:reviewUrl"
                                   placeholder="亚马逊评论链接"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" onclick="submitModifyAmOrderNo();" class="btn btn-primary">修改订单编号
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
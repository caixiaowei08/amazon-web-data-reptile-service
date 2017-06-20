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
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap-table/dist/bootstrap-table.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css"/>
    <link rel="stylesheet" href="/webpage/pages/manage/managePromot.css"/>
    <link rel="stylesheet" href="/webpage/pages/main/index.css"/>
    <script type="text/javascript" src="/webpage/plug-in/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap-table/dist/bootstrap-table.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap-table/dist/bootstrap-table-locale-all.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap-table/dist/locale/bootstrap-table-zh-CN.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap-datetimepicker/js/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap-datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>

</head>
<body>
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
                           aria-expanded="false">我的推广活动 <span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li>
                                <a href="promotOrderController.do?goNewPromotOne" target="_parent">新建推广活动</a>
                            </li>
                            <li>
                                <a href="redirectionController.do?goManagePromot" target="_parent">推广活动管理</a>
                            </li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle " data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">充值<span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <li><a href="promotOrderController.do?goNewPromotOne">保证金充值</a></li>
                            <li><a href="promotOrderController.do?goNewPromotOne">购买会员</a></li>
                            <li><a href="redirectionController.do?goManagePromot">充值记录</a></li>
                        </ul>
                    </li>
                    <li><a href="#">我的账户</a></li>
                </ul>
            </div>
        </div>
    </nav>
    <div class="main-content">
        <div style="height: 20px;"></div>
        <div class="container">
            <div class="panel panel-warning">
                <div class="panel-heading">
                    <h3 class="panel-title">推广活动管理</h3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-sm-3">
                            <div class="form-group">
                                <label class="col-xs-3 control-label amazon-label" for="amazon_asin">ASIN</label>
                                <div class="col-xs-8">
                                    <input class="form-control" id="amazon_asin" type="text"/>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-3">
                            <div class="form-group">
                                <label class="col-xs-3 control-label amazon-label" for="amazon_state"><nobr>状态</nobr></label>
                                <div class="col-xs-8">
                                    <select id="disabledSelect" id="amazon_state" class="form-control">
                                        <option>--选择--</option>
                                        <option>开启</option>
                                        <option>关闭</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="col-sm-6">
                            <div class="form-group">
                                <label class="col-xs-2 control-label amazon-label" for="amazon_state"><nobr>创建时间</nobr></label>
                                <div class="col-xs-5">
                                    <div class="input-group date form_date" id="addDate_begin" data-date-format="yyyy-mm-dd"
                                         data-link-field="addDate_begin_input" data-link-format="yyyy-mm-dd">
                                        <input class="form-control" name="addDate_begin" id="addDate_begin_value" size="16" type="text" value="" readonly>
                                        <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                    </div>
                                    <input type="hidden" id="addDate_begin_input" value=""/><br/>
                                </div>
                                <div class="col-xs-5">
                                    <div class="input-group date form_date" id="addDate_end" data-date-format="yyyy-mm-dd"
                                         data-link-field="addDate_end_input" data-link-format="yyyy-mm-dd">
                                        <input class="form-control" name="addDate_end" id="addDate_end_value" size="16" type="text" value="" readonly>
                                        <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                    </div>
                                    <input type="hidden" id="addDate_end_input" value=""/><br/>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="panel-footer">
                    <div class="row">
                        <div class="col-sm-4 col-sm-offset-8">
                            <div class="btn-group" role="group">
                                <%--<button type="button" class="btn btn-default">重置</button>--%>
                                <button type="button" onclick="doPromotSearch()" class="btn btn-default">查询</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <table id="promotListTable" class="table table-hover">
            </table>
        </div>
    </div>
</div>
</body>
<script src="/webpage/pages/manage/managePromot.js"></script>
</html>
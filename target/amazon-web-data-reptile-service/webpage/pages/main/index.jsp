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
    <link rel="stylesheet" href="/webpage/plug-in/toastr/toastr.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/ace/css/ace.css"/>
    <link rel="stylesheet" href="/webpage/pages/main/index.css"/>
    <script type="text/javascript" src="/webpage/plug-in/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/webpage/pages/main/index.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/toastr/toastr.min.js"></script>
    <!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
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
                            <li><a href="/promotOrderController.do?goNewPromotOne" target="_parent">新建推广活动</a>
                            </li>
                            <li><a href="/redirectionController.do?goManagePromot" target="_parent">推广活动管理</a></li>
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
                    <li>
                        <a href="/userController.do?doLogOff">
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
                    <h3 class="panel-title">亚马逊推广平台</h3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="space-6"></div>
                        <div class="col-sm-7 infobox-container">
                            <div class="infobox infobox-green">
                                <div class="infobox-icon">
                                    <i class="ace-icon fa fa-shopping-cart"></i>
                                </div>
                                <div class="infobox-data">
                                    <span class="infobox-data-number">8</span>
                                    <a href="/redirectionController.do?goManagePromot" target="_parent" class="infobox-content">我的活动订单</a>
                                </div>
                                <%--<div class="stat stat-important">4%</div>--%>
                            </div>
                            <div class="infobox infobox-pink">
                                <div class="infobox-icon">
                                    <i class="ace-icon fa fa-comments"></i>
                                </div>
                                <div class="infobox-data">
                                    <span class="infobox-data-number">32</span>
                                    <a href="" class="infobox-content">今日评论</a>
                                </div>
                                <%--<div class="stat stat-success">8%</div>--%>
                            </div>
                            <div class="infobox infobox-blue">
                                <div class="infobox-icon">
                                    <i class="ace-icon fa fa-amazon"></i>
                                </div>
                                <div class="infobox-data">
                                    <span class="infobox-data-number">11</span>
                                    <a href="" class="infobox-content">联系买家</a>
                                </div>
                            </div>
                            <div class="infobox infobox-red">
                                <div class="infobox-icon">
                                    <i class="ace-icon fa fa-pencil-square-o"></i>
                                </div>
                                <div class="infobox-data">
                                    <span class="infobox-data-number">523</span>
                                    <a href="" class="infobox-content">总好评数</a>
                                </div>
                            </div>
                            <div class="infobox infobox-orange2">
                                <div class="infobox-icon">
                                    <i class="ace-icon fa fa-history"></i>
                                </div>
                                <div class="infobox-data">
                                    <span class="infobox-data-number">6,251</span>
                                    <a href="" class="infobox-content">完成的推广</a>
                                </div>
                            </div>
                            <div class="infobox infobox-blue2">
                                <div class="infobox-icon">
                                    <i class="ace-icon fa fa-file-archive-o" aria-hidden="true"></i>
                                </div>
                                <div class="infobox-data">
                                    <span class="infobox-data-number">156</span>
                                    <a href="" class="infobox-content">消费明细</a>
                                </div>
                            </div>
                            <div class="space-6"></div>
                            <div class="space-6"></div>
                            <div class="space-6"></div>
                            <div class="space-6"></div>
                            <div class="hr hr2 hr-double"></div>
                            <div class="space-6"></div>
                            <div class="space-6"></div>
                            <div class="space-6"></div>
                            <div class="space-6"></div>
                            <button type="button" onclick="newPromotOrder();" class="btn btn-sm btn-primary btn-white btn-round">
                                <i class="ace-icon fa fa-rss bigger-150 middle orange2"></i>
                                <span class="bigger-110">新建推广活动</span>
                                <i class="icon-on-right ace-icon fa fa-arrow-right"></i>
                            </button>
                        </div>
                        <div class="vspace-12-sm"></div>
                        <div class="col-sm-5">
                            <div class="widget-box">
                                <div class="widget-header widget-header-flat widget-header-small">
                                    <h5 class="widget-title">
                                        <i class="ace-icon fa fa-signal"></i>
                                        我的推广账号
                                    </h5>
                                </div>
                                <div class="widget-body">
                                    <div class="widget-main">
                                        <ul class="list-group">
                                            <li class="list-group-item">
                                                <div class="row">
                                                    <div class="col-xs-5"><span style="color:#555;">账户名称:</span></div>
                                                    <div class="col-xs-7"><span>1005814292@qq.com</span></div>
                                                </div>
                                            </li>
                                            <li class="list-group-item">
                                                <div class="row">
                                                    <div class="col-xs-5"><span style="color:#555;">账户余额(美元)：</span></div>
                                                    <div class="col-xs-7"><span style="color:#0bb8e3;">$131</span></div>
                                                </div>
                                            </li>
                                        </ul>
                                        <div class="input-group">
                                            <span class="input-group-addon" id="sizing-addon3">保证金充值(美元)</span>
                                            <input type="number" min="1" class="form-control" placeholder="充值金额" aria-describedby="sizing-addon1" value="100">
                                            <span class="input-group-addon" id="basic-addon4">充值</span>
                                        </div>
                                        <div class="hr hr8 hr-double"></div>
                                        <div class="input-group">
                                            <span class="input-group-addon" id="sizing-addon1">会员到期时间</span>
                                            <input type="text" class="form-control" placeholder="会员到期时间" aria-describedby="sizing-addon1" value="2018-09-18">
                                            <span class="input-group-addon" id="basic-addon2">续期</span>
                                        </div>
                                    </div><!-- /.widget-main -->
                                </div><!-- /.widget-body -->
                            </div><!-- /.widget-box -->
                        </div><!-- /.col -->
                    </div><!-- /.row -->
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
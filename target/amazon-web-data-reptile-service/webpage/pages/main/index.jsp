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
    <link rel="stylesheet" href="/webpage/plug-in/bootstrapvalidator/dist/css/bootstrapValidator.min.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/toastr/toastr.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/ace/css/ace.css"/>
    <link rel="stylesheet" href="/webpage/pages/main/index.css"/>
    <!--[if lte IE 9]>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <script src="https://cdn.bootcss.com/html5shiv/r29/html5.js"></script>
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <![endif]-->
    <script type="text/javascript" src="/webpage/plug-in/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/webpage/pages/main/index.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/toastr/toastr.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/knockoutjs/dist/knockout.js"></script>
</head>
<body style="overflow-y:auto;">
<div class="main-container" >
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
                                <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=414068730&site=qq&menu=yes">
                                    客服1&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img border="0" src="http://wpa.qq.com/pa?p=2:414068730:51" alt="客服1" title="客服1"/>
                                </a>
                            </li>
                            <li>
                                <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=348759523&site=qq&menu=yes">
                                    客服2&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img border="0" src="http://wpa.qq.com/pa?p=2:348759523:51" alt="客服2" title="客服2"/>
                                </a>
                            </li>
                            <li>
                                <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=710535607&site=qq&menu=yes">
                                    客服3&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img border="0" src="http://wpa.qq.com/pa?p=2:710535607:51" alt="客服3" title="客服2"/>
                                </a>
                            </li>
                            <li>
                                <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=1005814292&site=qq&menu=yes">
                                    技术支持&nbsp;<img border="0" src="http://wpa.qq.com/pa?p=2:1005814292:51" alt="技术支持" title="技术支持"/>
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
                    <h3 class="panel-title">亚马逊推广平台</h3>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="space-6"></div>
                        <div class="col-sm-7 infobox-container">
                            <a href="/redirectionController.do?goManagePromot" target="_parent">
                                <div class="infobox infobox-green btn-a-infobox">
                                        <div class="infobox-icon">
                                            <i class="ace-icon fa fa-shopping-cart"></i>
                                        </div>
                                       <div class="infobox-data">
                                            <span class="infobox-data-number" data-bind="text:activeOrderNum">0</span>
                                            <div
                                               class="infobox-content">活动订单</div>
                                       </div>
                                </div>
                            </a>
                            <div class="infobox infobox-pink">
                                <div class="infobox-icon">
                                    <i class="ace-icon fa fa-comments"></i>
                                </div>
                                <div class="infobox-data">
                                    <span class="infobox-data-number" data-bind="text:todayEvaluateNum">0</span>
                                    <lable href="" class="infobox-content">今日评论</lable>
                                </div>
                            </div>
                            <div class="infobox infobox-blue">
                                <div class="infobox-icon">
                                    <i class="ace-icon fa fa-amazon"></i>
                                </div>
                                <div class="infobox-data">
                                    <span class="infobox-data-number" data-bind="text:buyerNum">0</span>
                                    <lable class="infobox-content">联系买家</lable>
                                </div>
                            </div>
                            <div class="infobox infobox-red">
                                <div class="infobox-icon">
                                    <i class="ace-icon fa fa-pencil-square-o"></i>
                                </div>
                                <div class="infobox-data">
                                    <span class="infobox-data-number" data-bind="text:totalEvaluateNum">0</span>
                                    <lable class="infobox-content">总好评数</lable>
                                </div>
                            </div>
                            <div class="infobox infobox-orange2">
                                <div class="infobox-icon">
                                    <i class="ace-icon fa fa-history"></i>
                                </div>
                                <div class="infobox-data">
                                    <span class="infobox-data-number" data-bind="text:historyOrderNum">0</span>
                                    <lable class="infobox-content">完成推广</lable>
                                </div>
                            </div>
                            <div class="infobox infobox-blue2">
                                <div class="infobox-icon">
                                    <i class="ace-icon fa fa-file-archive-o" aria-hidden="true"></i>
                                </div>
                                <div class="infobox-data">
                                    <span class="infobox-data-number" data-bind="text:totalConsumption">$0.00</span>
                                    <lable class="infobox-content">全部消费(美元)</lable>
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
                            <button type="button" onclick="newPromotOrder();"
                                    class="btn btn-sm btn-primary btn-white btn-round">
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
                                                    <div class="col-xs-7"><span data-bind="text:account"></span></div>
                                                </div>
                                            </li>
                                            <li class="list-group-item">
                                                <div class="row">
                                                    <div class="col-xs-5"><span style="color:#555;">账户余额(美元):</span>
                                                    </div>
                                                    <div class="col-xs-7"><span style="color:#0bb8e3;">$<span
                                                            data-bind="text:totalFund"></span></span></div>
                                                </div>
                                            </li>
                                            <li class="list-group-item">
                                                <div class="row">
                                                    <div class="col-xs-5"><span style="color:#555;">可用余额(美元):</span>
                                                    </div>
                                                    <div class="col-xs-7"><span style="color:#0bb8e3;">$<span
                                                            data-bind="text:usableFund"></span></span></div>
                                                </div>
                                            </li>
                                            <li class="list-group-item">
                                                <div class="row">
                                                    <div class="col-xs-5"><span style="color:#555;">冻结金额(美元):</span>
                                                    </div>
                                                    <div class="col-xs-7"><span style="color:#0bb8e3;">$<span
                                                            data-bind="text:freezeFund"></span></span></div>
                                                </div>
                                            </li>
                                            <li class="list-group-item">
                                                <div class="row">
                                                    <div class="col-xs-5"><span style="color:#555;">会员标识:</span>
                                                    </div>
                                                    <div class="col-xs-7">
                                                        <span>
                                                            <i data-bind="style:{ color: vip() ? 'red' : 'black' }" class="fa fa-vimeo"></i>
                                                        </span>
                                                    </div>
                                                </div>
                                            </li>
                                        </ul>
                                        <div class="hr hr8 hr-double"></div>
                                        <!-- ko if: beforeVip -->
                                        <div class="input-group">
                                            <span class="input-group-addon">会员到期时间</span>
                                            <input type="text" readonly="readonly" class="form-control" placeholder="会员到期时间"
                                                   data-bind="value:membershipEndTime">
                                        </div>
                                        <div style="height: 10px;"></div>
                                        <!-- /ko -->
                                        <!-- ko ifnot: vip -->
                                        <div class="btn-group btn-group-justified" role="group" aria-label="...">
                                            <div class="btn-group" role="group">
                                                <button type="button" onclick="goToChargeMemberShip();"  class="btn btn-danger">立即开通会员</button>
                                            </div>
                                            <div class="btn-group" role="group">
                                                <button type="button" onclick="goToChargeFund();" class="btn btn-default">余额充值</button>
                                            </div>
                                        </div>
                                        <!-- /ko -->
                                        <!-- ko if: vip -->
                                        <div class="btn-group btn-group-justified" role="group" aria-label="...">
                                            <div class="btn-group" role="group">
                                                <button type="button" onclick="goToChargeMemberShip();"  class="btn btn-default">会员续期</button>
                                            </div>
                                            <div class="btn-group" role="group">
                                                <button type="button" onclick="goToChargeFund();" class="btn btn-default">余额充值</button>
                                            </div>
                                        </div>
                                        <!-- /ko -->
                                    </div><!-- /.widget-main -->
                                </div><!-- /.widget-body -->
                            </div><!-- /.widget-box -->
                        </div><!-- /.col -->
                    </div><!-- /.row -->
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
                    Copyright&copy;武汉市维斯卡尔技术服务有限公司 &reg;鄂ICP备17013383号
                </div>
            </div>
        </div>
    </div>
</nav>
</body>
</html>
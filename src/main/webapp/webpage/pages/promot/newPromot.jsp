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
    <link rel="stylesheet" href="/webpage/pages/main/index.css"/>
    <link rel="stylesheet" href="/webpage/plug-in/bootstrapvalidator/dist/css/bootstrapValidator.min.css"/>
    <script type="text/javascript" src="/webpage/plug-in/jquery/jquery.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrapvalidator/dist/js/bootstrapValidator.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/toastr/toastr.min.js"></script>
    <script type="text/javascript" src="/webpage/plug-in/bootstrap/js/jquery.bootstrap.wizard.min.js"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">
    <div id="rootwizard">
        <div class="navbar">
            <div class="navbar-inner">
                <div class="container">
                    <ul>
                        <li><a href="#tab1" data-toggle="tab">First</a></li>
                        <li><a href="#tab2" data-toggle="tab">Second</a></li>
                        <li><a href="#tab3" data-toggle="tab">Third</a></li>
                        <li><a href="#tab4" data-toggle="tab">Forth</a></li>
                        <li><a href="#tab5" data-toggle="tab">Fifth</a></li>
                        <li><a href="#tab6" data-toggle="tab">Sixth</a></li>
                        <li><a href="#tab7" data-toggle="tab">Seventh</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div id="bar" class="progress progress-striped active">
            <div class="progress-bar"></div>
        </div>
        <div class="tab-content">
            <div class="tab-pane" id="tab1">
                1
            </div>
            <div class="tab-pane" id="tab2">
                2
            </div>
            <div class="tab-pane" id="tab3">
                3
            </div>
            <div class="tab-pane" id="tab4">
                4
            </div>
            <div class="tab-pane" id="tab5">
                5
            </div>
            <div class="tab-pane" id="tab6">
                6
            </div>
            <div class="tab-pane" id="tab7">
                7
            </div>
            <ul class="pager wizard">
                <li class="previous first" style="display:none;"><a href="javascript:;">First</a></li>
                <li class="previous"><a href="javascript:;">Previous</a></li>
                <li class="next last" style="display:none;"><a href="javascript:;">Last</a></li>
                <li class="next"><a href="javascript:;">Next</a></li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>
<script>
    $(document).ready(function () {
        $('#rootwizard').bootstrapWizard({
            onTabShow: function (tab, navigation, index) {
                var $total = navigation.find('li').length;
                var $current = index + 1;
                var $percent = ($current / $total) * 100;
                $('#rootwizard').find('.progress-bar').css({width: $percent + '%'});
            }
        });
    });
</script>

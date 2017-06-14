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
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="http://apps.bdimg.com/libs/html5shiv/3.7/html5shiv.min.js"></script>
    <script src="http://apps.bdimg.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div class="container">
    <div style="height: 20px;"></div>
    <div class="panel panel-warning">
            <div class="panel-heading">
                <h3 class="panel-title">新建推广活动</h3>
            </div>
            <div class="panel-body">
                <form id="formObj" onsubmit="return doDealAsinOrUrl()">
                    <h5>
                        <strong>第一步</strong>:&nbsp;&nbsp;输入亚马逊商品ASIN编码
                    </h5>
                    <div style="height: 10px;"></div>
                    <div class="form-group">
                        <div class="input-group">
                            <span class="input-group-addon" id="basic-addon1">ASIN编码：</span>
                            <input type="text" class="form-control" id="asinOrUrl" name="asinOrUrl" placeholder="请输入亚马逊ASIN或者商品主页链接！"
                                   aria-describedby="basic-addon1">
                        </div>
                    </div>
                </form>
            </div>
            <div style="height: 20px;"></div>
            <div class="panel-footer">
                <div class="container">
                    <div class="row">
                        <div class="col-md-2 col-md-offset-8">
                            <div class="btn-group" role="group">
                                <button type="button" id="btn_sub" class="btn btn-default">下一步</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
    </div>
</div>
</body>
</body>
</html>
<script>
    $(function () {
        $('#formObj').bootstrapValidator({
            framework: 'bootstrap',
            icon: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                asinOrUrl: {
                    validators: {
                        notEmpty: {
                            message: '请输入亚马逊ASIN或者商品主页链接！'
                        }
                    }
                }
            }
        });
    });

    $("#btn_sub").click(function(){
        $('#formObj').submit();
    });

    function doDealAsinOrUrl() {
        var asinOrUrl = $("#asinOrUrl").val();
        console.log(asinOrUrl);

        if (asinOrUrl === null || asinOrUrl === undefined || asinOrUrl === '') {
            console.log("true");
            return;
        }
        $.ajax({
            url:"/promotOrderController.do?doDealAsinOrUrl",
            type:'post',
            data:$('#formObj').serialize(),
            success:function(data){
                if(data.success==="success"){
                    toastr.success("新增成功！");
                }else{
                    toastr.error(data.msg);
                }
            },
            error:function(jqxhr,textStatus,errorThrow){
                toastr.success("服务器异常,请联系管理员！");
                console.log(errorThrow);
            },
            complete:function () {
                $("#addAdCarousel").modal("hide");
            },
            statusCode:{
                404:function(){console.log('not found');},
                500:function(){console.log('error by server');},
            }
        });
    }
</script>

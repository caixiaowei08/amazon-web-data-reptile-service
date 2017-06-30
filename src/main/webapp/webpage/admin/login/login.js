/**
 * Created by User on 2017/6/22.
 */
$(function () {
    $('#formObj').bootstrapValidator({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            account: {
                validators: {
                    notEmpty: {
                        message: '登录账号不能为空'
                    }
                }
            },
            pwd: {
                validators: {
                    notEmpty: {
                        message: '请输入登录密码！'
                    }
                }
            }
        }
    });
})

function loginAdmin() {
    $('#formObj').submit();
}

function doAdminLogin() {
    var account = $("#account").val();
    var pwd = $("#pwd").val();
    if (
        account === null || account === undefined || account === ''||
        pwd === null || pwd === undefined || pwd === ''
    ) {
        return;
    }

    $.ajax({
        url:"/adminSystemController.admin?doLogin",
        type:'post',
        beforeSend:beforeSend,
        data:$('#formObj').serialize(),
        success:function(data){
            if(data.success === "success"){
                window.location='/skipController.admin?goToAdminMain'
            }else{
                toastr.error(data.msg);
            }
        },
        error:function(jqxhr,textStatus,errorThrow){
            toastr.success("服务器异常,请联系管理员！");
        },
        complete:function () {
            SendComplete();
        }
    });

}

function beforeSend() {
    $("#loginSubmit").addClass("disabled"); // Disables visually
    $("#loginSubmit").prop("disabled", true); // Disables visually + functionally
}

function SendComplete() {
    $("#loginSubmit").removeClass("disabled"); // Disables visually
    $("#loginSubmit").prop("disabled", false); // Disables visually + functionally
}
/**
 * Created by User on 2017/6/28.
 */
$(function () {
    $('#formobj').bootstrapValidator({
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
                        message: '请输入你的登录账号'
                    }
                }
            },
            pwd: {
                validators: {
                    notEmpty: {
                        message: '请输入你的登录密码'
                    }, stringLength: {
                        min: 6,
                        max: 30,
                        message: '密码长度必须在6到30之间'
                    },  regexp:{
                        regexp:/^[a-zA-Z0-9_\.]+$/,
                        message:'密码由数字字母下划线和.组成'
                    }
                }
            },
            reRwd:{
                validators:{
                    notEmpty: {
                        message: '重复密码不能为空'
                    },
                    identical:{
                        field:'pwd',
                        message:'两次密码输入不一致'
                    }
                }
            }
        }
    }).on('success.form.bv',function (e) {
        console.log('success.form.bv');
        var form = $('#formobj');
        $.post(form.attr('action'),form.serialize(),function (result) {
            if (result.success == "success") {
                toastr.success(result.msg);
                setTimeout("window.location='/loginController.do?login'", 500);
            } else if (result.success == "fail") {
                toastr.warning(result.msg);
                form.bootstrapValidator('disableSubmitButtons', false);
            }
        },'json');
    })
})

function loginRegister() {
    $('#formobj').submit();
}
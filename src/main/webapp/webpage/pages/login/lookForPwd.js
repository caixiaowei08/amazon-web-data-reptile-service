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
            emailCode: {
                validators: {
                    notEmpty: {
                        message: '验证码不能为空！'
                    },
                    regexp: {
                        regexp: /^\d{4}$/,
                        message: '请输入4位数字验证码!'
                    }
                }
            },
            pwd: {
                validators: {
                    notEmpty: {
                        message: '请输入你的登录密码'
                    },
                    stringLength: {
                        min: 6,
                        max: 30,
                        message: '密码长度必须在6到30之间'
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_\.]+$/,
                        message: '密码由数字字母下划线和.组成'
                    }
                }
            },
            reRwd: {
                validators: {
                    notEmpty: {
                        message: '重复密码不能为空'
                    },
                    identical: {
                        field: 'pwd',
                        message: '两次密码输入不一致'
                    }
                }
            }
        }
    }).on('success.form.bv', function (e) {
        var form = $('#formobj');
        $.post(form.attr('action'), form.serialize(), function (result) {
            if (result.success == "success") {
                toastr.success(result.msg);
                setTimeout("window.location='/loginController.do?login'", 500);
            } else if (result.success == "fail") {
                toastr.warning(result.msg);
                form.bootstrapValidator('disableSubmitButtons', false);
            }
        }, 'json');
    });

    $("#getEmailCode").click(function () {
        var email = $("#email").val();
        if (!isEmail(email)) {
            toastr.warning("请输入正确的邮箱！");
            return;
        }
        $.ajax({
            type: "post",
            url: "userController.do?sendEmailCode",
            async: true,
            timeout: 10000,
            data: {account: email},
            success: function (res) {
                if (res.success === "success") {
                    toastr.success("发送邮箱验证码成功！");
                } else {
                    toastr.warning(res.msg);
                }
            },
            error: function () {
                toastr.warning("服务器异常，请联系维护人员！");
            },
            complete: function (XMLHttpRequest, textStatus) {
                if (status == 'timeout') {//超时,status还有success,error等值的情况
                    toastr.warning("获取数据超时,请检查网络问题！");
                }
            }
        }, 'json');

        var i = 60;
        $("#getEmailCode").addClass('disabled');
        var flag = setInterval(function () {
            $("#getEmailCode").val(i + "s");
            i--;
            if (i < 0) {
                $("#getEmailCode").removeClass('disabled');
                $("#getEmailCode").val("获取验证码");
                clearInterval(flag);
            }
        }, 1000);
    });

});

function lookForPwdSubmit() {
    $("#formobj").submit();
}

function isEmail(str) {
    var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
    return reg.test(str);
}

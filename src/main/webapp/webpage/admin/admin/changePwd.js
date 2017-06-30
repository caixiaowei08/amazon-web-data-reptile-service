$(function () {
    loadData();
    $('#formobj').bootstrapValidator({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            oldPwd: {
                validators: {
                    notEmpty: {
                        message: '请输入您的原始密码'
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
                        message: '请输入您的新密码'
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
                setTimeout("window.location='/skipController.admin?goToAdminMain'", 1000);
            } else if (result.success === "fail") {
                toastr.warning(result.msg);
                form.bootstrapValidator('disableSubmitButtons', false);
            } else{
                window.location='/adminSystemController.admin?goAdminLogin';
            }
        }, 'json');
    });
});

function changeUserPwd() {
    $('#formobj').submit();
}

var ViewModel = function (account) {
    this.account = ko.observable(account);
};

function loadData() {
    $.ajax({
        url: "/adminSystemController.admin?doGetAdmin",
        type: 'post',
        success: function (data) {
            if (data.success === "success") {
                ko.applyBindings(
                    new ViewModel(
                        data.content.account
                    )
                );
            } else if (data.success == "fail") {
                toastr.warning(data.msg);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin'
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.warning("服务器异常,请联系管理员！");
        }
    });
}
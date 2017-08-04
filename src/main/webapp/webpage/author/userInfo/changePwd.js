$(function () {
    loadData();
    $('#formChangeAuthorPwd').bootstrapValidator({
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
            newPwd: {
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
                        field: 'newPwd',
                        message: '两次密码输入不一致'
                    }
                }
            }
        }
    }).on('success.form.bv', function (e) {
        e.preventDefault();
        var form = $('#formChangeAuthorPwd');
        $.post(form.attr('action'), form.serialize(), function (result) {
            if (result.success == "success") {
                toastr.success(result.msg);
                setTimeout("window.location = '/author/userController.author?login'", 500);
            } else if (result.success === "fail") {
                toastr.error(result.msg);
                form.bootstrapValidator('disableSubmitButtons', false);
            } else{
                window.location = '/author/userController.author?login';
            }
        }, 'json');
    });
    ko.applyBindings(viewModel);
});


var viewModel = {
    account: ko.observable()
}

function loadData() {
    $.ajax({
        url: "/author/userController.author?doGet",
        type: 'post',
        success: function (data) {
            if (data.success === "success") {
                viewModel.account(data.content.account);
            } else if (data.success == "fail") {
                toastr.error(data.msg);
            } else {
                window.location = '/author/userController.author?login';
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.warning("服务器异常,请联系管理员！");
        }
    });
}
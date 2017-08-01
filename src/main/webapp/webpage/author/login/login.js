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
                        message: '请输入你的登录账号！'
                    }
                }
            },
            pwd: {
                validators: {
                    notEmpty: {
                        message: '请输入你的登录密码！'
                    }
                }
            }
        }
    }).on('success.form.bv', function (e) {
        var form = $('#formObj');
        beforeSend();
        $.post(form.attr('action'), form.serialize(), function (result) {
            if (result.success === "success") {
                window.location = '/author/pageController.author?main';
            } else if (result.success === "fail") {
                toastr.warning(result.msg);
                form.bootstrapValidator('disableSubmitButtons', false);
            }
            sendComplete();
        }, 'json');
    })
});

function loginAuthorSubmit() {
    $('#formObj').submit();
    return false;
}

function beforeSend() {
    $("#authorLoginBtn").addClass("disabled"); // Disables visually
    $("#authorLoginBtn").prop("disabled", true); // Disables visually + functionally
}

function sendComplete() {
    $("#authorLoginBtn").removeClass("disabled"); // Disables visually
    $("#authorLoginBtn").prop("disabled", false); // Disables visually + functionally
}
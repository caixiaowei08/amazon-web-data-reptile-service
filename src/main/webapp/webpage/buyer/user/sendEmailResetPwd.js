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
                    }
                }
            },
            pwd: {
                validators: {
                    notEmpty: {
                    }, stringLength: {
                        min: 6,
                        max: 30
                    }
                }
            }
        }
    }).on('success.form.bv',function (e) {
        console.log('success.form.bv');
        var form = $('#formObj');
        $.post(form.attr('action'),form.serialize(),function (result) {
            if (result.success === "success") {
                toastr.success(result.msg);
                var i = 60;
                $("#sendEmailResetPwd").addClass('disabled');
                var flag = setInterval(function () {
                    $("#sendEmailResetPwd").val(i + "s");
                    i--;
                    if (i < 0) {
                        $("#sendEmailResetPwd").removeClass('disabled');
                        form.bootstrapValidator('disableSubmitButtons', false);
                        $("#sendEmailResetPwd").val("Send Password reset email");
                        clearInterval(flag);
                    }
                }, 1000);
            } else if (result.success === "fail") {
                toastr.error(result.msg);
                form.bootstrapValidator('disableSubmitButtons', false);
            }
        },'json');
    })
});

function sendEmailClick() {
    $('#formObj').submit();
}

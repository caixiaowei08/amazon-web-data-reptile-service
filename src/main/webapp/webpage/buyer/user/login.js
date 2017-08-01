/**
 * Created by User on 2017/7/8.
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
                window.location='/userPageController.buyer?index';
            } else if (result.success == "fail") {
                toastr.warning(result.msg);
                form.bootstrapValidator('disableSubmitButtons', false);
            }
        },'json');
    })
});

function loginBuyerSubmit() {
    $('#formObj').submit();
}
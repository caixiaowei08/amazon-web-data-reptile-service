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
                    }
                }
            }
        }
    }).on('success.form.bv',function (e) {
        console.log('success.form.bv');
       var form = $('#formobj');
       $.post(form.attr('action'),form.serialize(),function (result) {
           if (result.success == "success") {
               window.location='/mainController.do?index';
           } else if (result.success == "fail") {
               toastr.warning(result.msg);
               form.bootstrapValidator('disableSubmitButtons', false);
           }
       },'json');
    })
})

function loginSubmit() {
    $('#formobj').submit();
}


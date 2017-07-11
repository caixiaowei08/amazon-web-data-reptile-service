/**
 * Created by User on 2017/7/10.
 */
$(function () {
    loadData();
    $('#formObj').bootstrapValidator({
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
                    }
                }
            },
            pwd: {
                validators: {
                    notEmpty: {
                    },
                    stringLength: {
                        min: 6,
                        max: 30
                    },
                    regexp: {
                        regexp: /^[a-zA-Z0-9_\.]+$/
                    }
                }
            }
        }
    }).on('success.form.bv', function (e) {
        var form = $('#formObj');
        $.post(form.attr('action'), form.serialize(), function (result) {
            if (result.success == "success") {
                toastr.success(result.msg);
                setTimeout("window.location='/userPageController.buyer?index'", 500);
            } else if (result.success === "fail") {
                toastr.warning(result.msg);
                form.bootstrapValidator('disableSubmitButtons', false);
            } else {
                window.location='/userPageController.buyer?login';
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
        url: "/buyerUserController.buyer?doGet",
        type: 'post',
        success: function (data) {
            if (data.success === "success") {
                ko.applyBindings(
                    new ViewModel(
                        data.content.account
                    )
                );
            } else if(data.success === "fail"){
                toastr.error(data.msg);
            } else{
                window.location='/userPageController.buyer?login';
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.warning("error!");
        }
    });
}
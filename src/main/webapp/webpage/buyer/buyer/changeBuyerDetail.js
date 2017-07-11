/**
 * Created by User on 2017/6/28.
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
        }
    }).on('success.form.bv', function (e) {
        var form = $('#formObj');
        $.post(form.attr('action'), form.serialize(), function (result) {
            if (result.success === "success") {
                toastr.success(result.msg);
                setTimeout("window.location='/userPageController.buyer?index'", 500);
            } else if (result.success === "fail") {
                toastr.warning(result.msg);
                form.bootstrapValidator('disableSubmitButtons', false);
            } else{
                window.location='/userPageController.buyer?login';
            }
        }, 'json');
    });
});

function changeBuyerDetailInfo() {
    $('#formObj').submit();
}

var ViewModel = function (account, weChatAccount, paypalAccount, zfbAccount,
                          defaultPaymentAccount) {
    this.account = ko.observable(account);
    this.weChatAccount = ko.observable(weChatAccount);
    this.paypalAccount = ko.observable(paypalAccount);
    this.zfbAccount = ko.observable(zfbAccount);
    this.defaultPaymentAccount = ko.observable(defaultPaymentAccount);
};

function loadData() {
    $.ajax({
        url: "/buyerUserController.buyer?doGet",
        type: 'post',
        success: function (data) {
            if (data.success === "success") {
                ko.applyBindings(
                    new ViewModel(
                        data.content.account,
                        data.content.weChatAccount,
                        data.content.paypalAccount,
                        data.content.zfbAccount,
                        data.content.defaultPaymentAccount
                    )
                );
            } else if (data.success === "fail") {
                toastr.warning(data.msg);
            } else {
                window.location='/userPageController.buyer?login';
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.warning("server error !");
        }
    });
}
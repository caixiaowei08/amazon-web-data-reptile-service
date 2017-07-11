/**
 * Created by User on 2017/6/28.
 */
$(function () {
    loadData();
});

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
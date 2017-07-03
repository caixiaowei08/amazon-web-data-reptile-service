/**
 * Created by User on 2017/6/28.
 */
$(function () {
    loadData();
});

var ViewModel = function (account, totalFund, usableFund, freezeFund,
                          membershipEndTime, memberShipMonthCost, memberShipMonth,
                          vip, beforeVip) {
    this.account = ko.observable(account);
    this.totalFund = ko.observable("$" + totalFund);
    this.usableFund = ko.observable("$" + usableFund);
    this.freezeFund = ko.observable("$" + freezeFund);
    this.membershipEndTime = ko.observable(membershipEndTime);
    this.memberShipMonthCost = ko.observable(memberShipMonthCost);
    this.memberShipMonth = ko.observable(memberShipMonth);
    this.vip = ko.observable(vip);
    this.beforeVip = ko.observable(beforeVip);
};

function loadData() {
    $.ajax({
        url: "/userMembershipController.do?getUserMembershipInfo",
        type: 'post',
        success: function (data) {
            if (data.success === "success") {
                ko.applyBindings(
                    new ViewModel(
                        data.content.account,
                        data.content.totalFund,
                        data.content.usableFund,
                        data.content.freezeFund,
                        data.content.membershipEndTime,
                        data.content.memberShipMonthCost,
                        6,
                        data.content.vip,
                        data.content.beforeVip
                    )
                );
            } else if (data.success === "fail") {
                toastr.warning(data.msg);
            } else {
                window.location='/loginController.do?login';
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.warning("服务器异常,请联系管理员！");
        },
        statusCode: {
            404: function () {
                console.log('not found');
            },
            500: function () {
                console.log('error by server');
            },
        }
    });
}
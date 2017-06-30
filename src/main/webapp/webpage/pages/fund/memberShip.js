/**
 * Created by User on 2017/6/27.
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
            memberShipMonth: {
                validators: {
                    notEmpty: {
                        message: '请输入购买会员月份！'
                    },
                    regexp: {
                        regexp: /^\+?[1-9][0-9]*$/,
                        message: '购买会员月份必须是整数！'
                    }
                }
            }
        }
    });


})

var ViewModel = function (account, totalFund, usableFund, freezeFund,
                          membershipEndTime,memberShipMonthCost,memberShipMonth,
                          vip,beforeVip
) {
    this.account = ko.observable(account);
    this.totalFund = ko.observable(totalFund);
    this.usableFund = ko.observable(usableFund);
    this.freezeFund = ko.observable(freezeFund);
    this.membershipEndTime = ko.observable(membershipEndTime);
    this.memberShipMonthCost = ko.observable(memberShipMonthCost);
    this.memberShipMonth = ko.observable(memberShipMonth);
    this.vip = ko.observable(vip);
    this.beforeVip = ko.observable(beforeVip);
    this.memberShipMonthTotalCost = ko.computed(function () {
        var m = 0,
            s1 = this.memberShipMonth().toString(),
            s2 = this.memberShipMonthCost().toString();
        try {
            m += s1.split(".")[1].length
        } catch (e) {
        }
        try {
            m += s2.split(".")[1].length
        } catch (e) {
        }
        return (Math.ceil((Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)) * 100)) / Math.pow(10, 2)
    }, this);
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
                window.location = '/loginController.do?login';
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.warning("服务器异常,请联系管理员！");
        }
    });
}
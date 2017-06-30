/**
 * Created by User on 2017/6/20.
 */
$(function () {
    loadData();
    $('#chargeFundId').bootstrapValidator({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            chargeFund: {
                validators: {
                    notEmpty: {
                        message: '请输入充值金额！'
                    },
                    regexp: {
                        regexp: /(^[1-9]{1}[0-9]*$)|(^[0-9]*\.[0-9]{2}$)/,
                        message: '充值金额大于0的整数或者保留两位小数的正数！'
                    }
                }
            }
        }
    });
})

function newPromotOrder() {
    window.location = '/promotOrderController.do?goNewPromotOne'
}

var ViewModel = function (account, totalFund, usableFund, freezeFund, vip, beforeVip, membershipEndTime, activeOrderNum, todayEvaluateNum, buyerNum, totalEvaluateNum, historyOrderNum, totalConsumption) {
    this.account = ko.observable(account);
    this.totalFund = ko.observable(totalFund);
    this.usableFund = ko.observable(usableFund);
    this.freezeFund = ko.observable(freezeFund);
    this.vip = ko.observable(vip);
    this.beforeVip = ko.observable(beforeVip);
    this.membershipEndTime = ko.observable(membershipEndTime);
    this.activeOrderNum = ko.observable(activeOrderNum);
    this.todayEvaluateNum = ko.observable(todayEvaluateNum);
    this.buyerNum = ko.observable(buyerNum);
    this.totalEvaluateNum = ko.observable(totalEvaluateNum);
    this.historyOrderNum = ko.observable(historyOrderNum);
    this.totalConsumption = ko.observable(totalConsumption);
};

function loadData() {
    $.ajax({
        url: "/userController.do?doGetBaseUserInfo",
        type: 'post',
        success: function (data) {
            if (data.success === "success") {
                ko.applyBindings(
                    new ViewModel(
                        data.content.account,
                        data.content.totalFund,
                        data.content.usableFund,
                        data.content.freezeFund,
                        data.content.vip,
                        data.content.beforeVip,
                        data.content.membershipEndTime,
                        data.content.activeOrderNum,
                        data.content.todayEvaluateNum,
                        data.content.buyerNum,
                        data.content.totalEvaluateNum,
                        data.content.historyOrderNum,
                        data.content.totalConsumption
                    )
                );
            } else if (data.success === "fail") {
                toastr.warning(data.msg);
            } else {
                window.location = '/loginController.do?login';
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.error("服务器异常,请联系管理员！");
        }
    });
}

/**
 * 资金充值按钮点击
 */
function goToChargeFund() {
    window.location = "/redirectionController.do?goToChargeFund";
}

/**
 * 会员充值按钮点击
 */
function goToChargeMemberShip() {
    window.location = "/redirectionController.do?goToChargeMemberShip";
}

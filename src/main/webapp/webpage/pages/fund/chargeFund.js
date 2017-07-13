/**
 * Created by User on 2017/6/26.
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
            chargeFund: {
                validators: {
                    notEmpty: {
                        message: '请输入充值金额！'
                    },
                    regexp: {
                        regexp: /^[0-9]+([.]{1}[0-9]{1,2})?$/,
                        message: '金额至多保留两位小数的正数!'
                    }
                }
            }
        }
    });
});

var ViewModel = function (account, totalFund, usableFund, freezeFund, exchangeRate, chargeFund) {
    this.account = ko.observable(account);
    this.totalFund = ko.observable(totalFund);
    this.usableFund = ko.observable(usableFund);
    this.freezeFund = ko.observable(freezeFund);
    this.exchangeRate = ko.observable(exchangeRate);
    this.chargeFund = ko.observable(chargeFund);
    this.chargeFundRMB = ko.computed(function () {
        var m = 0,
            s1 = this.chargeFund().toString(),
            s2 = this.exchangeRate().toString();
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
        url: "/userFundController.do?getUserFundInfo",
        type: 'post',
        success: function (data) {
            if (data.success === "success") {
                var chargeFund = 100;
                var chargeFundUrl = getQueryString("chargeFund");
                if( chargeFundUrl !== null && chargeFundUrl !== undefined && chargeFundUrl !== ''){
                    chargeFund = chargeFundUrl.trim();
                }
                ko.applyBindings(
                    new ViewModel(
                        data.content.account,
                        data.content.totalFund,
                        data.content.usableFund,
                        data.content.freezeFund,
                        data.content.exchangeRate,
                        chargeFund
                    )
                );
            } else if (data.success === "fail") {
                toastr.error(data.msg);
            } else {
                window.location = '/loginController.do?login';
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.warning("服务器异常,请联系管理员！");
        }
    });
}

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}
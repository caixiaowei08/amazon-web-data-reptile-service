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

    $("#btn_sub").click(function () {
        console.log("btn_sub");
        $('#formObj').submit();
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
        console.log(s1);
        console.log(s2);
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
                ko.applyBindings(
                    new ViewModel(
                        data.content.account,
                        data.content.totalFund,
                        data.content.usableFund,
                        data.content.freezeFund,
                        data.content.exchangeRate,
                        100
                    )
                );
            } else {
                toastr.error(data.msg);
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.success("服务器异常,请联系管理员！");
            console.log(errorThrow);
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

function beforeSend() {
    $("#btn_sub").addClass("disabled"); // Disables visually
    $("#btn_sub").prop("disabled", true); // Disables visually + functionally
}

function SendComplete() {
    $("#btn_sub").removeClass("disabled"); // Disables visually
    $("#btn_sub").prop("disabled", false); // Disables visually + functionally
}

function doSubmitChargeFund() {

    var chargeFund = $("#chargeFund").val();
    console.log(chargeFund);
    if (chargeFund === null || chargeFund === undefined || chargeFund === ''){
        return;
    }
    var re = new RegExp(/^[0-9]+([.]{1}[0-9]{1,2})?$/);
    if(!re.test(chargeFund)) {
        console.log(re.test(chargeFund));
        return;
    }

    $.ajax({
        url: "/userFundController.do?doAlipayTradePagePay",
        type: 'post',
        beforeSend: beforeSend,
        data: $('#formObj').serialize(),
        success: function (data) {
            if (data.success === "success") {
                window.location = '/redirectionController.do?goManagePromot'
            } else {
                toastr.error(data.msg);
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.success("服务器异常,请联系管理员！");
        },
        complete: function () {
            SendComplete();
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
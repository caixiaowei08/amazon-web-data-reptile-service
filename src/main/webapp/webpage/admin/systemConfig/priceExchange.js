/**
 * Created by User on 2017/7/3.
 */
$(function () {
    loadData();
    $('#formobj').bootstrapValidator({
        framework: 'bootstrap',
        icon: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            monthRent: {
                validators: {
                    notEmpty: {
                        message: '请输入会员月租价格！'
                    },
                    regexp: {
                        regexp: /^[0-9]+([.]{1}[0-9]{1,4})?$/,
                        message: '会员月租价格至多保留四位小数的正数!'
                    }
                }
            },
            reviewPrice: {
                validators: {
                    notEmpty: {
                        message: '请输入单个评价价格！'
                    },
                    regexp: {
                        regexp: /^[0-9]+([.]{1}[0-9]{1,4})?$/,
                        message: '单个评价价格至多保留四位小数的正数!'
                    }
                }
            },
            exchangeRate: {
                validators: {
                    notEmpty: {
                        message: '请输入会员月租价格！'
                    },
                    regexp: {
                        regexp: /^[0-9]+([.]{1}[0-9]{1,4})?$/,
                        message: '会员月租价格至多保留四位小数的正数!'
                    }
                }
            }
        }
    }).on('success.form.bv', function (e) {
        var form = $('#formobj');
        $.post(form.attr('action'), form.serialize(), function (result) {
            if (result.success == "success") {
                toastr.success(result.msg);
                setTimeout("window.location='/skipController.admin?goPriceExchange'", 1000);
            } else if (result.success === "fail") {
                toastr.warning(result.msg);
                form.bootstrapValidator('disableSubmitButtons', false);
            } else{
                window.location='/adminSystemController.admin?goAdminLogin';
            }
        }, 'json');
    });
});

function changeUserPwd() {
    $('#formobj').submit();
}

var ViewModel = function (id,monthRent,reviewPrice,exchangeRate) {
    this.id = ko.observable(id);
    this.monthRent = ko.observable(monthRent);
    this.monthRentCopy = ko.observable(monthRent);
    this.reviewPrice = ko.observable(reviewPrice);
    this.reviewPriceCopy = ko.observable(reviewPrice);
    this.exchangeRate = ko.observable(exchangeRate);
    this.exchangeRateCopy = ko.observable(exchangeRate);
};

function loadData() {
    $.ajax({
        url: "/adminPromotPriceController.admin?doGet",
        type: 'post',
        success: function (data) {
            if (data.success === "success") {
                ko.applyBindings(
                    new ViewModel(
                        data.content.id,
                        data.content.monthRent,
                        data.content.reviewPrice,
                        data.content.exchangeRate
                    )
                );
            } else if (data.success === "fail") {
                toastr.error(data.msg);
            } else {
                window.location = '/adminSystemController.admin?goAdminLogin'
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.warning("服务器异常,请联系管理员！");
        }
    });
}
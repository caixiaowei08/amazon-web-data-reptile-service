/**
 * Created by User on 2017/6/20.
 */
function newPromotOrder() {
    window.location = '/promotOrderController.do?goNewPromotOne'
}

$(function () {
    loadData();
})

var ViewModel = function (activeOrderNum, todayEvaluateNum, buyerNum, totalEvaluateNum, historyOrderNum, totalConsumption) {
    this.activeOrderNum = ko.observable(activeOrderNum);
    this.todayEvaluateNum = ko.observable(todayEvaluateNum);
    this.buyerNum = ko.observable(buyerNum);
    this.totalEvaluateNum = ko.observable(totalEvaluateNum);
    this.historyOrderNum = ko.observable(historyOrderNum);
    //this.totalConsumption = ko.observable(totalConsumption);
    this.totalConsumption = ko.observable(totalConsumption);
};

function loadData() {
    $.ajax({
        url: "/userController.do?doGetBaseUserInfo",
        type: 'post',
        data: $('#formObj').serialize(),
        success: function (data) {
            if (data.success === "success") {
                ko.applyBindings(
                    new ViewModel(
                        data.content.activeOrderNum,
                        data.content.todayEvaluateNum,
                        data.content.buyerNum,
                        data.content.totalEvaluateNum,
                        data.content.historyOrderNum,
                        data.content.totalConsumption
                    )
                );
            } else {
                window.location = '/loginController.do?login'
            }
        },
        error: function (jqxhr, textStatus, errorThrow) {
            toastr.success("服务器异常,请联系管理员！");
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

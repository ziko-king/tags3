$(function () {
    /**
     * ajax封装
     * url 发送请求的地址
     * data 发送到服务器的数据，数组存储，如：{"date": new Date().getTime(), "state": 1}
     * dataType 预期服务器返回的数据类型，常用的如：xml、html、json、text
     * successfn 成功回调函数
     * errorfn 失败回调函数
     */
    jQuery.axsGet = function (async, url, data, successfn, errorfn) {
        data = (data == null || data == "" || typeof (data) == "undefined") ? {"date": new Date().getTime()} : data;
        $.ajax({
            async: async,
            type: "get",
            url: url,
            data: data,
            dataType: "json",
            //timeout:5000,    //超时时间
            success: function (data) {
                successfn(data);
            },
            error: function (e) {
                errorfn(e);
            }
        });
    };
    /**
     * ajax封装
     * url 发送请求的地址
     * data 发送到服务器的数据，数组存储，如：{"date": new Date().getTime(), "state": 1}
     * dataType 预期服务器返回的数据类型，常用的如：xml、html、json、text
     * successfn 成功回调函数
     * errorfn 失败回调函数
     */
    jQuery.axsPost = function (async, url, data, successfn, errorfn) {
        data = (data == null || data == "" || typeof (data) == "undefined") ? {"date": new Date().getTime()} : data;
        $.ajax({
            async: async,
            type: "post",
            url: url,
            data: JSON.stringify(data),
            contentType: "application/json",
            dataType: "json",
            //timeout:5000,    //超时时间
            success: function (data) {
                successfn(data);
            },
            error: function (e) {
                errorfn(e);
            }
        });
    };


    jQuery.ajaxPost = function (async, url, data, successfn, errorfn) {
        data = (data == null || data == "" || typeof (data) == "undefined") ? {"date": new Date().getTime()} : data;
        $.ajax({
            async: async,
            type: "post",
            url: url,
            data: data,
            dataType: "json",
            //timeout:50000,    //超时时间
            success: function (data) {
                successfn(data);
            },
            error: function (e) {
                errorfn(e);
            }
        });
    };

});
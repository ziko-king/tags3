/*method*/

var baseMethod = {
    getId: function (id) {
        return document.getElementById(id);
    },
    getClass: function (tagName, className) //获得标签名为tagName,类名className的元素
    {
        if (document.getElementsByClassName) //支持这个函数
        {
            return document.getElementsByClassName(className);
        } else {
            var tags = document.getElementsByTagName(tagName);//获取标签
            var tagArr = [];//用于返回类名为className的元素
            for (var i = 0; i < tags.length; i++) {
                if (tags[i].class == className) {
                    tagArr[tagArr.length] = tags[i];//保存满足条件的元素
                }
            }
            return tagArr;
        }
    },
    isPhone: function (str) {
        var reg = /^1[3|4|5|7|8]\d{9}$/;
        return reg.test(str);
    },
    isEmail: function (str) {
        var reg = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
        return reg.test(str);
    },
    isNum: function (str) {
        var reg = /^[0-9]*[1-9][0-9]*$/;
        return reg.test(str);
    },
    isIE: function () {
        if (!!window.ActiveXObject || "ActiveXObject" in window)
            return true;
        else
            return false;
    },
    whatBrowser: function () {
        var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
        var isOpera = userAgent.indexOf("Opera") > -1; //判断是否Opera浏览器
        var isChrome = userAgent.indexOf("Chrome") > -1; //判断是否Opera浏览器
        //判断是否IE浏览器
        var isFF = userAgent.indexOf("Firefox") > -1; //判断是否Firefox浏览器
        var isSafari = userAgent.indexOf("Safari") > -1; //判断是否Safari浏览器

        if (isFF) {
            return "FF";
        }
        if (isOpera) {
            return "Opera";
        }
        if (isChrome) {
            return "Chrome";
        }

        if (isSafari) {
            return "Safari"
        }
    },
    randomRgbaColor: function () {
        return '#' + '0123456789abcdef'.split('').map(function (v, i, a) {
            return i > 5 ? null : a[Math.floor(Math.random() * 16)]
        }).join('');
    },
    getRandomColor: function () { //随机生成RGBA颜色
        var r = Math.floor(Math.random() * 256); //随机生成256以内r值
        var g = Math.floor(Math.random() * 256); //随机生成256以内g值
        var b = Math.floor(Math.random() * 256); //随机生成256以内b值
        var alpha = Math.random(); //随机生成1以内a值
        return 'rgba(' + r + ',' + g + ',' + b + ',.7)'; //返回rgba(r,g,b,a)格式颜色
    }
};
Array.prototype.map = function (fn, thisObj) {
    var scope = thisObj || window;
    var a = [];
    for (var i = 0, j = this.length; i < j; ++i) {
        a.push(fn.call(scope, this[i], i, this));
    }
    return a;
};

if (baseMethod.whatBrowser() == "Safari") {
    console.info("--safari--");
    var userlradio = $(".Userl_radio6");
    var useraddlabelimg = $(".useraddlabel_img");
    useraddlabelimg.css("margin-left", "-4px");
    userlradio.css("margin-left", "-4px");
}
$(document).ready(function () {
    var num;
    $('.navbar-nav>li[id]').hover(function () {
        var Obj = $(this).attr('id');
        num = Obj.substring(3, Obj.length);
        $('#box-' + num).slideDown(300);
    }, function () {
        $('#box-' + num).hide();
    });
    $('.hidden-box').hover(function () {

        $(this).show();
    }, function () {
        $(this).slideUp(200);
    });
});


function sroclltop() {
    $(window).scroll(function () {
        var s = $(window).scrollTop();
        if (s > 0) {
            $("#navbar").css('boxShadow', '0px 1px 10px rgba(0,0,0,0.5)');
        }
        if (s == 0) {
            $("#navbar").css('boxShadow', '0px 0px 0px #fff');
        }

    });

}

sroclltop();
















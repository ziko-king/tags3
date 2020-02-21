/*method*/
var baseMethod = {
    getId: function (id) {
        return document.getElementById(id);
    },
    getClass: function (tagName, className) { //获得标签名为tagName,类名className的元素
        if (document.getElementsByClassName) { //支持这个函数
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

/*将数值四舍五入后格式化. 
 @param num 数值(Number或者String) 
 @param cent 要保留的小数位(Number) 
 @param isThousand 是否需要千分位 0:不需要,1:需要(数值类型); 
 @return 格式的字符串,如'1,234,567.45' 
 @type String 
*/
function formatNumber(num, cent, isThousand) {
    num = num.toString().replace(/$|,/g, '');
    if (isNaN(num))//检查传入数值为数值类型.
        num = "0";
    if (isNaN(cent))//确保传入小数位为数值型数值.
        cent = 0;
    cent = parseInt(cent);
    cent = Math.abs(cent);//求出小数位数,确保为正整数.
    if (isNaN(isThousand))//确保传入是否需要千分位为数值类型.
        isThousand = 0;
    isThousand = parseInt(isThousand);
    if (isThousand < 0)
        isThousand = 0;
    if (isThousand >= 1) //确保传入的数值只为0或1
        isThousand = 1;
    sign = (num == (num = Math.abs(num)));//获取符号(正/负数)
    //Math.floor:返回小于等于其数值参数的最大整数
    num = Math.floor(num * Math.pow(10, cent) + 0.50000000001);//把指定的小数位先转换成整数.多余的小数位四舍五入.
    cents = num % Math.pow(10, cent); //求出小数位数值.
    num = Math.floor(num / Math.pow(10, cent)).toString();//求出整数位数值.
    cents = cents.toString();//把小数位转换成字符串,以便求小数位长度.
    while (cents.length < cent) {//补足小数位到指定的位数.
        cents = "0" + cents;
    }
    if (isThousand == 0) { //不需要千分位符.
        if (cent == 0) {
            return (((sign) ? '' : '-') + num);
        } else {
            return (((sign) ? '' : '-') + num + '.' + cents);
        }
    } else {
        //对整数部分进行千分位格式化.
        for (var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++)
            num = num.substring(0, num.length - (4 * i + 3)) + ',' +
                num.substring(num.length - (4 * i + 3));
        if (cent == 0) {
            return (((sign) ? '' : '-') + num);
        } else {
            return (((sign) ? '' : '-') + num + '.' + cents);
        }
    }
} 
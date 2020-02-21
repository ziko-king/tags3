/**
 * Created by syw on 2017/5/18.
 */
    //			------   首页 图表------
var chartBar = null;
window.onload = function () {
    jQuery.axsGet(true, "../home/currentTagCount", {'level': 5}, function (data) {
        $(".chartOneNum span").text(formatNumber(data.data, 0, 1));

    }, function (xhr) {
        layer.msg("数据服务错误！")
    })
    jQuery.axsGet(true, "../home/userCount", {}, function (data) {
        $(".chartTwoNum span").text(formatNumber(data.data, 0, 1));

    }, function (xhr) {
        layer.msg("数据服务错误！")
    });

    var xAxis_one = ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"];
    var yAxis_One = [100, 80, 120, 60, 140, 10, 190, 100, 110, 90, 100, 100];
    var yAxis_Two = [1000, 12000, 50000, 40000, 5600, 50500, 10000, 90000, 6000, 4000, 10000, 30000];
    chartsFn('barOne', "当前有效标签数量", xAxis_one, yAxis_One);
    chartsFn('barTwo', "标签系统覆盖用户数", xAxis_one, yAxis_Two);
}


function chartsFn(ele, name, Xdata, Ydata) {
    var myChart = echarts.init(document.getElementById(ele));
    var option = {
        color: ['#3398DB'],
        tooltip: {
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: [
            {
                type: 'category',
                data: Xdata,
                axisTick: {
                    alignWithLabel: true
                }
            }
        ],
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: [
            {
                name: name,
                type: 'bar',
                barWidth: '60%',
                data: Ydata
            }
        ]
    };
    myChart.setOption(option);
}

/* 
将数值四舍五入后格式化. 
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


/**banner***/
/**
 * Created by syw on 2017/3/29.
 */


$(document).ready(function () {
    var length,
        currentIndex = 0,
        interval,
        hasStarted = false, //是否已经开始轮播
        t = 3000; //轮播时间间隔
    length = $('.slider-panel').length;
    //将除了第一张图片隐藏
    $('.slider-panel:not(:first)').hide();
    //将第一个slider-item设为激活状态
    $('.slider-item:first').addClass('slider-item-selected');
    //隐藏向前、向后翻按钮
    $('.slider-page').hide();
    //鼠标上悬时显示向前、向后翻按钮,停止滑动，鼠标离开时隐藏向前、向后翻按钮，开始滑动
    $('.slider-panel, .slider-pre, .slider-next').hover(function () {
        stop();
        $('.slider-page').show();
    }, function () {
        $('.slider-page').hide();
        start();
    });
    $('.slider-item').hover(function (e) {
        stop();
        var preIndex = $(".slider-item").filter(".slider-item-selected").index();
        currentIndex = $(this).index();
        play(preIndex, currentIndex);
    }, function () {
        start();
    });
    $('.slider-pre').unbind('click');
    $('.slider-pre').bind('click', function () {
        pre();
    });
    $('.slider-next').unbind('click');
    $('.slider-next').bind('click', function () {
        next();
    });

    /**
     * 向前翻页
     */
    function pre() {
        var preIndex = currentIndex;
        currentIndex = (--currentIndex + length) % length;
        play(preIndex, currentIndex);
    }

    /**
     * 向后翻页
     */
    function next() {
        var preIndex = currentIndex;
        currentIndex = ++currentIndex % length;
        play(preIndex, currentIndex);
    }

    function play(preIndex, currentIndex) {
        $('.slider-panel').eq(preIndex).fadeOut(500)
            .parent().children().eq(currentIndex).fadeIn(1000);
        $('.slider-item').removeClass('slider-item-selected');
        $('.slider-item').eq(currentIndex).addClass('slider-item-selected');
    }

    /**
     * 开始轮播
     */
    function start() {
        if (!hasStarted) {
            hasStarted = true;
            interval = setInterval(next, t);
        }
    }

    /**
     * 停止轮播
     */
    function stop() {
        clearInterval(interval);
        hasStarted = false;
    }

    //开始轮播
    start();
});
$(document.body).css({
    "overflow-x": "hidden",

});
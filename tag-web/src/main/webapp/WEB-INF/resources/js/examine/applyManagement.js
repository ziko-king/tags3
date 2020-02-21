if ($(".cover_style").height() < 330) {
//	$(".cover_style").css('height',"330px");
} else {
    var height_r = $(".cont_right_label").height();
}
//判断是全量数据0，还是左侧一级菜单下面的局部数据1
var flag = 0;

//判断是标签列表下哪一种状态0已失败，1已暂停，2全部，3任务管理
var index = 0;

//入参对象
var req = new Object();


//搜索
$(document).on('click', ".Userl_radio6", function () {
    gettableList();
});
/**首页**/
/*$('.current_table').on("click","#home_page",function(){
	setReqAndIndex();
	req.text = $(".User_input").val();
	if(flag == 0){
		queryAllList(req,index);
	}else if(flag == 1){
		queryListByfirst(req,index);
	}
});*/

/**尾页**/
/*$('.current_table').on("click","#end_page",function(){
	setReqAndIndex();
	req.text = $(".User_input").val();
	req.tot=parseInt($(this).parents(".pages").find('#tota').text());
	if(flag == 0){
		queryAllList(req,index);
	}else if(flag == 1){
		queryListByfirst(req,index);
	}
});*/
/**跳页**/
/*$('.current_table').on("click","#search_page",function(){
	var sear=$(this).prev().prev(input).val();
	searchPage(sear);
});*/


//状态转显示文本
function userGroupState(state) {
    if (state == 1) {
        return '已上线,#6666ff';
    } else if (state == 2) {
        return '申请中,#00cc99';
    } else if (state == 3) {
        return '开发完成,#66ccff';
    }
}

/**时间格式**/    //long转string
function FormatDate(strTime) {
    var date = new Date(strTime);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    var h = date.getHours();
    h = h < 10 ? ('0' + h) : h;
    var minute = date.getMinutes();
    minute = minute < 10 ? ('0' + minute) : minute;
    var second = date.getSeconds();
    second = second < 10 ? ('0' + second) : second;
    return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
}

/*******************************************************方法区**************************************/

/****页面初始化****/
$(document).ready(function () {
    var clientHeight = window.screen.height - 190;
    var objectObj = $(".con_gr");
    objectObj.css("height", clientHeight + 'px');


    //Safari浏览器兼容
    if (baseMethod.whatBrowser() == "Safari") {
        console.log("Safari");
        var failnum = $(".failnum_wrap_content_left_img");
        var pusuenum = $(".pusuenum_wrap_content_left_img");
        failnum.attr('class', 'failnum_wrap_content_left_img safari_failnum_wrap_content_left_img');
        pusuenum.attr('class', 'pusuenum_wrap_content_left_img safari_failnum_wrap_content_left_img');
        var fialnum_data_top = $(".fialnum_data");
        var safari_fialnum_data_top = $(".safari_fialnum_data_top");
        fialnum_data_top.attr("class", "safari_fialnum_data_top fialnum_data");
        safari_fialnum_data_top.attr("class", "safari_fialnum_data_top fialnum_data");
    }


    //刷新标签列表内容
    var req = new Object();
    req.status = 4;
    var span_all = $('#User_centerIN .User_centerIN_top span');
    var current = $('#User_centerIN .current');
    var index = span_all.index(current);
    //$.fn.jqLoading({ height: 85, width: 240, text: "正在加载中，请耐心等待...." });
    //queryAllList(req,index);
});

/*图表*/
function getOption(s_data1, s_data2, s_data3, s_data4) {
    var option = {
        title: {
            text: ''
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            }
        },
        legend: {
            x: 'center',
            y: 'bottom',
            data: ['组合申请', '标签申请', '标签删除', '组合删除'],
            show: true,
            itemGap: 40
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '10%',
            containLabel: true
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, 0.1],
            splitLine: {
                show: true
            },
            label: {
                normal: {
                    show: true,
                    position: "top",
                    color: "#333333"
                }
            }
        },
        xAxis: {
            type: 'category',
            data: ['申请中', '开发中', '已上线', '开发完成'],
            axisLine: {
                show: false
            },
            axisTick: {
                show: false
            },
            splitLine: {
                show: true
            }
        },
        series: [
            {
                name: '组合申请',
                type: 'bar',
                data: s_data1,
                itemStyle: {
                    normal: {
                        color: "#66ccff",
                        label: {
                            show: true,
                            position: "top",
                            textStyle: {
                                color: "#333333"
                            }
                        }
                    }
                }
            },
            {
                name: '标签申请',
                type: 'bar',
                data: s_data2,
                itemStyle: {
                    normal: {
                        color: "#6666ff",
                        label: {
                            show: true,
                            position: "top",
                            textStyle: {
                                color: "#333333"
                            }
                        }
                    }
                }
            },
            {
                name: '标签删除',
                type: 'bar',
                data: s_data3,
                itemStyle: {
                    normal: {
                        color: "#66cccc",
                        label: {
                            show: true,
                            position: "top",
                            textStyle: {
                                color: "#333333"
                            }
                        }
                    }
                }
            },
            {
                name: '组合删除',
                type: 'bar',
                data: s_data4,
                itemStyle: {
                    normal: {
                        color: "#00cc99",
                        label: {
                            show: true,
                            position: "top",
                            textStyle: {
                                color: "#333333"
                            }
                        }
                    }
                }
            }
        ]
    }
    return option;
}

var s_data1 = [103, 238, 203, 100], s_data2 = [132, 233, 100, 154], s_data3 = [103, 149, 203, 170],
    s_data4 = [125, 238, 100, 134];
var mychart = echarts.init(document.getElementById("chart"));
mychart.setOption(getOption(s_data1, s_data2, s_data3, s_data4));
//获取图表数据
getchart(id);

function getchart(id) {
    var req = {}
    $.ajax({
        url: basePath + '/deltag',
        type: 'POST',
        async: false,
        data: JSON.stringify(req),
        timeout: 5000,    //超时时间
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            if (data.code == 200) {
                mychart.setOption(getOption(s_data1, s_data2, s_data3, s_data4));
            } else {
                layer.msg(data.msg)
            }
        },
        error: function (xhr) {
            layer.msg('数据服务错误！');
        }
    });
}

$(".labeltask_middle_wrap input").on("click", function () {
    var id = $(this).attr("data-id");
    getchart(id);
})
//取消标签创建
$("tbody").on("click", ".cancel_msg", function () {
    var id = $(this).attr("data-id");
    layer.confirm('确定取消当前标签申请吗??', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        $.ajax({
            url: basePath + '/deltag',
            type: 'POST',
            async: false,
            data: JSON.stringify(req),
            timeout: 5000,    //超时时间
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            success: function (data) {
                if (data.code == 200) {
                    layer.msg('取消标签申请成功');
                } else {
                    layer.msg(data.msg)
                }
            },
            error: function (xhr) {
                layer.msg('数据服务错误！');
            }
        });

    });
});

//获取表格
gettableList();

function gettableList() {
    var req = {
        "": $(".User_input").val()
    }
    $.ajax({
        url: basePath + '/deltag',
        type: 'POST',
        async: false,
        data: JSON.stringify(req),
        timeout: 5000,    //超时时间
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            if (data.code == 200) {
                var str = '';
                $.each(data, function (k, v) {
                    str = '<tr>'
                        + '<td style="text-align:left;"><input type="checkbox">1</td>'
                        + '<td>组合标签</td>'
                        + '<td>组合标签创建</td>'
                        + '<td>A小A</td>'
                        + '<td title="123123123123" style="cursor:pointer;"><div style="width:66px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">123123123123</div></td>'
                        + '<td title="模型1模型1模型1模型1" style="cursor pointer;"><div style="width:66px;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;">模型1模型1模型1模型1</div></td>'
                        + '<td>demo</td>'
                        + '<td>--</td>'
                        + '<td></td>'
                        + '<td>2016-01-10 18:10:10</td>'
                        + '<td><div class="work workFail">申请中</div></td>'
                        + '<td class="resale" data-id="' + v.id + '">发布审批</td>'
                        + '</tr>'
                });
                $("#failtable table").html(str);
            } else {
                layer.msg(data.msg)
            }
        },
        error: function (xhr) {
            layer.msg('数据服务错误！');
        }
    });
}


if ($(".cover_style").height() < 330) {
//	$(".cover_style").css('height',"330px");
} else {
    var height_r = $(".cont_right_label").height();
}
//判断是全量数据0，还是左侧一级菜单下面的局部数据1
var flag = 0;

//判断是标签列表下哪一种状态0已失败，1已暂停，2全部，3任务管理
var index = 0;

//搜索
$(document).on('click', ".Userl_radio6", function () {
    gettableList();
});

//状态转显示文本
function userGroupState(state) {
    if (state == 1) {
        return '已上线,#66cccc';
    } else if (state == 2) {
        return '申请中,#00cc99';
    } else if (state == 3) {
        return '开发完成,#66cccc';
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

//加载数据 显示右侧图内容
/****页面初始化****/
$(document).ready(function () {
    var clientHeight = window.screen.height - 190;
    var objectObj = $(".con_gr");
    objectObj.css("height", clientHeight + 'px');
    //Safari浏览器兼容
    if (baseMethod.whatBrowser() == "Safari") {
        var failnum = $(".failnum_wrap_content_left_img");
        var pusuenum = $(".pusuenum_wrap_content_left_img");
        failnum.attr('class', 'failnum_wrap_content_left_img safari_failnum_wrap_content_left_img');
        pusuenum.attr('class', 'pusuenum_wrap_content_left_img safari_failnum_wrap_content_left_img');
        var fialnum_data_top = $(".fialnum_data");
        var safari_fialnum_data_top = $(".safari_fialnum_data_top");
        fialnum_data_top.attr("class", "safari_fialnum_data_top fialnum_data");
        safari_fialnum_data_top.attr("class", "safari_fialnum_data_top fialnum_data");
    }
});
//点击左侧事件
$(document).on('click', ".firstGr", function () {
    $(".firstGr").css("color", "#000");
    $(".firstGr img").attr("src", "../res/imgs/71.png");
    $(this).css("color", "#2fa4ff");
    $(this).find("img").attr("src", "../res/imgs/u14.png")
});
// 表格行全选
$(document).on("click", "#failtable thead th input", function () {
    if ($(this).prop("checked")) {
        $(this).prop("checked", true);
        $("#failtable tbody").find("input").prop("checked", true);
    } else {
        $(this).prop("checked", false);
        $("#failtable tbody").find("input").prop("checked", false);
    }
});
$(document).on("click", "#failtable tbody input", function () {
    var j = 0;
    for (var i = 0; i < $("#failtable tbody tr").length; i++) {
        if ($("#failtable tbody tr").eq(i).find("input").prop("checked")) {
            j = j + 1;
        }
    }
    if (j == $("#failtable tbody tr").length) {
        $("#failtable thead th input").prop("checked", true);
    } else {
        $("#failtable thead th input").prop("checked", false);
    }
});
//发布标签
$("#failtable tbody").on("click", ".resale", function () {
    var id = $(this).attr('data-id');
    layer.confirm('是否确定发布当前标签?', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        var req = {
            "id": id
        }
        //发送异步请求
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
                    layer.msg('发布成功');
                } else {
                    layer.msg(data.msg);
                }
            },
            error: function (xhr) {
                layer.msg('数据服务错误！');
            }
        });
    });
});
//批量发布
$(".User_radio_sosuo .shenpi").on("click", function () {
    var ids = [];
    for (var i = 0; i < $("#failtable tbody tr").length; i++) {
        if ($("#failtable tbody tr").eq(i).find("input").prop("checked")) {
            ids.push($("#failtable tbody tr").eq(i).find("input").attr("data-id"))
        }
    }
    if (ids.length == 0) {
        layer.msg("请选择要发布的标签");
        return false;
    }
    layer.confirm('是否确定发布当前选中的标签?', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        var req = {
            "id": ids.join(",")
        }
        //发送异步请求
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
                    layer.msg('批量发布成功');
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
//gettableList();
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
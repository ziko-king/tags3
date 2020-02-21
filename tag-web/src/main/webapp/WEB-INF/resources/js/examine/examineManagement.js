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

/****页面初始化****/
$(document).ready(function () {
    var clientHeight = window.screen.height - 154;
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
    //获取表格
    gettableList();
});
//状态转显示文本
//1申请中、2审核通过、3运行中、4未运行、5已禁用
function getState(state) {
    var state_val = "";

    if (state == "0") {
        state_val = '全部,#7ad0da';
    } else if (state == "1") {
        state_val = '申请中,#7ad0da';
    } else if (state == "2") {
        state_val = '审核通过,#73c2e6';
    } else if (state == "3") {
        state_val = '运行中,#73c2e6';
    } else if (state == "4") {
        state_val = '未运行,#b1b1b1';
    } else if (state == "5") {
        state_val = '已禁用,#686868';
    }
    return state_val;
}

function getTypes(type) {
    var type_val = "";
    if (type == 1) {
        type_val = "基础标签";
    } else if (type == 2) {
        type_val = "组合标签";
    }
    return type_val;
}

function getUn(val) {
    if (val == undefined) {
        return "";
    }
}

function gettableList() {
    var dataReq = {
        "tagName": $(".User_input").val(),
        "page": 1,
        "pageSize": 10
    }
    jQuery.axsGet(true, basePath + '/examine/list', dataReq, function (data) {
        if (data.code == 10001) {
            var str = '';
            $.each(data.data.data, function (k, v) {
                var bgcolor = v.business == undefined && v.model == "暂无" ? "background-color:#ffeff0;" : "";
                //var flag = v.rule == undefined || v.model==undefined ? false : true;
                var flag = v.state == 1 ? true : false;
                var name = v.name == undefined || v.name == null ? '暂无' : v.name;
                var check = flag ? "check" : "";
                var business = v.business == undefined ? "暂无" : v.business;
                var applyUserName = v.applyUserName == undefined ? "暂无" : v.applyUserName;
                str += '<tr style="text-align:left;' + bgcolor + '">'
                    + '<td><s class="' + check + '"></s>' + (k + 1) + '</td>'
                    + '<td>' + getTypes(v.type) + '</td>'
                    + '<td>' + name + '</td>'
                    + '<td><div style="cursor: pointer;width:90px;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;" title="' + business + '">' + business + '</div></td>'
                    + '<td>' + v.model + '</td>'
                    + '<td>' + applyUserName + '</td>'
                    + '<td><div class="work workFail" style="background-color:' + getState(v.state).split(",")[1] + ';">' + getState(v.state).split(",")[0] + '</div></td>'
                    + '<td class="resale" data-id="' + v.id + '" data-type="' + v.type + '"  style="text-align:center;">'
                if (flag) {
                    str += "<span class='pass'>通过</span>"
                        + "<span class='notPass' style='margin-left:10px;'>不通过</span>"
                } else {
                    str += "<span class='pass' disabled='disabled' style='color:#ccc;'>通过</span>"
                        + "<span class='notPass' disabled='disabled' style='margin-left:10px;color:#ccc;'>不通过</span>"
                }
                str += '</td>'
                    + '</tr>'
            });
            $("#failtable tbody").html(str);
            var max_count = data.data.count;

            function pageselectCallback(page_id) {
                dataReq.page = page_id + 1;
                jQuery.axsGet(true, basePath + '/examine/list', dataReq, function (data) {
                    if (data.code == 10001) {
                        var str = '';
                        $.each(data.data.data, function (k, v) {
                            var bgcolor = v.business == undefined && v.model == "暂无" ? "background-color:#ffeff0;" : "";
                            //var flag = v.rule == undefined || v.model==undefined ? false : true;
                            var flag = v.state == 1 ? true : false;
                            var name = v.name == undefined || v.name == null ? '暂无' : v.name;
                            var check = flag ? "check" : "";
                            var business = v.business == undefined ? "暂无" : v.business;
                            var applyUserName = v.applyUserName == undefined ? "暂无" : v.applyUserName;
                            str += '<tr style="text-align:left;' + bgcolor + '">'
                                + '<td><s class="' + check + '"></s>' + (page_id * 10 + k + 1) + '</td>'
                                + '<td>' + getTypes(v.type) + '</td>'
                                + '<td>' + name + '</td>'
                                + '<td><div style="cursor: pointer;width:90px;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;" title="' + business + '">' + business + '</div></td>'
                                + '<td>' + v.model + '</td>'
                                + '<td>' + applyUserName + '</td>'
                                + '<td><div class="work workFail" style="background-color:' + getState(v.state).split(",")[1] + ';">' + getState(v.state).split(",")[0] + '</div></td>'
                                + '<td class="resale" data-id="' + v.id + '" data-type="' + v.type + '"  style="text-align:center;">'
                            if (flag) {
                                str += "<span class='pass'>通过</span>"
                                    + "<span class='notPass' style='margin-left:10px;'>不通过</span>"
                            } else {
                                str += "<span class='pass' disabled='disabled' style='color:#ccc;'>通过</span>"
                                    + "<span class='notPass' disabled='disabled' style='margin-left:10px;color:#ccc;'>不通过</span>"
                            }
                            str += '</td>'
                                + '</tr>'
                        });
                        $("#failtable tbody").html(str);
                    } else {
                        $("#failtable tbody").html("");
                        layer.msg(data.msg)
                    }
                }, function (e) {
                    layer.msg('服务器错误！');
                });
            }

            // 创建分页元素
            $("#Pagination").pagination(max_count, {
                num_edge_entries: 2,
                num_display_entries: 6,
                items_per_page: 10,
                callback: pageselectCallback  //回调函数
            });
        } else {
            $("#failtable tbody").html("");
            layer.msg(data.msg)
        }
    }, function (e) {
        layer.msg('服务器错误！');
    });
}

// 表格行全选
$(document).on("click", "#failtable thead th s", function () {
    if ($(this).hasClass("checked")) {
        $(this).removeClass("checked");
        $("#failtable tbody").find("s.check").removeClass("checked");
    } else {
        $(this).addClass("checked");
        $("#failtable tbody").find("s.check").addClass("checked");
    }
});
$(document).on("click", "#failtable tbody s.check", function () {
    if ($(this).hasClass("checked")) {
        $(this).removeClass("checked");
    } else {
        $(this).addClass("checked");
    }
    var j = 0;
    for (var i = 0; i < $("#failtable tbody tr s.check").length; i++) {
        if ($("#failtable tbody tr").find("s.check").eq(i).hasClass("checked")) {
            j = j + 1;
        }
    }
    if (j == $("#failtable tbody tr s.check").length) {
        $("#failtable thead th s").addClass("checked");
    } else {
        $("#failtable thead th s").removeClass("checked");
    }
});
//批量审批
$(".User_radio_sosuo .shenpi").on("click", function () {
    var dataReq = [];
    for (var i = 0; i < $("#failtable tbody tr").length; i++) {
        if ($("#failtable tbody tr").eq(i).find("s.checked").hasClass("checked")) {
            var id = $("#failtable tbody tr").eq(i).find(".resale").attr("data-id");
            var type = $("#failtable tbody tr").eq(i).find(".resale").attr("data-type");
            var json = {
                "id": id,
                "type": type,
                "state": "2"
            }
            dataReq.push(json);
        }
    }
    if (dataReq.length == 0) {
        layer.msg("请选择要发布的标签");
        return false;
    }
    layer.confirm('批量操作当前所有选中记录?', {
        btn: ['通过', '不通过'] //按钮
    }, function () {
        //通过
        var dataReq = [];
        for (var i = 0; i < $("#failtable tbody tr").length; i++) {
            if ($("#failtable tbody tr").eq(i).find("s.checked").hasClass("checked")) {
                var id = $("#failtable tbody tr").eq(i).find(".resale").attr("data-id");
                var type = $("#failtable tbody tr").eq(i).find(".resale").attr("data-type");
                var json = {
                    "id": id,
                    "type": type,
                    "state": "2"
                }
                dataReq.push(json);
            }
        }
        jQuery.ajaxPost(true, basePath + '/examine/batchAudit', {"conditions": JSON.stringify(dataReq)}, function (data) {
            if (data.code == 10003) {
                layer.msg(data.msg);
                //获取表格
                gettableList();
                $("#failtable thead th input").prop("checked", false);
            } else {
                layer.msg(data.msg);
            }
        }, function (e) {
            layer.msg('服务器错误！');
        });
    }, function () {
        //不通过
        var dataReq = [];
        for (var i = 0; i < $("#failtable tbody tr").length; i++) {
            if ($("#failtable tbody tr").eq(i).find("s.checked").hasClass("checked")) {
                var id = $("#failtable tbody tr").eq(i).find(".resale").attr("data-id");
                var type = $("#failtable tbody tr").eq(i).find(".resale").attr("data-type");
                var json = {
                    "id": id,
                    "type": type,
                    "state": "0"
                }
                dataReq.push(json);
            }
        }
        jQuery.ajaxPost(true, basePath + '/examine/batchAudit', {"conditions": JSON.stringify(dataReq)}, function (data) {
            if (data.code == 10003) {
                layer.msg(data.msg);
                //获取表格
                gettableList();
                $("#failtable thead th").find("s").removeClass("checked")
            } else {
                layer.msg(data.msg);
            }
        }, function (e) {
            layer.msg('服务器错误！');
        });
    });
});
//审批通过
$("#failtable tbody").on("click", ".pass", function () {
    var that = $(this);
    if (that.attr("disabled") == "disabled") {
        return false;
    }
    layer.confirm('确定通过当前审批?', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        var type = that.parent(".resale").attr("data-type"),
            id = that.parent(".resale").attr("data-id"),
            state = "2";
        var dataReq = {
            "id": Number(id),
            "type": Number(type),
            "state": state
        }
        jQuery.ajaxPost(true, basePath + '/examine/audit', dataReq, function (data) {
            if (data.code == 10003) {
                layer.msg(data.msg);
                //获取表格
                gettableList();
            } else {
                layer.msg(data.msg);
            }
        }, function (e) {
            layer.msg('服务器错误！');
        });
    });
});
//审批不通过
$("#failtable tbody").on("click", ".notPass", function () {
    var that = $(this);
    if (that.attr("disabled") == "disabled") {
        return false;
    }
    layer.confirm('确定不通过当前审批?', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        var type = that.parent(".resale").attr("data-type"),
            id = that.parent(".resale").attr("data-id"),
            state = "5";
        var dataReq = {
            "id": Number(id),
            "type": Number(type),
            "state": state
        }
        jQuery.ajaxPost(true, basePath + '/examine/audit', dataReq, function (data) {
            if (data.code == 10003) {
                layer.msg(data.msg);
                //获取表格
                gettableList();
            } else {
                layer.msg(data.msg);
            }
        }, function (e) {
            layer.msg('服务器错误！');
        });
    });
});
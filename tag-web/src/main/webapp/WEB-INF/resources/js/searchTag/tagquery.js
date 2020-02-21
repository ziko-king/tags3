if ($(".cover_style").height() < 330) {
//	$(".cover_style").css('height',"330px");
} else {
    var height_r = $(".cont_right_label").height();
}
var h_all = $(document).height();
$("#treeDemo").css({
    "height": h_all - 31 - 50 + "px",
    "overflow": "auto"
})
//判断是全量数据0，还是左侧一级菜单下面的局部数据1
var flag = 0;
//判断是标签列表下哪一种状态0已失败，1已暂停，2全部，3任务管理
var index = 0;

//获取左侧树菜单
function getNodes() {
    var zNodes = [];
    $.ajaxPost(false, basePath + "/basic/queryBasicTag", {}, function (data) {
        if (data.code == 10005) {
            var nodes = data.data;
            $.each(nodes, function (k, v) {
                var iconSkin = v.subTags != undefined && v.subTags.length > 0 ? "pIcon01" : "icon01";
                var json1 = {
                    id: v.id,
                    pId: v.pid,
                    name: v.name,
//					iconSkin:iconSkin,
                    open: false,
                    click: false
                }
                if (k == 0) {
                    json1.open = true;
                    json1.checked = true;
                }
                zNodes.push(json1);
                if (v.subTags != undefined && v.subTags.length > 0) {
                    $.each(v.subTags, function (m, n) {
                        var iconSkin1 = n.subTags != undefined && n.subTags.length > 0 ? "pIcon01" : "icon01";
                        var json2 = {
                            id: n.id,
                            pId: n.pid,
                            name: n.name,
//							iconSkin:iconSkin1,
                            open: false,
                            click: false
                        }
                        if (k == 0 && m == 0) {
                            json2.open = true;
                            json2.checked = true;
                        }
                        zNodes.push(json2);
                        if (n.subTags != undefined && n.subTags.length > 0) {
                            $.each(n.subTags, function (r, s) {
                                var iconSkin2 = s.subTags != undefined && s.subTags.length > 0 ? "pIcon01" : "icon01";
                                var json3 = {
                                    id: s.id,
                                    pId: s.pid,
                                    name: s.name,
//									iconSkin:iconSkin2,
                                    open: false,
                                    click: true
                                }
                                if (k == 0 && m == 0 && r == 0) {
                                    json3.open = true;
                                    json3.checked = true;
                                }
                                zNodes.push(json3);
                                if (s.subTags != undefined && s.subTags.length > 0) {
                                    $.each(s.subTags, function (a, b) {
                                        var iconSkin3 = b.subTags != undefined && b.subTags.length > 0 ? "pIcon01" : "icon01";
                                        var json4 = {
                                            id: b.id,
                                            pId: b.pid,
                                            name: b.name,
//											iconSkin:iconSkin3,
                                            open: false,
                                            click: true
                                        }
                                        if (k == 0 && m == 0 && r == 0 && a == 0) {
                                            json4.open = true;
                                            json4.checked = true;
                                        }
                                        zNodes.push(json4);
                                        if (b.subTags != undefined && b.subTags.length > 0) {
                                            $.each(b.subTags, function (c, d) {
                                                var iconSkin4 = d.subTags != undefined && d.subTags.length > 0 ? "pIcon01" : "icon01";
                                                var json5 = {
                                                    id: d.id,
                                                    pId: d.pid,
                                                    name: d.name,
//										 			iconSkin:"icon01",
                                                    open: false,
                                                    click: true
                                                }
                                                if (k == 0 && m == 0 && r == 0 && a == 0 && c == 0) {
                                                    json5.checked = true;
                                                }
                                                zNodes.push(json5);
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            });
        } else {
            layer.msg(data.msg);
        }
    }, function (e) {
        //$.fn.jqLoading("destroy");
        layer.msg('数据服务错误！');
    });
    return zNodes;
}

//左侧树形菜单
var setting = {
    check: {
        enable: true
    },
    view: {
        showIcon: false
    },
    data: {
        simpleData: {
            enable: true
        }
    }
};
// var zNodes =[
//     { id:1, pId:0, name:"展开、折叠 ", iconSkin:"pIcon01",open:true,click:false},
//         { id:11, pId:1, name:"叶子节点1", iconSkin:"pIcon01",open:true,click:false},
//             { id:111, pId:11, name:"叶子节点111", iconSkin:"pIcon01",open:true,click:true},
//             	{ id:1111, pId:111, name:"叶子节点1111", iconSkin:"pIcon01",open:true,click:true},
//             		{ id:11111, pId:1111, name:"叶子节点11111", iconSkin:"icon01",click:true},
//             		{ id:11112, pId:1111, name:"叶子节点11111", iconSkin:"icon01",click:true},
//             	{ id:1112, pId:111, name:"叶子节点111", iconSkin:"icon01",open:false,click:true},
//         { id:12, pId:1, name:"叶子节点2", iconSkin:"icon01",click:false},
//         { id:13, pId:1, name:"叶子节点3", iconSkin:"icon01",click:false},
//     { id:2, pId:0, name:"展开、折叠自定义图标相同", iconSkin:"pIcon01",click:false},
//         { id:21, pId:2, name:"叶子节点1", iconSkin:"pIcon01",click:false},
//         	{ id:211, pId:21, name:"叶子节点1", iconSkin:"icon01",click:true},
//         	{ id:212, pId:21, name:"叶子节点1", iconSkin:"icon01",click:true},
//         { id:22, pId:2, name:"叶子节点2", iconSkin:"icon01",click:false},
//         { id:23, pId:2, name:"叶子节点3", iconSkin:"icon01",click:false},
//     { id:3, pId:0, name:"不使用自定义图标",click:false,iconSkin:"pIcon01", },
//         { id:31, pId:3, name:"叶子节点1", iconSkin:"icon01",click:false},
//         { id:32, pId:3, name:"叶子节点2", iconSkin:"icon01",click:false},
//         { id:33, pId:3, name:"叶子节点3", iconSkin:"icon01",click:false}
// ];
/*获取被选中的五级标签*/
function choicebtn() {
    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
    var nodes = treeObj.getCheckedNodes(true);
    var arr = [];
    $.each(nodes, function (k, v) {
        if (v.level == 4) {
            arr.push(v.id);
        }
    });
    return arr.join(",");
}

$(document).ready(function () {
    //渲染dom树
    $.fn.zTree.init($("#treeDemo"), setting, getNodes());
    //获取查询结果
    getList();
});

//获取右侧表格内容
function getList() {
    var dataReq = {
        "ids": choicebtn(),
        "page": 1,
        "pageSize": 10
    }
    if (dataReq.ids == "") {
        layer.msg("请选择查询的标签");
        return false;
    }
    $.axsGet(false, basePath + "/search/queryUserByTagIds", dataReq, function (data) {
        if (data.code == 10005) {
            if (data.data != null) {
                //清空table
                var target = $(".cover_style");
                target.find('tbody').remove();
                addRecord(target.find('table'), data.data.data, 0);
                var max_count = data.data.count;

                function pageselectCallback(page_id) {
                    dataReq.page = page_id + 1;
                    $.axsGet(false, basePath + "/search/queryUserByTagIds", dataReq, function (data) {
                        if (data.code == 10005) {
                            if (data.data != null) {
                                //清空table
                                var target = $(".cover_style");
                                target.find('tbody').remove();
                                addRecord(target.find('table'), data.data.data, page_id);
                            }
                        } else {
                            layer.msg(data.msg);
                        }
                    }, function (e) {
                        layer.msg('数据服务错误！');
                    });
                }

                // 创建分页元素
                $("#Pagination").pagination(max_count, {
                    num_edge_entries: 2,
                    num_display_entries: 6,
                    items_per_page: 10,
                    callback: pageselectCallback  //回调函数
                });
            }
        } else {
            $(".cover_style").find("tbody").html("");
            $("#Pagination").pagination(0, {
                num_edge_entries: 2,
                num_display_entries: 6,
                items_per_page: 10
            });
            layer.msg(data.msg);
        }
    }, function (e) {
        layer.msg('数据服务错误！');
    });
}

$('.searchBtn span').on("click", function (k, v) {
    getList();
});

//添加表记录   target：目标table的tbody  list TaskListResult的集合  index点击（失败，暂停，全部等）的数组下标 0，1，2，3
function addRecord(target, list, page_id) {
    for (var i = 0; i < list.length; i++) {
        var current = list[i];
        var record = $('<tr></tr>');
        var one = $('<td></td>').text(page_id * 10 + i + 1);
        var two = $('<td></td>').text(current.name);
        var three = $('<td></td>').text(current.idNum);
        var four = $('<td></td>').text(current.phone);
        var five = $('<td></td>').text(current.bankNum);
        var six = $('<td></td>').text("");
        record.append(one, two, three, four, five, six);
        target.append('<tbody class="taskmanage"></tbody>').append(record);
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
    //刷新标签列表内容
    /*var req=new Object();
    req.status = 4;
    var span_all=$('#User_centerIN .User_centerIN_top span');
    var current =$('#User_centerIN .current');
    var index=span_all.index(current);*/
    //$.fn.jqLoading({ height: 85, width: 240, text: "正在加载中，请耐心等待...." });
    //queryAllList(req,index);
});
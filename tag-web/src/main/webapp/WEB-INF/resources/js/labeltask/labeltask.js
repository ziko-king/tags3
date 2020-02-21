if ($(".cover_style").height() < 330) {
//	$(".cover_style").css('height',"330px");
} else {
    var height_r = $(".cont_right_label").height();
}
var h_all = $(document).height();
$("#treeDemo").css({
    "height": h_all - 50 + "px",
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
                    iconSkin: iconSkin,
                    open: false,
                    click: false
                }
                zNodes.push(json1);
                if (v.subTags != undefined && v.subTags.length > 0) {
                    $.each(v.subTags, function (m, n) {
                        var iconSkin1 = n.subTags != undefined && n.subTags.length > 0 ? "pIcon01" : "icon01";
                        var json2 = {
                            id: n.id,
                            pId: n.pid,
                            name: n.name,
                            iconSkin: iconSkin1,
                            open: false,
                            click: false
                        }
                        zNodes.push(json2);
                        if (n.subTags != undefined && n.subTags.length > 0) {
                            $.each(n.subTags, function (r, s) {
                                var iconSkin2 = s.subTags != undefined && s.subTags.length > 0 ? "pIcon01" : "icon01";
                                var json3 = {
                                    id: s.id,
                                    pId: s.pid,
                                    name: s.name,
                                    iconSkin: iconSkin2,
                                    open: false,
                                    click: true
                                }
                                zNodes.push(json3);
                                if (s.subTags != undefined && s.subTags.length > 0) {
                                    $.each(s.subTags, function (a, b) {
                                        // var iconSkin3 = b.subTags!=undefined && b.subTags.length > 0 ?"pIcon01": "icon01";
                                        var json4 = {
                                            id: b.id,
                                            pId: b.pid,
                                            name: b.name,
                                            iconSkin: "icon01",
                                            open: false,
                                            click: true
                                        }
                                        zNodes.push(json4);
                                        // if(b.subTags.length > 0){
                                        // 	$.each(b.subTags,function(c,d){
                                        // 		var iconSkin4 = d.subTags!=undefined && d.subTags.length > 0  ? "pIcon01": "icon01";
                                        // 		var json5 = {
                                        // 			id:d.id,
                                        // 			pId:d.pid,
                                        // 			name:d.name,
                                        // 			iconSkin:iconSkin4,
                                        // 			open:false,
                                        // 			click:true
                                        // 		}
                                        // 		zNodes.push(json5);
                                        // 	});
                                        // }
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
    data: {
        simpleData: {
            enable: true
        }
    },
    view: {
        showIcon: false
    },
    callback: {
        onClick: onClick
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

$(document).ready(function () {
    $.fn.zTree.init($("#treeDemo"), setting, getNodes());
    //初始化获得数据
    //0.未启动 1.成功 2.失败 3.运行中
    //失败
    var req0 = {
        "queryCode": 0,
        "status": "2"
    }
    queryListThree(basePath + "/engine/queryEngineInfo?", req0, 1);
    //成功
    var req1 = {
        "queryCode": 0,
        "status": "1"
    }
    queryListThree(basePath + "/engine/queryEngineInfo?", req1, 2);
    //全部
    var req2 = {
        "queryCode": 0
    }
    queryListThree(basePath + "/engine/queryEngineInfo?", req2, 0);
    queryListThree(basePath + "/engine/queryEngineInfo?", req2, 3);
});
//左侧树菜单与右侧表格联动
var tagId;
var level;

function onClick(event, treeId, treeNode, clickFlag) {
    $(".zTreeDemoBackground a").css("color", "#333");
    if (treeNode.click) {
        $("#" + treeNode.tId).find("#" + treeNode.tId + "_a").css("color", "#aaa");
        var id = treeNode.id, pId = treeNode.pId;
        tagId = id;
        level = treeNode.level;
        //0.查询所有1.按四级标签查询 2.按三级标签查询
        //失败
        if (treeNode.level == 2) {
            var req0 = {
                "tagId": id,
                "queryCode": 2,
                "name": $(".User_input").val(),
                "status": "2"
            }
            queryListThree(basePath + "/engine/queryEngineInfo?", req0, 1);
            //成功
            var req1 = {
                "tagId": id,
                "queryCode": 2,
                "name": $(".User_input").val(),
                "status": "1"
            }
            queryListThree(basePath + "/engine/queryEngineInfo?", req1, 2);
            //全部
            var req2 = {
                "tagId": id,
                "queryCode": 2,
                "name": $(".User_input").val()
            }
            queryListThree(basePath + "/engine/queryEngineInfo?", req2, 0);
            queryListThree(basePath + "/engine/queryEngineInfo?", req2, 3);
        } else if (treeNode.level == 3) {
            var req0 = {
                "tagId": id,
                "queryCode": 1,
                "name": $(".User_input").val(),
                "status": "2"
            }
            queryListThree(basePath + "/engine/queryEngineInfo?", req0, 1);
            //成功
            var req1 = {
                "tagId": id,
                "queryCode": 1,
                "name": $(".User_input").val(),
                "status": "1"
            }
            queryListThree(basePath + "/engine/queryEngineInfo?", req1, 2);
            //全部
            var req2 = {
                "tagId": id,
                "queryCode": 1,
                "name": $(".User_input").val()
            }
            queryListThree(basePath + "/engine/queryEngineInfo?", req2, 0);
            queryListThree(basePath + "/engine/queryEngineInfo?", req2, 3);
        }

    }
}

//入参对象
//var req=new Object();
//失败  成功 全部 。。 导航点击事件
$('#User_centerIN .User_centerIN_top .labelTab span').click(function () {
    $(this).addClass('current').css("color", "#000").css("border-bottom", "2px solid #1e8af9");
    $(this).siblings().removeClass('current').css("border-bottom", "1px solid rgb(211,211,211)").css("color", "gray");
    var index = $('#User_centerIN .User_centerIN_top span').index($(this));
    var show_all = $('#User_centerIN').children('div').not('.User_centerIN_top');
    // show_all.eq(index).find('tbody').children().remove();   //index 为点击数组下标 0~3  清除表内数据
    //根据index判断，然后填充数据
    /**
     *
     *
     *
     *
     */
    show_all.eq(index).addClass('current_table').show();
    show_all.not(show_all.eq(index)).removeClass('current_table').hide();
});
//点击任务标签列表req.search = $("#query").val
/*$(document).on('click',".status",function(){
	//$.fn.jqLoading({ height: 85, width: 240, text: "正在加载中，请耐心等待...." });
	// $(".Userl_radio6").val("");
	if(flag == 0){
		// setReqAndIndex();
	    //queryAllList(req,index);
	}else if(flag == 1){
		// setReqAndIndex();
	    //queryListByfirst(req,index);
	}
	
});*/

/*function setReqAndIndex(){
	var span_all=$('#User_centerIN .User_centerIN_top span');
	var current =$('#User_centerIN .current');
	index=span_all.index(current);
	if(index==0){
	  req.status=4;
	}else if(index==1){
	  req.status=3;
	}else{
	  req.status=0;
	}
}*/
function getList() {
    var val = $(".User_input").val();
    var req0 = {
            "name": val,
            "status": "2"
        },
        req1 = {
            "name": val,
            "status": "1"
        },
        req2 = {
            "name": val
        }
    if (tagId == undefined) {
        req0.queryCode = 0;
        queryListThree(basePath + "/engine/queryEngineInfo?", req0, 1);
        req1.queryCode = 0;
        queryListThree(basePath + "/engine/queryEngineInfo?", req1, 2);
        req2.queryCode = 0;
        queryListThree(basePath + "/engine/queryEngineInfo?", req2, 0);
        queryListThree(basePath + "/engine/queryEngineInfo?", req2, 3);
    } else {
        if (level == 2) {
            req0.queryCode = 2;
            queryListThree(basePath + "/engine/queryEngineInfo?", req0, 1);
            req1.queryCode = 2;
            queryListThree(basePath + "/engine/queryEngineInfo?", req1, 2);
            req2.queryCode = 2;
            queryListThree(basePath + "/engine/queryEngineInfo?", req2, 0);
            queryListThree(basePath + "/engine/queryEngineInfo?", req2, 3);
        } else if (level == 3) {
            req2.queryCode = 1;
            queryListThree(basePath + "/engine/queryEngineInfo?", req0, 1);
            req2.queryCode = 1;
            queryListThree(basePath + "/engine/queryEngineInfo?", req1, 2);
            req2.queryCode = 1;
            queryListThree(basePath + "/engine/queryEngineInfo?", req2, 0);
            queryListThree(basePath + "/engine/queryEngineInfo?", req2, 3);
        }
    }
}

//搜索
$(document).on('click', ".Userl_radio6", function () {
    getList();
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
//0.未启动 1.成功 2.失败 3.运行中 4.已终止
//1申请中、2审核通过、3运行中、4未运行、5已禁用
function getState(state) {
    //0 未启动,1 成功,2失败,3正在运行
    if (state == "1") {
        return '申请中,#7acfd9';
    } else if (state == "2") {
        return '审核通过,#79cfd9';
    } else if (state == "3") {
        return '运行中,#73c2e6';
    } else if (state == "4") {
        return '未运行,#79cfd9';
    } else if (state == "5") {
        return '已禁用,#d64646';
    }
    /*if(state == "0"){
        return	'任务未启动,#b1b1b1';
    }else if(state == "1"){
        return	'任务完成,#73c2e6';
    }else if(state == "2"){
        return	'任务失败,#d64646';
    }else if(state == "3"){
        return '任务进行中,#7ad0da';
    }else if(state == "4"){
        return '任务已暂停,#7ad0da';
    }else if(state == "5"){
        return '任务已终止,#d64646';
    }*/

}

//添加表记录   target：目标table的tbody  list TaskListResult的集合  index点击（失败，暂停，全部等）的数组下标 0，1，2，3
function addRecord(target, list, index) {
    for (var i = 0; i < list.length; i++) {
        var current = list[i];
        var record = $('<tr></tr>');
        var one = $('<td></td>').text(current.tagType);
        /*var two=$('<td></td>').text("");*/
        var three = $('<td></td>').text(current.name);
        var four = '<td><div style="cursor: pointer;width:90px;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;" title="' + current.business + '">' + current.business + '</div></td>';
        var five = '<td><div style="cursor: pointer;width:80px;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;" title="' + current.modelName + '">' + current.modelName + '</div></td>';
        var six = $('<td></td>').text(current.applyPerson);
        var seven = $('<td></td>').text(current.operator);
        var eight = $('<td></td>').text(current.startTime);
        var nine = $('<td></td>').text(current.endTime);
        //0 未启动,1 成功,2失败,3正在运行
        var ten = "";
        if (current.status == 2) {
            ten = '<td><div class="work" data-reason="' + current.status + '" style="background-color:' + getState(current.status).split(",")[1] + ';">' + getState(current.status).split(",")[0] + '</div></td>';
        } else {
            ten = '<td><div class="work " style="background-color:' + getState(current.status).split(",")[1] + ';">' + getState(current.status).split(",")[0] + '</div></td>';
        }
        var operation = "";
        if (index == 3) {
            //0.未启动 1.成功 2.失败 3.运行中 4.任务暂停 5.任务终止
            //1申请中、2审核通过、3运行中、4未运行、5已禁用

            /*if(current.status == 0){//未启用
                operation=$('<td class="td-manage" data-id="'+current.tagId+'">'+
                        '<a title="暂停" disabled="disabled" class="btn btn-xs pusetask" value="0" style="color:#ccc;"><i class="fa fa-check  bigger-120"></i>暂停</a>'+
                        '<a title="终止"  class="btn btn-xs stoptask" value="0"><i class="fa fa-check   bigger-120"></i>终止</a></td>');
            }else*/
            if (current.status == 3) {//启用
                operation = $('<td class="td-manage" data-id="' + current.tagId + '" style="text-align:center;">' +
                    '<a title="暂停"  class="btn btn-xs pusetask" value="0"><i class="fa fa-check   bigger-120"></i>暂停</a>' +
                    '<a title="终止"  class="btn btn-xs stoptask" value="0"><i class="fa fa-check   bigger-120"></i>终止</a></td>');
            } else if (current.status == 4 || current.status == 2 || current.status == 5) {//暂停
                operation = $('<td class="td-manage" data-id="' + current.tagId + '" style="text-align:center;">' +
                    '<a title="暂停" disabled="disabled"  class="btn btn-xs pusetask" value="0" style="color:#ccc;"><i class="fa fa-check   bigger-120"></i>暂停</a>' +
                    '<a title="启用"  class="btn btn-xs runtask" value="0"><i class="fa fa-check   bigger-120"></i>启用</a></td>');
            } else if (current.status == 1) {//终止
                operation = $('<td class="td-manage" data-id="' + current.tagId + '" style="text-align:center;">' +
                    '<a title="暂停" disabled="disabled" class="btn btn-xs pusetask" value="0" style="color:#ccc;"><i class="fa fa-check   bigger-120"></i>暂停</a>' +
                    '<a title="启用" disabled="disabled" class="btn btn-xs runtask" value="0"  style="color:#ccc;"><i class="fa fa-check   bigger-120"></i>启用</a></td>');
            }
            //增加if判断，确定可执行操作样式
            record.append(one, three, four, five, six, seven, eight, nine, ten, operation);
        } else {
            record.append(one, three, four, five, six, seven, eight, nine, ten, operation);
        }
        target.append('<tbody class="taskmanage"></tbody>').append(record);
    }
}

//失败原因
$(document).on('click', "tbody .workFail", function () {
    var reason = $(this).attr("data-reason");
    layer.confirm(reason, {
        btn: [] //按钮
    });
});
//暂停任务
$(document).on('click', "tbody .td-manage .pusetask", function () {
    if ($(this).attr("disabled") != "disabled") {
        var tagId = $(this).parent().attr("data-id");
        var req = {
            "tagId": tagId
        }
        layer.confirm('确定暂停该标签任务?', {
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.ajaxPost(false, basePath + "/engine/suspendEngine", req, function (data) {
                if (data.code == 10001) {
                    layer.msg("标签任务暂停成功")
                } else {
                    layer.msg(data.msg)
                }
            }, function (e) {
                layer.msg("数据服务错误！")
            });
        });
    }
});
//终止任务
$(document).on('click', "tbody .td-manage .stoptask", function () {
    if ($(this).attr("disabled") != "disabled") {
        //修改后台状态
        var tagId = $(this).parent().attr("data-id");
        var req = {
            "tagId": tagId
        }
        layer.confirm('确定终止该标签任务?', {
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.ajaxPost(false, basePath + "/engine/stopEngine", req, function (data) {
                if (data.code == 10001) {
                    layer.msg("标签任务终止成功")
                } else {
                    layer.msg(data.msg)
                }
            }, function (e) {
                layer.msg("数据服务错误！")
            });
        });
    }
});
//启用任务
$(document).on('click', "tbody .td-manage .runtask", function () {
    if ($(this).attr("disabled") != "disabled") {
        var tagId = $(this).attr("data-id");
        var req = {
            "tagId": parseInt(tagId),
            "operation": 1
        }
        layer.confirm('确定启用该标签任务?', {
            content: '<p style="text-align:center;">确定启用该标签任务?</p>'
                + '<p style="text-align:center;margin-bottom:10px;">可设置标签任务及对应标签是否可见权限。</p>'
                + '<div class="select_ss" style="text-align:center;"><input type="radio" value="1" checked name="ss"/><span style="margin-right:20px;">用户管理员可见</span><input name="ss" type="radio" value="2"/><span>只管理员可见</span></div>',
            btn: ['确定', '取消'] //按钮
        }, function () {
            $.axsGet(false, basePath + "/engine/startEngine", req, function (data) {
                if (data.code == 200) {
                    layer.msg("标签任务终止成功");
                    getList();
                } else {
                    layer.msg(data.msg)
                }
            }, function (e) {
                layer.msg("数据服务错误！")
            });
        });
    }
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

//点击三级
function queryListThree(url, req, index) {
    var target = $('.s_table').eq(index);
    $.axsGet(false, url, req, function (data) {
        if (data.data != null) {
            //清空table
            target.find('tbody').remove();
            addRecord(target.find('table'), data.data, index);
            /*if(index == 0){
                addPageInfo("#fail-table_info",result.pageBean);
            }else if(index == 1){
                addPageInfo("#success-table_info",result.pageBean);
            }else if(index == 2){
                addPageInfo("#all-table_info",result.pageBean);
            }else if(index == 3){
                addPageInfo("#manage-table_info",result.pageBean);
            }*/
        } else {
            layer.msg(data.msg);
        }
        // $.fn.jqLoading("destroy");
    }, function (e) {
        // $.fn.jqLoading("destroy");
        layer.msg('数据服务错误！');
    });
}

//获取所有任务列表
/*function queryAllList(req,index){
	 var target=$('#User_centerIN').children('.current_table');
	 $.ajaxPost(async,url, JSON.stringify(req), function(data){
	 	// $.fn.jqLoading("destroy");
	    	if(data.code==200&&data.data!=null){
	    		var result=data.data;
	    		target.find('tbody').remove();
	    		addRecord(target.find('table'),data.data,index);
	    		if(index == 0){
	    			addPageInfo("#fail-table_info",result.pageBean);
	    		}else if(index == 1){
	    			addPageInfo("#success-table_info",result.pageBean);
	    		}else if(index == 2){
	    			addPageInfo("#all-table_info",result.pageBean);
	    		}else if(index == 3){
	    			addPageInfo("#manage-table_info",result.pageBean);
	    		}
	    	}else{
	    		layer.msg(data.msg,{time: 1000, icon:2});
	    	}
	 },function(e){
	 	// $.fn.jqLoading("destroy");
    	layer.msg('服务器错误',{time: 1000, icon:2});
	 });
	 $.ajax({
	    url:basePath+'/taskListAll',
	    type:'POST',
	    async:true,
	    data:JSON.stringify(req),
	    contentType:"application/json; charset=utf-8",
	    dataType:'json',
	    success:function(data){
	    	$.fn.jqLoading("destroy");
	    	if(data.code==200&&data.data!=null){
	    		var result=data.data;
	    		target.find('tbody').remove();
	    		addRecord(target.find('table'),data.data,index);
	    		if(index == 0){
	    			addPageInfo("#fail-table_info",result.pageBean);
	    		}else if(index == 1){
	    			addPageInfo("#success-table_info",result.pageBean);
	    		}else if(index == 2){
	    			addPageInfo("#all-table_info",result.pageBean);
	    		}else if(index == 3){
	    			addPageInfo("#manage-table_info",result.pageBean);
	    		}
	    	}else{
	    		popMessage(data.msg,'red');
	    	}
	    },
	    error:function(xhr){
	    	$.fn.jqLoading("destroy");
	    	popMessage('服务器错误','red');
	    }
	});
}
*/
//加载数据 显示右侧图内容


/*******************************************************方法区**************************************/

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
//分页
/*function addPageInfo(ele,pageinfo){
	$('#curr').text(pageinfo.curPage);
	$('#reco').text(pageinfo.rowsCount);	
	$('#tota').text(pageinfo.pageCount);
	$('#size').text(pageinfo.pageSize);
	if(pageinfo.curPage==1){
		$(ele).find('.up_page').css('cursor','Default').css('background','#ddd');
	    $('#up_page').unbind();
	}else{
		$(ele).find('.up_page').css('cursor','pointer').css('background','#fff');
		$('#up_page').bind("click",upPage);
	}
	if(pageinfo.curPage==pageinfo.pageCount){
		$(ele).find('.next_page').css('cursor','Default').css('background','#ddd');
		 $('#next_page').unbind();
	}else{
		$(ele).find('.next_page').css('cursor','pointer').css('background','#fff');
		$('#next_page').bind("click",nextPage);
	}
}*/
//上一页
/*function upPage(){
   var curPage=parseInt($('#curr').text());
   if(curPage>1){
   		setReqAndIndex();
		req.text = $(".User_input").val();
		req.pageCur = curPage-1;
	    if(flag == 0){
			queryAllList(req,index);
		}else if(flag == 1){
			queryListByfirst(req,index);
		}
   } 
} */
//下一页
/*function nextPage(){
   var curPage=parseInt($('#curr').text());
   var tot=parseInt($('#tota').text());
   if(curPage<tot){
   		setReqAndIndex();
		req.text = $(".User_input").val();
		req.pageCur = curPage+1;
	    if(flag == 0){
			queryAllList(req,index);
		}else if(flag == 1){
			queryListByfirst(req,index);
		}
   }
}*/
//跳转页面
/*function searchPage(query){
   var reg = new RegExp("^[0-9]*$");
   if(!reg.test(query)){
	   layer.msg("请输入数字!");
	   return false;
    }
   var tot=parseInt($('#tota').text());
   if(query>tot){
	   query=tot;
   }
    setReqAndIndex();
	req.text = $(".User_input").val();
	req.pageCur = query;
    if(flag == 0){
		queryAllList(req,index);
	}else if(flag == 1){
		queryListByfirst(req,index);
	}
}*/
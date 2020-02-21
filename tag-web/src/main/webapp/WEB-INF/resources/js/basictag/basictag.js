$(document).ready(function () {
    //一二三级标签编辑的表格
    $('#tb_powerset').treegridData({
        id: 'id',
        parentColumn: 'pid',
        type: "POST", //请求数据的ajax类型
        url: '../basic/queryMainBasicTag',   //请求数据的ajax的url
        ajaxParams: {}, //请求数据的ajax的data属性
        expandColumn: null,//在哪一列上面显示展开按钮
        striped: true,   //是否各行渐变色
        bordered: true,  //是否显示边框
        expandAll: false,  //是否全部展开
        columns: [
            {
                title: '标签名称',
                field: 'name'
            },
            {
                title: '操作',
                field: ''
            }
        ]
    });
    //编辑1,2,3级标签
    $("#tb_powerset").on("click", ".labelEdit", function () {
        var id = $(this).attr("data-id");
        var text = $(this).parents("td").prev().text();
        $("#maineditModal").find("#labelName").val(text);
        /*$(document).("show.bs.modal",".modal",function(e){
            var zIndex = 1040 + (10 * $(".modal:visible").length);
            $(this).css("z-index",zIndex);
        })*/

        //点击一级二级三级的确定编辑按钮
        $("#sureeditlabel").click(function () {
            var mainName = $("#labelName").val(),
                name = /^[A-Za-z0-9_\-\u4e00-\u9fa5]+$/,
                data = {"id": id, "name": mainName};
            if (mainName == "") {
                layer.msg("标签名称不能为空！");
            } else {
                if (name.test(mainName)) {
                    jQuery.axsGet(true, "../basic/updateMainBasicTagForId", data, function (data) {
                        if (data.code == 10003) {
                            layer.msg("更新成功！");
                            $("#maineditModal").modal("hide");
                            $("#setModal").modal("hide");
                            location.reload();
                        } else {
                            layer.msg(data.msg);
                        }
                    }, function (xrh) {
                        layer.msg("数据服务错！")
                    })
                } else {
                    layer.msg("标签名称为数字，字母，汉字，下划线，横杠！");
                }
            }
        });
    })
    var bread = getBreadIdAndLevel();
    var resultList = getTags(bread.tagId, bread.level, '');
    refreshContentList(resultList);
});

// 根据标签名称搜索其子级标签
function searchTag() {
    var name = $('#query').val();
    var breadData = getBreadIdAndLevel();
    var id = breadData.tagId;
    var level = breadData.level;
    var resultList = searchTagForName(id, level, name);
    refreshContentList(resultList);
}

//获取最后一级面包屑的id和level
function getBreadIdAndLevel() {
    var value = $("#breadDiv>span:last").attr("value");
    var id = value.split('`')[0];
    var level = value.split('`')[1];
    var result = {
        "tagId": id,
        "level": level
    };
    return result;
}

//刷新内容列表
function reloadContentList() {
    //根据面包屑拿到id
    var breadData = getBreadIdAndLevel();
    var resultList = getTags(breadData.tagId, breadData.level, '');
    refreshContentList(resultList);
}

// 根据id查询其子级
function getTags(id, level, name) {
    var result = '';
    $.ajax({
        type: 'get',
        url: "../basic/queryBasicTagAndModelForWithPid",
        dataType: "json",
        data: {
            "tagId": id,
            "level": level,
            "name": name
        },
        async: false,
        success: function (data) {
            result = data;
        }
    });
    return result;
}

// 根据标签名称搜索
function searchTagForName(id, level, name) {
    var result = '';
    $.ajax({
        type: 'get',
        url: "../basic/searchBasicTagForName",
        dataType: "json",
        data: {
            "id": id,
            "level": level,
            "name": name
        },
        async: false,
        success: function (data) {
            result = data;
        }
    });
    return result;
}

//根据ID拿详情
function getTagById(id) {
    var result = '';
    $.ajax({
        type: 'get',
        url: "../basic/queryBasicTagForId",
        dataType: "json",
        data: {
            "id": id
        },
        async: false,
        success: function (data) {
            result = data;
        }
    });
    return result;
}

//根据ID拿第四级详情
function getFourthTagById(id) {
    var result = '';
    $.ajax({
        type: 'get',
        url: "../basic/queryBasicModelRuleForId",
        dataType: "json",
        data: {
            "tagId": id
        },
        async: false,
        success: function (data) {
            result = data;
        }
    });
    return result;
}

//更新四级标签
function updateLevel4TagShow(this_) {
    var id = $(this_).attr('id');
    var tag = getFourthTagById(id);
    var data = tag.data;
    $("#sub_hidefileupload").attr("data-url", "");
    $("#levelFourName").attr("disabled", "disabled");
    $(".fourlabelname_worn").hide();
    if (data == null || data == undefined || data.length == 0) {
        var name = '';
        var business = '';
        var rule = '';
        var modelMain = '';
        var modelName = '';
        var args = '';
        var modelPath = '';
        var scheTime = '';
        var path = modelPath.split("/");
        $(".fileName").val('');
    } else {
        var tagData = data[0];
        //赋值
        $("#childrenModalLabel").text("编辑四级标签");
        $(".successtip").hide();

        var name = tagData.tagName == null ? '' : tagData.tagName;
        var business = tagData.business == null ? '' : tagData.business;
        var rule = tagData.rule == null ? '' : tagData.rule;
        var modelMain = tagData.modelMain == null ? '' : tagData.modelMain;
        var modelName = tagData.modelName == null ? '' : tagData.modelName;
        var args = tagData.args == null ? '' : tagData.args;
        var modelPath = tagData.modelPath == null ? '' : tagData.modelPath;
        var scheTime = tagData.scheTime == null ? '' : tagData.scheTime;
        var path = modelPath.split("/");
        $(".fileName").val((path[path.length - 1]).substring(0, 25) + '....')
        var tagArr = tagData.scheTime.split("#");

        if (tagArr[0] == "每天") {
            $(".updatechoice option").eq(0).attr("selected", true);
        } else if (tagArr[0] == "每周") {
            $(".updatechoice option").eq(1).attr("selected", true);
        } else if (tagArr[0] == "每月") {
            $(".updatechoice option").eq(2).attr("selected", true);
        } else if (tagArr[0] == "每年") {
            $(".updatechoice option").eq(3).attr("selected", true);
        }
        $(".startTime").val(tagArr[1]);
        $(".endTime").val(tagArr[2]);
    }

    $(".sortoneLabel").val($("#breadDiv .label_cont_top_l").eq(0).text());
    $(".sorttwoLabel").val($("#breadDiv .label_cont_top_l").eq(1).text());
    $(".sortthreeLabel").val($("#breadDiv .label_cont_top_l").eq(2).text());
    document.getElementById('levelFourName').value = name;
    document.getElementById('fourMeanings').value = business;
    document.getElementById('fourLabelRules').value = rule;
    document.getElementById('algorithmMain').value = modelMain;
    document.getElementById('algorithmName').value = modelName;
    //document.getElementById('fileName').value=modelPath;


    $("#sub_hidefileupload").attr("data-url", modelPath)
    document.getElementById('algorithmmodel').value = args;
    //更改按钮
    $('#createchildrentype').attr("data-val", "1&" + id);
    document.getElementById('createchildrentype').title = 'update';
    document.getElementById('createchildrentype').innerHTML = '更新';
    $("#childrenModalLabel label.error").text("");
}

//更新五级标签
function updateLevel5TagShow(this_) {
    var id = $(this_).attr('value');
    var tag = getTagById(id);
    var data = tag.data;
    $("#levelFifthName").attr("disabled", "disabled");
    $(".labelname_worn").hide();
    if (data.length == 0) {
        $("#levelFifthName").val("");
        $("#fifthMeanings").val("");
        $("#fifthLabelRules").val("");
    } else {
        var tagData = data[0];
        //赋值
        $("#smallchildrenModalLabel").text("编辑五级标签");
        $("#levelFifthId").val(id);
        document.getElementById('levelFifthName').value = tagData.name;
        document.getElementById('fifthMeanings').value = tagData.business == null ? '' : tagData.business;
        document.getElementById('fifthLabelRules').value = tagData.rule == null ? '' : tagData.rule;

    }
    //更改按钮
    document.getElementById('createsmallchildrentype').title = 'update';
    document.getElementById('createsmallchildrentype').innerHTML = '更新';
    $("#smallchildrenModal label.error").text("");

}

//更新列表
function refreshContentList(resultList) {
    var resultArr = resultList.data;
    var html = '';
    //更新
    document.getElementById('contentTagList').innerHTML = '';
    if (resultArr != undefined) {
        for (var i = 0; i < resultArr.length; i++) {
            var resultTag = resultArr[i];
            if (null == resultTag.business || resultTag.business == '' || resultTag.business == undefined || resultTag.business == "undefine") {
                var business = "暂无";
            } else {
                var business = resultTag.business;
            }
            if (null == resultTag.scheTime || resultTag.scheTime == '' || resultTag.scheTime == undefined || resultTag.scheTime == "undefine") {
                var scheTime = "暂无";
            } else {
                var scheTime = resultTag.scheTime;
            }
            var subList = resultTag.subTags;
            var tagName = resultTag.tagName == null ? '' : resultTag.tagName;
            html += '<div style="display:inline-block" class="year clearfix">';
            html += '<div class="year0"><span class="shu_name" value=' + resultTag.tagId + ' title= ' + tagName + ' level = ' + resultTag.level + '>' + tagName + '</span></div>';
            html += '<div class="gender3 clearfix">';
            html += '<div class="gender3_1">';
            html += '<div style="display:none;" class="gender3_1top shu_name"></div>';
            html += '<div class="gender3_1buttom">';
            if (resultTag.state == 1) {// 状态：1申请中、2审核通过、3运行中、4未运行、5已禁用
                html += '<div class="shangxian"><span style="background: #7acfd9;border: 1px solid #7acfd9;">申请中</span></div>';//状态
            } else if (resultTag.state == 3) {
                html += '<div class="shangxian"><span style="background:#73c2e6;border: 1px solid #73c2e6;">运行中</span></div>';//状态
            } else if (resultTag.state == 2) {
                html += '<div class="shangxian"><span style="background:#79cfd9;border: 1px solid #79cfd9;">审核通过</span></div>';//状态
            } else if (resultTag.state == 4) {
                html += '<div class="shangxian"><span style="background:#79cfd9;border: 1px solid #79cfd9;">未运行</span></div>';//状态
            } else if (resultTag.state == 5) {
                html += '<div class="shangxian"><span style="background:#d64646;border: 1px solid #d64646;">已禁用</span></div>';//状态
            }
            html += '<span><img src="../res/imgs/refreshN.png" style="margin-top:-4px;width:16px;"></span> ';
            html += '<span class="updatatime" title="' + scheTime + '">' + scheTime + '</span>';//更新策略 每月更新
            html += '</div>';
            html += '</div>';
            html += '<div class="gender3_2">';
            html += '<span style="display:inline-block;"><img src="../res/imgs/wornN.png" style="margin-top:-35px;width: 16px;"></span><span title="' + business + '" class="fourwrong">' + business + '</span></div>';//每年零时更新......
            html += '<div class="gender3_3">';
            html += '</div>';
            html += '<div class="gender3_4">';//
            if (resultTag.state == 1) {
                html += '';
            } else if (resultTag.state == 2) {
                html += '<span class="open_stop start" title="启动" style="cursor:pointer;color:#67c23a;" level="' + resultTag.level + '" data-status="' + resultTag.state + '" id="' + resultTag.tagId + '">启动</span>';
            } else if (resultTag.state == 3) {
                html += '<span class="open_stop stop" title="停止" style="cursor:pointer;color:#eecd14;" level="' + resultTag.level + '" data-status="' + resultTag.state + '" id="' + resultTag.tagId + '">停止</span>';
            } else if (resultTag.state == 4) {
                html += '<span class="open_stop start" title="启动" style="cursor:pointer;color:#67c23a;" level="' + resultTag.level + '" data-status="' + resultTag.state + '" id="' + resultTag.tagId + '">启动</span>';
            } else if (resultTag.state == 5) {
                html += '';
            }
            html += '<span class="fourEdit" title="编辑" style="cursor:pointer;display:none;color:rgb(64, 158, 255);" data-toggle="modal" data-target="#childrenModal" onclick="updateLevel4TagShow(this)" id="' + resultTag.tagId + '" level="' + resultTag.level + '">';
            html += '编辑</span>';
            html += '<span class="commondel commondelfour" title="删除" style="cursor:pointer;display:none;color:rgb(245, 108, 108);" level="' + resultTag.level + '"; id="' + resultTag.tagId + '">删除</span></div>';
            html += '</div>';
            if (null != subList) {
                //二级
                for (var j = 0; j < subList.length; j++) {
                    var subTag = subList[j];
                    html += getLevel5Html(subTag, j);
                }
            }
            html += '</div>';
        }

    }
    document.getElementById('contentTagList').insertAdjacentHTML('afterBegin', html);
    if (resultArr.length == 1) {
        $(".gender_in").css("display", "block");
    } else {
        $(".gender_in").css("display", "none");
    }
    if (resultArr.length == 1 && $("#breadDiv span:last").attr("value").split("`")[0] == 4) {
        $(".gender_in").css("display", "none");
    }
    if ($("#breadDiv span:last").attr("value").split("`")[1] == 4) {
        $(".year0").css({"background": "rgb(47, 164, 255)", "color": "rgb(255, 255, 255)"});
        $(".gender3").css({"background": "rgb(47, 164, 255)", "color": "rgb(255, 255, 255)"});
        $(".gender3_4 span").css("color", "#fff");
        $(".gender3_1buttom img").attr("src", "../res/imgs/refresh.png");
        $(".gender3_2 img").attr("src", "../res/imgs/worn.png");
    }

    //更新

    for (var i = 0; i < fourArr.length; i++) {
        if (fourArr[i] == "edit") {
            $("#contentTagList").find(".fourEdit").show();
        } else if (fourArr[i] == "del") {
            $("#contentTagList").find(".commondelfour").show();
        }

    }
    for (var i = 0; i < fiveArr.length; i++) {
        if (fiveArr[i] == "edit") {
            $("#contentTagList").find(".fiveEdit").show();
        } else if (fiveArr[i] == "del") {
            $("#contentTagList").find(".commondelfive").show();

        }

    }
}

// 拼接第五级标签
function getLevel5Html(subTag, m) {
    if (null == subTag.business || subTag.business == '' || subTag.business == undefined || subTag.business == "undefine") {
        var business = "暂无";
    } else {
        var business = subTag.business;
    }
    var tagName = subTag.tagName == null ? '' : subTag.tagName;
    var html = '';
    html += '<div class="gender_in" style="display:block;">';
    html += '<div class="gender4">';
    html += '<div class="gender2_right">';
    html += '<div class="gender4_on2">';
    html += '';
    html += '<div class="gender4_on2_1">';
    html += '<span style="color: #3385ff; padding: 0px 10px;" class="shu_name" value="180" value=' + subTag.tagId + ' title=' + tagName + '>' + tagName + '</span>';
    html += '</div>';
    html += '<div class="gender4_on2_2">';
    html += '<span><img src="../res/imgs/wornN.png" style="margin-top:-35px;width:16px;"></span><span style="margin-left: 5px;" class="fivewrong" title="' + business + '">' + business + '</span>';
    html += '</div>';
    html += '<div class="gender4_on2_3">';
    html += '<div class="gender4_on2_3T">';
    html += '<span style="margin-left: 15px !important;">' + formatNumber(subTag.userCount, 0, 1) + '</span><span>用户拥有该标签</span>';
    html += '</div>';
    html += '<div class="gender4_on2_3B"></div>';
    html += '</div>';
    html += '<div class="gender4_on2_4">';
    html += '<span class="fiveEdit" title="编辑" style="cursor:pointer;display:none;color:rgb(64, 158, 255);" level="' + subTag.level + '" id=' + subTag.tagId + '  data-toggle="modal" data-target="#smallchildrenModal" onclick="updateLevel5TagShow(this)" value=' + subTag.tagId + '>编辑</span>';
    html += '<span class="commondel commondelfive" title="删除" style="cursor:pointer;display:none;color:rgb(245, 108, 108);" level="' + subTag.level + '" id=' + subTag.tagId + '>删除</span>';// value='+subTag.id+' onclick="deleteFifth(this)"
    html += '</div>';
    html += '</div>';
    html += '</div>';
    html += '</div>';
    html += '<div style="display:none" class="gender_in_fifth"></div>';
    html += '</div>';
    return html;
}


//标签名称验证
$.validator.addMethod("isUser", function (value, element) {
    var length = value.length;
    var user = /^[A-Za-z0-9_\-\u4e00-\u9fa5]+$/;
    return this.optional(element) || user.test(value);
}, "请正确填写标签名称");
//五级新建和编辑校验
$(".smallchildrenModalLabel").validate({
    rules: {
        addfifthName: {
            required: true,
            isUser: true
        },
        fifthLabelRules: {
            required: true
        },
        fifthMeanings: {
            required: true
        }
    },
    messages: {
        addfifthName: {
            required: "请输入标签名称",
            isUser: "请正确填写标签名"
        },
        fifthLabelRules: {
            required: "请输入标签规则"
        },
        fifthMeanings: {
            required: "请输入业务含义"
        }
    }
})
//四级标签判断是否存在
$("#childrenModal").on("blur", "#levelFourName", function () {
    var fourtagname = $("#levelFourName").val(),
        pid = $(".label_cont_top_l").eq(2).attr("value").split("`")[0];
    jQuery.axsGet(true, "../basic/isExistForName", {'fivetagname': fourtagname, 'pid': pid}, function (data) {
        if (data.code == 11008) {
            $(".fourlabelname_worn").show();
        } else {
            $(".fourlabelname_worn").hide();
        }
    }, function (xhr) {
        layer.msg("数据服务错误！");
    })

}).on("focus", "#levelFourName", function () {
    $(".fourlabelname_worn").hide();
})
//五级标签判断是否存在
$("#smallchildrenModal").on("blur", "#levelFifthName", function () {
    var fivetagname = $("#levelFifthName").val(),
        pid = $(".label_cont_top_l").eq(3).attr("value").split("`")[0];
    jQuery.axsGet(true, "../basic/isExistForName", {'fivetagname': fivetagname, 'pid': pid}, function (data) {
        if (data.code == 11008) {
            $(".labelname_worn").show();
        } else {
            $(".labelname_worn").hide();
        }
    }, function (xhr) {
        layer.msg("数据服务错误！");
    })

}).on("focus", "#levelFifthName", function () {
    $(".labelname_worn").hide();
})

//创建或更新基础标签
$("#createsmallchildrentype").on('click', function (e) {
    var flag = $(".smallchildrenModalLabel").valid();
    if (!flag) {
        //没有通过验证
        return;
    }
    if ($(".labelname_worn").css("display") == "inline") {
        //没有通过验证
        return;
    }
    var layerIndex = layer.load(0, {shade: [0.3, '#ccc'], offset: ["50%", "50%"]});
    var levelFifthName = $("#levelFifthName").val(),//标签名称
        fifthMeanings = $("#fifthMeanings").val(),//标签含义
        fifthLabelRules = $("#fifthLabelRules").val();//标签规则
    var createButton = document.getElementById('createsmallchildrentype');
    //创建
    if (createButton.title == 'create') {
        //根据面包屑获取pid
        var breadData = getBreadIdAndLevel();
        var pid = breadData.tagId;
        var data = {
            'name': levelFifthName,
            'business': fifthMeanings,
            'industry': '标签',
            'rule': fifthLabelRules,
            'level': 5,
            'pid': pid //从面包屑获取
        };
        $.ajax({
            type: "POST",
            url: '../basic/addFifthBasicTag',
            dataType: "json",
            data: data,
            success: function (data) {
                if (data.code == 10002) {
                    layer.close(layerIndex);
                    layer.msg("添加成功");
                    $(".layui-layer-move").remove();
                    window.location.reload();
                } else {
                    layer.close(layerIndex);
                    layer.msg(data.msg);
                    $(".layui-layer-move").remove();
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                layer.close(layerIndex);
                layer.msg("数据服务错误！");
                $(".layui-layer-move").remove();
            }
        });
    }
    //更新
    if (createButton.title == 'update') {
        var levelFifthId = $("#levelFifthId").val();//标签ID
        var data = {
            'id': levelFifthId,
            'name': levelFifthName,
            'business': fifthMeanings,
            'rule': fifthLabelRules
        };
        $.ajax({
            type: "POST",
            url: '../basic/updateFifthBasicTag',
            dataType: "json",
            data: data,
            success: function (data) {
                if (data.code == 10003) {
                    layer.close(layerIndex);
                    layer.msg("更新成功");
                    $(".layui-layer-move").remove();
                    $("#smallchildrenModal").modal("hide");
                    window.location.reload();
                    //reloadContentList();
                } else {
                    layer.close(layerIndex);
                    layer.msg("更新失败");
                    $(".layui-layer-move").remove();
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                layer.close(layerIndex);
                layer.msg("数据服务错误！");
                $(".layui-layer-move").remove();
            }
        });
    }
    $("#createsmallchildrentype").modal("hide");
})
//模态框消失清空文本框内容
$('#smallchildrenModal').on('hidden.bs.modal', function () {
    emptyTextById('levelFifthId');
    emptyTextById('levelFifthName');
    emptyTextById('fifthMeanings');
    emptyTextById('fifthLabelRules');
    //修改按钮状态
    document.getElementById('createsmallchildrentype').title = 'create';
    document.getElementById('createsmallchildrentype').innerHTML = '创建';
})

//上传jar						  
$('#sub_hidefileupload').change(function () {
    var file = document.getElementById("sub_hidefileupload").files[0];
    if (!file) {
        return;
    } else if (file.name.indexOf(".jar") > 0) {
        var layerIndex = layer.load(0, {shade: [0.3, '#ccc'], offset: ["50%", "50%"]});
        var formData = new FormData();
        formData.append("file1", file);
        $.ajax({
            url: "../basic/modelUpload",
            type: "POST",
            data: formData,
            dataType: 'json',
            contentType: false,
            processData: false,
            success: function (data) {
                if (data.code == 10001) {
                    layer.close(layerIndex);
                    //layer.msg("上传成功！");
                    $(".layui-layer-move").remove();
                    $(".successtip").html("上传成功").show();
                    $('#sub_hidefileupload').attr('data-url', data.data);
                    if (file.name.length > 30) {
                        $('.fileName').val((file.name).substring(0, 30) + '....');
                    } else {
                        $('.fileName').val(file.name);
                    }

                } else {
                    layer.msg(data.msg);
                    $(".layui-layer-move").remove();
                }
            },
            error: function (xrh) {
                layer.msg("数据服务错误！");
                $(".layui-layer-move").remove();
            }
        });
    } else {
        layer.msg("请上传.jar结尾的文件");
        $(".layui-layer-move").remove();
    }
});
//点击新建业务标签和属性标签弹窗内容清空
$(".add_tab_n2").on("click", function () {
    $("#smallchildrenModal input[type='text']").val("");
    $("#smallchildrenModal textarea").val("");
    $("#smallchildrenModal label.error").text("");
    $('#createchildrentype').attr("data-val", "0&0");
    $("#createchildrentype").text("创建");
    $("#childrenModalLabel").text("新建四级标签");
    $("#smallchildrenModalLabel").text("新建五级标签");
    $("#childrenModal input[type='text']").val("");
    $("#childrenModal textarea").val("");
    $("#childrenModal input[type='file']").removeAttr("data-url");
    $("#childrenModal input[type='file']").removeAttr("aria-invalid");
    $("#childrenModal input[type='file']").removeClass("valid");
    $("#childrenModal label.error").text("");
    $(".successtip").hide();
    $(".Wdate").val(hms());
    $(".sortoneLabel").val($("#breadDiv .label_cont_top_l").eq(0).text());
    $(".sorttwoLabel").val($("#breadDiv .label_cont_top_l").eq(1).text());
    $(".sortthreeLabel").val($("#breadDiv .label_cont_top_l").eq(2).text());
    $("#levelFourName").removeAttr("disabled");
    $(".fourlabelname_worn").hide();
    $("#levelFifthName").removeAttr("disabled");
    $(".labelname_worn").hide();
})

//时间戳转换为年-月-日 时:分
function hms() {
    var date = new Date();//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    Y = date.getFullYear() + '-';
    M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' ';
    h = date.getHours() + ':';
    m = (date.getMinutes() < 10 ? '0' + (date.getMinutes()) : date.getMinutes());
    //s = (date.getSeconds() < 10 ? '0'+(date.getSeconds()) : date.getSeconds());
    return Y + M + D + h + m;
}

//四级标签创建和编辑的校验
$(".childrenfourModal").validate({
    rules: {
        addfourName: {
            required: true,
            isUser: true
        },
        fourLabelRules: {
            required: true
        },
        fourMeanings: {
            required: true
        },
        algorithmType: {
            required: true
        },
        algorithmName: {
            required: true
        }
    },
    messages: {
        addfourName: {
            required: "请输入标签名称",
            isUser: "请正确填写标签名"
        },
        fourLabelRules: {
            required: "请输入标签规则"
        },
        fourMeanings: {
            required: "请输入业务含义"
        },
        algorithmType: {
            required: "请输入算法类型"
        },
        algorithmName: {
            required: "请输入算法名称"
        }
    }
})
//新增四级标签
$("#createchildrentype").on('click', function (e) {
    var startTime = $(".startTime").val();
    startTime = new Date(Date.parse(startTime.replace(/-/g, "/")));
    startTime = startTime.getTime();
    var endTime = $(".endTime").val();
    endTime = new Date(Date.parse(endTime.replace(/-/g, "/")));
    endTime = endTime.getTime();
    var flag = $(".childrenfourModal").valid();
    if (!flag) {
        //没有通过验证
        return;
    }
    if (endTime < startTime) {
        layer.msg("结束日期不能小于开始日期！");
        return;
    }
    if ($("#sub_hidefileupload").attr("data-url") == undefined || $("#sub_hidefileupload").attr("data-url") == null) {
        layer.msg("请上传文件！");
        return;
    }

    var layerIndex1 = layer.load(0, {shade: [0.3, '#ccc'], offset: ["50%", "50%"]});
    var dataVal = $(this).attr("data-val").split('&'); // 取得按钮的标识值,为1时表示更新，为0则表示创建
    var flag1 = dataVal[0]; // 取得按钮的标识值,为1时表示更新，为0则表示创建
    var id = dataVal[1];
    var levelFourName = $("#levelFourName").val(),//标签名称
        fourMeanings = $("#fourMeanings").val(),//标签含义
        fourLabelRules = $("#fourLabelRules").val(),//标签规则
        algorithmMain = $("#algorithmMain").val(),//算法主程序入口
        algorithmName = $("#algorithmName").val(),//算法名称
        args = $("#algorithmmodel").val();//模型参数
//更新周期
    var scheTime1 = $(".updatechoice option:selected").text(),
        scheTime2 = $(".startTime").val(),
        scheTime3 = $(".endTime").val(),
        scheTime = scheTime1 + '#' + scheTime2 + '#' + scheTime3;
    var thirdLevelId = $("#breadDiv .label_cont_top_l").eq(2).attr("value").split("`")[0];
    var url = $("#sub_hidefileupload").attr("data-url");
    var data = {
        "tagId": id,
        "tagName": levelFourName,
        "business": fourMeanings,
        "industry": '属性',
        "rule": fourLabelRules,
        "threeTagId": thirdLevelId,
        "modelPath": url,
        "modelMain": algorithmMain,
        "modelName": algorithmName,
        "level": 4,
        "args": args,
        "scheTime": scheTime,
        "state": 1
    }
    var queryUrl = null;
    if (flag1 == "1") {
        queryUrl = "../basic/updateFourthBasicTag";
    } else {
        document.getElementById('createchildrentype').innerHTML = '创建';
        $("#childrenModalLabel label.error").text("");
        $("#childrenModalLabel").text("创建四级标签");
        queryUrl = "../basic/addFourtag";
    }
    jQuery.ajaxPost(true, queryUrl, data, function (data) {
        if (data.code == 10002) {
            $("#childrenModal").modal("hide");
            layer.close(layerIndex1);
            layer.msg(data.msg);
            window.location.reload();
        } else if (data.code == 10003) {
            $("#childrenModal").modal("hide");
            layer.close(layerIndex1);
            layer.msg(data.msg);
            window.location.reload();
        } else {
            layer.close(layerIndex1);
            layer.msg(data.msg);
            $(".layui-layer-move").remove();
        }
    }, function (xhr) {
        layer.close(layerIndex1);
        layer.msg("数据服务错误！");
        $(".layui-layer-move").remove();
    })
    $("#childrenModal").modal("hide");
})

//根据父ID获取子标签
function getSubTags(parentId, name) {
    var result = '';
    $.ajax({
        type: 'get',
        url: "../basic/queryBasicTagForPid",
        dataType: "json",
        data: {
            "pid": parentId,
            "name": name
        },
        async: false,
        success: function (data) {
            result = data;
        }
    });
    return result;
}


//新增标签
/*function addAjax(data){
	var id = '';
	$.ajax({  
		type:'post',  
		url:"../basic/addMainBasicTag",
		dataType:"json",
		data:data, 
		async: false,
		success:function(data){
			id = data.data;
		}  
	});
	return id;
}*/

//获取文本框中对应的数据ID（如果没有则为''）
function getIdBySelectAndText(selectName, textName) {
    var levelName = document.getElementById(textName).value;
    var select = document.getElementById(selectName);
    var options = select.options;
    var id = '';
    for (var i = 0; i < options.length; i++) {
        var text = options[i].text;
        var value = options[i].value;
        if (levelName == text) {
            id = value;
            break;
        }
    }
    return id;
}

//根据文本框ID获取内容
function getTextById(textName) {
    var value = document.getElementById(textName).value;
    return value;
}

//置空文本框
function emptyTextById(textName) {
    document.getElementById(textName).value = '';
}

//根据后台结果构建select HTML
function getSelectHtmlByData(data) {
    var html = '';
    if (null == data) {
        return html;
    }
    if (data.code == '10005') {
        var subTags = [];
        subTags = data.data;
        $.each(subTags, function (k, v) {
            html += '<option value="' + v.id + '">' + v.name + '</option>';
        })
    }
    return html;
}

//点击其他地方一二三级标签下拉框消失
var TempArr1 = [],
    TempArr2 = [],
    TempArr3 = [];//存储option
$("#oneModal").bind('click', function (e) {
    var e = e || window.event; //浏览器兼容性
    var elem = e.target || e.srcElement;
    while (elem) { //循环判断至跟节点，防止点击的是div子元素
        if (elem.id && (elem.id == 'typenum1' || elem.id == "levelOneName"
            || elem.id == 'typenum2' || elem.id == "levelTwoName"
            || elem.id == 'typenum3' || elem.id == "levelThreeName")) {
            return;
        }
        elem = elem.parentNode;
    }
    $('#typenum1,#typenum2,#typenum3').css('display', 'none'); //点击的不是div或其子元素

});

//点击下拉
function changeF(this_) {
    $(this_).prev("input").val($(this_).find("option:selected").text());
    $(this_).css({"display": "none"});
    //获取ID存储 val()
}

function setfocus(this_) {
    $(this_).next().css({"display": "block"});
    var select = $(this_).next();
    var data;
    if ($(this_).next().attr("id") == "typenum1") {
        //获取一级标签
        data = getSubTags(-1, getTextById("levelOneName"));
    } else if ($(this_).next().attr("id") == "typenum2") {
        var id = getIdBySelectAndText("typenum1", "levelOneName");
        if ('' != id) {
            //根据id查
            //获取一级标签
            data = getSubTags(id, getTextById("levelTwoName"));
        }
    } else if ($(this_).next().attr("id") == "typenum3") {
        var id = getIdBySelectAndText("typenum2", "levelTwoName");
        if ('' != id) {
            data = getSubTags(id, getTextById("levelThreeName"));
        }
    }
    var html = getSelectHtmlByData(data);
    select.html(html);
    setinput(this_);
}

function setinput(this_) {
    var select = $(this_).next();
    select.html("");
    var data;
    if ($(this_).next().attr("id") == "typenum1") {
        //获取一级标签
        data = getSubTags(-1, getTextById("levelOneName"));
        //清空后边两个文本框
        emptyTextById("levelTwoName");
        emptyTextById("levelThreeName");
    } else if ($(this_).next().attr("id") == "typenum2") {
        //获取二级标签
        var pid = getIdBySelectAndText("typenum1", "levelOneName");
        if ('' != pid) {
            data = getSubTags(pid, getTextById("levelTwoName"));
        }
        //清空第三个文本框
        emptyTextById("levelThreeName");
    } else if ($(this_).next().attr("id") == "typenum3") {
        //获取二级标签
        var pid = getIdBySelectAndText("typenum2", "levelTwoName");
        if ('' != pid) {
            data = getSubTags(pid, getTextById("levelThreeName"));
        }
    }
    var html = getSelectHtmlByData(data);
    select.html(html);
}

//点击创建一二三级标签
$("#createonetype").on('click', function (e) {
    var levelOneName = $("#levelOneName").val(),
        levelTwoName = $("#levelTwoName").val(),
        levelThreeName = $("#levelThreeName").val(),
        name = /^[A-Za-z0-9_\-\u4e00-\u9fa5]+$/;
    if (levelOneName == "") {
        layer.msg("一级标签名称不能为空！");
        return;
    } else {
        if (levelTwoName == "") {
            if (levelThreeName != "") {
                layer.msg("二级标签名称不能为空！");
                return;
            } else {
                if (name.test(levelOneName) && (name.test(levelTwoName) || levelTwoName == "") && (name.test(levelThreeName) || levelThreeName == "")) {
                    var labelAll = [{'name': levelOneName, 'level': '1'}, {
                        'name': levelTwoName,
                        'level': '2'
                    }, {'name': levelThreeName, 'level': '3'}];
                    jQuery.axsPost(true, "../basic/addMainBasicTag", labelAll, function (data) {
                        layer.msg(data.msg);
                        if (data.code == 10002) {
                            window.location.reload();
                        }
                    }, function (xhr) {
                        layer.msg("数据服务错误！")
                    })
                } else {
                    layer.msg("标签名称为数字，字母，汉字，下划线，横杠！");
                }
            }
        } else {
            if (name.test(levelOneName) && (name.test(levelTwoName) || levelTwoName == "") && (name.test(levelThreeName) || levelThreeName == "")) {
                var labelAll = [{'name': levelOneName, 'level': '1'}, {
                    'name': levelTwoName,
                    'level': '2'
                }, {'name': levelThreeName, 'level': '3'}];
                jQuery.axsPost(true, "../basic/addMainBasicTag", labelAll, function (data) {
                    layer.msg(data.msg);
                    if (data.code == 10002) {
                        window.location.reload();
                    }
                }, function (xhr) {
                    layer.msg("数据服务错误！")
                })
            } else {
                layer.msg("标签名称为数字，字母，汉字，下划线，横杠！");
            }
        }

    }
});


//获取左侧标签列表
function getBasicTag() {
    var result = '';
    $.ajax({
        type: 'get',
        url: "../basic/queryBasicTag",
        dataType: "json",
        data: {},
        async: false,
        success: function (data) {
            result = data;
        }
    });
    return result.data;
}

//点击左侧标签列表
function spanClick(tag) {
    var title = $(tag).attr("title");
    var value = $(tag).attr("value");
    var tagId = value.split('-')[0];
    var level = value.split('-')[1];
    var breadData = [];
    //level 3\4更新
    if (level == 3 || level == 4) {
        $("#query").val("");
        if (level == 4) {
            var breadOnemsg = $(tag).parents(".zi").prev(".firstGr").find("span").attr("value");
            var breadOne = {
                "id": breadOnemsg.split('-')[0],
                "level": breadOnemsg.split('-')[1],
                "name": $(tag).parents(".zi").prev(".firstGr").find("span").attr("title")
            };
            var breadTwomsg = $(tag).parents(".secondzi").prev(".secondGr").find(".second_img").attr("value");
            var breadTwo = {
                "id": breadTwomsg.split('-')[0],
                "level": breadTwomsg.split('-')[1],
                "name": $(tag).parents(".secondzi").prev(".secondGr").find(".second_img").attr("title")
            };
            var breadThreemsg = $(tag).parents(".fourth").prev(".we").find(".we_div_span").attr("value");
            var breadThree = {
                "id": breadThreemsg.split('-')[0],
                "level": breadThreemsg.split('-')[1],
                "name": $(tag).parents(".fourth").prev(".we").find(".we_div_span").attr("title")
            };
            var breadFour = {
                "id": tagId,
                "level": level,
                "name": $(tag).attr("title")
            };
            breadData.push(breadOne);
            breadData.push(breadTwo);
            breadData.push(breadThree);
            breadData.push(breadFour);
        } else if (level == 3) {
            var breadOnemsg = $(tag).parents(".zi").prev(".firstGr").find("span").attr("value");
            var breadOne = {
                "id": breadOnemsg.split('-')[0],
                "level": breadOnemsg.split('-')[1],
                "name": $(tag).parents(".zi").prev(".firstGr").find("span").attr("title")
            };
            var breadTwomsg = $(tag).parents(".secondzi").prev(".secondGr").find(".second_img").attr("value");
            var breadTwo = {
                "id": breadTwomsg.split('-')[0],
                "level": breadTwomsg.split('-')[1],
                "name": $(tag).parents(".secondzi").prev(".secondGr").find(".second_img").attr("title")
            };
            var breadThreemsg = $(tag).parents(".fourth").prev(".we").find(".we_div_span").attr("value");
            var breadThree = {
                "id": tagId,
                "level": level,
                "name": $(tag).attr("title")
            };
            breadData.push(breadOne);
            breadData.push(breadTwo);
            breadData.push(breadThree);
        }
        //更新面包屑
        refreshBread(breadData);
        //更新内容列表
        var resultList = getTags(tagId, level, '');
        refreshContentList(resultList);
    }
}

//面包屑更新
function refreshBread(data) {
    var html = '';
    $('#breadDiv').html("");
    for (var i = 0; i < data.length; i++) {
        var subData = data[i];
        var value = subData.id + '`' + subData.level;
        html += '<span id="label_level1" class="label_cont_top_l label_cont_top_l1" value=' + value + ' title=' + subData.name + '>' + subData.name + '</span>';
        if (i != data.length - 1) {
            html += '<span class="label_nav_sp2">></span>';
        }
    }
    document.getElementById('breadDiv').insertAdjacentHTML('afterBegin', html);
}

//启动和停止
$(".drop_down").on("click", ".open_stop", function () {
    var status_id = $(this).attr("id"),
        status = $(this).attr("data-status"),
        json = {
            'id': status_id,
            'state': status
        };

    jQuery.axsGet(true, "../basic/taskProcessing", json, function (data) {
        if (data.code == 10001) {
            layer.msg("操作成功");
            $(".layui-layer-move").remove();
            var bread = getBreadIdAndLevel();
            var resultList = getTags(bread.tagId, bread.level, '');
            refreshContentList(resultList);
        }
    }, function (xhr) {
        layer.msg("数据服务错误！");
        $(".layui-layer-move").remove();
    })
})
// 闭包使用方法
var TagInit = function () {
    // 内部变量或方法
    var selectedTree = {}; // 已选的标签树
    for (var i = 1; i < 6; i++) {
        var tagN = 'level' + i;
        selectedTree[tagN] = new Array();
    }

    // 扩展原生方法
    Array.prototype.remove = function (val) {
        var index = this.indexOf(val);
        if (index > -1) {
            this.splice(index, 1);
        }
    };
    if (typeof String.prototype.startsWith != 'function') {
        String.prototype.startsWith = function (prefix) {
            return this.slice(0, prefix.length) === prefix;
        };
    }
    if (typeof String.prototype.endsWith != 'function') {
        String.prototype.endsWith = function (suffix) {
            return this.indexOf(suffix, this.length - suffix.length) !== -1;
        };
    }

    function getTagHtml(id, pid, level, name, isChecked) {
        if (isChecked) {
            return '<li data-id="' + id + '" data-pid="' + pid + '" data-level="' + level + '"><input type="checkbox" checked><span>' + name + '</span></li>';
        } else {
            return '<li data-id="' + id + '" data-pid="' + pid + '" data-level="' + level + '"><input type="checkbox"><span>' + name + '</span></li>';
        }
    }

    // 现在每次加载的子级标签都不被选中
    function renderTag(data, level) { // 渲染数据
        $('#tag' + level).html("");	 // 先清空
        var tagHtml = "";
        for (let i = 0; i < data.length; i++) {
            tagHtml += getTagHtml(data[i].id, data[i].pid, data[i].level, data[i].name, false);
        }
        $('#tag' + level).html(tagHtml);

    }

    // 初始化监听事件
    $(".centent_box").on("click", "li > input", function () {
        // 获取点击的标签信息
        var id = $(this).parent().data('id');
        var pid = $(this).parent().data('pid');
        var level = $(this).parent().data('level');
        var str = $(this).next().text();

        // 处理选中的标签树
        if ($(this).is(":checked")) {
            // 渲染子级
            TagInit.queryTagByPid(id);

            TagInit.selectedTreeAddTag(id, pid, level, str);
        } else {
            TagInit.selectedTreeRemoveTag(id, level, false);
            // 删除下级
            delTagN(parseInt(level) + 1);
        }

        // 处理前台展示N级标签的数据
        TagInit.updateSelTagN(level);
    });

    // 首页
    $("#sample-table_info").on("click", ".home_page", function () {
        $("#curr").html(1);
        $('.searchbtn input').click();
    });
    // 上一页
    $("#sample-table_info").on("click", ".up_page", function () {
        var curr = Number($("#curr").html());
        var tota = Number($("#tota").html());
        if (curr != 1 && curr < tota) {
            $("#curr").html(curr - 1);
            $('.searchbtn input').click();
        }
    });
    // 下一页
    $("#sample-table_info").on("click", ".next_page", function () {
        var curr = Number($("#curr").html());
        var tota = Number($("#tota").html());
        if (curr < tota) {
            $("#curr").html(curr + 1);
            $('.searchbtn input').click();
        }
    });
    // 尾页
    $("#sample-table_info").on("click", ".end_page", function () {
        $("#curr").html($("#tota").html());
        $('.searchbtn input').click();
    });
    // 跳页
    $("#sample-table_info").on("click", ".search_page", function () {
        var tar = Math.ceil($("#sample-table_info input").val());
        var tota = Number($("#tota").html());
        if (tar && tar > 0 && tar <= tota) {
            $("#curr").html(tar);
            $('.searchbtn input').click();
        }
    });

    $('.del').click(function () {
        var targetTag = $(this).parent().prev().find('div');
        var targetClass = $(targetTag).attr('class');
        var targetLevel = targetClass.substring(targetClass.length - 1, targetClass.length);
        // 更新已选标签
        selectedTree['level' + targetLevel] = [];
        // 更新显示标签
        targetTag.html('');
        // 更新选中项
        $('#tag' + targetLevel).find('input').each(function (i) {
            if ($(this).is(":checked")) {
                $(this).removeAttr("checked");
            }
        });
        // 取消下级选中
//		clickDel(parseInt(targetLevel)+1);
        // 删除下级
        delTagN(parseInt(targetLevel) + 1);
    });

    function clickDel(level) {
        // 取消选中
        $('.selStr' + level).parent().next().find('.del').click();
        if (level < 5) {
            clickDel(level + 1);
        }
    }

    function delTagN(level) {
        // 取消选中
        $('#tag' + level).children().remove();
        if (level < 5) {
            delTagN(level + 1);
        }
    }

    $('.searchbtn input').click(function () {
        var ids = TagInit.getSelTag2DB();
        var curr = $('#curr').html();
        var size = $('#size').html();
        if (!ids) {
            alert('请至少选择一个标签！');
            return false;
        }
        if (!curr || curr < 1) curr = 1;
        if (!size || size < 1) size = 10;
//		jQuery.axsPost(true, basePath + "/search/queryUserByTagIds",{ids: selectedTag.join('')},function(data){

//  		},function(xhr){layer.msg("服务器错误！")});

        $.ajax({
            url: basePath + "/search/queryUserByTagIds",
            type: 'POST',
            async: false,
            data: {ids: ids, page: curr, pageSize: size},
            timeout: 5000,    //超时时间
            dataType: 'json',
            success: function (data) {
                $("#page1").hide();
                $("#page2").show();
                if (data.data && data.data.count > 0) {
                    console.log(data.data);
                    TagInit.renderTagList(data.data);
                } else {
                    $('#tagList').html('');
                }
            },
            error: function (xhr) {
                layer.msg('错误');
            }
        });
    });

    return {
        init: function () { // 对外暴漏的方法
            // 获取一级标签并渲染
            TagInit.queryTagByPid();

        },
        getSelTagTree: function () { 	// 查看哪些标签选中了
            return selectedTree;
        },
        queryTagByPid: function (pid) { // 查询子标签
            var req = {"pid": "-1"};
            if (typeof pid != 'undefined') {
                req = {"pid": pid};
            }
            jQuery.axsGet(true, basePath + "/basic/queryBasicTagForPid", req, function (data) {
                if (data.code == 10005 && data && data.data.length > 0) {
                    renderTag(data.data, data.data[0].level);
                    for (var i = 1; i < ($(".centent_box>div .ulbox").length) + 1; i++) {
                        if (data.data[0].level == i) {
                            if (data.data.length > 8) {
                                if (data.data.length % 4 != 0) {
                                    var hei = ((parseInt((data.data.length) / 4)) + 1) * 30;
                                } else {
                                    var hei = (parseInt((data.data.length) / 4)) * 30;
                                }
                                $(".centent_box>div").find(".ulbox" + i).parent().css("height", "60px");
                                $(".centent_box>div").find(".ulbox" + i).find("ul").css({"height": hei + 'px'});
                                $(".centent_box>div").find(".ulbox" + i).find("div").css({
                                    "height": '60px',
                                    "overflow": "hidden"
                                });
                                $(".centent_box>div").find(".ulbox" + i).next(".more").html("更多<img src='../res/imgs/down.png'>").show();
                            } else if (data.data.length <= 8 && data.data.length > 4) {
                                $(".centent_box>div").find(".ulbox" + i).parent().css("height", "60px");
                                $(".centent_box>div").find(".ulbox" + i).find("ul").css("height", "60px");
                                $(".centent_box>div").find(".ulbox" + i).find("div").css("height", "60px");
                                $(".centent_box>div").find(".ulbox" + i).next(".more").html("更多<img src='../res/imgs/down.png'>").hide();
                            } else if (data.data.length <= 4) {
                                $(".centent_box>div").find(".ulbox" + i).parent().css("height", "30px");
                                $(".centent_box>div").find(".ulbox" + i).find("ul").css("height", "30px");
                                $(".centent_box>div").find(".ulbox" + i).find("div").css("height", "30px");
                                $(".centent_box>div").find(".ulbox" + i).next(".more").html("更多<img src='../res/imgs/down.png'>").hide();
                            }
                        }
                    }

                }
            }, function (xhr) {
                layer.msg("服务器错误！")
            });
        },
        updateSelTagN: function (level) {
            $('.selStr' + level).html('');
            $('.selStr' + level).html(TagInit.getSelTagNStr(level));
            if (level < 5) {
                TagInit.updateSelTagN(level + 1);
            }
        },
        selectedTreeAddTag: function (id, pid, level, name) {
            selectedTree['level' + level][selectedTree['level' + level].length] = {
                "id": id,
                "pid": pid,
                "name": name
            };
        },
        selectedTreeRemoveTag: function (id, level, isChild) {
            // 处理自己
            if (!isChild) {
                $.each(selectedTree['level' + level], function (key, val) {
                    if (id == val.id) {
                        selectedTree['level' + level].remove(this);
                    }
                });
            } else { // 处理的是子级标签
                $.each(selectedTree['level' + level], function (key, val) {
                    if (id == val.pid) {
                        selectedTree['level' + level].remove(this);
                    }
                });
            }
            // 处理子级
            if (level < 5) {
                TagInit.selectedTreeRemoveTag(id, level + 1, true);
            }
        },
        getSelTagNStr: function (level) { // 获取选中的N级标签名称
            var name = '';
            $.each(selectedTree['level' + level], function (key, val) {
                name += '<span data-id="' + val.id + '">' + val.name + '</span>' + '、';
            });
            if (name.endsWith('、')) name = name.substring(0, name.length - 1);
            return name;
        },
        getSelTag2DB: function () {
            var ids = '';
            for (let i = 1; i < 6; i++) {
                if (selectedTree['level' + i].length == 0) continue;
                $.each(selectedTree['level' + i], function (key, val) {
                    ids += val.id + ',';
                });
            }
            if (ids.endsWith(',')) ids = ids.substring(0, ids.length - 1);
            return ids;
        },
        renderTagPage: function (page) {
            $('#curr').html(page.page);
            $('#tota').html(Math.ceil(page.count / page.pageSize));
            $('#size').html(page.pageSize);
            $('#reco').html(page.count);
        },
        renderTagList: function (data) {
            $('#tagList').html('');
            var tagListHtml = '';
            $.each(data.data, function (key, val) {
                tagListHtml +=
                    '<tr>                           ' +
                    '	<tr>                        ' +
                    '	<td>' + val.name + '</td>       ' +
                    '	<td>' + val.idNum + '</td>      ' +
                    '	<td>' + val.phone + '</td>      ' +
                    '	<td>' + val.bankNum + '</td>    ' +
                    '</tr>                          ';

            });
            $('#tagList').html(tagListHtml);
            TagInit.renderTagPage(data);
        }
    };
}();
if (baseMethod.whatBrowser() == 'Chrome') {
    $(".guide_lead").css('line-height', '21px');
}
//地址栏获取参数
(function ($) {
    $.getUrlParam = function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }
})(jQuery);
$("#cancel").bind('click', function () {
    console.log()
    window.location.href = '../merge/index';
});


var clientHeight = window.screen.height - 50;
var objectObj = $(".con_gr");
objectObj.css("height", clientHeight + 'px');

var MutationObserver = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver;
var mutationObserverSupport = !!MutationObserver;

window.onload = function () {
    if (mutationObserverSupport) {
        var callback = function (records) {
            formula();
        };
        var mo = new MutationObserver(callback);
        var option = {
            'childList': true,
            'subtree': true,
        };
        var addtocontent = document.getElementById('addtocontent');
        mo.observe(addtocontent, option);
    }
}

function formula() {
    $(".sersureaddlabel_oversee_watch").html("");
    $(".sersureaddlabel_oversee_watch").attr("data-value", "");
    var conditions = $('#addtocontent').find('.sersureaddlabel_item ');
    var target = $('.sersureaddlabel_oversee_watch');
    target.children().remove();
    conditions.each(function (index1) {
        var header_name = $(this).find('p').text();
        var header_id = $(this).find('p').attr('value');
        var relationship = $(this).find('select');
        var all = $('<div  class="overseeitem"></div>');
        var label_son = $('<label class="sersureaddlabel_oversee_text"></label>');
        label_son.attr('value', header_id);
        label_son.text(header_name);

        var content = $('<div  class="attributetext"></div>');
        var list = $(this).find('.hascontent span');
        list.each(function (index2) {
            var content_son = $('<label class="sersureaddlabel_oversee_text"></label>');
            content_son.attr('value', $(this).attr('value'));
            if (0 == list.length - 1) {
                content_son.text("(" + $(this).text() + ")");
            } else if (index2 == 0) {
                content_son.text("(" + $(this).text());
            } else if (index2 == list.length - 1) {
                content_son.text($(this).text() + ")");
            } else {
                content_son.text($(this).text());
            }
            content.append(content_son);
        });
        if (index1 != conditions.length - 1) {
            var p_son = $('<p class="sersureaddlabel_oversee_text"></p>');
            p_son.attr('value', relationship.val()).text(relationship.find("option:selected").text());
        } else {
            var p_son = $('');
        }
        all.append(label_son, content, p_son);
        target.append(all);
    });
}

function searchStr() {
    var searchval = $("#searchval").val();
    $(".usersureaddlabel_content").each(function fun() {
        var str = $(this).find("span").text();
        substr = searchval;
        if (isContains(str, substr)) {
            $(this).css("display", "inline-block");
        } else {
            $(this).css("display", "none");
        }
    });
}

function isContains(str, substr) {
    return new RegExp(substr).test(str);
}

function strToDate(remindTime) {
    var str = remindTime.toString();
    str = str.replace("/-/g", "/");
    var oDate = new Date(str);
    return oDate
}

$(document).ready(function () {
    if ($.getUrlParam("id") == undefined || $.getUrlParam("id") == null) {
        $("#leftList .firstGr:nth-child(2)").find("img").attr("src", basePath + "/res/imgs/left_s11.png");
        $("#leftList .firstGr:nth-child(2)").find("span").css("color", "#fff");
        $("#leftList .zi:nth-child(2)").show();
        $("#leftList .zi:nth-child(2)").find(".secondGr").find("span").attr("style", "background:url(" + basePath + "/res/imgs/left_s1.png) no-repeat 0 10px !important;");
        $("#leftList .zi:nth-child(2)").find(".secondzi").find(".we").eq(0).show();
        $("#leftList .zi:nth-child(2)").find(".secondzi").show();
        $("#leftList .zi:nth-child(2)").find(".secondzi").find(".fourth").hide();
        $("#leftList .zi:nth-child(2)").find(".secondzi").find(".fourth").eq(0).show();
        $("#leftList .zi:nth-child(2)").find(".secondzi").find(".we").eq(0).find(".we_div").find("span").attr("style", "background:url(" + basePath + "/res/imgs/left_s1.png) no-repeat 0 10px !important;color: #00d2ff;");
    } else {
        $(".useraddlabel_nav_text").attr("value", $.getUrlParam("id"))
    }
    $("#searchbtn").bind("click", searchStr);
    $("#searchval").blur(searchStr);

    /**************************添加组合标签************************************/
    $('#addusergroup').click(function () {
        var req = new Object();
        req.mergeTagName = $('#usergroupname').val(); //组合标签名称
        req.purpose = $('#threeusergroupdeuse').text(); // 用途
        req.ctime = strToDate($("#starttime").val()); // 开始时间
        req.utime = strToDate($("#endtime").val()); // 结束时间
        req.listBasicTagId = $("#threeuseraddlabel_id").text(); // 基础标签id
        req.condition = $('#threeuseraddlabel_text').text(); // 组合标签条件
        req.intro = $('#usergroupmean').text(); // 组合标签含义
        req.state = 1;
        var idself = $.getUrlParam("id");
        if (idself != undefined && idself != null) {
            req.mergeTagId = idself;
            req.condition = $(".sersureaddlabel_oversee_watch").text();
            jQuery.axsGet(true, "../merge/updateMergeTag", req, function (data) {
                if (data.code == 10003) {
                    layer.confirm('更新成功！', {
                        btn: ['确定', '取消'] //按钮
                    }, function () {
                        window.location.href = '../merge/index';
                    }, function () {

                    });
                } else {
                    layer.msg(data.msg);
                }
            }, function (xhr) {
                layer.msg("更新失败！")
            })
        } else {
            $.ajax({
                url: '../merge/addMergeTag',
                type: 'post',
                async: false,
                data: req,
                timeout: 5000,    //超时时间
                dataType: 'json',
                success: function (data) {
                    if (data.code == 10002) {
                        layer.confirm('添加成功！', {
                            btn: ['确定', '取消'] //按钮
                        }, function () {
                            window.location.href = '../merge/index';
                        }, function () {

                        });
                    } else {
                        layer.msg(data.msg);
                    }
                },
                error: function (xhr) {
                    layer.msg('添加失败');
                }
            });
        }

    });

    /**************************点击第一步的条件删除************************************/
    $(document).on('click', "#addtocontent .sersureaddlabel_rec_delimg", function () {
        $(this).parents('.sersureaddlabel_item').remove();
        $(".sersureaddlabel_oversee_watch").html("");
        $(".sersureaddlabel_oversee_watch").attr("data-value", "");
        for (var i = 0; i < $("#addtocontent").find(".sersureaddlabel_item").length; i++) {
            $("#addtocontent").find(".sersureaddlabel_item").eq(i).find(".sersureaddlabel_rec_num").text(i + 1)
        }
    });

    /**************************点击1级出现2级标签************************************/
    $(document).on('click', ".firstGr", function () {
        var NOB = $(this).next(".zi");
        if (NOB.css("display") === "none") {
            $(this).siblings(".zi").slideUp(500);
            NOB.slideDown(500);
            NOB.css("background-color", "#424a51");
            $(".firstGr").find("img").attr("src", basePath + "/res/imgs/left_s22.png");
            $(".firstGr").find("span").css("color", "#a3a3a3");
            $(this).find("span").css("color", "#fff");
            $(this).find("img").attr("src", basePath + "/res/imgs/left_s11.png");
        } else {
            //关闭2级标签
            NOB.slideUp(500);
            $(this).find("img").attr("src", basePath + "/res/imgs/left_s22.png");
        }
    });
    /**************************点击4级出现右侧动态标签************************************/
    $(document).on('click', ".four .four_div", function () {
        var tagname = $(this).find('span').text();
        var tagid = $(this).find('span').attr('value');
        var id = tagid.split('-')[0];
        var level = tagid.split('-')[1];
        $('.first_text').text(tagname).attr('value', tagid);
        $(".zi span").css("color", "#fff");
        $(this).find("span").css("color", "#00d2ff");
        var req = new Object();
        req.id = parseInt(id);
        req.level = parseInt(level);
        $.ajax({
            url: '../basic/queryBasicTagForWithPid',
            type: 'get',
            async: false,
            data: req,
            timeout: 5000,    //超时时间
            dataType: 'json',
            success: function (data) {
                if (data.code == '10005' && data.data != null) {
                    var result = data.data[0].subTags;
                    createug.addCheckBox(result, req.id);
                }
            },
            error: function (xhr) {
                layer.msg('数据服务错误！');
            }
        });
    })

    /**************************点击3级出现4级标签************************************/
    $(document).on('click', '.we .we_div', function () {
        var onelabel = $(this).parents(".zi").prev(".firstGr").find("span").text();//一级标签的名称
        var ourid = $(this).find('span').text();//三级标签名称
        var twolabel = $(this).parents(".secondzi").prev().find('.second_img').text();//二级标签名称
        var twolabs = $(this).parents(".secondzi").siblings(".secondGr").find('.second_img');//其他二级标签名称
        var tagLevel1 = $(this).parents('.left_gr');
        var wedis = $(this).parent().next().css('display');
        $(".zi span").css("color", "#fff");
        $(this).find("span").css("color", "#00d2ff");
        if (wedis == "block") {
            $(this).parent().next().slideUp(500);
            $(this).find(".we_div_span").css("background", "url(../res/imgs/left_s2.png) no-repeat 0 10px");
        } else {
            $(this).parent().next().slideDown(500);
            $(this).find(".we_div_span").css("background", "url(../res/imgs/left_s1.png) no-repeat 0 10px");
        }
        // queryUGList(1,null);
        $("#five").css("display", "none");
        $("#four").css("display", "block");
    });

    /**************************点击2级出现3级标签************************************/
    $(document).on('click', ".secondGr", function () {
        //得到当前2级的3级的display
        var current = $(this);
        var dis = current.next(".secondzi").css('display');
        var spanimg = current.find("span.second_img");
        //判断当前的3级是否是合并的状态
        if (dis == 'none') {
            var tagLevel1 = $(this).parents('.left_gr');
            //关闭所有同级标签
            current.next(".secondzi").find(".we").show();
            current.next(".secondzi").slideDown(500);

            spanimg.css("background", "url(../res/imgs/left_s1.png) no-repeat 0 10px");
        } else {
            $(this).next(".secondzi").slideUp(500);
            spanimg.css("background", "url(../res/imgs/left_s2.png) no-repeat 0 10px");
        }
    });

    /**************************点击2级出现3级标签************************************/
    //格式化日期
    function FormatDate(strTime) {
        var date = new Date(strTime);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = date.getDate();
        d = d < 10 ? ('0' + d) : d;
        return y + '-' + m + '-' + d;
    }

    //点击第一步的下一步
    // 1. 第一个面板隐藏，第二个展示 2.把值传递给第三个界面
    $("#one_next").on('click', function () {
        //必须选择才能够传值到下一步循环遍历每一个item，除掉第一个
        var flagone = false;
        var basicId = $(this).attr('tagId');
        if ($("#addtocontent section").length >= 1) {
            flagone = true;
        }
        if (!flagone) {
            layer.msg('请填写或者选择完整的信息!');
        } else {
            //第一个隐藏
            var createusergroupcount_wrap = $(".createusergroupcount_wrap");
            var btngroup_wrap = $(".btngroup_wrap");
            var guide_line_active = $(".guide_line_active");
            var guide_one_img = $(".guide_one_img");
            createusergroupcount_wrap.css("display", "none");
            btngroup_wrap.css("display", "none");
            guide_line_active.attr("class", "guide_line");
            guide_one_img.css("background", "url(../res/imgs/circle.png) no-repeat");
            //第二步显示
            var createusergroupcount_twowrap = $(".createusergroupcount_twowrap");
            var btngroup_twowrap = $(".btngroup_twowrap");
            var guide_two_line = $(".guide_two_line");
            var guide_two_img = $(".guide_two_img");
            createusergroupcount_twowrap.css("display", "block");
            btngroup_twowrap.css("display", "block");
            guide_two_line.attr("class", "guide_two_line_active");
            guide_two_img.css("background", "url(../res/imgs/circle_active.png) no-repeat");
            var idself = $.getUrlParam("id");
            var basicTagName = $(".first_text").text();
            if (idself != undefined && idself != null) {
                jQuery.axsGet(true, "../merge/queryUpdateMergeTagData", {
                    'mergeTagId': idself,
                    'baisicLevel': 1,
                    'basicTagName': basicTagName
                }, function (data) {
                    if (data.code == 10005) {
                        var html = '';
                        $.each(data.data.basicTag, function (k, v) {
                            if (v.flag) {
                                html += "<option value='" + v.id + "' checked='checked'>" + v.name + "</option>";
                            } else {
                                html += "<option value='" + v.id + "'>" + v.name + "</option>";
                            }

                        })
                        $("#usergrouptype").html(html);
                        $("#usergroupname").val(data.data.mergeTag.name);
                        $("#usergroupmean").val(data.data.mergeTag.intro);
                        $("#usergroupdeuse").val(data.data.mergeTag.purpose);
                        $("#starttime").val(FormatDate(data.data.mergeTag.ctime));
                        $("#endtime").val(FormatDate(data.data.mergeTag.utime));
                    } else {
                        layer.msg(data.msg);
                    }
                }, function (xhr) {
                    layer.msg("数据服务错误！")
                })
            } else {
                jQuery.axsGet(true, "../merge/queryUpdateMergeTagData", {
                    baisicLevel: "1",
                    "basicTagId": basicId
                }, function (data) {
                    if (data.code == '10005' && data.data != null) {
                        var html = '';
                        var result = data.data;
                        var basictag = result.basicTag;
                        var mergetag = result.mergeTag;
                        $.each(basictag, function (k, v) {
                            html += "<option value='" + v.id + "'>" + v.name + "</option>";
                        })
                        $("#usergrouptype").html(html);
                    }
                }, function () {
                    layer.msg("服务错误！")
                })
            }

        }
    });
    //点击第二步的上一步
    var two_pro_btn = $(".two_pro_btn");
    two_pro_btn.on("click", function () {
        //第二个面板隐藏
        var createusergroupcount_twowrap = $(".createusergroupcount_twowrap");
        var guide_two_line = $(".guide_two_line_active");
        var guide_two_img = $(".guide_two_img");
        var btngroup_twowrap = $(".btngroup_twowrap");
        createusergroupcount_twowrap.css("display", "none");
        guide_two_line.attr("class", "guide_two_line");
        guide_two_img.css("background", "url(../res/imgs/circle.png) no-repeat");
        //第一个面板出现
        var createusergroupcount_wrap = $(".createusergroupcount_wrap");
        createusergroupcount_wrap.css("display", "block");
        //第一步按钮组出现
        var btngroup_wrap = $(".btngroup_wrap");
        btngroup_wrap.css("display", "block");
        var guide_line = $(".guide_line");
        guide_line.attr("class", "guide_line_active");
        var guide_one_img = $(".guide_one_img");
        guide_one_img.css("background", "url(../res/imgs/circle_active.png) no-repeat");

    });
    //点击第二步下一步
    // 1. 第二步的面板的隐藏第三个面板的出现 2. 传值给第三个面板
    var two_next_btn = $("#two_next_btn");
    two_next_btn.on('click', function () {
        /*第一步的条件*/
        var formula = '';
        var formula_id = '';
        if ($(".sersureaddlabel_oversee_watch").attr("data-value") != '') {
            formula_id = $(".sersureaddlabel_oversee_watch").attr("data-value");
            var addchoice = $(".sersureaddlabel_oversee_watch").text();
        } else {
            $('.sersureaddlabel_oversee_watch').find('.overseeitem').each(function () {
                var threelabel = $(this).children().first().text();
                formula = formula + threelabel + " ";
                var attribute = $(this).children('.attributetext').find('.sersureaddlabel_oversee_text');
                attribute.each(function (index) {
                    formula = formula + $(this).text() + " ";
                    if (index == 0) {
                        formula_id = formula_id + ' ';
                    }
                    formula_id = formula_id + $(this).attr('value') + " ";
                    if (index == attribute.length - 1) {
                        formula_id = formula_id + ' ';
                    } else {
                        formula_id = formula_id + 'or ';
                    }
                });
                var relationship = $(this).children('p');
                formula = formula + relationship.text() + " ";
                formula_id = formula_id + (typeof (relationship.attr('value')) == 'undefined' ? "" : relationship.attr('value') + " ");
            });
            //定义条件
            var addchoice = $(".sersureaddlabel_oversee_text").text();
        }
        /*第二步的表单*/
        var woektype = $("#usergrouptype option:selected").text();
        var usergroupname = $("#usergroupname");//组合名称
        var usergroupmean = $("#usergroupmean");//组合含义
        var usergroupdeuse = $("#usergroupdeuse");//组合用途
        var starttime = $("#starttime");
        var endtime = $("#endtime");
        //空值的判断
        if (usergroupname.val() == "" || usergroupmean.val() == "" || usergroupdeuse.val() == "" || starttime.val() == "" || endtime.val() == "") {
            layer.msg("请填写完整的信息!");
        } else {
            //日期的判断
            if (!checkdate()) {
            } else {
                //隐藏第二个面板的信息
                //行业分类
                var usergrouptype = $("#usergrouptype option:selected").text();
                //组合名称
                var usergroupname = $("#usergroupname").val();
                // 组合用途
                var usergroupmean = usergroupmean.val();
                //组合用途
                var usergroupdeuse = $("#usergroupdeuse").val();
                //有效时间
                var startTime = $("#starttime").val();
                var endTime = $("#endtime").val();


                var createusergroupcount_twowrap = $(".createusergroupcount_twowrap");
                var guide_two_line = $(".guide_two_line_active");
                var guide_two_img = $(".guide_two_img");
                createusergroupcount_twowrap.css("display", "none");
                guide_two_line.attr("class", "guide_two_line");
                guide_two_img.css("background", "url(../res/imgs/circle.png) no-repeat");
                // 展示第三个面板的信息
                var createusergroupcount_threewrap = $(".createusergroupcount_threewrap");
                var guide_three_line = $(".guide_three_line");
                var guide_three_img = $(".guide_three_img");
                createusergroupcount_threewrap.css("display", "block");
                guide_three_line.attr("class", "guide_three_line_active");
                guide_three_img.css("background", "url(../res/imgs/circle_active.png) no-repeat");
                //给第三个面板赋值
                $("#worktypeLabel").text(usergrouptype);//行业分类
                //组合名称
                $("#mergeName").text(usergroupname);
                $("#threeusergroupdeuse").text(usergroupdeuse);//组合用途
                //有效时间
                $("#time").text(startTime + '至' + endTime);
                //定义条件
                $('#threeuseraddlabel_text').text(addchoice);
                // 基础标签ID
                if ($(".sersureaddlabel_oversee_watch").attr("data-value") != '') {
                    $("#threeuseraddlabel_id").text(formula_id);
                } else {
                    $("#threeuseraddlabel_id").text(formula_id + 'and');
                }

                // 组合标签含义
                $("#usergroupmean").text(usergroupmean);
            }
        }
    });


    //点击第三步的上一步
    var three_pro_btn = $("#three_pro_btn");
    three_pro_btn.on("click", function () {
        //首先第三个面板隐藏
        var createusergroupcount_threewrap = $(".createusergroupcount_threewrap");
        createusergroupcount_threewrap.css("display", "none");
        //第三个面板线条置灰
        var guide_three_line = $(".guide_three_line_active");
        guide_three_line.attr("class", "guide_three_line");
        //第三个面板图片置灰
        var guide_three_img = $(".guide_three_img");
        guide_three_img.css("background", "url(../res/imgs/circle.png) no-repeat");
        //第二个面板出现
        var createusergroupcount_twowrap = $(".createusergroupcount_twowrap");
        createusergroupcount_twowrap.css("display", "block")
        var guide_two_line = $(".guide_two_line");
        var guide_two_img = $(".guide_two_img");
        guide_two_line.attr("class", "guide_two_line_active");
        guide_two_img.css("background", "url(../res/imgs/circle_active.png) no-repeat");
    });


    function checkdate() {
        //开始日期
        var s1 = document.getElementById("starttime").value;
        //结束日期
        var s2 = document.getElementById("endtime").value;
        var sDate = new Date(s1.replace(/\-/g, '/'));
        var eDate = new Date(s2.replace(/\-/g, '/'));
        if (sDate > eDate) {
            layer.msg("结束日期不能小于开始日期!");
            eDate = '';
            return false;
        }
        return true;
    }


});
//点击添加按钮操作1.出现在列表中2.出现在公式预览中
$(".userlabel_addbtn").bind("click", function () {
    //判断至少选择一个属性
    var SelectFalse = false; //用于判断是否被选择条件
    $(".check").each(function () {
        if ($(this).hasClass("checked")) {
            SelectFalse = true;
        }
    })
    if (!SelectFalse) {
        layer.msg("对不起至少要选一项!")
        return false;
    }
    if (SelectFalse) {
        var useraddlabel_nav_text = $(".first_text").text();   //四级标签name

        $(".check").each(function () {
            if ($(this).hasClass("checked")) {
                var useraddlabel_nav_text_id = $(this).next(".usersureaddlabel_text").attr('value').split('`')[0];  //四级标签id
                var choseId = $(this).next().attr("value").split('`')[1];
                var choseContent = $(this).next().text();
                var index = $('#addtocontent').children().length + 1;
                createug.addCondition(useraddlabel_nav_text, useraddlabel_nav_text_id, choseContent, choseId, index);
            }
        })
    }
});

var createug = {
    addTreeForThree: function (tagThrees, target) {
        target.html("");
        for (var i = 0; i < tagThrees.length; i++) {
            var current = tagThrees[i];
            var moban = $('#addTreeForThree').clone();
            moban.find('span').attr("value", current.id).attr("title", current.name).text(current.name);
            target.append(moban.html());
        }
    },
    addTreeForTwo: function (tagTwos, target) {
        for (var i = 0; i < tagTwos.length; i++) {
            var current = tagTwos[i];
            var moban = $('#addTreeForTwo').clone();
            moban.find('span').attr("value", current.id).attr("title", current.name).text(current.name);
            target.append(moban.html());
        }
    },
    addCheckBox: function (tagFour, id) {
        var target = $('.createusergroupcount_wrap .useraddlabel_wrap .usersureaddlabel_contentchoice');
        target.children().remove();
        if (tagFour != undefined) {
            for (var i = 0; i < tagFour.length; i++) {
                var current = tagFour[i];
                //var choose=$('<div class="usersureaddlabel_choose usersureaddlabel_choose_search"><input name="item"  class="usersureaddlabel_checkbox" type="checkbox"/></div>');
                var choose = $('<div class="usersureaddlabel_choose usersureaddlabel_choose_search"><s class="check"></s></div>');
                var usersureaddlabel_text_span = $('<span class="usersureaddlabel_text"></span>');
                usersureaddlabel_text_span.text(current.name);
                usersureaddlabel_text_span.attr('value', id + '`' + current.id);
                choose.append(usersureaddlabel_text_span);
                target.append(choose);
            }
        }

    },
    addCondition: function (name3, id3, name4, id4, index) {
        //console.log(name3+'====='+id3+'====='+name4+"====="+id4+"===="+index)
        $(".sersureaddlabel_oversee_watch").attr("data-value", "")
        var inspect = true;
        if ($.getUrlParam("id") == undefined || $.getUrlParam("id") == null) {
            if ($("#addtocontent").html() != '') {
                $('.sersureaddlabel_item div.sersureaddlabel_rec').each(function () {
                    if ($(this).find('p').attr('value') == id3) {
                        inspect = false;
                        var include = true;
                        $(this).find('.hascontent span').each(function () {
                            if ($(this).attr('value') == id4) {
                                include = false;
                            }
                            ;
                        });
                        if (include) {
                            var addspan = $('<span class="sersureaddlabel_rec_instruction"></span>');
                            addspan.text(name4).attr({'value': id4, 'title': name4}).css("cursor", "pointer");
                            $(this).find('.hascontent').append(addspan);
                        }
                    }
                });
            }
        } else {
            $('.sersureaddlabel_item div.sersureaddlabel_rec').each(function () {
                //console.log($(this).find('p').attr('value'))
                if ($(this).find('p').attr('value') == id3) {
                    inspect = false;
                    var include = true;
                    //console.log($(this).find('.hascontent span').length)
                    $(this).find('.hascontent span').each(function () {
                        if ($(this).attr('value') == id4) {
                            include = false;
                        }
                        ;
                    });
                    //console.log(include)
                    if (include) {
                        var addspan = $('<span class="sersureaddlabel_rec_instruction"></span>');
                        addspan.text(name4).attr({'value': id4, 'title': name4}).css("cursor", "pointer");
                        $(this).find('.hascontent').append(addspan);
                    }
                }
            });
        }


        if (inspect) {
            if ($.getUrlParam("id") != undefined || $.getUrlParam("id") != null) {
                //$(".addtocontent").empty();
                index = $(".sersureaddlabel_item").length;
                $(".sersureaddlabel_oversee_watch").empty();
            }
            var moban = $('#condition').clone();
            moban.find('.sersureaddlabel_rec_num').text(index);
            moban.find('p').text(name3).attr({'value': id3, 'title': name3}).css("cursor", "pointer");

            moban.find('.hascontent span').text(name4).attr({'value': id4, 'title': name4}).css("cursor", "pointer");
            $(".addtocontent").append(moban.html());
        }
    }
}
$(".useraddlabel_wrap").on("click", ".usersureaddlabel_contentchoice s.check", function () {
    if ($(this).hasClass("checked")) {
        $(this).removeClass("checked");
    } else {
        $(this).addClass("checked");
    }
})
$(".clearAll").on("click", function () {
    $("#addtocontent,.sersureaddlabel_oversee_watch").empty();
})
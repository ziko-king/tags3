$(document).ready(function () {
    $(document).on("click", ".labelEdit", function () {
        $(".modal-backdrop").eq(1).css("z-index", "1050")
    })
    var hei = window.screen.height - 310;
    $(".drop_down").css({"height": hei + "px", "overflow": "auto"});
    //点击左侧的三级标签，右侧页面显示四级标签和五级标签的内容
    var Ddrop_down = $('.drop_down');
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
        for (var i = 0; i < fourArr.length; i++) {
            if (fourArr[i] == "add") {
                $("#four").show();
                $("#five").hide();
            }
        }

    });

    //分页
    function queryUGList(curpage, pageSize) {
        var req = new Object();
        var state = $('.User_radio .User_radio_j input').prop('checked');
        req.state = state ? 1 : 0;
        req.curPage = curpage;
        pageSize = 10;
        req.pageSize = pageSize;
        req.type = $('#tag_type').text();
        $.ajax({
            url: basePath + '/tagAndSon',
            type: 'POST',
            async: false,
            data: JSON.stringify(req),
            timeout: 5000,    //超时时间
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            success: function (data) {
                if (data.code == 200 && data.data != null) {
                    var result = data.data;
                    if (result.tag != null) {
                        var tags = new Array(1);
                        tags[0] = result.tag;
                        tagman.addThree(tags);
                        tagman.addFour(result.tlist, req.tagtype, ourid);
                    }
                }
            },
            error: function (xhr) {
                layer.msg('数据服务错误！');
                $(".layui-layer-move").remove();
            }
        });
    }

    function vagueQueryUGList(curpage, pageSize) {
        var req = new Object();
        var state = $('.User_radio .User_radio_j input').prop('checked');
        req.state = state ? 1 : 0;
        req.name = $('#query').val();
        req.curPage = curpage;
        pageSize = 10;
        req.pageSize = pageSize;
        req.type = $('#tag_type').text();
        $.ajax({
            url: basePath + '/tagAndSon',
            type: 'POST',
            async: false,
            data: JSON.stringify(req),
            timeout: 5000,    //超时时间
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            success: function (data) {
                if (data.code == 200 && data.data != null) {
                    var result = data.data;
                    if (result.tag != null) {
                        var tags = new Array(1);
                        tags[0] = result.tag;
                        tagman.addThree(tags);
                        tagman.addFour(result.tlist, req.tagtype, ourid);
                    }
                }
            },
            error: function (xhr) {
                layer.msg('数据服务错误！');
                $(".layui-layer-move").remove();
            }
        });
    }

    function vagueQuery() {
        if ('' == $('#query').val()) {
            queryUGList(1, null);
            return false;
        }
        vagueQueryUGList(1, null);
    }

    /**************************点击四级出现当前四级和五级标签************************************/
    $(document).on('click', ".four .four_div", function () {    	//得到当前4级的3级的display
        var onelabel = $(this).parents(".zi").prev(".firstGr").find("span").text();//1级标签
        var twolabel = $(this).parents(".secondzi").prev().find('.second_img').text();//二级标签名称
        var fourlabel = $(this).find('span').text();//四级标签名称
        var threelabel = $(this).parent(".four").prev().find('span').text();//三级标签名称
        for (var i = 0; i < fiveArr.length; i++) {
            if (fiveArr[i] == "add") {
                $("#five").show();
                $("#four").hide();
            }
        }
        $(".zi span").css("color", "#fff");
        $(this).find("span").css("color", "#00d2ff");
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
//               current.next(".secondzi").find(".we").slideUp(500);
            spanimg.css("background", "url(../res/imgs/left_s2.png) no-repeat 0 10px");
        }
    });

    /**************************列表************************************/
    //右侧合并展开
    //右侧的第一个三级标签下拉效果
    $(document).on('click', '.drop_downRight .year0', function () {
        $(".year").find(".year0").css({"background": "#fff", "color": "#999"}).next().css({
            "background": "#fff",
            "color": "#999"
        });
        $(".year").find(".gender3").find(".gender3_4 .start").css("color", "#67c23a");
        $(".year").find(".gender3").find(".gender3_4 .stop").css("color", "#eecd14");
        $(".year").find(".gender3").find(".gender3_4 .fourEdit").css("color", "rgb(64, 158, 255)");
        $(".year").find(".gender3").find(".gender3_4 .commondelfour").css("color", "rgb(245, 108, 108)");
        $(".year").find(".year0").next().find(".gender3_1buttom img").attr("src", "../res/imgs/refreshN.png");
        $(".year").find(".year0").next().find(".gender3_2 img").attr("src", "../res/imgs/wornN.png");
        $(this).css({"background": "#2fa4ff", "color": "#fff"}).next().css({"background": "#2fa4ff", "color": "#fff"});
        $(this).next().find(".gender3_4 span").css("color", "#fff");
        $(this).next().find(".gender3_1buttom img").attr("src", "../res/imgs/refresh.png");
        $(this).next().find(".gender3_2 img").attr("src", "../res/imgs/worn.png");
        var shu_name = $(this).find(".shu_name");
        var tagtype = $(".label_cont_top").find(".label_cont_top_l").siblings('span').text();
        //展开4级
        if ($(this).parent().find(".gender_in").css("display") == "none") {
            $(".gender_in").slideUp(500);
            $(this).parent().find(".gender_in").slideDown(500);
        } else if ($(this).parent().find(".gender_in").css("display") == "block") {
            $(this).parent().find(".gender_in").slideUp(500);
        } else {
            var req = new Object();
            req.tagId = parseInt(shu_name.attr("value"));
            req.level = parseInt(shu_name.attr("level"));
            //发送异步请求
            $.ajax({
                url: basePath + '/basic/queryBasicTagAndModelForWithPid',
                type: 'get',
                async: false,
                data: req,
                timeout: 5000,    //超时时间
                dataType: 'json',
                success: function (data) {
                    if (data.code == 10005 && data.data != null) {
                        if (data.data[0].subTags != undefined && data.data[0].subTags != null) {
                            $.each(data.data[0].subTags, function (k, v) {
                                $('.year').append(getLevel5Html(v));
                            })
                        } else {
                            $(".gender_in").slideUp(500);
                        }
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
                },
                error: function (xhr) {
                    layer.msg('数据服务错误！');
                    $(".layui-layer-move").remove();
                }
            });

        }
    });

    //删除
    $(document).on("click", ".commondel", function () {
        var id = $(this).attr('id');
        var level = $(this).attr('level');
        var req = new Object();
        req.id = parseInt(id);
        req.level = parseInt(level);
        layer.confirm('是否删除该标签？', {
            btn: ['确定', '取消'] //按钮
        }, function () {
            //确定删除执行的ajax
            jQuery.ajaxPost(true, basePath + "/basic/delBasicTagForId", req, function (data) {
                if (data.code == 10004) {
                    layer.msg(data.msg);
                    location.reload();
                } else {
                    layer.msg(data.msg);
                    $(".layui-layer-move").remove();
                }
            }, function (xhr) {
                layer.msg("服务器错误！");
                $(".layui-layer-move").remove();
            })
        }, function () {

        });
        $(".layui-layer-move").remove();

    });

    /**************************点击1级出现2级标签************************************/
    $("#leftList .firstGr:nth-child(2)").find("img").attr("src", basePath + "/res/imgs/left_s11.png");
    $("#leftList .firstGr:nth-child(2)").find("span").css("color", "#fff");
    $("#leftList .zi:nth-child(3)").find(".secondGr").find("span").attr("style", "background:url(" + basePath + "/res/imgs/left_s1.png) no-repeat 0 10px !important;");
    $("#leftList .zi:nth-child(3)").find(".secondzi").find(".we").eq(0).show();
    $("#leftList .zi:nth-child(3)").find(".secondzi").show();
    $("#leftList .zi:nth-child(3)").find(".secondzi").find(".fourth").hide();
    $("#leftList .zi:nth-child(3)").find(".secondzi").find(".fourth").eq(0).show();
    $("#leftList .zi:nth-child(3)").find(".secondzi").find(".we").eq(0).find(".we_div").find("span").attr("style", "background:url(" + basePath + "/res/imgs/left_s1.png) no-repeat 0 10px !important;color: #00d2ff;");
//$("#leftList .zi:nth-child(3)").find(".secondzi").find(".fourth").show();

    $(document).on('click', ".firstGr", function () {
        //背景
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


    /**点击新建三级标签事件**/
    $('#three .add_tab_n2').click(function () {
        $('#oneLevelTag').children().remove();
        var req = new Object();
        req.onlyValid = 1;
        req.my = 1;
        $.ajax({
            url: basePath + '/queryMyOneBasicTag',
            type: 'POST',
            async: false,
            data: JSON.stringify(req),
            timeout: 5000,    //超时时间
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            success: function (data) {
                var tlist = data.data;
                for (var i = 0; i < tlist.length; i++) {
                    var ul_li1 = $('<li></li>');
                    var li_a = $('<a href="#" class="base_dropdown-menu_ul_a"></a>');
                    li_a.attr('value', tlist[i].id).attr('title', tlist[i].name);
                    li_a.text(tlist[i].name);
                    ul_li1.append(li_a);
                    var ul_li2 = $('<li role="separator" class="divider"></li>');
                    $('#oneLevelTag').append(ul_li1, ul_li2);
                }
            },
            error: function (xhr) {
                layer.msg('数据服务错误！');
                $(".layui-layer-move").remove();
            }
        });
    });

    var clientHeight = window.innerHeight;
    var objectObj = $(".con_gr");
    var heightaddTab = $(".add_tab").height();
    var heightNavbar = $("#navbar").height();
    /*console.log(clientHeight+"---"+heightaddTab+"---"+ heightNavbar);*/
    objectObj.css("height", (clientHeight - heightaddTab - heightNavbar) + 'px');
});

function QueryByCondition(onlyValid, my) {
    window.location.href = basePath + '/mge?onlyValid=' + onlyValid + '&my=' + my;
}

function CreateCompositeLabel() {
    var form = $('#form');
    var namevalue = "";
    var pathvalue = "";
    var pIdvalue = "";
    var cIdvalue = "";
    var pLevelvalue = "";
    $(".dian_name").each(function (index) {
        namevalue = $(this).text();
        pLevelvalue = $(this).children("div").text();
        if (pLevelvalue == 3) {
            pIdvalue = $(this).attr("value");
            cIdvalue = 0;
        }
        if (pLevelvalue == 4) {
            cIdvalue = $(this).attr("value");
            pIdvalue = $(this).children("span").text();
        }
        var spanvalue = "";
        $(this).parent().children("div").children("span").each(function () {
            spanvalue = spanvalue + "/" + $(this).text();
        })
        pathvalue = spanvalue;
        var name = $('<input type="text" name="tagCombination[' + index + '].name" value="' + namevalue + '"/>');
        var path = $('<input type="text" name="tagCombination[' + index + '].path" value="' + pathvalue + '"/>');
        var pId = $('<input type="text" name="tagCombination[' + index + '].pId"  value="' + pIdvalue + '"/>');
        var cId = $('<input type="text" name="tagCombination[' + index + '].cId"  value="' + cIdvalue + '"/>');
        var pLevel = $('<input type="text" name="tagCombination[' + index + '].pLevel"  value="' + pLevelvalue + '"/>');
        form.append(name);
        form.append(path);
        form.append(pId);
        form.append(cId);
        form.append(pLevel);

    });

    form.submit();
    return false;

}


//点击列表四级标签的编辑
$(".fourEdit").on("click", function () {
    $("#childrenModalLabel").text("编辑四级标签");
    $("#childrenModal label.error").text("");
    //标签名称验证
    /*$.validator.addMethod("isUser", function(value, element) {
        var length = value.length;
        var user = /^[A-Za-z0-9_\-\u4e00-\u9fa5]+$/;
        return this.optional(element) || user.test(value);
    }, "请正确填写标签名称");
    $(".childrenfourModal").validate({
      rules: {
          addfourName: {
              required: true,
              isUser: true
          },
          fourLabelRules: {
            required: true
          },
          fourMeanings : {
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
              isUser : "请正确填写标签名"
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
          algorithmName : {
              required : "请输入算法名称"
          }
      }
    })

    $("#labelsystem_childrenlabel").on('click',"#createchildrentype", function (e) {
      var flag = $(".childrenfourModal").valid();
      if (!flag) {
          //没有通过验证
          return;
      }
      var levelFourName = $("#levelFourName").val(),//标签名称
          fourMeanings = $("#fourMeanings").val(),//标签含义
          fourLabelRules = $("#fourLabelRules").val(),//标签规则
          algorithmType = $("#algorithmType").val(),//算法类型
          algorithmName = $("#algorithmName").val();//算法名称
      $.ajax({
              type: "POST",
              url: basePath+"/addTag",
              dataType:"json",
              data: JSON.stringify(data,null,2),
              contentType:"application/json",
              success: function(data){
                  if(data.code == 200){
                    layer.msg("添加成功!");
                    $("#childrenModal").modal("hide");
                     location.reload();

                  }else{
                    layer.msg(data.msg);
                    location.reload();
                  }
              },
              error: function(XMLHttpRequest, textStatus, errorThrown) {
                layer.msg("数据服务错误！");
                location.reload();
                  }
           });
      $("#childrenModal").modal("hide");
    });*/
});
//点击列表五级标签的编辑
$(".fiveEdit").on("click", function () {
    $("#smallchildrenModalLabel").text("编辑五级标签");
    $("#smallchildrenModal label.error").text("");
    //标签名称验证
    $.validator.addMethod("isUser", function (value, element) {
        var length = value.length;
        var user = /^[A-Za-z0-9_\-\u4e00-\u9fa5]+$/;
        return this.optional(element) || user.test(value);
    }, "请正确填写标签名称");

    $(".smallchildrenModalLabel").validate({
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
            }
        }
    })
    $("#createsmallchildrentype").on('click', function (e) {
        var flag = $(".smallchildrenModalLabel").valid();
        if (!flag) {
            //没有通过验证
            return;
        }
        var levelFourName = $("#levelFourName").val(),//标签名称
            fourMeanings = $("#fourMeanings").val(),//标签含义
            fourLabelRules = $("#fourLabelRules").val();//标签规则
        $.ajax({
            type: "POST",
            url: basePath + "/addTag",
            dataType: "json",
            data: JSON.stringify(data, null, 2),
            contentType: "application/json",
            success: function (data) {
                if (data.code == 200) {
                    layer.msg("添加成功!");
                    $("#smallchildrenModal").modal("hide");
                    location.reload();
                } else {
                    layer.msg(data.msg);
                    $(".layui-layer-move").remove();
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                layer.msg("数据服务错误！");
                $(".layui-layer-move").remove();
            }
        });
        $("#createsmallchildrentype").modal("hide");
    })
})

//点击一二三级标签的启用禁用
$(".main_openStop").on("click", function () {
    $.ajax({
        type: "POST",
        url: basePath + "/addTag",
        dataType: "json",
        data: JSON.stringify(data, null, 2),
        contentType: "application/json",
        success: function (data) {
            if (data.code == 200) {
                layer.msg("执行成功!");
                location.reload();
            } else {
                layer.msg(data.msg);
                $(".layui-layer-move").remove();
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.msg("数据服务错误！");
            $(".layui-layer-move").remove();
        }
    });
})



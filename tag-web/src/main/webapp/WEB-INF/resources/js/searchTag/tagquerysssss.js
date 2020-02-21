$(document).ready(function () {
    var bread = getBreadIdAndLevel();
    var name = $('#searchDiv').val();
    var result = queryByIdAndLevel(bread.id, bread.level, name);
    refreshContentList(result);
    //列表中的环形图表
//	if(null != result){
//		for(var i = 0; i <result.length; i++){
//			var tag = result[i];
//			var dom = document.getElementById("chart_"+tag.id);
//			  var myChart = echarts.init(dom);
//			  var option = {
//			    color:["blue","#999"],
//			    series: [
//			        {
//			            name:'访问来源',
//			            type:'pie',
//			            radius: ['40%', '60%'],
//			            hoverAnimation:false,
//			            avoidLabelOverlap: false,
//			            label: {
//			                normal: {
//			                    show: false,
//			                    position: 'center'
//			                },
//			            },
//			            labelLine: {
//			                normal: {
//			                    show: false
//			                }
//			            },
//			            data:[
//			                {value:335, name:'直接访问'},
//			                {value:30, name:'邮件营销'}
//			            ]
//			        }
//			    ]
//			  };
//			  myChart.setOption(option);
//		}
//	}

    //
    var hei = window.screen.height - 310;
    $(".drop_down").css({"height": hei + "px", "overflow": "auto"});
    //点击左侧的三级标签，右侧页面显示四级标签和五级标签的内容
    var Ddrop_down = $('.drop_down');
//    $(".left_gr").find("div").eq(0).attr("style","background:#435569!important");
    $(document).on('click', '.we .we_div', function () {
        $(".zi span").css("color", "#fff");
        $(this).find("span").css("color", "#00d2ff");
        var wedis = $(this).parent().next().css('display')
        if (wedis == "block") {
            $(this).parent().next().slideUp(500);
            $(this).find(".we_div_span").css("background", "url(../res/imgs/left_s2.png) no-repeat 0 10px");
        } else {
            $(this).parent().next().slideDown(500);
            $(this).find(".we_div_span").css("background", "url(../res/imgs/left_s1.png) no-repeat 0 10px");
        }
        var onelabel = $(this).parents(".zi").prev(".firstGr").find("span").text();//一级标签的名称
        var onelabelvalue = $(this).parents(".zi").prev(".firstGr").find("span").attr('value');//一级标签的id
        var onelabellevel = $(this).parents(".zi").prev(".firstGr").find("span").attr('level');//一级标签的level
        var twolabel = $(this).parents(".secondzi").prev().find('.second_img').text();//二级标签名称
        var twolabelvalue = $(this).parents(".secondzi").prev().find('.second_img').attr('value');//二级标签id
        var twolabellevel = $(this).parents(".secondzi").prev().find('.second_img').attr('level');//二级标签level
        var twolabs = $(this).parents(".secondzi").siblings(".secondGr").find('.second_img');//其他二级标签名称
        var ourid = $(this).find('span').text();//三级标签名称
        var threelabelvalue = $(this).find('span').attr('value');//三级标签id
        var threelabellevel = $(this).find('span').attr('level');//三级标签level
        var tagLevel1 = $(this).parents('.left_gr');
        var onelabelcombine = onelabelvalue + '`' + onelabellevel;
        var twolabelcombine = twolabelvalue + '`' + twolabellevel;
        var threelabelcombine = threelabelvalue + '`' + threelabellevel;
        var html = '<span class="label_cont_top_l label_cont_top_l1" value=' + onelabelcombine + ' title=' + onelabel + '>'
            + onelabel + '</span><span class="label_nav_sp2">></span>'
            + '<span class="label_cont_top_l label_cont_top_l2" value=' + twolabelcombine + ' title=' + twolabel + '>'
            + twolabel + '</span><span class="label_nav_sp2">></span>'
            + '<span class="label_cont_top_l label_cont_top_l3" value=' + threelabelcombine + ' title=' + ourid + '>'
            + ourid + '</span>';
        $(".nav_second").html(html);

        //加载右侧空间
        refreshList();

    });

    //根据当前id和level查数据
    function queryUGList(id, level) {
        var req = new Object();
        req.id = id;
        req.level = level;
        $.ajax({
            url: 'queryTagByIdAndLevel',
            type: 'GET',
            async: false,
            data: req,
            timeout: 5000,    //超时时间
            dataType: 'json',
            success: function (data) {
                if (data.code == 101 && data.data != null) {
                    var result = data.data;
                }
                if (data.code == 102) {
                    layer.msg('无数据');
                }
            },
            error: function (xhr) {
                layer.msg('错误');
            }
        });
    }

    function vagueQueryUGList(curpage, pageSize) {
        var req = new Object();
        //var state=$('.User_radio .User_radio_j input').prop('checked');
        //req.state=state?1:0;
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
                    //addPageInfo(result.pageBean)
                }
            },
            error: function (xhr) {
                layer.msg('错误');
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
    $(document).on('click', ".four .four_div", function () {
        /*$(".firstGr").attr("style","background: #435569");
        $(".secondGr").attr("style","background: #435569");
        $(".we .we_div").attr("style","background: #435569");
      $(".four .four_div").attr("style","background: #435569");
        $(this).attr("style","background:#2fa4ff!important");*/    	//得到当前4级的3级的display
        $(".zi span").css("color", "#fff");
        $(this).find("span").css("color", "#00d2ff");
        var onelabel = $(this).parents(".zi").prev(".firstGr").find("span").text();//1级标签
        var onelabelvalue = $(this).parents(".zi").prev(".firstGr").find("span").attr('value');//1级标签id
        var onelabellevel = $(this).parents(".zi").prev(".firstGr").find("span").attr('level');//1级标签level
        var twolabel = $(this).parents(".secondzi").prev().find('.second_img').text();//二级标签名称
        var twolabelvalue = $(this).parents(".secondzi").prev().find('.second_img').attr('value');//二级标签id
        var twolabellevel = $(this).parents(".secondzi").prev().find('.second_img').attr('level');//二级标签level
        var threelabel = $(this).parents(".fourth").prev().find('span').text();//三级标签名称

        var threelabelvalue = $(this).parents(".fourth").prev().find('span').attr('value');//三级标签id
        var threelabellevel = $(this).parents(".fourth").prev().find('span').attr('level');//三级标签level
        var fourlabel = $(this).find('span').text();//四级标签名称
        var fourlabelvalue = $(this).find('span').attr('value');//四级标签id
        var fourlabellevel = $(this).find('span').attr('level');//四级标签level

        var onelabelcombine = onelabelvalue + '`' + onelabellevel;
        var twolabelcombine = twolabelvalue + '`' + twolabellevel;
        var threelabelcombine = threelabelvalue + '`' + threelabellevel;
        var fourlabelcombine = fourlabelvalue + '`' + fourlabellevel;
        var html = '<span class="label_cont_top_l label_cont_top_l1" value=' + onelabelcombine + ' title=' + onelabel + '>'
            + onelabel + '</span><span class="label_nav_sp2">></span>'
            + '<span class="label_cont_top_l label_cont_top_l2" value=' + twolabelcombine + ' title=' + twolabel + '>'
            + twolabel + '</span><span class="label_nav_sp2">></span>'
            + '<span class="label_cont_top_l label_cont_top_l3" value=' + threelabelcombine + ' title=' + threelabel + '>'
            + threelabel + '</span><span class="label_nav_sp2">></span>'
            + '<span class="label_cont_top_l label_cont_top_l3" value=' + fourlabelcombine + ' title=' + fourlabel + '>'
            + fourlabel + '</span>';
        $(".nav_second").html(html);

        //加载右侧空间
        refreshList();

    });

    /**************************点击2级出现3级标签************************************/
    $(document).on('click', ".secondGr", function () {
        //得到当前2级的3级的display
        var current = $(this);
        var dis = current.next().css('display');
        var spanimg = current.find("span.second_img");
        //判断当前的3级是否是合并的状态
        if (dis == 'none') {
            var tagLevel1 = $(this).parents('.left_gr');
            //关闭所有同级标签
            /*current.siblings(".secondzi").css("display","none");
            current.siblings(".secondzi").find(".we").css("display","none");
            current.siblings(".secondzi").find(".four").css("display","none");*/
            current.siblings(".secondzi").find(".we").eq(0).find(".we_div_span").css("background", "url(../res/imgs/left_s2.png) no-repeat 0 10px");
            //打开当前标签
            current.next().slideDown(500);
//              current.next().find(".we").css('display','block');
            spanimg.css("background", "url(../res/imgs/left_s1.png) no-repeat 0 10px");

        } else {
            $(this).next().slideUp(500);
            spanimg.css("background", "url(../res/imgs/left_s2.png) no-repeat 0 10px");
        }
    });

    /**************************列表************************************/

    //右侧合并展开
    //右侧的第一个三级标签下拉效果
    $(document).on('click', '.drop_downRight .year0', function () {
        var shu_name = $(this).find(".shu_name");
        var tagtype = $(".label_cont_top").find(".label_cont_top_l").siblings('span').text();
        //展开4级
        if ($(this).parent().find(".gender_in").css("display") == "none") {
            $(this).parent().find(".gender_in").slideDown(500);
        } else if ($(this).parent().find(".gender_in").css("display") == "block") {
            $(this).parent().find(".gender_in").slideUp(500);
            $(this).parent().find(".gender_infive").slideUp(500);
        }
    });
    //右侧合并展开
    //右侧的第四个标签出现第5个

    $(document).on('click', ".drop_downRight .gender4_on2_1", function () {
        if ($(this).parents(".gender_in").next(".gender_infive").css("display") == "none") {
            $(this).parents(".gender_in").next(".gender_infive").show();
        } else if ($(this).parents(".gender_in").next(".gender_infive").css("display") == "block") {
            $(this).parents(".gender_in").next(".gender_infive").hide();
        }
    });


    /**************************点击1级出现2级标签************************************/
    $(document).on('click', ".firstGr", function () {
        //背景
        var NOB = $(this).siblings(".zi");
        if (NOB.css("display") === "none") {
            $(this).siblings(".zi").slideUp(500);
            $(this).css("color", "#fff");
            $(this).find("img").attr("src", basePath + "/res/imgs/left_s11.png");
            $(this).siblings(".zi").find(".secondGr").eq(0).find(".second_img").css("background", "url(../res/imgs/left_s2.png) no-repeat 0 10px");
            NOB.slideDown(500);
        } else {
            //关闭2级标签
            NOB.slideUp(500);
            $(this).find("img").attr("src", basePath + "/res/imgs/left_s22.png");
            $(this).css("color", "#a3a3a3");
        }
    });
    var clientHeight = window.screen.height - 156;
    var objectObj = $(".con_gr");
    objectObj.css("height", clientHeight + 'px');
});


//-----------------------------me-------------------------------------

//获取最后一级面包屑的id和level
function getBreadIdAndLevel() {
    var value = $("#breadDiv>span:last").attr("value");
    var id = value.split('`')[0];
    var level = value.split('`')[1];
    var result = {
        "id": id,
        "level": level
    };
    return result;
}

//点击搜索
function search() {
    var bread = getBreadIdAndLevel();
    var name = $('#query').val();
    var result = queryByIdAndLevel(bread.id, bread.level, name);
    //刷新
    refreshContentList(result);
}

//刷新列表页
function refreshList() {
    var bread = getBreadIdAndLevel();
    var result = queryByIdAndLevel(bread.id, bread.level, '');
    //刷新
    refreshContentList(result);
}

//获取数据
function queryByIdAndLevel(id, level, name) {
    var result;
    if (null == id || '' == id || null == level || '' == level) {
        return result;
    }
    var req = new Object();
    req.id = id;
    req.level = level;
    req.name = name;
    $.ajax({
        url: 'queryTag',
        type: 'POST',
        async: false,
        data: req,
        timeout: 5000,    //超时时间
        dataType: 'json',
        success: function (data) {
            if (data.code == 10005 && data.data != null) {
                result = data.data;
            }
            if (data.code == 10006) {
                layer.msg('无数据');
            }
        },
        error: function (xhr) {
            layer.msg('错误');
        }
    });
    return result;
}

function getArr() {
    var examineArr = new Array();
    examineArr.push();

    examineArr.push(863933299619031);
    examineArr.push(865443815972178);
    examineArr.push(861245355002864);
    examineArr.push(864651695587027);
    examineArr.push(866543453431221);
    examineArr.push(869989794200546);
    examineArr.push(869984127067760);
    examineArr.push(868719319866073);
    examineArr.push(865065682256124);
    examineArr.push(866994217510469);
    examineArr.push(862882036450596);
    examineArr.push(862948887897844);
    examineArr.push(863541704559510);
    examineArr.push(862276439654692);
    examineArr.push(866820839972169);
    examineArr.push(864482348446063);
    examineArr.push(861884170973300);
    examineArr.push(865915229672783);
    examineArr.push(868175999099794);
    examineArr.push(868236282885634);
    examineArr.push(867546983538484);
    examineArr.push(860947957735990);
    examineArr.push(867021918789687);
    examineArr.push(869814655715121);
    examineArr.push(862552384673614);
    examineArr.push(867199657014896);
    examineArr.push(869217259231494);
    examineArr.push(861252165005495);
    examineArr.push(866681939347234);
    examineArr.push(866365133940321);
    examineArr.push(862435544527908);
    examineArr.push(869852001929370);
    examineArr.push(861773434983544);
    examineArr.push(862202421755974);
    examineArr.push(864484308058757);
    examineArr.push(861435655735543);
    examineArr.push(866940984044128);
    examineArr.push(865705980482878);
    examineArr.push(868740599341980);
    examineArr.push(862392717570771);
    examineArr.push(864066971269002);
    examineArr.push(867634149598033);
    examineArr.push(865041525851582);
    examineArr.push(867892574892843);
    examineArr.push(867081434498366);
    examineArr.push(864569941267895);
    examineArr.push(867102464292476);
    examineArr.push(869951490198898);
    examineArr.push(865569702211115);
    examineArr.push(863915019664289);
    examineArr.push(358252161562090);
    examineArr.push(350303509940228);
    examineArr.push(350500370688243);
    examineArr.push(357814218875309);
    examineArr.push(357732286531980);
    examineArr.push(357343688217870);
    examineArr.push(358748635005460);
    examineArr.push(354224163954618);
    examineArr.push(357903693930251);
    examineArr.push(356303330497610);
    examineArr.push(357118441650315);
    examineArr.push(359455017848302);
    examineArr.push(353205837008721);
    examineArr.push(359856897835711);
    examineArr.push(350931190486374);
    examineArr.push(351921809482102);
    examineArr.push(354969617365563);
    examineArr.push(353551132522276);
    examineArr.push(355011431031489);
    examineArr.push(352038085779758);
    examineArr.push(356470963112386);
    examineArr.push(352163838191439);
    examineArr.push(355943058944450);
    examineArr.push(358505137411517);
    examineArr.push(355211983594905);
    examineArr.push(350369886488219);
    examineArr.push(355367117280083);
    examineArr.push(354313764463472);
    examineArr.push(350240389937001);
    examineArr.push(359807577746338);
    examineArr.push(351874120755625);
    examineArr.push(351628607811458);
    examineArr.push(357571532288882);
    examineArr.push(359245620587685);
    examineArr.push(354459286716111);
    examineArr.push(357980192794904);
    examineArr.push(358377204161937);
    examineArr.push(357492977606411);
    examineArr.push(357789347011371);
    examineArr.push(355298790391373);
    examineArr.push(356898990034661);
    examineArr.push(351351664059210);
    examineArr.push(351565992092680);
    examineArr.push(350944974359111);
    examineArr.push(356140118886766);
    examineArr.push(355152891258516);
    examineArr.push(350975148358083);
    examineArr.push(351746049181215);
    examineArr.push(352794748255536);
    examineArr.push(350787213504457);
    return examineArr;
}

//刷新页面
function refreshContentList(result) {
    if (null == result) {
        return;
    }
    //渲染页面
    var html = '';
    document.getElementById('contentTagList').innerHTML = '';
    var arr = getArr();
    for (var i = 0; i < result.length; i++) {
        var tag = result[i];
        var rule = (tag.rule == null || tag.rule == '') ? "暂无" : tag.rule;
        if (null == tag.business || tag.business == '' || tag.business == undefined || tag.business == "undefine") {
            var business = "暂无";
        } else {
            var business = tag.business;
        }
        if (null == tag.scheTime || tag.scheTime == '' || tag.scheTime == undefined || tag.scheTime == "undefine") {
            var scheTime = "暂无";
        } else {
            var scheTime = tag.scheTime;
        }
        html += '<div style="display:inline-block" class="year clearfix">';
        html += '<div class="year0"><span class="shu_name" value=' + tag.id + ' title=' + tag.name + '>' + tag.name + '</span></div>';
        html += '<div class="gender2"></div>';
        html += '<div class="gender3 clearfix">';
        html += '<div class="gender3_1">';
        html += '<div style="display:none;" class="gender3_1top shu_name"></div>';
        //html+='<div class="gender3_1buttom"><span class="shangxian">已上线</span>';
        html += '<div class="gender3_1buttom">';
        if (tag.state == 1) {// 状态：1申请中、2审核通过、3运行中、4未运行、5已禁用
            html += '<div class="shangxian"><span style="background: #7acfd9;border: 1px solid #7acfd9;">申请中</span></div>';//状态
        } else if (tag.state == 3) {
            html += '<div class="shangxian"><span style="background:#73c2e6;border: 1px solid #73c2e6;">运行中</span></div>';//状态
        } else if (tag.state == 2) {
            html += '<div class="shangxian"><span style="background:#79cfd9;border: 1px solid #79cfd9;">审核通过</span></div>';//状态
        } else if (tag.state == 4) {
            html += '<div class="shangxian"><span style="background:#79cfd9;border: 1px solid #79cfd9;">未运行</span></div>';//状态
        } else if (tag.state == 5) {
            html += '<div class="shangxian"><span style="background:#d64646;border: 1px solid #d64646;">已禁用</span></div>';//状态
        }
        html += '<span><img src="../res/imgs/refesh.png" style="margin-top:-5px;"></span>';
        html += '<span title="' + scheTime + '" class="updatatime">' + scheTime + '</span>';//更新策略 每月更新
        html += '</div>';
        html += '</div>';
        html += '<div class="gender3_2"><span style="display: inline-block;"><img src="../res/imgs/worn.png" style="margin-top: -35px"></span><span class="labelDes" title=' + business + '>' + business + '</span></div>';
        html += '<div class="gender3_3">';
        //html+='<span><div id="chart_'+tag.id+'" style="width: 40px;height: 40px;display: inline-block;"></div><span style="display: inline-block;width: 128px;">&nbsp;&nbsp;统计：'+tag.userPercent+'%</span></span>';
        html += '</div>';
        html += '<div class="gender3_4"></div>';
        html += '</div>';
        var subTags = tag.subSearchTags;
        for (var j = 0; j < subTags.length; j++) {
            var subTag = subTags[j];
            var subRule = (subTag.rule == null || subTag.rule == '') ? "暂无" : subTag.rule;
            if (null == subTag.business || subTag.business == '' || subTag.business == undefined || subTag.business == "undefine") {
                var business = "暂无";
            } else {
                var business = subTag.business;
            }
            var top5Users = subTag.top5Users;
            html += '<div class="gender_in" style="display:block">';
            html += '<div class="gender4">';
            html += '<div class="gender2_right">';
            html += '<div class="gender4_on2">';
            html += '<div class="gender4_on2_1">';
            html += '<span class="shu_name_img" style="visibility:hidden;"></span> <span style="color: #3385ff; padding: 0px 20px;" class="shu_name" value="180" title=' + subTag.name + '>' + subTag.name + '</span>';
            html += '</div>';
            html += '<div class="gender4_on2_2" title="' + business + '">';
            html += '<span><img src="../res/imgs/worn.png" style="margin-top: -35px"></span><span style="margin-left: 5px;" class="labelDes">' + business + '</span>';
            html += '</div>';
            html += '<div class="gender4_on2_3">';
            html += '<div class="gender4_on2_3T">';
//           html+='<div class="progress">';
//           html+='<span class="blue" style="width: 83px; height: 7px;"></span>';
//           html+='</div>';
            html += '<span style="margin-left: 15px !important;">' + formatNumber(subTag.userCount, 0, 1) + '</span><span>用户拥有该标签</span>';
            html += '</div>';
            html += '<div class="gender4_on2_3B"></div>';
            html += '</div>';
            html += '<div class="gender4_on2_4">';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            html += '<div style="display:none" class="gender_in_fifth"></div>';
            html += '</div>';
            html += '<div class="gender_infive" style="display: none;">';
            html += '<div class="titleTop" style="line-height: 30px;color: #3385ff;">用户数据TOP5</div>';
            html += '<div>';
            html += '<table class="table table-striped table-bordered table-hover table-text-center">';
            html += '<thead>';
            html += '<tr>';
            html += '<th>用户姓名</th>';
            html += '<th>身份证号码</th>';
//           html+='<th>手机号码</th>';
//           html+='<th>银行卡号</th>';
            html += '<th>设备类型</th>';
            html += '<th>设备号</th>';
            html += '</tr>';
            html += '</thead>';
            html += '<tbody>';
            for (var k = 0; k < top5Users.length; k++) {
                var arrIndex = Math.floor(Math.random() * 100);
                var sbNum = arr[arrIndex] + '';
                var sbType = sbNum.substring(0, 2) == "86" ? "Andriod" : "iOS";
                var user = top5Users[k];
                var uesrName = user.name == null ? '' : user.name;
                var idNum = user.idNum == null ? '' : user.idNum;
                var phone = user.phone == null ? '' : user.phone;
                var bankNum = user.bankNum == null ? '' : user.bankNum;
                html += '<tr>';
                html += '<td>' + uesrName + '</td>';
                html += '<td>' + idNum + '</td>';
//             html+='<td>'+phone+'</td>';
//             html+='<td>'+bankNum+'</td>';
                html += '<td>' + sbType + '</td>';
                html += '<td>' + sbNum + '</td>';
                html += '</tr>';
            }
            html += '</tbody>';
            html += '</table>';
            html += '</div>';
            html += '</div>';
        }

        html += '</div>';
    }
    //渲染
    document.getElementById('contentTagList').insertAdjacentHTML('afterBegin', html);
    for (var i = 0; i < $(".gender_in").length; i++) {
        if ($(".gender_in").eq(i).next().hasClass("gender_infive")) {
            $(".gender_in").eq(i).next().show();
            return;
        }
    }
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

//-----------------------------------------me end ----------------------------------




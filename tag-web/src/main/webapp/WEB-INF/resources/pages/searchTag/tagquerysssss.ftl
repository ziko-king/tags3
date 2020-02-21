<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <!--S logo-->
    <link rel="icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <!--E logo-->
    <title>用户画像-标签系统-标签查询</title>
    <meta name="keywords" content="大数据，用户管理群，用户标签管理系统">
    <meta name="description" content="">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <!--优先使用 IE 最新版本和 Chrome -->
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <!--忽略数字自动识别为电话号码 -->
    <meta name="format-detection" content="telephone=no"/>
    <!--忽略识别邮箱 -->
    <meta name="format-detection" content="email=no"/>
    <!-- 针对手持设备优化，主要是针对一些老的不识别viewport的浏览器，比如黑莓 -->
    <meta name="HandheldFriendly" content="true"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/res/lib/bootstrap3/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/res/commons/css/base.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/res/css/searchTag/tagquery.css"/>
    <link rel="stylesheet" href="${basePath}/res/lib/jquery-confirm.min.css">
    <link rel="stylesheet" type="text/css" href="${basePath}/res/css/searchTag/tagqueryList.css"/>
    <script src="${basePath}/res/lib/jquery1/jquery.min.js"></script>
</head>
<body>

<div class="container">
    <#include "./commons/header.ftl"/>
    <div class="nav_wrap">
        <!--S 导航-->
        <div class="label_nav">
            <img src="${basePath}/res/imgs/home.png" style="margin-left:12px;margin-top: -5px;"/>
            <span class="label_nav_sp2">您当前的位置：</span>
            <a href="${basePath}/home/index" class="label_nav_sp1">首页</a>
            <span class="label_nav_sp2">></span>
            <a href="javascript:void(0);" class="label_nav_sp3 active">标签查询</a>
        </div>
        <!--E 导航-->
    </div>
    <!--E 头部的导航-->
    <!--S 下面的内容 -->
    <div id="page1" class="centent_wrap">
        <div class="centent_wrap_middle">
            <img src="${basePath}/res/imgs/groupsearch.png"/>
            <div class="search_span">标签数据查询</div>
        </div>
        <div class="centent_box">
            <div>
                <h1>一级标签</h1>
                <div class="ulbox ulboxone ulbox1">

                    <div>
                        <ul id="tag1">
                            <!-- <li><input type="checkbox" data-id="id"><span>金融行业</span></li>
                            <li><input type="checkbox" data-id="id"><span>电商行业</span></li>
                            <li><input type="checkbox" data-id="id"><span>快消品行业</span></li>
                            <li><input type="checkbox" data-id="id"><span>电商行业</span></li> -->
                        </ul>
                    </div>
                </div>
                <div class="more"><span>更多<img src="${basePath}/res/imgs/down.png"></span></div>
            </div>
            <p class="clear"></p>
            <div>
                <h1>二级标签</h1>
                <div class="ulbox ulboxtwo ulbox2">
                    <div>
                        <ul id="tag2">
                            <!-- <li><input type="checkbox" data-id="id"><span>中信银行</span></li>
                            <li><input type="checkbox" data-id="id"><span>招商银行</span></li>
                            <li><input type="checkbox" data-id="id"><span>华夏银行</span></li>
                            <li><input type="checkbox" data-id="id"><span>建设银行</span></li> -->
                        </ul>
                    </div>

                </div>
                <div class="more"><span>更多<img src="${basePath}/res/imgs/down.png"></span></div>
            </div>
            <p class="clear"></p>
            <div>
                <h1>三级标签</h1>
                <div class="ulbox ulboxthree ulbox3">
                    <div>
                        <ul id="tag3">
                            <!-- <li><input type="checkbox" data-id="id"><span>社保数据</span></li>
                            <li><input type="checkbox" data-id="id"><span>公积金数据</span></li>
                            <li><input type="checkbox" data-id="id"><span>信用等级</span></li> -->
                        </ul>
                    </div>

                </div>
                <div class="more"><span>更多<img src="${basePath}/res/imgs/down.png"></span></div>
            </div>
            <p class="clear"></p>
            <div>
                <h1>四级标签</h1>
                <div class="ulbox ulboxfour ulbox4">
                    <div>
                        <ul id="tag4">
                            <!-- <li><input type="checkbox" data-id="id"><span>城市</span></li>
                            <li><input type="checkbox" data-id="id"><span>年龄</span></li>
                            <li><input type="checkbox" data-id="id"><span>性别</span></li>
                            <li><input type="checkbox" data-id="id"><span>职业</span></li>
                            <li><input type="checkbox" data-id="id"><span>收入</span></li>
                            <li><input type="checkbox" data-id="id"><span>断缴次数</span></li> -->
                        </ul>
                    </div>
                    <p class="clear"></p>
                </div>
                <div class="more"><span>更多<img src="${basePath}/res/imgs/down.png"></span></div>
            </div>
            <p class="clear"></p>
            <div class="fivetags">
                <h1>五级标签</h1>
                <div class="ulbox ulboxfive ulbox5">
                    <div>
                        <ul id="tag5">
                            <!-- <li><input type="checkbox" data-id="id"><span>北京</span></li>
                            <li><input type="checkbox" data-id="id"><span>天津</span></li>
                            <li><input type="checkbox" data-id="id"><span>上海</span></li>
                            <li><input type="checkbox" data-id="id"><span>广州</span></li>
                            <li><input type="checkbox" data-id="id"><span>武汉</span></li>
                            <li><input type="checkbox" data-id="id"><span>杭州</span></li>
                            <li><input type="checkbox" data-id="id"><span>西安</span></li>
                            <li><input type="checkbox" data-id="id"><span>沈阳</span></li>
                            <li><input type="checkbox" data-id="id"><span>成都</span></li>
                            <li><input type="checkbox" data-id="id"><span>石家庄</span></li>
                            <li><input type="checkbox" data-id="id"><span>北京</span></li>
                            <li><input type="checkbox" data-id="id"><span>天津</span></li>
                            <li><input type="checkbox" data-id="id"><span>上海</span></li>
                            <li><input type="checkbox" data-id="id"><span>广州</span></li>
                            <li><input type="checkbox" data-id="id"><span>武汉</span></li>
                            <li><input type="checkbox" data-id="id"><span>杭州</span></li>
                            <li><input type="checkbox" data-id="id"><span>西安</span></li>
                            <li><input type="checkbox" data-id="id"><span>沈阳</span></li>
                            <li><input type="checkbox" data-id="id"><span>成都</span></li>
                            <li><input type="checkbox" data-id="id"><span>石家庄</span></li> -->
                        </ul>
                    </div>
                </div>
                <div class="more"><span>更多<img src="${basePath}/res/imgs/down.png"></span></div>
            </div>
            <p class="clear"></p>
            <div class="choice">
                <h1>已选条件</h1>
                <div class="choicebox">
                    <dl class="onecontent">
                        <dt>
                            <p>一级：</p>
                            <div class="selStr1"></div>
                        </dt>
                        <dd><img src="${basePath}/res/imgs/55.png" class="del"></dd>
                    </dl>
                    <dl class="twocontent">
                        <dt><p>二级：</p>
                            <div class="selStr2"></div>
                        </dt>
                        <dd><img src="${basePath}/res/imgs/55.png" class="del"></dd>
                    </dl>
                    <dl class="threecontent">
                        <dt><p>三级：</p>
                            <div class="selStr3"></div>
                        </dt>
                        <dd><img src="${basePath}/res/imgs/55.png" class="del"></dd>
                    </dl>
                    <dl class="fourcontent">
                        <dt><p>四级：</p>
                            <div class="selStr4"></div>
                        </dt>
                        <dd><img src="${basePath}/res/imgs/55.png" class="del"></dd>
                    </dl>
                    <dl class="fivecontent">
                        <dt><p>五级：</p>
                            <div class="selStr5"></div>
                        </dt>
                        <dd><img src="${basePath}/res/imgs/55.png" class="del"></dd>
                    </dl>
                </div>
            </div>
        </div>
        <div class="searchbtn"><input type="button" value="查询"></div>
    </div>
    <!--E 下面的内容-->

    <!--S 下面的内容 -->
    <div id="page2" style="display:none" class="centent_wrap clearfix">
        <div class="User_center">
            <div id="User_centerIN" class="User_centerIN">
                <div class="User_centerIN_top1">
                    <!-- <span class="centerIN_top1">图表</span> -->
                    <span class="centerIN_top1">分类过滤<img src="${basePath}/res/imgs/sort.png"></span>
                    <div class="choicebox">
                        <dl class="onecontent">
                            <dt><p>一级：</p>
                                <div class="selStr1"></div>
                            </dt>
                        </dl>
                        <dl class="twocontent">
                            <dt><p>二级：</p>
                                <div class="selStr2"></div>
                            </dt>
                        </dl>
                        <dl class="threecontent">
                            <dt><p>三级：</p>
                                <div class="selStr3"></div>
                            </dt>
                        </dl>
                        <dl class="fourcontent">
                            <dt><p>四级：</p>
                                <div class="selStr4"></div>
                            </dt>
                        </dl>
                        <dl class="fivecontent">
                            <dt><p>五级：</p>
                                <div class="selStr5"></div>
                            </dt>
                        </dl>
                    </div>
                    <span class="centerIN_top_export export"><img src="${basePath}/res/imgs/export.png"></span>
                </div>
                <p class="clear"></p>
                <div class="User_centerIN_top2">
                    <!-- <span class="centerIN_top1">图表</span> -->
                    <span class="centerIN_top2">用户数据</span>
                </div>
                <div class="User_centerIN_liebiao">
                    <!--table-->
                    <section class="table_wrap clearfix all-table-container">
                        <div class="cover_style">
                            <!--用户列表-->
                            <div>
                                <table class="table table-striped table-bordered table-hover table-text-center">
                                    <thead class="all-table-header">
                                    <tr>
                                        <th width="">用户姓名</th>
                                        <th width="">身份证号</th>
                                        <th width="">手机号码</th>
                                        <th width="">银行卡号</th>
                                        <!-- <th width="">状态</th>
                                        <th width="">创建时间</th>
                                        <th width="">操作</th> -->
                                    </tr>
                                    </thead>
                                    <tbody id="tagList">
                                    <tr>
                                        <td>金融行业</td>
                                        <td>高富帅</td>
                                        <td>121,000,000</td>
                                        <td>10</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </section>
                    <!--/.table-->
                    <!--pages-->
                    <!--number-->
                    <section class="pages">
                        <div class="col-sm-6">
                            <div class="dataTables_info page-num" role="status" aria-live="polite">
                                当前第<span id='curr'>1</span>/<span id='tota'>50</span>页，每页<span
                                        id='size'>10</span>条，共<span id='reco'>1</span>条记录
                            </div>
                        </div>
                        <!--上一頁，下一頁-->
                        <div class="col-sm-6">
                            <div class="dataTables_info page-num page_num_right" id="sample-table_info" role="status"
                                 aria-live="polite">
                                <span class="home_page" id='home_page'>首页</span>
                                <span class="up_page" id='up_page'>上一页</span>
                                <span class="next_page" id='next_page'>下一页</span>
                                <input class="search_page_num"
                                       onkeyup="this.value=this.value.replace(/[^\d]/g,'');"></input>
                                <span class="home_page" id='end_page'>尾页</span>
                                <span class="search_page" id='search_page'>跳页</span>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
<script src="${basePath}/res/lib/layer/layer.js"></script>
<script src="${basePath}/res/commons/js/base.js"></script>
<script src="${basePath}/res/lib/bootstrap3/js/bootstrap.js"></script>
<script src="${basePath}/res/lib/jquery-confirm.min.js"></script>
<script src="${basePath}/res/commons/js/ajax.js"></script>
<script src="${basePath}/res/js/searchTag/tagInit.js"></script>
<script>
    $(function () {
        if ($(".centent_box>div .ulboxone").height() > 60) {
            $(".centent_box>div .ulboxone").parent().css("height", "60px");
            $(".centent_box>div .ulboxone ul").css({"height": $(".centent_box>div .ulboxone").height() + 'px'});
            $(".centent_box>div .ulboxone>div").css({"height": '60px', "overflow": "hidden"});
            $(".centent_box>div .ulboxone").next(".more").show();
        } else if ($(".centent_box>div .ulboxone").height() == 60) {
            $(".centent_box>div .ulboxone").parent().css("height", "60px");
            $(".centent_box>div .ulboxone ul").css("height", "60px");
            $(".centent_box>div .ulboxone>div").css("height", "60px");
            $(".centent_box>div .ulboxone").next(".more").hide();
        } else if ($(".centent_box>div .ulboxone").height() < 60) {
            $(".centent_box>div .ulboxone").parent().css("height", "30px");
            $(".centent_box>div .ulboxone ul").css("height", "30px");
            $(".centent_box>div .ulboxone>div").css("height", "30px");
            $(".centent_box>div .ulboxone").next(".more").hide();
        }
        //展开收起
        $(".centent_box .more").off("click").on("click", function () {
            if ($(this).prev(".ulbox").find("ul").height() > 60 && $(this).prev(".ulbox").find("div").css("overflow") == "hidden") {
                $(this).html("收起<img src='${basePath}/res/imgs/up.png'>");
                $(this).prev(".ulbox").find("div").css({"height": "90px", "overflow": "auto"});
                $(this).parent("div").css({"height": "90px"});
            } else if ($(this).prev(".ulbox").find("ul").height() > 60 && $(this).prev(".ulbox").find("div").css("overflow") == "auto") {
                $(this).html("更多<img src='${basePath}/res/imgs/down.png'>");
                $(this).prev(".ulbox").find("div").css({"height": "60px", "overflow": "hidden"});
                $(this).parent("div").css({"height": "60px"});
            }
        })

        TagInit.init();
        $(".centerIN_top1 img").on("click", function () {
            $(".chioce_wrap").slideDown(500, function () {

            });
        })
        $(document).on("click", ".cancel", function () {
            $(".chioce_wrap").slideUp(500, function () {

            });
        });
        $(".export").on("click", function () {
            reportJson = {
                ids: TagInit.getSelTag2DB(),
                isExport: true
            }
            downloadFile(reportJson, basePath + "/search/export2Excel");
        });

        //导出
        function downloadFile(p_obj, p_url) {
            var form = $("<form>");//定义一个form表单
            form.attr("style", "display:none");
            form.attr("target", "_self");
            form.attr("method", "post");
            form.attr("action", p_url);
            $("body").append(form);//将表单放置在web中
            form.append("<input type='hidden' name='exportData' value='" + (new Date()).getMilliseconds() + "' />");
            for (var p in p_obj) {
                form.append("<input type='hidden' name='" + p + "' value='" + p_obj[p] + "' />");
            }
            form.submit();//表单提交
        }

    });

</script>
</html>
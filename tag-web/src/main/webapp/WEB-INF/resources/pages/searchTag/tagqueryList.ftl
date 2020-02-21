<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <!--S logo-->
    <link rel="icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <!--E logo-->
    <title>用户画像-标签系统-标签查询</title>
    <meta name="keywords" content="大数据，用户标签管理系统">
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
    <link href="${basePath}/res/lib/bootstrap3/css/bootstrap-table.css" rel="stylesheet" media="screen">
    <link rel="stylesheet" type="text/css" href="${basePath}/res/commons/css/base.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/res/css/labelsystem.css"/>
    <link rel="stylesheet" href="${basePath}/res/lib/jquery-confirm.min.css">
    <style>
        .drop_down::-webkit-scrollbar { /*滚动条整体样式*/
            width: 4px; /*高宽分别对应横竖滚动条的尺寸*/
            height: 4px;
        }

        .drop_down::-webkit-scrollbar-thumb { /*滚动条里面小方块*/
            border-radius: 5px;
            -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
            background: rgba(0, 0, 0, 0.2);
        }

        .drop_down::-webkit-scrollbar-track { /*滚动条里面轨道*/
            -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
            border-radius: 0;
            background: rgba(0, 0, 0, 0.1);
        }

        .we, .zi {
            background: rgb(66, 74, 81);
        }

        .fourth, .secondzi {
            display: none;
        }

        .we {
            display: block;
        }

        .firstGr {
            color: #a3a3a3;
        }
    </style>
    <script src="${basePath}/res/lib/jquery1/jquery.min.js"></script>
    <script src="${basePath}/res/lib/bootstrap3/js/bootstrap.js"></script>
    <script src="${basePath}/res/lib/layer/layer.js"></script>
    <script type="text/javascript" src="${basePath}/res/lib/echarts/echarts.min.js"></script>
    <script src="${basePath}/res/lib/bootstrap3/js/bootstrap-table.js" type="text/javascript"></script>
    <script src="${basePath}/res/lib/bootstrap3/js/bootstrap-table-zh-CN.js" charset="UTF-8"
            type="text/javascript"></script>
</head>
<body>
<div class="container">
    <#include "./commons/header.ftl"/>
    <div class="centent clearfix">
        <div class="cont_left_label">
            <!--S 左侧的一级标签-->
            <!--S 新增一级分类-->
            <div class="leftNav" style="background-color:#525c65;">
                <!--S 左侧的动态的添加的树状列表-->
                <div class="con_gr">
                    <div id="leftList" class="left_gr">
                        <!--<span class="set" data-toggle="modal" data-target="#setModal"><img src="${basePath}/res/imgs/set.png" alt="" style="width: 16px;height: 16px;margin-right: 5px;margin-left: 10px;margin-top: 0px;position: absolute;right: 12px;top:10px;display: block;"></span>-->
                        <#list basicTagList as levelOne>
                            <!-- 一级标签 -->
                            <div class="firstGr" style="transition: 0.5s;position: relative;color:#fff;">
                                <img src="${basePath}/res/imgs/left_s11.png">
                                <span value=${levelOne.id} level=${levelOne.level} title=${levelOne.name}>${levelOne.name}</span>
                            </div>
                            <#list levelOne.subTags as levelTwo>
                                <!-- 二级标签 -->
                                <div style="display:block;" class="zi" style="background-color: rgb(66, 74, 81);">
                                    <div class="secondGr">
                                        <span class="second_img"
                                              style="background:url(${basePath}/res/imgs/left_s2.png) no-repeat 0 10px !important;"
                                              value=${levelTwo.id} level=${levelTwo.level} title=${levelTwo.name}>${levelTwo.name}</span>
                                    </div>
                                    <div class="secondzi">
                                        <#list levelTwo.subTags as levelThree>
                                            <!-- 三级标签 -->
                                            <div class="we">
                                                <div class="we_div">
                                                    <span class="we_div_span"
                                                          value=${levelThree.id} level=${levelThree.level} title=${levelThree.name} style="background:url(${basePath}/res/imgs/left_s2.png)
                                                          no-repeat 0 10px !important;">${levelThree.name}</span>
                                                </div>
                                            </div>
                                            <div class="fourth">
                                                <#list levelThree.subTags as levelFourth>
                                                    <!-- 四级标签 -->
                                                    <div class="four">
                                                        <div class="four_div">
                                                            <span class="four_div_span"
                                                                  value=${levelFourth.id} level=${levelFourth.level} title=${levelFourth.name}>${levelFourth.name}</span>
                                                        </div>
                                                    </div>
                                                </#list>
                                            </div>
                                        </#list>
                                    </div>
                                    <div class="we"></div>
                                </div>
                            </#list>
                        </#list>
                        <span hidden="">${var1.tagType}</span>
                    </div>
                    <div style="display:block;" class="zi"></div>
                    <span hidden="">${var1.tagType}</span>
                </div>
            </div>
            <!--E 左侧的动态的添加的树状列表-->
        </div>
        <!--S cont_right_label-->
        <div class="cont_right_label" style="margin-left: 195px; width: 85%; ">
            <!--S 导航-->
            <div class="label_nav">
                <img src="${basePath}/res/imgs/home.png" style="margin-left:12px;margin-top: -5px;"/>
                <span class="label_nav_sp2">您当前的位置：</span>
                <a href="${basePath}/home/index" class="label_nav_sp1">首页</a>
                <span class="label_nav_sp2">></span>
                <a href="javascript:void(0);" class="label_nav_sp3 active">标签查询 </a>
            </div>
            <!--E 导航-->
            <div class="small_nav">
                <div class="label_radio clearfix">
                    <!--<div class="label_radio_j">
                        <input class="label_radio1" type="checkbox" checked="checked">
                        <span class="label_radio2">仅显有效</span>
                    </div>-->
                    <div class="User_radio_sosuo">
                        <input class="User_input" type="text" name="" id="query" value="" placeholder="请输入关键词检索"/>
                        <span id="searchDiv" class="Userl_radio6" onclick="search()"></span>
                    </div>
                </div>
            </div>

            <div class="label_cont">
                <div class="label_cont_top clearfix">
                    <!--S 导航的二级标签-->
                    <div id="breadDiv" class="nav_second" style="float: left;">
                        <span id="label_level1" class="label_cont_top_l label_cont_top_l1"
                              value="${basicTagList[0].id}`${basicTagList[0].level}"
                              title=${basicTagList[0].name}>${basicTagList[0].name}</span>
                        <span class="label_nav_sp2">></span>
                        <span id="label_level2" class="label_cont_top_l label_cont_top_l2"
                              value="${basicTagList[0].subTags[0].id}`${basicTagList[0].subTags[0].level}"
                              title=${basicTagList[0].subTags[0].name}>${basicTagList[0].subTags[0].name}</span>
                        <span class="label_nav_sp2">></span>
                        <span id="label_level3" class="label_cont_top_l label_cont_top_l3"
                              value="${basicTagList[0].subTags[0].subTags[0].id}`${basicTagList[0].subTags[0].subTags[0].level}"
                              title=${basicTagList[0].subTags[0].subTags[0].name}>${basicTagList[0].subTags[0].subTags[0].name}</span>
                    </div>

                </div>
                <div>
                    <!--S 新建二级分类-->
                    <div class="label_cont_buttomadd">
                        <!--S 标签-->
                        <div class="drop_down clearfix">
                            <div class="drop_downRight" id="contentTagList">

                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <script src="${basePath}/res/lib/jquery1/jquery.min.js"></script>
        <script src="${basePath}/res/lib/bootstrap3/js/bootstrap.js"></script>
        <script src="${basePath}/res/js/searchTag/tagquery.js"></script>
        <script src="${basePath}/res/js/json2.js"></script>
</body>
</html>
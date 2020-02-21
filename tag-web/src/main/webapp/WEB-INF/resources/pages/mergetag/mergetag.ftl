<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <!--S logo-->
    <link rel="icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <!--E logo-->
    <title>用户画像-标签系统-组合标签</title>
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
    <link rel="stylesheet" type="text/css" href="${basePath}/res/lib/pagination/pagination.css">
    <link rel="stylesheet" type="text/css" href="${basePath}/res/commons/css/base.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/res/css/mergetag/mergetag.css"/>
    <link rel="stylesheet" href="${basePath}/res/lib/jquery-confirm.min.css">
    <script src="${basePath}/res/lib/jquery1/jquery.min.js"></script>
</head>

<body>
<div class="container">
    <!--S 头部的导航-->
    <#include "./commons/header.ftl"/>
    <!--E 顶部导航-->
    <div class="nav_wrap">
        <!--S 导航-->
        <div class="label_nav">
            <!--<img src="${basePath}/res/imgs/home.png" style="margin-left:12px;margin-top: -5px;"/>-->
            <span class="label_nav_sp2">您当前的位置：</span>
            <a href="${basePath}/home/index" class="label_nav_sp1">首页</a>
            <span class="label_nav_sp2">></span>
            <a href="javascript:void(0);" class="label_nav_sp3 active">组合标签</a>
        </div>
        <!--E 导航-->
    </div>
    <!--E 头部的导航-->
    <!--S 下面的内容 -->
    <div class="centent_wrap clearfix">
        <div class="User_center">
            <div id="User_centerIN" class="User_centerIN">
                <div class="User_centerIN_top">
                    <a href="${basePath}/merge/createMergeTag" class="centerIN_top3" style="display:none;"><span
                                class="addIcon">+</span>创建组合</a>
                    <div class="User_radio_sosuo">
                        <input class="User_input" type="text" name="" id="query" value="" placeholder="请输入关键词检索"/>
                        <span class="Userl_radio6"></span>
                    </div>
                </div>
                <!--表格-->
                <div class="User_centerIN_liebiao">
                    <!--table-->
                    <section class="table_wrap clearfix all-table-container">
                        <div class="cover_style">
                            <!--用户列表-->
                            <div>
                                <table class="table">
                                    <thead class="all-table-header">
                                    <tr>
                                        <th width="">组合标签名称</th>
                                        <th width="">覆盖用户数</th>
                                        <th width="">包含标签组数</th>
                                        <th width="">状态</th>
                                        <th width="">创建时间</th>
                                        <th width="" style="text-align: center;">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    </tbody>
                                </table>
                                <div id="Pagination" class="pagination fr" style="float:right;"></div>
                            </div>
                        </div>
                    </section>
                    <!--/.table-->
                    <!--pages-->
                    <!--number-->
                    <!--<section class="pages">
                        <div class="col-sm-6">
                            <div class="dataTables_info page-num"  role="status" aria-live="polite">
                                当前第<span id='curr'>1</span>/<span id='tota'>50</span>页，每页<span id='size'>10</span>条，共<span id='reco'>1</span>条记录
                            </div>
                        </div>-->
                    <!--上一頁，下一頁-->
                    <!--<div class="col-sm-6">
                        <div class="dataTables_info page-num page_num_right" id="sample-table_info" role="status" aria-live="polite">
                            <span class="home_page" id='home_page'>首页</span>
                            <span class="up_page" id='up_page'>上一页</span>
                            <span class="next_page" id='next_page'>下一页</span>
                            <input class="search_page_num" onkeyup="this.value=this.value.replace(/[^\d]/g,'');"></input>
                            <span class="home_page" id='end_page'>尾页</span>
                            <span class="search_page" id='search_page'>跳页</span>
                        </div>
                    </div>
                </section>-->
                </div>
            </div>
        </div>
    </div>
</div>
<script src="${basePath}/res/lib/jquery1/jquery.min.js"></script>
<script src="${basePath}/res/commons/js/base.js"></script>
<script src="${basePath}/res/lib/bootstrap3/js/bootstrap.js"></script>
<script type="text/javascript" charset="utf-8" src='${basePath}/res/lib/pagination/pagination.js'></script>
<script src="${basePath}/res/lib/jquery-confirm.min.js"></script>
<script src="${basePath}/res/lib/echarts/echarts.min.js"></script>
<script src="${basePath}/res/commons/js/ajax.js"></script>
<script src="${basePath}/res/lib/layer/layer.js"></script>
<script src="${basePath}/res/js/mergetag/fun.js"></script>
<script src="${basePath}/res/js/mergetag/mergetag.js"></script>

</body>
</html>


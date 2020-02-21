<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <!--S logo-->
    <link rel="icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <!--E logo-->
    <title>用户画像-标签系统-微观画像概况</title>
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
    <link rel="stylesheet" type="text/css" href="${basePath}/res/css/microportraitsearch.css"/>
    <link rel="stylesheet" href="${basePath}/res/lib/jquery-confirm.min.css">
    <script src="${basePath}/res/lib/jquery1/jquery.min.js"></script>
</head>

<body>
<div class="container">
    <!--S 头部的导航-->
    <nav class="navbar navbar-default navbar-fixed-top g_row">
        <div class="container">
            <#include "./../commons/header.ftl"/>
        </div>
    </nav>
    <div class="nav_wrap">
        <!--S 导航-->
        <div class="label_nav">
            <span class="label_nav_sp2">您当前的位置：</span>
            <a href="${basePath}/home/index" class="label_nav_sp1">首页</a>
            <span class="label_nav_sp2">></span>
            <a href="javacript:void(0);" class="label_nav_sp3 active">微观画像</a>
        </div>
        <!--E 导航- ->
    </div>
    <!--E 头部的导航-->
        <!--S 下面的内容 -->
        <div class="centent_wrap">

            <div class="centent_wrap_middle">
                <span class="centent_gid">用户标示GID</span>
            </div>
            <div class="content_wrap_bottom">
                <div class="left_spnal">
                    <div class="iteminfo">
                        <span class="item_description">手机号：</span>
                        <span id="phone" class="item_detils_phone">${result.phoneNum}</span>
                    </div>
                    <div class="iteminfo">
                        <span class="item_description item_description_email">邮箱：</span>
                        <span id="email" class="item_detils_email">${result.email}</span>
                    </div>
                    <div class="iteminfo">
                        <span class="item_description item_description_qq">QQ：</span>
                        <span id="qq" class="item_detils_qq">${result.qq}</span>
                    </div>
                </div>
                <div class="right_spnal">
                    <section class="right_spal_wrap">
                        <div class="Microcosmic_centerIN_top">
                            <span class="Microcosmic_centerIN_top1">概况</span>
                        </div>
                        <div id="tag_show" style="width: 100%;height: 650px"></div>
                    </section>
                </div>
            </div>

        </div>
        <!--E 下面的内容-->
    </div>

    <script src="${basePath}/res/commons/js/base.js"></script>
    <script type="text/javascript" src="${basePath}/res/lib/echarts/echarts.js"></script>
    <script src="${basePath}/res/lib/bootstrap3/js/bootstrap.js"></script>
    <script src="${basePath}/res/commons/js/ajax.js"></script>
    <script src="${basePath}/res/lib/layer/layer.js"></script>
    <script src="${basePath}/res/lib/jquery-confirm.min.js"></script>
    <script src="${basePath}/res/js/microportrait/microportraitSearch.js"></script>
</body>

</html>

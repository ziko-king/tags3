<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <title>用户画像-标签系统-首页</title>
    <meta name="keywords" content="管理系统首页">
    <meta http-equiv="cache-control" content="no-cache">
    <link rel="stylesheet" type="text/css" href="${basePath}/res/lib/bootstrap3/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="${basePath}/res/lib/bootstrap3/css/bootstrap-theme.min.css">
    <link rel="stylesheet" type="text/css" href="${basePath}/res/commons/css/base.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/res/css/home.css"/>
    <script type="text/javascript" src="${basePath}/res/lib/jquery1/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/res/lib/bootstrap3/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${basePath}/res/commons/js/ajax.js"></script>
    <script type="text/javascript" src="${basePath}/res/lib/layer/layer.js"></script>
</head>
<body>
<div class="container">
    <#include "./commons/header.ftl"/>
    <div class="centent clearfix">
        <div class="cont_right">
            <!--S 轮播-->
            <div style="height:477px;" class="slider">
                <ul class="slider-main">
                    <li class="slider-panel">
                        <a href="javascript:void(0);"><img src="${basePath}/res/imgs/banner02.jpg"></a>
                    </li>
                    <li class="slider-panel">
                        <a href="javascript:void(0);"><img src="${basePath}/res/imgs/banner03.gif"></a>
                    </li>
                    <li class="slider-panel">
                        <a href="javascript:void(0);"><img src="${basePath}/res/imgs/banner01.jpg"></a>
                    </li>
                </ul>
                <div class="slider-extra">
                    <ul class="slider-nav">
                        <li class="slider-item"></li>
                        <li class="slider-item"></li>
                        <li class="slider-item"></li>
                    </ul>
                    <div class="slider-page">
                        <a class="slider-pre" href="javascript:;">
                            <</a>
                        <a class="slider-next" href="javascript:;">></a>
                    </div>
                </div>
            </div>
            <!--E 轮播-->
            <!--S chrat.js-->
            <div class="chartbox">
                <div class="chartbar chartOne">
                    <h5 class="chartOneNum">当前有效标签数<span></span></h5>
                    <div id="barOne"></div>
                </div>
                <div class="chartbar chartTwo">
                    <h5 class="chartTwoNum">标签系统覆盖用户数<span></span></h5>
                    <div id="barTwo"></div>
                </div>
            </div>
        </div>
    </div>
    <script src="${basePath}/res/lib/echarts/echarts.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${basePath}/res/js/home.js"></script>
    <script src="${basePath}/res/js/base.js"></script>
</body>
</html>

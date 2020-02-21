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
    <!-- Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" type="text/css" href="${basePath}/res/lib/bootstrap3/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="${basePath}/res/lib/bootstrap3/css/bootstrap-theme.min.css">
    <link rel="stylesheet" type="text/css" href="${basePath}/res/commons/css/base.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/res/css/home.css"/>
    <!-- 在bootstrap.min.js 之前引入 -->
    <script type="text/javascript" src="${basePath}/res/lib/jquery1/jquery.min.js"></script>
    <!-- Bootstrap 核心 JavaScript 文件 -->
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
                        <a href="javascript:void(0);"><img src="${basePath}/res/imgs/banner03.jpg"></a>
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
            <!--<div class="Chart clearfix">-->
            <!--S 第一个图-->
            <!--<div class="chart_one">
		                    <div class="one_t">
		                        <span><img src="${basePath}/res/imgs/nav_usergroupmanagement.png"/></span>
		                        <span class="one_t_text">1,32304</span>
		                        <p class="one_t_text1">当前有效标签数量</p>
		                    </div>
		                    <div class="one_p">
		                        <canvas id="home_echart_one"></canvas>
		                    </div>
		                </div>-->
            <!--E 第一个图-->
            <!--S 第二个图-->
            <!--<div class="chart_three">
		                    <div class="one_t_p">
		                        <span><img src="${basePath}/res/imgs/nav_usergroupmanagement.png"/></span>
		                        <span class="one_t_p_text">1200</span>
		                        <p class="one_t_p_text1">标签系统覆盖用户数</p>
		                    </div>
		                    <div class="one_p_three">
		                        <canvas id="home_echart_three"></canvas>
		                    </div>
		                </div>-->
            <!--E 第二个图-->
            <!--</div>-->
            <!--E chrat.js-->
        </div>
    </div>
    <!--<script src="${basePath}/res/lib/echarts/Chart-1.0.1-beta.4.js" type="text/javascript" charset="utf-8"></script>-->
    <script src="${basePath}/res/lib/echarts/echarts.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${basePath}/res/js/home.js"></script>
    <script src="${basePath}/res/js/base.js"></script>
</body>
</html>

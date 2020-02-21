<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <!--S logo-->
    <link rel="icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <!--E logo-->
    <title>用户画像-标签系统-标签任务</title>
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
    <link rel="stylesheet" type="text/css" href="${basePath}/res/lib/pagination/pagination.css">
    <link rel="stylesheet" type="text/css" href="${basePath}/res/commons/css/base.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/res/css/labelingtask.css"/>
    <link rel="stylesheet" href="${basePath}/res/lib/jquery-confirm.min.css">
    <link rel="stylesheet" href="${basePath}/res/lib/zTreeStyle/zTreeStyle.css" type="text/css">
    <link rel="stylesheet" type="text/css" href="${basePath}/res/css/searchTag/tagquery.css"/>
    <script src="${basePath}/res/lib/jquery1/jquery.min.js"></script>
</head>
<body>
<div class="container">
    <!--S 顶部导航-->
    <#include "./commons/header.ftl"/>
    <div class="centent clearfix">
        <div class="cont_left_label">
            <!--S 左侧的标签-->
            <div class="zTreeDemoBackground left">
                <div class="searchBtn"><span>查询</span></div>
                <ul id="treeDemo" class="ztree"></ul>
            </div>
            <!--E 左侧的标签-->
        </div>
        <!--S cont_right_label-->
        <div class="cont_right_label">
            <!--S 导航-->
            <div class="label_nav">
                <span class="label_nav_sp2">您当前的位置：</span>
                <a href="${basePath}/home/index" class="label_nav_sp1">首页</a>
                <span class="label_nav_sp2">></span>
                <a href="javacript:void(0);" class="label_nav_sp3 active">标签查询</a>
            </div>
            <!--E 导航-->
            <!--S 图表的内容-->
            <div class="labeltask_bottom_wrap">
                <section class="label_bottom_wrap">
                    <div class="User_center">
                        <div id="User_centerIN" class="User_centerIN">
                            <!-- <div class="User_centerIN_top">
                                <div class="labelTab" style="display:none;">
                                	<span class="status centerIN_top1 current">全部</span>
                                    <span class="status centerIN_top2">失败</span>
                                    <span class="status centerIN_top3">成功</span>
                                    <span class="status centerIN_top4">任务管理</span>
                                </div>                                
                                <div class="User_radio_sosuo">
                                    <input class="User_input" type="text" name="" id="query" value="" placeholder="请输入关键词检索"/>
                                    <span class="Userl_radio6"></span>
                                </div>
                            </div> -->
                            <div class="User_centerIN_all current_table s_table">
                                <!--table-->
                                <section class="table_wrap clearfix all-table-container">
                                    <div class="cover_style">
                                        <!--用户列表全部-->
                                        <div>
                                            <table class="table">
                                                <thead class="all-table-header">
                                                <tr>
                                                    <th width="">序号</th>
                                                    <th width="">用户姓名</th>
                                                    <th width="">身份证号码</th>
                                                    <th width="">手机号码</th>
                                                    <th width="">银行卡号</th>
                                                    <th width="">参保基数</th>
                                                </tr>
                                                </thead>
                                                <tbody></tbody>
                                            </table>
                                            <div id="Pagination" class="pagination fr"></div>
                                        </div>
                                    </div>
                                </section>
                            </div>
                        </div>
                    </div>
                </section>
            </div>
            <!--E 图表的内容-->
        </div>
        <!--E cont_right_label-->
    </div>
    <!--E 中间的内容-->
    <script type="text/javascript" charset="utf-8" src="${basePath}/res/lib/bootstrap3/js/bootstrap.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/res/commons/js/base.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/res/commons/js/ajax.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/res/lib/layer/layer.js"></script>
    <script type="text/javascript" charset="utf-8" src='${basePath}/res/lib/pagination/pagination.js'></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/res/lib/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/res/lib/jquery.ztree.excheck-3.5.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/res/js/searchTag/tagquery.js"></script>
</body>
</html>
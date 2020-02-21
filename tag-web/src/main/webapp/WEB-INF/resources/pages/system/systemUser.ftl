<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <!--S logo-->
    <link rel="icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <!--E logo-->
    <title>用户画像-标签系统-系统设置</title>
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
    <link rel="stylesheet" type="text/css" href="${basePath}/res/commons/css/base.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/res/css/system/ordinaryuser.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/res/lib/jquery-confirm.min.css">
    <script src="${basePath}/res/lib/jquery1/jquery.min.js"></script>
    <script src="${basePath}/res/lib/layer/layer.js"></script>
    <script src="${basePath}/res/lib/bootstrap3/js/bootstrap.js"></script>
    <script type="text/javascript" src="${basePath}/res/lib/jquery.validate.js"></script>
    <script src="${basePath}/res/lib/localization/messages_zh.js" charset="utf-8"></script>
    <script type="text/javascript" src="${basePath}/res/commons/js/ajax.js"></script>
</head>
<body>
<div class="container">
    <!--S 顶部导航-->
    <#include "./commons/header.ftl"/>
    <!--E 顶部导航-->
    <div class="nav_wrap">
        <!--S 导航-->
        <div class="label_nav">
            <span class="label_nav_sp2">您当前的位置：</span>
            <a href="${basePath}/home/index" class="label_nav_sp1">首页</a>
            <span class="label_nav_sp2">></span>
            <a href="javascript:void(0)" class="label_nav_sp3">系统设置</a>
            <span class="label_nav_sp2">></span>
            <a href="javascript:void(0)" class="label_nav_sp3 active">用户信息</a>
        </div>
        <!--E 导航-->
    </div>
    <!--E 头部的导航-->
    <div class="centent_wrap clearfix">
        <!--S cont_right_label-->
        <div class="cont_right_label_wrap" style="margin-left: 19px; width: 97%; ">

            <div class="label_cont_top_addlabel clearfix">
                <!--S 导航的二级标签-->
                <div class="label_cont_top_l_addlabel">用户信息<span hidden="">2&gt;</span></div>
            </div>
            <form action="" class="usermessage">
                <input id="userId" type="hidden" class="addlabel_id_input" name="userId" value="${user.id}">
                <div class="form_data">
                    <span class="addlabel_username"><span class="tip">*</span>用户名：</span>
                    <input id="username" type="text" name="username" class="addlabel_name_input"
                           value="${user.username}" placeholder="最多可输入20个字符" maxlength="20">
                </div>
                <div class="form_data">
                    <span class="addlabel_user"><span class="tip">*</span>姓名：</span>
                    <input id="user" type="text" name="name" class="addlabel_user_input" value="${user.name}"
                           placeholder="最多可输入10个字符" maxlength="10">
                </div>
                <div class="form_data">
                    <span class="addlabel_email"><span class="tip"></span>邮箱：</span>
                    <input id="email" type="text" name="email" class="addlabel_email_input" value="${user.email}">
                </div>
                <div class="form_data">
                    <span class="addlabel_phone"><span class="tip">*</span>手机号码：</span>
                    <input id="phonenumber" type="text" name="phone" class="addlabel_phone_input" value="${user.phone}"
                           placeholder="最多可输入11个字符" maxlength="11">
                </div>
                <div class="btnsbox">
                    <input type="button" class="btns resetPWD" value="重置密码">
                    <input type="button" class="btns save" value="保存">
                </div>
            </form>
        </div>
        <!--E cont_righabel-->
    </div>
</div>
<script src="${basePath}/res/commons/js/base.js"></script>
<script src="${basePath}/res/lib/jquery-confirm.min.js"></script>
<script src="${basePath}/res/lib/md5.min.js"></script>
<script src="${basePath}/res/js/system/ordinaryuser.js"></script>
</body>
</html>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <!--S logo-->
    <link rel="icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <!--E logo-->
    <title>用户画像-标签系统-登录</title>
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
    <link rel="stylesheet" href="${basePath}/res/lib/semantic.min.css"/>
    <link rel="stylesheet" href="${basePath}/res/css/login.css"/>
    <link rel="stylesheet" href="${basePath}/res/lib/jquery-confirm.min.css">
</head>
<body>
<!--顶部logo-->
<div><img src="${basePath}/res/imgs/logo.png" style="height:34px;margin-top:15px;margin-left:28px;"/></div>
<div class="login_title">
    <h1用户画像-能标签系统
    </h1>
    <p>
        依托海量权威数据的深度挖掘分析，建立以行业纵深为基准的智能标签体系。丰富的标签算法模型，支持基于智能统计分析、大数据挖掘等分析。通过可视化查询实现标签的物理图谱构建，快速响应各种业务需求。通过安全多视角数据源分析，建立标准化API接口服务，增强数据应用性扩展性。</p>
</div>
<div class="ui middle aligned center aligned grid">
    <!--悬浮框-->
    <div class="column popuwindow">
        <form id="loginForm" class="ui large form wrap_form" action="${basePath}/login" method="post">
            <div></div>
            <div class="ui stacked segment">
                <div class="field">
                    <div style="text-align:center;" class="ui left icon input">
                        <h2 class="login_label">账户登录</h2>
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <input id="userName" class="user" type="text" name="username" value="demo" placeholder="请输入用户名">
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon input">
                        <input id="password" class="user" type="password" name="password" value="demo"
                               placeholder="请输入密码">
                    </div>
                </div>
                <div class="field">
                    <div class="ui left icon">
                        <input class="login_forgetpwd" type="checkbox" name="rememberMe"></input>
                        <span class="login_forgetpwd_text">记住密码</span>
                    </div>
                </div>
                <div class="loginbtn fields">
                    <div class="field">
                        <a id="loginSubmit" class="ui fluid large teal submit button login_a ">登录</a>
                    </div>
                </div>
            </div>
            <div class="ui error message"></div>
        </form>
    </div>
</div>
<div class="public">
    <span></span>
</div>
<script type="text/javascript" src="${basePath}/res/lib/jquery1/jquery.min.js"></script>
<script type="text/javascript" src="${basePath}/res/lib/semantic.min.js"></script>
<script type="text/javascript" src="${basePath}/res/lib/layer/layer.js"></script>
<script type="text/javascript" src="${basePath}/res/js/md5.min.js"></script>
<script type="text/javascript" src="${basePath}/res/lib/jquery-confirm.min.js"></script>
<script type="text/javascript" src="${basePath}/res/js/index/index.js"></script>
</body>
</html>

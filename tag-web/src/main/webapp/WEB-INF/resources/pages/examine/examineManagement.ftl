<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <!--S logo-->
    <link rel="icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <!--E logo-->
    <title>用户画像-标签系统-审核管理</title>
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
    <link rel="stylesheet" href="${basePath}/res/lib/jquery-confirm.min.css">
    <link rel="stylesheet" type="text/css" href="${basePath}/res/css/releaseManagement.css"/>
    <script src="${basePath}/res/lib/jquery1/jquery.min.js"></script>
    <script src="${basePath}/res/lib/jquery-confirm.min.js"></script>
</head>
<body>
<div class="container">
    <#include "./commons/header.ftl"/>
    <div class="centent_wrap clearfix">
        <!--S cont_right_label-->
        <div class="nav_wrap">
            <!--S 导航-->
            <div class="label_nav"><span class="label_nav_sp2">您当前的位置：</span>
                <a href="${basePath}/home/index" class="label_nav_sp1">首页</a>
                <span class="label_nav_sp2">></span>
                <a href="javacript:void(0);" class="label_nav_sp3">审核管理</a>
                <span class="label_nav_sp2">></span>
                <a href="javacript:void(0);" class="label_nav_sp3 active">审批管理</a>
            </div>
            <!--E 导航-->
        </div>
        <!--S 图表的内容-->
        <div class="labeltask_bottom_wrap">
            <section class="label_bottom_wrap">
                <div class="User_center">
                    <div id="User_centerIN" class="User_centerIN">
                        <div class="User_centerIN_top">
                            <div class="User_radio_sosuo">
                                <input class="User_input" type="text" name="" id="query" value=""
                                       placeholder="请输入关键词检索"/>
                                <span class="Userl_radio6"></span>
                                <span class="shenpi">批量审批</span>
                            </div>
                        </div>
                        <div class="User_centerIN_tubiao current_table">
                            <!--table-->
                            <section class="table_wrap clearfix all-table-container">
                                <div class="cover_style">
                                    <!--用户列表失败-->
                                    <div>
                                        <table id="failtable" class="table">
                                            <thead class="all-table-header">
                                            <tr>
                                                <th width="" style="text-align: left;color:#2fa4ff;"><s></s>全选</th>
                                                <th width="">标签类型</th>
                                                <th width="">标签名称</th>
                                                <th width="">标签描述</th>
                                                <th width="">算法模型</th>
                                                <th width="">申请人</th>
                                                <th width="">审批状态</th>
                                                <th width="" style="text-align:center;">操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            </tbody>
                                        </table>
                                        <div id="Pagination" class="pagination" style="float:right;"></div>
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
    <section>
        <div class="modal fade" id="popermodal" tabindex="1" role="dialog"
             aria-labelledby="popermodalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                        <h4 class="modal-title" id="popermodalLabel">标签审核</h4>
                    </div>
                    <div class="modal-body modal-body_wrap">
                        <span class="typename_span">标签名称：</span>
                        <span></span>
                    </div>
                    <div class="modal-body modal-body_wrap">
                        <span class="typename_span">标签分类：</span>
                        <span></span>
                    </div>
                    <div class="modal-body modal-body_wrap">
                        <span class="typename_span">更新周期：</span>
                        <span></span>
                    </div>
                    <div class="modal-body modal-body_wrap">
                        <span class="typename_span">业务含义：</span>
                        <span></span>
                    </div>
                    <div class="modal-body modal-body_wrap">
                        <span class="typename_span">标签规则：</span>
                        <span></span>
                    </div>
                    <div class="modal-body modal-body_wrap">
                        <span class="typename_span">算法引擎：</span>
                        <span></span>
                    </div>
                    <div class="instruction_upload" style="color:red;">
                        *以文件形式上传标签算法包，用户可自定义算法规则，后台二次处理后启用
                    </div>
                    <div class="modal-footer" style="text-align:center;">
                        <button id="createchildrentype" type="button" class="btn btn-primary execute">通过</button>
                        <button type="button" class="btn btn-primary">不通过</button>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!--E 中间的内容-->
    <script type="text/javascript" charset="utf-8" src="${basePath}/res/lib/bootstrap3/js/bootstrap.js"></script>
    <script type="text/javascript" charset="utf-8" src='${basePath}/res/lib/pagination/pagination.js'></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/res/commons/js/base.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/res/commons/js/ajax.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/res/lib/layer/layer.js"></script>
    <script type="text/javascript" charset="utf-8" src="${basePath}/res/js/examine/examineManagement.js"></script>
</body>
</html>


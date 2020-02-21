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
    <link rel="stylesheet" type="text/css" href="${basePath}/res/lib/pagination/pagination.css">
    <link rel="stylesheet" type="text/css" href="${basePath}/res/commons/css/base.css"/>
    <link rel="stylesheet" href="${basePath}/res/lib/zTreeStyle/zTreeStyle.css" type="text/css">

    <link rel="stylesheet" type="text/css" href="${basePath}/res/css/system/adminmanage.css"/>
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

    <!--S 中间的内容-->
    <div class="centent clearfix">
        <div class="cont_left_label">
            <!--S 左侧的一级标签-->
            <!--S 新增一级分类-->
            <div class="leftNav">
                <!--S 左侧的动态的添加的树状列表-->
                <div class="con_gr">
                    <!--S 左侧的树图-->
                    <div class="left_gr usermanage">
                        <div class="firstGr">
                            <span hidden="">1&gt;</span>
                            <img class="userimg com_img" src="${basePath}/res/imgs/nav_labelsystem.png">
                            <span class="usermanage_text com_span">用户管理</span>
                        </div>
                    </div>
                    <@shiro.hasAnyRoles name="supper_admin,admin">
                        <div class="left_gr rolemanage">
                            <div class="firstGr">
                                <span hidden="">2&gt;</span>
                                <img class="roleimg com_img" src="${basePath}/res/imgs/nav_labelsystem.png">
                                <span class="rolemanage_text com_span">角色管理</span>
                            </div>
                        </div>
                    </@shiro.hasAnyRoles>
                    <!--E 左侧的树图-->
                </div>
                <!--E 左侧的动态的添加的树状列表-->
            </div>
            <!--E 左侧的一级标签-->
        </div>
        <!--S cont_right_label-->
        <div class="cont_right_label">
            <!--S 导航-->
            <div class="label_nav">
                <span class="label_nav_sp2">您当前的位置：</span>
                <a href="${basePath}/home/index" class="label_nav_sp1">首页</a>
                <span class="label_nav_sp2">></span>
                <a href="${basePath}/system/index" class="label_nav_sp3">系统设置</a>
                <span class="label_nav_sp2">></span>
                <a href="javascript:void(0);" class="label_nav_sp3">用户管理</a>

            </div>
            <!--E 导航-->
            <!--S 用户管理-->
            <section style="display:block;" class="user_right_wrap right_wrap">
                <!--S 用户列表-->
                <section class="admin_bottom_wrap">
                    <!--S 左側的列表-->
                    <div class="admin_left">
                        <div class="admin_left_top">
                            <div>
                                <span class="admin_left_top_txt">所属机构</span>
                            </div>
                            <@shiro.hasRole name="supper_admin">
                                <div class="admin_left_top_img">
                                    <!-- <span class="admin_left_top_del">删除</span> -->
                                    <span id="addOrg" class="admin_left_top_add" data-toggle="modal"
                                          data-target="#institutionModal"><span class="addIcon">+</span>添加</span>
                                </div>
                            </@shiro.hasRole>
                        </div>

                        <div class="admin_left_userlist" style="height: 555px;">
                            <#list orgs as var1>
                                <div class="list_item_div">
                                    <div class="companyName">
                                        <span class="company group"
                                              style="background:url(${basePath}/res/imgs/u62.png) no-repeat 0 10px !important;padding-left: 17px;"
                                              value=${var1.id} title=${var1.name}>${var1.name}</span>
                                        <!-- <span class="delcompany">X</span> -->
                                        <!-- <span class="addGroupNext" data-toggle="modal" data-target="#groupModel">+</span> -->
                                    </div>
                                    <#if var1.children>
                                        <div class="companyChild">
                                            <#list var1.children as var2>
                                                <div class="childItem">
                                                    <#if var2.children>
                                                        <span class="group" data-flag="0"
                                                              style="background:url(${basePath}/res/imgs/u62.png) no-repeat 0 10px !important;padding-left: 17px;"
                                                              value=${var2.id} title=${var2.name} style="padding-left: 30px;">${var2.name}</span>
                                                    <#else>
                                                        <span class="group" data-flag="1"
                                                              value=${var2.id} title=${var2.name}>${var2.name}</span>
                                                    </#if>
                                                    <!-- <span class="delchild">X</span> -->
                                                </div>
                                                <#if var2.children>
                                                    <div class="groupChild">
                                                        <#list var2.children as var3>
                                                            <div class="childItems">
                                                                <span class="group" data-sta="3"
                                                                      value=${var3.id} title=${var3.name} style="padding-left: 30px;">${var3.name}</span>
                                                                <!-- <span class="delchild">X</span> -->
                                                            </div>
                                                        </#list>
                                                    </div>
                                                </#if>
                                            </#list>
                                        </div>
                                    </#if>
                                </div>
                            </#list>
                        </div>

                    </div>
                    <!--E 又側的表格-->
                    <div class="admin_right">
                        <section class="admin_nav_wrap">
                            <span class="admin_nav_text">用户列表</span>
                            <span id="current_company" style='display:none'>0</span>
                            <@shiro.hasAnyRoles name="supper_admin,admin">
                                <section class="admin_middle_import" style="float:right;">
                                    <div class="import_add">
                                        <!-- <span class="import_btn" data-toggle="modal" data-target="#listimportantModal">批量导入</span> -->
                                        <span class="add_btn" data-toggle="modal" data-target="#userlistModal"><span
                                                    class="addIcon">+</span>添加用户</span>
                                    </div>
                                </section>
                            </@shiro.hasAnyRoles>
                            <div class="User_radio_sosuo" style="margin-top: 14px;margin-right:26px;">
                                <input class="User_input" type="text" name="" id="query" value=""
                                       placeholder="请输入关键词检索">
                                <span class="Userl_radio6"></span>
                            </div>
                        </section>

                        <section calss="admin_bottom_table">
                            <!--table-->
                            <section class="table_wrap clearfix all-table-container">
                                <div class="cover_style">
                                    <!--用户列表-->
                                    <div>
                                        <table id="table_wrap" class="table">
                                            <thead class="all-table-header">
                                            <tr>
                                                <th width="">序号</th>
                                                <!-- <th width="">公司名称</th>
                                                <th width="">职务</th> -->
                                                <th width="">姓名</th>
                                                <th width="">用户名</th>
                                                <th width="">手机号</th>
                                                <th width="">邮箱</th>
                                                <th width="">角色</th>
                                                <th width="" style="text-align:center;">操作</th>
                                            </tr>
                                            </thead>
                                            <tbody></tbody>
                                        </table>
                                        <div id="Pagination1" class="pagination fr"></div>
                                    </div>
                                </div>
                            </section>
                            <!--/.table-->

                        </section>
                    </div>
                </section>
                <!--E 用户列表-->
            </section>
            <!--E 用户管理-->
            <!--S 角色管理-->
            <section style="display:none;" class="role_right_wrap right_wrap">
                <section class="rolemanage_wrap">
                    <nav class="rolemanage_nav">
                        <span class="role_nav_text">角色列表</span>
                        <section class="rolemanage_search_add" style="float:right;">

                            <shiro:hasRole name="supper_admin">
                                <div class="rolemanage_right_add" data-toggle="modal" data-target="#oneModal">
                                    <span class="rolemanage_add_text"><span class="addIcon">+</span>添加角色</span>
                                </div>
                            </shiro:hasRole>
                        </section>
                        <div class="rolemanage_left_search">
                            <div class="rolemanage_search_div">
                                <input type="input" placeholder="请输入角色名称" class="rolemanage_input">
                            </div>
                            <span class="rolemanage_span"></span>
                        </div>

                    </nav>

                    <section class="rolemanage_table">
                        <!--table-->
                        <section class="table_wrap clearfix all-table-container">
                            <div class="cover_style">
                                <!--用户列表-->
                                <div>
                                    <table id="role_table_wrap" class="table">
                                        <thead class="all-table-header">
                                        <tr>
                                            <th width="">序号</th>
                                            <th width="">角色名称</th>
                                            <th width="">描述</th>
                                            <th width="">创建时间</th>
                                            <th width="" style="text-align:center;">操作</th>
                                        </tr>
                                        </thead>
                                        <tbody></tbody>
                                    </table>
                                    <div id="Pagination2" class="pagination fr"></div>
                                </div>
                            </div>
                        </section>
                        <!--/.table-->
                    </section>
                </section>
            </section>
            <!--E 角色管理-->

        </div>
        <!--E cont_right_label-->
    </div>
    <!--E 中间的内容-->
    <!--S 添加角色的弹窗-->
    <section>
        <div class="modal fade" id="oneModal" tabindex="-1" role="dialog"
             aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close"
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="myModalLabel">
                            添加角色
                        </h4>
                    </div>
                    <div class="modal-body addrolemodel_sp">
                        <div>
                            <span class="tip">*</span>
                            <span class="addrolemodel_span">角色名称：</span>
                            <input maxlength="10" type="text" placeholder="最多可输入10个字符"
                                   class="addrolemodel_input"></input>
                        </div>
                        <div>
                            <span class="tip"></span>
                            <span class="addrolemodel_span">角色描述：</span>
                            <textarea type="text" maxlength="50" class="form-input  roleDes"
                                      placeholder="最多可输入50个字符" name="roleDes"></textarea>
                        </div>
                    </div>
                    <div class="modal-footer" style="text-align:center;">

                        <button id="createRoleType" type="button" class="btn btn-primary" data-dismiss="modal">
                            保存
                        </button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            取消
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
    </section>
    <!--E 添加角色的弹窗-->
    <!--S 编辑角色的弹窗-->
    <section>
        <div class="modal fade" id="editModal" tabindex="-1" role="dialog"
             aria-labelledby="editModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="editModalLabel">
                            编辑角色
                        </h4>
                    </div>
                    <div class="modal-body">
                        <div>
                            <span class="tip">*</span>
                            <span class="addrolemodel_span">角色名称：</span>
                            <input id="rolename_input" maxlength="10" type="text" placeholder="最多可输入10个字符"
                                   class="editrolemodel_input"></input>
                        </div>
                        <div>
                            <span class="tip"></span>
                            <span class="addrolemodel_span">角色描述：</span>
                            <textarea type="text" maxlength="50" class="form-input roleDes"
                                      placeholder="最多可输入50个字符" name="roleDes"></textarea>
                        </div>
                    </div>
                    <div class="modal-footer" style="text-align:center;">

                        <button id="excutehavair" type="button" class="btn btn-primary" data-dismiss="modal">
                            保存
                        </button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            取消
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
    </section>
    <!--E 编辑角色的弹窗-->
    <!--S 资源权限的弹窗-->
    <section>
        <div class="modal fade" id="resourceModal" tabindex="-1" role="dialog"
             aria-labelledby="resourceModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close"
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="resourceModalLabel">
                            资源权限分配
                        </h4>
                    </div>
                    <div class="modal-body">
                        <div>
                            <span class="tip">*</span>
                            <span class="addrolemodel_span">角色名称：</span>
                            <input type="text" name="makeupCo" required="true" class="addrolemodel_input makeup"
                                   readonly/>

                        </div>
                        <div>
                            <span class="tip">*</span>
                            <span class="addrolemodel_span">功能权限：</span>
                            <div class="resourceLeft" id="treeboxbox_tree2">
                                <div class="qx_div">
                                    <div class="ztree" id="treeDemo2">

                                    </div>
                                </div>

                            </div>
                            <div class="resourceRight" id="treeboxbox_tree3">

                            </div>
                        </div>
                    </div>
                    <div class="modal-footer" style="text-align:center;">

                        <button id="resourceModalbtn" type="button" class="btn btn-primary">
                            保存
                        </button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            取消
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
    </section>
    <!--E 资源权限的弹窗-->
    <!--S 数据权限的弹窗-->
    <section>
        <div class="modal fade" id="dateModal" tabindex="-1" role="dialog"
             aria-labelledby="dateModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close"
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="dateModalLabel">
                            数据权限分配
                        </h4>
                    </div>
                    <div class="modal-body">
                        <div>
                            <span class="tip">*</span>
                            <span class="addrolemodel_span">角色名称：</span>
                            <input type="text" name="makeupCo" required="true" class="addrolemodel_input makeup"
                                   readonly/>

                        </div>
                        <div>
                            <span class="tip">*</span>
                            <span class="addrolemodel_span">数据权限：</span>
                            <div class="dateBox">
                                <p class="tab"><span class="spanbg">基础标签</span><span>组合标签</span></p>
                                <div class="ztree" id="treeDemobas">

                                </div>
                                <div class="ztree" id="treeDemomer" style="display:none;">

                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer" style="text-align:center;">

                        <button id="dateModalbtn" type="button" class="btn btn-primary">
                            保存
                        </button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            取消
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
    </section>
    <!--E 数据权限的弹窗-->

    <!--S 所属机构的添加弹窗-->
    <section>
        <div class="modal fade" id="institutionModal" tabindex="-1" role="dialog"
             aria-labelledby="institutionModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close"
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="institutionModalLabel">
                            添加组织
                        </h4>
                    </div>
                    <div class="modal-body">
                        <div>
                            <span class="tip">*</span>
                            <span class="addrolemodel_span">公司名称：</span>
                            <input type="text" name="makeupCo" maxlength="20" placeholder="最多可输入20个字符" id="company_name"
                                   class="addrolemodel_input makeup"/>
                        </div>
                    </div>
                    <div class="modal-footer" style="text-align:center;">

                        <button id="createOrganization" type="button" class="btn btn-primary">
                            保存
                        </button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            取消
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
    </section>
    <!--E 所属机构的添加弹窗-->
    <!--S 下级机构的添加弹窗-->
    <section>
        <div class="modal fade" id="groupModel" tabindex="-1" role="dialog"
             aria-labelledby="institutionModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close"
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="institutionModalLabel">
                            添加下级组织
                        </h4>
                    </div>
                    <div class="modal-body">
                        <div>
                            <span class="tip"></span>
                            <span class="addrolemodel_span">组织名称：</span>
                            <span class="groupNames"></span>
                        </div>
                        <div>
                            <span class="tip">*</span>
                            <span class="addrolemodel_span">下级组织：</span>
                            <input type="text" name="makeupCo" maxlength="20" placeholder="最多可输入20个字符"
                                   id="nextGroupName" class="addrolemodel_input makeup"/>
                        </div>
                    </div>
                    <div class="modal-footer" style="text-align:center;">
                        <button id="createNextgroup" type="button" class="btn btn-primary">
                            保存
                        </button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            取消
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
    </section>
    <!--E 下级机构的添加弹窗-->
    <!--S 机构的编辑弹窗-->
    <section>
        <div class="modal fade" id="editgroupModel" tabindex="-1" role="dialog"
             aria-labelledby="editgroupModel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title">编辑组织</h4>
                    </div>
                    <div class="modal-body">
                        <div>
                            <span class="tip"></span>
                            <span class="addrolemodel_span">组织名称：</span>
                            <input type="text" class="groupNames"/>
                        </div>
                    </div>
                    <div class="modal-footer" style="text-align:center;">
                        <button id="editgroupName" type="button" class="btn btn-primary">保存</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
    </section>
    <!--E 机构的编辑弹窗-->
    <!--S 用戶管理的添加的弹窗-->
    <section>
        <div class="modal fade" id="userlistModal" tabindex="-1" role="dialog"
             aria-labelledby="userlistModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close"
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="userlistModalLabel">
                            添加用户
                        </h4>
                    </div>
                    <div class="modal-body">
                        <form action="" id="addUsermanageForm" class="">
                            <div>
                                <span class="tip"></span>
                                <span class="addrolemodel_span">组织名称：</span>
                                <span class="groupName" data-id=""></span>
                            </div>
                            <div>
                                <span class="tip">*</span>
                                <span class="addrolemodel_span">用户名：</span>
                                <input id="add_username" maxlength="20" type="text" placeholder="最多可输入20个字符"
                                       class="addrolemodel_input addUserInput user_username" name="user_username">
                            </div>
                            <div>
                                <span class="tip">*</span>
                                <span class="addrolemodel_span">密码：</span>
                                <input id="add_password" minlength="6" maxlength="18" type="password"
                                       placeholder="可输入6-18个字符" class="addrolemodel_input addUserInput user_password"
                                       name="user_password">
                            </div>
                            <div>
                                <span class="tip">*</span>
                                <span class="addrolemodel_span">姓名：</span>
                                <input id="add_name" maxlength="10" type="text" placeholder="最多可输入10个字符"
                                       class="addrolemodel_input addUserInput user_name" name="user_name">
                            </div>
                            <div>
                                <span class="tip"></span>
                                <span class="addrolemodel_span">邮箱：</span>
                                <input id="add_email" type="text" class="addrolemodel_input addUserInput user_useremail"
                                       name="user_useremail">
                            </div>
                            <div>
                                <span class="tip">*</span>
                                <span class="addrolemodel_span">手机号码：</span>
                                <input id="add_userphone" maxlength="11" type="text"
                                       class="addrolemodel_input addUserInput user_userphone" name="user_userphone">
                            </div>
                            <div>
                                <span class="tip">*</span>
                                <span class="typename_datatype">角色分配：</span>
                                <select name="" id="addUserRole" class="addUserInput">
                                    <@shiro.hasRole name="supper_admin">
                                        <option value="1">超级管理员</option>
                                    </@shiro.hasRole>
                                    <@shiro.hasAnyRoles name="supper_admin,admin">
                                        <option value="2">管理员</option>
                                    </@shiro.hasAnyRoles>
                                    <option value="3">用户</option>
                                    <!-- <#list result.data as var1>
                                    <option value="${var1.companyId}">${var1.companyName}</option>
                                    </#list> -->
                                </select>
                            </div>
                        </form>

                    </div>
                    <div class="modal-footer" style="text-align:center;">

                        <button id="create_user" type="button" class="btn btn-primary">
                            保存
                        </button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            取消
                        </button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
        </div>
    </section>
    <!--E 用戶管理的添加的弹窗-->
    <!--S 批量导入-->
    <section>
        <div class="modal fade" id="listimportantModal" tabindex="-1" role="dialog"
             aria-labelledby="listimportantModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close"
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="listimportantModalLabel">
                            批量导入用户
                        </h4>
                    </div>

                    <div class="modal-body">
                        <div style="position: relative;">
                            <span class="tip">*</span>
                            <span class="addrolemodel_span">上传文件：</span>
                            <input type="text" readonly="" style="height: 30px;border:1px solid #ccc;"
                                   class="typename_input"/>
                            <span id="sub_autoupload" class="fileName">上传文件</span>
                            <input type="file" id="sub_hidefileupload" class="file"/>
                        </div>
                        <div>
                            <span class="tip">*</span>
                            <a href="../../import_model/user_import.xlsx" class="addrolemodel_span">下载模板</a>
                        </div>
                    </div>

                    <div class="modal-footer" style="text-align:center;">
                        <button id="upload_submit" type="button" class="btn btn-primary">保存
                        </button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消
                        </button>
                    </div>


                </div><!-- /.modal-content -->
            </div><!-- /.modal -->

    </section>
    <!--E 批量导入-->
    <!--S 用戶管理的编辑的弹窗-->
    <section>
        <div class="modal fade" id="usermanageeditModal" tabindex="-1" role="dialog"
             aria-labelledby="usermanageeditModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close"
                                data-dismiss="modal" aria-hidden="true">
                            &times;
                        </button>
                        <h4 class="modal-title" id="usermanageeditModalLabel">编辑用户</h4>
                    </div>
                    <div class="modal-body">
                        <form action="" class="usermanageForm">
                            <div>
                                <span class="tip">*</span>
                                <span class="addrolemodel_span">用户名：</span>
                                <input id="edit_userName" maxlength="20" type="text" id="edit_userName"
                                       placeholder="最多可输入20个字符" class="addrolemodel_input addUserInput user_username"
                                       name="user_username">
                            </div>
                            <div>
                                <span class="tip">*</span>
                                <span class="addrolemodel_span">姓名：</span>
                                <input maxlength="10" type="text" id="edit_adminName" placeholder="最多可输入10个字符"
                                       class="addrolemodel_input addUserInput user_name" name="user_name">
                            </div>
                            <div>
                                <span class="tip"></span>
                                <span class="addrolemodel_span">邮箱：</span>
                                <input type="text" id="edit_admin_email"
                                       class="addrolemodel_input addUserInput user_useremail" name="user_useremail">
                            </div>
                            <div>
                                <span class="tip">*</span>
                                <span class="addrolemodel_span">手机号码：</span>
                                <input id="edit_admin_phoneNumber" maxlength="11" type="text"
                                       class="addrolemodel_input addUserInput user_userphone" name="user_userphone">
                            </div>
                            <div>
                                <span class="tip">*</span>
                                <span class="typename_datatype">角色分配：</span>
                                <select name="" id="adminRole" class="addUserInput">
                                    <@shiro.hasRole name="supper_admin">
                                        <option value="1">超级管理员</option>
                                    </@shiro.hasRole>
                                    <@shiro.hasAnyRoles name="supper_admin,admin">
                                        <option value="2">管理员</option>
                                    </@shiro.hasAnyRoles>
                                    <option value="3">用户</option>
                                    <!-- <#list result.data as var1>
                                    <option value="${var1.companyId}">${var1.companyName}</option>
                                    </#list> -->
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer" style="text-align:center;">
                        <button id="edit_user_btn" type="button" class="btn btn-primary">保存</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    </div>
                </div><!-- /.modal-content -->
            </div><!-- /.modal -->
    </section>
    <!--E 用戶管理的编辑的弹窗-->
</div>
<script type="text/javascript" charset="utf-8" src='${basePath}/res/lib/pagination/pagination.js'></script>
<script src="${basePath}/res/commons/js/base.js"></script>
<script src="${basePath}/res/lib/jquery-confirm.min.js"></script>
<script type="text/javascript" src="${basePath}/res/lib/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${basePath}/res/lib/jquery.ztree.excheck-3.5.js"></script>
<script src="${basePath}/res/js/system/adminmanage.js"></script>
<script src="${basePath}/res/lib/md5.min.js"></script>
</body>
</html>
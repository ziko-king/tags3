<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <!--S logo-->
    <link rel="icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <!--E logo-->
    <title>用户画像-标签系统-组合标签</title>
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
    <link rel="stylesheet" type="text/css" href="${basePath}/res/css/mergetag/mergetag.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/res/css/mergetag/create.css"/>
    <link rel="stylesheet" href="${basePath}/res/lib/jquery-confirm.min.css">
    <script src="${basePath}/res/lib/jquery1/jquery.min.js"></script>
    <script src="${basePath}/res/lib/jquery-confirm.min.js"></script>
    <script src="${basePath}/res/lib/layer/layer.js"></script>
    <script src="${basePath}/res/commons/js/ajax.js"></script>
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
                    <div id="leftList" class="left_gr">
                        <!-- <span class="set" data-toggle="modal" data-target="#setModal"><img src="${basePath}/res/imgs/set.png" alt="" style="width: 16px;height: 16px;margin-right: 5px;margin-left: 10px;margin-top: 0px;position: absolute;right: 12px;top:10px;display: block;"></span> -->
                        <#list basicTagList as levelOne>
                            <!-- 一级标签 -->
                            <div class="firstGr" style="transition: 0.5s;position: relative;">
                                <img src="${basePath}/res/imgs/left_s22.png">
                                <span value='${levelOne.id}-${levelOne.level}'
                                      title='${levelOne.name}'>${levelOne.name}</span>
                            </div>

                            <!-- 二级标签 -->
                            <div class="zi">
                                <#list levelOne.subTags as levelTwo>
                                    <div class="secondGr">
                                        <span class="second_img"
                                              style="background:url(${basePath}/res/imgs/left_s2.png) no-repeat 0 10px !important;"
                                              value='${levelTwo.id}-${levelTwo.level}'
                                              title=${levelTwo.name}>${levelTwo.name}</span>
                                    </div>
                                    <div class="secondzi">
                                        <#list levelTwo.subTags as levelThree>
                                            <!-- 三级标签 -->
                                            <div class="we">
                                                <div class="we_div">
                                                    <span class="we_div_span"
                                                          value='${levelThree.id}-${levelThree.level}'
                                                          title='${levelThree.name}'
                                                          style="background:url(${basePath}/res/imgs/left_s2.png) no-repeat 0 10px !important;">${levelThree.name}</span>
                                                </div>
                                            </div>
                                            <div class="fourth">
                                                <#list levelThree.subTags as levelFourth>
                                                    <!-- 四级标签 -->
                                                    <div class="four">
                                                        <div class="four_div">
                                                            <span class="four_div_span"
                                                                  value='${levelFourth.id}-${levelFourth.level}'
                                                                  title='${levelFourth.name}'>${levelFourth.name}</span>
                                                        </div>
                                                    </div>
                                                </#list>
                                            </div>
                                        </#list>
                                    </div>
                                    <div style="display:block;" class="we"></div>
                                </#list>
                            </div>

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
        <div class="cont_right_label">
            <!--S 导航-->
            <div class="label_nav">
                <span class="label_nav_sp2">您当前的位置：</span>
                <a href="${basePath}/home/index" class="label_nav_sp1">首页</a>
                <span class="label_nav_sp2">></span>
                <a href="javascript:void(0);" class="label_nav_sp3">组合标签</a>
                <span class="label_nav_sp2">></span>
                <a href="javascript:void(0);" class="label_nav_sp3 active">创建组合标签</a>
            </div>
            <!--E 导航-->
            <!--S 1.2.3步-->
            <div class="createusergroupguide_wrap clearfix">
                <div class="guide_one guide">
                    <span class="guide_line_active"></span>
                    <div class="guide_one_img guide_lead"><span class="guide_one_span">1</span></div>
                    <span class="guide_line_active"></span>
                    <div class="guide_text">定义条件</div>
                </div>
                <div class="guide_two guide">
                    <span class="guide_two_line"></span>
                    <div class="guide_two_img guide_lead">
                        <span class="guide_one_span">2</span>
                    </div>
                    <span class="guide_two_line"></span>
                    <div class="guide_text">填写基本信息</div>
                </div>
                <div class="guide_three guide">
                    <span class="guide_three_line"></span>
                    <div class="guide_three_img guide_lead">
                        <span class="guide_one_span">3</span>
                    </div>
                    <span class="guide_three_line"></span>
                    <div class="guide_text">确认信息</div>
                </div>
            </div>
            <!--E 1.2.3步-->
            <!--S 第一步的内容-->
            <div style="display:block;" class="createusergroupcount_wrap">
                <!--S　用户选择面板-->

                <section class="useraddlabel_wrap">
                    <section class="useraddlabel_nav">
                        <span class="useraddlabel_nav_text first_text"><#if mergetag.mergeTagName == null >未选择<#else>${mergetag.mergeTagName}</#if></span>
                        <!-- <div  class="useraddlabel_search">
                             <input id="searchval" class="useraddlabel_input" type="text" />
                             <span id="searchbtn" class="useraddlabel_img" ></span>
                         </div>-->
                    </section>

                    <div class="usersureaddlabel_contentchoice addlabel">
                        <#list basictag as i>
                            <#list i.subTags as j>
                                <!--<input name="item" class="usersureaddlabel_checkbox" type="checkbox" checked>-->
                                <s class="check checked"></s>
                                <span class="usersureaddlabel_text" value=${j.pid}`${j.id}>${j.name}</span>
                            </#list>
                        </#list>
                    </div>

                    <div class="userlabel_addbtn">
                        <span class="userlabel_addbtn_text">添加</span>
                    </div>

                </section>
                <!--E　用户选择面板-->
                <!--S　用户确认选择-->
                <section class="usersureaddlabel_wrap">
                    <!--S 内容-->
                    <div class="usersureaddlabel_contentchoice">
                        <section class="useraddlabel_nav" style="margin-bottom:10px;">
                            <span class="useraddlabel_nav_text">已选条件</span>
                            <span class="clearAll">清空</span>
                        </section>
                        <!--S 每一项的内容-->
                        <div id="addtocontent" class="addtocontent">
                            <#list basictag as i>
                                <section class="sersureaddlabel_item ">
                                    <div class="sersureaddlabel_rec">
                                        <span class="sersureaddlabel_rec_num">${i_index+1}</span>
                                        <span class="sersureaddlabel_rec_img" style="margin-left:-5px;"></span>
                                        <p class="sersureaddlabel_rec_instruction_title" value=${i.id} title="${i.name}"
                                           style="cursor: pointer;">${i.name}</p>
                                        <div class="sersureaddlabel_content  hascontent">
                                            <#list i.subTags as j>
                                                <span class="sersureaddlabel_rec_instruction"
                                                      value=${j.id} title="${j.name}"
                                                      style="cursor: pointer;">${j.name}</span>
                                            </#list>
                                        </div>
                                        <span class="sersureaddlabel_rec_delimg"></span>
                                    </div>
                                    <select style="margin-left:5px;" class="sersureaddlabel_checkbox"
                                            onchange="formula();">
                                        <#if i.conditions == "and">
                                            <option value="and" checked="checked">且</option>
                                            <option value="or">或</option>
                                        <#else>
                                            <option value="or" checked="checked">或</option>
                                            <option value="and">且</option>
                                        </#if>
                                    </select>
                                </section>
                            </#list>
                        </div>
                        <!--E 每一项的内容-->

                        <!--S 条件公式预览-->
                        <span class="sersureaddlabel_oversee_span">条件公式预览</span>
                        <div class="sersureaddlabel_oversee_watch"
                             data-value="${mergetag.listBasicTagId}">${mergetag.condition}</div>
                        <!--E 条件公式预览-->
                    </div>
                    <!--E 内容-->
                </section>
                <!--E　用户确认选择-->
                <!--S　按钮-->
                <section style="display:block;" class="btngroup_wrap">
                    <div class="form_data_btn">
                        <div class="center_btn">
                            <button id="one_next" type="button" style=" background: rgb(0, 104, 181);"
                                    class="btn btn-default base_btn one_next_btn">
                                <span class="btn_span">下一步</span>
                            </button>
                            <button id="cancel" type="button" style="background: rgb(146, 146, 146);color:#fff"
                                    class="btn btn-default base_btn  one_cancel_btn">
                                <a class="one_btn_span">取消</a>
                            </button>
                        </div>
                    </div>
                </section>
                <!--E　按钮-->
            </div>
            <!--E 第一步的内容-->
            <!--S 第二步的内容-->
            <div style="display:none;" class="createusergroupcount_twowrap">
                <!--S　用户填写面板-->
                <section class="useraddlabel_wrap">
                    <section class="useraddlabel_nav">
                        <span class="useraddlabel_nav_text">基本信息</span>
                    </section>
                    <div class="usersureaddlabel_content">
                        <div class="usersureaddlabel_choose">
                            <div class="userinputaddlabel_type">
                                <span class="userinputaddlabel_description_text">行业分类：</span><select name=""
                                                                                                     id="usergrouptype"
                                                                                                     class="userinputaddlabel_type_text">
                                    <option value=""></option>
                                </select>
                            </div>
                            <div class="userinputaddlabel_name">
                                <span class="userinputaddlabel_description_text">组合名称：</span><input id="usergroupname"
                                                                                                    maxlength="10"
                                                                                                    type="text"
                                                                                                    class="userinputaddlabel_name_text"
                                                                                                    name=""
                                                                                                    placeholder="最多可输入10个字符"/>
                            </div>
                            <div class="userinputaddlabel_description">
                                <span class="userinputaddlabel_description_text">组合含义：</span><textarea
                                        id="usergroupmean" maxlength="400" class="userinputaddlabel_meaning_textarea"
                                        placeholder="最多可输入400个字符"></textarea>
                            </div>
                            <div class="userinputaddlabel_description">
                                <span class="userinputaddlabel_description_text">组合用途：</span><textarea
                                        id="usergroupdeuse" maxlength="50"
                                        class="userinputaddlabel_description_textarea"
                                        placeholder="最多可输入50个字符"></textarea>
                            </div>
                            <div class="userinputaddlabel_time">
                                <span class="userinputaddlabel_description_text">有效时间：</span><input type="text"
                                                                                                    id="starttime"
                                                                                                    readonly=""
                                                                                                    onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})"
                                                                                                    class="Wdate"
                                                                                                    style="height: 25px;border:1px solid #ccc;padding-left: 5px;width: 150px;"/><input
                                        type="text" id="endtime" readonly=""
                                        onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd'})" class="Wdate"
                                        style="height: 25px;border:1px solid #ccc;margin-left: 12px;padding-left: 5px;width: 150px;"/>
                            </div>
                        </div>
                    </div>
                </section>
                <!--E　用户填写面板-->
                <!--S　按钮-->
                <section style="display:none;" class="btngroup_twowrap">
                    <div class="form_data_btn">
                        <div class="center_btn">
                            <button id="two_next_btn" type="button" style="background:#0068b5;color:#fff"
                                    class="btn btn-default base_btn  two_next_btn">
                                <span class="btn_span">下一步</span>
                            </button>
                            <button type="button" class="btn btn-default base_btn  two_pro_btn">
                                <span class="btn_span">上一步</span>
                            </button>
                        </div>
                    </div>
                </section>
                <!--E　按钮-->
            </div>
            <!--E 第二步的内容-->

            <!--S 第三步的内容-->
            <div style="display:none;" class="createusergroupcount_threewrap">
                <!--S　用户填写面板-->
                <section class="useraddlabel_wrap">
                    <div class="nameType">
                        <p>行业分类：<span id="worktypeLabel">金融行业</span></p>
                        <p>组合名称：<span id="mergeName">高端用户</span></p>
                    </div>
                    <section class="useraddlabel_nav">
                        <span class="useraddlabel_nav_text">基本信息</span>
                    </section>
                    <div class="usersureaddlabel_contentsure">
                        <div class="usersureaddlabel_choosesure">
                            <div>
                                <span class="user_three_text">标签用途：</span>
                                <span id="threeusergroupdeuse" class="user_three_text"></span>
                            </div>
                            <div>
                                <span class="user_three_text">有效时间:</span>
                                <span id="time" class="user_three_text"></span>
                            </div>
                        </div>
                    </div>
                    <section class="useraddlabel_nav">
                        <span class="useraddlabel_nav_text">定义条件</span>
                    </section>
                    <div class="usersureaddlabel_contentsure">
                        <div>
                            <span id="threeuseraddlabel_text" class="userinputaddlabel_description_text"></span>
                            <span id="threeuseraddlabel_id" style="display:none;"></span>
                        </div>
                        <span id="threecompeople" class="userinputaddlabel_description_text"></span>
                        <span id="threecoverpeople" class="userinputaddlabel_description_text"></span>
                    </div>
                </section>
                <!--E　用户填写面板-->
                <!--S　按钮-->
                <section style="display:none;" class="btngroup_twowrap">
                    <div class="form_data_btn">
                        <div class="three_center_btn">
                            <button id='addusergroup' type="button"
                                    class="btn btn-default base_btn  three_next_btn saveandcommit">
                                <span class="btn_span">保存并提交申请</span>
                            </button>
                            <button id="three_pro_btn" type="button" class="btn btn-default base_btn  three_pro_btn">
                                <span class="btn_span">上一步</span>
                            </button>
                        </div>
                    </div>
                </section>
                <!--E　按钮-->
            </div>
            <!--E 第三步的内容-->
        </div>
        <!--E cont_right_label-->
    </div>
    <#include "./commons/usergroup_model.ftl"/>
    <!--E 中间的内容-->
    <script src="${basePath}/res/lib/bootstrap3/js/bootstrap.js"></script>
    <script src="${basePath}/res/lib/My97DatePicker/WdatePicker.js"></script>
    <script src="${basePath}/res/lib/layer/layer.js"></script>
    <script src="${basePath}/res/commons/js/ajax.js"></script>
    <script src="${basePath}/res/lib/layer/layer.js"></script>
    <script src="${basePath}/res/commons/js/base.js"></script>
    <script src="${basePath}/res/lib/jquery-confirm.min.js"></script>
    <script src="${basePath}/res/js/mergetag/create.js"></script>
</body>
</html>
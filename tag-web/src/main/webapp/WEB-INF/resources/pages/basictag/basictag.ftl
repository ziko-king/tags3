<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <!--S logo-->
    <link rel="icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <!--E logo-->
    <title>用户画像-标签系统-基础标签</title>
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
    <link href="${basePath}/res/lib/bootstrap3/css/bootstrap-table.css" rel="stylesheet" media="screen">
    <link rel="stylesheet" type="text/css" href="${basePath}/res/commons/css/base.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/res/css/basictag/basictag.css"/>
    <link rel="stylesheet" href="${basePath}/res/lib/jquery-confirm.min.css">
    <style>

        .Userl_radio6 {
            top: 16px;
        }

        /*.shu_name_img{
            background: url('

        ${basePath}

                /res/imgs/hebing.png') no-repeat;
                        }*/
        .outlogin {
            /* background: url('

        ${basePath}  /res/imgs/outlogin.png') no-repeat 20px center; */
        }

        /*.gender2 {
            background: #f5f5f5 url(

        ${basePath}

                /res/imgs/samllsj.png) no-repeat 0px;
                        }*/
        .drop_down::-webkit-scrollbar { /*滚动条整体样式*/
            width: 4px; /*高宽分别对应横竖滚动条的尺寸*/
            height: 4px;
        }

        .drop_down::-webkit-scrollbar-thumb { /*滚动条里面小方块*/
            border-radius: 5px;
            -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
            background: rgba(0, 0, 0, 0.2);
        }

        .drop_down::-webkit-scrollbar-track { /*滚动条里面轨道*/
            -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
            border-radius: 0;
            background: rgba(0, 0, 0, 0.1);
        }
    </style>
    <script src="${basePath}/res/lib/jquery1/jquery.min.js"></script>
    <script src="${basePath}/res/commons/js/ajax.js"></script>
    <script type="text/javascript" src="${basePath}/res/lib/jquery.validate.js"></script>
    <script src="${basePath}/res/lib/localization/messages_zh.js" charset="utf-8"></script>
    <script type="text/javascript" src="${basePath}/res/lib/echarts/echarts.min.js"></script>
    <script src="${basePath}/res/lib/bootstrap3/js/bootstrap-table.js" type="text/javascript"></script>
    <script src="${basePath}/res/lib/bootstrap3/js/bootstrap-table-zh-CN.js" charset="UTF-8"
            type="text/javascript"></script>
    <script src="${basePath}/res/lib/layer/layer.js" type="text/javascript"></script>

</head>
<body>
<div class="container">
    <#include "./commons/header.ftl"/>
    <div class="centent clearfix">
        <div class="cont_left_label">
            <!--S 左侧的一级标签-->
            <!--S 新增一级分类-->
            <div class="leftNav">
                <!--S 左侧的动态的添加的树状列表-->
                <div class="con_gr">
                    <div id="leftList" class="left_gr">
                        <span class="set" data-toggle="modal" data-target="#setModal"><img
                                    src="${basePath}/res/imgs/set.png" alt=""
                                    style="width: 16px;height: 16px;margin-right: 5px;margin-left: 10px;margin-top: 0px;position: absolute;right: 12px;top:10px;display: block;z-index: 5;"></span>
                        <#list basicTagList as levelOne>
                            <!-- 一级标签 -->
                            <div class="firstGr" style="transition: 0.5s;position: relative;">
                                <img src="${basePath}/res/imgs/left_s22.png">
                                <span tid="${levelOne.id}" tlevel="${levelOne.level}"
                                      value='${levelOne.id}-${levelOne.level}'
                                      title=${levelOne.name} onclick='spanClick(this)'>${levelOne.name}</span>
                            </div>

                            <!-- 二级标签 -->
                            <div class="zi">
                                <#list levelOne.subTags as levelTwo>
                                    <div class="secondGr">
                                        <span tid="${levelTwo.id}" tlevel="${levelTwo.level}" class="second_img"
                                              onclick='spanClick(this)'
                                              style="background:url(${basePath}/res/imgs/left_s2.png) no-repeat 0 10px !important;"
                                              value='${levelTwo.id}-${levelTwo.level}'
                                              title=${levelTwo.name}>${levelTwo.name}</span>
                                    </div>
                                    <div class="secondzi">
                                        <#list levelTwo.subTags as levelThree>
                                            <!-- 三级标签 -->
                                            <div class="we">
                                                <div class="we_div">
                                                    <span tid="${levelThree.id}" tlevel="${levelThree.level}"
                                                          class="we_div_span" onclick='spanClick(this)'
                                                          value='${levelThree.id}-${levelThree.level}'
                                                          title=${levelThree.name} style="background:url(${basePath}/res/imgs/left_s2.png)
                                                          no-repeat 0 10px !important;">${levelThree.name}</span>
                                                </div>
                                            </div>
                                            <div class="fourth">
                                                <#list levelThree.subTags as levelFourth>
                                                    <!-- 四级标签 -->
                                                    <div class="four">
                                                        <div class="four_div">
                                                            <span tid="${levelFourth.id}" tlevel="${levelFourth.level}"
                                                                  class="four_div_span" onclick='spanClick(this)'
                                                                  value='${levelFourth.id}-${levelFourth.level}'
                                                                  title=${levelFourth.name}>${levelFourth.name}</span>
                                                        </div>
                                                    </div>
                                                </#list>
                                            </div>
                                        </#list>
                                    </div>
                                    <div class="we"></div>
                                </#list>
                            </div>

                        </#list>
                        <span hidden="">${var1.tagType}</span>
                    </div>
                    <div class="zi"></div>
                    <span hidden="">${var1.tagType}</span>
                </div>
                <div class="add_tab" data-toggle="modal" data-target="#oneModal" style="display:none;">
                    <div class="add_tab_n"><img src="${basePath}/res/imgs/addlabel.png"><span>新建主分类标签</span></div>
                </div>
            </div>
            <!--E 左侧的动态的添加的树状列表-->
        </div>
        <section><!-- 模态框（Modal） -->
            <div class="modal fade" id="setModal" tabindex="-1" role="dialog"
                 aria-labelledby="setModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                ×
                            </button>
                            <h4 class="modal-title" id="twoModalLabel">
                                操作主分类标签
                            </h4>
                        </div>
                        <div class="modal-body" style="height:430px;overflow:auto;">
                            <table class="table table-striped table-bordered table-hover table-text-center"
                                   id="tb_powerset">
                            </table>
                        </div>
                        <div class="modal-footer" style="text-align:center;">
                            <button type="button" class="btn btn-default" data-dismiss="modal">
                                关闭
                            </button>
                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->
            </div>
        </section>
        <!--E 左侧的一级标签-->
        <section><!-- 模态框（Modal） -->
            <div class="modal fade" id="maineditModal" tabindex="-1" role="dialog"
                 aria-labelledby="editModalLabel" aria-hidden="true" style="z-index: 1053;margin-top: 200px;">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                ×
                            </button>
                            <h4 class="modal-title" id="twoModalLabel">
                                编辑主分类标签
                            </h4>
                        </div>
                        <div class="modal-body" style="padding-left:74px;">
                            <span class="typename_span"><span class="tip">*</span>标签名称：</span>
                            <input type="text" maxlength="10" id="labelName" placeholder="最多可输入10个字符"
                                   class="form-control typename_input">
                        </div>
                        <div class="modal-footer" style="text-align:center;">
                            <button id="sureeditlabel" type="button" class="btn btn-primary">
                                确定
                            </button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">
                                关闭
                            </button>

                        </div>
                    </div><!-- /.modal-content -->
                </div><!-- /.modal -->

            </div>
        </section>
        <!--S cont_right_label-->
        <div class="cont_right_label" style="margin-left: 214px; ">
            <!--S 导航-->
            <div class="label_nav">
                <span class="label_nav_sp2">您当前的位置：</span>
                <a href="${basePath}/home/index" class="label_nav_sp1">首页</a>
                <span class="label_nav_sp2">></span>
                <a href="javascript:void(0);" class="label_nav_sp3 active">基础标签</a>
            </div>
            <!--E 导航-->

            <div class="label_cont" style="width:97%;margin:0 auto;">
                <div class="label_cont_top clearfix"
                     style="height:49px;line-height:49px;width:97%;margin:0 auto;font-size:14px;">
                    <!--S 导航的二级标签-->
                    <div id="breadDiv" class="nav_second" style="float: left;height:49px;line-height:49px;">
                        <span id="label_level1" class="label_cont_top_l label_cont_top_l1"
                              value="${basicTagList[0].id}`${basicTagList[0].level}"
                              title=${basicTagList[0].name}>${basicTagList[0].name}</span>
                        <span class="label_nav_sp2">></span>
                        <span id="label_level2" class="label_cont_top_l label_cont_top_l2"
                              value="${basicTagList[0].subTags[0].id}`${basicTagList[0].subTags[0].level}"
                              title=${basicTagList[0].subTags[0].name}>${basicTagList[0].subTags[0].name}</span>
                        <span class="label_nav_sp2">></span>
                        <span id="label_level3" class="label_cont_top_l label_cont_top_l3"
                              value="${basicTagList[0].subTags[0].subTags[0].id}`${basicTagList[0].subTags[0].subTags[0].level}"
                              title=${basicTagList[0].subTags[0].subTags[0].name}>${basicTagList[0].subTags[0].subTags[0].name}</span>
                    </div>

                    <!--E 导航的二级标签-->
                    <div class="label_cont_top_r">
                        <div id="four" style="display:none;float: right;" class="add_tab_n2a">
                            <span class="add_tab_n2" data-toggle="modal" data-target="#childrenModal"><span
                                        class="addIcon">+</span>新建业务标签</span>
                        </div>
                        <div id="five" style="display:none;float: right;" class="add_tab_n2a">
                            <span class="add_tab_n2" data-toggle="modal" data-target="#smallchildrenModal"><span
                                        class="addIcon">+</span>新建属性标签</span>
                        </div>
                        <div class="User_radio_sosuo">
                            <input class="User_input" type="text" name="" id="query" value="" placeholder="请输入关键词检索"/>
                            <span class="Userl_radio6" onclick='searchTag()'></span>
                        </div>
                    </div>
                </div>
                <div>
                    <!--S 新建二级分类-->
                    <div class="label_cont_buttomadd">
                        <!--S 标签-->
                        <div class="drop_down clearfix">
                            <div class="drop_downRight" id='contentTagList'>

                            </div>
                        </div>
                        <!-- <section class="pages">
                          <div class="col-sm-6">
                            <div class="dataTables_info page-num"  role="status" aria-live="polite">
                              当前第<span id='curr'>1</span>/<span id='tota'>50</span>页，每页<span id='size'>10</span>条，共<span id='reco'>1</span>条记录
                            </div>
                          </div>
                          上一頁，下一頁
                          <div class="col-sm-6">
                            <div class="dataTables_info page-num page_num_right" id="sample-table_info" role="status" aria-live="polite">
                              <span class="home_page" id='home_page'>首页</span>
                              <span class="up_page" id='up_page'>上一页</span>
                              <span class="next_page" id='next_page'>下一页</span>
                              <input class="search_page_num" onkeyup="this.value=this.value.replace(/[^\d]/g,'');"></input>
                              <span class="home_page" id='end_page'>尾页</span>
                              <span class="search_page" id='search_page'>跳页</span>
                            </div>
                          </div> -->
                        </section>
                        <!--S 创建一级分类-->
                        <section id="labelsystem_dataTypeId"><!-- 模态框（Modal） -->
                            <!-- 模态框（Modal） -->
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
                                                创建分类标签
                                            </h4>
                                        </div>
                                        <div class="modal-body">
                                            <div class="modalbox first" style="position: relative;height:40px;">
                                                <span class="typename_span">一级标签名称：</span>
                                                <input type="text" name="makeupCo" maxlength="10" required="true"
                                                       placeholder="最多可输入10个字符" id="levelOneName"
                                                       class="form-control typename_input makeup"
                                                       onfocus="setfocus(this)" oninput="setinput(this);"
                                                       onKeypress="javascript:if(event.keyCode == 32)event.returnValue = false;"/>
                                                <select name="makeupCoSe" id="typenum1" onchange="changeF(this)"
                                                        class="makeupCoSe" size="10" style="display:none;">
                                                </select>
                                                <span style="display:none;" class="typename_worn">最多可输入10个字符</span>
                                            </div>
                                            <div class="modalbox second" style="position: relative;height:40px;">
                                                <span class="typename_span">二级标签名称：</span>
                                                <input type="text" name="makeupCo" maxlength="10" required="true"
                                                       placeholder="最多可输入10个字符" id="levelTwoName"
                                                       class="form-control typename_input makeup"
                                                       onfocus="setfocus(this)" oninput="setinput(this);"
                                                       onKeypress="javascript:if(event.keyCode == 32)event.returnValue = false;"/>
                                                <select name="makeupCoSe" id="typenum2" onchange="changeF(this)"
                                                        class="makeupCoSe" size="10" style="display:none;">
                                                </select>
                                                <span style="display:none;" class="typename_worn">最多可输入10个字符</span>
                                            </div>
                                            <div class="modalbox third" style="position: relative;height:40px;">
                                                <span class="typename_span">三级标签名称：</span>
                                                <input type="text" name="makeupCo" maxlength="10" required="true"
                                                       placeholder="最多可输入10个字符" id="levelThreeName"
                                                       class="form-control typename_input makeup"
                                                       onfocus="setfocus(this)" oninput="setinput(this);"
                                                       onKeypress="javascript:if(event.keyCode == 32)event.returnValue = false;"/>
                                                <select name="makeupCoSe" id="typenum3" onchange="changeF(this)"
                                                        class="makeupCoSe" size="10" style="display:none;">
                                                </select>
                                                <span style="display:none;" class="typename_worn">最多可输入10个字符</span>
                                            </div>
                                        </div>
                                        <div class="modal-footer" style="text-align:center;">
                                            <button id="createonetype" type="button" class="btn btn-primary">
                                                创建
                                            </button>
                                            <button type="button" class="btn btn-default" data-dismiss="modal">
                                                取消
                                            </button>
                                        </div>
                                    </div><!-- /.modal-content -->
                                </div><!-- /.modal -->
                        </section>
                        <!--E 创建一级分类-->
                    </div>

                    <!--S 创建四级标签-->
                    <section id="labelsystem_childrenlabel"><!-- 模态框（Modal） -->
                        <div class="modal fade" id="childrenModal" tabindex="-1" role="dialog"
                             aria-labelledby="childrenModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content" style="width:615px;">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                            ×
                                        </button>
                                        <h4 class="modal-title" id="childrenModalLabel">
                                            新建四级标签
                                        </h4>
                                    </div>
                                    <div class="modal-body">
                                        <form class="childrenfourModal">
                                            <div>
                                                <span class="tip">*</span>
                                                <span class="typename_span">标签名称：</span>
                                                <input type="text" id="levelFourName" placeholder="最多可输入10个字符"
                                                       class="form-control typename_input" name="addfourName"
                                                       maxlength="10">
                                                <span style="display:none;color:red;"
                                                      class="fourlabelname_worn">标签名已存在</span>
                                            </div>
                                            <!-- 标签分类 -->
                                            <div style="margin-top: 8px;">
                                                <span class="tip">*</span>
                                                <span class="typename_span">标签分类：</span>
                                                <input type="text" class="sortoneLabel" readonly>
                                                <input type="text" class="sorttwoLabel" readonly>
                                                <input type="text" class="sortthreeLabel" readonly>
                                            </div>
                                            <!-- 更新周期 -->
                                            <div style="margin-bottom: 8px;margin-top: 8px;">
                                                <span class="tip">*</span>
                                                <span class="typename_span">更新周期：</span>
                                                <select name="" id="" class="updatechoice" style="width:55px;">
                                                    <option value="1">每天</option>
                                                    <option value="2">每周</option>
                                                    <option value="3">每月</option>
                                                    <option value="4">每年</option>
                                                </select>
                                                <div style="display: inline-block;" class="timechoice datachoice">
                                                    <input class="Wdate startTime" type="text"
                                                           onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm'})"
                                                           style="height: 30px;border:1px solid #ccc;padding-left: 5px;border-radius: 3px;width: 156px;">
                                                    <input class="Wdate endTime" type="text"
                                                           onfocus="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm'})"
                                                           style="height: 30px;border:1px solid #ccc;padding-left: 5px;border-radius: 3px;width: 156px;">
                                                </div>
                                            </div>
                                            <!--业务含义文本框-->
                                            <div class="type_wrap">
                                                <span class="tip">*</span>
                                                <span class="typename_datatype">业务含义：</span>
                                                <textarea type="text" maxlength="400" id="fourMeanings"
                                                          class="form-control  form-input  business_mean"
                                                          placeholder="最多可输入400个字符" name="fourMeanings"></textarea>
                                                <div><span style="margin-top:15px;display:none" class="typename_worn">最多可输入400个字符</span>
                                                </div>
                                            </div>
                                            <!--标签规则文本框-->
                                            <div class="type_wrap">
                                                <span class="tip">*</span>
                                                <span class="typename_datatype">标签规则：</span>
                                                <textarea type="text" rows="15" cols="60" id="fourLabelRules"
                                                          class="form-control  form-input  business_mean"
                                                          placeholder="key=value,例如:type=hive or hdfs"
                                                          name="fourLabelRules"></textarea>
                                                <div><span style="display:none;"
                                                           class="typename_worn">请输入key=value</span></div>
                                            </div>
                                            <!-- 算法类型 -->
                                            <!-- <div style="margin-bottom: 8px;">
                                                <span class="tip">*</span>
                                                <span class="typename_span">算法类型：</span>
                                                <input type="text" id = "algorithmType" class="form-control typename_input" name="algorithmType">
                                            </div> -->
                                            <!-- 主程序入口 -->
                                            <div style="margin-bottom: 8px;">
                                                <span class="tip">*</span>
                                                <span class="typename_span">程序入口：</span>
                                                <input type="text" id="algorithmMain"
                                                       class="form-control typename_input" name="algorithmMain">
                                            </div>
                                            <!-- 算法名称 -->
                                            <div style="margin-bottom: 8px;">
                                                <span class="tip">*</span>
                                                <span class="typename_span">算法名称：</span>
                                                <input type="text" id="algorithmName"
                                                       class="form-control typename_input" name="algorithmName">
                                            </div>
                                            <!-- 算法引擎 -->
                                            <div class="algorithm">
                                                <span class="tip">*</span>
                                                <span class="typename_span">算法引擎：</span>
                                                <input type="text" readonly=""
                                                       style="height: 30px;border:1px solid #ccc;"
                                                       class="form-control typename_input fileName"/>
                                                <span id="sub_autoupload">上传文件</span>
                                                <input type="file" id="sub_hidefileupload" class="file"/>
                                                <span class="successtip">上传成功</span>
                                            </div>
                                            <!--模型参数 -->
                                            <div class="type_wrap">
                                                <span class="tip">*</span>
                                                <span class="typename_datatype">模型参数：</span>
                                                <textarea type="text" maxlength="1000" id="algorithmmodel"
                                                          class="form-control  form-input  business_mean"
                                                          placeholder="最多可输入1000个字符" name="fifthMeanings"></textarea>
                                                <div><span style="margin-top:15px;display:none" class="typename_worn">最多可输入1000个字符</span>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="modal-footer" style="text-align:center;">
                                        <button id="createchildrentype" type="button" class="btn btn-primary"
                                                data-val="0&0">
                                            创建
                                        </button>
                                        <button type="button" class="btn btn-default" data-dismiss="modal">
                                            取消
                                        </button>
                                    </div>
                                </div><!-- /.modal-content -->
                            </div><!-- /.modal -->
                        </div>

                    </section>
                    <!--E 创建四级标签-->
                    <!--S 创建五级标签-->
                    <section id="labelsystem_childrenlabel"><!-- 模态框（Modal） -->
                        <div class="modal fade" id="smallchildrenModal" tabindex="-1" role="dialog"
                             aria-labelledby="smallchildrenModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content" style="width:615px;">
                                    <div class="modal-header">
                                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                            ×
                                        </button>
                                        <h4 class="modal-title" id="smallchildrenModalLabel">
                                            新建五级标签
                                        </h4>
                                    </div>
                                    <div class="modal-body">
                                        <form action="" class="smallchildrenModalLabel">
                                            <input type="text" id="levelFifthId" style="display:none;"
                                                   name="addfifthId">
                                            <div>
                                                <span class="tip">*</span>
                                                <span class="typename_span">标签名称：</span>
                                                <input type="text" id="levelFifthName" placeholder="最多可输入10个字符"
                                                       class="form-control typename_input" name="addfifthName">
                                                <span style="display:none;color:red;"
                                                      class="labelname_worn">标签名已存在</span>
                                            </div>
                                            <!--业务含义文本框-->
                                            <div class="type_wrap">
                                                <span class="tip">*</span>
                                                <span class="typename_datatype">业务含义：</span>
                                                <textarea type="text" maxlength="400" id="fifthMeanings"
                                                          class="form-control  form-input  business_mean"
                                                          placeholder="最多可输入400个字符" name="fifthMeanings"></textarea>
                                                <div><span style="margin-top:15px;display:none" class="typename_worn">最多可输入400个字符</span>
                                                </div>
                                            </div>
                                            <!--标签规则文本框-->
                                            <div class="type_wrap">
                                                <span class="tip">*</span>
                                                <span class="typename_datatype">标签规则：</span>
                                                <textarea type="text" maxlength="50" id="fifthLabelRules"
                                                          class="form-control  form-input  business_mean"
                                                          placeholder="最多可输入50个字符" name="fifthLabelRules"></textarea>
                                                <div><span style="display:none;" class="typename_worn">最多可输入50个字符</span>
                                                </div>
                                            </div>
                                            <!--模型参数 -->
                                            <!-- <div class="type_wrap">
                                              <span class="tip">*</span>
                                              <span class="typename_datatype">模型参数：</span>
                                              <textarea type="text" maxlength="1000" id="fifthMeanings" class="form-control  form-input  business_mean"
                                                        placeholder="最多可输入1000个字符" name="fifthMeanings"></textarea>
                                              <div><span style="margin-top:15px;display:none" class="typename_worn">最多可输入1000个字符</span></div>
                                            </div> -->
                                            <!-- 算法引擎 -->
                                            <!-- <div class="algorithm">
                                        <span class="tip">*</span>
                                        <span class="typename_span">算法引擎：</span>
                                        <input type="text" readonly="" style="height: 30px;border:1px solid #ccc;" class="form-control typename_input" />
                                        <span id="sub_autoupload">上传文件</span>
                                        <input type="file" id="sub_hidefileupload" class="file"/>
                                        <span class="commondel" style="cursor:pointer;margin-left: 20px;"><img src="${basePath}/res/imgs/55.png"></span>
                                    </div>   -->
                                        </form>
                                    </div>
                                    <div class="modal-footer" style="text-align:center;">
                                        <button id="createsmallchildrentype" title='create' type="button"
                                                class="btn btn-primary">
                                            创建
                                        </button>
                                        <button type="button" class="btn btn-default" data-dismiss="modal">
                                            取消
                                        </button>
                                    </div>
                                </div><!-- /.modal-content -->
                            </div><!-- /.modal -->
                        </div>
                    </section>
                    <!--E 创建五级标签-->
                </div>
            </div>
        </div>
    </div>

    <script src="${basePath}/res/js/basictag/create.js"></script>
    <script src="${basePath}/res/lib/bootstrap3/js/bootstrap.js"></script>
    <script src="${basePath}/res/lib/bootstrap3/js/jquery.treegrid.min.js"></script>
    <script src="${basePath}/res/lib/bootstrap3/js/jquery.treegrid.bootstrap3.js"></script>
    <script src="${basePath}/res/js/basictag/create.js"></script>
    <script src="${basePath}/res/lib/bootstrap3/js/jquery.treegrid.extension.js"></script>
    <script src="${basePath}/res/lib/My97DatePicker/WdatePicker.js"></script>
    <!-- <script src="${basePath}/res/js/basictag/fun.js"></script>-->
    <script src="${basePath}/res/commons/js/base.js"></script>
    <!-- <script src="${basePath}/res/js/basictag/create.js"></script>-->
    <script src="${basePath}/res/js/basictag/basictag.js"></script>
    <script src="${basePath}/res/js/basictag/manager.js"></script>
    <!-- <script src="../../js/json2.js"></script> -->
    <script src="${basePath}/res/lib/jquery-confirm.min.js"></script>
</body>
</html>
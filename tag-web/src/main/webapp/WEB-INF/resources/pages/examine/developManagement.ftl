<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <!--S logo-->
    <link rel="icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="${basePath}/res/imgs/favicon.ico" type="image/x-icon">
    <!--E logo-->
    <title>审核管理-标签管理系统</title>
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
    <link rel="stylesheet" href="${basePath}/res/lib/jquery-confirm.min.css">
    <link rel="stylesheet" type="text/css" href="${basePath}/res/css/releaseManagement.css"/>
    <script src="${basePath}/res/lib/jquery1/jquery.min.js"></script>
    <script src="${basePath}/res/lib/jquery-confirm.min.js"></script>
</head>
<body>
<div class="container">
    <#include "./commons/header.ftl"/>
    <div class="centent clearfix">
        <div class="cont_left_label">
            <!--S 左侧的一级标签-->
            <div class="leftNav">
                <div class="con_gr">
                    <div class="left_gr">
                        <div class="firstGr" style="transition: 0.5s;position: relative;">
                            <img src="${basePath}/res/imgs/71.png">
                            <span value=${var1.id} title=${var1.name}><a href="${basePath}/examine/page/apply">申请管理</a></span>
                        </div>
                        <div class="firstGr" style="transition: 0.5s;position: relative;">
                            <img src="${basePath}/res/imgs/71.png">
                            <span value=${var1.id} title=${var1.name}><a
                                        href="${basePath}/examine/page/examine">审批管理</a></span>
                        </div>
                        <div class="firstGr" style="transition: 0.5s;position: relative;">
                            <img src="${basePath}/res/imgs/71.png">
                            <span value=${var1.id} title=${var1.name}><a
                                        href="${basePath}/examine/page/develop">开发管理</a></span>
                        </div>
                        <div class="firstGr" style="transition: 0.5s;position: relative;">
                            <img src="${basePath}/res/imgs/71.png">
                            <span value=${var1.id} title=${var1.name}><a
                                        href="${basePath}/examine/page/release">发布管理</a></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--S cont_right_label-->
        <div class="cont_right_label">
            <!--S 导航-->
            <div class="label_nav">
                <img src="${basePath}/res/imgs/home.png" style="margin-left:12px;margin-top: -5px;"/>
                <span class="label_nav_sp2">您当前的位置：</span>
                <a href="javacript:void(0);" class="label_nav_sp1">首页</a>
                <span class="label_nav_sp2">></span>
                <a href="javacript:void(0);" class="label_nav_sp3">审核管理</a>
                <span class="label_nav_sp2">></span>
                <a href="javacript:void(0);" class="label_nav_sp3 active">开发管理</a>
            </div>
            <!--E 导航-->

            <!--S 据顶部的div-->
            <div class="labeltask_middle_wrap"></div>
            <!--E 据顶部的div-->
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
                                </div>
                            </div>
                            <div class="User_centerIN_tubiao current_table">
                                <!--table-->
                                <section class="table_wrap clearfix all-table-container">
                                    <div class="cover_style">
                                        <!--用户列表失败-->
                                        <div>
                                            <table id="failtable"
                                                   class="table table-striped table-bordered table-hover table-text-center">
                                                <thead class="all-table-header">
                                                <tr>
                                                    <th width="">申请类型</th>
                                                    <th width="">申请要求</th>
                                                    <th width="">标签名称</th>
                                                    <th width="">标签规则</th>
                                                    <th width="">算法模型</th>
                                                    <th width="">申请人</th>
                                                    <th width="">最后处理人</th>
                                                    <th>最后处理时间</th>
                                                    <th width="">处理结果</th>
                                                    <th width="">审批理由</th>
                                                    <th width="">审批状态</th>
                                                    <th width="">操作</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <tr>
                                                    <td>组合标签</td>
                                                    <td>组合标签创建</td>
                                                    <td>A小A</td>
                                                    <td title="123123123123" style="cursor: pointer;">
                                                        <div style="    width: 66px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                                                            123123123123
                                                        </div>
                                                    </td>
                                                    <td title="模型1模型1模型1模型1" style="cursor: pointer;">
                                                        <div style="width: 66px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                                                            模型1模型1模型1模型1
                                                        </div>
                                                    </td>
                                                    <td>demo</td>
                                                    <td>--</td>
                                                    <td>2016-01-10 18:10:10</td>
                                                    <td></td>
                                                    <td></td>
                                                    <td>
                                                        <div class="work workFail">申请中</div>
                                                    </td>
                                                    <td class="resale" data-toggle="modal" data-target="#popermodal">
                                                        开发
                                                    </td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </section>
                                <!--/.table-->
                                <!--pages-->
                                <!--number-->
                                <!--  <section class="pages">
                                    <div class="col-sm-6">
                                        <div class="dataTables_info page-num"  role="status" aria-live="polite">
                                            当前第<span id='curr'>1</span>/<span id='tota'>50</span>页，每页<span id='size'>10</span>条，共<span id='reco'>1</span>条记录
                                        </div>
                                    </div>
                                    上一頁，下一頁
                                    <div class="col-sm-6">
                                        <div class="dataTables_info page-num page_num_right" id="fail-table_info" role="status" aria-live="polite">
                                            <span class="home_page" id='home_page'>首页</span>
                                            <span class="up_page" id='up_page'>上一页</span>
                                            <span class="next_page" id='next_page'>下一页</span>
                                            <input class="search_page_num" onkeyup="this.value=this.value.replace(/[^\d]/g,'');"></input>
                                            <span class="home_page" id='end_page'>尾页</span>
                                            <span class="search_page" id='search_page'>跳页</span>
                                        </div>
                                    </div>
                                </section> -->
                            </div>
                        </div>
                    </div>
                </section>

            </div>
            <!--E 图表的内容-->
        </div>
        <!--E cont_right_label-->
    </div>
    <!-- 模态框（Modal） -->
    <section>
        <div class="modal fade" id="popermodal" tabindex="-1" role="dialog"
             aria-labelledby="popermodalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                            ×
                        </button>
                        <h4 class="modal-title" id="popermodalLabel">
                            开发审批
                        </h4>
                    </div>
                    <div class="modal-body modal-body_wrap">
                        <span class="typename_span">标签名称：</span>
                        <span class=""></span>
                    </div>
                    <div class="modal-body modal-body_wrap">
                        <span class="typename_span">标签分类：</span>
                        <span class=""></span>
                    </div>
                    <div class="modal-body modal-body_wrap">
                        <span class="typename_span">更新周期：</span>
                        <span class=""></span>
                    </div>
                    <div class="modal-body modal-body_wrap">
                        <span class="typename_span">业务含义：</span>
                        <span></span>
                    </div>
                    <div class="modal-body modal-body_wrap">
                        <span class="typename_span">算法引擎：</span>
                        <span></span>
                    </div>
                    <div class="instruction_upload" style="color: red;">
                        *请开发人员自行到指定路径补完算法逻辑，再行启用该标签算法算法存储路径：http://192.168.0.0/123/234/789
                    </div>
                    <div class="modal-footer" style="text-align:center;">
                        <button id="createchildrentype" type="button" class="btn btn-primary execute">
                            启用
                        </button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">
                            取消
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <script>
            $.ajax({
                url: basePath + '/taskListAll',
                type: 'POST',
                async: true,
                data: JSON.stringify(req),
                contentType: "application/json; charset=utf-8",
                dataType: 'json',
                success: function (data) {
                    $.fn.jqLoading("destroy");
                    if (data.code == 200 && data.data != null) {

                    } else {
                        popMessage(data.msg, 'red');
                    }
                },
                error: function (xhr) {
                    $.fn.jqLoading("destroy");
                    popMessage('服务器错误', 'red');
                }
            });
            $("#createonetype").on('click', function (e) {
                $("#oneModal").modal("hide");
                // $("#oneModal input[type='text']").val("");
            });
        </script>
    </section>
    <!--E 中间的内容-->
    <script type="text/javascript" src="${basePath}/res/lib/bootstrap3/js/bootstrap.js"></script>
    <script type="text/javascript" src="${basePath}/res/commons/js/base.js"></script>
    <script type="text/javascript" src="${basePath}/res/lib/layer/layer.js"></script>
    <script type="text/javascript" src="${basePath}/res/js/examine/developManagement.js"></script>
</body>
</html>


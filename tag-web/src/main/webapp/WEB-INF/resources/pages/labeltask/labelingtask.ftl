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
    <link rel="stylesheet" type="text/css" href="${basePath}/res/commons/css/base.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/res/lib/jquery-confirm.min.css"/>
    <link rel="stylesheet" type="text/css" href="${basePath}/res/lib/zTreeStyle/zTreeStyle.css"/>
    <link rel="stylesheet" type="text/css" type="text/css" href="${basePath}/res/css/labelingtask.css"/>
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
                <a href="javacript:void(0);" class="label_nav_sp3 active">标签任务</a>
            </div>
            <!--E 导航-->
            <!--S 图表的内容-->
            <div class="labeltask_bottom_wrap">
                <section class="label_bottom_wrap">
                    <div class="User_center">
                        <div id="User_centerIN" class="User_centerIN">
                            <div class="User_centerIN_top">
                                <div class="labelTab">
                                    <span class="status centerIN_top1 current">全部</span>
                                    <span class="status centerIN_top2">失败</span>
                                    <span class="status centerIN_top3">成功</span>
                                    <span class="status centerIN_top4">任务管理</span>
                                </div>
                                <div class="User_radio_sosuo">
                                    <input class="User_input" type="text" name="" id="query" value=""
                                           placeholder="请输入关键词检索"/>
                                    <span class="Userl_radio6"></span>
                                </div>
                            </div>
                            <div class="User_centerIN_all current_table s_table">
                                <!--table-->
                                <section class="table_wrap clearfix all-table-container">
                                    <div class="cover_style">
                                        <!--用户列表全部-->
                                        <div>
                                            <table id="alltasktable" class="table">
                                                <thead class="all-table-header">
                                                <tr>
                                                    <th width="">申请类型</th>
                                                    <!-- <th width="">申请要求</th> -->
                                                    <th width="">标签名称</th>
                                                    <th width="">标签描述</th>
                                                    <th width="">算法模型</th>
                                                    <th width="">申请人</th>
                                                    <th width="">最后处理人</th>
                                                    <th width="">任务开始时间</th>
                                                    <th width="">任务结束时间</th>
                                                    <th width="">任务状态</th>
                                                </tr>
                                                </thead>
                                                <tbody></tbody>
                                            </table>
                                        </div>
                                    </div>
                                </section>
                                <!--/.table-->
                                <!--pages-->
                                <!--number-->
                                <!-- <section class="pages">
                                    <div class="col-sm-6">
                                        <div class="dataTables_info page-num"  role="status" aria-live="polite">
                                            当前第<span id='curr'>1</span>/<span id='tota'>50</span>页，每页<span id='size'>10</span>条，共<span id='reco'>1</span>条记录
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="dataTables_info page-num page_num_right" id="all-table_info" role="status" aria-live="polite">
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
                            <div class="User_centerIN_tubiao s_table">
                                <!--table-->
                                <section class="table_wrap clearfix all-table-container">
                                    <div class="cover_style">
                                        <!--用户列表失败-->
                                        <div>
                                            <table id="failtable" class="table">
                                                <thead class="all-table-header">
                                                <tr>
                                                    <th width="">申请类型</th>
                                                    <!-- <th width="">申请要求</th> -->
                                                    <th width="">标签名称</th>
                                                    <th width="">标签描述</th>
                                                    <th width="">算法模型</th>
                                                    <th width="">申请人</th>
                                                    <th width="">最后处理人</th>
                                                    <th width="">任务开始时间</th>
                                                    <th width="">任务结束时间</th>
                                                    <th width="">任务状态</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <!-- <tr>
                                                    <td>组合标签</td>
                                                    <td>组合标签创建</td>
                                                    <td>A小A</td>
                                                    <td title="123" style="cursor: pointer;"><div style="    width: 66px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">123123123123</div></td>
                                                    <td title="http://baidu.com" style="cursor: pointer;"><div style="    width: 66px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">模型1模型1模型1模型1</div></td>
                                                    <td>demo</td>
                                                    <td>--</td>
                                                    <td>2016-01-10 18:10:10</td>
                                                    <td>2016-01-10 18:10:10</td>
                                                    <td><div class="work workFail">任务失败</div></td>
                                                </tr> -->
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </section>
                                <!--/.table-->
                                <!--pages-->
                                <!--number-->
                                <!-- <section class="pages">
                                   <div class="col-sm-6">
                                       <div class="dataTables_info page-num"  role="status" aria-live="polite">
                                           当前第<span id='curr'>1</span>/<span id='tota'>50</span>页，每页<span id='size'>10</span>条，共<span id='reco'>1</span>条记录
                                       </div>
                                   </div>
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
                            <div class="User_centerIN_liebiao s_table">
                                <!--table-->
                                <section class="table_wrap clearfix all-table-container">
                                    <div class="cover_style">
                                        <!--用户列表成功-->
                                        <div>
                                            <table id="pusuetable" class="table">
                                                <thead class="all-table-header">
                                                <tr>
                                                    <th width="">申请类型</th>
                                                    <!-- <th width="">申请要求</th> -->
                                                    <th width="">标签名称</th>
                                                    <th width="">标签描述</th>
                                                    <th width="">算法模型</th>
                                                    <th width="">申请人</th>
                                                    <th width="">最后处理人</th>
                                                    <th width="">任务开始时间</th>
                                                    <th width="">任务结束时间</th>
                                                    <th width="">任务状态</th>
                                                </tr>
                                                </thead>
                                                <tbody>
                                                <!-- <tr>
                                                    <td>组合标签</td>
                                                    <td>组合标签创建</td>
                                                    <td>A小A</td>
                                                    <td title="123" style="cursor: pointer;"><div style="    width: 66px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">123123123123</div></td>
                                                    <td title="http://baidu.com" style="cursor: pointer;"><div style="    width: 66px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">模型1模型1模型1模型1</div></td>
                                                    <td>demo</td>
                                                    <td>--</td>
                                                    <td>2016-01-10 18:10:10</td>
                                                    <td>2016-01-10 18:10:10</td>
                                                    <td><div class="work workSuccess">任务完成</div></td>
                                                </tr> -->
                                                </tbody>
                                            </table>
                                        </div>
                                </section>
                                <!--/.table-->
                                <!--pages-->
                                <!--number-->
                                <!-- <section class="pages">
                                    <div class="col-sm-6">
                                        <div class="dataTables_info page-num"  role="status" aria-live="polite">
                                            当前第<span id='curr'>1</span>/<span id='tota'>50</span>页，每页<span id='size'>10</span>条，共<span id='reco'>1</span>条记录
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="dataTables_info page-num page_num_right" id="success-table_info" role="status" aria-live="polite">
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
                            <div class="User_centerIN_taskmanage s_table">
                                <!--table-->
                                <section class="table_wrap clearfix all-table-container">
                                    <div class="cover_style">
                                        <!--用户列表管理-->
                                        <div>
                                            <table id="taskmanagetable" class="table">
                                                <thead class="all-table-header">
                                                <tr>
                                                    <th width="">申请类型</th>
                                                    <!-- <th width="">申请要求</th> -->
                                                    <th width="" style="width: 115px;">标签名称</th>
                                                    <th width="">标签描述</th>
                                                    <th width="" style="width:84px;">算法模型</th>
                                                    <th width="">申请人</th>
                                                    <th width="">最后处理人</th>
                                                    <th width="">任务开始时间</th>
                                                    <th width="">任务结束时间</th>
                                                    <th width="">任务状态</th>
                                                    <th width="" style="text-align:center;">操作</th>
                                                </tr>
                                                </thead>
                                                <tbody class="taskmanage">
                                                <!-- <tr>
                                                    <td>组合标签</td>
                                                    <td>组合标签创建</td>
                                                    <td>A小A</td>
                                                    <td title="123" style="cursor: pointer;"><div style="    width: 66px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">123123123123</div></td>
                                                    <td title="http://baidu.com" style="cursor: pointer;"><div style="    width: 66px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">模型1模型1模型1模型1</div></td>
                                                    <td>demo</td>
                                                    <td>--</td>
                                                    <td>2016-01-10 18:10:10</td>
                                                    <td>2016-01-10 18:10:10</td>
                                                    <td><div class="work workSuccess">任务完成</div></td>
                                                    <td class="td-manage"><a title="暂停" class="btn btn-xs pusetask" value="0" disabled="disabled" style="color: rgb(0, 0, 0);"><i class="fa fa-check   bigger-120"></i>暂停</a><a title="停用" class="btn btn-xs failtask" value="0" style="color: rgb(0, 0, 0);"><i class="fa fa-check bigger-120"></i>停用</a><a title="启用" class="btn btn-xs stoptask" value="0" data-toggle="modal" data-target="#openModal"><i class="fa fa-check bigger-120"></i>停用</a></td>
                                                </tr>
                                                <tr>
                                                    <td>组合标签</td>
                                                    <td>组合标签创建</td>
                                                    <td>A小A</td>
                                                    <td title="123" style="cursor: pointer;"><div style="    width: 66px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">123123123123</div></td>
                                                    <td title="http://baidu.com" style="cursor: pointer;"><div style="    width: 66px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">模型1模型1模型1模型1</div></td>
                                                    <td>demo</td>
                                                    <td>--</td>
                                                    <td>2016-01-10 18:10:10</td>
                                                    <td>2016-01-10 18:10:10</td>
                                                    <td><div class="work workSuccess">任务完成</div></td>
                                                    <td class="td-manage"><a title="暂停" class="btn btn-xs pusetask" value="0"><i class="fa fa-check   bigger-120"></i>暂停</a><a title="停用" class="btn btn-xs failtask" value="0" style="color: rgb(0, 0, 0);"><i class="fa fa-check bigger-120"></i>停用</a><a title="启用" class="btn btn-xs runtask" value="0" data-toggle="modal" data-target="#openModal"><i class="fa fa-check bigger-120"></i>启用</a></td>
                                                </tr> -->
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </section>
                                <!--/.table-->
                                <!--pages-->
                                <!--number-->
                                <!-- <section class="pages">
                                    <div class="col-sm-6">
                                        <div class="dataTables_info page-num"  role="status" aria-live="polite">
                                            当前第<span id='curr'>1</span>/<span id='tota'>50</span>页，每页<span id='size'>10</span>条，共<span id='reco'>1</span>条记录
                                        </div>
                                    </div>
                                    <div class="col-sm-6">
                                        <div class="dataTables_info page-num page_num_right" id="manage-table_info" role="status" aria-live="polite">
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
    <!--E 中间的内容-->
    <script type="text/javascript" src="${basePath}/res/lib/bootstrap3/js/bootstrap.js"></script>
    <script type="text/javascript" src="${basePath}/res/commons/js/base.js"></script>
    <script type="text/javascript" src="${basePath}/res/commons/js/ajax.js"></script>
    <script type="text/javascript" src="${basePath}/res/lib/layer/layer.js"></script>
    <script type="text/javascript" src="${basePath}/res/lib/jquery.ztree.core-3.5.js"></script>
    <script type="text/javascript" src="${basePath}/res/js/labeltask/labeltask.js"></script>
</body>
</html>
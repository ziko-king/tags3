// date 类型转换为string
function dateToStr(dt) {
    var o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "h+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

/*列表*/
function getList() {
    var dataReq = new Object();
    var pages = new Object();
    pages.cp = 1;
    pages.pr = 10;
    dataReq.bean = pages;
    dataReq.name = $("#query").val();
    $.axsGet(false, "../merge/queryMergeTagData", {json: JSON.stringify(dataReq)}, function (data) {

        if (data.code == 10005) {
            if (data.data != null) {
                //清空table
                usergroup.addTableRecord(data.data.data);
                var max_count = data.data.page.tr;

                function pageselectCallback(page_id) {
                    dataReq.bean.cp = page_id + 1;
                    $.axsGet(false, "../merge/queryMergeTagData", {json: JSON.stringify(dataReq)}, function (data) {
                        if (data.code == 10005) {
                            if (data.data != null) {
                                //清空table
                                usergroup.addTableRecord(data.data.data);
                            }
                        } else {
                            layer.msg(data.msg);
                        }
                    }, function (e) {
                        layer.msg('数据服务错误！');
                    });
                }

                // 创建分页元素
                $("#Pagination").pagination(max_count, {
                    num_edge_entries: 2,
                    num_display_entries: 6,
                    items_per_page: 10,
                    callback: pageselectCallback  //回调函数
                });

            }
        } else {
            $(".cover_style").find("tbody").html("");
            $("#Pagination").pagination(0, {
                num_edge_entries: 2,
                num_display_entries: 6,
                items_per_page: 10
            });
            layer.msg(data.msg);
        }
    }, function (e) {
        layer.msg('数据服务错误！');
    });
}

/**组合标签模糊搜索**/
$('.User_radio_sosuo .Userl_radio6').click(function () {
    getList();
});

/*删除*/
function isDel(ug_id) {
    var req = new Object();
    req.mergeTagId = ug_id;
    layer.confirm('是否删除该标签？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        //确定删除执行的ajax
        jQuery.ajaxPost(true, '../merge/delMergeTagForId', req, function (data) {
            if (data.code == 10004) {
                layer.msg(data.msg);
                location.reload('../merge/index');
            } else {
                layer.msg(data.msg);
            }
        }, function (xhr) {
            layer.msg("服务器错误！");
        })
    }, function () {

    });

}

//权限
function adddeledit() {
    /*验证当前用户是否有增删改的权限*/
    var mergeArr = [];
    if ($(".curRole").val() == "supper_admin") {
        var mergeArr = ['add', 'del', 'edit'];
        for (var i = 0; i < mergeArr.length; i++) {
            if (mergeArr[i] == "add") {
                $(".centerIN_top3").show();
            } else if (mergeArr[i] == "edit") {
                $(".cover_style tbody tr").find(".combinEdit").show();
            } else if (mergeArr[i] == "del") {
                $(".cover_style tbody tr").find(".delete_ug").show();
            }
        }
    } else {
        var mergeArr = $("#navbar .active").attr("permids").split(",");
        for (var i = 0; i < mergeArr.length; i++) {
            if (mergeArr[i] == "add") {
                $(".centerIN_top3").show();
            } else if (mergeArr[i] == "edit") {
                $(".cover_style tbody tr").find(".combinEdit").show();
            } else if (mergeArr[i] == "del") {
                $(".cover_style tbody tr").find(".delete_ug").show();
            }
        }
    }
}

/**导航切换**/
$(document).ready(function () {
    //列表
    getList();
    if ($(".table_wrap").height() < 330) {
        $(".table_wrap").css("height", "330px");
    }


    /**组合标签数据删除**/
    $(document).on('click', '.delete_ug', function () {
        var id = $(this).attr('data-id');
        isDel(id);
    });
    //列表数据编辑
    $(document).on('click', '.combinEdit', function () {
        var id = $(this).attr('data-id');
        window.location.href = '../merge/updateMergeTagView?id=' + id;
    })
});
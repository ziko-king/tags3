/**
 * Created by syw on 2017/5/5.
 */
function user_search() {
    $('#current_company').text(0);
    $('.admin_left_userlist').children().css("background", "#fff");
    var req = new Object();
    var page = new Object();
    page.curPage = 1;
    req.pageBean = page;
    if ($('.search_componyname_input').val().trim() != '') {
        req.companyName = $('.search_componyname_input').val().trim();
    }
    if ($('.search_username_input').val().trim() != '') {
        req.name = $('.search_username_input').val().trim();
    }
    updateCharacterList(req);
}

function remindMessage(message, func, userid) {
    $.confirm({
        title: '提示信息!',
        content: message,
        buttons: {
            sure: {
                text: '确认',
                btnClass: 'btn-blue',
                keys: ['enter', 'shift'],
                action: function () {
                    func(userid);
                }
            },
            cancel: {
                text: '取消',
                keys: ['enter', 'shift'],
                action: function () {
                    console.log('取消');
                }
            },
        }
    });
}

//格式化日期
function FormatDate(strTime) {
    var date = new Date(strTime);
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    m = m < 10 ? ('0' + m) : m;
    var d = date.getDate();
    d = d < 10 ? ('0' + d) : d;
    var h = date.getHours();
    h = h < 10 ? ('0' + h) : h;
    var minute = date.getMinutes();
    minute = minute < 10 ? ('0' + minute) : minute;
    var second = date.getSeconds();
    second = second < 10 ? ('0' + second) : second;
    return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
}

function resetPW(userid) {
    var req = new Object();
    req.userId = userid;
    $.ajax({
        url: basePath + '/resetPassword',
        type: 'POST',
        async: false,
        data: JSON.stringify(req),
        timeout: 5000,    //超时时间
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            if (data.code == 200) {
                popMessage(data.msg, 'blue');
            } else {
                popMessage(data.msg, 'red');
            }
        },
        error: function (xhr) {
            popMessage("服务器错误", 'red');
            console.log('错误');
        }
    });
}

function delUser(userid) {
    var req = new Object();
    req.userId = userid;
    $.ajax({
        url: basePath + '/delSysUser',
        type: 'POST',
        async: false,
        data: JSON.stringify(req),
        timeout: 5000,    //超时时间
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            if (data.code == 200) {
                popMessage(data.msg, 'blue');
                window.location.href = basePath + '/systemSet';
            } else {
                popMessage(data.msg, 'red');
            }
        },
        error: function (xhr) {
            popMessage("服务器错误", 'red');
            console.log('错误');
        }
    });
}

function delRole(roleid) {
    var req = new Object();
    req.id = roleid;
    $.ajax({
        url: basePath + '/delSysRole',
        type: 'POST',
        async: false,
        data: JSON.stringify(req),
        timeout: 5000,    //超时时间
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            if (data.code == 200) {
                popMessage(data.msg, 'blue');
                var req = new Object();
                var page = new Object();
                page.curPage = 1;
                req.pageBean = page;
                updateRoleList(req);
            } else {
                popMessage(data.msg, 'red');
            }
        },
        error: function (xhr) {
            popMessage("服务器错误", 'red');
            console.log('错误');
        }
    });
}

function popMessage(message, color) {
    $.confirm({
        title: '提示信息',
        closeIcon: true,
        boxWidth: '30%',
        useBootstrap: false,
        type: color,
        typeAnimated: true,
        content: message,
        buttons: {
            somethingElse: {
                text: '确定',
                btnClass: 'btn-blue',
                keys: ['enter']
            }
        }
    });
}

function updateUserPageInfo(pagebean) {
    var spans = $('#user_table_info').find('span');
    spans.eq(0).text(pagebean.curPage);
    spans.eq(1).text(pagebean.pageCount);
    spans.eq(2).text(pagebean.pageSize);
    spans.eq(3).text(pagebean.rowsCount);
    if (pagebean.curPage == pagebean.pageCount) {
        $('#user_page_info').find('.next_page').css('background', '#ddd').css('cursor', 'default');
        $('#user_page_info').find('.next_page').attr('disabled', 'true');
    } else {
        $('#user_page_info').find('.next_page').css('background', '#fff').css('cursor', 'pointer');
        $('#user_page_info').find('.next_page').removeAttr('disabled');
    }
    if (pagebean.curPage == 1) {
        $('#user_page_info').find('.up_page').css('background', '#ddd').css('cursor', 'default');
        $('#user_page_info').find('.up_page').attr('disabled', 'true');
    } else {
        $('#user_page_info').find('.up_page').css('background', '#fff').css('cursor', 'pointer');
        $('#user_page_info').find('.up_page').removeAttr('disabled');
    }
}

function updateRolePageInfo(pagebean) {
    var spans = $('#role_table_info').find('span');
    spans.eq(0).text(pagebean.curPage);
    spans.eq(1).text(pagebean.pageCount);
    spans.eq(2).text(pagebean.pageSize);
    spans.eq(3).text(pagebean.rowsCount);
    if (pagebean.curPage == pagebean.pageCount) {
        $('#role_page_info').find('.next_page').css('background', '#ddd').css('cursor', 'default');
        $('#role_page_info').find('.next_page').attr('disabled', 'true');
    } else {
        $('#role_page_info').find('.next_page').css('background', '#fff').css('cursor', 'pointer');
        $('#role_page_info').find('.next_page').removeAttr('disabled');
    }
    if (pagebean.curPage == 1) {
        $('#role_page_info').find('.up_page').css('background', '#ddd').css('cursor', 'default');
        $('#role_page_info').find('.up_page').attr('disabled', 'true');
    } else {
        $('#role_page_info').find('.up_page').css('background', '#fff').css('cursor', 'pointer');
        $('#role_page_info').find('.up_page').removeAttr('disabled');
    }
}

function updateRoleTableRecord(roleBeans) {
    var tablebody = $('#role_table_wrap').find('tbody');
    tablebody.children().remove();
    for (var i = 0; i < roleBeans.length; i++) {
        var current = roleBeans[i];
        var record = $('<tr></tr>').attr('value', current.id);
        var number = $('<td></td>').text(i + 1);
        var name = $('<td></td>').text(current.name);
        var create = $('<td></td>').text(current.createName).attr('value', current.createUId);
        var ctime = $('<td></td>').text(FormatDate(current.ctime));
        var operation = $('<td class="td-manage">'
            + '<a data-toggle="modal" data-target="#editModal" class="btn btn-xs role_edit" title="编辑">'
            + '<i class="fa fa-list-ul bigger-120"></i>编辑</a>'
            + '<a title="删除" class="btn btn-xs role_dele">'
            + '<i class="fa fa-check  bigger-120"></i>删除 </a></td>');
        record.append(number, name, create, ctime, operation);
        tablebody.append(record);
    }
}


function updateTableRecord(sysSetBeans) {
    var tablebody = $('#table_wrap').find('tbody');
    tablebody.children().remove();
    // 加载table数据
    for (var i = 0; i < sysSetBeans.length; i++) {
        var current = sysSetBeans[i];
        var record = $('<tr></tr>').attr('value', current.userId);
        var number = $('<td></td>').text(i + 1);
        var company_name = $('<td></td>').text(current.companyName).attr('value', current.companyId);
        var job = $('<td></td>').text(current.job);
        var name = $('<td></td>').text(current.name);
        var username = $('<td></td>').text(current.userName);
        var phone = $('<td></td>').text(current.phone);
        var role_name = $('<td></td>').text(current.roleName).attr('value', current.roleId);   //时间格式修改
        var operation = $('<td class="td-manage">' +
            '<a  data-toggle="modal" data-target="#usermanageeditModal" class="btn btn-xs edit_user" title="编辑">' +
            '<i class="fa fa-list-ul bigger-120"></i>编辑 </a>' +
            '<a title="删除" class="btn btn-xs dele_user">' +
            '<i class="fa fa-check  bigger-120"></i>删除 </a>' +
            '<a title="重置密码" class="btn btn-xs reset_pw">' +
            ' <i class="fa fa-check  bigger-120"></i>重置密码 </a>');
        record.append(number, company_name, job, name, username, phone, role_name, operation);
        tablebody.append(record);
    }
}


function updateCharacterList(req) {
    $.ajax({
        url: basePath + '/queryUserByCompanyId',
        type: 'POST',
        async: false,
        data: JSON.stringify(req),
        timeout: 5000,    //超时时间
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            if (data.code == 200) {
                var result = data.data;
                updateTableRecord(result.sysSetBeans);
                updateUserPageInfo(result.pageBean);
            } else if (data.code == 204) {
                var tablebody = $('#table_wrap').find('tbody');
                tablebody.children().remove();
                var pagebean = new Object();
                pagebean.curPage = 1;
                pagebean.pageCount = 1;
                pagebean.pageSize = 10;
                pagebean.rowsCount = 0;
                updateUserPageInfo(pagebean);
                /*popMessage(data.msg,'red');*/
            } else {
                popMessage(data.msg, 'red');
            }
        },
        error: function (xhr) {
            popMessage("服务器错误", 'red');
            console.log('错误');
        }
    });
}


function updateRoleList(req) {
    $.ajax({
        url: basePath + '/queryRole',
        type: 'POST',
        async: false,
        data: JSON.stringify(req),
        timeout: 5000,    //超时时间
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            if (data.code == 200) {
                var result = data.data;
                updateRoleTableRecord(result.list);
                updateRolePageInfo(result.pageBean);
            } else if (data.code == 204) {
                var tablebody = $('#role_table_wrap').find('tbody');
                tablebody.children().remove();
                var pagebean = new Object();
                pagebean.curPage = 1;
                pagebean.pageCount = 1;
                pagebean.pageSize = 10;
                pagebean.rowsCount = 0;
                updateRolePageInfo(pagebean);
                popMessage(data.msg, 'red');
            } else {
                popMessage(data.msg, 'red');
            }
        },
        error: function (xhr) {
            popMessage("服务器错误", 'red');
            console.log('错误');
        }
    });
}

$('#fileupload input').fileinput({
    language: 'zh', //设置语言
    allowedFileExtensions: ['xlsx', 'xlx'],//接收的文件后缀
    maxFileCount: 1,//文件的最大数目
    enctype: 'multipart/form-data',
    showCaption: true,//是否显示文本框
    showUpload: false,//是否显示上传按钮
    showRemove: true,//是否显示删除按钮
    msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",//文件的数量以及最大数值
});


$(document).ready(function () {
    var req = new Object();
    var page = new Object();
    page.curPage = 1;
    req.pageBean = page;
    //updateCharacterList(req);
    //updateRoleList(req);
    $(".usermanage").css("background", "#d0e2fa");
    $(".usermanage").find("span.usermanage_text").css("color", "#0068b5");
    $(".usermanage").find("img.userimg").attr("src", "../../image/systemlabel_active.png");


    //列表的溢出的处理
    var clientHeight = window.screen.height - 180;
    var objectObj = $(".admin_bottom_wrap");
    objectObj.css("height", clientHeight + 'px');
    var admin_left_userlist = window.screen.height - 249;
    var adminObjectObj = $(".admin_left_userlist");
    adminObjectObj.css("height", admin_left_userlist + 'px');
    if ($(".cover_style").height() < 330) {
        $(".cover_style").css("height", "330px");
    }
});

/************添加组织机构**************/
$('.admin_left_top_img').click(function () {
    $('#company_name').val('');
    $('#company_industry').val('');
});


$('#createOrganization').click(function () {
    var companyName = $('#company_name').val();
    var industry = $('#company_industry').val();
    if (companyName.trim() == '' || industry.trim() == '') {
        popMessage('请填写完整信息', 'red');
    } else {
        var req = new Object();
        req.companyName = companyName;
        req.industry = industry;
        $.ajax({
            url: basePath + '/addOrganization',
            type: 'POST',
            async: false,
            data: JSON.stringify(req),
            timeout: 5000,    //超时时间
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            success: function (data) {
                if (data.code == 200) {
                    popMessage(data.msg, 'blue');
                    window.location.href = basePath + '/systemSet';
                } else {
                    popMessage(data.msg, 'red');
                    $('#institutionModal').hide();
                }
            },
            error: function (xhr) {
                popMessage("服务器错误", 'red');
                console.log('错误');
            }
        });
    }
});
/************添加用户**************/
//清除表单内容
$('.add_btn').click(function () {
    $('#rolelistModal').find('input').val('');
    $.ajax({
        url: basePath + '/queryRole',
        type: 'GET',
        async: false,
        data: null,
        timeout: 5000,    //超时时间
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            if (data.code == 200) {
                var target = $('#rolelistModal').find('ul').last();
                var result = data.data;
                for (var i = 0; i < result.length; i++) {
                    var li1 = $('<li></li>');
                    var a1 = $('<a class="base_dropdown-menu_ul_a"></a>');
                    a1.attr('value', result[i].id).text(result[i].name);
                    var li2 = $('<li role="separator" style="line-height:0.418571;" class="divider"></li>');
                    li1.append(a1);
                    target.append(li1, li2);
                }
            }
        },
        error: function (xhr) {
            popMessage("服务器错误", 'red');
            console.log('错误');
        }
    });
});

$('#create_user').click(function () {
    var companyId = $('#user_companyid').val().trim();
    var username = $('#user_username').val().trim();
    var password = $('#user_password').val().trim();
    var name = $('#user_name').val().trim();
    var duty = $('#user_job').val().trim();
    var phone = $('#user_phone').val().trim();
    var telephone = $('#user_telephone').val().trim();
    var roleId = $('#user_role').val().trim();

    if (companyId == '' || username == '' || password == '' || name == '' || roleId == '') {
        popMessage('请填写必要信息', 'red');
    } else {
        var req = new Object();
        req.companyId = companyId;
        req.username = username;
        req.newpassword = md5(password);
        req.name = name;
        req.duty = duty;
        req.phone = phone;
        req.telephone = telephone;
        req.roleId = roleId;
        $.ajax({
            url: basePath + '/addSysUser',
            type: 'POST',
            async: false,
            data: JSON.stringify(req),
            timeout: 5000,    //超时时间
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            success: function (data) {
                if (data.code == 200) {
                    popMessage(data.msg, 'blue');
                    window.location.href = basePath + '/systemSet';
                } else {
                    popMessage(data.msg, 'red');
                    $('#rolelistModal').hide();
                }
            },
            error: function (xhr) {
                popMessage("服务器错误", 'red');
                console.log('错误');
            }
        });
    }
});


/************重置密码操作**************/
$(document).on('click', ".reset_pw", function () {
    var userid = $(this).parents('tr').attr('value');
    remindMessage('是否重置密码？', resetPW, userid);
});

/************下拉列表的选值**************/
$(document).on("click", ".dropdown-menu li a", function (event) {
    var $this = $(this);
    //按鈕
    var $a = $this.parents('ul').prev();
    var value = $(this).attr('value');
    $a.text($this.text());
    $a.val(value);
    $a.attr("title", $(this).attr('title'));
    $a.append("<span style='margin-left: 5px;' class='caret'>");
});


$(".usermanage").on("click", function () {
    $(this).css("background", "#d0e2fa");
    $(this).find("span.usermanage_text").css("color", "#0068b5");
    $(this).find("img.userimg").attr("src", basePath + "/res/image/systemlabel_active.png");
    $(".user_right_wrap").css("display", "block");
    $(".role_right_wrap").css("display", "none");
    $(".left_gr").not($(this)).css("background", "rgb(245, 247, 250)");
    $(".com_span").not($(this).find("span.usermanage_text")).css("color", "#000");
    $(".com_img").not($(this).find("img.userimg")).attr("src", basePath + "/res/image/nav/nav_labelsystem.png");

});
$(".rolemanage").on("click", function () {
    $(this).css("background", "#d0e2fa");
    $(this).find("span.rolemanage_text").css("color", "#0068b5");
    $(this).find("img.roleimg").attr("src", basePath + "/res/image/systemlabel_active.png");
    $(".user_right_wrap").css("display", "none");
    $(".role_right_wrap").css("display", "block");
    $(".left_gr").not($(this)).css("background", "rgb(245, 247, 250)");
    $(".com_span").not($(this).find("span.rolemanage_text")).css("color", "#000");
    $(".com_img").not($(this).find("img.roleimg")).attr("src", basePath + "/res/image/nav/nav_labelsystem.png");
});

//删除
$(document).on('click', ".dele_user", function () {
    var userid = $(this).parents('tr').attr('value');
    remindMessage('是否删除该用户？', delUser, userid);
});

/*********所属机构点击事件**********/
$(document).on('click', ".list_item_div", function () {
    $('.search_componyname_input').val('');
    $('.search_username_input').val('');
    $(this).css("background", "#d6e5f1").siblings().css("background", "#fff");
    $('#current_company').text($(this).find('span').attr('value'));
    var req = new Object();
    var page = new Object();
    page.curPage = 1;
    req.pageBean = page;
    req.companyId = $(this).find('span').attr('value');
    updateCharacterList(req);
});
/*********所属机构点击事件**********/


/*********用户列表首页**********/
$('#user_page_info .home_page').click(function () {
    var req = new Object();
    var page = new Object();
    page.curPage = 1;
    req.pageBean = page;
    if ($('#current_company').text() != 0) {
        req.companyId = $('#current_company').text();
    }
    if ($('.search_componyname_input').val().trim() != '') {
        req.companyName = $('.search_componyname_input').val().trim();
    }
    if ($('.search_username_input').val().trim() != '') {
        req.name = $('.search_username_input').val().trim();
    }
    updateCharacterList(req);
});
/***********用户管理下一页**************/
$('#user_page_info .next_page').click(function () {
    if ($(this).attr('disabled') != 'disabled') {
        var curPage = $('#user_table_info').find('span').eq(0);
        var req = new Object();
        var page = new Object();
        page.curPage = parseInt(curPage.text()) + 1;
        req.pageBean = page;
        if ($('#current_company').text() != 0) {
            req.companyId = $('#current_company').text();
        }
        if ($('.search_componyname_input').val().trim() != '') {
            req.companyName = $('.search_componyname_input').val().trim();
        }
        if ($('.search_username_input').val().trim() != '') {
            req.name = $('.search_username_input').val().trim();
        }
        updateCharacterList(req);
    } else {
        return false;
    }
});

/***********用户管理上一页**************/
$('#user_page_info .up_page').click(function () {
    if ($(this).attr('disabled') != 'disabled') {
        var curPage = $('#user_table_info').find('span').eq(0);
        var req = new Object();
        var page = new Object();
        page.curPage = parseInt(curPage.text()) - 1;
        req.pageBean = page;
        if ($('#current_company').text() != 0) {
            req.companyId = $('#current_company').text();
        }
        if ($('.search_componyname_input').val().trim() != '') {
            req.companyName = $('.search_componyname_input').val().trim();
        }
        if ($('.search_username_input').val().trim() != '') {
            req.name = $('.search_username_input').val().trim();
        }
        updateCharacterList(req);
    } else {
        return false;
    }
});

/***********用户管理尾页**************/
$('#user_page_info .end_page').click(function () {
    var endPage = $('#user_table_info').find('span').eq(1);
    var req = new Object();
    var page = new Object();
    page.curPage = parseInt(endPage.text());
    req.pageBean = page;
    if ($('#current_company').text() != 0) {
        req.companyId = $('#current_company').text();
    }
    if ($('.search_componyname_input').val().trim() != '') {
        req.companyName = $('.search_componyname_input').val().trim();
    }
    if ($('.search_username_input').val().trim() != '') {
        req.name = $('.search_username_input').val().trim();
    }
    updateCharacterList(req);
});

/***********用户管理跳页**************/
$('#user_page_info .search_page').click(function () {
    var endPage = $('#user_table_info').find('span').eq(1).text();
    var req = new Object();
    var page = new Object();
    var search_num = $('#user_page_info .search_page_num').val();
    if (baseMethod.isNum(search_num)) {
        if (parseInt(search_num) < 1) {
            page.curPage = 1;
        } else if (parseInt(search_num) > parseInt(endPage)) {
            page.curPage = parseInt(endPage);
        } else {
            page.curPage = parseInt(search_num);
        }
        req.pageBean = page;
        if ($('#current_company').text() != 0) {
            req.companyId = $('#current_company').text();
        }
        if ($('.search_componyname_input').val().trim() != '') {
            req.companyName = $('.search_componyname_input').val().trim();
        }
        if ($('.search_username_input').val().trim() != '') {
            req.name = $('.search_username_input').val().trim();
        }
        updateCharacterList(req);
    } else {
        popMessage('请输入数字', 'red');
    }
});

/*************用户管理搜索*****************/
$('.search_componyname_btn').click(user_search);
$('.search_componyname_input').blur(user_search);
$('.search_username_input').blur(user_search);

//用户管理的编辑操作第一步 得到表格的数据，第二步把数据填充到弹窗中
$(document).on('click', ".edit_user", function () {
    var thobj = $(this).parents('tr').find("td");
    var userid = $(this).parents('tr').attr('value');
    //公司
    var conpanyName = thobj.eq(1).text();
    var conpanyId = thobj.eq(1).attr('value');
    $("#edit_admincompony").text(conpanyName).attr('value', conpanyId);
    //职务
    var job = thobj.eq(2).text();
    $("#edit_admin_post").val(job);
    //姓名
    var name = thobj.eq(3).text();
    $("#edit_adminName").val(name);
    //用户名
    var userName = thobj.eq(4).text();
    $("#edit_userName").val(userName).attr('uid', userid);
    //手机号
    var phone = thobj.eq(5).text();
    $("#edit_admin_phoneNumber").val(phone);
    //角色
    var roleName = thobj.eq(6).text();
    var roleId = thobj.eq(6).attr('value');
    $("#adminRole").text(roleName).attr('value', roleId);

    $.ajax({
        url: basePath + '/queryRole',
        type: 'GET',
        async: false,
        data: null,
        timeout: 5000,    //超时时间
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (data) {
            if (data.code == 200) {
                var target = $('#usermanageeditModal').find('ul').last();
                var result = data.data;
                for (var i = 0; i < result.length; i++) {
                    var li1 = $('<li></li>');
                    var a1 = $('<a class="base_dropdown-menu_ul_a"></a>');
                    a1.attr('value', result[i].id).text(result[i].name);
                    var li2 = $('<li role="separator" style="line-height:0.418571;" class="divider"></li>');
                    li1.append(a1);
                    target.append(li1, li2);
                }
            }
        },
        error: function (xhr) {
            popMessage("服务器错误", 'red');
            console.log('错误');
        }
    });
});
//点击编辑按钮的操作第一关闭弹窗(判断数据是否填写完整)，第二把修改的数据存入数据库，第三步重新渲染界面
$("#edit_user_btn").on("click", function () {

    var userId = $('#edit_userName').attr('uid').trim();
    var companyId = $('#edit_admincompony').val().trim();
    var username = $('#edit_userName').val().trim();
    var password = $('#edit_adminPwd').val().trim();
    var name = $('#edit_adminName').val().trim();
    var duty = $('#edit_admin_post').val().trim();
    var phone = $('#edit_admin_phoneNumber').val().trim();
    var telephone = $('#edit_admin_officephone').val().trim();
    var roleId = $('#adminRole').val().trim();

    if (!(companyId && username && password && name && roleId && userId)) {
        popMessage('请填写必要信息', 'red');
    } else {
        var req = new Object();
        req.companyId = companyId;
        req.username = username;
        req.newpassword = md5(password);
        req.name = name;
        req.duty = duty;
        req.phone = phone;
        req.telephone = telephone;
        req.roleId = roleId;
        req.userId = userId;
        $.ajax({
            url: basePath + '/updateSysUser',
            type: 'POST',
            async: false,
            data: JSON.stringify(req),
            timeout: 5000,    //超时时间
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            success: function (data) {
                if (data.code == 200) {
                    popMessage(data.msg, 'blue');
                    window.location.href = basePath + '/systemSet';
                } else {
                    popMessage(data.msg, 'red');
                }
            },
            error: function (xhr) {
                popMessage("服务器错误", 'red');
                console.log('错误');
            }
        });
    }
});
//角色管理的编辑
$(document).on('click', ".role_edit", function () {
    var role_id = $(this).parents('tr').attr("value");
    var roleName = $(this).parents('tr').find('td').eq(1).text();
    $("#rolename_input").val(roleName).attr('roleid', role_id);
});

$('#upload_submit').click(function () {
    var formData = new FormData(document.getElementById("fileupload"));
    $.ajax({
        url: basePath + '/microQuery',
        type: 'POST',
        data: formData,
        contentType: false,
        processData: false,
        success: function (data) {
            var obj = JSON.parse(data);
            if (obj.code == 200) {
                popMessage(obj.msg, 'blue');
            } else {
                popMessage(obj.msg, 'red');
            }
        },
        error: function (data) {
            popMessage("服务器错误", 'red');
            console.log('错误');
        }
    });
});


//关闭弹窗
$("#excutehavair").on('click', function () {
    var roleName = $("#rolename_input").val();
    var roleid = $("#rolename_input").attr('roleid');
    if (roleName == '') {
        popMessage('请填写角色名称', 'red');
    } else {
        var req = new Object();
        req.name = roleName;
        req.id = roleid;
        $.ajax({
            url: basePath + '/updateSysRole',
            type: 'POST',
            async: false,
            data: JSON.stringify(req),
            timeout: 5000,    //超时时间
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            success: function (data) {
                if (data.code == 200) {
                    popMessage(data.msg, 'blue');
                    $('#editModal').hide();
                    var req = new Object();
                    var page = new Object();
                    page.curPage = 1;
                    req.pageBean = page;
                    updateRoleList(req);
                } else {
                    popMessage(data.msg, 'red');
                    $('#editModal').hide();
                }
            },
            error: function (xhr) {
                popMessage("服务器错误", 'red');
                console.log('错误');
            }
        });
    }
});

//角色管理的删除
$(document).on('click', ".role_dele", function () {
    var role_id = $(this).parents('tr').attr("value");
    remindMessage('是否删除该角色？', delRole, role_id);
});
//点击角色添加 清楚输入框内容
$('.rolemanage_add_text').click(function () {
    $('#oneModal input').val('');
});
$('#createRoleType').click(function () {
    var roleName = $('#oneModal input').val().trim();
    if (roleName == '') {
        popMessage('请填写角色名称', 'red');
    } else {
        var req = new Object();
        req.name = roleName;
        $.ajax({
            url: basePath + '/addSysRole',
            type: 'POST',
            async: false,
            data: JSON.stringify(req),
            timeout: 5000,    //超时时间
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            success: function (data) {
                if (data.code == 200) {
                    popMessage(data.msg, 'blue');
                    $('#oneModal').hide();
                    var req = new Object();
                    var page = new Object();
                    page.curPage = 1;
                    req.pageBean = page;
                    updateRoleList(req);
                } else {
                    popMessage(data.msg, 'red');
                    $('#oneModal').hide();
                }
            },
            error: function (xhr) {
                popMessage("服务器错误", 'red');
                console.log('错误');
            }
        });
    }
});

/*********角色列表首页**********/
$('#role_page_info .home_page').click(function () {
    var req = new Object();
    var page = new Object();
    page.curPage = 1;
    req.pageBean = page;
    if ($('.rolemanage_input').val().trim() != '') {
        req.roleName = $('.rolemanage_input').val().trim();
    }
    updateRoleList(req);
});
/***********角色管理下一页**************/
$('#role_page_info .next_page').click(function () {
    if ($(this).attr('disabled') != 'disabled') {
        var curPage = $('#role_table_info').find('span').eq(0);
        var req = new Object();
        var page = new Object();
        page.curPage = parseInt(curPage.text()) + 1;
        if ($('.rolemanage_input').val().trim() != '') {
            req.roleName = $('.rolemanage_input').val().trim();
        }
        req.pageBean = page;
        updateRoleList(req);
    } else {
        return false;
    }
});

/***********角色管理上一页**************/
$('#role_page_info .up_page').click(function () {
    if ($(this).attr('disabled') != 'disabled') {
        var curPage = $('#role_table_info').find('span').eq(0);
        var req = new Object();
        var page = new Object();
        page.curPage = parseInt(curPage.text()) - 1;
        if ($('.rolemanage_input').val().trim() != '') {
            req.roleName = $('.rolemanage_input').val().trim();
        }
        req.pageBean = page;
        updateRoleList(req);
    } else {
        return false;
    }
});

/***********角色管理尾页**************/
$('#role_page_info .end_page').click(function () {
    var endPage = $('#role_table_info').find('span').eq(1);
    var req = new Object();
    var page = new Object();
    if ($('.rolemanage_input').val().trim() != '') {
        req.roleName = $('.rolemanage_input').val().trim();
    }
    page.curPage = parseInt(endPage.text());
    req.pageBean = page;
    updateRoleList(req);
});

/***********角色管理跳页**************/
$('#role_page_info .search_page').click(function () {
    var endPage = $('#role_table_info').find('span').eq(1).text();
    var req = new Object();
    var page = new Object();
    if ($('.rolemanage_input').val().trim() != '') {
        req.roleName = $('.rolemanage_input').val().trim();
    }
    var search_num = $('#role_page_info .search_page_num').val();
    if (baseMethod.isNum(search_num)) {
        if (parseInt(search_num) < 1) {
            page.curPage = 1;
        } else if (parseInt(search_num) > parseInt(endPage)) {
            page.curPage = parseInt(endPage);
        } else {
            page.curPage = parseInt(search_num);
        }
        req.pageBean = page;
        updateRoleList(req);
    } else {
        popMessage('请输入数字', 'red');
    }
});

/***********角色管理查询**************/
$('.rolemanage_span').click(function () {
    var req = new Object();
    var page = new Object();
    page.curPage = 1;
    req.pageBean = page;
    if ($('.rolemanage_input').val().trim() != '') {
        req.roleName = $('.rolemanage_input').val().trim();
    }
    updateRoleList(req);
});

$('.rolemanage_input').blur(function () {
    var req = new Object();
    var page = new Object();
    page.curPage = 1;
    req.pageBean = page;
    if ($('.rolemanage_input').val().trim() != '') {
        req.roleName = $('.rolemanage_input').val().trim();
    }
    updateRoleList(req);
});
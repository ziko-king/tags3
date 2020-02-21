/**
 * Created by syw on 2017/5/5.
 */
$(document).ready(function () {
    var req = new Object();
    var page = new Object();
    $(".admin_left_userlist .list_item_div:first-child .companyName:first-child span.company").addClass("active");
    var groupId = $(".admin_left_userlist .list_item_div:first-child .companyName:first-child span.active").attr("value");
    var groupName = $(".admin_left_userlist .list_item_div:first-child .companyName:first-child span.active").text();
    var str = '<s>'
        + '<span class="addGroupNext" title="添加" data-toggle="modal" data-target="#groupModel" style="width:12px;"></span>'
        + '<span class="aditGroupNext" title="编辑" data-toggle="modal" data-target="#editgroupModel" style="width:12px;"></span>'
        + '<span class="delGroupNext" title="删除" data-id="' + groupId + '" style="width:12px;"></span>'
        + '</s>'
    $(".admin_left_userlist .list_item_div:first-child .companyName:first-child span.company").after(str);
    page.curPage = 1;
    req.pageBean = page;
    req.groupId = $(".list_item_div .companyName:first-child span.active").attr("value");
    //模态框传值
    $("#addUsermanageForm .groupName").text(groupName);
    $("#addUsermanageForm .groupName").attr("data-id", groupId);
    $('#groupModel .groupNames').text(groupName);
    $('#groupModel .groupNames').attr("data-id", groupId);
    $("#editgroupModel .groupNames").val(groupName);
    $('#editgroupModel .groupNames').attr("data-id", groupId);
    //调用列表
    updateCharacterList(req);
    updateRoleList(req);
    $(".usermanage").css("background", "#424a51");
    //$(".usermanage").css("background","#d0e2fa");
    //$(".usermanage").find("span.usermanage_text").css("color","#0068b5");
    //$(".usermanage").find("img.userimg").attr("src",basePath + "/res/imgs/systemlabel_active.png");

    //列表的溢出的处理
    var clientHeight = window.screen.height - 180;
    var objectObj = $(".admin_bottom_wrap");
    objectObj.css("height", clientHeight + 'px');
    var admin_left_userlist = window.screen.height - 214;
    var adminObjectObj = $(".admin_left_userlist");
    adminObjectObj.css("height", admin_left_userlist + 'px');
    /*if($(".cover_style").height()<330){
        $(".cover_style").css("height","330px");
    }*/

    $(".label_nav a:last-child").addClass("active");

});

function user_search() {
//	$('#current_company').text(0);
    //$('.admin_left_userlist').children().css("background","#ddd");
    var req = new Object();
    var page = new Object();
    page.curPage = 1;
    req.pageBean = page;
//	if($('.search_username_input').val().trim()!=''){
//		req.companyName=$('.search_username_input').val().trim();
//	}
    if ($('.User_input').val().trim() != '') {
        req.name = $('.User_input').val().trim();
    }
    req.groupId = $(".list_item_div .active").attr("value");
    updateCharacterList(req);
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


// 更新角色列表数据
function updateRoleTableRecord(roleBeans, page_id) {
    var tablebody = $('#role_table_wrap').find('tbody');
    tablebody.children().remove();
    for (var i = 0; i < roleBeans.length; i++) {
        var current = roleBeans[i];
        var record = $('<tr></tr>').attr('value', current.id);
        var number = $('<td></td>').text(page_id * 10 + i + 1);
        var name = $('<td></td>').text(current.name);
        var remark = $('<td></td>').text(current.remark);
//	    var create=$('<td></td>').text(current.createName).attr('value',current.createUId);
        var ctime = $('<td></td>').text(FormatDate(current.ctime));
        var operation = $('<td class="td-manage" style="text-align:center;">'
            + '<span data-toggle="modal" data-target="#editModal" class="role_edit" title="编辑">编辑</span>'
            + '<span title="删除" class="role_dele">删除</span>'
            + '<span class="reset_resource" data-toggle="modal" data-target="#resourceModal">资源权限</span>'
            + '<span class="role_date" data-toggle="modal" data-target="#dateModal">数据权限</span></td>');
        record.append(number, name, remark, ctime, operation);
        tablebody.append(record);
    }
}

//更新角色列表数据
function updateRoleSelectRecord(roleBeans) {
    var selectDiv = $('#addUserRole');
    selectDiv.children().remove();
    for (var i = 0; i < roleBeans.length; i++) {
        var current = roleBeans[i];
        var record = $('<option></option>').attr('value', current.id).text(current.name);
//        record.append(number,name,remark,ctime,operation);
        selectDiv.append(record);
    }
}

// 渲染用户数据
function updateTableRecord(sysSetBeans, page_id) {
    var tablebody = $('#table_wrap').find('tbody');
    tablebody.children().remove();
    // 加载table数据
    for (var i = 0; i < sysSetBeans.length; i++) {
        var current = sysSetBeans[i];
        var record = $('<tr></tr>').attr('value', current.userId).attr('state', current.state);
        var number = $('<td></td>').text(page_id * 10 + i + 1);
        var name = $('<td></td>').text(current.name);
        var username = $('<td></td>').text(current.userName);
        var phone = $('<td></td>').text(current.phone);
        var email = $('<td></td>').text(current.email);
        var role_name = $('<td></td>').text(current.roleName).attr('value', current.roleId);   //时间格式修改
        var pendHtml = '';
        if (current.state == 1) {
            pendHtml = '<span title="禁用" class="dis_use">禁用 </span></td>';
        } else {
            pendHtml = '<span title="启用" class="dis_use en_use">启用 </span></td>'
        }
        // 增加启用
        var operation = $('<td class="td-manage"  style="text-align:center;">' +
            '<span  data-toggle="modal" data-target="#usermanageeditModal" class="edit_user" title="编辑">编辑 </span>' +
            '<span title="删除" class="dele_user">删除 </span>' +
            '<span title="重置密码" class="reset_pw">重置密码 </span>' +
            pendHtml);
        record.append(number, name, username, phone, email, role_name, operation);
        tablebody.append(record);
    }
}

// 查询用户列表
function updateCharacterList(req) {
    jQuery.axsGet(true, basePath + '/user/list', {json: JSON.stringify(req)}, function (data) {
        if (data.code == 10005) {
            var result = data.data;
            updateTableRecord(result.sysSetBeans, 0);
            var max_count = data.data.pageBean.rowsCount;

            //updateUserPageInfo(result.pageBean);
            function pageselectCallback(page_id) {
                req.pageBean.curPage = page_id + 1;
                $.axsGet(true, basePath + "/user/list", {json: JSON.stringify(req)}, function (data) {
                    if (data.code == 10005) {
                        if (data.data != null) {
                            //清空table
                            updateTableRecord(data.data.sysSetBeans, page_id);
                        }
                    } else {
                        layer.msg(data.msg);
                    }
                }, function (e) {
                    layer.msg('数据服务错误！');
                });
            }

            // 创建分页元素
            $("#Pagination1").pagination(max_count, {
                num_edge_entries: 2,
                num_display_entries: 6,
                items_per_page: 10,
                callback: pageselectCallback  //回调函数
            });
        } else {
            $('#table_wrap').find('tbody').html("");
            $("#Pagination1").pagination(0, {
                num_edge_entries: 2,
                num_display_entries: 6,
                items_per_page: 10
            });
            layer.msg(data.msg);
        }
    }, function (xhr) {
        layer.msg("服务器错误！")
    })

}

// 加载角色数据
function updateRoleList(req) {
    jQuery.axsGet(true, basePath + '/role/list', {json: JSON.stringify(req)}, function (data) {
        if (data.code == 10005) {
            var result = data.data;
            updateRoleTableRecord(result.list, 0);
            var max_count = data.data.pageBean.rowsCount;

            function pageselectCallback(page_id) {
                req.pageBean.curPage = page_id + 1;
                $.axsGet(false, basePath + "/role/list", {json: JSON.stringify(req)}, function (data) {
                    if (data.code == 10005) {
                        if (data.data != null) {
                            updateRoleTableRecord(data.data.list, page_id);
                        }
                    } else {
                        layer.msg(data.msg);
                    }
                }, function (e) {
                    layer.msg('数据服务错误！');
                });
            }

            // 创建分页元素
            $("#Pagination2").pagination(max_count, {
                num_edge_entries: 2,
                num_display_entries: 6,
                items_per_page: 10,
                callback: pageselectCallback  //回调函数
            });
        } else {
            $('#role_table_wrap').find('tbody').html("");
            $("#Pagination2").pagination(0, {
                num_edge_entries: 2,
                num_display_entries: 6,
                items_per_page: 10
            });
            layer.msg(data.msg);
        }
    }, function (xhr) {
        layer.msg("服务器错误！")
    })

}

//加载角色数据
function updateOrgList(req) {
    jQuery.axsGet(true, basePath + '/org/list', {json: JSON.stringify(req)}, function (data) {
        if (data.code == 10005) {
            var result = data.data;
            //


        } else {
            layer.msg(data.msg);
        }
    }, function (xhr) {
        layer.msg("服务器错误！")
    })

}

//批量导入
$('#sub_hidefileupload').change(function () {
    var file = document.getElementById("sub_hidefileupload").files[0];
    if (!file) {
        return;
    } else if (file.name.indexOf(".jar") > 0) {
        var formData = new FormData();
        formData.append("file1", file);
        $.ajax({
            url: "${basePath}/basic/modelUpload",
            type: "POST",
            data: formData,
            dataType: 'json',
            contentType: false,
            processData: false,
            success: function (data) {
                if (data.code == 10001) {
                    //alert(data.data);
                    //bounced("新增标签成功!","blue");
                    //$("#childrenModal").modal("hide");
                    //location.reload();
                    $('#sub_hidefileupload').attr('data-url', data.data);
                    $('.fileName').val(file.name);
                }
            },
            error: function () {
                alert("上传失败！");
            }
        });
    } else {
        layer.msg("请上传.jar结尾的文件")
    }
});


/************添加组织机构**************/
$('.admin_left_top_img').click(function () {
    $('#company_name').val('');
    $('#company_industry').val('');
});

//添加机构和部门
$('#addOrg').click(function () {

});

//删除机构和部门
$(".admin_left_top_del").on("click", function () {
    if ($(".companyName span").length == 1) {
        $(".companyName").append('<span class="delcompany">X</span>');
        $(".childItem").append('<span class="delchild">X</span>');
    } else {
        $(".delcompany").remove();
        $(".delchild").remove();
    }
    $(".delcompany").on("click", function () {
        if ($(this).parents(".list_item_div").find("div").hasClass("childItem")) {
            layer.msg("不能删除此公司");
        } else {
            //var userid=$(this).parents('tr').attr('value');
            layer.confirm('是否删除？', {
                btn: ['确定', '取消'] //按钮
            }, function () {
                //确定删除执行的ajax
                jQuery.axsGet(true, url, {}, function (data) {
                    if (data.code == 200) {
                        layer.msg(data.msg);
                    } else {
                        layer.msg(data.msg);
                    }
                }, function (xhr) {
                    layer.msg("服务器错误!")
                })
            }, function () {

            });
        }
    });
});

/************添加用户**************/
//添加清除表单内容
$('.add_btn').click(function () {
    $("#userlistModalLabel").text("添加用户");
    $('#userlistModal').find('input').val('');
    $('#userlistModal').find('label.error').text('');
    /*$.ajax({
           url:basePath+'/queryRole',
           type:'GET',
           async:false,
           data:null,
           timeout:5000,    //超时时间
           contentType:"application/json; charset=utf-8",
           dataType:'json',
           success:function(data){
            if(data.code==200){
               var target=$('#userlistModal').find('ul').last();
               var result=data.data;
               for(var i=0;i<result.length;i++){
                  var li1=$('<li></li>');
                  var a1=$('<a class="base_dropdown-menu_ul_a"></a>');
                  a1.attr('value',result[i].id).text(result[i].name);
                  var li2=$('<li role="separator" style="line-height:0.418571;" class="divider"></li>');
                  li1.append(a1);
                  target.append(li1,li2);
               }
             }
           },
           error:function(xhr){
               popMessage("服务器错误",'red');
               console.log('错误');
           }
       });*/
});

function userVlidate() {
    //用户名
    $.validator.addMethod("isUser", function (value, element) {
        var length = value.length;
        var user = /^[A-Za-z0-9\u4e00-\u9fa5]{1,20}$/;
        return this.optional(element) || user.test(value);
    }, "请正确填写用户名");
    // 密码验证
    $.validator.addMethod("isPwd", function (value, element) {
        var length = value.length;
        var pwd = /^[a-zA-Z0-9]{8,16}$/;
        return this.optional(element) || pwd.test(value);
    }, "请正确填写8-16位的数字和字母");
    //姓名
    $.validator.addMethod("isName", function (value, element) {
        var length = value.length;
        var user = /^[A-Za-z0-9\u4e00-\u9fa5]{1,10}$/;
        return this.optional(element) || user.test(value);
    }, "请正确填写姓名");
    // 手机号码验证"^(0[0-9]{2,3}/-)?([2-9][0-9]{6,7})+(/-[0-9]{1,4})?$"
    $.validator.addMethod("isMobile", function (value, element) {
        var length = value.length;
        var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
        return this.optional(element) || (length == 11 && mobile.test(value));
    }, "请正确填写您的手机号码");
    // 办公号码验证
    $.validator.addMethod("isphone", function (value, element) {
        var length = value.length;
        var phone = /(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$/;
        return this.optional(element) || phone.test(value);
    }, "请正确填写办公号码");

    $(".usermanageForm").validate({
        rules: {
            user_username: {
                required: true,
                isUser: true
            },
            user_password: {
                required: true,
                isPwd: true
            },
            user_name: {
                required: true,
                isName: true
            },
            user_useremail: {
                email: true
            },
            user_userphone: {
                required: true,
                isMobile: true
            },
            user_baphone: {
                required: true,
                isphone: true
            }
        },
        messages: {
            user_username: {
                required: "请输入用户名",
                isUser: "请正确填写用户名"
            },
            user_password: {
                required: "请输入密码",
                isPwd: "请输入 8到16 位数字或字母"
            },
            user_name: {
                required: "请输入姓名",
                isName: "请正确填写姓名"
            },
            user_useremail: "请输入一个正确的邮箱",
            user_userphone: {
                required: "请输入手机号码",
                isMobile: "请正确填写您的手机号码"
            },
            user_baphone: {
                required: "请输入手机号码",
                isphone: "请正确填写办公号码"
            }
        }
    })
}

$('#create_user').click(function () {
    userVlidate();
    var flag = $("#addUsermanageForm").valid();
//    var that = $(this);
    if (!flag) {
        //没有通过验证
        return;
    }
    var groupId = $("#addUsermanageForm .groupName").attr("data-id");
    var username = $('#add_username').val();//用户名
    var password = $('#add_password').val();//密码
    var name = $('#add_name').val();//姓名
    var email = $('#add_email').val();//邮箱
    var phone = $('#add_userphone').val();//手机号码
//	var telephone =$('#user_baphone').val();//办公电话
    var roleId = $('#addUserRole').val();//办公电话

    var req = new Object();
    req.groupId = groupId;
//		req.companyId=companyId;
    req.userName = username;
    req.password = md5(password);
    req.name = name;
//		req.duty=duty;
    req.phone = phone;
    req.email = email;
//		req.telephone=telephone;
    req.roleId = roleId;
    jQuery.axsGet(true, basePath + "/user/add", {json: JSON.stringify(req)}, function (data) {
        if (data.code == 10002) {
            layer.msg(data.msg);
            $('#userlistModal').modal("hide");
            window.location.href = basePath + '/system/index';
        } else {
            layer.msg(data.msg);
            $('#userlistModal').modal("hide");
        }
    }, function (xhr) {
        layer.msg("服务器错误！")
    })

});


/************重置密码操作**************/
$(document).on('click', ".reset_pw", function () {
    var userId = $(this).parents('tr').attr('value');
    //remindMessage('是否重置密码？默认密码为：123456',resetPW,userId);
    layer.confirm('是否重置密码？默认密码为：123456', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        //确定删除执行的ajax
        jQuery.axsGet(true, basePath + "/user/default/pwd/reset", {userId: userId}, function (data) {
            if (data.code == 200) {
                layer.msg(data.msg);
                window.location.href = basePath + '/system/index';
            } else {
                layer.msg(data.msg);
            }
        }, function (xhr) {
            layer.msg("服务器错误！")
        })
    }, function () {

    });
});
// 左侧导航栏-用户管理
$(".usermanage").on("click", function () {
    $(this).css("background", "#424a51");
    //$(this).find("span.usermanage_text").css("color","#0068b5");
    //$(this).find("img.userimg").attr("src",basePath + "/res/imgs/systemlabel_active.png");
    $(".user_right_wrap").css("display", "block");
    $(".role_right_wrap").css("display", "none");
    $(".left_gr").not($(this)).css("background", "#525c65");
    //$(".com_span").not($(this).find("span.usermanage_text")).css("color","#000");
    //$(".com_img").not($(this).find("img.userimg")).attr("src",basePath + "/res/imgs/nav_labelsystem.png");
    $(".label_nav>a:last").text("用户管理");
    $(".label_nav a:last-child").addClass("sys_active");
});
var staticRole = new Object();
staticRole.basicTag = [];
staticRole.mergeTag = [];
staticRole.oldbasicTag = [];
staticRole.oldmergeTag = [];
staticRole.resources = [];
staticRole.oldresources = [];
$(".rolemanage").on("click", function () {
    $(this).css("background", "#424a51");
    //$(this).css("background","#d0e2fa");
    //$(this).find("span.rolemanage_text").css("color","#0068b5");
    //$(this).find("img.roleimg").attr("src",basePath + "/res/imgs/systemlabel_active.png");
    $(".user_right_wrap").css("display", "none");
    $(".role_right_wrap").css("display", "block");
    $(".left_gr").not($(this)).css("background", "#525c65");
    //$(".com_span").not($(this).find("span.rolemanage_text")).css("color","#000");
    //$(".com_img").not($(this).find("img.roleimg")).attr("src",basePath + "/res/imgs/nav_labelsystem.png");
    $(".label_nav>a:last").text("角色管理");

//    date_box(getZNodes);
    // 初始化当前角色数据
    if (!staticRole.zNodes || staticRole.zNodes.length == 0) {
        getZNodes();
    }
    if (!staticRole.resources || staticRole.resources.length == 0) {
        getResources();
    }
});
//点击角色数据权限
$(document).on('click', ".role_date", function () {
    var role_id = $(this).parents('tr').attr("value");
    var roleName = $(this).parents('tr').find('td').eq(1).text();
//    var remark=$(this).parents('tr').find('td').eq(2).text();
    $("#dateModal").find(".addrolemodel_input").val(roleName).attr('value', role_id);

    // 获取此角色对应的数据权限
    getRoleZNodes(role_id);
});
//tab切换
$(document).on("click", ".tab span", function () {
    var ind = $(this).index();
    $(this).addClass("spanbg").siblings("span").removeClass("spanbg");
    $(".dateBox .ztree").eq(ind).show().siblings(".ztree").hide();
})

function getZNodes() {
    jQuery.axsGet(true, basePath + '/role/data', {}, function (data) {
        if (data.code == 10005) {
            var baseNode = {id: -1, pId: 0, name: "全选", open: true};
            staticRole.basicTag[staticRole.basicTag.length] = baseNode;
            staticRole.basicTag = staticRole.basicTag.concat(data.data.basicTag);
            staticRole.oldbasicTag = staticRole.basicTag;

            staticRole.mergeTag[staticRole.mergeTag.length] = baseNode;
            staticRole.mergeTag = staticRole.mergeTag.concat(data.data.mergeTag);
            staticRole.oldmergeTag = staticRole.mergeTag;
        } else {
            layer.msg(data.msg);
        }
    }, function (xhr) {
        layer.msg("服务器错误！")
    });
}

function getRoleZNodes(roleId) {
    jQuery.axsGet(true, basePath + '/role/roleData', {roleId: roleId}, function (data) {
        var isDone = false;
        if (staticRole.oldbasicTag && staticRole.oldbasicTag.length > 0) {
            staticRole.basicTag = staticRole.oldbasicTag.concat();
        }
        if (staticRole.oldmergeTag && staticRole.oldmergeTag.length > 0) {
            staticRole.mergeTag = staticRole.oldmergeTag.concat();
        }
        if (data.code == 10005 && data.data && data.data.basicTag) {
            if (staticRole.basicTag && staticRole.basicTag.length > 0) {
                for (let i = 0, len = data.data.basicTag.length; i < len; i++) {
                    for (let j = 0, lenj = staticRole.basicTag.length; j < lenj; j++) {
                        if (data.data.basicTag[i].id === staticRole.basicTag[j].id &&
                            data.data.basicTag[i].name === staticRole.basicTag[j].name) {
                            staticRole.basicTag[j] = data.data.basicTag[i];
                        }
                    }
                }
                for (let i = 0, len = data.data.mergeTag.length; i < len; i++) {
                    for (let j = 0, lenj = staticRole.mergeTag.length; j < lenj; j++) {
                        if (data.data.mergeTag[i].id === staticRole.mergeTag[j].id &&
                            data.data.mergeTag[i].name === staticRole.mergeTag[j].name) {
                            staticRole.mergeTag[j] = data.data.mergeTag[i];
                        }
                    }
                }
                isDone = true;
            } else {
                isDone = true;
            }
        } else {
            isDone = true;
        }
        if (isDone) {
            renderZNodes(staticRole.zNodes);
        }
    }, function (xhr) {
        layer.msg("服务器错误！")
    });
}

function renderZNodes(nodes) {
    var setting = {
        check: {
            enable: true
        },
        view: {
            showIcon: false
        },
        data: {
            simpleData: {
                enable: true
            }
        }
    };

    $.fn.zTree.init($("#treeDemobas"), setting, staticRole.basicTag);
    $.fn.zTree.init($("#treeDemomer"), setting, staticRole.mergeTag);

}

$("#dateModalbtn").on("click", function () {
    var treeObj = $.fn.zTree.getZTreeObj("treeDemobas");
    var nodes = treeObj.getCheckedNodes(true);
    var treeObjmer = $.fn.zTree.getZTreeObj("treeDemomer");
    var nodesmer = treeObjmer.getCheckedNodes(true);
    var result = [];
    var resultmer = [];
    if (nodes.length == 0 && nodesmer.length == 0) {
        alert("请选择标签！！");
        return false;
    }
    if (nodes.length > 0 && nodes[0].id == -1) nodes.shift();
    if (nodesmer.length > 0 && nodesmer[0].id == -1) nodesmer.shift();
    for (var i = 0; i < nodes.length; i++) {
        var tagNode = new Object();
        tagNode.id = nodes[i].id;
        tagNode.type = nodes[i].type;
        result.push(tagNode);
    }
    for (var i = 0; i < nodesmer.length; i++) {
        var tagNodemer = new Object();
        tagNodemer.id = nodesmer[i].id;
        tagNodemer.type = nodesmer[i].type;
        resultmer.push(tagNodemer);
    }
    var resultAll = result.concat(resultmer);
    var roleId = $("#dateModal").find(".addrolemodel_input").attr('value');
    var json = {
        'dataTags': resultAll,
        'roleId': roleId
    };
    jQuery.ajaxPost(true, basePath + '/role/roleData/edit', {json: JSON.stringify(json)}, function (data) {
        layer.msg(data.msg);
    }, function (xhr) {
        layer.msg("服务器错误！")
    });
    $("#dateModal").modal("hide");
});


/*** 点击角色资源权限 ***/
$(document).on('click', ".reset_resource", function () {
    var role_id = $(this).parents('tr').attr("value");
    var roleName = $(this).parents('tr').find('td').eq(1).text();
//    var remark=$(this).parents('tr').find('td').eq(2).text();
    $("#resourceModal").find(".addrolemodel_input").val(roleName).attr("value", role_id);
    // 获取此角色对应的数据权限
    getRoleResources(role_id);

});

function getResources() {
    jQuery.axsGet(true, basePath + '/resource/data', {}, function (data) {
        if (data.code == 10005 && data.data && data.data.length > 0) {
            var baseNode = {
                id: -1,
                pId: 0,
                name: "全选",
                allPermIds: "view",
                permIds: "view",
                open: true,
                checked: false
            };
            staticRole.resources[staticRole.resources.length] = baseNode;
            staticRole.resources = staticRole.resources.concat(data.data);

            staticRole.oldresources = staticRole.resources;
        } else {
            layer.msg(data.msg);
        }
    }, function (xhr) {
        layer.msg("服务器错误！")
    });
}

function getRoleResources(roleId) {
    var resourceData = [];
    $("#treeDemo2").html("");
    jQuery.axsGet(true, basePath + '/resource/roleData', {"roleId": roleId}, function (data) {
        var isDone = false;
        if (staticRole.oldresources && staticRole.oldresources.length > 0) {
            staticRole.resources = staticRole.oldresources.concat();
        }
        if (data.code == 10005 && data.data && data.data.length > 0) {
            if (staticRole.resources && staticRole.resources.length > 0) {
                for (let i = 0, len = data.data.length; i < len; i++) {
                    for (let j = 0, lenj = staticRole.resources.length; j < lenj; j++) {
                        if (data.data[i].id === staticRole.resources[j].id &&
                            data.data[i].name === staticRole.resources[j].name) {
                            staticRole.resources[j] = data.data[i];
                        }
                    }
                }
                isDone = true;
            } else {
                isDone = true;
            }
        } else {
            isDone = true;
        }
        if (isDone) {
//			renderResources(staticRole.resources);
            resourceData = staticRole.resources;
            zTree = $.fn.zTree.init($("#treeDemo2"), setting, resourceData);
        }
        // 渲染 TODO
    }, function (xhr) {
        layer.msg("服务器错误！")
    });
}


//删除
$(document).on('click', ".dele_user", function () {
    var userid = $(this).parents('tr').attr('value');
    //remindMessage('是否删除该用户？',delUser,userid);
    layer.confirm('是否删除该用户？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        //确定删除执行的ajax
        jQuery.axsGet(true, basePath + '/user/delSysUser', {userId: userid}, function (data) {
            if (data.code == 10004) {
                layer.msg(data.msg);
                window.location.href = basePath + '/system/index';
            } else {
                layer.msg(data.msg);
            }
        }, function (xhr) {
            layer.msg("服务器错误！")
        })
    }, function () {

    });
});
//禁用
$(document).on('click', ".dis_use", function () {
    var userId = $(this).parents('tr').attr('value');
    var state = $(this).parents('tr').attr('state');
    layer.confirm('是否禁用该用户？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        //确定禁用执行的ajax
        jQuery.axsGet(true, basePath + "/user/forbid", {userId: userId, state: state}, function (data) {
            if (data.code == 10003) {
                layer.msg(data.msg);
                window.location.href = basePath + '/system/index';
            } else {
                layer.msg(data.msg);
            }
        }, function (xhr) {
            layer.msg("服务器错误!")
        })
    }, function () {

    });
});

/*********所属机构点击事件**********/
$(document).on('click', ".list_item_div .group", function () {
    $(".list_item_div .group").removeClass("active");
    $(this).addClass("active");
    $(this).parents(".list_item_div").css("background", "#fbfbfb");
    if ($(this).parents(".list_item_div").find("div").length > 1) {
        $(this).parent().css("background", "url(../../imgs/u62.png) no-repeat 0 10px !important");
    } else {
        $(this).parent().css("background", "none !important");
    }
    $("#addUsermanageForm .groupName").text($(this).text());
    $("#addUsermanageForm .groupName").attr("data-id", $(this).attr("value"));
    $('#groupModel .groupNames').text($(this).text());
    $('#groupModel .groupNames').attr("data-id", $(this).attr("value"));
    $('#editgroupModel .groupNames').val($(this).text());
    $('#editgroupModel .groupNames').attr("data-id", $(this).attr("value"));
    $(".admin_left_userlist s").remove();
    if ($(this).attr("data-sta") == undefined) {
        var str = '<s>'
            + '<span class="addGroupNext" title="添加" data-toggle="modal" data-target="#groupModel" style="width:12px;"></span>'
            + '<span class="aditGroupNext" title="编辑" data-toggle="modal" data-target="#editgroupModel" style="width:12px;"></span>'
            + '<span class="delGroupNext" title="删除" data-id="' + $(this).attr("value") + '" style="width:12px;"></span>'
            + '</s>';
        $(this).after(str);
    }
    $('.search_componyname_input').val('');
    $('.search_username_input').val('');
    $('#current_company').text($(this).attr('value'));
    var req = new Object();
    var page = new Object();
    page.curPage = 1;
    req.pageBean = page;
    req.groupId = $(this).attr('value');
    updateCharacterList(req);
});


/*************用户管理搜索*****************/
$('.Userl_radio6').click(user_search);
/*$('.search_componyname_input').blur(user_search);
$('.search_username_input').blur(user_search);*/

//用户管理的编辑操作第一步 得到表格的数据，第二步把数据填充到弹窗中
$(document).on('click', ".edit_user", function () {
    var req = new Object;
    var thobj = $(this).parents('tr').find("td");
    req.userid = $(this).parents('tr').attr('value');
    //公司
//   req.conpanyName=thobj.eq(1).text();
//   var conpanyId=thobj.eq(1).attr('value');
//   $("#edit_admincompony").text(req.conpanyName).attr('value',conpanyId);
//   $("#edit_admincompony").text(req.conpanyName);
    //职务
//   req.job=thobj.eq(2).text();
//   $("#edit_admin_post").val(req.job);
    //姓名
    req.name = thobj.eq(1).text();
    $("#edit_adminName").val(req.name);
    //用户名
    req.userName = thobj.eq(2).text();
    $("#edit_userName").val(req.userName).attr('uid', req.userid);
    //手机号
    req.phone = thobj.eq(3).text();
    $("#edit_admin_phoneNumber").val(req.phone);
    //邮箱
    req.email = thobj.eq(4).text();
    $("#edit_admin_email").val(req.email);
    //角色
//   req.roleName = thobj.eq(5).text();
//   req.roleId = (typeof(thobj.eq(5).attr('value')) == 'undefined')?0:thobj.eq(5).attr('value');
//   $("#adminRole").text(req.roleName).attr('value',req.roleId);

//   jQuery.axsGet(true, basePath + '',{json:JSON.stringify(req)},function(data){
//   		
//	   if(data.code==200){
//	    	var target=$('#usermanageeditModal').find('ul').last();
//	    	var result=data.data;
//	    	for(var i=0;i<result.length;i++){
//	    	   var li1=$('<li></li>'); 
//	    	   var a1=$('<a class="base_dropdown-menu_ul_a"></a>');
//	    	   a1.attr('value',result[i].id).text(result[i].name);
//	          var li2=$('<li role="separator" style="line-height:0.418571;" class="divider"></li>');
//	          li1.append(a1);
//	          target.append(li1,li2);
//	    	}
//	    }
//   },function(xhr){layer.msg("服务器错误！")})

});
//点击编辑按钮的操作第一关闭弹窗(判断数据是否填写完整)，第二把修改的数据存入数据库，第三步重新渲染界面
$("#edit_user_btn").on("click", function () {

    var userId = $('#edit_userName').attr('uid').trim();
//	var companyId =$('#edit_admincompany').val().trim();
    var username = $('#edit_userName').val().trim();
//	var password =$('#edit_adminPwd').val().trim();
    var name = $('#edit_adminName').val().trim();
//	var duty =$('#edit_admin_post').val().trim();
    var phone = $('#edit_admin_phoneNumber').val().trim();
    var email = $('#edit_admin_email').val().trim();
//	var telephone =$('#edit_admin_officephone').val().trim();
    var roleId = $('#adminRole').val().trim();

    if (!(username && name && roleId && userId)) {
        layer.msg('请填写必要信息');
    } else {
        var req = new Object();
//		req.companyId=companyId;
        req.userName = username;
//		req.newpassword=md5(password);
        req.name = name;
//		req.duty=duty;
        req.phone = phone;
        req.email = email;
//		req.telephone=telephone;
        req.roleId = roleId;
        req.userId = userId;
        var url = basePath + "/user/edit";
        jQuery.axsGet(true, url, {json: JSON.stringify(req)}, function (data) {
                if (data.code == 10003) {
                    layer.msg(data.msg);
                    window.location.href = basePath + '/system/index';
                } else {
                    layer.msg(data.msg);
                }
            }, function (xhr) {
                layer.msg("服务器错误！");
            }
        );

    }
});
// 点击角色编辑
$(document).on('click', ".role_edit", function () {
    var role_id = $(this).parents('tr').attr("value");
    var roleName = $(this).parents('tr').find('td').eq(1).text();
    var remark = $(this).parents('tr').find('td').eq(2).text();
    $("#editModal").find(".roleDes").val(remark);
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
                layer.msg(obj.msg);
            } else {
                layer.msg(obj.msg);
            }
        },
        error: function (data) {
            layer.msg("服务器错误");
        }
    });
});

// 编辑角色弹框
$("#excutehavair").on('click', function () {
    var roleName = $("#editModal").find(".editrolemodel_input").val();
    var roleid = $("#editModal").find(".editrolemodel_input").attr('roleid');
    var remark = $("#editModal").find(".roleDes").val();
    if (roleName == '') {
        layer.msg('请填写角色名称');
    } else {
        var req = new Object();
        req.name = roleName;
        req.id = roleid;
        req.remark = remark;
        jQuery.axsGet(true, basePath + '/role/edit', {json: JSON.stringify(req)}, function (data) {
            if (data.code == 10003) {
                layer.msg(data.msg);
                $('#editModal').modal("hide");
                ;
                var req = new Object();
                var page = new Object();
                page.curPage = 1;
                req.pageBean = page;
                updateRoleList(req);
            } else {
                layer.msg(data.msg);
                $('#editModal').modal("hide");
                ;
            }
        }, function (xhr) {
            layer.msg("服务器错误!")
        })

    }
});

//角色管理的删除
$(document).on('click', ".role_dele", function () {
    var role_id = $(this).parents('tr').attr("value");
    layer.confirm('是否删除该角色？', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        //确定删除执行的ajax
        jQuery.axsGet(true, basePath + '/role/del', {roleId: role_id}, function (data) {
            if (data.code == 10004) {
                layer.msg(data.msg);
                var req = new Object();
                var page = new Object();
                page.curPage = 1;
                req.pageBean = page;
                updateRoleList(req);
            } else {
                layer.msg(data.msg);
            }
        }, function (xhr) {
            layer.msg("服务器错误!")
        })
    }, function () {

    });
});
// 点击角色添加 清除输入框内容
$('.rolemanage_add_text').click(function () {
    $('#oneModal input,textarea').val('');
});
// 保存角色
$('#createRoleType').click(function () {
    var roleName = $('#oneModal').find(".addrolemodel_input").val().trim();
    var remark = $('#oneModal').find(".roleDes").val().trim();
    // flag TODO 角色标识
    if (roleName == '') {
        layer.msg('请填写角色名称');
    } else {
        var req = new Object();
        req.name = roleName;
        req.remark = remark;
        jQuery.axsGet(true, basePath + '/role/add', {json: JSON.stringify(req)}, function (data) {
            if (data.code == 10002) {
                layer.msg(data.msg);
                $('#oneModal').modal("hide");
                ;
                var req = new Object();
                var page = new Object();
                page.curPage = 1;
                req.pageBean = page;
                updateRoleList(req);
            } else {
                layer.msg(data.msg);
                $('#oneModal').modal("hide");
                ;
            }
        }, function (xhr) {
            layer.msg("服务器错误！")
        })

    }
});
/***********角色管理查询**************/
$('.rolemanage_span').click(function () {
    var req = new Object();
    var page = new Object();
    page.curPage = 1;
    req.pageBean = page;
    if ($('.rolemanage_input').val().trim() != '') {
        req.name = $('.rolemanage_input').val().trim();
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
//
$(".sortImg").on("click", function () {
    if ($(this).parents(".parentsName").next('ul').css("display") == 'block') {
        $(this).attr("src", basePath + "/res/imgs/u61.png");
        $(this).parents(".parentsName").next('ul').hide();
    } else if ($(this).parents(".parentsName").next('ul').css("display") == 'none') {
        $(this).attr("src", basePath + "/res/imgs/u62.png");
        $(this).parents(".parentsName").next('ul').show();
    }
})

//点击右侧按钮
$(".resourceRight").on("click", "input", function () {
    jQuery.ajaxPost(true, basePath + '/resource/roleData/edit', {json: JSON.stringify(choicebtn())}, function (data) {

    }, function (xhr) {
        layer.msg("服务器错误！")
    });
})
//资源权限保存
$("#resourceModalbtn").on("click", function () {
    jQuery.ajaxPost(true, basePath + '/resource/roleData/edit', {json: JSON.stringify(choicebtn())}, function (data) {
//    	if(data.code == 10003){
//    		$("#dateModalbtn").modal("hide");
//    	}
        layer.msg(data.msg);
    }, function (xhr) {
        layer.msg("服务器错误！")
    });
    $("#resourceModal").modal("hide");
})

function choicebtn() {
    var treeObj = $.fn.zTree.getZTreeObj("treeDemo2");
    var nodes = treeObj.getCheckedNodes(true);
    var result = [];

    if (nodes.length == 0) {
        layer.msg("数据权限！！");
        return false;
    }
    for (var i = 0; i < nodes.length; i++) {
        if (nodes[i].id != -1) {
            result.push(nodes[i]);
        }
    }

    //
    var parentArr = [];
    for (var m = 0; m < $('.qx_div').find("a").length; m++) {
        if ($('.qx_div').find("a").eq(m).attr("data-Perm") != undefined) {
            var obj = {
                "id": $('.qx_div').find("a").eq(m).attr("data-id"),
                "permIds": $('.qx_div').find("a").eq(m).attr("data-Perm")
            };
            parentArr.push(obj)
        }
    }
    $.each(result, function (k, v) {
        $.each(parentArr, function (key, val) {
            if (v.id == parseInt(val.id)) {
                v.permIds = val.permIds;
            }
        })
    })

    var newdataArr = [];
    for (var i = 0; i < result.length; i++) {
        var dataChecked = {};
        dataChecked.id = result[i].id;
        dataChecked.permIds = result[i].permIds;
        newdataArr.push(dataChecked);
    }
    var roleId = $("#resourceModal").find(".addrolemodel_input").attr('value');
    var json = {
        'dataReses': newdataArr,
        'roleId': roleId
    };
    return json;
}

//资源权限
var setting = {
    check: {
        enable: true
    },
    view: {
        showIcon: false
    },
    data: {
        simpleData: {
            enable: true
        }
    },
    callback: {
        onClick: function (event, treeId, treeNode, clickFlag) {
            $(".resourceRight").html("");
            var arr = treeNode.allPermIds.split(";");
            arr.pop();
            var chioceArr = treeNode.permIds.split(",");
            chioceArr.pop();
            for (var j = 0; j < arr.length; j++) {
                if (arr[j] == "add") {
                    arr[j] = "新增"
                } else if (arr[j] == "del") {
                    arr[j] = "删除"
                } else if (arr[j] == "edit") {
                    arr[j] = "修改"
                }
            }
            for (var j = 0; j < chioceArr.length; j++) {
                if (chioceArr[j] == "add") {
                    chioceArr[j] = "新增"
                } else if (chioceArr[j] == "del") {
                    chioceArr[j] = "删除"
                } else if (chioceArr[j] == "edit") {
                    chioceArr[j] = "修改"
                }
            }
            var str = '<ul>';
            for (var i = 0; i < arr.length; i++) {
                if ($.inArray(arr[i], chioceArr) >= 0) {
                    str += '<li data-Perm="' + arr[i] + '" data-id="' + treeNode.id + '"><input type="checkbox" checked="ckecked">' + arr[i] + '</li>';
                } else {
                    str += '<li data-Perm="' + arr[i] + '" data-id="' + treeNode.id + '"><input type="checkbox">' + arr[i] + '</li>';
                }
            }
            str += '</ul>';
            $(".resourceRight").html(str);

            $(".resourceRight li input").on("click", function () {
                $(".curSelectedNode").attr("data-id", $(this).parent("li").attr("data-id"));
                var dd = [];
                for (var n = 0; n < $(this).parents(".resourceRight").find("li").length; n++) {
                    if ($(this).parents(".resourceRight").find("li").eq(n).find("input").prop("checked")) {
                        var Perm = $(this).parents(".resourceRight").find("li").eq(n).attr("data-Perm");
                        if (Perm == "新增") {
                            dd.push("add");
                        } else if (Perm == "修改") {
                            dd.push("edit");
                        } else if (Perm == "删除") {
                            dd.push("del");
                        }

                    }
                }
                dd.push("view")
                $(".curSelectedNode").attr("data-Perm", dd);

                function distinct(ar) {
                    var ret = [];

                    for (var i = 0, j = ar.length; i < j; i++) {
                        if (ret.indexOf(ar[i]) === -1) {
                            ret.push(ar[i]);
                        }
                    }

                    return ret;
                }
            });
        }
    }
}, zTree;
//添加机构
$('#createOrganization').click(function () {
    var name = $('#company_name').val();
    if (name.trim() == '') {
        layer.msg('请填写完整信息');
    } else {
        var req = new Object();
        req.name = name;
        jQuery.axsGet(true, basePath + "/org/add", {json: JSON.stringify(req)}, function (data) {
            if (data.code == 10002) {
                layer.msg(data.msg);
                location.reload();
            } else {
                layer.msg(data.msg);
                $("#institutionModal").modal("hide");
            }
        }, function (xhr) {
            layer.msg("服务器错误！")
        })

    }
});
//添加下级组织
$("#createNextgroup").on('click', function (e) {
    var id = $('#groupModel .groupNames').attr("data-id");
    var name = $('#groupModel #nextGroupName').val();
    if (name.trim() == '') {
        layer.msg('请填写完整信息');
    } else {
        var req = new Object();
        req.pid = id;
        req.name = name;
        jQuery.axsGet(true, basePath + "/org/add", {json: JSON.stringify(req)}, function (data) {
            if (data.code == 10002) {
                layer.msg(data.msg);
                location.reload();
                /* window.location.href=basePath+'/systemSet'; */
            } else {
                layer.msg(data.msg);
                $('#groupModel').modal("hide");
                ;
            }
        }, function (xhr) {
            layer.msg("服务器错误！");
        });
    }
});
/*修改组织名称*/
$("#editgroupName").on("click", function () {
    var id = $('#editgroupModel .groupNames').attr("data-id");
    var name = $('#editgroupModel .groupNames').val();
    if (name.trim() == '') {
        layer.msg('请填写完整信息');
    } else {
        var req = new Object();
        req.id = id;
        req.name = name;
        jQuery.axsGet(true, basePath + "/org/edit", {json: JSON.stringify(req)}, function (data) {
            if (data.code == 10003) {
                location.reload();
            } else {
                layer.msg(data.msg);
                $('#editgroupModel').modal("hide");
            }
        }, function (xhr) {
            layer.msg("服务器错误！");
        });
    }
});
/*删除组织*/
$(".list_item_div").on("click", ".delGroupNext", function () {
    var id = $(this).attr("data-id");
    layer.confirm('确定删除当前组织?', {
        btn: ['确定', '取消'] //按钮
    }, function () {
        var req = new Object();
        req.orgId = id;
        jQuery.axsGet(true, basePath + "/org/del", req, function (data) {
            if (data.code == 10001) {
                layer.msg(data.msg);
                location.reload();
            } else {
                layer.msg(data.msg);
            }
        }, function (xhr) {
            layer.msg("服务器错误！");
        });
    });
});
$(".companyName").on("click", function () {
    if ($(this).next().css("display") == "none") {
        $(this).find(".group").attr("style", "background:url(../res/imgs/u62.png) no-repeat 0 10px !important;padding-left: 17px;");
        $(this).next().slideDown();
    } else {
        $(this).next().slideUp();
        $(this).find(".group").attr("style", "background:url(../res/imgs/u61.png) no-repeat 0 10px !important;padding-left: 17px;");
    }
});
$(".childItem").on("click", function () {
    if ($(this).next().css("display") == "none" && $(this).find(".group").attr("data-flag") == "0") {
        $(this).find(".group").attr("style", "background:url(../res/imgs/u62.png) no-repeat 0 10px !important;padding-left: 17px;");
        $(this).next().slideDown();
    } else if ($(this).next().css("display") == "block" && $(this).find(".group").attr("data-flag") == "0") {
        $(this).next().slideUp();
        $(this).find(".group").attr("style", "background:url(../res/imgs/u61.png) no-repeat 0 10px !important;padding-left: 17px;");
    }
});	
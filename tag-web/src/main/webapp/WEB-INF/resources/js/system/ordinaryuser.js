//重置密码
$(".resetPWD").click(function () {
    layer.confirm('', {
        content: '<p style="height:40px;"><span class="tip">*</span>原密码：<input type="password" placeholder="最多可输入6-18个字符" class="oldPwd"/></p><p style="height:40px;"><span class="tip">*</span>新密码：<input type="password" placeholder="最多可输入6-18个字符" class="resetPwd"/></p>',
        btn: ['确定', '取消'] //按钮
    }, function () {
        var userId = $("#userId").val();
        var pwd = $(".resetPwd").val();
        var oldpwd = $(".oldPwd").val();
        if (pwd == '' || oldpwd == '') {
            layer.msg("密码不能为空！", {icon: 2});
        } else if (pwd == oldpwd) {
            layer.msg("密码相同，不用修改", {icon: 2});
        } else {
            var pwds = md5(pwd);
            var oldpwds = md5(oldpwd);
            jQuery.ajaxPost(true, basePath + '/user/pwd/reset', {
                'userId': userId,
                'pwd': pwds,
                'oldpwd': oldpwds
            }, function (data) {
                if (data.code == 10003) {
                    layer.msg("密码重置成功", {icon: 2})
                } else {
                    layer.msg(data.msg, {icon: 2})
                }
            }, function (xhr) {
                layer.msg("服务器错误", {icon: 2})
            });
        }
    });

})

function userVlidate() {
    //用户名
    $.validator.addMethod("isUser", function (value, element) {
        var length = value.length;
        var user = /^[A-Za-z0-9\u4e00-\u9fa5]{1,20}$/;
        return this.optional(element) || user.test(value);
    }, "请正确填写用户名");
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
    $(".usermessage").validate({
        rules: {
            addlabel_name_input: {
                required: true,
                isUser: true
            },
            addlabel_user_input: {
                required: true,
                isName: true
            },
            addlabel_email_input: {
                email: true
            },
            addlabel_phone_input: {
                required: true,
                isMobile: true
            },
            addlabel_office_input: {
                required: true,
                isphone: true
            }
        },
        messages: {
            addlabel_name_input: {
                required: "请输入用户名",
                isUser: "请正确填写用户名"
            },
            addlabel_user_input: {
                required: "请输入姓名",
                isName: "请正确填写姓名"
            },
            addlabel_email_input: "请输入一个正确的邮箱",
            addlabel_phone_input: {
                required: "请输入手机号码",
                isMobile: "请正确填写您的手机号码"
            },
            addlabel_office_input: {
                required: "请输入手机号码",
                isphone: "请正确填写办公号码"
            }
        }
    })
}

//保存
$(".save").click(function () {
    userVlidate();
    var flag = $(".usermessage").valid();
    var that = $(this);
    if (!flag) {
        //没有通过验证
        return;
    }
    var userId = $(".addlabel_id_input").val(),
        username = $(".addlabel_name_input").val(),
        name = $(".addlabel_user_input").val(),
        email = $(".addlabel_email_input").val(),
        phone = $(".addlabel_phone_input").val(),
//    	office = $(".addlabel_office_input").val(),
        json = {
            "id": userId,
            "username": username,
            "name": name,
            "email": email,
            "phone": phone
        };
    jQuery.axsGet(true, basePath + '/user/easy/edit', {json: JSON.stringify(json)}, function (data) {
        if (data.code = 10003) {
            layer.msg(data.msg, {icon: 2});
        } else {
            layer.msg(data.msg, {icon: 1});
        }
    }, function (xhr) {
        layer.msg("服务器错误！")
    });
})


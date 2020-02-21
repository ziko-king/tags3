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

$('.cancel_btn').click(function () {
    resetPW($("#username").attr('user'));
});

$('.create_btn').click(function () {

    var userId = $("#username").attr('user');
    var username = $("#username").val().trim();
    var ordinaryname = $("#ordinaryname").val().trim();
    var oldpwd = $("#oldpwd").val().trim();
    var newpwd = $("#newpwd").val().trim();
    var job = $('#position').val().trim();
    var phonenumber = $('#phonenumber').val().trim();
    var officephone = $('#officephone').val().trim();

    if (!(username && ordinaryname)) {
        popMessage('请填写必要的信息', 'red');
        return false;
    } else if (oldpwd != '' || newpwd != '') {
        if (!oldpwd) {
            popMessage('请填写旧密码', 'red');
            return false;
        } else if (!newpwd) {
            popMessage('请填写新密码', 'red');
            return false;
        }
    }
    var req = new Object();
    req.username = username;
    if (!!(oldpwd && newpwd)) {
        req.newpassword = md5(newpwd);
        req.oldpassword = md5(oldpwd);
    }
    req.name = ordinaryname;
    req.duty = job;
    req.phone = phonenumber;
    req.telephone = officephone;
    req.userId = userId;
    console.log(JSON.stringify(req));
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
            } else {
                popMessage(data.msg, 'red');
            }

        },
        error: function (xhr) {
            popMessage("服务器错误", 'red');
            console.log('错误');
        }
    });

});
   


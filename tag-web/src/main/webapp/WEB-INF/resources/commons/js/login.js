$(document).ready(function () {
    var basePath = $('#basePath').val();
    //login
    $('#loginSubmit').click(function () {
        login_check();
    });

    function login_check() {
        var username = $('input[name="username"]').val();
        var password = $('input[name="password"]').val();
        var rememberMe = $('input[name="rememberMe"]').prop('checked');
        if (username == null || username == '' || password == null || password == '') {
            popMessage("用户名或密码不能为空！");
            return;
        }
        $('input[name="password"]').val(md5(password));

        if (rememberMe) {
            $('input[name="rememberMe"]').val("true");
        } else {
            $('input[name="rememberMe"]').val("false");
        }
        $('#loginForm').submit();
    }

    function popMessage(message) {
        $.confirm({
            title: '提示信息',
            closeIcon: true,
            boxWidth: '30%',
            useBootstrap: false,
            type: 'red',
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
});

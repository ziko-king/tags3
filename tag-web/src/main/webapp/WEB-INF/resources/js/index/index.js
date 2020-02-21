$(document).ready(function () {
    var basePath = '${basePath}';
    // message
    if (typeof (data) != 'undefined' && data && data.code != 10001) {
        layer.msg(data.msg);
    }
    $('body').bind('keyup', function (event) {
        if (event.keyCode == "13") {
            //回车执行查询
            login_check();
        }
    });
    // login
    $('#loginSubmit').click(function () {
        login_check();
    });

    function login_check() {
        var username = $('input[name="username"]').val();
        var password = $('input[name="password"]').val();
        var rememberMe = $('input[name="rememberMe"]').prop('checked');
        if (username == null || username == '' || password == null || password == '') {
            layer.msg("用户名或密码不能为空！");
            return;
        }
        $('input[name="password"]').val(md5(password));
        if (rememberMe) {
            $('input[name="rememberMe"]').val("true");
        } else {
            $('input[name="rememberMe"]').val("false");
        }
        $('#loginForm').submit();
//		var req=new Object();
//		req.username=username;
//		req.password=md5(password);
//		req.rememberMe=rememberMe;
//		$.ajax({  
//			type: 'POST',  
//			url: basePath + '/login',  
//			async:false,
//			contentType: 'application/json;charset=utf-8',
//			dataType: 'json',  
//			data: JSON.stringify(req),
//			success: function(data){
//				if(data.code==200){
//		    		window.location.href = basePath + '/home';
//		    	}else{
//		    		popMessage(data.msg);
//		    	}
//			},  
//			error: function(err){  
//				popMessage("服务器错误");
//			}  
//		});  
    }

});
/**
 * Created by jxx on 2018/6/03.
 */
$(function () {
    var hei = $(window).height() - 87;
    $(".centent_wrap").css({"height": hei + "px"});
    $(".centent_wrap_middle").css({"height": "142px"});
    $("#search_btn").on('click', function () {
        var search_input = $("#search_input").val();
        if (search_input == '') {
            layer.msg('请填写或者选择完整的信息!');
        } else {
            if (search_input.match(/^1[34578]\d{9}$/) || search_input.match(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) || search_input.match(/^[1-9]\d{4,8}$/) || search_input.match(/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/)) {
                sessionStorage.setItem("key", search_input);
                window.location.href = "../micro/query";
                /*var form = $("<form style='display:none;' class='formbox' method = 'post' action = '../micro/query'></form>");
                var hdnPath = $('<input type="hidden" value="'+search_input+'" name="num" id="num"/>');
                form.append(hdnPath);
                $("body").append(form);
                form.submit();
                $(".formbox").remove();*/
            } else {
                layer.msg('请输入正确的数据!');
            }
        }
    });
});
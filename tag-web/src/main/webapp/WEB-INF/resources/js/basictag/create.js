//根据用户角色判断增删改是否有权限
var oneArr = [],
    twoArr = [],
    threeArr = [],
    fourArr = [],
    fiveArr = [];
if ($(".curRole").val() == "supper_admin") {
    var oneArr = ['add', 'del', 'edit'],
        twoArr = ['add', 'del', 'edit'],
        threeArr = ['add', 'del', 'edit'],
        fourArr = ['add', 'del', 'edit'],
        fiveArr = ['add', 'del', 'edit'];
    for (var i = 0; i < fourArr.length; i++) {
        if (fourArr[i] == "add") {
            $("#four").show();
        }
    }
    if ($.inArray("add", oneArr) >= 0 && $.inArray("add", twoArr) >= 0 && $.inArray("add", threeArr) >= 0) {
        $(".add_tab").show();
    }
} else {
    for (var i = 0; i < $(".levelLabels").length; i++) {
        if ($(".levelLabels").eq(i).attr("id") == "res1") {
            oneArr = $(".levelLabels").eq(i).attr("value").split(",");
        } else if ($(".levelLabels").eq(i).attr("id") == "res2") {
            twoArr = $(".levelLabels").eq(i).attr("value").split(",");
        } else if ($(".levelLabels").eq(i).attr("id") == "res3") {
            threeArr = $(".levelLabels").eq(i).attr("value").split(",");
        } else if ($(".levelLabels").eq(i).attr("id") == "res4") {
            fourArr = $(".levelLabels").eq(i).attr("value").split(",");
        } else if ($(".levelLabels").eq(i).attr("id") == "res5") {
            fiveArr = $(".levelLabels").eq(i).attr("value").split(",");
        }
    }
    for (var i = 0; i < fourArr.length; i++) {
        if (fourArr[i] == "add") {
            $("#four").show();
            $("#five").hide();
        }
    }

    if ($.inArray("add", oneArr) >= 0 && $.inArray("add", twoArr) >= 0 && $.inArray("add", threeArr) >= 0) {
        $(".add_tab").show();
    }
}
;
/*$("#createbasetype").click(function(){
    var levelThreeName = $("#levelThreeName").val();
    var meanings = $("#threeMeanings").val();
    var pId = $(".label_cont_buttom_conl_click").attr("value");
    var rules = $("#threeLabelRules").val();
    var data = {"name":levelThreeName,"level":3,"sign":1,"meanings":meanings,"pId":parseInt(pId),"tagRules":rules};
    if(levelThreeName == ""){
        layer.msg("标签名称不能为空!");
        $(".layui-layer-move").remove();
    }else if(meanings == ""){
        layer.msg("业务含义不能为空");
        $(".layui-layer-move").remove();
    }else if(rules == ""){
        layer.msg("标签规则不能为空");
        $(".layui-layer-move").remove();
    }else{
        $.ajax({
              type: "POST",
              url: basePath+"/addTag",
              dataType:"json",
              data: JSON.stringify(data,null,2),
              contentType:"application/json",
              success: function(data){
                   if(data.code == 200){
                       layer.msg("添加成功");
                       $("#baseModal").modal("hide");
                       location.reload();
                   }else{
                       layer.msg(data.msg);
                       $(".layui-layer-move").remove();
                   }

              },
                error: function(){
                  layer.msg("数据服务错误！");
                  $(".layui-layer-move").remove();
                }
           });
    }
})*/






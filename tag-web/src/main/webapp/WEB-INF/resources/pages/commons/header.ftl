<nav class="navbar navbar-default navbar-fixed-top g_row">
    <div class="container">
        <div class="navbar-header" style="width:214px;background-color:#363d42;position: relative;z-index:1;">
            <a class="navbar-brand" href="javascript:void(0);">
                <img src="${basePath}/res/imgs/logo.png" style="height:34px;margin-top:-7px;"/>
            </a>
        </div>
        <div id="navbar" class="collapse navbar-collapse" style="background-color:#363d42;">
            <ul class="nav navbar-nav nav_b_text" style="float:right!important;">
                <#list resources as list>
                    <#if list.type='-1'>
                        <li permids="${list.permIds}" ${(navigation?? && navigation=list.sign)?string("class=active","")}>
                            <a href="${basePath}${list.url!''}" res_id="${list.id}">${list.name}</a>
                        </li>
                    <#else>
                        <input id="${'res'+list.type}" type="hidden" value="${list.permIds}" class="levelLabels"/>
                    </#if>
                </#list>
                <div class="userInfo">
                    <s></s>
                    <span><@shiro.user><@shiro.principal property="name"/></@shiro.user></span>
                </div>
                <div class="outlogin">
                    <a class="outlogin_a" href="${basePath}/logout">
                        <span class="outlogin_exit">退出</span>
                    </a>
                </div>
            </ul>
        </div>
    </div>
</nav>
<@shiro.hasRole name="supper_admin">
    <input class="curRole" value="supper_admin" type="hidden">
</@shiro.hasRole>
<@shiro.hasRole name="admin">
    <input class="curRole" value="admin" type="hidden">
</@shiro.hasRole>
<@shiro.hasRole name="user">
    <input class="curRole" value="user" type="hidden">
</@shiro.hasRole>

<script type="text/javascript">
    var basePath = '${basePath}';
    var curRole = $(".curRole").val();
    $(".nav").on("click", "li", function () {
        $(".nav li").removeClass("active");
        $(this).addClass("active");
        /* $(this).find("a").attr("href",$(this).find("a").attr("href") + "?resId=" + $(this).find("a").attr("res_id")); */
    });
</script>

<style>
    .active2 {
        background: url('${basePath}/res/imgs/u62.png') no-repeat 0 4px;
    }

    .userInfo {
        height: 50px;
        float: left;
        margin: 0 0 0 24px;
        color: #fff;
    }

    .userInfo s {
        display: inline-block;
        width: 30px;
        height: 30px;
        background: url('${basePath}/res/imgs/userImg.png') no-repeat;
        background-size: 100% 100%;
        margin-top: 8px;
        float: left;
    }

    .userInfo span {
        display: inline-block;
        height: 50px;
        line-height: 50px;
        float: left;
        text-align: left;
        margin-left: 6px;
    }
</style>
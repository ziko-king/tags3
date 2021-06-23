package cn.itcast.tag.web.user.service;

import com.jagregory.shiro.freemarker.ShiroTags;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;

/**
 * 进行相应的扩展
 *
 * @author zhaocs
 */
public class FreeMarkerConfigExtend extends FreeMarkerConfigurer {
    @Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        super.afterPropertiesSet();
        Configuration cfg = this.getConfiguration();
        cfg.setSharedVariable("shiro", new ShiroTags());//shiro标签
        cfg.setNumberFormat("#");//防止页面输出数字,变成2,000
        //可以添加很多自己的要传输到页面的[方法、对象、值]

        /*
         * 在controller层使用注解再加一层判断
         * 注解 @RequiresPermissions("/delete")
         */

        /*shiro标签*/
        /**
         1.游客
         <@shiro.guest>
         您当前是游客，<a href="javascript:void(0);" class="dropdown-toggle qqlogin" >登录</a>
         </@shiro.guest>

         2.user（已经登录，或者记住我登录）
         <@shiro.user>
         欢迎[<@shiro.principal/>]登录，<a href="/logout.shtml">退出</a>
         </@shiro.user>

         3.authenticated（已经认证，排除记住我登录的）
         <@shiro.authenticated>
         用户[<@shiro.principal/>]已身份验证通过
         </@shiro.authenticated>

         4.notAuthenticated（和authenticated相反）
         <@shiro.notAuthenticated>
         当前身份未认证（包括记住我登录的）
         </@shiro.notAuthenticated>

         5.principal标签（能够取到你在realm中保存的信息比如我存的是ShiroUser对象，取出其中urlSet属性）
         <!--需要指定property-->
         <@shiro.principal property="urlSet"/>

         6.hasRole标签（判断是否拥有这个角色）
         <@shiro.hasRole name="admin">
         用户[<@shiro.principal/>]拥有角色admin<br/>
         </@shiro.hasRole>

         7.hasAnyRoles标签（判断是否拥有这些角色的其中一个）
         <@shiro.hasAnyRoles name="admin,user,member">
         用户[<@shiro.principal/>]拥有角色admin或user或member<br/>
         </@shiro.hasAnyRoles>

         8.lacksRole标签（判断是否不拥有这个角色）
         <@shiro.lacksRole name="admin">
         用户[<@shiro.principal/>]不拥有admin角色
         </@shiro.lacksRole>

         9.hasPermission标签（判断是否有拥有这个权限）
         <@shiro.hasPermission name="user:add">
         用户[<@shiro.principal/>]拥有user:add权限
         </@shiro.hasPermission>

         10.lacksPermission标签（判断是否没有这个权限）
         <@shiro.lacksPermission name="user:add">
         用户[<@shiro.principal/>]不拥有user:add权限
         </@shiro.lacksPermission>
         **/
    }
}

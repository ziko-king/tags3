package cn.itcast.tag.web.user.service;

import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.user.bean.UserRoleMapBean;
import cn.itcast.tag.web.utils.UserUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * 权限管理
 *
 * @author zhaocs
 * 2018-05-29
 * @version 1.0.0
 */
@Transactional
public class MyShiro extends AuthorizingRealm {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 权限认证
     * <p>
     * principalCollection
     *
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Principal principal = (Principal) getAvailablePrincipal(principalCollection);
        // 获取登录时输入的用户名
        String loginName = principal.username;
        // 到数据库查是否有此对象
        UserBean user = userService.queryForUsername(new UserBean(loginName));

        // 账号不存在
        if (user == null || user.getState() == 0) {
            return null;
        }

        //权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //用户的角色集合
        List<UserRoleMapBean> roleMaps = user.getRoleMaps();
        List<RoleBean> roleList = new ArrayList<>();
        Set<String> rolesName = new HashSet<>();
        for (UserRoleMapBean roleMap : roleMaps) {
            RoleBean role = roleService.queryForId(new RoleBean(roleMap.getRoleId()));
            if (role != null) {
                roleList.add(role);
                rolesName.add(UserUtil.RoleEnum.stateOf(role.getFlag()));
            }
        }

        info.setRoles(rolesName);
        // 用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的十行可以不要
//        for (RoleBean role : roleList) {
//            Collection<String> perssions = new ArrayList<String>();
//                List<RolePermissionRelation> permissionRelationList = role.getPermissionRelationList();
//                for(RolePermissionRelation permissionRelation:permissionRelationList){
//                    Permission p = permissionService.getById(permissionRelation.getPermissionId());
//                    //添加所有的权限标识
//                    perssions.addAll(getPerssions(p));
//                }
//            info.addStringPermissions(perssions);
//        }

        return info;
    }

    /**
     * 登录认证
     * <p>
     * authenticationToken
     *
     * @return AuthorizationInfo
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        // UsernamePasswordToken对象用来存放提交的登录信息
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 查出是否有此用户
        if (StringUtils.isEmpty(token.getUsername())) return null;
        UserBean user = userService.queryForUsername(new UserBean(token.getUsername()));
        if (null == user) return null;

        // 若存在，将此用户存放到登录认证info中
        try {
            return new SimpleAuthenticationInfo(new Principal(user), user.getPassword(), getName());
        } catch (AuthenticationException e) {
            logger.error("==== 用户认证失败@err:{} ====", e);
            // 验证不通过
            return null;
        }
    }

    /**
     * 授权用户信息
     */
    public static class Principal implements Serializable {

        private static final long serialVersionUID = 1L;

        private long id;                 // 编号
        private String username;         // 登录名
        private String name;             // 登录名
        private List<UserRoleMapBean> roleMaps;

        private Map<String, Object> cacheMap;

        public Principal(UserBean user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.name = user.getName();
            this.roleMaps = user.getRoleMaps();
        }

        public Principal() {
            super();
        }

        public Principal(long id, String username, String name, List<UserRoleMapBean> roleMaps) {
            super();
            this.id = id;
            this.username = username;
            this.name = name;
            this.roleMaps = roleMaps;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<UserRoleMapBean> getRoleMaps() {
            return roleMaps;
        }

        public void setRoleMaps(List<UserRoleMapBean> roleMaps) {
            this.roleMaps = roleMaps;
        }

        /**
         * 本函数输出将作为默认的<shiro:principal/>输出.
         */
        @Override
        public String toString() {
            return username;
        }

        public Map<String, Object> getCacheMap() {
            if (cacheMap == null) {
                cacheMap = new HashMap<>();
            }
            return cacheMap;
        }

    }
}

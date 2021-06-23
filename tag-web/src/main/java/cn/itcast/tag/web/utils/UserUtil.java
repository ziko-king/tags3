/**
 * 项目名称：mengyao
 * 创建日期：2018年5月28日
 * 修改历史：
 * 1、[2018年5月28日]创建文件 by zhaocs
 */
package cn.itcast.tag.web.utils;

/**
 * @author zhaocs
 *
 */
public class UserUtil {
    public static final String ENCRYPTING_KEY = "bjqt*20180101";  // 用户登录加密密匙
    public static final String DEFAULT_PWD = "73cb2b128e62ce63f6b4126bcc407db6";  // 用户登录加密密匙

    /**
     * 用户状态枚举类
     * 状态：1内部用户正常、2内部用户异常、3外部用户正常、4外部用户异常、0全部用户、-1已禁用
     * @author zhaocs
     *
     */
    public enum UserStateEnum {
        INNER_NORMAL(1, "内部正常用户"),
        INNER_EXCEPTION(2, "内部异常用户"),
        OUTER_NORMAL(3, "外部异常用户"),
        OUTER_EXCEPTION(4, "外部异常用户"),
        TOTAL(0, "全部用户"),
        DISABLE(-1, "已禁用用户");

        private int state;
        private String stateInfo;

        private UserStateEnum(int state, String stateInfo) {
            this.state = state;
            this.stateInfo = stateInfo;
        }

        public int getState() {
            return state;
        }

        public String getStateInfo() {
            return stateInfo;
        }
    }

    /**
     * shiro角色枚举类
     * @author zhaocs
     * 1超管、2普通管理员、3用户
     */
    public enum RoleEnum {
        SUPPER_ADMIN(1, "supper_admin"),
        ADMIN(2, "admin"),
        USER(3, "user");

        private int state;
        private String stateInfo;

        RoleEnum(int state, String stateInfo) {
            this.state = state;
            this.stateInfo = stateInfo;
        }

        public static String stateOf(int index) {
            for (RoleEnum state : values()) {
                if (state.getState() == index) {
                    return state.getStateInfo();
                }
            }
            return null;
        }

        public int getState() {
            return state;
        }

        public String getStateInfo() {
            return stateInfo;
        }
    }

    /**
     * 权限类别枚举类
     * @author zhaocs
     * 1增加、2删除、3修改、4查询、0全部、-1没有
     */
    public enum PermissionEnum {
        ADD(1, "add"),
        DEL(2, "del"),
        UPDATE(3, "update"),
        FIND(4, "find"),
        ALL(0, "all"),
        NONE(-1, "none");

        private int state;
        private String stateInfo;

        private PermissionEnum(int state, String stateInfo) {
            this.state = state;
            this.stateInfo = stateInfo;
        }

        public int getState() {
            return state;
        }

        public String getStateInfo() {
            return stateInfo;
        }
    }

    /**
     * 组织状态枚举类
     * @author zhaocs
     * 状态：-1禁用、1正常、2删除、3过期
     */
    public enum OrgStateEnum {
        DISABLED(-1, "禁用"),
        ENABLE(1, "正常"),
        DELETED(2, "删除"),
        EXPIRED(3, "过期");

        private int state;
        private String stateInfo;

        private OrgStateEnum(int state, String stateInfo) {
            this.state = state;
            this.stateInfo = stateInfo;
        }

        public int getState() {
            return state;
        }

        public String getStateInfo() {
            return stateInfo;
        }
    }
}

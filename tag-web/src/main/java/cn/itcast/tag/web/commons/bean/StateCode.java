package cn.itcast.tag.web.commons.bean;

public class StateCode {

    // 操作成功
    public static int SUCCESS = 10001;
    // 新增成功
    public static int ADD_SUCCESS = 10002;
    // 修改成功
    public static int UPD_SUCCESS = 10003;
    // 删除成功
    public static int DEL_SUCCESS = 10004;
    // 查询成功
    public static int QUERY_SUCCESS = 10005;
    // 暂无数据
    public static int QUERY_ZERO_SUCCESS = 10006;

    // 操作失败
    public static int FAILD = 11001;
    // 新增失败
    public static int ADD_FAILD = 11002;
    // 修改失败
    public static int UPD_FAILD = 11003;
    // 删除失败
    public static int DEL_FAILD = 11004;
    // 参数错误
    public static int PARAM_FAILD = 11005;
    // 参数为空
    public static int PARAM_NULL_FAILD = 11006;
    // 参数类型错误
    public static int PARAM_TYPE_FAILD = 11007;
    // 存在重复记录
    public static int RECORD_DUP_FAILD = 11008;
    // 子层级不为空，不能删除
    public static int RECORD_EXIST_FAILD = 11009;
    // 存在不可用记录
    public static int RECORD_DISABLED = 11010;
    // 一级标签存在重复记录
    public static int RECORD_DUP_LEVEL_ONE_FAILD = 11011;
    // 二级标签存在重复记录
    public static int RECORD_DUP_LEVEL_TWO_FAILD = 11012;
    // 三级标签存在重复记录
    public static int RECORD_DUP_LEVEL_THREE_FAILD = 11013;

    public enum State {

        SUCCESS(10001, "操作成功"),
        ADD_SUCCESS(10002, "新增成功"),
        UPD_SUCCESS(10003, "修改成功"),
        DEL_SUCCESS(10004, "删除成功"),

        FAILD(11001, "操作失败"),
        PARAM_FAILD(11002, "参数错误"),
        PARAM_NULL_FAILD(11003, "参数为空"),
        PARAM_TYPE_FAILD(11004, "参数类型错误");

        private String msg;
        private int code;

        private State(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public static String get(int code) {
            for (State state : State.values()) {
                if (state.getCode() == code) {
                    return state.msg;
                }
            }
            return null;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

    }

}

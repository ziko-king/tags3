package cn.itcast.tag.web.utils;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

/**
 * @author mengyao
 */
public class JsonUtil {

    /**
     * @param obj
     * @return
     */
    public static String obj2Json(Object obj) {
        return JSON.toJSONString(obj, true);
    }

    /**
     * @param list
     * @return
     */
    public static String list2Json(List<?> list) {
        return JSON.toJSONString(list, true);
    }

    /**
     * @param map
     * @return
     */
    public static String map2Json(Map<?, ?> map) {
        return JSON.toJSONString(map, true);
    }

    /**
     * @param jsonStr
     * @param caz
     * @return
     */
    public static Object json2Obj(String jsonStr, Class<?> caz) {
        if (null == jsonStr || jsonStr.isEmpty()) {
            return null;
        }
        return JSON.parseObject(jsonStr, caz);
    }

    /**
     * @param jsonStr
     * @param caz
     * @return
     */
    public static Object json2Array(String jsonStr, Class<?> caz) {
        if (null == jsonStr || jsonStr.isEmpty()) {
            return null;
        }
        return JSON.parseArray(jsonStr, caz);
    }

}

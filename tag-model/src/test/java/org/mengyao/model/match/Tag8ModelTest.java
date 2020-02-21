package org.mengyao.model.match;

import cn.itcast.model.models.match.Tag8Model;
import org.junit.Before;
import org.junit.Test;

/**
 * 用户性别标签模型测试用例
 * Created by mengyao
 * 2019年8月16日
 */
public class Tag8ModelTest {


    private Tag8Model tag8Model = null;

    @Before
    private void init() throws Exception {
        tag8Model = new Tag8Model();
        System.out.println("init...");
    }

    @Test
    protected void run() throws Throwable {
        System.out.println("running...");
        tag8Model.execute();
    }

}

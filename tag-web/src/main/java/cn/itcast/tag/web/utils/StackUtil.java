package cn.itcast.tag.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 栈操作工具类
 *
 * @author zhangwenguo
 */
public class StackUtil {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private int top = -1;
    private Object[] objs;

    // 栈大小
    public StackUtil(int capacity) throws Exception {
        if (capacity < 0)
            throw new Exception("Illegal capacity:" + capacity);
        objs = new Object[capacity];
    }

    public static void main(String[] args) throws Exception {
    	/*StackUtil s = new StackUtil(2);
        s.push(1);
        s.push(2);
        s.dispaly();
        System.out.println(s.pop());
        s.dispaly();
        s.push(99);
        s.dispaly();
        s.push(99); */
    }

    // 入栈
    public void push(Object obj) throws Exception {
        if (top == objs.length - 1)
            throw new Exception("Stack is full!");
        objs[++top] = obj;
    }

    // 出栈
    public Object pop() throws Exception {
        if (top == -1)
            throw new Exception("Stack is empty!");
        return objs[top--];
    }

    // 打印
    public void dispaly() {
        System.out.print("bottom -> top: | ");
        for (int i = 0; i <= top; i++) {
            logger.info("======== " + objs[i] + " | " + "======== ");
        }
    }

}

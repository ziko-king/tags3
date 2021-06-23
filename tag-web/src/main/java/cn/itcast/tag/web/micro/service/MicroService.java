package cn.itcast.tag.web.micro.service;

import cn.itcast.tag.web.micro.bean.MicroPortraitUserBean;

/**
 * 微观画像
 *
 * @author 83717
 */
public interface MicroService {
    /**
     * 根据手机号、QQ、邮箱查询用户相关标签
     *
     * @param num
     * @return
     */
    MicroPortraitUserBean queryByNum(String num);

}

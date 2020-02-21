package cn.itcast.tag.web.micro.service.impl;

import cn.itcast.tag.web.basictag.bean.BasicTagBean;
import cn.itcast.tag.web.basictag.service.BasicTagService;
import cn.itcast.tag.web.mergetag.bean.MergeTagBean;
import cn.itcast.tag.web.mergetag.service.MergeTagService;
import cn.itcast.tag.web.micro.bean.MicroPortraitTag;
import cn.itcast.tag.web.micro.bean.MicroPortraitUserBean;
import cn.itcast.tag.web.micro.service.MicroService;
import cn.itcast.tag.web.user.bean.RoleBean;
import cn.itcast.tag.web.user.bean.UserBean;
import cn.itcast.tag.web.user.bean.UserRoleMapBean;
import cn.itcast.tag.web.user.service.MyShiro.Principal;
import cn.itcast.tag.web.utils.AccountValidatorUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;

@Service
public class MicroServiceImpl implements MicroService {

    //HBase表名
    private final String TABLE_NAME = "socialSecurity";//socialSecurityHDFS
    //HBase用户信息列族
    private final String FAMILY_USERINFO = "userInfo";
    private Logger logger = LogManager.getLogger(getClass());
    //HBase标签列族
//	private final String FAMILY_TAG = "tag";
    @Resource
    private BasicTagService basicTagService;
    @Resource
    private MergeTagService mergeTagService;

    @Override
    public MicroPortraitUserBean queryByNum(String num) {
        MicroPortraitUserBean microPortraitUserBean = new MicroPortraitUserBean();
        String column = "";
        Principal curUser = (Principal) SecurityUtils.getSubject().getPrincipal();
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long roleId = roleMaps.get(0).getRoleId();
        if (AccountValidatorUtil.isMobile(num)) {
            column = "phoneNum";
        } else if (AccountValidatorUtil.isEmail(num)) {
            column = "email";
        } else if (AccountValidatorUtil.isQQ(num)) {
            column = "qq";
        } else if (AccountValidatorUtil.isIDCard(num)) {
            column = "id";
        }
        try {
            //TODO 改用假数据
            //HBaseUtil hBaseUtil = new HBaseUtil().build();
            //Collection<Map<String, String>> result = hBaseUtil.queryForQuilfierExactly(TABLE_NAME, FAMILY_USERINFO, column, num);
            Collection<Map<String, String>> result = new ArrayList<>();
            result.add(new HashMap<String, String>() {
                {
                    put("id", "110115199402265244");
                    put("email", "MR.Zhang@gmail.com");
                    put("phoneNum", "15231238866");
                    put("qq", "753446111");
                    put("type", "0");
                }
            });


            if (null != result && result.size() > 0) {
                Object[] mapArray = result.toArray();
                @SuppressWarnings("unchecked")
                Map<String, String> userMap = (Map<String, String>) mapArray[0];
                try {
                    microPortraitUserBean.setIdNum(userMap.get("id"));
                    microPortraitUserBean.setEmail(userMap.get("email"));
                    microPortraitUserBean.setPhoneNum(userMap.get("phoneNum"));
                    microPortraitUserBean.setQq(userMap.get("qq"));
                    microPortraitUserBean.setType(userMap.get("type"));
                    List<Long> basicTagIds = new ArrayList<>();
                    List<Long> mergeTagIds = new ArrayList<>();
                    //根据rowkey获取tag列族的信息，区分出basictag、mergetag
                    //TODO 改用假数据
                    //Map<String, String> tagMap = hBaseUtil.queryForRowKey(TABLE_NAME, userMap.get("rowKey"), FAMILY_USERINFO);
                    Map<String, String> tagMap = new HashMap<String, String>() {{
                        put("tb_50", "50");
                        put("tb_55", "55");
                        put("tb_62", "62");
                        put("tb_67", "67");
                        put("tb_78", "78");
                        put("tb_81", "81");
                        put("tb_88", "88");
                        put("tb_89", "89");
                        put("tb_96", "96");
                        put("tb_100", "100");
                        put("tb_113", "113");
                        put("tb_118", "118");
                        put("tb_122", "122");
                        put("tb_127", "127");
                        put("tb_133", "133");
                        put("tb_141", "141");
                        put("tb_143", "143");
                        put("tb_149", "149");
                        put("tb_151", "151");
                        put("tb_155", "155");
                        put("tb_158", "158");
                        put("tb_162", "162");
                        put("tb_164", "164");
                        put("tb_166", "166");
                        put("tb_170", "170");
                        put("tb_176", "176");
                        put("tb_180", "180");
                        put("tb_183", "183");
                        put("tb_190", "190");
                        put("tb_195", "195");
                        put("tb_198", "198");
                        put("tb_203", "203");
                        put("tb_210", "210");
                        put("tb_215", "215");
                        put("tb_220", "220");
                        put("tb_223", "223");
                        put("tb_230", "230");
                    }};
                    tagMap.forEach((k, v) -> {
                        if (k.contains("tb_")) {
                            basicTagIds.add(Long.parseLong(v));
                        } else if (k.contains("tm_")) {
                            mergeTagIds.add(Long.parseLong(v));
                        }
                    });

                    List<MicroPortraitTag> microPortraitTags = new ArrayList<>();
                    List<MicroPortraitTag> tagList = new ArrayList<>();
                    if (null != basicTagIds) {
                        for (Long tagId : basicTagIds) {
                            //获取基础标签信息
                            BasicTagBean paramBasicBean = new BasicTagBean();
                            paramBasicBean.setId(tagId);
                            List<BasicTagBean> basicTagBeans = basicTagService.queryBasicTagForId(paramBasicBean, getCurrentUserBean(), new RoleBean(roleId));
                            //需要一直拿到一级（嵌套到五级）
                            if (null != basicTagBeans && basicTagBeans.size() > 0) {
                                //五级
                                BasicTagBean basicTagBean = basicTagBeans.get(0);
                                MicroPortraitTag microPortraitTag = new MicroPortraitTag();
                                BeanUtils.copyProperties(basicTagBean, microPortraitTag);
                                microPortraitTag.setType(0);
                                //遍历拿到一级，并嵌套
                                MicroPortraitTag levelOneTag = null;
                                while (levelOneTag == null || levelOneTag.getLevel() != 1) {
                                    List<MicroPortraitTag> topLevelBeans = new ArrayList<>();
                                    topLevelBeans.add(microPortraitTag);
                                    BasicTagBean tmpBasic = getLevelOneTagBeanById(microPortraitTag.getPid());
                                    if (null == tmpBasic) {
                                        break;
                                    }
                                    levelOneTag = new MicroPortraitTag();
                                    BeanUtils.copyProperties(tmpBasic, levelOneTag);
                                    levelOneTag.setMicroSubTags(topLevelBeans);
                                    microPortraitTag = levelOneTag;
                                }
                                microPortraitTags.add(microPortraitTag);
                            }
                        }
                        //合并
                        tagList = combine(microPortraitTags);
                    }

                    if (null != mergeTagIds) {
                        for (Long tagId : mergeTagIds) {
                            //获取组合标签信息
                            MergeTagBean mergeTagBean = mergeTagService.queryMergeTagById(tagId);
                            if (null != mergeTagBean) {
                                MicroPortraitTag microPortraitTag = new MicroPortraitTag();
                                BeanUtils.copyProperties(mergeTagBean, microPortraitTag);
                                microPortraitTag.setName(mergeTagBean.getName());
                                microPortraitTag.setType(1);
                                tagList.add(microPortraitTag);
                            }
                        }
                    }
                    microPortraitUserBean.setTags(tagList);
                } catch (Exception e) {
                    logger.error("==== queryByNum@err:{} ====", e);
                }
            }
        } catch (Exception e) {
            logger.error("==== queryByNum@err:{} ====", e);
        }

        return microPortraitUserBean;
    }

    //根据value值获取到对应的所有的key值
    public List<Long> getKeyList(Map<Long, Long> ids, Long value) {
        List<Long> keyList = new ArrayList<>();
        for (Long getKey : ids.keySet()) {
            if (ids.get(getKey).equals(value)) {
                keyList.add(getKey);
            }
        }
        return keyList;
    }

    /**
     * 合并
     *
     * @param microPortraitTags
     * @return
     */
    public List<MicroPortraitTag> combine(List<MicroPortraitTag> tags) {

        Map<Long, MicroPortraitTag> resultMap = new HashMap<>();
        for (MicroPortraitTag microPortraitTag : tags) {
            Long id = microPortraitTag.getId();
            if (!resultMap.containsKey(id)) {
                resultMap.put(id, microPortraitTag);
            } else {
                MicroPortraitTag tmpTag = resultMap.get(id);
                List<MicroPortraitTag> subTags = new ArrayList<>();
                if (null == microPortraitTag.getMicroSubTags()) {
                    break;
                }
                subTags.addAll(microPortraitTag.getMicroSubTags());
                subTags.addAll(tmpTag.getMicroSubTags());
                List<MicroPortraitTag> tmpSubTags = combine(subTags);
                tmpTag.setMicroSubTags(tmpSubTags);
            }

        }
        List<MicroPortraitTag> result = new ArrayList<>();
        for (Entry<Long, MicroPortraitTag> entry : resultMap.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

    /**
     * 根据id获取标签
     */
    public BasicTagBean getLevelOneTagBeanById(Long pid) {
        Principal curUser = (Principal) SecurityUtils.getSubject().getPrincipal();
        List<UserRoleMapBean> roleMaps = curUser.getRoleMaps();
        Long roleId = roleMaps.get(0).getRoleId();
        BasicTagBean paramTag = new BasicTagBean();
        paramTag.setId(pid);
        List<BasicTagBean> resultTagBeans = basicTagService.queryBasicTagForId(paramTag, getCurrentUserBean(), new RoleBean(roleId));
        if (resultTagBeans.size() > 0) {
            BasicTagBean basicTagBean = resultTagBeans.get(0);
            return basicTagBean;
        }
        return null;
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public UserBean getCurrentUserBean() {
        UserBean userBean = new UserBean();
        Principal curUser = (Principal) SecurityUtils.getSubject().getPrincipal();
        userBean.setId(curUser.getId());
        return userBean;
    }
}

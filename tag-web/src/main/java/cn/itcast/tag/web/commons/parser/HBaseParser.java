package cn.itcast.tag.web.commons.parser;

import cn.itcast.tag.web.engine.bean.MetaDataBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author mengyao
 */
public class HBaseParser extends MetaParser {

    private final String ZK_HOSTS = "zkHosts";
    private final String ZK_PORT = "zkPort";
    private final String HBASE_TABLE = "hbaseTable";
    private final String FAMILY = "family";
    private final String SELECT_FIELD_NAMES = "selectFieldNames";
    private final String WHERE_FIELD_NAMES = "whereFieldNames";
    private final String WHERE_FIELD_VALUES = "whereFieldValues";
    private Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, String> ruleMap;

    HBaseParser(Map<String, String> ruleMap) {
        this.ruleMap = ruleMap;
    }

    @Override
    public MetaDataBean getMeta() {
        logger.info("==== Rule map is: {} ====", ruleMap);
        MetaDataBean meta = new MetaDataBean()
                .buildHBaseMeta(
                        ruleMap.get(ZK_HOSTS),
                        0,
                        ruleMap.get(HBASE_TABLE),
                        ruleMap.get(FAMILY),
                        ruleMap.get(SELECT_FIELD_NAMES),
                        ruleMap.get(WHERE_FIELD_NAMES),
                        ruleMap.get(WHERE_FIELD_VALUES)
                );
        meta.setInFields(ruleMap.get("inFields"));
        meta.setOutFields(ruleMap.get("outFields"));
        debugMeta(meta);
        return meta;
    }

    /**
     * @param meta
     */
    private void debugMeta(MetaDataBean meta) {
        logger.info("==== {} : {},{},{},{},{},{} ====",
                HBaseParser.class.getSimpleName(),
                meta.getZkHosts(),
                meta.getZkPort(),
                meta.getHbaseTable(),
                meta.getFamily(),
                meta.getSelectFieldNames(),
                meta.getWhereFieldNames(),
                meta.getWhereFieldValues()
        );
    }

}

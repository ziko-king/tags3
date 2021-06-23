package cn.itcast.tag.web.commons.parser;

import cn.itcast.tag.web.engine.bean.MetaDataBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author mengyao
 */
public class HDFSParser extends MetaParser {

    private final String IN_PATH = "inPath";
    private final String SPERATOR = "sperator";
    private final String IN_FIELDS = "inFields";
    private final String COND_FIELDS = "condFields";
    private final String OUT_FIELDS = "outFields";
    private final String OUT_PATH = "outPath";
    private Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, String> ruleMap;

    public HDFSParser(Map<String, String> ruleMap) {
        this.ruleMap = ruleMap;
    }

    @Override
    public MetaDataBean getMeta() {
        logger.info("==== Rule map is: {} ====", ruleMap);
        MetaDataBean meta = new MetaDataBean()
                .buildHDFSMeta(
                        ruleMap.get(IN_PATH),
                        ruleMap.get(SPERATOR),
                        ruleMap.get(IN_FIELDS),
                        ruleMap.get(COND_FIELDS),
                        ruleMap.get(OUT_FIELDS),
                        ruleMap.get(OUT_PATH));
        debugMeta(meta);
        return meta;
    }

    /**
     * @param meta
     */
    private void debugMeta(MetaDataBean meta) {
        logger.info("==== {} : {},{},{},{},{},{} ====",
                HDFSParser.class.getSimpleName(),
                meta.getInPath(),
                meta.getSperator(),
                meta.getInFields(),
                meta.getCondFields(),
                meta.getOutFields(),
                meta.getOutPath()
        );
    }

}

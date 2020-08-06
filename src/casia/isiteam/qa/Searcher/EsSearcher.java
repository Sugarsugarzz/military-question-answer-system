package casia.isiteam.qa.Searcher;

import casia.basic.analysisgrop.es.transport.EsClientUtil;
import casia.basic.analysisgrop.es.transport.EsSearchClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class EsSearcher {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    static EsSearchClient client;

    static {
        // ES初始化
        EsClientUtil.initEsConfig("isiteam-es-zh-nodes", "192.168.6.123", 9300);
    }

}

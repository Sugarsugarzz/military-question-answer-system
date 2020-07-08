package Test;

import QA.MilitaryQA;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QATest {

    private static Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    public static void main(String[] args) {

        MilitaryQA QA = new MilitaryQA();
        QA.qa_main("神舟七号的长度？");
    }
}

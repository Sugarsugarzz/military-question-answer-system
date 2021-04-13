package casia.isiteam.militaryqa;

import casia.isiteam.militaryqa.service.DictMapperService;
import casia.isiteam.militaryqa.service.PreprocessService;
import casia.isiteam.militaryqa.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MilitaryqaApplication {

    @Autowired
    PreprocessService preprocessService;
    @Autowired
    DictMapperService dictMapperService;

    public static void main(String[] args) {
        SpringApplication.run(MilitaryqaApplication.class, args);

        ApplicationContext context = SpringUtil.getApplicationContext();
        // 初始化分词词典
        PreprocessService preprocessService = context.getBean(PreprocessService.class);
        preprocessService.getDBToCustomDictionary();
        // 初始化DictMapper
        DictMapperService dictMapperService = context.getBean(DictMapperService.class);
        dictMapperService.initDictMapper();

    }
}

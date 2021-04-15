package casia.isiteam.militaryqa;

import casia.isiteam.militaryqa.service.PreprocessService;
import casia.isiteam.militaryqa.utils.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MilitaryqaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MilitaryqaApplication.class, args);

        // 初始化CustomDictionary和AliasMapper
        ApplicationContext context = SpringUtil.getApplicationContext();
        PreprocessService preprocessService = context.getBean(PreprocessService.class);
        preprocessService.initCustomDictionaryAndAliasMapper();
    }
}

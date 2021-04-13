package casia.isiteam.militaryqa.config;

import casia.isiteam.militaryqa.main.MultiMilitaryQA;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableScheduling
public class CleanerConfig {

//    @Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearQas() {
        MultiMilitaryQA.Qas.clear();
        MultiMilitaryQA.isUsingPronounMap.clear();
    }
}

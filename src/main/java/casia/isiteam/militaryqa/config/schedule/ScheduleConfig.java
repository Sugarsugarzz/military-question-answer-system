package casia.isiteam.militaryqa.config.schedule;

import casia.isiteam.militaryqa.common.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableScheduling
public class ScheduleConfig {

//    @Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearQas() {
        Constant.Qas.clear();
        Constant.isUsingPronounMap.clear();
    }
}

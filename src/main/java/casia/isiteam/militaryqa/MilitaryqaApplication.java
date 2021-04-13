package casia.isiteam.militaryqa;

import casia.isiteam.militaryqa.utils.DbFieldUpdater;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MilitaryqaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MilitaryqaApplication.class, args);
        DbFieldUpdater.getDBToCustomDictionary();  // init customdictionary
    }

}

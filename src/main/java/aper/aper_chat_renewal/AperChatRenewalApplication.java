package aper.aper_chat_renewal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"aper.aper_chat_renewal", "com.aperlibrary"})
@SpringBootApplication
public class AperChatRenewalApplication {

    public static void main(String[] args) {
        SpringApplication.run(AperChatRenewalApplication.class, args);
    }

}

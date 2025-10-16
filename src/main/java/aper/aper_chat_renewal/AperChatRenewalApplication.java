package aper.aper_chat_renewal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
        "aper.aper_chat_renewal.entity",
        "com.aperlibrary"
})
@EnableJpaRepositories(basePackages = "aper.aper_chat_renewal.repository")
@EnableJpaAuditing
public class AperChatRenewalApplication {

    public static void main(String[] args) {
        SpringApplication.run(AperChatRenewalApplication.class, args);
    }

}

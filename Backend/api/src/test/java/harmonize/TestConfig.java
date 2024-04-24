package harmonize;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import harmonize.Services.AdminTestService;
import harmonize.Services.ModeratorTestService;
import harmonize.Services.UserTestService;

@Configuration
public class TestConfig {

    @Bean
    public AdminTestService adminTestService() {
        return new AdminTestService("admin", "adminpw");
    }

    @Bean
    public ModeratorTestService modTestService() {
        return new ModeratorTestService("mod", "modpw");
    }

    @Bean
    public UserTestService todTestService() {
        return new UserTestService("twilson", "todpw");
    }

    @Bean
    public UserTestService bobTestService() {
        return new UserTestService("broberts", "bobpw");
    }
}

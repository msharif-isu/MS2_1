package harmonize;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import harmonize.DTOs.RegisterDTO;
import harmonize.ErrorHandling.Exceptions.UserNotFoundException;
import harmonize.Services.AdminService;
import harmonize.Services.AuthService;
import harmonize.Services.RoleService;

/**
 * Harmonize Spring Boot Application.
 * 
 * @author ms2-1
 */

@SpringBootApplication
@EnableJpaRepositories
public class Application {
	
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner initUser(AuthService authService, RoleService roleService, AdminService adminService) {
        return args -> {
            try {
                if (roleService.getRole("ADMIN") == null)
                    roleService.createRole("ADMIN");
                if (roleService.getRole("USER") == null)
                    roleService.createRole("USER");
                
                try {
                    adminService.getUser("admin");
                } catch (UserNotFoundException e) {
                    authService.register(
                        new RegisterDTO("first", "last", "admin", "adminpw")
                    );
                }
            
                adminService.updateRole(adminService.getUser("admin").getId(), "ADMIN");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}

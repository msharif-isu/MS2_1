package harmonize;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import harmonize.DTOs.RegisterDTO;
import harmonize.ErrorHandling.Exceptions.EntityAlreadyExistsException;
import harmonize.Services.AdminService;
import harmonize.Services.AuthService;
import harmonize.Services.RoleService;
import harmonize.Services.UserService;

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
    public CommandLineRunner initUser(AuthService authService, RoleService roleService, AdminService adminService, UserService userService) {
        return args -> {
            try {
                if (roleService.getRole("ADMIN") == null)
                    roleService.createRole("ADMIN");
                if (roleService.getRole("MODERATOR") == null)
                    roleService.createRole("MODERATOR");
                if (roleService.getRole("USER") == null)
                    roleService.createRole("USER");
                
                try {
                    authService.register(new RegisterDTO("first", "last", "admin", "adminpw"));
                    authService.register(new RegisterDTO("first", "last", "mod", "modpw"));
                    authService.register(new RegisterDTO("John", "Smith", "john", "johnpw"));
                    authService.register(new RegisterDTO("Tim", "Brown", "tim", "timpw"));
                    authService.register(new RegisterDTO("Manas", "Mathur", "manasmathur2023", "Backup890!"));
                } catch (EntityAlreadyExistsException e) {}
                
                try {
                    userService.addFriend(adminService.getUser("john").getId(), adminService.getUser("tim").getId());
                    userService.addFriend(adminService.getUser("tim").getId(), adminService.getUser("john").getId());
                    userService.addFriend(adminService.getUser("manasmathur2023").getId(), adminService.getUser("john").getId());
                    userService.addFriend(adminService.getUser("john").getId(), adminService.getUser("manasmathur2023").getId());
                } catch (EntityAlreadyExistsException e) {}
            
                try {
                    adminService.updateRole(adminService.getUser("admin").getId(), "ADMIN");
                    adminService.updateRole(adminService.getUser("mod").getId(), "MODERATOR");
                    adminService.deleteRole(adminService.getUser("admin").getId(), "USER");
                    adminService.deleteRole(adminService.getUser("mod").getId(), "USER");
                } catch (EntityAlreadyExistsException e) {}
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}

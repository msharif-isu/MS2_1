package harmonize;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import harmonize.Entities.Role;
import harmonize.Entities.User;
import harmonize.Repositories.RoleRepository;
import harmonize.Repositories.UserRepository;

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
    public CommandLineRunner initUser(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder encoder) {
        return args -> {
            try {
                Role adminRole = new Role("ADMIN");
                Role userRole = new Role("USER");
                User adminUser = new User("admin", encoder.encode("adminpw"));

                roleRepository.save(adminRole);
                roleRepository.save(userRole);
                adminUser.getRoles().add(adminRole);
                adminUser.getRoles().add(userRole);
                userRepository.save(adminUser);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        };
    }

}

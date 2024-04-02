package harmonize;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.apache.catalina.Context;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import harmonize.DTOs.RegisterDTO;
import harmonize.ErrorHandling.Exceptions.RolePermissionException;
import harmonize.ErrorHandling.Exceptions.UserAlreadyFriendException;
import harmonize.ErrorHandling.Exceptions.UsernameTakenException;
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
                    authService.register(new RegisterDTO("john", "smith", "jsmith", "johnpw"));
                    authService.register(new RegisterDTO("tim", "brown", "tbrown", "timpw"));
                } catch (UsernameTakenException e) {}
                
                try {
                    userService.addFriend(adminService.getUser("jsmith").getId(), adminService.getUser("tbrown").getId());
                    userService.addFriend(adminService.getUser("tbrown").getId(), adminService.getUser("jsmith").getId());
                } catch (UserAlreadyFriendException e) {}
            
                try {
                    adminService.updateRole(adminService.getUser("admin").getId(), "ADMIN");
                    adminService.updateRole(adminService.getUser("mod").getId(), "MODERATOR");
                    adminService.deleteRole(adminService.getUser("admin").getId(), "USER");
                    adminService.deleteRole(adminService.getUser("mod").getId(), "USER");
                } catch (RolePermissionException e) {}
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(redirectConnector());
        return tomcat;
    }

    private Connector redirectConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }

}

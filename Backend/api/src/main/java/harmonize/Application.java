package harmonize;

import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.apache.catalina.Context;
import java.io.File;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import harmonize.DTOs.RegisterDTO;
import harmonize.Entities.Conversation;
import harmonize.Entities.User;
import harmonize.ErrorHandling.Exceptions.EntityAlreadyExistsException;
import harmonize.Repositories.UserRepository;
import harmonize.Security.ChatCrypto;
import harmonize.Services.AdminService;
import harmonize.Services.AuthService;
import harmonize.Services.ConversationService;
import harmonize.Services.MessageService;
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
    public CommandLineRunner initUser(AuthService authService, RoleService roleService, AdminService adminService, UserService userService, ConversationService conversationService, MessageService messageService, ChatCrypto chatCrypto, UserRepository userRepository) {
        return args -> {
            try {
                new File(System.getProperty("user.dir") + File.separator + "icons" + File.separator).mkdirs();

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
                
                User john = userRepository.findByUsername("john");
                User tim = userRepository.findByUsername("tim");
                User manas = userRepository.findByUsername("manasmathur2023");

                try {
                    userService.addFriend(john.getId(), tim.getId());
                    userService.addFriend(tim.getId(), john.getId());
                    userService.addFriend(manas.getId(), john.getId());
                    userService.addFriend(john.getId(), manas.getId());
                    userService.addFriend(manas.getId(), tim.getId());
                    userService.addFriend(tim.getId(), manas.getId());
                } catch (EntityAlreadyExistsException e) {}

                john = userRepository.findByUsername("john");
                tim = userRepository.findByUsername("tim");
                manas = userRepository.findByUsername("manasmathur2023");



                Conversation conversation = conversationService.createConversation(Set.of(manas, john));
                messageService.createMessage(john, conversation, "Wassup", chatCrypto.unwrap("johnpw", john.getPrivateKeyWrapped()));

                conversation = conversationService.createConversation(Set.of(tim, john));
                messageService.createMessage(tim, conversation, "Hii", chatCrypto.unwrap("timpw", tim.getPrivateKeyWrapped()));
            
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

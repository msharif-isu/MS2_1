package harmonize.Users;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * 
 * @author Isaac Denning and Phu Nguyen
 * 
 */ 
@Entity
@Table(name = "users")
@Data
public class User {
    static final int MAXUSERNAMELENGTH = 20;

     /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Username cannot be blank")
    @Size(max = MAXUSERNAMELENGTH, message = "Username cannot not be more than {max} characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {

    }
}


package harmonize.Users;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 
 * @author Isaac Denning and Phu Nguyen
 * 
 */ 
@Entity
@Table(name = "Users")
public class User {
    static final int MAXUSERNAMELENGTH = 20;
    static final int MINPASSWORDLENGTH = 4;

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
    @Size(min = MINPASSWORDLENGTH, message = "Password should not be less than {min} characters")
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
    
}


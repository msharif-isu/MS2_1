package harmonize.Entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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
     /* 
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private String bio;

    private String privateKeyWrapped;

    private String publicKey;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
                             inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "friends", joinColumns = @JoinColumn(name = "user_id",   referencedColumnName = "id"),
                          inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id"))
    private Set<User> friends = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "friendInvites", joinColumns = @JoinColumn(name = "user_id",    referencedColumnName = "id"),
                                inverseJoinColumns = @JoinColumn(name = "inviter_id", referencedColumnName = "id"))
    private Set<User> friendInvites = new HashSet<>();

    public User(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.bio = "";
    }

    public User(String username, String password) {
        this.firstName = "";
        this.lastName = "";
        this.username = username;
        this.password = password;
        this.bio = "";
    }

    public User() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        User user = (User) o;
        return user.id == this.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}


package harmonize.Users;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import harmonize.Roles.Role;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
                             inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
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


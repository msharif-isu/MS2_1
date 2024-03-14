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
 * @author Isaac Denning
 * 
 */ 
@Entity
@Table(name = "chats")
@Data
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "chat_members", joinColumns = @JoinColumn(name = "chat_id", referencedColumnName = "id"),
                             inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private Set<User> members = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "chat_messages", joinColumns = @JoinColumn(name = "chat_id", referencedColumnName = "id"),
                             inverseJoinColumns = @JoinColumn(name = "message_id", referencedColumnName = "id"))
    private Set<Message> messages = new HashSet<>();
}

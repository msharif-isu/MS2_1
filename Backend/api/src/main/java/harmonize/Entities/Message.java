package harmonize.Entities;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 
 * @author Isaac Denning
 * 
 */ 
@Entity
@Table(name = "messages")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private java.util.Date time;

    @ElementCollection
    @CollectionTable(name="message_encryptions", joinColumns=@JoinColumn(name="message_id"))
    @MapKeyJoinColumn(name="user_id")
    @Column(name="encrypted_message")
    private Map<User, String> encryptions = new HashMap<>();
}

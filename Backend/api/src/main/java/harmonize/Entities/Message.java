package harmonize.Entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Isaac Denning
 * 
 */ 
@Entity
@Table(name = "messages")
@Data
@RequiredArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date time;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User sender;

    @ManyToOne
    @JoinColumn(name="conversation_id", referencedColumnName = "id")
    private Conversation conversation;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="message_encryptions", joinColumns=@JoinColumn(name="message_id"))
    @MapKeyJoinColumn(name="recipient_id", referencedColumnName = "id")
    @Column(name="encrypted_message", columnDefinition = "LONGTEXT")
    private Map<User, String> encryptions = new HashMap<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return message.id == this.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

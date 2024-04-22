package harmonize.Entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import harmonize.Entities.FeedItems.AbstractFeedItem;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.PreRemove;
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(columnDefinition = "LONGTEXT")
    private String bio;

    @Column(columnDefinition = "TEXT")
    private String publicKey;

    @Column(columnDefinition = "TEXT")
    private String privateKeyWrapped;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @OrderBy("time DESC")
    private List<LikedSong> likedSongs = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_top_artists", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "artist_id")
    @OrderColumn(name = "top_artist_index")
    private List<String> topArtists = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "friends",
        joinColumns = @JoinColumn(name = "user_id",   referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "friend_id", referencedColumnName = "id"))
    private Set<User> friends = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "friend_invites",
        joinColumns = @JoinColumn(name = "user_id",    referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "inviter_id", referencedColumnName = "id"))
    private Set<User> friendInvites = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "conversation_members",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "conversation_id", referencedColumnName = "id"))
    private Set<Conversation> conversations = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_seen_feed",
        joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "feed_item_id", referencedColumnName = "id"))
    private Set<AbstractFeedItem> seenFeed = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Report> sentReports = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Report> receivedReports = new HashSet<>();

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

    @PreRemove
    public void removeReference() {
        for (LikedSong likedSong : likedSongs)
            likedSong.removeReference();
        
        for (Role role : roles)
            role.getUsers().remove(this);

        for (User user : friends)
            user.friends.remove(this);

        for (Report report : sentReports)
            report.removeReference();

        for (Report report : receivedReports)
            report.removeReference();

        for (Conversation conversation : conversations) {
            conversation.getMembers().remove(this);
            if (conversation.getMembers().size() <= 1) {
                conversation.removeReference();
            } else {
                for (Message message : conversation.getMessages()) {
                    message.getEncryptions().remove(this);
                    if (message.getSender() == this) {
                        message.removeReference();
                    }
                }
            }
        }

        for (AbstractFeedItem feedItem : seenFeed)
            feedItem.removeReference();
    }

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


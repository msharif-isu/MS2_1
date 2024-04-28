package harmonize.Entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "posts")
@Data
@RequiredArgsConstructor
public class Post {
    public static final int POST_LENGTH_MAX = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date time;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    private User poster;

    @Column(columnDefinition = "LONGTEXT")
    @Size(max = POST_LENGTH_MAX)
    private String post;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return post.id == this.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

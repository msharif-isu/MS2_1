package harmonize.Entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
    public static final int POST_LENGTH_MAX = 250;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date time;

    @JsonIncludeProperties(value = {"id"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id", referencedColumnName = "id")  
    private User poster;

    @Column(columnDefinition = "LONGTEXT")
    @Size(max = POST_LENGTH_MAX)
    private String post;

    public Post(User poster, String post) {
        this.poster = poster;
        this.post = post;
        this.time = new Date(System.currentTimeMillis());
    }

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

package harmonize.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "reports")
@Data
@RequiredArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="message_id", referencedColumnName = "id")
    private Message message;

    @ManyToOne
    @JoinColumn(name="reporter_id", referencedColumnName = "id")
    private User reporter;

    @ManyToOne
    @JoinColumn(name="reported_id", referencedColumnName = "id")
    private User reported;

    @Column(columnDefinition = "LONGTEXT")
    private String messageText;

    @Column(columnDefinition = "LONGTEXT")
    private String reportText;

    @PreRemove
    public void removeReference() {
        message.getReports().remove(this);
        reporter.getSentReports().remove(this);
        reported.getReceivedReports().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return report.id == this.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

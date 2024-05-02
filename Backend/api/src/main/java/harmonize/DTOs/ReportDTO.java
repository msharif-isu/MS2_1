package harmonize.DTOs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import harmonize.Entities.Report;
import lombok.Data;

@Data
public class ReportDTO {
    private int id;
    private MessageDTO message;
    private UserDTO reporter;
    private UserDTO reported;
    private String reportText;

    public ReportDTO(Report report) {
        this.id = report.getId();
        this.message = new MessageDTO(report.getMessage());
        this.message.setText(report.getMessageText());
        this.reporter = new UserDTO(report.getReporter());
        this.reported = new UserDTO(report.getReported());
        this.reportText = report.getReportText();
    }

    @JsonCreator
    public ReportDTO(
        @JsonProperty("id") int id,
        @JsonProperty("message") MessageDTO message,
        @JsonProperty("reporter") UserDTO reporter,
        @JsonProperty("reported") UserDTO reported,
        @JsonProperty("reportText") String reportText
        ) {
        this.id = id;
        this.message = message;
        this.reporter = reporter;
        this.reported = reported;
        this.reportText = reportText;
    }
}

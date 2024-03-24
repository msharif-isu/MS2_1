package harmonize.DTOs;

import harmonize.Entities.Report;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
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
}

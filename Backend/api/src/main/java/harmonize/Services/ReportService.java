package harmonize.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import harmonize.DTOs.ReportDTO;
import harmonize.Entities.Message;
import harmonize.Entities.Report;
import harmonize.Entities.User;
import harmonize.ErrorHandling.Exceptions.EntityNotFoundException;
import harmonize.ErrorHandling.Exceptions.InvalidArgumentException;
import harmonize.ErrorHandling.Exceptions.UnauthorizedException;
import harmonize.Repositories.MessageRepository;
import harmonize.Repositories.ReportRepository;
import harmonize.Repositories.UserRepository;

@Service
public class ReportService {
    private UserRepository userRepository;
    private MessageRepository messageRepository;
    private ReportRepository reportRepository;
    private BCryptPasswordEncoder encoder;

    @Autowired
    ReportService(UserRepository userRepository, MessageRepository messageRepository, ReportRepository reportRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.reportRepository = reportRepository;
        this.encoder = encoder;
    }

    @NonNull
    public List<ReportDTO> getSentReports(int id) {
        User user = userRepository.findReferenceById(id);
        if (user == null)
            throw new EntityNotFoundException("User " + id + " not found.");
        List<ReportDTO> result = new ArrayList<>();
        for (Report report : user.getSentReports()) {
            result.add(new ReportDTO(report));
        }
        return result;
    }

    @NonNull
    public List<ReportDTO> getReceivedReports(int id) {
        User user = userRepository.findReferenceById(id);
        if (user == null)
            throw new EntityNotFoundException("User " + id + " not found.");
        List<ReportDTO> result = new ArrayList<>();
        for (Report report : user.getReceivedReports()) {
            result.add(new ReportDTO(report));
        }
        return result;
    }

    @NonNull
    public ReportDTO getSentReport(int id, int reportID) {
        User user = userRepository.findReferenceById(id);
        if (user == null)
            throw new EntityNotFoundException("User " + id + " not found.");
        Report report = reportRepository.findReferenceById(reportID);
        if (report == null || !user.getSentReports().contains(report))
            throw new EntityNotFoundException("Report" + reportID + " not found.");
        return new ReportDTO(report);
    }

    @NonNull
    public ReportDTO getReceivedReport(int id, int reportID) {
        User user = userRepository.findReferenceById(id);
        if (user == null)
            throw new EntityNotFoundException("User " + id + " not found.");
        Report report = reportRepository.findReferenceById(reportID);
        if (report == null || !user.getReceivedReports().contains(report))
            throw new EntityNotFoundException("Report" + reportID + " not found.");
        return new ReportDTO(report);
    }

    @NonNull
    public ReportDTO sendReport(int id, ReportDTO reportDTO) {
        User reporter = userRepository.findReferenceById(id);
        if (reporter == null)
            throw new EntityNotFoundException("User " + id + " not found.");
        Report report = new Report();
        if (reportDTO.getMessage() == null)
            throw new InvalidArgumentException("No message field provided.");
        Message message = messageRepository.findReferenceById(reportDTO.getMessage().getId());
        if (message == null)
            throw new EntityNotFoundException("Message " + reportDTO.getMessage().getId() + " not found.");
        User reported = message.getSender();
        if (!message.getConversation().getMembers().contains(reporter))
            throw new UnauthorizedException("User " + reporter.getId() + " does not have access to message " + message.getId());
        if (!encoder.matches(reportDTO.getMessage().getText(), message.getHash()))
            throw new InvalidArgumentException("Message text is incorrect.");

        report.setMessage(message);
        report.setReported(reported);
        report.setReporter(reporter);
        report.setReportText(reportDTO.getReportText());
        report.setMessageText(reportDTO.getMessage().getText());

        message.getReports().add(report);
        reported.getReceivedReports().add(report);
        reporter.getSentReports().add(report);

        reportRepository.save(report);
        messageRepository.save(message);
        userRepository.save(reported);
        userRepository.save(reporter);
    
        return new ReportDTO(report);
    }

    @NonNull
    public String deleteSentReport(int id, int reportID) {
        User user = userRepository.findReferenceById(id);
        if (user == null)
            throw new EntityNotFoundException("User " + id + " not found.");
        Report report = reportRepository.findReferenceById(reportID);
        if (report == null || !user.getSentReports().contains(report))
            throw new EntityNotFoundException("Report" + reportID + " not found.");
        deleteReport(report);
        return new String(String.format("Report %d was deleted.", report.getId()));
    }

    @NonNull
    public List<ReportDTO> getReports() {
        List<ReportDTO> result = new ArrayList<>();
        for (Report report : reportRepository.findAll()) {
            result.add(new ReportDTO(report));
        }
        return result;
    }

    @NonNull
    public ReportDTO getReport(int id) {
        Report report = reportRepository.findReferenceById(id);
        if (report == null)
            throw new EntityNotFoundException("Report" + id + " not found.");
        return new ReportDTO(report);
    }

    @NonNull
    public String deleteReport(int reportID) {
        Report report = reportRepository.findReferenceById(reportID);
        if (report == null)
            throw new EntityNotFoundException("Report" + reportID + " not found.");
        deleteReport(report);

        return new String(String.format("Report %d was deleted.", report.getId()));
    }

    public void deleteReport(Report report) {
        reportRepository.delete(report);
    }
}

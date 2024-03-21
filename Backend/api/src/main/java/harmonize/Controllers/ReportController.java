package harmonize.Controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import harmonize.DTOs.ReportDTO;
import harmonize.Services.ReportService;

/**
 * 
 * @author Phu Nguyen
 * 
 */ 

@RestController
@RequestMapping("/reports")
public class ReportController {

    private ReportService reportService;
    
    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    
    @PostMapping(path = "")
    public ResponseEntity<ReportDTO> report(Principal principal, @RequestBody ReportDTO report){
        return ResponseEntity.ok(reportService.report(principal, report));
    }

}

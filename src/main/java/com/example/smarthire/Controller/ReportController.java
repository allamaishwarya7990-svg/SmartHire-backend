package com.example.smarthire.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.smarthire.Dto.ReportDto;
import com.example.smarthire.Service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportDto> submitReport(
            @RequestBody ReportDto dto,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(reportService.submitReport(email, dto));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReportDto>> getMyReports(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(reportService.getUserReports(email));
    }
}

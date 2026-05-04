package com.ark.construction.controller;

import com.ark.construction.dto.ProjectReportDto;
import com.ark.construction.service.PdfService;
import com.ark.construction.service.ReportService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ReportController {

    private final ReportService service;
    private final PdfService pdfService;

    public ReportController(ReportService service, PdfService pdfService) {
        this.service = service;
        this.pdfService = pdfService;
    }

    @GetMapping("/reports")
    public String reports(@RequestParam(required = false) String from,
                          @RequestParam(required = false) String to,
                          Model model) {

        List<ProjectReportDto> reports;

        if (from != null && to != null && !from.isEmpty() && !to.isEmpty()) {
            reports = service.projectReportsByDate(from, to);
            model.addAttribute("from", from);
            model.addAttribute("to", to);
        } else {
            reports = service.projectReports();
        }

        // totals
        Double totalContract = reports.stream().mapToDouble(ProjectReportDto::getTotalCost).sum();
        Double totalPaid = reports.stream().mapToDouble(ProjectReportDto::getTotalPaid).sum();
        Double totalExpense = reports.stream().mapToDouble(ProjectReportDto::getTotalExpense).sum();
        Double totalPending = reports.stream().mapToDouble(ProjectReportDto::getPending).sum();
        Double totalBalance = reports.stream().mapToDouble(ProjectReportDto::getBalance).sum();
        Double totalProfit = reports.stream().mapToDouble(ProjectReportDto::getProfit).sum();

        model.addAttribute("reports", reports);
        model.addAttribute("totalContract", totalContract);
        model.addAttribute("totalPaid", totalPaid);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("totalPending", totalPending);
        model.addAttribute("totalBalance", totalBalance);
        model.addAttribute("totalProfit", totalProfit);

        return "reports/reports";
    }

    @GetMapping("/reports/pdf")
    public ResponseEntity<byte[]> exportPdf(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {

        List<ProjectReportDto> reports;

        // ✅ FIX: handle null and "null"
        if (from != null && to != null &&
                !from.equals("null") && !to.equals("null") &&
                !from.isEmpty() && !to.isEmpty()) {

            reports = service.projectReportsByDate(from, to);

        } else {
            reports = service.projectReports();
        }

        byte[] pdf = pdfService.generateReportPdf(reports);

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=project-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
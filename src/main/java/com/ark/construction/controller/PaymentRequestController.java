package com.ark.construction.controller;

import com.ark.construction.entity.PaymentRequest;
import com.ark.construction.repository.BankAccountRepository;
import com.ark.construction.service.PaymentRequestService;
import com.ark.construction.service.PdfService;
import com.ark.construction.service.ProjectService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;

@Controller
@RequestMapping("/payment-requests")
public class PaymentRequestController {

    private final PaymentRequestService service;
    private final ProjectService projectService;
    private final BankAccountRepository bankRepo;
    private final PdfService pdfService;

    public PaymentRequestController(PaymentRequestService service,
                                    ProjectService projectService,
                                    BankAccountRepository bankRepo, PdfService pdfService) {
        this.service = service;
        this.projectService = projectService;
        this.bankRepo = bankRepo;
        this.pdfService = pdfService;
    }

    // FORM
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("paymentRequest", new PaymentRequest());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("banks", bankRepo.findByActiveTrue());
        return "payment-request/form";
    }

    // SAVE → redirect preview
    @PostMapping
    public String save(@ModelAttribute PaymentRequest pr) {
        PaymentRequest saved = service.save(pr);
        return "redirect:/payment-requests/" + saved.getId() + "/preview";
    }

    // PREVIEW
    @GetMapping("/{id}/preview")
    public String preview(@PathVariable Long id, Model model) {
        PaymentRequest pr = service.get(id);
        model.addAttribute("pr", pr);
        return "payment-request/preview";
    }

    @GetMapping("/qr/{id}")
    @ResponseBody
    public byte[] qr(@PathVariable Long id) throws Exception {

        PaymentRequest pr = service.get(id);

        String upi = "upi://pay?pa=" + pr.getBankAccount().getUpiId()
                + "&pn=ARK Builders"
                + "&am=" + pr.getAmount()
                + "&cu=INR";

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(upi, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);

        return out.toByteArray();
    }

    @GetMapping("/{id}/pdf")
    @ResponseBody
    public ResponseEntity<byte[]> downloadPaymentRequestPdf(@PathVariable Long id) {

        PaymentRequest pr = service.get(id);

        byte[] pdf = pdfService.generatePaymentRequestPdf(pr);

        String projectName = pr.getProject().getProjectName()
                .replaceAll("[^a-zA-Z0-9]", "_");

        String fileName = "ARK_" + projectName + "_PAY_REQUEST_" + pr.getId() + ".pdf";

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}

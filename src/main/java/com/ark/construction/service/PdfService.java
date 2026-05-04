package com.ark.construction.service;

import com.ark.construction.dto.ProjectReportDto;
import com.ark.construction.entity.Payment;
import com.ark.construction.entity.PaymentRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfService {

    public byte[] generatePaymentReceipt(Payment payment) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);

            doc.setMargins(35, 35, 35, 35);

            Color primary = new DeviceRgb(25, 135, 84);
            Color lightGray = new DeviceRgb(245, 247, 250);
            Color dark = new DeviceRgb(33, 37, 41);

            // HEADER
            Table header = new Table(UnitValue.createPercentArray(new float[]{70, 30}))
                    .useAllAvailableWidth();

            header.addCell(new Cell()
                    .add(new Paragraph("ARK BUILDERS AND DEVELOPERS")
                            .setBold()
                            .setFontSize(20)
                            .setFontColor(primary))
                    .add(new Paragraph("Building Your Dream Home Together")
                            .setFontSize(10)
                            .setFontColor(ColorConstants.GRAY))
                    .setBorder(Border.NO_BORDER));

            header.addCell(new Cell()
                    .add(new Paragraph("PAYMENT RECEIPT")
                            .setBold()
                            .setFontSize(14)
                            .setTextAlignment(TextAlignment.RIGHT))
                    .add(new Paragraph("Receipt No: RCPT-" + payment.getId())
                            .setFontSize(10)
                            .setTextAlignment(TextAlignment.RIGHT))
                    .setBorder(Border.NO_BORDER));

            doc.add(header);
            doc.add(new Paragraph("\n"));

            // RECEIPT SUMMARY BOX
            Table summary = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                    .useAllAvailableWidth();

            summary.addCell(labelValue("Client Name", payment.getProject().getClient().getName()));
            summary.addCell(labelValue("Project", payment.getProject().getProjectName()));
            summary.addCell(labelValue("Payment Date", String.valueOf(payment.getPaymentDate())));
            summary.addCell(labelValue("Payment Mode", payment.getPaymentMode()));

            doc.add(summary);
            doc.add(new Paragraph("\n"));

            // AMOUNT BOX
            Table amountBox = new Table(1).useAllAvailableWidth();

            amountBox.addCell(new Cell()
                    .setBackgroundColor(primary)
                    .setPadding(18)
                    .setBorder(Border.NO_BORDER)
                    .add(new Paragraph("Amount Received")
                            .setFontColor(ColorConstants.WHITE)
                            .setFontSize(11))
                    .add(new Paragraph("Rs. " + payment.getAmount())
                            .setFontColor(ColorConstants.WHITE)
                            .setBold()
                            .setFontSize(24)));

            doc.add(amountBox);
            doc.add(new Paragraph("\n"));

            // DETAILS
            Table details = new Table(UnitValue.createPercentArray(new float[]{35, 65}))
                    .useAllAvailableWidth();

            addRow(details, "Note", payment.getNote() != null ? payment.getNote() : "-");
            addRow(details, "Received By", "ARK Builders");
            addRow(details, "Status", "PAID");

            doc.add(details);

            doc.add(new Paragraph("\n\n"));

            // FOOTER
            doc.add(new Paragraph("Thank you for your payment.")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(dark)
                    .setBold());

            doc.add(new Paragraph("This is a computer generated receipt please crosscehck the receipt carefully.")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(9)
                    .setFontColor(ColorConstants.GRAY));

            doc.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
    //

    private Cell labelValue(String label, String value) {
        return new Cell()
                .setPadding(12)
                .setBackgroundColor(new DeviceRgb(245, 247, 250))
                .setBorder(new SolidBorder(new DeviceRgb(230, 230, 230), 1))
                .add(new Paragraph(label)
                        .setFontSize(9)
                        .setFontColor(ColorConstants.GRAY))
                .add(new Paragraph(value != null ? value : "-")
                        .setFontSize(12)
                        .setBold());
    }

    private void addRow(Table table, String label, String value) {
        table.addCell(new Cell()
                .setPadding(10)
                .setBackgroundColor(new DeviceRgb(248, 249, 250))
                .add(new Paragraph(label).setBold())
                .setBorder(new SolidBorder(new DeviceRgb(230, 230, 230), 1)));

        table.addCell(new Cell()
                .setPadding(10)
                .add(new Paragraph(value != null ? value : "-"))
                .setBorder(new SolidBorder(new DeviceRgb(230, 230, 230), 1)));
    }

    //payment request pdf
    public byte[] generatePaymentRequestPdf(PaymentRequest pr) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document doc = new Document(pdf);

            doc.setMargins(22, 28, 20, 28);

            Color primary = new DeviceRgb(13, 110, 253);

            // HEADER
            Table header = new Table(UnitValue.createPercentArray(new float[]{70, 30}))
                    .useAllAvailableWidth();

            header.addCell(new Cell()
                    .add(new Paragraph("ARK BUILDERS AND DEVELOPERS")
                            .setBold()
                            .setFontSize(18)
                            .setFontColor(primary))
                    .add(new Paragraph("Building Your Dream Home Together")
                            .setFontSize(9)
                            .setFontColor(ColorConstants.GRAY))
                    .setBorder(Border.NO_BORDER));

            header.addCell(new Cell()
                    .add(new Paragraph("PAYMENT REQUEST")
                            .setBold()
                            .setFontSize(13)
                            .setTextAlignment(TextAlignment.RIGHT))
                    .add(new Paragraph("Request No: PAY-REQ-" + pr.getId())
                            .setFontSize(9)
                            .setTextAlignment(TextAlignment.RIGHT))
                    .setBorder(Border.NO_BORDER));

            doc.add(header);
            doc.add(new Paragraph("\n"));

            // SUMMARY
            Table summary = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                    .useAllAvailableWidth();

            summary.addCell(labelValue("Client Name", pr.getProject().getClient().getName()));
            summary.addCell(labelValue("Project", pr.getProject().getProjectName()));
            summary.addCell(labelValue("Request Date", String.valueOf(pr.getRequestDate())));
            summary.addCell(labelValue("Status", pr.getStatus()));

            doc.add(summary);
            doc.add(new Paragraph("\n"));

            // AMOUNT BOX
            Table amountBox = new Table(1).useAllAvailableWidth();

            amountBox.addCell(new Cell()
                    .setBackgroundColor(primary)
                    .setPadding(14)
                    .setBorder(Border.NO_BORDER)
                    .add(new Paragraph("Amount Requested")
                            .setFontColor(ColorConstants.WHITE)
                            .setFontSize(10))
                    .add(new Paragraph("Rs. " + pr.getAmount())
                            .setFontColor(ColorConstants.WHITE)
                            .setBold()
                            .setFontSize(22)));

            doc.add(amountBox);
            doc.add(new Paragraph("\n"));

            // BANK + QR SIDE BY SIDE
            Table paymentSection = new Table(UnitValue.createPercentArray(new float[]{65, 35}))
                    .useAllAvailableWidth();

            Table bank = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                    .useAllAvailableWidth();

            addRow(bank, "Bank Name", pr.getBankAccount().getBankName());
            addRow(bank, "Account Holder", pr.getBankAccount().getAccountHolder());
            addRow(bank, "Account Number", pr.getBankAccount().getAccountNumber());
            addRow(bank, "IFSC Code", pr.getBankAccount().getIfscCode());
            addRow(bank, "UPI ID", pr.getBankAccount().getUpiId());
            addRow(bank, "Remarks", pr.getRemarks() != null ? pr.getRemarks() : "-");

            Cell bankCell = new Cell()
                    .add(new Paragraph("Bank Details").setBold().setFontSize(11))
                    .add(bank)
                    .setBorder(Border.NO_BORDER)
                    .setPadding(0)
                    .setPaddingRight(10)
                    .setVerticalAlignment(VerticalAlignment.TOP);

            String upi = "upi://pay?pa=" + pr.getBankAccount().getUpiId()
                    + "&pn=ARK Builders"
                    + "&am=" + pr.getAmount()
                    + "&cu=INR"
                    + "&tn=Payment Request " + pr.getId();

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(upi, BarcodeFormat.QR_CODE, 170, 170);

            ByteArrayOutputStream qrOut = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", qrOut);

            Image qrImage = new Image(ImageDataFactory.create(qrOut.toByteArray()))
                    .setWidth(115)
                    .setHeight(115)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER);

            Cell qrCell = new Cell()
                    .add(new Paragraph("Scan & Pay")
                            .setBold()
                            .setFontSize(10)
                            .setTextAlignment(TextAlignment.CENTER))
                    .add(qrImage)
                    .add(new Paragraph("GPay / PhonePe / UPI")
                            .setFontSize(8)
                            .setFontColor(ColorConstants.GRAY)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setBorder(new SolidBorder(new DeviceRgb(230, 230, 230), 1))
                    .setPadding(6)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE);

            paymentSection.addCell(bankCell);
            paymentSection.addCell(qrCell);

            doc.add(paymentSection);

            doc.add(new Paragraph("\nThank you.")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
                    .setFontSize(10));

            doc.add(new Paragraph("This is a computer generated payment request.")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(8)
                    .setFontColor(ColorConstants.GRAY));

            doc.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating payment request PDF", e);
        }
    }

    public byte[] generateReportPdf( List<ProjectReportDto> reports) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // 🔷 TITLE
            document.add(new Paragraph("ARK Builders - Project Report")
                    .setBold()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(15));

            // 🔷 TABLE
            float[] columnWidths = {140, 120, 80, 80, 80, 80, 80};
            Table table = new Table(columnWidths);

            // Header Style
            DeviceRgb headerBg = new DeviceRgb(240, 240, 240);

            addHeader(table, "Project", headerBg);
            addHeader(table, "Client", headerBg);
            addHeader(table, "Contract", headerBg);
            addHeader(table, "Paid", headerBg);
            addHeader(table, "Expense", headerBg);
            addHeader(table, "Pending", headerBg);
            addHeader(table, "Profit", headerBg);

            // 🔷 DATA
            for (ProjectReportDto r : reports) {

                table.addCell(cell(r.getProjectName()));
                table.addCell(cell(r.getClientName()));

                table.addCell(cell("₹ " + r.getTotalCost()));
                table.addCell(cell("₹ " + r.getTotalPaid()));
                table.addCell(cell("₹ " + r.getTotalExpense()));
                table.addCell(cell("₹ " + r.getPending()));

                Cell profitCell = cell("₹ " + r.getProfit());

                if (r.getProfit() < 0) {
                    profitCell.setFontColor(ColorConstants.RED);
                } else {
                    profitCell.setFontColor(new DeviceRgb(0, 128, 0));
                }

                table.addCell(profitCell);
            }

            document.add(table);

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    // 🔹 HEADER CELL
    private void addHeader(Table table, String text, DeviceRgb bg) {
        table.addHeaderCell(new Cell()
                .add(new Paragraph(text).setBold())
                .setBackgroundColor(bg)
                .setPadding(8));
    }

    // 🔹 NORMAL CELL
    private Cell cell(String text) {
        return new Cell()
                .add(new Paragraph(text))
                .setPadding(6)
                .setBorder(new SolidBorder(new DeviceRgb(230, 230, 230), 1));
    }
}
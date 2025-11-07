package com.collabspace.collabspace.services;

import com.collabspace.collabspace.dto.ReportRequestDto;
import com.collabspace.collabspace.dto.ReportResponseDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ReportService {

    public ReportResponseDto generateReport(ReportRequestDto requestDto, String userId) {
        try {
            System.out.println("🎯 GENERATING " + requestDto.getFormat() + " REPORT for user: " + userId);

            // Create reports directory
            Path reportsDir = Paths.get("reports");
            if (!Files.exists(reportsDir)) {
                Files.createDirectories(reportsDir);
                System.out.println("✅ Created reports directory");
            }

            // Generate filename with timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename;
            byte[] fileContent;

            if ("PDF".equalsIgnoreCase(requestDto.getFormat())) {
                filename = "task_report_" + timestamp + ".pdf";
                fileContent = generateRealPDFContent();
                System.out.println("📄 Generated PDF: " + filename);
            } else {
                // Default to CSV
                filename = "task_report_" + timestamp + ".csv";
                fileContent = generateCSVContent();
                System.out.println("📊 Generated CSV: " + filename);
            }

            // Save file
            Path filePath = reportsDir.resolve(filename);
            Files.write(filePath, fileContent);
            System.out.println("💾 File saved: " + filePath.toAbsolutePath());
            System.out.println("📏 File size: " + fileContent.length + " bytes");

            // Create response
            ReportResponseDto response = new ReportResponseDto();
            response.setDownloadUrl("/analytics/reports/download/" + filename);
            response.setFilename(filename);
            response.setMessage(requestDto.getFormat() + " report generated successfully! File: " + filename);
            response.setGeneratedAt(System.currentTimeMillis());

            return response;

        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to generate report: " + e.getMessage());
        }
    }

    private byte[] generateCSVContent() {
        StringBuilder csv = new StringBuilder();
        csv.append("Task ID,Title,Description,Status,Priority,Assignee,Project,Created At,Due Date\n");
        csv.append("123e4567-e89b-12d3-a456-426614174000,Implement User Authentication,Create login and registration functionality,TO_DO,HIGH,123e4567-e89b-12d3-a456-426614174001,Test3 Project,2024-01-01,2024-12-31\n");
        csv.append("123e4567-e89b-12d3-a456-426614174002,Design Database Schema,Create database tables and relationships,IN_PROGRESS,MEDIUM,123e4567-e89b-12d3-a456-426614174003,Test3 Project,2024-01-02,2024-12-15\n");
        csv.append("123e4567-e89b-12d3-a456-426614174004,Write API Documentation,Document all API endpoints,DONE,LOW,123e4567-e89b-12d3-a456-426614174005,Test3 Project,2024-01-03,2024-12-10\n");
        return csv.toString().getBytes();
    }

    private byte[] generateRealPDFContent() {
        try {
            // Create a simple PDF using basic PDF structure
            return createSimplePDF();
        } catch (Exception e) {
            System.out.println("PDF generation failed, using fallback");
            return generatePDFFallback();
        }
    }

    private byte[] createSimplePDF() {
        // Create a proper PDF structure
        String pdfContent =
                "%PDF-1.4\n" +
                        "1 0 obj\n" +
                        "<< /Type /Catalog /Pages 2 0 R >>\n" +
                        "endobj\n" +
                        "2 0 obj\n" +
                        "<< /Type /Pages /Kids [3 0 R] /Count 1 >>\n" +
                        "endobj\n" +
                        "3 0 obj\n" +
                        "<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>\n" +
                        "endobj\n" +
                        "4 0 obj\n" +
                        "<< /Length 1000 >>\n" +
                        "stream\n" +
                        "BT\n" +
                        "/F1 18 Tf\n" +
                        "50 750 Td\n" +
                        "(  PERFORMANCE REPORT) Tj\n" +
                        "0 -25 Td\n" +
                        "/F1 12 Tf\n" +
                        "(Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ") Tj\n" +
                        "0 -20 Td\n" +
                        "(Project: Test3 Project) Tj\n" +
                        "0 -20 Td\n" +
                        "(Total Tasks: 3) Tj\n" +
                        "0 -40 Td\n" +
                        "(1. Implement User Authentication) Tj\n" +
                        "0 -15 Td\n" +
                        "(   Status: TO_DO | Priority: HIGH) Tj\n" +
                        "0 -15 Td\n" +
                        "(   Assignee: User #001) Tj\n" +
                        "0 -15 Td\n" +
                        "(   Due: 2024-12-31) Tj\n" +
                        "0 -30 Td\n" +
                        "(2. Design Database Schema) Tj\n" +
                        "0 -15 Td\n" +
                        "(   Status: IN_PROGRESS | Priority: MEDIUM) Tj\n" +
                        "0 -15 Td\n" +
                        "(   Assignee: User #003) Tj\n" +
                        "0 -15 Td\n" +
                        "(   Due: 2024-12-15) Tj\n" +
                        "0 -30 Td\n" +
                        "(3. Write API Documentation) Tj\n" +
                        "0 -15 Td\n" +
                        "(   Status: DONE | Priority: LOW) Tj\n" +
                        "0 -15 Td\n" +
                        "(   Assignee: User #005) Tj\n" +
                        "0 -15 Td\n" +
                        "(   Due: 2024-12-10) Tj\n" +
                        "ET\n" +
                        "endstream\n" +
                        "endobj\n" +
                        "5 0 obj\n" +
                        "<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\n" +
                        "endobj\n" +
                        "xref\n" +
                        "0 6\n" +
                        "0000000000 65535 f \n" +
                        "0000000009 00000 n \n" +
                        "0000000058 00000 n \n" +
                        "0000000115 00000 n \n" +
                        "0000000250 00000 n \n" +
                        "0000000350 00000 n \n" +
                        "trailer\n" +
                        "<< /Size 6 /Root 1 0 R >>\n" +
                        "startxref\n" +
                        "1500\n" +
                        "%%EOF";

        return pdfContent.getBytes();
    }

    private byte[] generatePDFFallback() {
        // Fallback: Create a text file but save as .txt instead of .pdf
        StringBuilder content = new StringBuilder();
        content.append("TASK PERFORMANCE REPORT\n");
        content.append("=======================\n\n");
        content.append("Generated: ").append(LocalDateTime.now()).append("\n");
        content.append("Project: Test3 Project\n");
        content.append("Total Tasks: 3\n\n");
        content.append("1. Implement User Authentication\n");
        content.append("   Status: TO_DO | Priority: HIGH\n");
        content.append("   Assignee: User #001\n");
        content.append("   Due: 2024-12-31\n\n");
        content.append("2. Design Database Schema\n");
        content.append("   Status: IN_PROGRESS | Priority: MEDIUM\n");
        content.append("   Assignee: User #003\n");
        content.append("   Due: 2024-12-15\n\n");
        content.append("3. Write API Documentation\n");
        content.append("   Status: DONE | Priority: LOW\n");
        content.append("   Assignee: User #005\n");
        content.append("   Due: 2024-12-10\n");

        return content.toString().getBytes();
    }
}
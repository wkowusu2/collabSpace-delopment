package com.collabspace.collabspace.controllers;

import com.collabspace.collabspace.dto.ReportRequestDto;
import com.collabspace.collabspace.dto.ReportResponseDto;
import com.collabspace.collabspace.services.ReportService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/analytics/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/export")
    public ResponseEntity<ReportResponseDto> exportReport(
            @RequestBody ReportRequestDto requestDto,
            @RequestHeader("X-User-Id") String userId) {

        System.out.println("🚀 POST /analytics/reports/export");
        System.out.println("📋 Format: " + requestDto.getFormat());
        System.out.println("👤 User: " + userId);

        ReportResponseDto response = reportService.generateReport(requestDto, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadReport(
            @PathVariable String filename,
            @RequestHeader("X-User-Id") String userId) {

        try {
            System.out.println("📥 GET /analytics/reports/download/" + filename);
            System.out.println("👤 User: " + userId);

            Path filePath = Paths.get("reports").resolve(filename).normalize();
            System.out.println("📁 File path: " + filePath.toAbsolutePath());

            if (!Files.exists(filePath)) {
                System.out.println("❌ FILE NOT FOUND: " + filePath);
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                System.out.println("✅ FILE FOUND, serving download...");

                long fileSize = Files.size(filePath);
                System.out.println("📏 File size: " + fileSize + " bytes");

                MediaType mediaType;
                String contentDisposition;

                if (filename.toLowerCase().endsWith(".csv")) {
                    mediaType = MediaType.parseMediaType("text/csv");
                    contentDisposition = "inline; filename=\"" + filename + "\"";
                } else {
                    mediaType = MediaType.APPLICATION_OCTET_STREAM;
                    contentDisposition = "attachment; filename=\"" + filename + "\"";
                }

                return ResponseEntity.ok()
                        .contentType(mediaType)
                        .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                        .body(resource);
            } else {
                System.out.println("❌ FILE EXISTS BUT CANNOT BE READ");
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println("💥 DOWNLOAD ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/test")
    public String testEndpoint() {
        return "✅ Report endpoints are working!";
    }
}
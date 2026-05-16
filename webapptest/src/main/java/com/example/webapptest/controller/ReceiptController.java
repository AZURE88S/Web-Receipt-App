package com.example.webapptest.controller;

import com.example.webapptest.response.ReceiptResponse;
import com.example.webapptest.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/receipt")
@CrossOrigin(origins = "*")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping("/save")
    public String saveToExcel(@RequestBody ReceiptResponse data) {
        File file = new File("receipts.xlsx");
        Workbook workbook;
        Sheet sheet;

        try {
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                workbook = new XSSFWorkbook(fis);
                sheet = workbook.getSheetAt(0);
                fis.close();
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Scanned Receipts");
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Merchant");
                header.createCell(1).setCellValue("Date");
                header.createCell(2).setCellValue("Total");
                header.createCell(3).setCellValue("Currency");
            }

            int lastRow = sheet.getLastRowNum();
            Row row = sheet.createRow(lastRow + 1);
            row.createCell(0).setCellValue(data.getMerchant());
            row.createCell(1).setCellValue(data.getDate());
            row.createCell(2).setCellValue(data.getTotal());
            row.createCell(3).setCellValue(data.getCurrency());

            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            workbook.close();
            fos.close();

            return "Data saved successfully!";
        } catch (Exception e) {
            return "Error saving to Excel: " + e.getMessage();
        }
    }

    @PostMapping("/upload")
    public ReceiptResponse uploadReceipt(@RequestParam("file") MultipartFile file) throws Exception {
        File tempFile = Files.createTempFile("upload_", file.getOriginalFilename()).toFile();
        file.transferTo(tempFile);

        ReceiptResponse response = receiptService.extractData(tempFile);

        tempFile.delete();

        return response;
    }
}

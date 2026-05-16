package com.example.webapptest.service;

import com.example.webapptest.response.ReceiptResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReceiptService {

    @Value("${tesseract.datapath}")
    private String tessDataPath;

    @Autowired
    private RestTemplate restTemplate;

    private final String OLLAMA_URL = "http://localhost:11434/api/generate";

    public ReceiptResponse extractData(File imageFile) {

        System.setProperty("jna.library.path", "C:/Program Files/Tesseract-OCR");

        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(tessDataPath);
        tesseract.setLanguage("eng");

        try {
            String fullText = tesseract.doOCR(imageFile);

            return callOllamaAI(fullText);
        } catch (Exception e) {
            return new ReceiptResponse("Error", "N/A", "0.00", "N/A");
        }
    }

    private ReceiptResponse callOllamaAI(String rawText) {
        String prompt = "Extract data from this receipt text into JSON format. Rules:\n" +
                "1. 'merchant': The store name (usually at the header or footer). Ignore 'Bill To', 'Customer Name', or 'Recipient Names'.\n"
                +
                "2. 'date': The transaction date (format: DD/MM/YYYY). If missing, use 'N/A'.\n" +
                "3. 'total': The final amount paid. Ignore subtotal or tax.\n" +
                "4. 'currency': The currency symbol or code (e.g., USD, IDR).\n" +
                "Return ONLY the raw JSON object. No explanations or think tags.\n" +
                "Text: " + rawText;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-r1:1.5b");
        requestBody.put("prompt", prompt);
        requestBody.put("stream", false);
        requestBody.put("format", "json");

        try {
            Map<String, Object> response = restTemplate.postForObject(OLLAMA_URL, requestBody, Map.class);
            String aiJson = (String) response.get("response");

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(aiJson, ReceiptResponse.class);

        } catch (Exception e) {
            e.printStackTrace();
            return new ReceiptResponse("AI Failed", "N/A", "0.00", "N/A");
        }
    }
}
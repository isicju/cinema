package com.vkatit.cinema.service.ticket;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;

import java.io.File;

@TestComponent
public class ImageOcrServiceTest {


    public static String performOcr(String imagePath) {
        imagePath = "src/main/resources/Ticket-123.jpg";
        File imageFile = new File(imagePath);
        ITesseract instance = new Tesseract();
        instance.setDatapath("src/main/resources");
        try {
            instance.setLanguage("eng");
            String ocrResult = instance.doOCR(imageFile);
            ocrResult = ocrResult.replaceAll("\\n", "");
            return ocrResult;
        } catch (TesseractException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String imagePath = "";
        String recognizedText = performOcr(imagePath);
        if (recognizedText != null) {
            System.out.println("OCR text: ");
            System.out.println(recognizedText);
        } else {
            System.err.println("OCR error");
        }
    }

}
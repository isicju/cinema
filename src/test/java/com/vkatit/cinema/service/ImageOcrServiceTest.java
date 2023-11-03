package com.vkatit.cinema.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.boot.test.context.TestComponent;

import java.io.File;

@TestComponent
public class ImageOcrServiceTest {

    public static String performOcr(String imagePath) {
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

}
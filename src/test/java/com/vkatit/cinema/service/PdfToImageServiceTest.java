package com.vkatit.cinema.service.ticket;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@TestComponent
public class PdfToImageServiceTest {

    public static void convertPdfToJpeg() throws IOException {
        String fileName = "Ticket-123";
        String pdfFilePath = "src/main/resources/" + fileName + ".pdf";
        String outputDirectory = "src/main/resources";
        try (PDDocument document = PDDocument.load(new File(pdfFilePath))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                String outputFileName = String.format(outputDirectory + "/" + fileName + ".jpg", outputDirectory, page + 1);
//                String outputFileName = String.format("%s/page_%1d.jpg", outputDirectory, page + 1);
                ImageIO.write(image, "JPEG", new File(outputFileName));
            }
        }
    }
//
//    public static void main(String[] args) {
//        String pdfFilePath = "";
//        String outputDirectory = "";
//        try {
//            convertPdfToJpeg(pdfFilePath, outputDirectory);
//            System.out.println("Conversion completed successfully");
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.err.println("Conversion error");
//        }
//    }
}

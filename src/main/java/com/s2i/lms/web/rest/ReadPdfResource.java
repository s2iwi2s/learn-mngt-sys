package com.s2i.lms.web.rest;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.s2i.lms.domain.PdfInvoice;
import com.s2i.lms.service.dto.FileDto;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReadPdfResource {
    private final Logger log = LoggerFactory.getLogger(ReadPdfResource.class);

    @PostMapping("/pdf/read")
    public ResponseEntity<?> readPdf(@RequestBody String pdfBase64) {
        PdfInvoice pdfInvoice = getPdfInvoice(pdfBase64);
        return ResponseEntity.ok(pdfInvoice);
    }

    @PostMapping("/pdf/readOcr")
    public ResponseEntity<?> readPdfOCR(@RequestBody String pdfBase64) {
        PdfInvoice pdfInvoice = getPdfInvoiceOcr(pdfBase64);
        return ResponseEntity.ok(pdfInvoice);
    }

    @PostMapping("/pdf/readx")
    public ResponseEntity<?> readPdfx(@RequestBody String base64) {
        Map<String, String> map = new HashMap<>();
        String pdfFileInText = "";
        String invoiceNo = "";
        String invoiceDate = "";
        byte[] bytes = Base64.getDecoder().decode(base64);
        try (PDDocument document = PDDocument.load(new java.io.ByteArrayInputStream(bytes))) {
            document.getClass();
            if (!document.isEncrypted()) {
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                PDFTextStripper tStripper = new PDFTextStripper();

                pdfFileInText = tStripper.getText(document);
                String[] lines = pdfFileInText.split("\\r?\\n");
                List<String> list = Arrays.asList(lines);
                invoiceNo = parse("INVOICE #", list);
                invoiceDate = parse("INVOICE DATE:", list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("pdfFileInText", pdfFileInText);
        map.put("invoiceNo", invoiceNo);
        map.put("invoiceDate", invoiceDate);

        return ResponseEntity.ok(map);
    }

    public PdfInvoice getPdfInvoice(String pdfBase64) {
        String pdfFileInText = "";
        String invoiceNumber = "";
        String invoiceDate = "";
        byte[] bytes = Base64.getDecoder().decode(pdfBase64);
        try (PDDocument document = PDDocument.load(new java.io.ByteArrayInputStream(bytes))) {
            document.getClass();
            if (!document.isEncrypted()) {
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                PDFTextStripper tStripper = new PDFTextStripper();

                pdfFileInText = tStripper.getText(document);
                invoiceNumber = parse(Pattern.quote(
                    "-Invoice Number-") +
                    "(.*?)" +
                    Pattern.quote("-Page 1-"
                    ), pdfFileInText.replaceAll("\\r?\\n", "-"));
                if (invoiceNumber.trim().length() == 0) {
                    String text = pdfFileInText.replaceAll("\\r?\\n", "-");
                    invoiceNumber = parse(Pattern.quote("-INVOICE # ") + "(.*?)" + Pattern.quote("- ATTN:"), text);
                    invoiceDate = parse(Pattern.quote("INVOICE DATE: ") + "(.*?)" + Pattern.quote(" T-CHEK SYSTEMS"), text);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PdfInvoice(pdfFileInText, invoiceNumber, invoiceDate);
    }

    public PdfInvoice getPdfInvoiceOcr(String pdfBase64) {
        String pdfFileInText = "";
        String invoiceNumber = "";
        String invoiceDate = "";
        byte[] bytes = Base64.getDecoder().decode(pdfBase64);
        try (PDDocument document = PDDocument.load(new java.io.ByteArrayInputStream(bytes))) {
            document.getClass();
            if (!document.isEncrypted()) {
                pdfFileInText = extractTextFromScannedDocument(document);
                String text = pdfFileInText.replaceAll("\\r?\\n", "-");
                invoiceNumber = parse(Pattern.quote("-INVOICE # ") + "(.*?)" + Pattern.quote("- ATTN:"), text);
                invoiceDate = parse(Pattern.quote("INVOICE DATE: ") + "(.*?)" + Pattern.quote(" T-CHEK SYSTEMS"), text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PdfInvoice(pdfFileInText, invoiceNumber, invoiceDate);
    }

    private String extractTextFromScannedDocument(PDDocument document) throws IOException, TesseractException {
        // Extract images from file
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();

        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("tessdata");
        tesseract.setLanguage("eng");

        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            // Create a temp image file
//            File tempFile = File.createTempFile("tempfile_" + page, ".png");
//            ImageIO.write(bufferedImage, "png", tempFile);

//            String result = tesseract.doOCR(tempFile);
            String result = tesseract.doOCR(bufferedImage);
            out.append(result);

            // Delete temp file
//            tempFile.delete();

        }

        return out.toString();

    }

    @GetMapping("/pdf/toBase4")
    public ResponseEntity<?> toBase4(String fileName) {
        FileDto fileDto = new FileDto();
        try (PDDocument document = PDDocument.load(new File(fileName))) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);

            byte[] byteAry = baos.toByteArray();
            fileDto.setByteAry(byteAry);

            String base64 = Base64.getEncoder().encodeToString(byteAry);
            fileDto.setBase64(base64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(fileDto);
    }
//    private String getInvoiceNumber(List<String> list) {
//        String invoiceNo = "";
//        Optional<String> first = list.stream().filter(s -> s.contains("INVOICE #")).findFirst();
//        if(first.isPresent()) {
//            String[] tempAry = first.get().trim().split(" ");
//            if(tempAry.length > 2 ) {
//                invoiceNo = tempAry[2];
//            }
//        }
//        return invoiceNo;
//    }
//
//    private String getInvoiceDate(List<String> list) {
//        String invoiceDate = "";
//        Optional<String> first = list.stream().filter(s -> s.contains("INVOICE DATE:")).findFirst();
//        if(first.isPresent()) {
//            String[] tempAry = first.get().trim().split(" ");
//            if(tempAry.length > 2) {
//                invoiceDate = tempAry[2];
//            }
//        }
//        return invoiceDate;
//    }

    private String parse(String regexPattern, String input) {
        log.info("start>=======================================================");
        log.info("Parse using regex");
        log.info("regexPattern: {}", regexPattern);
        log.info("input: {}", input);
        String ret = "";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            log.info("Start index: " + matcher.start());
            log.info(" End index: " + matcher.end() + " ");
            ret = matcher.group(1);
            log.info(" - " + ret);
        }
        log.info("end>=======================================================");
        return ret;
    }

    private String parse(String input, List<String> list) {
        log.info("Parse using split");
        String ret = "";
        Optional<String> first = list.stream().filter(s -> s.contains(input)).findFirst();
        if (first.isPresent()) {
            int inputLenght = input.split(" ").length;
            String[] tempAry = first.get().trim().split(" ");
            if (tempAry.length > inputLenght) {
                ret = tempAry[inputLenght];
            }
        }
        return ret;
    }

    public static void main(String[] args) {
//        String str = "its a string with pattern1 aleatory pattern2 things between pattern1 and pattern2 and sometimes pattern1 pattern2 nothing";
//        str = "TN 38115\r\nInvoice Number\r\n314641821\r\nPage 1\r\nOriginal";
//
//        System.out.println(">>"+str+"<<");
//        Matcher m = Pattern.compile(
//            Pattern.quote("Invoice Number\n")
//                + "(.*?)"
//                + Pattern.quote("\nPage 1")
//        ).matcher(str);
//        while(m.find()){
//            String match = m.group(1);
//            System.out.println(">"+match+"<");
//            //here you insert 'match' into the list
//        }
//        String pattern1 = "hgb";
//        String pattern2 = "|";
//        String text = "sdfjsdkhfkjsdf hgb sdjfkhsdkfsdf |sdfjksdhfjksd sdf sdkjfhsdkf | sdkjfh hgb sdkjfdshfks|";
//
//        Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
//        Matcher m = p.matcher(text);
//        while (m.find()) {
//            System.out.println(m.group(1));
//        }


        String pattern1 = "Invoice Number\\r\\n";
        String pattern2 = "\\r\\nPage 1";
        String text = "TN 38115\r\nInvoice Number\r\n314641821\r\nPage 1\r\nOriginal";
        System.out.println(">>" + text + "<<");
//        List<String> strings = Arrays.asList( text.replaceAll("^.*?"+pattern1, "").split(pattern2 + ".*?("+pattern1+"|$)"));
//        System.out.println( strings);
        Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
        Matcher m = p.matcher(text);
        while (m.find()) {
            System.out.println(m.group(1));
        }
    }
}

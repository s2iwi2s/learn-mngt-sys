package com.s2i.lms.web.rest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.*;

import com.s2i.lms.service.dto.FileDto;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReadPdfResource {
    private final Logger log = LoggerFactory.getLogger(ReadPdfResource.class);

    @PostMapping("/pdf/read")
    public ResponseEntity<?> readPdf(@RequestBody String base64) {
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


    private String parse(String input, List<String> list) {
        String ret = "";
        Optional<String> first = list.stream().filter(s -> s.contains(input)).findFirst();
        if(first.isPresent()) {
            String[] tempAry = first.get().trim().split(" ");
            if(tempAry.length > input.split(" ").length) {
                ret = tempAry[2];
            }
        }
        return ret;
    }


}

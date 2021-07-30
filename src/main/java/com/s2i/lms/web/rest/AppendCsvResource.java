package com.s2i.lms.web.rest;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AppendCsvResource {

	@GetMapping("/csv/append")
	public ResponseEntity<?> append(String invoiceNo, String fileName) {
        String base64 = "";
		try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            List<String[]> csvData = reader.readAll();

            csvData = csvData.stream().map(l -> {
            	List<String> list = Arrays.asList(l);
            	//list.add(invoiceNo);
                List<String> list2 = new ArrayList<>(list);
                list2.add(invoiceNo);
            	return Arrays.copyOf(list2.toArray(), list2.size(),
                        String[].class);
            }).collect(Collectors.toList());

            String[] header = csvData.get(0);
            header[header.length - 1] = "Invoice Number";

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos))) {
                writer.writeAll(csvData);
            }
            base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
        }catch(Exception e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(base64);
	}

    @GetMapping("/csv/toBase4")
    public ResponseEntity<?> toBase4(String fileName) {
        String base64 = "";
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            List<String[]> csvData = reader.readAll();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(baos))) {
                writer.writeAll(csvData);
            }
            base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
        }catch(Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(base64);
    }
}

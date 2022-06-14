package com.imc.demurrage.service.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.util.Base64Utils;

import com.groupdocs.conversion.Converter;
import com.groupdocs.conversion.options.convert.PdfConvertOptions;

public class EmailMessagesConverter {
    
//	<dependency>
//	        <groupId>com.groupdocs</groupId>
//	        <artifactId>groupdocs-conversion</artifactId>
//	        <version>22.3</version> 
//	</dependency>
	public static String convertMsgtoPDF(String base64Data) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(Base64Utils.decodeFromString(base64Data));
		Converter converter = new Converter(bis);
		PdfConvertOptions options = new PdfConvertOptions();

		String base64 = "";
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			converter.convert(baos, options);
			base64 = Base64Utils.encodeToString(baos.toByteArray());
		}
		return base64;
	}
}

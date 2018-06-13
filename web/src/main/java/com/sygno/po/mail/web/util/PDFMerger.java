package com.sygno.po.mail.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

public class PDFMerger {

//	private static final Logger LOGGER = LoggerFactory.getLogger("pdfMergerLogger");
	private static final String PDF_MERGER_PROPERTIES = "src/main/resources/properties/pdfMerger.properties";

	public static void main(String args[]) {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(new File(PDF_MERGER_PROPERTIES)));
			final String pdf1 = props.getProperty("pdf_1");
			final String pdf2 = props.getProperty("pdf_2");
			final String pdfOut = props.getProperty("pdf_out");
			PDFMergerUtility ut = new PDFMergerUtility();
			ut.addSource(pdf1);
			ut.addSource(pdf2);
			ut.setDestinationFileName(pdfOut);
			ut.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
			System.out.println("Success merge files.");
		} catch (IOException e) {
			System.out.println(String.format("Can't merge files. %s", e.getMessage()));
		}
	}
}
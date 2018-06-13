package com.sygno.po.mail.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WordParser {
	private static final String WORD_PARSER_PROPERTIES = "src/main/resources/properties/wordParser.properties";
	private static final String OLD_WORD = "\\bpersonal\\b";
	private static final String NEW_WORD = "public";

	public static void main(String args[]) {
		try {
			Properties props = new Properties();
			props.load(new FileInputStream(new File(WORD_PARSER_PROPERTIES)));
			final String inputDocx = props.getProperty("input");
			final String outputDocx = props.getProperty("output");
			FileInputStream inputStream = new FileInputStream(inputDocx);

			XWPFDocument document = new XWPFDocument(inputStream); // открываем конвертируемый/копируемый файл
			XWPFDocument documentNew = new XWPFDocument();// новый документ, в который переносим всю информацию.

			List<XWPFParagraph> paragraphs = document.getParagraphs();
			for (XWPFParagraph oldParagraph : paragraphs) {
				XWPFParagraph newParagraph = documentNew.createParagraph();
				XWPFRun run = newParagraph.createRun();
				String text = oldParagraph.getParagraphText();
				// поменять все стили
				Pattern p = Pattern.compile(OLD_WORD);
				if (p.matcher(text).find()) {
					Matcher m = p.matcher(text);
					text = (m.replaceAll(NEW_WORD));
				}
				run.setText(text);
			}

			FileOutputStream fileOut2 = new FileOutputStream(outputDocx);
			// Сохраняем получившийся файл
			documentNew.write(fileOut2);
			documentNew.close();
			document.close();
			fileOut2.close();
			System.out.println(String.format("%s was successfully updated to %s.", inputDocx, outputDocx));
		} catch (IOException e) {
			System.out.println(String.format("Something went wrong. %s", e.getMessage()));
		}

	}
}

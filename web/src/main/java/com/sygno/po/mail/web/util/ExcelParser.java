package com.sygno.po.mail.web.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelParser {
	private static final String EXCEL_PARSER_PROPERTIES = "src/main/resources/properties/excelParser.properties";
        private static final String[] MOBILE_TYPES = {"New", "Porting", "Porting with same SIM"};
	
	public static void main(String args[]) {
		try {
			Convert conv = new Convert(); // Класс, в котором будет происходить преобразование
			XSSFWorkbook workBook = null; // документ, который надо конвертировать
			XSSFWorkbook workOutputBook = new XSSFWorkbook();// новый документ, в который переносим всю информацию.
			Properties props = new Properties();
			props.load(new FileInputStream(new File(EXCEL_PARSER_PROPERTIES)));
			final String inputXLS = props.getProperty("input");
			final String outputXLS = props.getProperty("output");
			FileInputStream inputStreamPattern = new FileInputStream(inputXLS);
			workBook = new XSSFWorkbook(inputStreamPattern); // открываем конвертируемый/копируемый файл
			Integer sheetCount = workBook.getNumberOfSheets();
			for (int i = 0; i < sheetCount; i++) {
				XSSFSheet sheet = workBook.getSheetAt(i);
				String sheetName = sheet.getSheetName();
				XSSFSheet newSheet = workOutputBook.createSheet(sheetName);
				conv.convert(sheet, newSheet);
			}
			FileOutputStream fileOut2 = new FileOutputStream(outputXLS);
			// Сохраняем получившийся файл
			workOutputBook.write(fileOut2);
			workOutputBook.close();
			workBook.close();
			fileOut2.close();
			System.out.println(String.format("%s was successfully updated to %s.", inputXLS, outputXLS));
		} catch (IOException e) {
			System.out.println(String.format("Something went wrong. %s", e.getMessage()));
		}

	}
public static void createDropdownList(XSSFSheet worksheet) {
        DataValidationHelper validationHelper = new XSSFDataValidationHelper(worksheet);
        CellRangeAddressList addressList = new CellRangeAddressList(1, 10, 0, 0);
        DataValidationConstraint constraint = validationHelper
                .createExplicitListConstraint(MOBILE_TYPES);
        DataValidation dataValidation = validationHelper.createValidation(constraint, addressList);
        dataValidation.setSuppressDropDownArrow(true);
        worksheet.addValidationData(dataValidation);
    }
}

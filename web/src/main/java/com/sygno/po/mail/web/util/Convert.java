package com.sygno.po.mail.web.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;

public class Convert {
	private static final String OLD_WORD = "\\bpersonal\\b";
	private static final String NEW_WORD = "public";
	private int _numColumn = 0;

	public void convert(Sheet sheetOld, Sheet sheetNew) {
		// Задаём тут папраметры необходимые для комментариев на листе
		CreationHelper factory = sheetNew.getWorkbook().getCreationHelper();
		Drawing drawing = sheetNew.createDrawingPatriarch();
		ClientAnchor anchor = null;
		Comment comment = null;
		setSheetOptions(sheetOld, sheetNew);
		// Работаем со строками
		XSSFRow rowNew;
		for (Row row : sheetOld) {
			rowNew = (XSSFRow) sheetNew.createRow(row.getRowNum());
			if (rowNew != null)
				// Обращаемся к методу конвертирования строк
				convert((XSSFRow) row, rowNew, factory, drawing, anchor, comment);
		}
		// задаём ширину и высоту для колонок
		for (int i = 0; i < this._numColumn; i++) {
			sheetNew.setColumnWidth(i, sheetOld.getColumnWidth(i));
			sheetNew.setColumnHidden(i, sheetOld.isColumnHidden(i));
		}
		// задаём объединение ячеек.
		for (int i = 0; i < sheetOld.getNumMergedRegions(); i++) {
			CellRangeAddress merged = sheetOld.getMergedRegion(i);
			sheetNew.addMergedRegion(merged);
		}
	}

	private void setSheetOptions(Sheet sheetOld, Sheet sheetNew) {
		sheetNew.setDisplayFormulas(sheetOld.isDisplayFormulas());
		sheetNew.setDisplayGridlines(sheetOld.isDisplayGridlines());
		sheetNew.setDisplayGuts(sheetOld.getDisplayGuts());
		sheetNew.setDisplayRowColHeadings(sheetOld.isDisplayRowColHeadings());
		sheetNew.setDisplayZeros(sheetOld.isDisplayZeros());
		sheetNew.setFitToPage(sheetOld.getFitToPage());
		sheetNew.setForceFormulaRecalculation(sheetOld.getForceFormulaRecalculation());
		sheetNew.setHorizontallyCenter(sheetOld.getHorizontallyCenter());
		sheetNew.setPrintGridlines(sheetNew.isPrintGridlines());
		sheetNew.setRightToLeft(sheetNew.isRightToLeft());
		sheetNew.setRowSumsBelow(sheetNew.getRowSumsBelow());
		sheetNew.setRowSumsRight(sheetNew.getRowSumsRight());
		sheetNew.setVerticallyCenter(sheetOld.getVerticallyCenter());
	}

	// Преобразование строк в apach POI

	private void convert(XSSFRow rowOld, XSSFRow rowNew, CreationHelper factory, Drawing drawing, ClientAnchor anchor,
			Comment comment) {
		XSSFCell cellNew;
		rowNew.setHeight(rowOld.getHeight());
		for (Cell cell : rowOld) {
			// Создаём новые ячейки в новом-документе по числу и расположению в старом
			cellNew = rowNew.createCell(cell.getColumnIndex(), cell.getCellType());
			if (cellNew != null)
				// Обращаемся к методу конвертирования ячеек строк
				this.convertCell((XSSFCell) cell, cellNew, factory, drawing, anchor, comment);
		}
		this._numColumn = Math.max(this._numColumn, rowOld.getLastCellNum());
	}

	// Конвертирование ячеек в apach POI

	private void convertCell(XSSFCell cellOld, XSSFCell cellNew, CreationHelper factory, Drawing drawing,
			ClientAnchor anchor, Comment comment) {
		cellNew.setCellComment(cellOld.getCellComment());
		CellStyle newCellStyle = cellNew.getSheet().getWorkbook().createCellStyle();

		// Создаём в новом документе тот же стиль ячейкам, что был в старом документе
		CellStyle cellStyle = cellOld.getCellStyle();
		if (cellStyle != null) {
	        newCellStyle.cloneStyleFrom(cellStyle);
		}
		newCellStyle.setWrapText(true);
		cellNew.setCellStyle(newCellStyle);

		switch (cellOld.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			cellNew.setCellValue(cellOld.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			cellNew.setCellValue(cellOld.getErrorCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			cellNew.setCellValue(cellOld.getCellFormula());
			//при необходимости заменить "personal" на "public" и здесь
			break;
		case Cell.CELL_TYPE_NUMERIC:
			cellNew.setCellValue(cellOld.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_STRING:
			String stringCellValue = cellOld.getStringCellValue();
			Pattern p =Pattern.compile(OLD_WORD);
			if (p.matcher(stringCellValue).find()) {
				Matcher m = p.matcher(stringCellValue);
				cellNew.setCellValue(m.replaceAll(NEW_WORD));
			} else {
				cellNew.setCellValue(stringCellValue);
			}
			break;
		default:
			System.out.println("Unknown cell type " + cellOld.getCellType());
		}
		// Вставляем комментарии
		if (cellOld.getCellComment() != null) {
			if (cellOld.getCellComment().getAuthor() != null) {
				anchor = factory.createClientAnchor();
				comment = drawing.createCellComment(anchor);

				comment.setAuthor(cellOld.getCellComment().getAuthor());
				RichTextString str = factory.createRichTextString(comment.getAuthor());
				comment.setString(str);
				cellNew.setCellComment(comment);

			}
		}
	}
}

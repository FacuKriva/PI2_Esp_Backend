package com.digital.money.msvc.api.account.utils;

import com.digital.money.msvc.api.account.model.dto.TransactionGetDto;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedHashMap;

public class GeneratorPdf {

    // List to hold all Students
    private TransactionGetDto transactionSuccessful;

    public void generate(HttpServletResponse response) throws DocumentException, IOException {

        // Creating the Object of Document
        Document document = new Document(PageSize.A4);
        // Getting instance of PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());
        // Opening the created document to modify it
        document.open();

        // Creating font
        // Setting font style and size
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(25);
        fontTiltle.setColor(Color.BLACK);

        // Creating paragraph
        Paragraph paragraph = new Paragraph("Successful Transfer", fontTiltle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        // Adding the created paragraph in document
        document.add(paragraph);
        //document.addHeader("title", "Successful Transfer");

        // Creating a table of 3 columns
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{3, 3});
        table.setSpacingBefore(5);

        // Create Table Cells for table header
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(CMYKColor.CYAN);
        cell.setPadding(5);

        // Creating font
        // Setting font style and size
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 18);
        font.setColor(CMYKColor.BLACK);

        // Adding headings in the created table cell/ header
        // Adding Cell to table
        table.addCell("ID");
        table.addCell(String.valueOf(transactionSuccessful.getTransactionId()));
        table.addCell("Amount");
        table.addCell(String.valueOf(transactionSuccessful.getAmount()));
        table.addCell("Date");
        table.addCell(String.valueOf(transactionSuccessful.getRealizationDate()));
        table.addCell("Description");
        table.addCell(transactionSuccessful.getDescription());
        table.addCell("FromD");
        table.addCell(transactionSuccessful.getFromCvu());
        table.addCell("To");
        table.addCell(transactionSuccessful.getToCvu());
        table.addCell("Type");
        table.addCell(transactionSuccessful.getType().name());
        table.addCell("Account");
        table.addCell(transactionSuccessful.getAccount().getAlias());
        table.addCell("Available Balance");
        table.addCell(String.valueOf(transactionSuccessful.getAccount().getAvailableBalance()));

        document.add(table);
        document.close();
    }
}

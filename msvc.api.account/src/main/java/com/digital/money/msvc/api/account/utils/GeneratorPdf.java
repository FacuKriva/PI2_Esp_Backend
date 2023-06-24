package com.digital.money.msvc.api.account.utils;

import com.digital.money.msvc.api.account.model.dto.TransactionGetDto;
import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

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
        fontTiltle.setSize(20);

        // Creating paragraph
        Paragraph paragraph = new Paragraph("List Of Students", fontTiltle);
        // Aligning the paragraph in document
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        // Adding the created paragraph in document
        document.add(paragraph);

        // Creating a table of 3 columns
        PdfPTable table = new PdfPTable(3);

        // Setting width of table, its columns and spacing
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{3, 3, 3});
        table.setSpacingBefore(5);

        // Create Table Cells for table header
        PdfPCell cell = new PdfPCell();

        // Setting the background color and padding
        cell.setBackgroundColor(CMYKColor.MAGENTA);
        cell.setPadding(5);

        // Creating font
        // Setting font style and size
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        font.setColor(CMYKColor.BLACK);

        // Adding headings in the created table cell/ header
        // Adding Cell to table
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Amount", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Date", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("From", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("To", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Type", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Account", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Available Balance", font));
        table.addCell(cell);

        table.addCell(String.valueOf(transactionSuccessful.getTransactionId()));
        table.addCell(String.valueOf(transactionSuccessful.getAmount()));
        table.addCell(String.valueOf(transactionSuccessful.getRealizationDate()));
        table.addCell(transactionSuccessful.getDescription());
        table.addCell(transactionSuccessful.getFromCvu());
        table.addCell(transactionSuccessful.getToCvu());
        table.addCell(transactionSuccessful.getType().name());
        table.addCell(transactionSuccessful.getAccount().getAlias());
        table.addCell(String.valueOf(transactionSuccessful.getAccount().getAvailableBalance()));

        document.add(table);
        document.close();
    }
}

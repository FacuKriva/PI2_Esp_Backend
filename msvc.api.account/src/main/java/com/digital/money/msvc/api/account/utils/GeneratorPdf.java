package com.digital.money.msvc.api.account.utils;

import com.digital.money.msvc.api.account.model.dto.TransactionGetDto;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;

import java.awt.*;
import java.io.IOException;

@Setter
public class GeneratorPdf {

    // List to hold all Students
    private TransactionGetDto transactionSuccessful;

    public void generate(HttpServletResponse response) throws DocumentException, IOException {

        // Creating the Object of Document
        Document document = new Document(PageSize.A6);
        // Getting instance of PdfWriter
        PdfWriter.getInstance(document, response.getOutputStream());
        // Opening the created document to modify it
        document.open();

        // Creating font
        // Setting font style and size
        Font fontTiltle = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        fontTiltle.setSize(16);
        fontTiltle.setColor(Color.BLACK);

        // Creating paragraph
        Paragraph paragraph = new Paragraph("Successful Transfer", fontTiltle);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        // Adding the created paragraph in document
        document.add(paragraph);

        // Creating a table of 3 columns
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{3, 3});
        table.setSpacingBefore(5);

        // Create Table Cells for table header
        PdfPCell cell = new PdfPCell();
        //cell.setBackgroundColor(CMYKColor.CYAN);
        cell.setPadding(4);
        cell.setIndent(4f);

        // Creating font
        // Setting font style and size
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8);
        font.setColor(CMYKColor.BLACK);

        // Adding headings in the created table cell/ header
        // Adding Cell to table
        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase(String.valueOf(transactionSuccessful.getTransactionId()), font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Amount", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase(String.valueOf(transactionSuccessful.getAmount()), font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Date", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase(String.valueOf(transactionSuccessful.getRealizationDate()), font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase(transactionSuccessful.getDescription(), font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("From", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase(transactionSuccessful.getFromCvu(), font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("To", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase(transactionSuccessful.getToCvu(), font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Type", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase(transactionSuccessful.getType().name(), font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Account", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase(transactionSuccessful.getAccount().getAlias(), font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Available Balance", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase(String.valueOf(transactionSuccessful.getAccount().getAvailableBalance()), font));
        table.addCell(cell);

        document.add(table);
        document.close();
    }
}

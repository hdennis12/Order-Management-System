package presentation;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import main.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.stream.Stream;

import model.*;

/**
 * Class for input/output
 */
public class Presentation {

    /**
     * Reads an input and provides and output
     * @param connection connection
     * @param path path for commands
     */
    public static void executeFromInputFile(Connection connection, String path) {
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            Model m = new Model();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
                m.executeOperation(connection, data);
            }
            myReader.close();
        } catch (
                FileNotFoundException | SQLException | DocumentException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Operations with headers
     * @param table
     * @param nr
     * @param s
     */
    private void addTableHeader(PdfPTable table, int nr, String s) {
        if (s.equals("client")) {
            Stream.of("name", "address").forEach(columnTitle -> {
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(columnTitle));
                table.addCell(header);
            });
        } else if (s.equals("product")) {
            Stream.of("name", "quantity", "price").forEach(columnTitle -> {
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(columnTitle));
                table.addCell(header);
            });
        } else if (s.equals("order")) {
            Stream.of("client", "product", "quantity").forEach(columnTitle -> {
                PdfPCell header = new PdfPCell();
                header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(columnTitle));
                table.addCell(header);
            });
        }
    }

    /**
     * Operations with rows
     * @param table
     * @param rs
     * @param s
     * @throws SQLException
     */
    private void addRows(PdfPTable table, ResultSet rs, String s) throws SQLException {
        if (s.equals("client")) {
            while (rs.next()) {
                table.addCell(rs.getString("name"));
                table.addCell(rs.getString("address"));
            }
        } else if (s.equals("product")) {
            while (rs.next()) {
                table.addCell(rs.getString("name"));
                table.addCell(rs.getString("quantity"));
                table.addCell(rs.getString("price"));
            }
        } else if (s.equals("order")) {
            while (rs.next()) {
                table.addCell(rs.getString("client"));
                table.addCell(rs.getString("product"));
                table.addCell(rs.getString("quantity"));
            }
        }

    }

    /**
     * Creates specific document depending on parameters
     * @param rs
     * @param nr
     * @param s
     * @throws FileNotFoundException
     * @throws DocumentException
     * @throws SQLException
     */
    public void createDocument(ResultSet rs, int nr, String s) throws FileNotFoundException, DocumentException, SQLException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("PDF" + Main.pdf_nr + ".pdf"));
        Main.pdf_nr++;

        document.open();
        PdfPTable table = new PdfPTable(nr);

        addTableHeader(table, nr, s);
        addRows(table, rs, s);
        //addCustomRows(table);

        document.add(table);
        document.close();
    }

    /**
     * For successful orders
     * @param con
     * @param str
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public void generateBill(Connection con, String[] str) throws FileNotFoundException, DocumentException {
        String message1 = "Clientul cu numele " + str[1] + " " + str[2] + " comanda " + str[4] + " " + str[3];
        Statement stmt = null;
        double pret = 0;
        String query = "select price from " + "products where name = \"" + str[3] + "\"";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next())
                pret = rs.getDouble("price");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        double total = Double.parseDouble(str[4]) * pret;
        String message2 = "TOTAL PLATA: " + str[4] + "x " + pret + " = " + total;

        Document document = new Document();
        Paragraph paragraph1 = new Paragraph();
        Paragraph paragraph2 = new Paragraph();
        PdfWriter.getInstance(document, new FileOutputStream("PDF" + Main.pdf_nr + ".pdf"));
        Main.pdf_nr++;

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk chunk1 = new Chunk(message1, font);
        Chunk chunk2 = new Chunk(message2, font);

        paragraph1.add(chunk1);
        paragraph2.add(chunk2);
        document.add(paragraph1);
        document.add(paragraph2);
        document.close();
    }

    /**
     * Used when there are not enough products in stock
     * @param str
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public void generateUnderStockPDF(String[] str) throws FileNotFoundException, DocumentException {
        String message = "Nu sunt destule " + str[3] + " in stoc pentru comanda lui " + str[1] + " " + str[2];
        Document document = new Document();
        Paragraph paragraph = new Paragraph();
        PdfWriter.getInstance(document, new FileOutputStream("PDF" + Main.pdf_nr + ".pdf"));
        Main.pdf_nr++;
        document.open();

        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk chunk = new Chunk(message, font);
        paragraph.add(chunk);
        document.add(paragraph);
        document.close();
    }
}

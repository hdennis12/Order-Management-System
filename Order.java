package model;

import com.itextpdf.text.DocumentException;
import connection.ConnectionControl;
import presentation.Presentation;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class for operations on table "orders"
 */
public class Order {

    /**
     * Determines if the operation is Report and if it has to create additional bill/under stock files too
     * @param con connection
     * @param str split commands string
     * @throws SQLException
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public static void detOperation(Connection con, String[] str) throws SQLException, FileNotFoundException, DocumentException {
        String q = "";
        if (str[0].equals("Report"))
            Order.viewTable(con);
        else {
            int newQuantity = Order.checkValidity(con, str);
            Presentation p = new Presentation();

            if (newQuantity >= 0) {
                q += "insert into orders values(\"" + str[1] + str[2] + "\", \"" + str[3] + "\", " + str[4] + ")";
                Order.insertOrder(con, q);
                Product.updateStock(con, str, newQuantity);
                p.generateBill(con, str);
            }
            else
                p.generateUnderStockPDF(str);
        }

    }

    /**
     * An exception class used to view orders table after establishing a connection
     *
     * @param con the connection
     * @throws SQLException
     */
    public static void viewTable(Connection con) throws SQLException {

        Statement stmt = null;
        String query = "select client, product, quantity from " + "orders";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Presentation p = new Presentation();
            p.createDocument(rs, 3, "order");
        } catch (SQLException e) {
            System.out.println(e);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                ConnectionControl.closeStatement(stmt);
            }
        }
    }

    /**
     * inserts an order in the database
     * @param con
     * @param q
     * @throws SQLException
     */
    public static void insertOrder(Connection con, String q) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(q);
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (stmt != null) {
                ConnectionControl.closeStatement(stmt);
            }
        }

    }

    /**
     * verifies if the order can be realized
     * @param con
     * @param str
     * @return
     * @throws SQLException
     */
    private static int checkValidity(Connection con, String[] str) throws SQLException {
        int request = Integer.parseInt(str[4]);
        for (String val : str) {
            System.out.println(val);
        }
        int newQuantity = -1;
        int quantity = 0;
        Statement stmt = null;
        String query = "select * from products where name = \"" + str[3] + "\"";

        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                quantity = rs.getInt("quantity");
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            if (stmt != null) {
                ConnectionControl.closeStatement(stmt);
            }
        }
        int stock = quantity;

        if (request <= stock)
            newQuantity = stock - request;
        return newQuantity;
    }


}

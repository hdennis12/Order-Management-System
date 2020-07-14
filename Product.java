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
 * Class for operations on table "products"
 */
public class Product {
    /**
     * Determines operation (insert, report or delete)
     * @param con connection
     * @param str split commands string
     * @throws SQLException
     */
    public static void detOperation(Connection con, String[] str) throws SQLException {
        String q = "";
        for (String val : str)
            if (val.equals("Insert")) {
                Statement stmt = null;
                String query = "select quantity from products where name = \"" + str[2] + "\"";
                try {
                    stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (!rs.next()) {
                        q += "insert into products values(\"" + str[2] + "\", " + str[3] + ", " + str[4] + ")";
                        Product.insertProduct(con, q);
                    } else {
                        String quantity = rs.getString("quantity");
                        int quant = Integer.parseInt(quantity);
                        int ins = Integer.parseInt(str[3]);
                        int rez = quant + ins;
                        Product.updateStock(con, str, rez);
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                } finally {
                    if (stmt != null) {
                        ConnectionControl.closeStatement(stmt);
                    }
                }
            } else if (val.equals("Report"))
                Product.viewTable(con);
            else if (val.equals("Delete")) {
                q += "delete from products where name = \"" + str[2] + "\"";
                Product.deleteProduct(con, q);
            }
    }

    /**
     * An exception class used to view a product table after establishing a connection
     *
     * @param con the connection
     * @throws SQLException
     */
    public static void viewTable(Connection con) throws SQLException {

        Statement stmt = null;
        String query = "select name, quantity, price from " + "products";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Presentation p = new Presentation();
            p.createDocument(rs, 3, "product");
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

    public static void insertProduct(Connection con, String q) throws SQLException {
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

    public static void deleteProduct(Connection con, String q) throws SQLException {
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

    public static void updateStock(Connection con, String[] str, int newQuantity) throws SQLException {
        Statement stmt = null;
        String q = "update products set quantity = " + newQuantity + " where name = \"" + str[2] + "\"";
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
}

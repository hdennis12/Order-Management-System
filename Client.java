package model;

import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import presentation.*;
import connection.*;

/**
 * Class for operations on table "clients"
 */
public class Client {
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
                q += "insert into clients values(\"" + str[2] + " " + str[3] + "\", \"" + str[4] + "\")";
                Client.insertClient(con, q);
            }
            else if(val.equals("Report"))
                Client.viewTable(con);
            else if(val.equals("Delete"))
            {
                q += "delete from clients where name = \"" + str[2] + " " + str[3] + "\" and address = \"" + str[4] + "\"";
                Client.deleteClient(con, q);
            }
    }

    /**
     * An exception class used to view client table after establishing a connection
     *
     * @param con the connection
     * @throws SQLException
     */
    public static void viewTable(Connection con) throws SQLException {

        Statement stmt = null;
        String query = "select name, address from " + "clients";
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            Presentation p = new Presentation();
            p.createDocument(rs, 2, "client");
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
     * inserts a client in the database
     * @param con
     * @param q
     * @throws SQLException
     */
    public static void insertClient(Connection con, String q) throws SQLException {
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
     * deletes a client from the database
     * @param con
     * @param q
     * @throws SQLException
     */
    public static void deleteClient(Connection con, String q) throws SQLException {
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
}

package main;
//import connection.ConnectionFactory;

import com.itextpdf.text.DocumentException;
import connection.ConnectionControl;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

import presentation.*;

public class Main {
    public static int pdf_nr = 0;

    /**
     * main.Main program containing a few instructions that cover the whole functionality of the program
     * @param args
     * @throws SQLException
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public static void main(String[] args) throws SQLException, FileNotFoundException, DocumentException {
        ConnectionControl c = new ConnectionControl();
        Connection connection = c.getConnection();

        String path = "commands.txt";
        Presentation.executeFromInputFile(connection, path);












    }


}















    /* -------------------------
            import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

    public class Student {
        private int id;
        private String name;
        private String address;
        private String email;
        private int age;

        private final static String findStatementString = "Select * from student where id = 5";

        public static Student findById(int studentId) {
            Student toReturn = null;

            Connection dbConnection = ConnectionFactory.getConnection();
            PreparedStatement findStatement = null;
            ResultSet rs = null;

            try {
                findStatement = dbConnection.prepareStatement(findStatementString);
                findStatement.setLong(1, studentId);
                rs = findStatement.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            toReturn = rs.
            return toReturn;
        }
    }


    --------------------------------------*/




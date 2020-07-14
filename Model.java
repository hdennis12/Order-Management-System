package model;

import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.sql.*;


/**
 * Class that guides the execution of the program (calling specific methods for every command)
 */
public class Model {

    /**
     * Method for finding which class is meant to execute the operation given as parameter
     * @param con - connection
     * @param s - operation
     * @throws SQLException
     */
    public void executeOperation(Connection con, String s) throws SQLException, FileNotFoundException, DocumentException {
        boolean client = false, product = false, order = false;
        String[] str = s.split("(:)?(,)? ");

        /*for(String val : str) {
            System.out.println(val);
        }*/
        String q = "";
        for(String val : str)
            if(val.equals("model.Client") || val.equals("client"))
                client = true;
            else if(val.equals("model.Product") || val.equals("product"))
                product = true;
            else if(val.equals("model.Order") || val.equals("order"))
                order = true;
        if(client)
        {
            Client.detOperation(con, str);
        }
        else if(product)
        {
            Product.detOperation(con, str);
        }
        else if(order)
        {
            Order.detOperation(con, str);
        }

    }

}

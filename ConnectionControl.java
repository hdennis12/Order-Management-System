package connection;//package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.logging.Logger;
import java.sql.*;

/**
 * Class for Connection management
 */
public class ConnectionControl {
    public static final Logger LOGGER = Logger.getLogger(ConnectionControl.class.getName());
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DBURL = "jdbc:mysql://localhost:3306/warehouse";
    public static final String USER = "root";
    public static final String PASS = "rootpass";

    /**
     * Connection provider
     * @return - connection
     */
    public Connection getConnection() {
        Connection c = null;

        try{

            Class.forName(DRIVER);
            c = DriverManager.getConnection(DBURL, USER, PASS);
        }
        catch(Exception e){
            System.out.println(e);
        }

        return c;
    }

    /**
     * Closes the connection
     * @param statement
     * @throws SQLException
     */
    public static void closeStatement(Statement statement) throws SQLException {
        if(statement != null)
            statement.close();
    }



}

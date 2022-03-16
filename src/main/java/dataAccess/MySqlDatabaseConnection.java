package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//Mit dieser Klasse wird eine DB-Verbindung aufgebaut.

public class MySqlDatabaseConnection {
    private static Connection con = null;

    private MySqlDatabaseConnection() {

    }

    public static Connection getConnection(
            String url, String user, String pwd) throws ClassNotFoundException, SQLException {
        if (con != null) {
            return con;
        } else {
            Class.forName("com.mysql.cj.jdbc.Driver"); //überprüfen ob der Treiber vorhanden
            con = DriverManager.getConnection(url, user, pwd); //Verbindung aufbauen
            return con;
        }
    }
}

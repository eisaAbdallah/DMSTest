import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author User
 */
public class DBCon {

    Connection connect = null;

    public Connection Connenctivity() throws ClassNotFoundException {
        String url = "jdbc:mysql://localhost:3306/employees";

        String username = "emp";
        String password = "123";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/employees?useUnicode=yes&characterEncoding=UTF-8&useSSL=false", "emp", "123");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return connect;

    }

}

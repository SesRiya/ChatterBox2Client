package com.example.chatterboxclient.utils;

import java.sql.*;

public class DbUtils {
    public static Connection connectToDb() throws SQLException, ClassNotFoundException {
        String name = "root";
        String password = "";
        String url = "jdbc:mysql://localhost/chatterbox";
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection connection= DriverManager.getConnection(url,name,password);

        return connection;
    }

    public static ResultSet executeQuery(Connection connection, String query) throws SQLException {
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery(query);

        return rs;
    }

    public static int updateQuery(Connection connection, String query) throws SQLException {
        Statement s = connection.createStatement();
        return s.executeUpdate(query);
    }

}

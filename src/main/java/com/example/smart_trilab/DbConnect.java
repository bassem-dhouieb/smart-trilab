package com.example.smart_trilab;
import java.sql.*;

public class DbConnect {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 3306;
    private static final String DB_NAME = "smart_trilab";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "monastirM1923l*";
    private static Connection connection;

public static Connection getConnect(){
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s",HOST,PORT,DB_NAME),USERNAME,PASSWORD);
        System.out.println("connect");
    } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
    }

    return connection;

}

}

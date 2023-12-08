package com.fatykhov.restapp.dbConfigAndConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {
    public static Connection getConnection() {
        DbConfig config = new DbConfig();

        try {
            // C помощью рефлексии подгружаем класс org.postgresql.Driver и удостоверяемся в том, что
            // этот класс загружен в нашу оперативную память.
            Class.forName(config.getDriver());
            return DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database");
        }
    }
}

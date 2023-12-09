package com.fatykhov.restapp.dbConfigAndConnection;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {
    public Connection getConnection() {
        DbConfig config = new DbConfig();

        try {
            Class.forName(config.getDriver());
            return DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database");
        }
    }
}

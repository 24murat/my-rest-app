package com.fatykhov.restapp.dbConfig;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPool {
    private static final String CONFIG_FILE = "database.properties";
    private static final Properties properties;

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        try (InputStream input = ConnectionPool.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + CONFIG_FILE);
                System.exit(1);
            }
            properties = new Properties();
            properties.load(input);

            config.setDriverClassName(properties.getProperty("driver"));
            config.setJdbcUrl(properties.getProperty("url"));
            config.setUsername(properties.getProperty("username"));
            config.setPassword(properties.getProperty("password"));
            ds = new HikariDataSource(config);
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration from " + CONFIG_FILE, e);
        }
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}

package com.fatykhov.restapp.dbConfigAndConnection;

import java.io.InputStream;
import java.util.Properties;

public class DbConfig {
    private static final String CONFIG_FILE = "database.properties";
    private Properties properties;

    public DbConfig() {
        this.properties = new Properties();
        loadConfig();
    }

    private void loadConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + CONFIG_FILE);
                return;
            }

            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDriver() {
        return properties.getProperty("driver");
    }

    public String getUrl() {
        return properties.getProperty("url");
    }

    public String getUsername() {
        return properties.getProperty("username");
    }

    public String getPassword() {
        return properties.getProperty("password");
    }
}

package net.mat0u5.serverresourcepackreload.config;

import net.mat0u5.serverresourcepackreload.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ConfigManager {

    private Properties properties = new Properties();
    private String filePath;

    public ConfigManager(String filePath) {
        this.filePath = filePath;
        createFileIfNotExists();
        loadProperties();
    }

    private void createFileIfNotExists() {
        File configFile = new File(filePath);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (OutputStream output = new FileOutputStream(configFile)) {
                    // Add default properties or leave it empty
                    properties.setProperty("resourcepack.url","");
                    properties.setProperty("resourcepack.sha1","");
                    properties.store(output, null);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadProperties() {
        try (InputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
        try (OutputStream output = new FileOutputStream(filePath)) {
            properties.store(output, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

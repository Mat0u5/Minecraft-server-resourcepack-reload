package net.mat0u5.serverresourcepackreload.utils;

import net.minecraft.server.MinecraftServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class FileManager {
    private static String resourcePackURL = "";
    private static String resourcePackSHA1 = "";
    private static String getTrueServerProperties(MinecraftServer server) {
        Path path = server.getRunDirectory().toPath().resolve("server.properties");
        try {
            String everything = "";
            BufferedReader br = new BufferedReader(new FileReader(path.toString()));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                everything = sb.toString();
            } finally {
                br.close();
            }
            return everything;
        } catch (IOException e1) {}
        return null;
    }
    public static void updateStoredServerRPInfo(MinecraftServer server) {
        String[] serverProperties = getTrueServerProperties(server).split("\r\n");
        for (String line : serverProperties) {
            if (line.startsWith("resource-pack=")) resourcePackURL = line.replaceFirst("resource-pack=","");
            if (line.startsWith("resource-pack-sha1=")) resourcePackSHA1 = line.replaceFirst("resource-pack-sha1=","");
            if (!resourcePackURL.startsWith("https://") && !resourcePackURL.startsWith("http://")) {
                resourcePackURL = "https://"+resourcePackURL;
            }
        }
    }
    public static String getRPURL() {
        return resourcePackURL;
    }
    public static String getRPSHA1() {
        return resourcePackSHA1;
    }
}

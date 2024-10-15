package net.mat0u5.serverresourcepackreload;

import net.fabricmc.api.ModInitializer;

import net.mat0u5.serverresourcepackreload.config.ConfigManager;
import net.mat0u5.serverresourcepackreload.utils.ModRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main implements ModInitializer {
	public static final String MOD_ID = "serverresourcepackreload";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ConfigManager config;
	public static String commitMessage = "";

	@Override
	public void onInitialize() {
		config = new ConfigManager("./config/"+MOD_ID+".properties");
		System.out.println("config_resourcepack.url: " + Main.config.getProperty("resourcepack.url"));
		System.out.println("config_resourcepack.sha1: " + Main.config.getProperty("resourcepack.sha1"));
		ModRegistries.registerModStuff();
		LOGGER.info("Initializing Server Resource Pack Reload...");
	}
}
package net.mat0u5.serverresourcepackreload;

import net.fabricmc.api.ModInitializer;

import net.mat0u5.serverresourcepackreload.utils.ModRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
	public static final String MOD_ID = "serverresourcepackreload";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModRegistries.registerModStuff();
		LOGGER.info("Initializing Server Resource Pack Reload...");
	}
}
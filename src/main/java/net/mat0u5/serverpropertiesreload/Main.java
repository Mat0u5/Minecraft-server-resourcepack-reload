package net.mat0u5.serverpropertiesreload;

import net.fabricmc.api.ModInitializer;

import net.mat0u5.serverpropertiesreload.utils.ModRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
	public static final String MOD_ID = "serverpropertiesreload";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModRegistries.registerModStuff();
		LOGGER.info("Initializing...");
	}
}
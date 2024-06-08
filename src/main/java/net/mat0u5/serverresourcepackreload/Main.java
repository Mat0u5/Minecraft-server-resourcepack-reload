package net.mat0u5.serverresourcepackreload;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.mat0u5.serverresourcepackreload.events.Events;
import net.mat0u5.serverresourcepackreload.utils.ModRegistries;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.mat0u5.serverresourcepackreload.utils.FileManager.updateStoredServerRPInfo;

public class Main implements ModInitializer {
	public static final String MOD_ID = "serverresourcepackreload";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModRegistries.registerModStuff();
		LOGGER.info("Initializing Server Resource Pack Reload...");
	}
}
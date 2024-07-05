package net.mat0u5.serverresourcepackreload.events;



import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.mat0u5.serverresourcepackreload.Main;
import net.mat0u5.serverresourcepackreload.command.Resourcepack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class Events {

    private static final int RESOURCE_PACK_UPDATE_CHECK_INTERVAL_TICKS = 20 * 60; // 60 seconds in ticks
    private static int tickCounter = 0;

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> onPlayerJoin(server, handler.getPlayer()));
        ServerTickEvents.END_SERVER_TICK.register((server) -> onServerTick(server));
    }

    private static void onPlayerJoin(MinecraftServer server, ServerPlayerEntity player) {
        //Sends the message that a new RP version is available
        Main.config.loadProperties();
        MinecraftServer.ServerResourcePackProperties rp = Resourcepack.getServerResourcePackProperties(server);
        if (!rp.url().equalsIgnoreCase(Main.config.getProperty("resourcepack.url")) || !rp.hash().equalsIgnoreCase(Main.config.getProperty("resourcepack.sha1"))) {
            if (player.hasPermissionLevel(2)) Resourcepack.sendNewRPMessage(player);
        }
    }

    private static void onServerTick(MinecraftServer server) {
        tickCounter++;
        if (tickCounter >= RESOURCE_PACK_UPDATE_CHECK_INTERVAL_TICKS) {
            tickCounter = 0;
            Main.config.loadProperties();
            Resourcepack.checkResourcepackUpdate(server);
        }
    }
}

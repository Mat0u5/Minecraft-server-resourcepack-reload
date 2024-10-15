package net.mat0u5.serverresourcepackreload.events;



import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.mat0u5.serverresourcepackreload.Main;
import net.mat0u5.serverresourcepackreload.command.Resourcepack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class Events {

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> onPlayerJoin(server, handler.getPlayer()));
    }

    private static void onPlayerJoin(MinecraftServer server, ServerPlayerEntity player) {
        try {
            //Sends the message that a new RP version is available
            Main.config.loadProperties();
            MinecraftServer.ServerResourcePackProperties rp = Resourcepack.getServerResourcePackProperties(server);
            if (rp == null) return;
            if (!rp.url().equalsIgnoreCase(Main.config.getProperty("resourcepack.url")) || !rp.hash().equalsIgnoreCase(Main.config.getProperty("resourcepack.sha1"))) {
                Resourcepack.sendNewRPMessage(player, Main.commitMessage);
            }
        }catch(Exception e){}
    }
}

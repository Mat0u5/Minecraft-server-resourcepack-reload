package net.mat0u5.serverresourcepackreload.events;



import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.mat0u5.serverresourcepackreload.command.Resourcepack;
import net.mat0u5.serverresourcepackreload.utils.FileManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class Events {

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> onPlayerJoin(server, handler.getPlayer()));
    }

    private static void onPlayerJoin(MinecraftServer server, ServerPlayerEntity player) {
        // Custom code to run when a player joins the server
        FileManager.updateStoredServerRPInfo(server);
        Resourcepack.reloadLatestResourcePack(player);
    }
}

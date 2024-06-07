package net.mat0u5.serverresourcepackreload.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.server.MinecraftServer.ServerResourcePackProperties;

import java.util.Collection;
import java.util.Optional;

public class Properties {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("resource-pack")
                .then(CommandManager.literal("reload").executes(Properties::reload)));
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("resource-pack")
                .then(CommandManager.literal("reloadForAllPlayers").executes(Properties::reload)));
    }
    public static int reload(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        final ServerCommandSource source = context.getSource();
        final PlayerEntity self = source.getPlayer();
        MinecraftServer server = source.getServer();

        //server.getPlayerManager().broadcast(Text.translatable("TEST BROADCAST"),false);
        server.getPlayerManager().broadcast(Text.translatable(source.getSignedArguments().toString()),false);

        ServerResourcePackProperties serverProperties = getServerResourcePackProperties(server);

        String resourcePackURL = serverProperties.url();
        String resourcePackSHA1 = serverProperties.hash();
        String newRPurl = "https://github.com/Mat0u5/DO2-Resources/releases/download/release-caa62738a9f910e67555b206c9dc70176f9c3462/RP.zip";
        String newRPSHA1 = "0705c22cc6e0ddb6cfc21992efa61f4d27facc68";

        server.getPlayerManager().broadcast(Text.translatable("Resource pack url: " + resourcePackURL),false);
        server.getPlayerManager().broadcast(Text.translatable("Resource pack sha1: " + resourcePackSHA1),false);
        //self.sendMessage(Text.translatable("Message To Player"));
        reloadResourcePack(server, self, newRPurl,newRPSHA1);
        return 1;
    }
    private static ServerResourcePackProperties getServerResourcePackProperties(MinecraftServer server) {
        Optional<ServerResourcePackProperties> rp = server.getResourcePackProperties();
        if (rp.isPresent()) {
            return rp.get();
        }
        return null;
    }
    private static void reloadResourcePackForAllPlayers(MinecraftServer server, String newRPurl, String newRPSHA1) {
        Collection<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player : players) {
            player.networkHandler.sendPacket(new net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket(newRPurl,newRPSHA1,true,Text.translatable("New resource pack update!")));
        }
    }
    private static void reloadResourcePack(MinecraftServer server, PlayerEntity player, String newRPurl, String newRPSHA1) {
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        serverPlayer.networkHandler.sendPacket(new net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket(newRPurl,newRPSHA1,true,Text.translatable("New resource pack update!")));
    }

}

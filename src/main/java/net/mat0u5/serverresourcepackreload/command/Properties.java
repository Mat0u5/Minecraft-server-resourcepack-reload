package net.mat0u5.serverresourcepackreload.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.server.MinecraftServer.ServerResourcePackProperties;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Optional;

public class Properties {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        /*serverCommandSourceCommandDispatcher.register(CommandManager.literal("resourcepack")
                .then(CommandManager.literal("reload").executes(Properties::reload)));*/

//.then(CommandManager.argument("url", StringArgumentType.string()).then(CommandManager.argument("sha1", StringArgumentType.string())
        serverCommandSourceCommandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("resourcepack").then(CommandManager.literal("reload").executes(context -> Properties.execute((ServerCommandSource)context.getSource(), ImmutableList.of(((ServerCommandSource)context.getSource()).getPlayerOrThrow())))).then(CommandManager.literal("reload").then(CommandManager.argument("targets", EntityArgumentType.players()).requires(source -> source.hasPermissionLevel(2)).executes(context -> Properties.execute((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"))))))));
        serverCommandSourceCommandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("resourcepack")
                .then(CommandManager.literal("custom").requires(source -> source.hasPermissionLevel(2)).then(CommandManager.argument("url", StringArgumentType.string()).then(CommandManager.argument("sha1", StringArgumentType.string()).executes(context -> Properties.executeApplyCustom((ServerCommandSource)context.getSource(), ImmutableList.of(((ServerCommandSource)context.getSource()).getPlayerOrThrow()), StringArgumentType.getString(context, "url"), StringArgumentType.getString(context, "sha1")))
                .then(CommandManager.argument("targets", EntityArgumentType.players()).executes(context -> Properties.executeApplyCustom((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), StringArgumentType.getString(context, "url"), StringArgumentType.getString(context, "sha1"))))))))));
    }
    public static int execute(ServerCommandSource source, Collection<? extends PlayerEntity> targets) throws CommandSyntaxException {
        //final ServerCommandSource source = context.getSource();
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
        for(PlayerEntity player : targets) {
            reloadResourcePack(server, player, newRPurl,newRPSHA1);
        }
        return 1;
    }
    public static int executeApplyCustom(ServerCommandSource source, Collection<? extends PlayerEntity> targets, String newRPurl, String newRPSHA1) throws CommandSyntaxException {
        MinecraftServer server = source.getServer();


        server.getPlayerManager().broadcast(Text.translatable("Applying custom RP_URL: " + newRPurl),false);
        server.getPlayerManager().broadcast(Text.translatable("Applying custom RP_SHA: " + newRPSHA1),false);
        for(PlayerEntity player : targets) {
            reloadResourcePack(server, player, newRPurl,newRPSHA1);
        }
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

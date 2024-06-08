package net.mat0u5.serverresourcepackreload.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.mat0u5.serverresourcepackreload.Main;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.include.com.google.common.collect.ImmutableList;

import java.util.Collection;


public class Resourcepack {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("resourcepack")
                .then(CommandManager.literal("reload").executes(context -> Resourcepack.execute((ServerCommandSource)context.getSource(), ImmutableList.of(((ServerCommandSource)context.getSource()).getPlayerOrThrow()))))
                .then(CommandManager.literal("reload").then(CommandManager.argument("targets", EntityArgumentType.players()).requires(source -> source.hasPermissionLevel(2)).executes(context -> Resourcepack.execute((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"))))))));

        serverCommandSourceCommandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("resourcepack")
                .then(CommandManager.literal("sendNewRPMessage").requires(source -> source.hasPermissionLevel(2)).executes(context -> Resourcepack.executeNewRPMessage((ServerCommandSource)context.getSource(), ImmutableList.of(((ServerCommandSource)context.getSource()).getPlayerOrThrow()))))
                .then(CommandManager.literal("sendNewRPMessage").then(CommandManager.argument("targets", EntityArgumentType.players()).requires(source -> source.hasPermissionLevel(2)).executes(context -> Resourcepack.executeNewRPMessage((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"))))))));

        serverCommandSourceCommandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("resourcepack")
                .then(CommandManager.literal("custom").requires(source -> source.hasPermissionLevel(2)).then(CommandManager.argument("url", StringArgumentType.string()).then(CommandManager.argument("sha1", StringArgumentType.string()).executes(context -> Resourcepack.executeApplyCustom((ServerCommandSource)context.getSource(), ImmutableList.of(((ServerCommandSource)context.getSource()).getPlayerOrThrow()), StringArgumentType.getString(context, "url"), StringArgumentType.getString(context, "sha1")))
                .then(CommandManager.argument("targets", EntityArgumentType.players()).executes(context -> Resourcepack.executeApplyCustom((ServerCommandSource)context.getSource(), EntityArgumentType.getPlayers(context, "targets"), StringArgumentType.getString(context, "url"), StringArgumentType.getString(context, "sha1"))))))))));

        serverCommandSourceCommandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)CommandManager.literal("resourcepack")
                .then(CommandManager.literal("setLatestResourcepack").requires(source -> source.hasPermissionLevel(2)).then(CommandManager.argument("url", StringArgumentType.string()).then(CommandManager.argument("sha1", StringArgumentType.string()).executes(context -> Resourcepack.executeSetNewRP((ServerCommandSource)context.getSource(), StringArgumentType.getString(context, "url"), StringArgumentType.getString(context, "sha1")))))))));

    }
    public static int execute(ServerCommandSource source, Collection<? extends PlayerEntity> targets) throws CommandSyntaxException {
        MinecraftServer server = source.getServer();
        final PlayerEntity self = source.getPlayer();

        for(PlayerEntity player : targets) {
            reloadLatestResourcePack(server, player);
        }
        sendCommandFeedback(self,targets,"latest");
        return 1;
    }
    public static int executeApplyCustom(@NotNull ServerCommandSource source, Collection<? extends PlayerEntity> targets, String newRPurl, String newRPSHA1) throws CommandSyntaxException {
        MinecraftServer server = source.getServer();
        final PlayerEntity self = source.getPlayer();

        newRPurl = parseRPString(newRPurl);
        newRPSHA1 = parseRPString(newRPSHA1);

        for(PlayerEntity player : targets) {
            reloadResourcePack(player, newRPurl,newRPSHA1);
        }
        sendCommandFeedback(self,targets,"custom");
        return 1;
    }
    public static int executeNewRPMessage(ServerCommandSource source, Collection<? extends PlayerEntity> targets)  throws CommandSyntaxException {
        MinecraftServer server = source.getServer();
        final PlayerEntity self = source.getPlayer();

        sendCommandFeedback(self,targets,"newRP");
        for(PlayerEntity player : targets) {
            sendNewRPMessage(player);
        }
        return 1;
    }
    public static int executeSetNewRP(ServerCommandSource source, String newRPurl, String newRPSHA1)  throws CommandSyntaxException {
        MinecraftServer server = source.getServer();
        final PlayerEntity self = source.getPlayer();

        newRPurl = parseRPString(newRPurl);
        newRPSHA1 = parseRPString(newRPSHA1);
        Main.config.setProperty("resourcepack.url",newRPurl);
        Main.config.setProperty("resourcepack.sha1",newRPSHA1);

        if (self != null) self.sendMessage(Text.translatable("§6New latest resourcepack has been set!"));
        else System.out.println("New latest resourcepack has been set!");

        //Send message to all players
        Collection<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player : players) {
            sendNewRPMessage(player);
        }
        return 1;
    }
    public static void reloadResourcePack(PlayerEntity player, String newRPurl, String newRPSHA1) {
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        serverPlayer.networkHandler.sendPacket(new net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket(newRPurl,newRPSHA1,true,Text.translatable("New resource pack update!")));
    }
    public static void reloadLatestResourcePack(MinecraftServer server, PlayerEntity player) {
        reloadResourcePack(player, Main.config.getProperty("resourcepack.url"),Main.config.getProperty("resourcepack.sha1"));
    }
    public static void sendCommandFeedback(PlayerEntity self, Collection<? extends PlayerEntity> targets, String RPType) {

        if (targets.size()==1) {
            PlayerEntity player = (PlayerEntity) targets.toArray()[0];
            if (self != null) {//sent by player
                if (self != player) {
                    if (RPType != "newRP") self.sendMessage(Text.translatable("§6Applying "+RPType+" resourcepack to " +player.getEntityName()+ "..."));
                    else self.sendMessage(Text.translatable("§6Sending message to " +player.getEntityName()+ "..."));
                }
                else {
                    if (RPType != "newRP") self.sendMessage(Text.translatable("§6Applying "+RPType+" resourcepack..."));
                }
            }
            else {//sent by console
                if (RPType != "newRP") System.out.println("Applying "+RPType+" resourcepack to " +player.getEntityName()+ "...");
                else System.out.println("Sending message to " +player.getEntityName()+ "...");
            }
        }
        else {
            if (self != null) {//sent by player
                if (RPType != "newRP") self.sendMessage(Text.translatable("§6Applying "+RPType+" resourcepack to multiple players..."));
                else self.sendMessage(Text.translatable("§6Sending message to multiple players..."));
            }
            else {//sent by console
                if (RPType != "newRP") System.out.println("Applying "+ RPType +" resourcepack to multiple players...");
                else System.out.println("Sending message to multiple players...");
            }
        }
    }
    public static void sendNewRPMessage(PlayerEntity player) {
        player.sendMessage(Text.translatable("§6New Resourcepack version available! ").append(Text.translatable("§9§nClick to apply.").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/resourcepack reload")))), false);
    }
    private static String parseRPString(String str) {
        while (str.startsWith("\"") && str.endsWith("\"")) {
            str = str.substring(1,str.length()-1);
        }
        return str;
    }
}

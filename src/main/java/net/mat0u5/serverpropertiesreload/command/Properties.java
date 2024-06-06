package net.mat0u5.serverpropertiesreload.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import net.kaupenjoe.tutorialmod.util.IEntityDataSaver;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.message.MessageType;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.dedicated.ServerPropertiesHandler;
import net.minecraft.server.dedicated.ServerPropertiesLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.MinecraftServer.ServerResourcePackProperties;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.ServerWorldProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Properties {
    public static void register(CommandDispatcher<ServerCommandSource> serverCommandSourceCommandDispatcher,
                                CommandRegistryAccess commandRegistryAccess,
                                CommandManager.RegistrationEnvironment registrationEnvironment) {
        serverCommandSourceCommandDispatcher.register(CommandManager.literal("properties")
                .then(CommandManager.literal("reload").executes(Properties::run)));
    }
    public static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        final ServerCommandSource source = context.getSource();
        final PlayerEntity self = source.getPlayer();
        MinecraftServer server = source.getServer();

        server.getPlayerManager().broadcast(Text.translatable("TEST BROADCAST"),false);
        //ServerPropertiesHandler.
        //ServerPropertiesLoader
        //ServerWorldProperties
        //ServerPropertiesLoader propertiesLoader = new ServerPropertiesLoader(server.getRunDirectory().toPath().resolve("server.properties"));
        //propertiesLoader.getPropertiesHandler().serverResourcePackProperties.

        Optional<ServerResourcePackProperties> rp = server.getResourcePackProperties();
        if (rp.isPresent()) {
            ServerResourcePackProperties serverProperties = rp.get();

            server.getPlayerManager().broadcast(Text.translatable(serverProperties.toString()),false);
            String resourcePackURL = serverProperties.url();
            String resourcePackSHA1 = serverProperties.hash();
            //self.sendMessage(Text.translatable("Message To Player"));
            reloadResourcePack(server,source);
        }
        return 1;
    }
    private static void reloadResourcePack(MinecraftServer server, ServerCommandSource source) {
        String newRPurl = "https://github.com/Mat0u5/DO2-Resources/releases/download/release-8b4397887f9392467d59aade203ee74fdc5f93a4/RP.zip";
        String newRPSHA1 = "f780d21b2aea865ba63e2052e14c5e713ea3e84d";

        Collection<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player : players) {
            player.networkHandler.sendPacket(new net.minecraft.network.packet.s2c.play.ResourcePackSendS2CPacket(newRPurl,newRPSHA1,true,Text.translatable("New resource pack update!")));
        }
    }

}

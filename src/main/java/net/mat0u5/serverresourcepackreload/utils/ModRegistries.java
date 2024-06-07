package net.mat0u5.serverresourcepackreload.utils;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.mat0u5.serverresourcepackreload.command.Properties;

public class ModRegistries {
    public static void registerModStuff() {
        registerCommands();
    }
    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(Properties::register);
    }
}

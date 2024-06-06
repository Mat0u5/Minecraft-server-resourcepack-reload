package net.mat0u5.serverpropertiesreload.utils;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.mat0u5.serverpropertiesreload.command.Properties;

public class ModRegistries {
    public static void registerModStuff() {
        registerCommands();
    }
    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(Properties::register);
    }
}

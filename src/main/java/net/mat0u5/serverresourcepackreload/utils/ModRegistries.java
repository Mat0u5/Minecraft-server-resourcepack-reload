package net.mat0u5.serverresourcepackreload.utils;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.mat0u5.serverresourcepackreload.command.Resourcepack;
import net.mat0u5.serverresourcepackreload.events.Events;

public class ModRegistries {
    public static void registerModStuff() {
        registerCommands();
        registerEvents();
    }
    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(Resourcepack::register);
    }
    private static void registerEvents() {
        Events.register();
    }
}

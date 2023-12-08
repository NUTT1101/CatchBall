package com.github.nutt1101;

import java.util.logging.Level;


import com.github.nutt1101.command.Command;
import com.github.nutt1101.command.TabComplete;
import com.github.nutt1101.event.DropGoldEgg;
import com.github.nutt1101.event.GUIClick;
import com.github.nutt1101.event.HitEvent;
import com.github.nutt1101.event.SkullClick;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Arrays;
import java.util.List;

public class CatchBall extends JavaPlugin{
    private FileConfiguration config = this.getConfig();

    public static Plugin plugin;

    private static final List<String> SUPPORTED_VERSIONS = Arrays.asList(
            "1.20.4-R0.1-SNAPSHOT",
            "1.20.3-R0.1-SNAPSHOT",
            "1.20.2-R0.1-SNAPSHOT",
            "1.20.1-R0.1-SNAPSHOT",
            "1.20-R0.1-SNAPSHOT",
            "1.19.4-R0.1-SNAPSHOT",
            "1.18.2-R0.1-SNAPSHOT",
            "1.17.1-R0.1-SNAPSHOT",
            "1.16.5-R0.1-SNAPSHOT"
    );

    private void checkPluginHook(String pluginName) {
        if (this.getServer().getPluginManager().getPlugin(pluginName) != null) {
            plugin.getLogger().log(Level.INFO, ChatColor.GREEN + pluginName + " Hook!");
        }
    }

    @Override
    public void onEnable() {

        plugin = this;

        ConfigSetting.checkConfig();

        new Metrics(this, 12380);
        registerEvent();
        registerCommand();

        String serverVersion = getServer().getBukkitVersion();

        if (!SUPPORTED_VERSIONS.contains(serverVersion)) {
            getLogger().warning("Your Minecraft version is not supported by CatchBall, we only support " + String.join(", ", SUPPORTED_VERSIONS));
            getLogger().warning("CatchBall will be automatically disabled");
            // Disable the plugin
            getServer().getPluginManager().disablePlugin(this);
        } else {
            getLogger().info("CatchBall has been enabled on a supported version: " + serverVersion);
            checkPluginHook("Residence");
            checkPluginHook("MythicMobs");
            checkPluginHook("GriefPrevention");
            checkPluginHook("Lands");
            checkPluginHook("PlaceholderAPI");
            checkPluginHook("RedProtect");
            // TODO
            // checkPluginHook("WorldGuard");
        }

    }

    // register event
    public void registerEvent() {
        PluginManager registerEvent = this.getServer().getPluginManager();
        registerEvent.registerEvents(new HitEvent(), this);
        registerEvent.registerEvents(new DropGoldEgg(), this);
        registerEvent.registerEvents(new SkullClick(), this);
        registerEvent.registerEvents(new GUIClick(), this);
    }

    // register command
    public void registerCommand() {
        PluginCommand ctbCommand = this.getCommand("ctb");
        if (ctbCommand != null) {
            ctbCommand.setExecutor(new Command());
            ctbCommand.setTabCompleter(new TabComplete());
        }
    }

    public static String getServerVersion() {
        return plugin.getServer().getBukkitVersion();
    }

}

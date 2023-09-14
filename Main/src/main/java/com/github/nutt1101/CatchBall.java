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

public class CatchBall extends JavaPlugin{
    private FileConfiguration config = this.getConfig();

    public static Plugin plugin;

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

        checkPluginHook("Residence");
        checkPluginHook("MythicMobs");
        checkPluginHook("GriefPrevention");
        checkPluginHook("Lands");
        checkPluginHook("RedProtect");

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
        ctbCommand.setExecutor(new Command());
        ctbCommand.setTabCompleter(new TabComplete());
    }

    public static String getServerVersion() {
        return plugin.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    public  static  String getRealServerVersion() {
        String[] versionNodes = CatchBall.getServerVersion().split("_");
        return versionNodes[0] + "_" + versionNodes[1];
    }

}

package com.pohuang;

import com.pohuang.command.Command;
import com.pohuang.command.TabComplete;
import com.pohuang.event.DropGoldEgg;
import com.pohuang.event.GUIClick;
import com.pohuang.event.HitEvent;
import com.pohuang.event.SkullClick;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class CatchBall extends JavaPlugin{
    private FileConfiguration config = this.getConfig();
    private Boolean plguinStatus = config.getBoolean("Enable");

    @Override
    public void onEnable() {
        if (!this.getServer().getVersion().contains("1.16")) {
            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "=============[CatchBall]=============");
            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "This Plugin only support 1.16 now!");
            this.getServer().getConsoleSender().sendMessage(ChatColor.RED + "=============[CatchBall]=============");
            return;
        }

        ConfigSetting.checkConfig();

        if (!ConfigSetting.enabled) { 
            this.getLogger().info("Plugin Status: " + plguinStatus);
        } else {
            new Metrics(this, 12380);
            registerEvent();
            registerCommand();

            if (this.getServer().getPluginManager().getPlugin("Residence") != null) {
                this.getServer().getConsoleSender().sendMessage("[CatchBall]" + ChatColor.GREEN + "Residence Hook!");
            }

            if (this.getServer().getPluginManager().getPlugin("MythicMobs") != null) {
                this.getServer().getConsoleSender().sendMessage("[CatchBall] " + ChatColor.GREEN + "MythicMobs Hook!");
            }

            if (this.getServer().getPluginManager().getPlugin("GriefPrevention") != null) {
                this.getServer().getConsoleSender().sendMessage("[CatchBall] " + ChatColor.GREEN + "GriefPrevention Hook!");
            }
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
        ctbCommand.setExecutor(new Command());
        ctbCommand.setTabCompleter(new TabComplete());
    }

}

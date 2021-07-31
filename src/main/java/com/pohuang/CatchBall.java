package com.pohuang;

import com.pohuang.Recipe.BallRecipe;
import com.pohuang.command.Command;
import com.pohuang.command.TabComplete;
import com.pohuang.event.DropGoldEgg;
import com.pohuang.event.GUIClick;
import com.pohuang.event.HitEvent;
import com.pohuang.event.LaunchEvent;
import com.pohuang.event.SkullClick;

import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class CatchBall extends JavaPlugin{
    private FileConfiguration config = this.getConfig();
    private Boolean plguinStatus = config.getBoolean("Enable");
    private Plugin resPlug = getServer().getPluginManager().getPlugin("Residence");

    @Override
    public void onEnable() {
        ConfigSetting.checkConfig();
        if (!ConfigSetting.enabled) { 
            this.getLogger().info("Plugin Status: " + plguinStatus);
        } else {
            registerEvent();
            registerCommand();
            new BallRecipe();

            if (resPlug != null) {
                this.getLogger().info(ChatColor.GREEN + "Residence Hook!");
            }
        }
    }

    // register event
    public void registerEvent() {
        PluginManager registerEvent = this.getServer().getPluginManager();
        registerEvent.registerEvents(new HitEvent(), this);
        registerEvent.registerEvents(new LaunchEvent(), this);
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

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
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CatchBall extends JavaPlugin{
    private FileConfiguration config = this.getConfig();
    private Boolean plguinStatus = config.getBoolean("Enable");
    
    @Override
    public void onEnable() {
        ConfigSetting.checkConfig();
        if (!ConfigSetting.enabled) { 
            this.getLogger().info("Plugin Status: " + plguinStatus);
        } else {
            registerEvent();
            registerCommand();
            new BallRecipe();
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

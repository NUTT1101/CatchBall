package com.pohuang.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pohuang.ConfigSetting;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;

public class TabComplete implements TabCompleter {
    List<String> entityList = new ArrayList<>();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        
        // tabComplete will be show suggest arument to commandSender
        if (command.getName().equals("ctb")) {
            if (!sender.hasPermission("catchball.op")) { return null; }
            
            if (args.length == 1) { return CommandCheck.getCommandArgument(); }

            if (args.length == 2) { 
                entityList.clear();
                if (args[0].equalsIgnoreCase("get")) {
                    return Arrays.asList("CatchBall", "GoldEgg");
                } else if (args[0].equalsIgnoreCase("add")) {

                    for (String entity : ConfigSetting.getEntityFile().getConfigurationSection("EntityList").getKeys(false)) {
                        if (!ConfigSetting.catchableEntity.contains(EntityType.valueOf(entity))) {
                            entityList.add(entity);
                        }
                    }
                    return entityList;

                } else if (args[0].equalsIgnoreCase("remove")) {

                    for (String entity : ConfigSetting.getEntityFile().getConfigurationSection("EntityList").getKeys(false)) {
                        if (ConfigSetting.catchableEntity.contains(EntityType.valueOf(entity))) {
                            entityList.add(entity);
                        }
                    }
                    return entityList;
                }
            } 

        }
        
        return null;
    }
}

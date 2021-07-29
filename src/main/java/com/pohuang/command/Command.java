package com.pohuang.command;

import java.util.List;

import com.pohuang.ConfigSetting;
import com.pohuang.GUI.CatchableList;
import com.pohuang.Recipe.BallRecipe;
import com.pohuang.items.Ball;
import com.pohuang.items.GoldEgg;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class Command implements CommandExecutor {
    private List<String> commandArgument = CommandCheck.getCommandArgument();
    ItemStack catchBall = new Ball().getCatchBall();
    ItemStack goldEgg = new GoldEgg().getGoldEgg();

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (command.getName().equals("ctb")) {
            
            if (!(CommandCheck.check(sender, command, label, args))) { return true; }

            /* player use "/ctb get" command
            player will get a catchBall*/
            if (args[0].equalsIgnoreCase("get")) {
                
                if (!checkSenderPlayer(sender)) { return true; }

                Player player = (Player) sender;
                
                if (args.length == 1) {
                    player.sendMessage(ConfigSetting.toChat(ConfigSetting.itemDoesNotExist, "", ""));
                    return true;
                }

                if (checkItem(args[1]) == null) {
                    player.sendMessage(ConfigSetting.toChat(ConfigSetting.itemNameError, "", ""));
                    return true;
                }
                
                // check if inventory of player is full
                if (player.getInventory().firstEmpty() == -1) {
                    player.sendMessage(ConfigSetting.toChat(ConfigSetting.playerInventoryFull, "", ""));
                    player.getWorld().dropItem(player.getLocation(), checkItem(args[1]));
                    return true;
                }

                player.getInventory().addItem(checkItem(args[1]));
                
                String message = checkItem(args[1]).equals(catchBall) ? ConfigSetting.toChat(ConfigSetting.sucessGetBall, "", "").
                    replace("{ITEM}", ConfigSetting.catchBallName) : ConfigSetting.toChat(ConfigSetting.sucessGetBall, "", "").
                    replace("{ITEM}", ConfigSetting.goldEggName);

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

            } else if (args[0].equalsIgnoreCase("reload")) {
                ConfigSetting.checkConfig();
                new BallRecipe();
                sender.sendMessage(ConfigSetting.toChat(ConfigSetting.reloadSucess, "", ""));
                return true;

            } else if (args[0].equalsIgnoreCase("list")){
                if (!checkSenderPlayer(sender)) { return true; }
                
                new CatchableList().openCatchableList((Player) sender, 1);

            } else if (args[0].equalsIgnoreCase("add")){
                if (args.length == 1) { 
                    sender.sendMessage(ConfigSetting.toChat(ConfigSetting.addEntityDoesNotExist, "", "")); 
                    return true;
                }

                if (!ConfigSetting.getEntityFileExist(args[1])) {
                    sender.sendMessage(ConfigSetting.toChat(ConfigSetting.unknownEntityType, "", ""));
                    return true;
                }

                if (ConfigSetting.catchableEntity.contains(EntityType.valueOf(args[1].toUpperCase()))) { 
                    sender.sendMessage(ConfigSetting.toChat(ConfigSetting.entityDoesExists, "", ""));
                    return true;
                }

                ConfigSetting.catchableEntity.add(EntityType.valueOf(args[1].toUpperCase()));
                sender.sendMessage(ConfigSetting.toChat(ConfigSetting.sucessAddEntity, "", args[1].toUpperCase()));
                ConfigSetting.saveEntityList();
                return true;
                
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (args.length == 1) { 
                    sender.sendMessage(ConfigSetting.toChat(ConfigSetting.removeEntityDoesNotExist, "", "")); 
                    return true;
                }

                if (!ConfigSetting.getEntityFileExist(args[1])) {
                    sender.sendMessage(ConfigSetting.toChat(ConfigSetting.unknownEntityType, "", ""));
                    return true;
                }

                if (!ConfigSetting.catchableEntity.contains(EntityType.valueOf(args[1].toUpperCase()))) { 
                    sender.sendMessage(ConfigSetting.toChat(ConfigSetting.removeEntityNotFound, "", ""));
                    return true;
                }

                ConfigSetting.catchableEntity.remove(EntityType.valueOf(args[1].toUpperCase()));
                sender.sendMessage(ConfigSetting.toChat(ConfigSetting.sucessRemove, "", args[1].toUpperCase()));
                ConfigSetting.saveEntityList();
                return true;

            } else if (!commandArgument.contains(args[0])) {
                
                // player enter unknown argument
                sender.sendMessage(ConfigSetting.toChat(ConfigSetting.unknownCommandArgument, "", ""));
                return true;
            }
        }

        return true;
    }
   
    private ItemStack checkItem(String item) {
        item = item.toLowerCase();
        if (item.equals("catchball")) { return catchBall; }
        if (item.equals("goldegg")) { return goldEgg; }    
        return null;
    }

    private Boolean checkSenderPlayer(CommandSender sender) {
        if (sender instanceof Player) { return true; }
        sender.sendMessage(ConfigSetting.toChat(ConfigSetting.consoleExcuteCommand, "", ""));
        return false;
    } 

}
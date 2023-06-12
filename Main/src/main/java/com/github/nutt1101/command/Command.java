package com.github.nutt1101.command;

import java.util.List;
import java.util.Set;


import com.github.nutt1101.ConfigSetting;
import com.github.nutt1101.GUI.CatchableList;
import com.github.nutt1101.items.Ball;
import com.github.nutt1101.items.GoldEgg;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class Command implements CommandExecutor {
    private List<String> commandArgument = CommandCheck.getCommandArgument();
    private Set<String> entityList = ConfigSetting.entityFile.getConfigurationSection("EntityList").getKeys(false);

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

                givePlayerItem(player, checkItem(args[1]));

                String message = checkItem(args[1]).equals(Ball.makeBall()) ? ConfigSetting.toChat(ConfigSetting.successGetBall, "", "").
                        replace("{ITEM}", ConfigSetting.catchBallName) : ConfigSetting.toChat(ConfigSetting.successGetBall, "", "").
                        replace("{ITEM}", ConfigSetting.goldEggName);

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));

            } else if (args[0].equalsIgnoreCase("reload")) {
                ConfigSetting.checkConfig();
                sender.sendMessage(ConfigSetting.toChat(ConfigSetting.reloadSuccess, "", ""));
                return true;

            } else if (args[0].equalsIgnoreCase("list")){
                if (!checkSenderPlayer(sender)) { return true; }

                new CatchableList().openCatchableList((Player) sender, 1);

            } else if (args[0].equalsIgnoreCase("add")){
                if (args.length == 1) {
                    sender.sendMessage(ConfigSetting.toChat(ConfigSetting.addEntityDoesNotExist, "", ""));
                    return true;
                }

                if (args[1].equalsIgnoreCase("all")) {
                    Set<String> entityList = ConfigSetting.entityFile.getConfigurationSection("EntityList").getKeys(false);
                    for (String entity : entityList) {
                        if (!ConfigSetting.catchableEntity.contains(EntityType.valueOf(entity))) {
                            ConfigSetting.catchableEntity.add(EntityType.valueOf(entity.toUpperCase()));
                        }
                    }
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigSetting.allEntityAddSuccess));
                    ConfigSetting.saveEntityList();
                    return true;
                }

                if (!entityList.contains(args[1].toUpperCase())) {
                    sender.sendMessage(ConfigSetting.toChat(ConfigSetting.unknownEntityType, "", ""));
                    return true;
                }

                if (ConfigSetting.catchableEntity.contains(EntityType.valueOf(args[1].toUpperCase()))) {
                    sender.sendMessage(ConfigSetting.toChat(ConfigSetting.entityDoesExists, "", ""));
                    return true;
                }

                ConfigSetting.catchableEntity.add(EntityType.valueOf(args[1].toUpperCase()));
                sender.sendMessage(ConfigSetting.toChat(ConfigSetting.successAddEntity, "", args[1].toUpperCase()));
                ConfigSetting.saveEntityList();
                return true;

            } else if (args[0].equalsIgnoreCase("remove")) {
                if (args.length == 1) {
                    sender.sendMessage(ConfigSetting.toChat(ConfigSetting.removeEntityDoesNotExist, "", ""));
                    return true;
                }

                if (args[1].equalsIgnoreCase("all")) {
                    ConfigSetting.catchableEntity.clear();
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigSetting.allEntityRemoveSuccess));
                    ConfigSetting.saveEntityList();
                    return true;
                }

                if (!entityList.contains(args[1])) {
                    sender.sendMessage(ConfigSetting.toChat(ConfigSetting.unknownEntityType, "", ""));
                    return true;
                }

                if (!ConfigSetting.catchableEntity.contains(EntityType.valueOf(args[1].toUpperCase()))) {
                    sender.sendMessage(ConfigSetting.toChat(ConfigSetting.removeEntityNotFound, "", ""));
                    return true;
                }

                ConfigSetting.catchableEntity.remove(EntityType.valueOf(args[1].toUpperCase()));
                sender.sendMessage(ConfigSetting.toChat(ConfigSetting.successRemove, "", args[1].toUpperCase()));
                ConfigSetting.saveEntityList();
                return true;

            } else if (args[0].equalsIgnoreCase("give")) {
                if (args.length == 1) {
                    sender.sendMessage(ConfigSetting.toChat(ConfigSetting.playerNotExist, "", ""));
                    return true;
                }

                if (args.length == 2) {
                    sender.sendMessage(ConfigSetting.toChat(ConfigSetting.itemDoesNotExist, "", ""));
                    return true;
                }

                Player player = Bukkit.getPlayer(args[1]);

                if (player == null) {
                    sender.sendMessage(ConfigSetting.toChat(ConfigSetting.unknownOrOfflinePlayer, "", "").
                            replace("{PLAYER}", args[1]));
                    return true;
                }

                if (checkItem(args[2]) == null) {
                    sender.sendMessage(ConfigSetting.toChat(ConfigSetting.itemNameError, "", ""));
                    return true;
                }

                int itemAmount = 1;
                if (args.length == 4) {
                    try {
                        itemAmount = Integer.parseInt(args[3]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(ConfigSetting.toChat(ConfigSetting.invalidItemAmount, "", ""));
                        return true;
                    }
                }

                givePlayerItem(player, checkItem(args[2]), itemAmount);

                sender.sendMessage(ConfigSetting.toChat(ConfigSetting.successGiveItemToPlayer, "", "").
                        replace("{ITEM}", args[2].toLowerCase().equals("catchball") ? ConfigSetting.catchBallName : ConfigSetting.goldEggName).
                        replace("{PLAYER}", player.getName()).
                        replace("&", "§"));

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
        if (item.equals("catchball")) { return Ball.makeBall(); }
        if (item.equals("goldegg")) { return GoldEgg.makeGoldEgg(); }
        return null;
    }

    private Boolean checkSenderPlayer(CommandSender sender) {
        if (sender instanceof Player) { return true; }
        sender.sendMessage(ConfigSetting.toChat(ConfigSetting.consoleExecuteCommand, "", ""));
        return false;
    }

    private Boolean givePlayerItem(Player player, ItemStack itemStack, int amount) {
        if (player.getInventory().firstEmpty() == -1) {
            player.sendMessage(ConfigSetting.toChat(ConfigSetting.playerInventoryFull, "", ""));
            itemStack.setAmount(amount);
            player.getWorld().dropItem(player.getLocation(), itemStack);
            return true;
        } else {
            itemStack.setAmount(amount);
            player.getInventory().addItem(itemStack);
            return true;
        }
    }

}
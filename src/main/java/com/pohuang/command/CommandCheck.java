package com.pohuang.command;

import java.util.Arrays;
import java.util.List;

import com.pohuang.ConfigSetting;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandCheck {
    private static List<String> argmumentList = Arrays.asList("get", "reload", "list", "add", "remove");
    
    /* check commandSender , permission and argument
    if return false, this command will be stop and send error message to player
    if return true , this command will be continue execution */    
    public static Boolean check(CommandSender sender, Command command, String label, String[] args) {
        
        // Check if player has permission 
        if (!(sender.hasPermission("catchball.op"))) {
            sender.sendMessage(ConfigSetting.toChat(ConfigSetting.noPermission, "", ""));
            return false;
        }
        
        // check if commandSender does not exist argument
        if (args.length == 0) {
            sender.sendMessage(ConfigSetting.toChat(ConfigSetting.argDoesNotExist, "", ""));
            return false;
        }

        return true;
    }

    public static List<String> getCommandArgument() { return argmumentList; };
}

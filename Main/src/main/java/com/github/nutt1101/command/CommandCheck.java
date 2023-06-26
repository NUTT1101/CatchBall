package com.github.nutt1101.command;

import java.util.Arrays;
import java.util.List;

import com.github.nutt1101.ConfigSetting;

import com.github.nutt1101.utils.TranslationFileReader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandCheck {
    private static List<String> argmumentList = Arrays.asList("reload", "list", "add", "remove", "give");
    
    public static Boolean check(CommandSender sender, Command command, String label, String[] args) {
        
        // Check if player has permission 
        if (!(sender.hasPermission("catchball.op"))) {
            sender.sendMessage(ConfigSetting.toChat(TranslationFileReader.noPermission, "", ""));
            return false;
        }
        
        // check if commandSender does not exist argument
        if (args.length == 0) {
            sender.sendMessage(ConfigSetting.toChat(TranslationFileReader.argDoesNotExist, "", ""));
            return false;
        }

        return true;
    }

    public static List<String> getCommandArgument() { return argmumentList; };
}

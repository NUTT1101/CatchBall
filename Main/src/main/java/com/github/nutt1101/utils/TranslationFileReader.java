package com.github.nutt1101.utils;

import com.github.nutt1101.CatchBall;
import com.github.nutt1101.ConfigSetting;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import me.clip.placeholderapi.PlaceholderAPI;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TranslationFileReader {
     public static String consoleExecuteCommand;
     public static String noPermission;
     public static String playerInventoryFull;
     public static String argDoesNotExist;
     public static String argTooMuch;
     public static String unknownCommandArgument;
     public static String successGetBall;
     public static String reloadSuccess;
     public static String canNotCatchable;
     public static String ballHitBlock;
     public static String noPermissionToUse;
     public static String catchSuccess;
     public static String addEntityDoesNotExist;
     public static String unknownEntityType;
     public static String entityDoesExists;
     public static String successAddEntity;
     public static String itemDoesNotExist;
     public static String itemNameError;
     public static String removeEntityDoesNotExist;
     public static String removeEntityNotFound;
     public static String successRemove;
     public static String skullDoesNotFound;
     public static String locationUnsafe;
     public static String noResidencePermissions;
     public static String allowCatchMessage;
     public static String allEntityAddSuccess;
     public static String allEntityRemoveSuccess;
     public static String playerNotExist;
     public static String unknownOrOfflinePlayer;
     public static String successGiveItemToPlayer;
     public static String invalidItemAmount;
     public static String catchableListTitle;
     public static String prevPage;
     public static String nextPage;
     public static String currentPage;
     public static String catchBallName;
     public static List<String> catchBallLore = new ArrayList<>();
     public static String goldEggName;
     public static List<String> goldEggLore = new ArrayList<>();
     public static List<String> dropSkullLore = new ArrayList<>();
     public static List<String> guiSkullLore = new ArrayList<>();
     public static YamlConfiguration localeYamlConfig;
    public static String catchFail;

    public static void init() throws IOException {
          prepareLocaleYamlConfig();
          load();
     }

     public static void prepareLocaleYamlConfig() throws IOException {
          File localeFile = new File(CatchBall.plugin.getDataFolder(), "locale/" + ConfigSetting.locale + ".yml");
          if (!localeFile.exists()) {
               generateLocaleFile(localeFile);
          }
          localeYamlConfig = YamlConfiguration.loadConfiguration(localeFile);
     }

     public static void generateLocaleFile(File localeFile) throws IOException {
          Plugin plugin = CatchBall.plugin;
          InputStream inputStream = plugin.getResource("locale/" + ConfigSetting.locale + ".yml");
          if (inputStream == null) {
               throw new IOException();
          }

          if (!localeFile.exists()) {
               File localeDir = new File(localeFile.getParent());
               if (!localeDir.exists()) {
                    localeDir.mkdirs();
               }

               FileOutputStream outputStream = new FileOutputStream(localeFile);
               inputStream.transferTo(outputStream);
               outputStream.close();
          }

          inputStream.close();
     }

     public static void load() {
          localeYamlConfig = YamlConfiguration.loadConfiguration(new File(CatchBall.plugin.getDataFolder(), "locale/" + ConfigSetting.locale + ".yml"));

          if (CatchBall.plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
               consoleExecuteCommand = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("ConsoleExcuteCommand", "&cThis command can only be executed by player!"));
               noPermission = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("NoPermission", "&cYou have no permission!"));
               playerInventoryFull = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("PlayerInventoryFull", "&aYour inventory is full, so item falls at your feet!"));
               argDoesNotExist = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("ArgDoesNotExist", "&aCommands Usage: \n&b/ctb get &7Get special items of plugins.\n&b/ctb reload &7Reload plugin.\n&b/ctb list &7Lists all catchable entities and states\n&b/ctb add &7Add the entity to the catch list\n&b/ctb remove &7Remove the entity from the catchable list"));
               argTooMuch = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("ArgTooMuch", "&cToo many Argument!"));
               unknownCommandArgument = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("UnknownCommandArgument", "&cUnknown Argument!"));
               successGetBall = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("SuccessGetBall", "&aSuccessfully get {ITEM}"));
               reloadSuccess = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("ReloadSuccess", "&aThe plugin reloaded successfully!"));
               canNotCatchable = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("CanNotCatchable", "&cThis entity cannot be captured, so &e{BALL} &7fell in &e{LOCATION}"));
               ballHitBlock = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("BallHitBlock", "&cYou did not hit a entity,So {BALL} fell in {LOCATION}"));
               noPermissionToUse = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("NoPermissionToUse", "&cYou don't have permission to use {BALL}, so {BALL} &cfell in &e{LOCATION}."));
               catchSuccess = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("CatchSuccess", "&aSuccessfully captured {ENTITY} location: {LOCATION}"));
               itemDoesNotExist = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("ItemDoesNotExist", "&cPlease enter the item to be picked up  &7CatchBall | GoldEgg"));
               itemNameError = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("ItemNameError", "&cPlease enter the correct item name  &7CatchBall | GoldEgg"));
               addEntityDoesNotExist = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("AddEntityDoesNotExist", "&cPlease enter the name of the entity to be added to the list of captured creatures!"));
               unknownEntityType = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("UnknownEntityType", "&cUnknown entity type!"));
               entityDoesExists = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("EntityDoesExists", "&cThe entity already exists in the catchable list!"));
               successAddEntity = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("SuccessAddEntity", "&b{ENTITY} &aSuccessfully added to the catchable list!"));
               removeEntityDoesNotExist = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("RemoveEntityDoesNotExist", "&cPlease enter the name of the entity to be removed from the catchable list"));
               removeEntityNotFound = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("RemoveEntityNotFound", "&cNo entity found in the catchable list"));
               successRemove = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("SuccessRemove", "&aSuccessfully removed from the catchable list &b{ENTITY}"));
               skullDoesNotFound = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("SkullDoesNotFound", "&cThe data stored in the skull is missing"));
               locationUnsafe = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("LocationUnsafe", "&cCould not find a safe area to spawn, so this request has been cancelled"));
               noResidencePermissions = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("NoResidencePermissions", "&cYou can’t spawn entity here because you are lacking {FLAG} permission for this residense"));
               allowCatchMessage = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("AllowCatchMessage", "&b{ENTITY} &6allow catch: {STATUS} !"));
               allEntityAddSuccess = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("AllEntityAddSuccess", "&aAll Entity added success成功！"));
               allEntityRemoveSuccess = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("AllEntityRemoveSuccess", "&eAll Entity removed success!"));
               playerNotExist = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("PlayerNotExist", "&cPlease enter a player that want to give the item!"));
               unknownOrOfflinePlayer = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("UnknownOrOfflinePlayer", "&cPlayer {PLAYER} not find!"));
               successGiveItemToPlayer = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("SuccessGiveItemToPlayer", "&aSuccess give {ITEM} to &a{PLAYER}!"));
               invalidItemAmount = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("invalidItemAmount", "&cInvalid item amount!"));
               catchableListTitle = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("catchableListTitle", "&lCatch List Settings"));
               prevPage = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("prevPage", "&aPrevious Page"));
               nextPage = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("nextPage", "&aNext Page"));
               currentPage = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("currentPage", "&eCurrent Page: &a{PAGE}"));
               catchBallName = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("Items.CatchBall.DisplayName", "&aCat&bch &cball"));

               catchBallLore = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getStringList("Items.CatchBall.Lore"));
               goldEggName = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("Items.GoldEgg.DisplayName", "&6GoldEgg"));
               goldEggLore = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getStringList("Items.GoldEgg.Lore"));
               dropSkullLore = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getStringList("DropSkullLore"));
               guiSkullLore = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getStringList("guiSkullLore"));

               catchFail = PlaceholderAPI.setPlaceholders(null, localeYamlConfig.getString("catchFail", "&cOops! Your attempt to catch the creature failed. Try again!"));
          } else {

               consoleExecuteCommand = localeYamlConfig.getString("ConsoleExcuteCommand", "&cThis command can only be executed by player!");

               noPermission = localeYamlConfig.getString("NoPermission", "&cYou have no permission!");
               playerInventoryFull = localeYamlConfig.getString("PlayerInventoryFull", "&aYour inventory is full, so item falls at your feet!");
               argDoesNotExist = localeYamlConfig.getString("ArgDoesNotExist", "&aCommands Usage: \n&b/ctb get &7Get special items of plugins.\n&b/ctb reload &7Reload plugin.\n&b/ctb list &7Lists all catchable entities and states\n&b/ctb add &7Add the entity to the catch list\n&b/ctb remove &7Remove the entity from the catchable list");
               argTooMuch = localeYamlConfig.getString("ArgTooMuch", "&cToo many Argument!");
               unknownCommandArgument = localeYamlConfig.getString("UnknownCommandArgument", "&cUnknown Argument!");
               successGetBall = localeYamlConfig.getString("SuccessGetBall", "&aSuccessfully get {ITEM}");
               reloadSuccess = localeYamlConfig.getString("ReloadSuccess", "&aThe plugin reloaded successfully!");
               canNotCatchable = localeYamlConfig.getString("CanNotCatchable", "&cThis entity cannot be captured, so &e{BALL} &7fell in &e{LOCATION}");
               ballHitBlock = localeYamlConfig.getString("BallHitBlock", "&cYou did not hit a entity,So {BALL} fell in {LOCATION}");
               noPermissionToUse = localeYamlConfig.getString("NoPermissionToUse", "&cYou don't have permission to use {BALL}, so {BALL} &cfell in &e{LOCATION}.");
               catchSuccess = localeYamlConfig.getString("CatchSuccess", "&aSuccessfully captured {ENTITY} location: {LOCATION}");
               itemDoesNotExist = localeYamlConfig.getString("ItemDoesNotExist", "&cPlease enter the item to be picked up  &7CatchBall | GoldEgg");
               itemNameError = localeYamlConfig.getString("ItemNameError", "&cPlease enter the correct item name  &7CatchBall | GoldEgg");
               addEntityDoesNotExist = localeYamlConfig.getString("AddEntityDoesNotExist", "&cPlease enter the name of the entity to be added to the list of captured creatures!");
               unknownEntityType = localeYamlConfig.getString("UnknownEntityType", "&cUnknown entity type!");
               entityDoesExists = localeYamlConfig.getString("EntityDoesExists", "&cThe entity already exists in the catchable list!");
               successAddEntity = localeYamlConfig.getString("SuccessAddEntity", "&b{ENTITY} &aSuccessfully added to the catchable list!");
               removeEntityDoesNotExist = localeYamlConfig.getString("RemoveEntityDoesNotExist", "&cPlease enter the name of the entity to be removed from the catchable list");
               removeEntityNotFound = localeYamlConfig.getString("RemoveEntityNotFound", "&cNo entity found in the catchable list");
               successRemove = localeYamlConfig.getString("SuccessRemove", "&aSuccessfully removed from the catchable list &b{ENTITY}");
               skullDoesNotFound = localeYamlConfig.getString("SkullDoesNotFound", "&cThe data stored in the skull is missing");
               locationUnsafe = localeYamlConfig.getString("LocationUnsafe", "&cCould not find a safe area to spawn, so this request has been cancelled");
               noResidencePermissions = localeYamlConfig.getString("NoResidencePermissions", "&cYou can’t spawn entity here because you are lacking {FLAG} permission for this residense");
               allowCatchMessage = localeYamlConfig.getString("AllowCatchMessage", "&b{ENTITY} &6allow catch: {STATUS} !");
               allEntityAddSuccess = localeYamlConfig.getString("AllEntityAddSuccess", "&aAll Entity added success成功！");
               allEntityRemoveSuccess = localeYamlConfig.getString("AllEntityRemoveSuccess", "&eAll Entity removed success!");
               playerNotExist = localeYamlConfig.getString("PlayerNotExist", "&cPlease enter a player that want to give the item!");
               unknownOrOfflinePlayer = localeYamlConfig.getString("UnknownOrOfflinePlayer", "&cPlayer {PLAYER} not find!");
               successGiveItemToPlayer = localeYamlConfig.getString("SuccessGiveItemToPlayer", "&aSuccess give {ITEM} to &a{PLAYER}!");
               invalidItemAmount = localeYamlConfig.getString("invalidItemAmount", "&cInvalid item amount!");
               catchableListTitle = localeYamlConfig.getString("catchableListTitle", "&lCatch List Settings");
               prevPage = localeYamlConfig.getString("prevPage", "&aPrevious Page");
               nextPage = localeYamlConfig.getString("nextPage", "&aNext Page");
               currentPage = localeYamlConfig.getString("currentPage", "&eCurrent Page: &a{PAGE}");
               catchBallName = localeYamlConfig.getString("Items.CatchBall.DisplayName", "&aCat&bch &cball");

               catchBallLore = localeYamlConfig.getStringList("Items.CatchBall.Lore");
               goldEggName = localeYamlConfig.getString("Items.GoldEgg.DisplayName", "&6GoldEgg");
               goldEggLore = localeYamlConfig.getStringList("Items.GoldEgg.Lore");
               dropSkullLore = localeYamlConfig.getStringList("DropSkullLore");
               guiSkullLore = localeYamlConfig.getStringList("guiSkullLore");

               catchFail = localeYamlConfig.getString("catchFail", "&cOops! Your attempt to catch the creature failed. Try again!");

          }
     }
}
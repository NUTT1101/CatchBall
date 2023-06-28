package com.github.nutt1101.utils;

import com.github.nutt1101.CatchBall;
import com.github.nutt1101.ConfigSetting;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
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

     public static YamlConfiguration localeYamlConfig;

     public static void init() throws IOException {
          generateLocaleFile();
          prepareLocaleYamlConfig();
          load();
     }

     public static void prepareLocaleYamlConfig() {
          localeYamlConfig = YamlConfiguration.loadConfiguration(
                  new File(CatchBall.plugin.getDataFolder().getAbsoluteFile(),
                          "locale/" + ConfigSetting.locale + ".yml")
          );
     }


     public static void generateLocaleFile() throws IOException {
          Plugin plugin = CatchBall.plugin;
          InputStream inputStream = plugin.getResource("locale/" + ConfigSetting.locale + ".yml");
          if (inputStream == null) throw new IOException();

        File dataFolder = plugin.getDataFolder();
        File localeDir = new File(dataFolder, "locale/"),
                localeFile = new File(localeDir, ConfigSetting.locale + ".yml");
        if (!localeDir.exists()) localeDir.mkdirs();

        FileOutputStream outputStream = new FileOutputStream(localeFile);
        inputStream.transferTo(outputStream);
        outputStream.close();
        inputStream.close();
     }

     public static void load() {
          consoleExecuteCommand = localeYamlConfig.isSet("ConsoleExcuteCommand") ? localeYamlConfig.getString("ConsoleExcuteCommand") :
                  "&cThis command can only be executed by player!" ;

          noPermission = localeYamlConfig.isSet("NoPermission") ? localeYamlConfig.getString("NoPermission") :
                  "&cYou have no permission!";

          playerInventoryFull = localeYamlConfig.isSet("PlayerInventoryFull") ? localeYamlConfig.getString("PlayerInventoryFull") :
                  "&aYour inventory is full, so item falls at your feet!";

          argDoesNotExist = localeYamlConfig.isSet("ArgDoesNotExist") ? localeYamlConfig.getString("ArgDoesNotExist") :
                  "&aCommands Usage: \n&b/ctb get &7Get special items of plugins.\n&b/ctb reload &7Reload plugin.\n&b/ctb list &7Lists all catchable entities and states\n&b/ctb add &7Add the entity to the catch list\n&b/ctb remove &7Remove the entity from the catchable list";

          argTooMuch = localeYamlConfig.isSet("ArgTooMuch") ? localeYamlConfig.getString("ArgTooMuch") :
                  "&cToo many Argument!";

          unknownCommandArgument = localeYamlConfig.isSet("UnknownCommandArgument") ? localeYamlConfig.getString("UnknownCommandArgument") :
                  "&cUnknown Argument!";

          successGetBall = localeYamlConfig.isSet("SuccessGetBall") ? localeYamlConfig.getString("SuccessGetBall") :
                  "&aSuccessfully get {ITEM}";

          reloadSuccess = localeYamlConfig.isSet("ReloadSuccess") ? localeYamlConfig.getString("ReloadSuccess") :
                  "&aThe plugin reloaded successfully!";

          canNotCatchable = localeYamlConfig.isSet("CanNotCatchable") ? localeYamlConfig.getString("CanNotCatchable") :
                  "&cThis entity cannot be captured, so &e{BALL} &7fell in &e{LOCATION}";

          ballHitBlock = localeYamlConfig.isSet("BallHitBlock") ? localeYamlConfig.getString("BallHitBlock") :
                  "&cYou did not hit a entity,So {BALL} fell in {LOCATION}";

          noPermissionToUse = localeYamlConfig.isSet("NoPermissionToUse") ? localeYamlConfig.getString("NoPermissionToUse") :
                  "&cYou don''t have permission to use {BALL}, so {BALL} &cfell in &e{LOCATION}.";

          catchSuccess = localeYamlConfig.isSet("CatchSuccess") ? localeYamlConfig.getString("CatchSuccess") :
                  "&aSuccessfully captured {ENTITY} location: {LOCATION}";

          itemDoesNotExist = localeYamlConfig.isSet("ItemDoesNotExist") ? localeYamlConfig.getString("ItemDoesNotExist") :
                  "&cPlease enter the item to be picked up  &7CatchBall | GoldEgg";

          itemNameError = localeYamlConfig.isSet("ItemNameError") ? localeYamlConfig.getString("ItemNameError") :
                  "&cPlease enter the correct item name  &7CatchBall | GoldEgg";

          addEntityDoesNotExist = localeYamlConfig.isSet("AddEntityDoesNotExist") ? localeYamlConfig.getString("AddEntityDoesNotExist") :
                  "&cPlease enter the name of the entity to be added to the list of captured creatures!";

          unknownEntityType = localeYamlConfig.isSet("UnknownEntityType") ? localeYamlConfig.getString("UnknownEntityType") :
                  "&cUnknown entity type!";

          entityDoesExists = localeYamlConfig.isSet("EntityDoesExists") ? localeYamlConfig.getString("EntityDoesExists") :
                  "&cThe entity already exists in the catchable list!";

          successAddEntity = localeYamlConfig.isSet("SuccessAddEntity") ? localeYamlConfig.getString("SuccessAddEntity") :
                  "&b{ENTITY} &aSuccessfully added to the catchable list!";

          removeEntityDoesNotExist = localeYamlConfig.isSet("RemoveEntityDoesNotExist") ? localeYamlConfig.getString("RemoveEntityDoesNotExist") :
                  "&cPlease enter the name of the entity to be removed from the catchable list";

          removeEntityNotFound = localeYamlConfig.isSet("RemoveEntityNotFound") ? localeYamlConfig.getString("RemoveEntityNotFound") :
                  "&cNo entity found in the catchable list";

          successRemove = localeYamlConfig.isSet("SuccessRemove") ? localeYamlConfig.getString("SuccessRemove") :
                  "&aSuccessfully removed from the catchable list &b{ENTITY}";

          skullDoesNotFound = localeYamlConfig.isSet("SkullDoesNotFound") ? localeYamlConfig.getString("SkullDoesNotFound") :
                  "&cThe data stored in the skull is missing";

          locationUnsafe = localeYamlConfig.isSet("LocationUnsafe") ? localeYamlConfig.getString("LocationUnsafe") :
                  "&cCould not find a safe area to spawn, so this request has been cancelled";

          noResidencePermissions = localeYamlConfig.isSet("NoResidencePermissions") ? localeYamlConfig.getString("NoResidencePermissions") :
                  "&cYou can’t spawn entity here because you are lacking {FLAG} permission for this residense";

          allowCatchMessage = localeYamlConfig.isSet("AllowCatchMessage") ? localeYamlConfig.getString("AllowCatchMessage") :
                  "&b{ENTITY} &6allow catch: {STATUS} !";

          allEntityAddSuccess = localeYamlConfig.isSet("AllEntityAddSuccess") ? localeYamlConfig.getString("AllEntityAddSuccess") :
                  "&aAll Entity added success!";

          allEntityRemoveSuccess = localeYamlConfig.isSet("AllEntityRemoveSuccess") ? localeYamlConfig.getString("AllEntityRemoveSuccess") :
                  "&eAll Entity removed success!";

          playerNotExist = localeYamlConfig.isSet("PlayerNotExist") ? localeYamlConfig.getString("PlayerNotExist") :
                  "&cPlease enter a player that want to give the item!";

          unknownOrOfflinePlayer = localeYamlConfig.isSet("UnknownOrOfflinePlayer") ? localeYamlConfig.getString("UnknownOrOfflinePlayer") :
                  "&cPlayer {PLAYER} not find!";

          successGiveItemToPlayer = localeYamlConfig.isSet("SuccessGiveItemToPlayer") ? localeYamlConfig.getString("SuccessGiveItemToPlayer") :
                  "&aSuccess give {ITEM} to &a{PLAYER}!";

          invalidItemAmount = localeYamlConfig.isSet("invalidItemAmount") ? localeYamlConfig.getString("invalidItemAmount") :
                  "&cInvalid item amount!";
          catchableListTitle = localeYamlConfig.isSet("catchableListTitle") ? localeYamlConfig.getString("catchableListTitle") :
                  "&lCatch List Settings";
          prevPage = localeYamlConfig.isSet("prevPage") ? localeYamlConfig.getString("prevPage") :
                  "&aPrevious Page";
          nextPage = localeYamlConfig.isSet("nextPage") ? localeYamlConfig.getString("nextPage") :
                  "&aNext Page";
          currentPage = localeYamlConfig.isSet("currentPage") ? localeYamlConfig.getString("currentPage") :
                  "&eCurrent Page: &a{PAGE}";
          catchBallName = localeYamlConfig.isSet("Items.CatchBall.DisplayName") ? localeYamlConfig.getString("Items.CatchBall.DisplayName") :
                  "&aCat&bch &cball";

          catchBallLore = localeYamlConfig.isSet("Items.CatchBall.Lore") ? localeYamlConfig.getStringList("Items.CatchBall.Lore") :
                  Arrays.asList("&7Used to capture catchable entity");

          goldEggName = localeYamlConfig.isSet("Items.GoldEgg.DisplayName") ? localeYamlConfig.getString("Items.GoldEgg.DisplayName") :
                  "&6GoldEgg";

          goldEggLore = localeYamlConfig.isSet("Items.GoldEgg.Lore") ? localeYamlConfig.getStringList("Items.GoldEgg.Lore") :
                  Arrays.asList("&7Chickens have a &e{PERCENT} &7chance of them lay GOLDEGG",
                          "&7Can be used with CraftRecipe to make CatchBall");
     }
}

package com.pohuang;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.bekvon.bukkit.residence.containers.Flags;
import com.pohuang.Recipe.BallRecipe;
import com.pohuang.items.Ball;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import me.ryanhamshire.GriefPrevention.ClaimPermission;

public class ConfigSetting {
    private final static Plugin plugin = CatchBall.getPlugin(CatchBall.class);
    public static Boolean enabled;
    public static List<EntityType> catchableEntity = new ArrayList<>();
    public static Boolean chickenDropGoldEgg;
    public static int chickenDropGoldEggChance;
    public static String catchSuccessSound;
    public static String catchableListTitle;
    public static String prevPage;
    public static String nextPage;
    public static String currentPage;
    public static List<String> guiSkullLore = new ArrayList<>();
    public static YamlConfiguration entityFile;

    public static List<String> dropSkullLore = new ArrayList<>();

    public static Boolean recipeEnabled;
    
    public static String catchBallName;
    public static List<String> catchBallLore = new ArrayList<>();

    public static String goldEggName;
    public static List<String> goldEggLore = new ArrayList<>();

    public static List<String> residenceFlag = new ArrayList<>();

    public static List<String> griefPreventionFlag = new ArrayList<>();
    
    public static String consoleExcuteCommand;
    public static String noPermission;
    public static String playerInventoryFull;
    public static String argDoesNotExist;
    public static String argTooMuch;
    public static String unknownCommandArgument;
    public static String successGetBall;
    public static String reloadSuccess;
    public static String canNotCatchable;
    public static String ballHitBlock;
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

    public static Boolean checkConfig() {
        // check if the file exist
        if (!load("configFile").exists()) { plugin.saveResource("config.yml", false); }
        if (!load("entityFile").exists()) { plugin.saveResource("entity.yml", false); }
        
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        enabled = config.isSet("Enabled") ? config.getBoolean("Enabled") : true;
        chickenDropGoldEgg = config.isSet("ChickenDropGoldEgg") ? config.getBoolean("ChickenDropGoldEgg") : true;
        
        if (!enabled) { return false; }

        entityFile = getEntityFile();
        
        chickenDropGoldEggChance = config.isSet("ChickenDropGoldEggChance") ? Integer.parseInt
            (config.getString("ChickenDropGoldEggChance").replace("%", ""))  : 50;
        
        catchSuccessSound = config.isSet("CatchSuccessSound") ? config.getString("CatchSuccessSound").toUpperCase() :
            "ENTITY_ARROW_HIT_PLAYER".toUpperCase();

        catchableListTitle = "&lCatch List Settings";

        prevPage = "&aPrevious Page";
        
        nextPage = "&aNext Page";

        currentPage = "&eCurrent Page: &a{PAGE}";

        guiSkullLore = Arrays.asList("&6Custom Name: {ENTITY}", "&6Allow Catch: {CATCHABLE}"); 

        dropSkullLore = config.isSet("DropSkullLore") ? config.getStringList("DropSkullLore") : Arrays.asList("&eENTITY TYPE: &a{ENTITY}",
            "&eThe Catcher: &a{PLAYER}", "&eCapture Time: &a{TIME}", "&eCapture Location: &a{LOCATION}") ;

        recipeEnabled = config.isSet("Recipe.enabled") ? config.getBoolean("Recipe.enabled") : true;
        
        catchBallName = config.isSet("Items.CatchBall.DisplayName") ? config.getString("Items.CatchBall.DisplayName") : 
            "&aCat&bch &cball";
        
        catchBallLore = config.isSet("Items.CatchBall.Lore") ? config.getStringList("Items.CatchBall.Lore") : 
            Arrays.asList("&7Used to capture catchable entity");

        goldEggName = config.isSet("Items.GoldEgg.DisplayName") ? config.getString("Items.GoldEgg.DisplayName") : 
        "&6GoldEgg";
        
        goldEggLore = config.isSet("Items.GoldEgg.Lore") ? config.getStringList("Items.GoldEgg.Lore") : 
            Arrays.asList("&7Chickens have a &e{PERCENT} &7chance of them lay GOLDEGG",
            "&7Can be used with CraftRecipe to make CatchBall");

        residenceFlag = config.isSet("ResidenceFlag") ? config.getStringList("ResidenceFlag") :
            Arrays.asList("animalkilling");

        griefPreventionFlag = config.isSet("GriefPreventionFlag") ? config.getStringList("GriefPreventionFlag") :
            Arrays.asList("Access");

        consoleExcuteCommand = config.isSet("Message.ConsoleExcuteCommand") ? config.getString("Message.ConsoleExcuteCommand") : 
            "&cThis command can only be executed by player!" ;

        noPermission = config.isSet("Message.NoPermission") ? config.getString("Message.NoPermission") :
            "&cYou have no permission!";
        
        playerInventoryFull = config.isSet("Message.PlayerInventoryFull") ? config.getString("Message.PlayerInventoryFull") :
            "&aYour inventory is full, so item falls at your feet!";

        argDoesNotExist = config.isSet("Message.ArgDoesNotExist") ? config.getString("Message.ArgDoesNotExist") :
            "&aCommands Usage: \n&b/ctb get &7Get special items of plugins.\n&b/ctb reload &7Reload plugin.\n&b/ctb list &7Lists all catchable entities and states\n&b/ctb add &7Add the entity to the catch list\n&b/ctb remove &7Remove the entity from the catchable list";

        argTooMuch = config.isSet("Message.ArgTooMuch") ? config.getString("Message.ArgTooMuch") :
            "&cToo many Argument!";

        unknownCommandArgument = config.isSet("Message.UnknownCommandArgument") ? config.getString("Message.UnknownCommandArgument") :
            "&cUnknown Argument!";
        
        successGetBall = config.isSet("Message.SuccessGetBall") ? config.getString("Message.SuccessGetBall") :
            "&aSuccessfully get {ITEM}";

        reloadSuccess = config.isSet("Message.ReloadSuccess") ? config.getString("Message.ReloadSuccess") :
            "&aThe plugin reloaded successfully!";

        canNotCatchable = config.isSet("Message.CanNotCatchable") ? config.getString("Message.CanNotCatchable") :
            "&cThis entity cannot be captured, so &e{BALL} &7fell in &e{LOCATION}";

        ballHitBlock = config.isSet("Message.BallHitBlock") ? config.getString("Message.BallHitBlock") :
            "&cYou did not hit a entity,So {BALL} fell in {LOCATION}";

        catchSuccess = config.isSet("Message.CatchSuccess") ? config.getString("Message.CatchSuccess") :
            "&aSuccessfully captured {ENTITY} location: {LOCATION}";

        itemDoesNotExist = config.isSet("Message.ItemDoesNotExist") ? config.getString("Message.ItemDoesNotExist") :
            "&cPlease enter the item to be picked up  &7CatchBall | GoldEgg";

        itemNameError = config.isSet("Message.ItemNameError") ? config.getString("Message.ItemNameError") :
            "&cPlease enter the correct item name  &7CatchBall | GoldEgg";

        addEntityDoesNotExist = config.isSet("Message.AddEntityDoesNotExist") ? config.getString("Message.AddEntityDoesNotExist") :
            "&cPlease enter the name of the entity to be added to the list of captured creatures!";

        unknownEntityType = config.isSet("Message.UnknownEntityType") ? config.getString("Message.UnknownEntityType") :
            "&cUnknown entity type!";

        entityDoesExists = config.isSet("Message.EntityDoesExists") ? config.getString("Message.EntityDoesExists") :
            "&cThe entity already exists in the catchable list!";

        successAddEntity = config.isSet("Message.SuccessAddEntity") ? config.getString("Message.SuccessAddEntity") :
            "&b{ENTITY} &aSuccessfully added to the catchable list!";

        removeEntityDoesNotExist = config.isSet("Message.RemoveEntityDoesNotExist") ? config.getString("Message.RemoveEntityDoesNotExist") :
            "&cPlease enter the name of the entity to be removed from the catchable list";

        removeEntityNotFound = config.isSet("Message.RemoveEntityNotFound") ? config.getString("Message.RemoveEntityNotFound") :
            "&cNo entity found in the catchable list";

        successRemove = config.isSet("Message.SuccessRemove") ? config.getString("Message.SuccessRemove") :
            "&aSuccessfully removed from the catchable list &b{ENTITY}";

        skullDoesNotFound = config.isSet("Message.SkullDoesNotFound") ? config.getString("Message.SkullDoesNotFound") :
            "&cThe data stored in the skull is missing";

        locationUnsafe = config.isSet("Message.LocationUnsafe") ? config.getString("Message.LocationUnsafe") :
            "&cCould not find a safe area to spawn, so this request has been cancelled";

        noResidencePermissions = config.isSet("Message.NoResidencePermissions") ? config.getString("Message.NoResidencePermissions") :
            "&cYou can’t spawn entity here because you are lacking {FLAG} permission for this residense";

        allowCatchMessage = config.isSet("Message.AllowCatchMessage") ? config.getString("Message.AllowCatchMessage") : 
            "&b{ENTITY} &6allow catch: {STATUS} !";
        
        new Ball();
        new BallRecipe();

        if (!catchableEntity.isEmpty()) { catchableEntity.clear(); }
        
        try {
            if (plugin.getServer().getPluginManager().getPlugin("Residence") != null) {
                residenceFlag.stream().
                map(flag -> Flags.valueOf(flag)).collect(Collectors.toList());
            }
        } catch (IllegalArgumentException e) {
            plugin.getLogger().info(ChatColor.RED + e.getMessage());
            plugin.getLogger().info(ChatColor.RED + "Unknown Residence flag!");
            plugin.getLogger().info(ChatColor.RED + "Please check your config setting!");
            residenceFlag.clear();
            residenceFlag.add("animalkilling");
        }

        try {
            if (plugin.getServer().getPluginManager().getPlugin("GriefPrevention") != null) {
                for (int i=0; i < griefPreventionFlag.size(); i++) {
                    String[] flag = griefPreventionFlag.get(i).split("");
                    flag[0] = flag[0].toUpperCase();
                    ClaimPermission.valueOf(String.join("", flag));
                }
            }
        } catch (IllegalArgumentException e) {
            plugin.getLogger().info(ChatColor.RED + e.getMessage());
            plugin.getLogger().info(ChatColor.RED + "Unknown GriefPrevention flag!");
            plugin.getLogger().info(ChatColor.RED + "Please check your config setting!");
            griefPreventionFlag.clear();
            griefPreventionFlag.add("Access");
        }

        for (String entity : entityFile.getStringList("CatchableEntity")) {            
            try {
                if (EntityType.valueOf(entity.toUpperCase()) != null) {

                    // entityType only can be recive UpperCase words
                    catchableEntity.add(EntityType.valueOf(entity.toUpperCase()));    
                }
                
            // There is a common issue that you put an unknown entityType in the list of CatchableEntity
            } catch (IllegalArgumentException e) {
                plugin.getLogger().info("§cunkown EntityType: " + entity);
                plugin.getLogger().info("§cPlease check \"CatchableEntity\" list in config.yml");
                plugin.getLogger().info("§cError Message: " + e.getMessage());
            }
            
        }

        return true;
    }

    private static File load(String fileName) {
        switch (fileName) {
            case "configFile":
                return new File(plugin.getDataFolder().getAbsolutePath() + "/config.yml");
            case "entityFile":
                return new File(plugin.getDataFolder().getAbsolutePath() + "/entity.yml");
        }
        return null;
    }
    
    public static String toChat(String text, String location, String entity) {
        if (text.contains("{BALL}")) { text = text.replace("{BALL}", catchBallName); }
        if (text.contains("{LOCATION}")) { text = text.replace("{LOCATION}", location); }
        if (text.contains("ENTITY")) {text = text.replace("{ENTITY}", new HeadDrop().getEntityDisplayname(entity)); }

        text = ChatColor.translateAlternateColorCodes('&', text);
        return text;
    }

    public static YamlConfiguration getEntityFile() { 
        YamlConfiguration file = YamlConfiguration.loadConfiguration(load("entityFile"));
        return file; 
    }

    public static void saveEntityList() {
        YamlConfiguration entityFile = ConfigSetting.entityFile;                   
        List<String> savelist = new ArrayList<>();
        
        for (int i=0; i < ConfigSetting.catchableEntity.toArray().length; i++) {
            savelist.add(ConfigSetting.catchableEntity.get(i).toString());
        }

        entityFile.set("CatchableEntity", savelist.toArray());

        try{
            entityFile.save(load("entityFile"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean getEntityFileExist(String entityName) {
        List<String> entityList = new ArrayList<>();
        entityList.addAll(getEntityFile().getConfigurationSection("EntityList").getKeys(false));
        return entityList.contains(entityName.toUpperCase());
    }
}

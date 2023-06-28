package com.github.nutt1101;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.stream.Collectors;

import com.bekvon.bukkit.residence.containers.Flags;

import com.github.nutt1101.Recipe.BallRecipe;
import com.github.nutt1101.utils.TranslationFileReader;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import me.ryanhamshire.GriefPrevention.ClaimPermission;

public class ConfigSetting {
    private final static Plugin plugin = CatchBall.plugin;
    public static Boolean enabled;
    public static String locale;
    public static Boolean updatecheck;
    public static String version;
    public static List<EntityType> catchableEntity = new ArrayList<>();
    public static Boolean chickenDropGoldEgg;
    public static int chickenDropGoldEggChance;
    public static String catchSuccessSound;
    public static YamlConfiguration entityFile;
    public static Boolean recipeEnabled;

    public static List<String> residenceFlag = new ArrayList<>();

    public static List<String> griefPreventionFlag = new ArrayList<>();
    public static Boolean allowCatchableTamedOwnerIsNull;

    /**
     * Initialize or reload the plugin
     */
    public static void checkConfig() {
        // check if the file exist
        if (!new File(plugin.getDataFolder().getAbsolutePath() + "/config.yml").exists()) { plugin.saveResource("config.yml", false); }

        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        enabled = !config.isSet("Enabled") || config.getBoolean("Enabled");
        locale = config.isSet("Locale") ? config.getString("Locale") : "en";

        entityFileCreate();

        chickenDropGoldEgg = !config.isSet("ChickenDropGoldEgg") || config.getBoolean("ChickenDropGoldEgg");

        if (!enabled) { return; }
        
        updatecheck = !config.isSet("Update-Check") || config.getBoolean("Update-Check");

        version = getPluginVersion();

        entityFile = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder().getAbsolutePath() + "/entity.yml"));
        
        chickenDropGoldEggChance = config.isSet("ChickenDropGoldEggChance") ? Integer.parseInt
            (config.getString("ChickenDropGoldEggChance").replace("%", ""))  : 50;
        
        catchSuccessSound = config.isSet("CatchSuccessSound") ? config.getString("CatchSuccessSound").toUpperCase() :
            "ENTITY_ARROW_HIT_PLAYER".toUpperCase();


        recipeEnabled = !config.isSet("Recipe.enabled") || config.getBoolean("Recipe.enabled");

        residenceFlag = config.isSet("ResidenceFlag") ? config.getStringList("ResidenceFlag") :
            Arrays.asList("animalkilling");

        griefPreventionFlag = config.isSet("GriefPreventionFlag") ? config.getStringList("GriefPreventionFlag") :
            Arrays.asList("Access");
        allowCatchableTamedOwnerIsNull = !config.isSet("AllowCatchableTamedOwnerIsNull") || config.getBoolean("AllowCatchableTamedOwnerIsNull");


        try {
            TranslationFileReader.init();
        } catch (IOException e) {
            e.printStackTrace();
            plugin.getLogger().log(Level.WARNING, ChatColor.RED +
                    String.format("The locale you have selected '%s' is currently not supported", locale)
            );
            plugin.getLogger().log(Level.WARNING, ChatColor.RED + "We only support: en, zh_tw");
            locale = "en";
        }

        new BallRecipe();

        if (!catchableEntity.isEmpty()) { catchableEntity.clear(); }
        
        try {
            if (plugin.getServer().getPluginManager().getPlugin("Residence") != null) {
                residenceFlag.stream().map(flag -> Flags.valueOf(flag)).collect(Collectors.toList());
            }

        } catch (IllegalArgumentException e) {
            plugin.getLogger().log(Level.WARNING, ChatColor.RED + e.getMessage());
            plugin.getLogger().log(Level.WARNING, ChatColor.RED + "Unknown Residence flag!");
            plugin.getLogger().log(Level.WARNING, ChatColor.RED + "Please check your config setting!");
            residenceFlag.clear();
            residenceFlag.add("animalkilling");
        }

        try {
            if (plugin.getServer().getPluginManager().getPlugin("GriefPrevention") != null) {
                for (int i=0; i < griefPreventionFlag.size(); i++) {
                    String[] flag = griefPreventionFlag.get(i).split("");
                    flag[0] = flag[0].toUpperCase();
                    ClaimPermission.valueOf(String.join("", flag));
                    griefPreventionFlag.set(i, String.join("", flag));
                }
            }

        } catch (IllegalArgumentException e) {
            plugin.getLogger().log(Level.WARNING, ChatColor.RED + e.getMessage());
            plugin.getLogger().log(Level.WARNING, ChatColor.RED + "Unknown Residence flag!");
            plugin.getLogger().log(Level.WARNING, ChatColor.RED + "Please check your config setting!");
            griefPreventionFlag.clear();
            griefPreventionFlag.add("Access");
        }

        for (String entity : config.getStringList("CatchableEntity")) {
            try {
                EntityType.valueOf(entity.toUpperCase());// entityType only can be recive UpperCase words
                catchableEntity.add(EntityType.valueOf(entity.toUpperCase()));

                // There is a common issue that you put an unknown entityType in the list of CatchableEntity
            } catch (IllegalArgumentException e) {
            }
        }

        if (updatecheck) {
            if (!isLatestVersion(plugin.getDescription().getVersion(), version)) {
                plugin.getLogger().log(Level.INFO, ChatColor.LIGHT_PURPLE + "Plugin has a new update available: " + version);
                plugin.getLogger().log(Level.INFO, ChatColor.GREEN + "Download here: https://www.spigotmc.org/resources/catchball.94867/");
            } else {
                plugin.getLogger().log(Level.INFO, ChatColor.GREEN + "Plugin is already the latest version");
            }
        }
    }

    /**
     * Check the version of server to determine entity.yml file output version.
     */
    public static void entityFileCreate() {
        File file = new File(plugin.getDataFolder() , "entity.yml");
        InputStream inputStream = plugin.getResource(  "entity/" + locale + ".yml");

        if (inputStream != null) {
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                inputStream.transferTo(outputStream);
                outputStream.close();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            plugin.getLogger().log(Level.WARNING, ChatColor.RED + "Unknown Error make plugin file can not place!");
        }
    }

    /**
     * Get entityDisplayName saved on entity.yml.
     * @param entityName name of saved entity
     * @return entityDisplayName
     */
    public static String getEntityDisplayName(String entityName) {
        if (entityFile.getConfigurationSection("EntityList").contains(entityName)) {
            return entityFile.getString("EntityList." + entityName.toUpperCase() + ".DisplayName");
        }
        return entityName;
    }
    
    /**
     * Replace the message argument from config.yml.
     * @param location replace {L＃OCATION} from source message
     * @param entity replace {ENTITY} from source message
     * @return replaced message
     */
    public static String toChat(String message, String location, String entity) {
        message = message.contains("{BALL}") ? message.replace("{BALL}", TranslationFileReader.catchBallName) : message;
        message = message.contains("{LOCATION}") ? message.replace("{LOCATION}", location) : message;
        message = message.contains("{ENTITY}") ? message.replace("{ENTITY}", getEntityDisplayName(entity)) : message;

        return ChatColor.translateAlternateColorCodes('&', message);
    }
    
    
    /**
     * Save the entity.yml file, if the catchableEntity changes.
     */
    public static void saveEntityList() {
        FileConfiguration fileConfiguration = plugin.getConfig();
        List<String> savelist = new ArrayList<>();

        ConfigSetting.catchableEntity.forEach(e -> savelist.add(e.toString()));

        fileConfiguration.set("CatchableEntity", savelist.toArray());

        try{
            fileConfiguration.save(new File(plugin.getDataFolder().getAbsolutePath() + "/config.yml"));
        } catch (IOException e) { e.printStackTrace(); }
    }


    /**
     * check version of plugin
     * @return version of plugin
     */
    public static String getPluginVersion() {
        try {
            InputStream inputStream = new URL(("https://api.spigotmc.org/legacy/update.php?resource=94867")).openStream();
            Scanner scanner = new Scanner(inputStream);
            String version = scanner.next().replace("v", "");
            
            scanner.close();

            return version;
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, ChatColor.RED + "Cannot check for plugin version: " + e.getMessage());
        }

        return "";
        
    }
    
    public static boolean isLatestVersion(String current, String latest) {
        if (current.equals(latest)) return true;

        current = current.replace(".","");
        latest = latest.replace(".", "");

        for (int i = 0; i < 3; i++) {
            if (current.charAt(i) - '0' < latest.charAt(i) - '0') return false;
        }

        return true;
    }
}

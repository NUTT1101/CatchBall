package com.pohuang;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

public class ConfigSetting {
    private static Plugin plugin = CatchBall.getPlugin(CatchBall.class);
    public static Boolean enabled;
    public static List<EntityType> catchableEntity = new ArrayList<>();
    public static Boolean chickenDropGoldEgg;
    public static int chickenDropGoldEggChance;
    public static String catchSucessSound;
    public static String catchableListTitle;
    public static String prevPage;
    public static String nextPage;
    public static String currentPage;
    public static List<String> guiSkullLore;

    public static List<String> dropSkullLore;

    public static Boolean recipeEnabled;
    
    public static String catchBallName;
    public static List<String> catchBallLore;

    public static String goldEggName;
    public static List<String> goldEggLore;

    public static List<String> residenceFlag;
    
    public static String consoleExcuteCommand;
    public static String noPermission;
    public static String playerInventoryFull;
    public static String argDoesNotExist;
    public static String argTooMuch;
    public static String unknownCommandArgument;
    public static String sucessGetBall;
    public static String reloadSucess;
    public static String canNotCatchable;
    public static String ballHitBlock;
    public static String catchSucess;
    public static String addEntityDoesNotExist;
    public static String unknownEntityType;
    public static String entityDoesExists;
    public static String sucessAddEntity;
    public static String itemDoesNotExist;
    public static String itemNameError;
    public static String removeEntityDoesNotExist;
    public static String removeEntityNotFound;
    public static String sucessRemove;
    public static String skullDoesNotFound;
    public static String locationUnsafe;
    public static String noResidencePermissions;

    public static Boolean checkConfig() {
        // check if the file exist
        if (!load("configFile").exists()) { plugin.saveResource("config.yml", false); }
        if (!load("entityFile").exists()) { plugin.saveResource("entity.yml", false); }
        
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        FileConfiguration entityfile = YamlConfiguration.loadConfiguration(load("entityFile"));

        enabled = config.isSet("Enabled") ? config.getBoolean("Enabled") : true;
        chickenDropGoldEgg = config.isSet("ChickenDropGoldEgg") ? config.getBoolean("ChickenDropGoldEgg") : true;
        
        chickenDropGoldEggChance = config.isSet("ChickenDropGoldEggChance") ? Integer.parseInt
        (config.getString("ChickenDropGoldEggChance").replace("%", ""))  : 50;
        
        catchSucessSound = config.isSet("CatchSucessSound") ? config.getString("CatchSucessSound").toUpperCase() :
        "ENTITY_ARROW_HIT_PLAYER".toUpperCase();

        catchableListTitle = config.isSet("CatchableListTitle") ? config.getString("CatchableListTitle") : 
        "&l生物捕捉設定";

        prevPage = config.isSet("PrevPage") ? config.getString("PrevPage") : "&a上一頁";
        
        nextPage = config.isSet("NextPage") ? config.getString("NextPage") : "&a下一頁";

        currentPage = config.isSet("CurrentPage") ? config.getString("CurrentPage") : "&d當前頁面: &e{PAGE}";

        guiSkullLore = config.isSet("GUISkullLore") ? config.getStringList("GUISkullLore") : Arrays.asList("&6自訂義名稱: {ENTITY}", 
            "&6允許抓捕: {CATCHABLE}");

        dropSkullLore = config.isSet("DropSkullLore") ? config.getStringList("DropSkullLore") : Arrays.asList("&e生物種類: &a{ENTITY}",
        "&e抓捕者: &a{PLAYER}", "&e抓捕時間: &a{TIME}", "&e抓捕地點: &a{LOCATION}") ;

        recipeEnabled = config.isSet("Recipe.enabled") ? config.getBoolean("Recipe.enabled") : true;
        
        catchBallName = config.isSet("Items.CatchBall.DisplayName") ? config.getString("Items.CatchBall.DisplayName") : 
        "&a捕&b捉&c球";
        
        catchBallLore = config.isSet("Items.CatchBall.Lore") ? config.getStringList("Items.CatchBall.Lore") : 
        Arrays.asList("&7用於捕捉特定生物");

        goldEggName = config.isSet("Items.GoldEgg.DisplayName") ? config.getString("Items.GoldEgg.DisplayName") : 
        "&6金雞蛋";
        
        goldEggLore = config.isSet("Items.GoldEgg.Lore") ? config.getStringList("Items.GoldEgg.Lore") : 
        Arrays.asList("&7雞在生蛋時會有&e{PERCENT}%&7機率生出", "&7可用於合成捕捉球");

        residenceFlag = config.isSet("ResidenceFlag") ? config.getStringList("ResidenceFlag") :
        Arrays.asList("animalkilling");

        consoleExcuteCommand = config.isSet("Message.ConsoleExcuteCommand") ? config.getString("Message.ConsoleExcuteCommand") : 
        "&c此指令只有玩家才可執行!" ;

        noPermission = config.isSet("Message.NoPermission") ? config.getString("Message.NoPermission") :
        "&c您沒有權限!";
        
        playerInventoryFull = config.isSet("Message.PlayerInventoryFull") ? config.getString("Message.PlayerInventoryFull") :
        "&a您背包物品已滿，故物品掉落在您腳下";

        argDoesNotExist = config.isSet("Message.ArgDoesNotExist") ? config.getString("Message.ArgDoesNotExist") :
        "&a指令使用方法: \n&b/ctb get &7取得插件特殊物品\n&b/ctb reload &7重新整理插件設定\n&b/ctb list &7列出所有可捕捉實體與捕捉狀態\n&b/ctb add &7將指定的實體新增至捕捉清單\n&b/ctb remove &7將指定的實體從捕捉清單移除";

        argTooMuch = config.isSet("Message.ArgTooMuch") ? config.getString("Message.ArgTooMuch") :
        "&c輸入的指令參數過多!";

        unknownCommandArgument = config.isSet("Message.UnknownCommandArgument") ? config.getString("Message.UnknownCommandArgument") :
        "&c未知的指令參數!";
        
        sucessGetBall = config.isSet("Message.SucessGetBall") ? config.getString("Message.SucessGetBall") :
        "&a成功提取 {BALL}";

        reloadSucess = config.isSet("Message.ReloadSucess") ? config.getString("Message.ReloadSucess") :
        "&a插件重新整理完畢!";

        canNotCatchable = config.isSet("Message.CanNotCatchable") ? config.getString("Message.CanNotCatchable") :
        "&c此生物無法捕捉，故 {BALL} &c掉落在 &e{LOCATION}";

        ballHitBlock = config.isSet("Message.BallHitBlock") ? config.getString("Message.BallHitBlock") :
        "您未捕捉特定的生物，故 {BALL} 掉落在 &e{LOCATION}";

        catchSucess = config.isSet("Message.CatchSucess") ? config.getString("Message.CatchSucess") :
        "&a成功捕捉 {ENTITY} 位置: {LOCATION}";

        itemDoesNotExist = config.isSet("Message.ItemDoesNotExist") ? config.getString("Message.ItemDoesNotExist") :
        "&c請輸入要提取的物品  &7CatchBall | GoldEgg";

        itemNameError = config.isSet("Message.ItemNameError") ? config.getString("Message.ItemNameError") :
        "&c請輸入正確的物品名稱  &7CatchBall | GoldEgg";

        addEntityDoesNotExist = config.isSet("Message.AddEntityDoesNotExist") ? config.getString("Message.AddEntityDoesNotExist") :
        "&c請輸入要新增至捕捉生物清單的實體名稱!";

        unknownEntityType = config.isSet("Message.UnknownEntityType") ? config.getString("Message.UnknownEntityType") :
        "&c未知的實體名稱!";

        entityDoesExists = config.isSet("Message.EntityDoesExists") ? config.getString("Message.EntityDoesExists") :
        "&c實體已存在列表裡面!";

        sucessAddEntity = config.isSet("Message.SucessAddEntity") ? config.getString("Message.SucessAddEntity") :
        "&a成功將 &b{ENTITY} &a新增至捕捉清單!";

        removeEntityDoesNotExist = config.isSet("Message.RemoveEntityDoesNotExist") ? config.getString("Message.RemoveEntityDoesNotExist") :
        "&c請輸入要從捕捉列表移除的生物名稱";

        removeEntityNotFound = config.isSet("Message.RemoveEntityNotFound") ? config.getString("Message.RemoveEntityNotFound") :
        "&c未在捕捉列表內找到實體";

        sucessRemove = config.isSet("Message.SucessRemove") ? config.getString("Message.SucessRemove") :
        "&a成功從捕捉列表移除 &b{ENTITY}";

        skullDoesNotFound = config.isSet("Message.SkullDoesNotFound") ? config.getString("Message.SkullDoesNotFound") :
        "&c頭顱存放的實體數據遺失";

        locationUnsafe = config.isSet("Message.LocationUnsafe") ? config.getString("Message.LocationUnsafe") :
        "&c未能找到一個安全的區域生成，故此次重生已取消";

        noResidencePermissions = config.isSet("Message.NoResidencePermissions") ? config.getString("Message.NoResidencePermissions") :
        "&c您無法在此生成或捕捉動物，因為您缺少了此領地的 &e{FLAG} &c權限";
        
        if (!enabled) { return false; }
        
        if (!catchableEntity.isEmpty()) { catchableEntity.clear(); }
        
        for (String entity : entityfile.getStringList("CatchableEntity")) {            
            try {
                if (EntityType.valueOf(entity.toUpperCase()) != null) {

                    // entityType only can be recive UpperCase words
                    catchableEntity.add(EntityType.valueOf(entity.toUpperCase()));    
                }
                
            // There is a common issue that you put an unknown entityType in the list of CatchableEntity
            } catch (Exception e) {
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
        YamlConfiguration entityFile = YamlConfiguration.loadConfiguration(load("entityFile"));                   
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

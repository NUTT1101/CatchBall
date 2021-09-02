package com.pohuang;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.pohuang.event.HitEvent;
import com.pohuang.nms.SaveNMS_1_16;
import com.pohuang.nms.SaveNMS_1_17;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class HeadDrop {
    private final Plugin plugin = CatchBall.getPlugin(CatchBall.class);
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
    
    public ItemStack getEntityHead(Entity hitEntity , Player player) {
        
        YamlConfiguration entityFile = ConfigSetting.entityFile;

        ItemStack entityHead = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta headMeta = entityHead.getItemMeta();
        
        String hitEntityUuid = hitEntity.getUniqueId().toString();
        PersistentDataContainer headData = headMeta.getPersistentDataContainer();
        headData.set(new NamespacedKey(plugin, "skullData"), PersistentDataType.STRING, hitEntityUuid);
        
        Date now = new Date();
        String location = "(" + hitEntity.getWorld().getName() + ") " + 
            new HitEvent().getCoordinate(hitEntity.getLocation());
        
        if (hitEntity.getCustomName() != null) {
            headMeta.setDisplayName(ChatColor.WHITE + hitEntity.getCustomName());
        } else {
            headMeta.setDisplayName(ChatColor.WHITE + entityFile.getString("EntityList." + hitEntity.getType().toString() + ".DisplayName"));
        }

        List<String> headLore = new ArrayList<>();
        
        if (player == null) {
            headLore.addAll(ConfigSetting.dropSkullLore.stream().map(lore -> ChatColor.translateAlternateColorCodes('&', lore).
            replace("{ENTITY}", hitEntity.getType().toString()).replace("{PLAYER}", "Dispenser").replace("{TIME}", format.format(now)).
            replace("{LOCATION}", location)).collect(Collectors.toList()));
        } else {
            headLore.addAll(ConfigSetting.dropSkullLore.stream().map(lore -> ChatColor.translateAlternateColorCodes('&', lore).
            replace("{ENTITY}", hitEntity.getType().toString()).replace("{PLAYER}", player.getName()).replace("{TIME}", format.format(now)).
            replace("{LOCATION}", location)).collect(Collectors.toList()));
        }

        switch (plugin.getServer().getClass().getPackage().getName().split("\\.")[3]) {
            case "v1_17_R1":
                headMeta = new SaveNMS_1_17().saveEntityNMS(hitEntity, headMeta);
                break;
        
            case "v1_16_R3":
            headMeta = new SaveNMS_1_16().saveEntityNMS(hitEntity, headMeta);
                break;
            default:
                break;
        }


        headMeta.setLore(headLore);
        entityHead.setItemMeta(headMeta);
        
        return skullTextures(entityHead, entityFile, hitEntity.getType().toString());
    }

    public ItemStack skullTextures(ItemStack head, YamlConfiguration entityFile, String skullName) {
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", entityFile.getString("EntityList." + skullName.toUpperCase() + ".Skull")));
        
        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);    
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        
        head.setItemMeta(skullMeta);
        
        return head;
    }

    public String getEntityDisplayname(String entityName) {
        YamlConfiguration entityFile = ConfigSetting.entityFile;
        List<String> entitList = new ArrayList<>();
        entitList.addAll(entityFile.getConfigurationSection("EntityList").getKeys(false));
        if (entitList.contains(entityName)) {
            return entityFile.getString("EntityList." + entityName.toUpperCase() + ".DisplayName");
        }
    
        return entityName;
    }
}

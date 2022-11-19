package com.github.nutt1101;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

import com.github.nutt1101.event.HitEvent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

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
    
    /**
     * When CatchBall hit catchable entity, It will drop the skull of hitEntity.
     * @param hitEntity the hitEntity of the hit event 
     * @param player player who throw the CatchBall 
     * @return the skull of saved hitEntity info
     */
    public ItemStack getEntityHead(Entity hitEntity , Player player) {
        
        YamlConfiguration entityFile = ConfigSetting.entityFile;

        ItemStack entityHead = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta headMeta = entityHead.getItemMeta();
        
        String hitEntityUuid = hitEntity.getUniqueId().toString();
        PersistentDataContainer headData = headMeta.getPersistentDataContainer();
        headData.set(new NamespacedKey(plugin, "skullData"), PersistentDataType.STRING, hitEntityUuid);
        
        Date now = new Date();
        String location = "(" + hitEntity.getWorld().getName() + ") " + 
            HitEvent.getCoordinate(hitEntity.getLocation());
        
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

        switch (CatchBall.getRealServerVersion()) {
            case "v1_19" -> headMeta = NBT_v1_19.saveEntityNBT(plugin, hitEntity, headMeta);
            case "v1_18" -> headMeta = NBT_v1_18.saveEntityNBT(plugin, hitEntity, headMeta);
            case "v1_17" -> headMeta = NBT_v1_17.saveEntityNBT(plugin, hitEntity, headMeta);
            case "v1_16" -> headMeta = NBT_v1_16.saveEntityNBT(plugin, hitEntity, headMeta);
            default -> {
                plugin.getLogger().log(Level.WARNING, "Save entity NBT error.");
            }
        }


        headMeta.setLore(headLore);
        entityHead.setItemMeta(headMeta);
        
        return skullTextures(entityHead, entityFile, hitEntity.getType().toString());
    }

    /**
     * Get the entity.yml file entity skull textures.
     * @param head skull of hitEntity
     * @param entityFile entity.yml file
     * @param entityType entityType 
     * @return head with texture value
     */
    public ItemStack skullTextures(ItemStack head, YamlConfiguration entityFile, String entityType) {
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", entityFile.getString("EntityList." + entityType.toUpperCase() + ".Skull")));
        
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
}

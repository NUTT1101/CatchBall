package com.pohuang;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.pohuang.event.HitEvent;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.nbt.NBTTagCompound;

public class HeadDrop {
    private Plugin plugin = CatchBall.getPlugin(CatchBall.class);
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
    
    public ItemStack getEntityHead(Entity hitEntity , Player player) {
        
        YamlConfiguration entityFile = ConfigSetting.getEntityFile();

        String entityType = hitEntity.getType().toString().toLowerCase();
        entityType = entityType.replace(entityType.charAt(0), entityType.toUpperCase().charAt(0));

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
        headLore.add("§e生物種類: §a" + getEntityDisplayname(hitEntity.getType().toString())); 
        headLore.add("§e抓捕者: §a" + player.getName());
        headLore.add("§e抓捕時間: §a" + format.format(now)); 
        headLore.add("§e抓捕地點: §a" + location);
        new BukkitRunnable() {
            
            @Override
            public void run() {   
                net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) hitEntity).getHandle();
                NBTTagCompound nbt = new NBTTagCompound();
                headMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "entity"), PersistentDataType.STRING, nmsEntity.save(nbt).toString());
                headMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "entityType"), PersistentDataType.STRING, hitEntity.getType().toString());
            }

        }.run();

        headMeta.setLore(headLore);
        entityHead.setItemMeta(headMeta);
        
        return skullTextures(entityHead, entityFile, hitEntity.getType().toString());
    }

    public ItemStack skullTextures(ItemStack item, YamlConfiguration entityFile, String skullName) {
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", entityFile.getString("EntityList." + skullName + ".Skull")));
        
        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);    
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        
        item.setItemMeta(skullMeta);
        
        return item;
    }

    public String getEntityDisplayname(String entityName) {
        YamlConfiguration entityFile = ConfigSetting.getEntityFile();
        List<String> entitList = new ArrayList<>();
        entitList.addAll(entityFile.getConfigurationSection("EntityList").getKeys(false));
        if (entitList.contains(entityName)) {
            return entityFile.getString("EntityList." + entityName + ".DisplayName");
        }
    
        return entityName;
    }
}

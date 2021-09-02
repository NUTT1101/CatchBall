package com.pohuang.nms;

import com.pohuang.CatchBall;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import net.minecraft.server.v1_16_R3.NBTTagCompound;



public class SaveNMS_1_16 implements NMS{
    
    private final Plugin plugin = CatchBall.getPlugin(CatchBall.class);

    @Override 
    public ItemMeta saveEntityNMS(Entity hitEntity, ItemMeta headMeta) {
        net.minecraft.server.v1_16_R3.Entity nmsEntity = ((CraftEntity) hitEntity).getHandle();
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        headMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "entity"), PersistentDataType.STRING, nmsEntity.save(nbtTagCompound).toString());
        headMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "entityType"), PersistentDataType.STRING, hitEntity.getType().toString());
        
        return headMeta;
    }
    
}

package com.pohuang.nms;

import com.pohuang.CatchBall;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import net.minecraft.nbt.NBTTagCompound;

public class SaveNMS_1_17 implements NMS{
    private final Plugin plugin = CatchBall.getPlugin(CatchBall.class);

    @Override
    public ItemMeta saveEntityNMS(Entity hitEntity, ItemMeta headMeta) {
        net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) hitEntity).getHandle();
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        headMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "entity"), PersistentDataType.STRING, nmsEntity.save(nbtTagCompound).toString());
        headMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "entityType"), PersistentDataType.STRING, hitEntity.getType().toString());
        
        return headMeta;
    }
    
}

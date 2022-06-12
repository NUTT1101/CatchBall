package com.github.nutt1101;

import net.minecraft.server.v1_16_R3.MojangsonParser;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;


public class NBT_v1_16 {

    public static ItemMeta saveEntityNBT(Plugin plugin, Entity hitEntity, ItemMeta headMeta) {
        net.minecraft.server.v1_16_R3.Entity nmsEntity = ((CraftEntity) hitEntity).getHandle();
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        headMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "entity"), PersistentDataType.STRING, nmsEntity.save(nbtTagCompound).toString());
        headMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "entityType"), PersistentDataType.STRING, hitEntity.getType().toString());
        return headMeta;
    }

    public static void loadEntityNBT(Plugin plugin, Entity entity, PersistentDataContainer data) {
        net.minecraft.server.v1_16_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        try {
            NBTTagCompound nbt = MojangsonParser.parse(data.get(new NamespacedKey(plugin, "entity"), PersistentDataType.STRING));
            nmsEntity.load(nbt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String isCustomEntity(Entity hitEntity) {
        net.minecraft.server.v1_16_R3.Entity nmsEntity = ((CraftEntity) hitEntity).getHandle();
        return nmsEntity.save(new NBTTagCompound()).getString("Paper.SpawnReason");
    }
}

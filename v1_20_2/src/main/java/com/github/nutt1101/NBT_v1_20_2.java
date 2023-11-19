package com.github.nutt1101;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;


public class NBT_v1_20_2 {

    public static ItemMeta saveEntityNBT(Plugin plugin, Entity hitEntity, ItemMeta headMeta) {
        net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) hitEntity).getHandle();
        headMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "entity"), PersistentDataType.STRING, nmsEntity.saveWithoutId(new CompoundTag()).toString());
        headMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "entityType"), PersistentDataType.STRING, hitEntity.getType().toString());
        return headMeta;
    }

    public static void loadEntityNBT(Plugin plugin, Entity entity, PersistentDataContainer data) {
        net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        try {
            CompoundTag nbt = TagParser.parseTag(data.get(new NamespacedKey(plugin, "entity"), PersistentDataType.STRING));
            nmsEntity.load(nbt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String isCustomEntity(Entity hitEntity) {
        net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) hitEntity).getHandle();
        return nmsEntity.saveWithoutId(new CompoundTag()).getString("Paper.SpawnReason");
    }
}

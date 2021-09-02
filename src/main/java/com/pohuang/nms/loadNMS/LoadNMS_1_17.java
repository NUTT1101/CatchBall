package com.pohuang.nms.loadNMS;

import com.pohuang.CatchBall;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;

public class LoadNMS_1_17 implements LoadNMS{
    final private Plugin plugin = CatchBall.getPlugin(CatchBall.class);

    @Override
    public void loadEntityNMS(Entity entity, PersistentDataContainer data) {
        net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle();

        try {
            NBTTagCompound nbt = MojangsonParser.parse(data.get(new NamespacedKey(plugin, "entity"), PersistentDataType.STRING));    
            nmsEntity.load(nbt);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}

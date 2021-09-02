package com.pohuang.nms.loadNMS;

import com.pohuang.CatchBall;

import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import net.minecraft.server.v1_16_R3.MojangsonParser;
import net.minecraft.server.v1_16_R3.NBTTagCompound;

public class LoadNMS_1_16 implements LoadNMS{
    final private Plugin plugin = CatchBall.getPlugin(CatchBall.class);

    @Override
    public void loadEntityNMS(Entity entity, PersistentDataContainer data) {
        net.minecraft.server.v1_16_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();

        try {
            NBTTagCompound nbt = MojangsonParser.parse(data.get(new NamespacedKey(plugin, "entity"), PersistentDataType.STRING));    
            nmsEntity.load(nbt);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}

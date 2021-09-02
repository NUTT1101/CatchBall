package com.pohuang.nms.checkByCustomEntity;

import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;

import net.minecraft.server.v1_16_R3.NBTTagCompound;

public class CheckByCustomEntity_1_16 implements CheckByCustomEntity{

    @Override
    public String checkIsCustomEntity(Entity hitEntity) {
        
        net.minecraft.server.v1_16_R3.Entity nmsEntity = ((CraftEntity) hitEntity).getHandle();
        
        return nmsEntity.save(new NBTTagCompound()).getString("Paper.SpawnReason");
    }
    
}

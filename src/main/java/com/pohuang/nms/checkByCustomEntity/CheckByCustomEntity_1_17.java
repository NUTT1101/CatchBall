package com.pohuang.nms.checkByCustomEntity;

import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

import net.minecraft.nbt.NBTTagCompound;

public class CheckByCustomEntity_1_17 implements CheckByCustomEntity{

    @Override
    public String checkIsCustomEntity(Entity hitEntity) {
        
        net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) hitEntity).getHandle();
        
        return nmsEntity.save(new NBTTagCompound()).getString("Paper.SpawnReason");
    }
    
}

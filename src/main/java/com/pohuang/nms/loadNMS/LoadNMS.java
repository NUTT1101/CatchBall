package com.pohuang.nms.loadNMS;

import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;

public interface LoadNMS {
    
    void loadEntityNMS(Entity entity, PersistentDataContainer data);
}

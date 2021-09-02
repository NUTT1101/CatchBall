package com.pohuang.nms;

import org.bukkit.entity.Entity;
import org.bukkit.inventory.meta.ItemMeta;

public interface NMS {
    
    ItemMeta saveEntityNMS(Entity hitEntity, ItemMeta headMeta);

}

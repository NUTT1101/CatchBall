package com.pohuang.event;

import java.util.Random;

import com.pohuang.ConfigSetting;
import com.pohuang.items.GoldEgg;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;


public class DropGoldEgg implements Listener{
    private EntityType chicken = EntityType.CHICKEN;
    private Random chance = new Random();
    private Boolean status = ConfigSetting.chickenDropGoldEgg;

    @EventHandler
    public void ChickenDropEgg(EntityDropItemEvent event){
        if (event.getEntityType().equals(chicken) && status){

            // 1% chance to drop goldEgg
            if (chance.nextInt(99) < ConfigSetting.chickenDropGoldEggChance) {
                event.setCancelled(true);
                event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), new GoldEgg().getGoldEgg());                
            }
        }
    }    
}

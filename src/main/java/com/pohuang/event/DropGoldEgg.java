package com.pohuang.event;

import java.util.Random;

import com.pohuang.ConfigSetting;
import com.pohuang.items.GoldEgg;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.inventory.ItemStack;


public class DropGoldEgg implements Listener{
    private EntityType chicken = EntityType.CHICKEN;
    private Random chance = new Random();

    @EventHandler
    public void ChickenDropEgg(EntityDropItemEvent event){
        if (!event.getItemDrop().getItemStack().equals(new ItemStack(Material.EGG))) { return; }

        ConfigSetting.chickenDropGoldEggChance = ConfigSetting.chickenDropGoldEggChance > 100 ? 100 : ConfigSetting.chickenDropGoldEggChance ;

        if (event.getEntityType().equals(chicken) && ConfigSetting.chickenDropGoldEgg){

            if (chance.nextInt(99) < ConfigSetting.chickenDropGoldEggChance) {
                event.setCancelled(true);
                event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), new GoldEgg().getGoldEgg());                
            }
        }
    }    
}

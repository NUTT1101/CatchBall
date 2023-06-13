package com.github.nutt1101.event;

import java.util.Random;

import com.github.nutt1101.ConfigSetting;

import com.github.nutt1101.items.GoldEgg;
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

        ConfigSetting.chickenDropGoldEggChance = Math.min(ConfigSetting.chickenDropGoldEggChance, 100);

        if (event.getEntityType().equals(chicken) && ConfigSetting.chickenDropGoldEgg){

            if (chance.nextInt(99) < ConfigSetting.chickenDropGoldEggChance) {
                event.setCancelled(true);
                event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), GoldEgg.makeGoldEgg());
            }
        }
    }
}
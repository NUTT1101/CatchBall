package com.github.nutt1101.items;

import java.util.stream.Collectors;


import com.github.nutt1101.ConfigSetting;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.md_5.bungee.api.ChatColor;

public class Ball {
    private ItemStack catchball;
     
    // create a catchBall
    public Ball() {
        catchball = new ItemStack(Material.SNOWBALL);
    
        ItemMeta meta = catchball.getItemMeta();
        meta.setDisplayName(ConfigSetting.toChat(ConfigSetting.catchBallName, "", ""));
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        meta.setLore(ConfigSetting.catchBallLore.stream().map(lore -> ChatColor.
            translateAlternateColorCodes('&', lore)).
            collect(Collectors.toList()));
        
        catchball.setItemMeta(meta);
    }
        
    public ItemStack getCatchBall() { return catchball;} 
}

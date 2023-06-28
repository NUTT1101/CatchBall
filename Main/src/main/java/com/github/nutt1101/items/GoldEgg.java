package com.github.nutt1101.items;

import java.util.stream.Collectors;

import com.github.nutt1101.ConfigSetting;

import com.github.nutt1101.utils.TranslationFileReader;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class GoldEgg {

    public static ItemStack makeGoldEgg() {
        ItemStack goldEgg = new ItemStack(Material.EGG);

        ItemMeta meta = goldEgg.getItemMeta();
        meta.setDisplayName(ConfigSetting.toChat(TranslationFileReader.goldEggName, "", ""));
        meta.addEnchant(Enchantment.DURABILITY, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        meta.setLore(TranslationFileReader.goldEggLore.stream().map(lore -> ChatColor.
                translateAlternateColorCodes('&', lore).replace("{PERCENT}", String.valueOf(ConfigSetting.
                        chickenDropGoldEggChance))).collect(Collectors.toList()));

        goldEgg.setItemMeta(meta);

        return goldEgg;
    }
}

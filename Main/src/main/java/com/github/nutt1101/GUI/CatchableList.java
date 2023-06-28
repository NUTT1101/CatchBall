package com.github.nutt1101.GUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


import com.github.nutt1101.ConfigSetting;
import com.github.nutt1101.HeadDrop;
import com.github.nutt1101.utils.TranslationFileReader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;

public class CatchableList {
    List<ItemStack> head = new ArrayList<>();
    ItemStack prevPage = new ItemStack(Material.PAPER);
    ItemStack nextPage = new ItemStack(Material.PAPER);
    ItemStack currentPage = new ItemStack(Material.OAK_SIGN);

    public ItemStack itemSet(ItemStack item, String displayname, int page) {
        displayname = displayname.contains("{PAGE}") ? displayname.replace("{PAGE}", String.valueOf(page)) : displayname;
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ConfigSetting.toChat(displayname, "", ""));
        item.setItemMeta(itemMeta);
        return item;
    }

    public void openCatchableList(Player player, int page) {
        YamlConfiguration entityFile = ConfigSetting.entityFile;
        Set<String> entityList = entityFile.getConfigurationSection("EntityList").getKeys(false);
        
        Inventory catchableInventory = Bukkit.createInventory(player, 54, ConfigSetting.toChat(TranslationFileReader.catchableListTitle, "", ""));
        
        for (String entity : entityList) {
            ItemStack skull = new HeadDrop().skullTextures(new ItemStack(Material.PLAYER_HEAD), entityFile, entity);
            ItemMeta skullMeta = skull.getItemMeta();
            String catchable = ConfigSetting.catchableEntity.contains(EntityType.valueOf(entity)) ? TranslationFileReader.TRUE : TranslationFileReader.FALSE;

            skullMeta.setDisplayName(ChatColor.WHITE + entity);
            
            List<String> lore = new ArrayList<>();
            for (int i=0; i < TranslationFileReader.guiSkullLore.size(); i++) {
                lore.add(i, ChatColor.translateAlternateColorCodes('&', TranslationFileReader.guiSkullLore.get(i).
                replace("{ENTITY}", ChatColor.AQUA + entityFile.getString("EntityList." + entity + ".DisplayName")).
                replace("{CATCHABLE}", catchable)));
            }
            skullMeta.setLore(lore);
            
            skull.setItemMeta(skullMeta);
            head.add(skull);
        }
        
        ItemStack[] skullList = head.toArray(new ItemStack[0]);
        

        if ((page - 1) * 45 < 0) { 
            page = (int) Math.ceil(((float) skullList.length / 45.0)); 
        } else if ((page - 1) * 45 > skullList.length ){
            page = 1;
        }
        
        int start = (page - 1) * 45;
        int finish = Math.min(page * 45, skullList.length);  

        catchableInventory.addItem(Arrays.copyOfRange(skullList, start, finish));
        
        catchableInventory.setItem(45, itemSet(prevPage, TranslationFileReader.prevPage, page));
        catchableInventory.setItem(49, itemSet(currentPage, TranslationFileReader.currentPage, page));
        catchableInventory.setItem(53, itemSet(nextPage, TranslationFileReader.nextPage, page));

        player.openInventory(catchableInventory);
    }
}

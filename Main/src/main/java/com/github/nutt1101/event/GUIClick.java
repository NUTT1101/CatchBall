package com.github.nutt1101.event;

import java.util.ArrayList;
import java.util.List;


import com.github.nutt1101.ConfigSetting;
import com.github.nutt1101.GUI.CatchableList;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.md_5.bungee.api.ChatColor;


public class GUIClick implements Listener{
    List<String> savelist = new ArrayList<>();

    @EventHandler
    public void guiClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String title = ConfigSetting.toChat(ConfigSetting.catchableListTitle, "", "");
        
        if (event.getView().getTitle().equals(title)) {
            event.setCancelled(true);

            if (event.getClickedInventory() == null) { return; }

            if (event.getClickedInventory().equals(player.getInventory())) { return; }
            Integer page = Integer.valueOf(ChatColor.stripColor(event.getClickedInventory().getItem(49).getItemMeta().getDisplayName()
            .replace(" ", "").split(":")[1]));
             
            
            switch (event.getSlot()) {
                case 45:
                    new CatchableList().openCatchableList(player, page - 1);
                    player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
                    break;
                case 53:
                    new CatchableList().openCatchableList(player, page + 1);
                    player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
                    event.setCancelled(true);
                    break;
                default:
                    if (event.getClickedInventory().getItem(event.getSlot()) != null && event.getClickedInventory().getItem(event.getSlot()).getType().equals(Material.PLAYER_HEAD)) {
                        ItemStack clickItem = event.getClickedInventory().getItem(event.getSlot());
                        ItemMeta clickItemMeta = clickItem.getItemMeta();
                        List<String> lore = clickItemMeta.getLore();

                        EntityType entityType = EntityType.valueOf(ChatColor.stripColor(clickItem.getItemMeta().getDisplayName()));

                        int loreIndex = getLoreIndex(lore, "{CATCHABLE}");
                        if (ConfigSetting.catchableEntity.contains(entityType)) {
                            ConfigSetting.catchableEntity.remove(entityType);      
                            lore.set(loreIndex, ChatColor.translateAlternateColorCodes('&', ConfigSetting.
                                toChat(ConfigSetting.guiSkullLore.get(loreIndex), "", "").replace("{CATCHABLE}", "&cFALSE")));
                            
                        } else {
                            ConfigSetting.catchableEntity.add(entityType);
                            lore.set(loreIndex, ChatColor.translateAlternateColorCodes('&', ConfigSetting.
                                toChat(ConfigSetting.guiSkullLore.get(loreIndex), "", "").replace("{CATCHABLE}", "&aTRUE")));
                        }

                        clickItemMeta.setLore(lore);
                        clickItem.setItemMeta(clickItemMeta);
                        player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 1.0f, 1.0f);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigSetting.allowCatchMessage).
                            replace("{ENTITY}", ChatColor.stripColor(clickItem.getItemMeta().getLore().get(0).split(" ")[2])).
                            replace("{STATUS}", clickItem.getItemMeta().getLore().get(1).split(" ")[2]));
                        
                        ConfigSetting.saveEntityList();
                        
                        break;
                    }              
            }
        }
    }

    public int getLoreIndex(List<String> lore, String contain) {
        for (int i=0; i<lore.size(); i++) {
            if (lore.get(i).contains(contain)) {
                return i;
            }
        }
        return 1;
    }
     
}

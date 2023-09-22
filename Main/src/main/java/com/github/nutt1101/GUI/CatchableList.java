package com.github.nutt1101.GUI;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.md_5.bungee.api.ChatColor;
import com.github.nutt1101.ConfigSetting;
import com.github.nutt1101.HeadDrop;
import com.github.nutt1101.utils.TranslationFileReader;

public class CatchableList {
    private List<ItemStack> head = new ArrayList<>();  // Consider making it private to maintain encapsulation
    private ItemStack prevPage = new ItemStack(Material.PAPER);
    private ItemStack nextPage = new ItemStack(Material.PAPER);
    private ItemStack currentPage = new ItemStack(Material.OAK_SIGN);

    private ItemStack itemSet(ItemStack item, String displayName, int page) { // Make methods private if they're not used outside of this class
        displayName = displayName.replace("{PAGE}", String.valueOf(page));
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ConfigSetting.toChat(displayName, "", ""));
        item.setItemMeta(itemMeta);
        return item;
    }

    public void openCatchableList(Player player, int page) {
        YamlConfiguration entityFile = ConfigSetting.entityFile;
        Set<String> entityList = entityFile.getConfigurationSection("EntityList").getKeys(false);
        Inventory catchableInventory = Bukkit.createInventory(player, 54, ConfigSetting.toChat(TranslationFileReader.catchableListTitle, "", ""));
        head.clear();  // Clear existing items in the head list

        for (String entity : entityList) {
            ItemStack skull = new HeadDrop().skullTextures(new ItemStack(Material.PLAYER_HEAD), entityFile, entity);
            ItemMeta skullMeta = skull.getItemMeta();
            String catchable = ConfigSetting.catchableEntity.contains(EntityType.valueOf(entity)) ? "&aTRUE" : "&cFALSE";
            skullMeta.setDisplayName(ChatColor.WHITE + entity);

            List<String> lore = new ArrayList<>();
            for (String line : TranslationFileReader.guiSkullLore) {
                lore.add(ChatColor.translateAlternateColorCodes('&', line.replace("{ENTITY}", ChatColor.AQUA + entityFile.getString("EntityList." + entity + ".DisplayName")).replace("{CATCHABLE}", catchable)));
            }
            skullMeta.setLore(lore);
            skull.setItemMeta(skullMeta);
            head.add(skull);
        }

        int totalHeads = head.size();
        page = Math.min(page, (int) Math.ceil((float) totalHeads / 45.0));
        page = Math.max(page, 1);

        int start = (page - 1) * 45;
        int finish = Math.min(page * 45, totalHeads);

        catchableInventory.addItem(head.subList(start, finish).toArray(new ItemStack[0]));

        catchableInventory.setItem(45, itemSet(prevPage, TranslationFileReader.prevPage, page));
        catchableInventory.setItem(49, itemSet(currentPage, TranslationFileReader.currentPage, page));
        catchableInventory.setItem(53, itemSet(nextPage, TranslationFileReader.nextPage, page));

        player.openInventory(catchableInventory);
    }
}
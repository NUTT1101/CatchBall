package com.pohuang.event;

import com.pohuang.CatchBall;
import com.pohuang.ConfigSetting;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import net.minecraft.nbt.MojangsonParser;
import net.minecraft.nbt.NBTTagCompound;


public class SkullClick implements Listener{
    private Plugin plugin = CatchBall.getPlugin(CatchBall.class);
    
    @EventHandler
    public void skullClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = (event.getItem() != null) ? event.getItem() : new ItemStack(Material.AIR);
        

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (!item.getType().equals(Material.PLAYER_HEAD)) { return; }

            ItemMeta itemMeta = item.getItemMeta();
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            
            if (data.has(new NamespacedKey(plugin, "skullData"), PersistentDataType.STRING)) {
                String path = data.get(new NamespacedKey(plugin, "skullData"), PersistentDataType.STRING).toString();

                if (path == null) {
                    player.sendMessage(ConfigSetting.toChat(ConfigSetting.skullDoesNotFound, "", ""));
                    event.setCancelled(true);
                } else {
                    if (!new HitEvent().resCheck(player, event.getClickedBlock().getLocation())) {
                        event.setCancelled(true);
                        return;
                    }

                    try {
                        EntityType entityType = EntityType.valueOf(data.get(new NamespacedKey(plugin, "entityType"), PersistentDataType.STRING));
                        Location clickLocation = event.getClickedBlock().getLocation();
                        
                        for (int i=0; i < 3; i++) {
                            if (clickLocation.getBlock().getType().equals(Material.AIR)) { break; }
                            clickLocation.setY(clickLocation.getY() + 1D);
                            
                            if (i == 2) {
                                player.sendMessage(ConfigSetting.toChat(ConfigSetting.locationUnsafe, "", ""));
                                event.setCancelled(true);
                                return;
                            }
                        }

                        Entity entity = player.getWorld().spawnEntity(clickLocation, entityType);
                        
                        net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle();
                        NBTTagCompound nbt = MojangsonParser.parse(data.get(new NamespacedKey(plugin, "entity"), PersistentDataType.STRING));
                        nmsEntity.load(nbt);      
                        
                        entity.teleport(clickLocation);
                        
                        event.getItem().setAmount(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                }
                
            }
        }
    }
    
}

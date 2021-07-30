package com.pohuang.event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.pohuang.items.Ball;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

public class LaunchEvent implements Listener{
    private ItemStack catchBall = new Ball().getCatchBall();

    // this will be change by othoer class so I public this attribute
    public static List<UUID> ballUUID = new ArrayList<>();
    

    @EventHandler
    public void onCatchBallThrow(ProjectileLaunchEvent event) {
        
        // catchBall is a SnowBall
        if (event.getEntity() instanceof Snowball) {
            
            // check if shooter is a player
            if (!(event.getEntity().getShooter() instanceof Player)) { return; }
            
            Player player = (Player) event.getEntity().getShooter();
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            
            ItemStack hold =  !mainHand.getType().equals(Material.SNOWBALL) ?
                player.getInventory().getItemInOffHand() : mainHand;

            // check if tool of player is catchBall
            if (hold.getItemMeta().equals(catchBall.getItemMeta())) {

                // every thing be has a Unique id , so get that to do condition
                ballUUID.add(event.getEntity().getUniqueId());
            }
        }
    }
}

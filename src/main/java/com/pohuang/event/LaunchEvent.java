package com.pohuang.event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.pohuang.items.Ball;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.BlockProjectileSource;

public class LaunchEvent implements Listener{
    private ItemStack catchBall = new Ball().getCatchBall();

    // this will be change by othoer class so I public this attribute
    public static List<UUID> ballUUID = new ArrayList<>();
    

    @EventHandler
    public void onCatchBallThrow(ProjectileLaunchEvent event) {
        
        if (!(event.getEntity() instanceof Snowball)) { return; }

        if (event.getEntity().getShooter() instanceof Player) { 
            if (!(event.getEntity() instanceof ThrowableProjectile)) { return; }

            ThrowableProjectile throwableProjectile = (ThrowableProjectile) event.getEntity();
            ItemStack ball = throwableProjectile.getItem();

            if (!ball.getItemMeta().equals(catchBall.getItemMeta())) { return; }
            
            ballUUID.add(event.getEntity().getUniqueId());
        
        } else if (event.getEntity().getShooter() instanceof BlockProjectileSource) {
            if (!(event.getEntity() instanceof ThrowableProjectile)) { return; }
            
            ThrowableProjectile throwableProjectile = (ThrowableProjectile) event.getEntity();
            
            if (!throwableProjectile.getItem().getItemMeta().equals(catchBall.getItemMeta())) { return; }
            
            ballUUID.add(event.getEntity().getUniqueId());
        }
    }

}

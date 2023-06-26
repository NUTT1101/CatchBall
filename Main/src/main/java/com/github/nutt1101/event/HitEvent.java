package com.github.nutt1101.event;


import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.github.nutt1101.*;
import com.github.nutt1101.items.Ball;
import com.github.nutt1101.utils.TranslationFileReader;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.BlockProjectileSource;

import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;


public class HitEvent implements Listener {
    private List<EntityType> catchableEntity = ConfigSetting.catchableEntity;
    private Location hitLocation;
    private final Plugin plugin = CatchBall.plugin;
    private final String[] mmPackage = {"io.lumine.mythic.bukkit.BukkitAPIHelper", "io.lumine.xikage.mythicmobs.api.bukkit.BukkitAPIHelper"};
    
    /* private final EntityType[] blockEntity = {EntityType.ARROW, EntityType.AREA_EFFECT_CLOUD, EntityType.MINECART_COMMAND, 
        EntityType.EGG, EntityType.DRAGON_FIREBALL, EntityType.ENDER_PEARL, EntityType.THROWN_EXP_BOTTLE , EntityType.EXPERIENCE_ORB,
        EntityType.ENDER_SIGNAL, EntityType.FALLING_BLOCK, EntityType.FIREBALL, EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME, 
        EntityType.DROPPED_ITEM, EntityType.THROWN_EXP_BOTTLE, EntityType.SHULKER_BULLET , EntityType.SMALL_FIREBALL , EntityType.SNOWBALL,
        EntityType.PRIMED_TNT, EntityType.TRIDENT, EntityType.PLAYER};   */

    @EventHandler
    public void CatchBallHitEvent(ProjectileHitEvent event){
        
        // check if shooter is a player
        if (event.getEntity().getShooter() instanceof Player) { 
            Player player = (Player) event.getEntity().getShooter();

            if (!checkCatchBall(event.getEntity())) { return; }

            if (!player.hasPermission("catchball.use")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigSetting.toChat(TranslationFileReader.noPermissionToUse,
                    getCoordinate(event.getHitBlock() == null ? event.getHitEntity().getLocation() : event.getHitBlock().getLocation())
                    , "").replace("{BALL}", ConfigSetting.catchBallName)));
                
                event.getEntity().remove();

                if (event.getHitEntity() != null) {
                    event.getHitEntity().getWorld().dropItem(event.getHitEntity().getLocation(), Ball.makeBall());
                } else { event.getHitBlock().getWorld().dropItem(event.getHitBlock().getLocation(), Ball.makeBall()); }
                
                event.setCancelled(true);

                return;
            }

            event.setCancelled(true);

            event.getEntity().remove();
            // hit a entity
            if (event.getHitEntity() != null) {
                
                if (!resCheck(player, event.getHitEntity().getLocation())) { 
                    event.getHitEntity().getWorld().dropItem(event.getHitEntity().getLocation(), Ball.makeBall());
                    player.sendMessage(ConfigSetting.toChat(TranslationFileReader.canNotCatchable, getCoordinate(event.getHitEntity().getLocation()), ""));
                    return;
                }

                if (!mmCheck(player, event.getHitEntity())) {
                    event.getHitEntity().getWorld().dropItem(event.getHitEntity().getLocation(), Ball.makeBall());
                    player.sendMessage(ConfigSetting.toChat(TranslationFileReader.canNotCatchable, getCoordinate(event.getHitEntity().getLocation()), ""));
                    return;
                }

                if (!gfCheck(player, event.getHitEntity().getLocation())) {
                    event.getHitEntity().getWorld().dropItem(event.getHitEntity().getLocation(), Ball.makeBall());
                    player.sendMessage(ConfigSetting.toChat(TranslationFileReader.canNotCatchable, getCoordinate(event.getHitEntity().getLocation()), ""));
                    return;
                }
                
                if (event.getHitEntity() instanceof Tameable tameable) {
                    Player shooter = (Player) event.getEntity().getShooter();
                    if (tameable.isTamed()) {
                        boolean isNullOwnerValue = tameable.getOwner() == null;
                        boolean sameOwner = isNullOwnerValue ? true : tameable.getOwner().getName().equals(shooter.getName());
                        if ((isNullOwnerValue && !ConfigSetting.allowCatchableTamedOwnerIsNull) || !sameOwner) {
                            event.getHitEntity().getWorld().dropItem(event.getHitEntity().getLocation(), Ball.makeBall());
                            player.sendMessage(ConfigSetting.toChat(TranslationFileReader.canNotCatchable, getCoordinate(event.getHitEntity().getLocation()), ""));
                            return;
                        }
                    }
                    
                }
                
                Entity hitEntity = event.getHitEntity();
                hitLocation = hitEntity.getLocation();
                
                String checkCustom = getIsCustomEntity(hitEntity);
                
                
                // check if the hitEntity is a catchable entity. on config.yml CatchableEntity
                for (EntityType entity : catchableEntity) {
                    if (hitEntity.getType().equals(entity) && !(hitEntity instanceof Player) && !checkCustom.equals("CUSTOM")) {
                        // hitEntity.getWorld().dropItem(hitEntity.getLocation(), entityToItemStack(entity));
                        if (!(ConfigSetting.catchSuccessSound.equals("FALSE"))) { 
                            player.playSound(player.getLocation(), Sound.valueOf(ConfigSetting.catchSuccessSound), 1f, 1f);
                        }

                        event.getHitEntity().remove();

                        hitEntity.getWorld().dropItem(hitLocation, new HeadDrop().getEntityHead(event.getHitEntity(), player));
                        player.sendMessage(ConfigSetting.toChat(TranslationFileReader.catchSuccess, getCoordinate(hitLocation), entity.toString()));
                        return;
                    }             
                }
                
                // if player hit a can not be catch entity, catchBall will be return
                player.sendMessage(ConfigSetting.toChat(TranslationFileReader.canNotCatchable, getCoordinate(hitLocation), ""));
                hitEntity.getWorld().dropItem(hitLocation, Ball.makeBall());   

            // hit block, catchBall will be return
            } else if (event.getHitBlock() != null) {   
                
                event.getEntity().remove();

                hitLocation = event.getHitBlock().getLocation();
                player.sendMessage(ConfigSetting.toChat(TranslationFileReader.ballHitBlock, getCoordinate(hitLocation), ""));
                
                event.getHitBlock().getWorld().dropItem(event.getHitBlock().getLocation(), Ball.makeBall());
                return;
            }

        } else if (event.getEntity().getShooter() instanceof BlockProjectileSource){

            if (!checkCatchBall(event.getEntity())) { return; }

            event.setCancelled(true);
            event.getEntity().remove();
            // hit a entity
            if (event.getHitEntity() != null) {
                Entity hitEntity = (Entity) event.getHitEntity();
                hitLocation = hitEntity.getLocation();

                String checkCustom = getIsCustomEntity(hitEntity);
                
                // check if the hitEntity is a catchable entity. on config.yml CatchableEntity
                for (EntityType entity : catchableEntity) {
                    if (hitEntity.getType().equals(entity) && !(hitEntity instanceof Player) && !checkCustom.equals("CUSTOM")) {
                        hitEntity.remove();
                        hitEntity.getWorld().dropItem(hitLocation, new HeadDrop().getEntityHead(event.getHitEntity(), null));
                        
                        return;
                    }             
                }
                
                hitEntity.getWorld().dropItem(hitLocation, Ball.makeBall());   

            } else if (event.getHitBlock() != null) {
                hitLocation = event.getHitBlock().getLocation();
                event.getHitBlock().getWorld().dropItem(event.getHitBlock().getLocation(), Ball.makeBall());
                return;
            }
        }

        return;
    }


    // config text will be use this method , so put on this class
    public static String getCoordinate(Location location) {

        return location.getBlockX() + ", " +
                location.getBlockY() + ", " +
                location.getBlockZ();
    }

    public boolean resCheck(Player player, Location location) {
        if (plugin.getServer().getPluginManager().getPlugin("Residence") == null) { return true; }

        if (ResidenceApi.getResidenceManager().getByLoc(location) == null) { return true; }

        ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(location);

        if (residence.getOwnerUUID().equals(player.getUniqueId()) || player.isOp() || player.hasPermission("catchball.op")) { return true; }
            
        for (String flags : ConfigSetting.residenceFlag) {        
            if (!residence.getPermissions().playerHas(player, Flags.valueOf(flags.toLowerCase()) , true)) {

                player.sendMessage(ConfigSetting.toChat(TranslationFileReader.noResidencePermissions, "", "").
                    replace("{FLAG}", flags));

                return false;
            }
        }

        return true;
    }

    public boolean mmCheck(Player player, Entity entity) {
        if (plugin.getServer().getPluginManager().getPlugin("MythicMobs") == null) { return true; }
        
        for (int i=0 ; i < 2; i++) {
            try {
                Class<?> api = Class.forName(mmPackage[i]);
                Object ins = api.getConstructor().newInstance();

                Method isMythicMob = api.getDeclaredMethod("isMythicMob", Entity.class);
                
                return !((boolean) isMythicMob.invoke(ins, entity));
            } catch (Exception e) {
            }
        }

        return true;
    }

    public boolean gfCheck(Player player, Location location) {
        if (plugin.getServer().getPluginManager().getPlugin("GriefPrevention") == null) { return true; }

        if (GriefPrevention.instance.dataStore.getClaimAt(location, false, null) == null) { return true; }

        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false, null);

        if (claim.getOwnerID().equals(player.getUniqueId()) || player.hasPermission("catchball.op") || player.isOp()) { return true; }

        for (String flags : ConfigSetting.griefPreventionFlag) {
            if (!claim.hasExplicitPermission(player, ClaimPermission.valueOf(flags))) {

                player.sendMessage(ConfigSetting.toChat(TranslationFileReader.noResidencePermissions, "", "").
                    replace("{FLAG}", flags));

                return false;
            }
        }
        
        return true;
    }

    public boolean checkCatchBall(Projectile projectile) {
        if (!(projectile instanceof Snowball)) { return false; }

        if (projectile instanceof ThrowableProjectile) {
            ThrowableProjectile throwableProjectile = (ThrowableProjectile) projectile;
            if (!throwableProjectile.getItem().getItemMeta().equals(Ball.makeBall().getItemMeta())) { return false; }
        }

        return true;
    }

    public String getIsCustomEntity(Entity hitEntity) {
        String checkCustom = null;

        switch (CatchBall.getRealServerVersion()) {
            case "v1_20" -> checkCustom = NBT_v1_20.isCustomEntity(hitEntity);
            case "v1_19" -> checkCustom = NBT_v1_19.isCustomEntity(hitEntity);
            case "v1_18" -> checkCustom = NBT_v1_18.isCustomEntity(hitEntity);
            case "v1_17" -> checkCustom = NBT_v1_17.isCustomEntity(hitEntity);
            case "v1_16" -> checkCustom = NBT_v1_16.isCustomEntity(hitEntity);
            default -> {
                plugin.getLogger().log(Level.INFO, "can not check entity if it not a entity.");
            }
        }

        return checkCustom;
    }

}

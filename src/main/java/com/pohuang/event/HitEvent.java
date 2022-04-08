package com.pohuang.event;

import java.util.List;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.pohuang.CatchBall;
import com.pohuang.ConfigSetting;
import com.pohuang.HeadDrop;
import com.pohuang.items.Ball;
import com.pohuang.nms.checkByCustomEntity.CheckByCustomEntity_1_16;
import com.pohuang.nms.checkByCustomEntity.CheckByCustomEntity_1_17;
import com.pohuang.nms.checkByCustomEntity.CheckByCustomEntity_1_18;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.ThrowableProjectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.BlockProjectileSource;

import io.lumine.xikage.mythicmobs.MythicMobs;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;



public class HitEvent implements Listener {
    private List<EntityType> catchableEntity = ConfigSetting.catchableEntity;
    private Location hitLocation;
    private final Plugin plugin = CatchBall.getPlugin(CatchBall.class);
    
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
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigSetting.toChat(ConfigSetting.noPermissionToUse, 
                    getCoordinate(event.getHitBlock() == null ? event.getHitEntity().getLocation() : event.getHitBlock().getLocation())
                    , "").replace("{BALL}", ConfigSetting.catchBallName)));
                
                event.getEntity().remove();

                if (event.getHitEntity() != null) {
                    event.getHitEntity().getWorld().dropItem(event.getHitEntity().getLocation(), new Ball().getCatchBall());
                } else { event.getHitBlock().getWorld().dropItem(event.getHitBlock().getLocation(), new Ball().getCatchBall()); }
                
                event.setCancelled(true);

                return;
            }

            event.setCancelled(true);

            event.getEntity().remove();
            // hit a entity
            if (event.getHitEntity() != null) {
                
                if (!resCheck(player, event.getHitEntity().getLocation())) { 
                    event.getHitEntity().getWorld().dropItem(event.getHitEntity().getLocation(), new Ball().getCatchBall());
                    player.sendMessage(ConfigSetting.toChat(ConfigSetting.canNotCatchable, getCoordinate(event.getHitEntity().getLocation()), ""));
                    return;
                }

                if (!mmCheck(player, event.getHitEntity())) {
                    event.getHitEntity().getWorld().dropItem(event.getHitEntity().getLocation(), new Ball().getCatchBall());
                    player.sendMessage(ConfigSetting.toChat(ConfigSetting.canNotCatchable, getCoordinate(event.getHitEntity().getLocation()), ""));
                    return;
                }

                if (!gfCheck(player, event.getHitEntity().getLocation())) {
                    event.getHitEntity().getWorld().dropItem(event.getHitEntity().getLocation(), new Ball().getCatchBall());
                    player.sendMessage(ConfigSetting.toChat(ConfigSetting.canNotCatchable, getCoordinate(event.getHitEntity().getLocation()), ""));
                    return;
                }
                
                if (event.getHitEntity() instanceof Tameable) {
                    Tameable tameable = (Tameable) event.getHitEntity();
                    Player shooter = (Player) event.getEntity().getShooter();
                    
                    if (tameable.isTamed()) {
                        if (!tameable.getOwner().getName().equals(shooter.getName())){
                            event.getHitEntity().getWorld().dropItem(event.getHitEntity().getLocation(), new Ball().getCatchBall());
                            player.sendMessage(ConfigSetting.toChat(ConfigSetting.canNotCatchable, getCoordinate(event.getHitEntity().getLocation()), ""));
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
                        
                        player.sendMessage(ConfigSetting.toChat(ConfigSetting.catchSuccess, getCoordinate(hitLocation), entity.toString()));
                        return;
                    }             
                }
                
                // if player hit a can not be catch entity, catchBall will be return
                player.sendMessage(ConfigSetting.toChat(ConfigSetting.canNotCatchable, getCoordinate(hitLocation), ""));
                hitEntity.getWorld().dropItem(hitLocation, new Ball().getCatchBall());   

            // hit block, catchBall will be return
            } else if (event.getHitBlock() != null) {   
                
                event.getEntity().remove();

                hitLocation = event.getHitBlock().getLocation();
                player.sendMessage(ConfigSetting.toChat(ConfigSetting.ballHitBlock, getCoordinate(hitLocation), ""));
                
                event.getHitBlock().getWorld().dropItem(event.getHitBlock().getLocation(), new Ball().getCatchBall());
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
                
                hitEntity.getWorld().dropItem(hitLocation, new Ball().getCatchBall());   

            } else if (event.getHitBlock() != null) {
                hitLocation = event.getHitBlock().getLocation();
                event.getHitBlock().getWorld().dropItem(event.getHitBlock().getLocation(), new Ball().getCatchBall());
                return;
            }
        }

        return;
    }


    // config text will be use this method , so put on this class
    public String getCoordinate(Location location) {
        String xyz = String.valueOf(location.getBlockX()) + ", " +
        String.valueOf(location.getBlockY()) + ", " +
        String.valueOf(location.getBlockZ());

        return xyz;
    }

    public boolean resCheck(Player player, Location location) {
        if (plugin.getServer().getPluginManager().getPlugin("Residence") == null) { return true; }

        if (ResidenceApi.getResidenceManager().getByLoc(location) == null) { return true; }

        ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(location);

        if (residence.getOwnerUUID().equals(player.getUniqueId()) || player.isOp() || player.hasPermission("catchball.op")) { return true; }
            
        for (String flags : ConfigSetting.residenceFlag) {        
            if (!residence.getPermissions().playerHas(player, Flags.valueOf(flags.toLowerCase()) , true)) {

                player.sendMessage(ConfigSetting.toChat(ConfigSetting.noResidencePermissions, "", "").
                    replace("{FLAG}", flags));

                return false;
            }
        }

        return true;
    }

    public boolean mmCheck(Player player, Entity entity) {
        if (plugin.getServer().getPluginManager().getPlugin("MythicMobs") == null) { return true; }
        
        if (MythicMobs.inst().getAPIHelper().isMythicMob(entity)) {
            return false;
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

                player.sendMessage(ConfigSetting.toChat(ConfigSetting.noResidencePermissions, "", "").
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
            if (!throwableProjectile.getItem().getItemMeta().equals(new Ball().getCatchBall().getItemMeta())) { return false; }
        }

        return true;
    }

    public String getIsCustomEntity(Entity hitEntity) {
        String checkCustom = null;

        switch (plugin.getServer().getClass().getPackage().getName().split("\\.")[3]) {
            case "v1_18_R2":
                checkCustom = new CheckByCustomEntity_1_18().checkIsCustomEntity(hitEntity);        
                break;
            case "v1_17_R1":
                checkCustom = new CheckByCustomEntity_1_17().checkIsCustomEntity(hitEntity);        
                break;
            case "v1_16_R3":
                checkCustom = new CheckByCustomEntity_1_16().checkIsCustomEntity(hitEntity);
                break;
            default:
                break;
        }

        return checkCustom;
    }

}

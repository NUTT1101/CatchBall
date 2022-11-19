package com.github.nutt1101.Recipe;

import java.util.Set;
import java.util.stream.Collectors;


import com.github.nutt1101.CatchBall;
import com.github.nutt1101.ConfigSetting;
import com.github.nutt1101.items.Ball;
import com.github.nutt1101.items.GoldEgg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;


public class BallRecipe {
    private final Plugin plugin = CatchBall.plugin;

    // load from config.yml and add catchBall Recipe
    public BallRecipe() {
        if (ConfigSetting.recipeEnabled) {
            NamespacedKey ballKey = new NamespacedKey(plugin, "ballKey");
            ShapedRecipe ballRecipe = new ShapedRecipe(ballKey, new Ball().getCatchBall());
            
            FileConfiguration config = plugin.getConfig(); 
            
            // load from yml file
            
            Set<String> recipePath = config.getConfigurationSection("Recipe.key").getKeys(false);            
            
            ballRecipe.shape(config.getStringList("Recipe.shape").toArray(new String[3]));

            for (String key : recipePath) {
                String ItemName = config.getString("Recipe.key." + key).toUpperCase();
                if (ItemName instanceof String) {
                    if (ItemName.equals("GOLDEGG")) {
                        ballRecipe.setIngredient(key.charAt(0), new RecipeChoice.ExactChoice(new GoldEgg().getGoldEgg()));
                    } else {
                        ballRecipe.setIngredient(key.charAt(0), Material.valueOf(ItemName));
                    }

                } else {
                    ballRecipe.setIngredient(key.charAt(0), new RecipeChoice.MaterialChoice(
                        config.getStringList("Recipe.key." + key).stream().map(
                        list -> Material.valueOf(list)).collect(Collectors.toList())));
                }
                
            }
            
            if (Bukkit.getRecipe(ballKey) != null) { Bukkit.removeRecipe(ballKey); }
            
            Bukkit.addRecipe(ballRecipe);
        }
    }
}

package com.github.teraprath.lm_oraxenbridge.config;

import com.github.teraprath.lm_oraxenbridge.LM_OraxenBridge;
import io.th0rgal.oraxen.api.OraxenItems;
import me.lokka30.levelledmobs.customdrops.CustomDropInstance;
import me.lokka30.levelledmobs.customdrops.CustomDropItem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.HashMap;

public class CustomDrops {

    protected final LM_OraxenBridge plugin;
    private final String fileName;
    private File file;
    private FileConfiguration config;

    private HashMap<EntityType, CustomDropInstance> drops;

    public CustomDrops(@Nonnull LM_OraxenBridge plugin, @Nonnull String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
        this.drops = new HashMap<>();
    }

    public void reload() {

        // Load File

        this.file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            plugin.saveResource(this.fileName, false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);

        // Load entities

        plugin.getLogger().info("Loading entities...");
        for (String entity : config.getKeys(false)) {

            EntityType type = EntityType.valueOf(entity.toUpperCase());
            final CustomDropInstance dropInstance = new CustomDropInstance(type);

            for (String id : config.getConfigurationSection(entity).getKeys(false)) {

                int amount = config.getInt(entity + "." + id + ".amount");
                float chance = (float) config.getDouble(entity + "." + id + ".chance");
                float equipped = (float) config.getDouble(entity + "." + id + ".equipped");
                boolean offHand = config.getBoolean(entity + "." + id + ".off_hand");
                int minLevel = config.getInt(entity + "." + id + ".min_level");
                int maxLevel = config.getInt(entity + "." + id + ".max_level");

                ItemStack item = OraxenItems.getItemById(id).build();
                CustomDropItem dropItem = new CustomDropItem(plugin.levelledMobs);

                // Default Values
                dropItem.setItemStack(item);
                dropItem.chance = 1.0F;
                dropItem.equippedSpawnChance = 0.0F;
                dropItem.equipOffhand = false;

                // Update Values
                if (amount > 0 && amount < 64) { dropItem.setAmount(amount); }
                if (chance > 0 && chance < 1) { dropItem.chance = chance; }
                if (equipped > 0 && equipped < 1) { dropItem.equippedSpawnChance = equipped; }
                if (offHand) { dropItem.equipOffhand = true; }
                if (minLevel > 0) { dropItem.minLevel = minLevel; }
                if (maxLevel > 0) { dropItem.maxLevel = maxLevel; }

                dropInstance.customItems.add(dropItem);
            }

            this.drops.put(type, dropInstance);
            plugin.getLogger().info(entity + " loaded.");
        }
        plugin.getLogger().info(this.drops.size() + " entities loaded.");
        if (!this.drops.isEmpty()) { register(); }
    }

    private void register() {
        this.drops.forEach((entity, customDropInstance) -> {
            plugin.levelledMobs.customDropsHandler.externalCustomDrops.addCustomDrop(customDropInstance);
        });
        plugin.getLogger().info("Custom drops registered.");
    }

    public HashMap<EntityType, CustomDropInstance> getDrops() {
        return this.drops;
    }
}

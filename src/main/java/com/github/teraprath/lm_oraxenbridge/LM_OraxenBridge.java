package com.github.teraprath.lm_oraxenbridge;

import com.github.teraprath.lm_oraxenbridge.command.AdminCommand;
import com.github.teraprath.lm_oraxenbridge.config.CustomDrops;
import me.lokka30.levelledmobs.LevelledMobs;
import org.bukkit.plugin.java.JavaPlugin;

public final class LM_OraxenBridge extends JavaPlugin {

    public final CustomDrops customDrops = new CustomDrops(this, "customdrops.yml");
    public LevelledMobs levelledMobs;

    @Override
    public void onEnable() {

        this.levelledMobs = LevelledMobs.getInstance();
        getCommand("lm_oraxenbridge").setExecutor(new AdminCommand(this));

        reload();
    }
    
    public void reload() {
        this.customDrops.reload();
    }

}

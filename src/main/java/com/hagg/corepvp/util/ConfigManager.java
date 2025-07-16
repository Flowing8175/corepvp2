package com.hagg.corepvp.util;

import com.hagg.corepvp.CorePVP;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private final CorePVP plugin;
    private FileConfiguration config;

    public ConfigManager(CorePVP plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        plugin.saveDefaultConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public int getBorderSize() {
        return config.getInt("border-size", 2000);
    }

    public int getGracePeriodMinutes() {
        return config.getInt("grace-period-minutes", 15);
    }

    public int getRevealPlayerCount() {
        return config.getInt("reveal-player-count", 5);
    }

    public int getRevealDelayMinutes() {
        return config.getInt("reveal-delay-minutes", 3);
    }

    public int getCoreHealth() {
        return config.getInt("core-health", 2000);
    }
}

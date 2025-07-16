package com.hagg.corepvp.manager;

import com.hagg.corepvp.CorePVP;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatsManager {

    private final CorePVP plugin;
    private final Map<UUID, Integer> kills = new HashMap<>();
    private final Map<UUID, Integer> deaths = new HashMap<>();
    private final Map<UUID, Integer> coreDamage = new HashMap<>();

    public StatsManager(CorePVP plugin) {
        this.plugin = plugin;
    }

    public void addKill(Player player) {
        kills.put(player.getUniqueId(), kills.getOrDefault(player.getUniqueId(), 0) + 1);
    }

    public void addDeath(Player player) {
        deaths.put(player.getUniqueId(), deaths.getOrDefault(player.getUniqueId(), 0) + 1);
    }

    public void addCoreDamage(Player player, int damage) {
        coreDamage.put(player.getUniqueId(), coreDamage.getOrDefault(player.getUniqueId(), 0) + damage);
    }

    public void clearStats() {
        kills.clear();
        deaths.clear();
        coreDamage.clear();
    }

    public Map<UUID, Integer> getTopKills() {
        return kills;
    }

    public Map<UUID, Integer> getTopDeaths() {
        return deaths;
    }

    public Map<UUID, Integer> getTopCoreDamage() {
        return coreDamage;
    }
}

package com.hagg.corepvp.manager;

import com.hagg.corepvp.CorePVP;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class CoreManager {

    private final CorePVP plugin;
    private final Map<TeamManager.Team, Integer> coreHealth = new HashMap<>();
    private final Map<TeamManager.Team, Location> coreLocations = new HashMap<>();

    public CoreManager(CorePVP plugin) {
        this.plugin = plugin;
    }

    public void setCoreHealth(TeamManager.Team team, int health) {
        coreHealth.put(team, health);
    }

    public int getCoreHealth(TeamManager.Team team) {
        return coreHealth.getOrDefault(team, 0);
    }

    public void setCoreLocation(TeamManager.Team team, Location location) {
        coreLocations.put(team, location);
    }

    public Location getCoreLocation(TeamManager.Team team) {
        return coreLocations.get(team);
    }

    public void spawnCores() {
        int borderSize = plugin.getConfigManager().getBorderSize();
        int coreHealth = plugin.getConfigManager().getCoreHealth();

        // Blue Core
        double blueX = borderSize * 0.8;
        double blueZ = borderSize * 0.8;
        Location blueCoreLocation = getSafeCoreLocation(new Location(plugin.getServer().getWorlds().get(0), blueX, 0, blueZ));
        setCoreLocation(TeamManager.Team.BLUE, blueCoreLocation);
        setCoreHealth(TeamManager.Team.BLUE, coreHealth);
        blueCoreLocation.getBlock().setType(org.bukkit.Material.BLUE_TERRACOTTA);
        spawnCoreSlime(blueCoreLocation, TeamManager.Team.BLUE);


        // Red Core
        double redX = -borderSize * 0.8;
        double redZ = -borderSize * 0.8;
        Location redCoreLocation = getSafeCoreLocation(new Location(plugin.getServer().getWorlds().get(0), redX, 0, redZ));
        setCoreLocation(TeamManager.Team.RED, redCoreLocation);
        setCoreHealth(TeamManager.Team.RED, coreHealth);
        redCoreLocation.getBlock().setType(org.bukkit.Material.RED_TERRACOTTA);
        spawnCoreSlime(redCoreLocation, TeamManager.Team.RED);
    }

    private Location getSafeCoreLocation(Location location) {
        Location check = location.getWorld().getHighestBlockAt(location).getLocation();
        while (!check.getBlock().getType().isSolid() || check.getBlock().getType() == org.bukkit.Material.WATER) {
            check.subtract(0, 1, 0);
        }
        return check.add(0, 2, 0);
    }

    private void spawnCoreSlime(Location location, TeamManager.Team team) {
        org.bukkit.entity.Slime slime = location.getWorld().spawn(location, org.bukkit.entity.Slime.class);
        slime.setSize(3);
        slime.setAI(false);
        slime.setInvulnerable(true);
        slime.setSilent(true);
        slime.setRemoveWhenFarAway(false);
        slime.setCustomName(team.name() + "_CORE_SLIME");
    }

    private final Map<TeamManager.Team, Long> lastDamageTime = new HashMap<>();

    public void damageCore(TeamManager.Team team, double damage) {
        int currentHealth = getCoreHealth(team);
        setCoreHealth(team, (int) (currentHealth - damage));
        lastDamageTime.put(team, System.currentTimeMillis());
        plugin.getScoreboardManager().updateScoreboard();
    }

    public boolean wasCoreRecentlyDamaged(TeamManager.Team team) {
        return System.currentTimeMillis() - lastDamageTime.getOrDefault(team, 0L) < 5000;
    }

    public void healCore(TeamManager.Team team, int amount) {
        int currentHealth = getCoreHealth(team);
        int maxHealth = plugin.getConfigManager().getCoreHealth();
        setCoreHealth(team, Math.min(currentHealth + amount, maxHealth));
        plugin.getScoreboardManager().updateScoreboard();
    }

    public void despawnCores() {
        coreLocations.values().forEach(location -> location.getBlock().setType(org.bukkit.Material.AIR));
        plugin.getServer().getWorlds().get(0).getEntitiesByClass(org.bukkit.entity.Slime.class).forEach(slime -> {
            if (slime.getCustomName() != null && slime.getCustomName().contains("_CORE_SLIME")) {
                slime.remove();
            }
        });
        coreLocations.clear();
        coreHealth.clear();
        lastDamageTime.clear();
    }
}

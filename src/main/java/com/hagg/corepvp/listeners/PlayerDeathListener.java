package com.hagg.corepvp.listeners;

import com.hagg.corepvp.CorePVP;
import com.hagg.corepvp.manager.TeamManager;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDeathListener implements Listener {

    private final CorePVP plugin;

    public PlayerDeathListener(CorePVP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        plugin.getStatsManager().addDeath(player);
        if (player.getKiller() != null) {
            plugin.getStatsManager().addKill(player.getKiller());
        }
        event.setDeathMessage(null);
        player.setGameMode(GameMode.SPECTATOR);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.spigot().respawn();
            }
        }.runTaskLater(plugin, 1);

        if (plugin.getCoreManager().getCoreHealth(plugin.getTeamManager().getPlayerTeam(player)) <= 0) {
            player.sendTitle("§c죽었습니다!", "§a코어§e가 파괴되어 더이상 부활할 수 없습니다!", 10, 70, 20);
            player.playSound(player.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_4, 1, 1);
        } else {
            startRespawnTimer(player);
        }
    }

    private void startRespawnTimer(Player player) {
        new BukkitRunnable() {
            int respawnTime = plugin.getCoreManager().wasCoreRecentlyDamaged(plugin.getTeamManager().getPlayerTeam(player)) ? 12 : 10;

            @Override
            public void run() {
                if (respawnTime <= 0) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.teleport(plugin.getCoreManager().getCoreLocation(plugin.getTeamManager().getPlayerTeam(player)));
                    cancel();
                    return;
                }

                player.sendTitle(" ", "부활까지 " + respawnTime + "초 남았습니다...", 0, 20, 0);
                if (respawnTime <= 3 && respawnTime > 0) {
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);
                }

                respawnTime--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}

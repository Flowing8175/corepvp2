package com.hagg.corepvp.listeners;

import com.hagg.corepvp.CorePVP;
import com.hagg.corepvp.manager.TeamManager;
import org.bukkit.GameMode;
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

        if (plugin.getCoreManager().getCoreHealth(plugin.getTeamManager().getPlayerTeam(player)) <= 0) {
            player.sendTitle("§c죽었습니다!", "§a코어§e가 파괴되어 더이상 부활할 수 없습니다!", 10, 70, 20);
            player.playSound(player.getLocation(), "item.goat_horn.sound.4", 1, 1);
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
                    // Teleport to team spawn
                    cancel();
                    return;
                }

                player.sendTitle(" ", "§a부활까지 " + respawnTime + "초 남았습니다...", 0, 20, 0);
                if (respawnTime <= 3) {
                    player.playSound(player.getLocation(), "entity.experience_orb.pickup", 1, 2);
                }

                respawnTime--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }
}

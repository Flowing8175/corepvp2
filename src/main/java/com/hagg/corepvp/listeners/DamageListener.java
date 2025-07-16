package com.hagg.corepvp.listeners;

import com.hagg.corepvp.CorePVP;
import com.hagg.corepvp.manager.GameManager;
import com.hagg.corepvp.manager.TeamManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageListener implements Listener {

    private final CorePVP plugin;

    public DamageListener(CorePVP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (plugin.getGameManager().getGameState() == GameManager.GameState.GRACE_PERIOD) {
            event.setCancelled(true);
            return;
        }

        Entity damaged = event.getEntity();
        if (damaged.getCustomName() != null && damaged.getCustomName().contains("_CORE_SLIME")) {
            if (event.getDamager() instanceof Player) {
                Player damager = (Player) event.getDamager();
                TeamManager.Team damagerTeam = plugin.getTeamManager().getPlayerTeam(damager);
                TeamManager.Team coreTeam = TeamManager.Team.valueOf(damaged.getCustomName().replace("_CORE_SLIME", ""));

                if (damagerTeam == coreTeam) {
                    event.setCancelled(true);
                } else {
                    double damage = event.getDamage();
                    if (plugin.getTeamManager().isTeamWiped(coreTeam)) {
                        damage *= 0.75;
                        String teamColor = coreTeam == TeamManager.Team.BLUE ? "§9" : "§c";
                        damager.sendActionBar("§l" + teamColor + coreTeam.name() + "팀§f이 전멸하여 데미지의 25%가 상쇄되었습니다!");
                        damager.playSound(damager.getLocation(), org.bukkit.Sound.ENCHANT_THORNS_HIT, 1, 1);
                    }
                    plugin.getCoreManager().damageCore(coreTeam, damage);
                    plugin.getScoreboardManager().flashCoreHealth(coreTeam);
                    plugin.getStatsManager().addCoreDamage(damager, (int) damage);
                    event.setCancelled(true);
                }
            }
        }
    }
}

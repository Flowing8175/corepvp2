package com.hagg.corepvp.listeners;

import com.hagg.corepvp.CorePVP;
import com.hagg.corepvp.manager.TeamManager;
import com.hagg.corepvp.util.ItemFactory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class InteractionListener implements Listener {

    private final CorePVP plugin;

    public InteractionListener(CorePVP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if (item.isSimilar(ItemFactory.createStarOfRecovery())) {
                TeamManager.Team team = plugin.getTeamManager().getPlayerTeam(player);
                if (team != null) {
                    plugin.getCoreManager().healCore(team, 100);
                    item.setAmount(item.getAmount() - 1);
                    player.sendMessage("Your team's core has been healed by 100 HP.");
                }
                event.setCancelled(true);
            } else if (item.isSimilar(ItemFactory.createBeaconOfLife())) {
                TeamManager.Team team = plugin.getTeamManager().getPlayerTeam(player);
                if (team != null) {
                    plugin.getCoreManager().healCore(team, 450);
                    item.setAmount(item.getAmount() - 1);
                    player.sendMessage("Your team's core has been healed by 450 HP.");
                }
                event.setCancelled(true);
            }
        }

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && item.getType() == Material.BEACON && item.getItemMeta().getDisplayName().equals("§l생명의 신호기")) {
            event.setCancelled(true);
        }
    }
}

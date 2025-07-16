package com.hagg.corepvp.manager;

import com.hagg.corepvp.CorePVP;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TeamManager {

    private final CorePVP plugin;
    private final Map<UUID, Team> playerTeams = new HashMap<>();

    public TeamManager(CorePVP plugin) {
        this.plugin = plugin;
    }

    public void setPlayerTeam(Player player, Team team) {
        playerTeams.put(player.getUniqueId(), team);
    }

    public Team getPlayerTeam(Player player) {
        return playerTeams.get(player.getUniqueId());
    }

    public void clearTeams() {
        playerTeams.clear();
    }

    public boolean isTeamWiped(Team team) {
        for (Map.Entry<UUID, Team> entry : playerTeams.entrySet()) {
            if (entry.getValue() == team) {
                Player player = plugin.getServer().getPlayer(entry.getKey());
                if (player != null && player.getGameMode() != org.bukkit.GameMode.SPECTATOR) {
                    return false;
                }
            }
        }
        return true;
    }

    public Map<UUID, Team> getPlayerTeams() {
        return playerTeams;
    }

    public void randomizeTeams() {
        clearTeams();
        java.util.List<Player> players = new java.util.ArrayList<>(plugin.getServer().getOnlinePlayers());
        java.util.Collections.shuffle(players);
        int teamSize = players.size() / 2;
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            if (player.getGameMode() == org.bukkit.GameMode.SPECTATOR) continue;
            if (i < teamSize) {
                setPlayerTeam(player, Team.BLUE);
                player.setDisplayName("§9[B] " + player.getName());
                player.setPlayerListName("§9[B] " + player.getName());
            } else {
                setPlayerTeam(player, Team.RED);
                player.setDisplayName("§c[R] " + player.getName());
                player.setPlayerListName("§c[R] " + player.getName());
            }
        }
    }

    public enum Team {
        RED,
        BLUE
    }
}

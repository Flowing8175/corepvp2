package com.hagg.corepvp.manager;

import com.hagg.corepvp.CorePVP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardManager {

    private final CorePVP plugin;

    public ScoreboardManager(CorePVP plugin) {
        this.plugin = plugin;
    }

    public void setScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("corepvp", "dummy", "§b§lCorePVP 시참 서버");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore("§7by. 해그").setScore(15);
        objective.getScore("§m----------").setScore(14);
        objective.getScore("§9블루팀").setScore(13);
        objective.getScore(getCoreHealthScore(TeamManager.Team.BLUE, false)).setScore(12);
        objective.getScore("§c레드팀").setScore(11);
        objective.getScore(getCoreHealthScore(TeamManager.Team.RED, false)).setScore(10);
        objective.getScore("인원: §9" + getTeamCount(TeamManager.Team.BLUE) + " §f: §c" + getTeamCount(TeamManager.Team.RED)).setScore(9);
        objective.getScore("§m-----------").setScore(8);

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            setScoreboard(player);
        }
    }

    private int getTeamCount(TeamManager.Team team) {
        return (int) plugin.getTeamManager().getPlayerTeams().values().stream().filter(t -> t == team).count();
    }

    private String getCoreHealthScore(TeamManager.Team team, boolean flashing) {
        String teamColor = team == TeamManager.Team.BLUE ? "§9" : "§c";
        String healthColor = flashing ? "§e" : "§f";
        return teamColor + "코어 체력: " + healthColor + plugin.getCoreManager().getCoreHealth(team);
    }

    public void flashCoreHealth(TeamManager.Team team) {
        new org.bukkit.scheduler.BukkitRunnable() {
            boolean yellow = true;
            int count = 0;
            @Override
            public void run() {
                if (count >= 2) {
                    cancel();
                    updateScoreboard(); // Reset to normal color
                    return;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Scoreboard scoreboard = player.getScoreboard();
                    Objective objective = scoreboard.getObjective("corepvp");
                    if (objective != null) {
                        scoreboard.resetScores(getCoreHealthScore(team, !yellow));
                        objective.getScore(getCoreHealthScore(team, yellow)).setScore(team == TeamManager.Team.BLUE ? 12 : 10);
                    }
                }
                yellow = !yellow;
                count++;
            }
        }.runTaskTimer(plugin, 0, 5); // 0.25 seconds
    }
}

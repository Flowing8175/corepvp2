package com.hagg.corepvp.manager;

import com.hagg.corepvp.CorePVP;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.FireworkEffect;

import java.util.Map;
import java.util.UUID;

public class GameManager {

    private final CorePVP plugin;
    private GameState gameState;
    private int gracePeriodTime;
    private BukkitRunnable gracePeriodTask;

    public GameManager(CorePVP plugin) {
        this.plugin = plugin;
        this.gameState = GameState.WAITING;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void startGracePeriod() {
        setGameState(GameState.GRACE_PERIOD);
        gracePeriodTime = plugin.getConfigManager().getGracePeriodMinutes() * 60;

        gracePeriodTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (gracePeriodTime <= 0) {
                    endGracePeriod();
                    return;
                }

                if (gracePeriodTime % 60 == 0 && gracePeriodTime / 60 > 0) {
                    int minutes = gracePeriodTime / 60;
                    if (minutes == 15 || minutes == 10 || minutes == 5 || minutes == 1) {
                        plugin.getServer().broadcastMessage("§b§l무적 시간 §e§l종료까지 §b§l" + minutes + "분 §e§l남았습니다!");
                        plugin.getServer().getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), "ui.button.click", 1, 2));
                    }
                } else if (gracePeriodTime == 30 || gracePeriodTime == 10 || (gracePeriodTime <= 3 && gracePeriodTime > 0)) {
                    plugin.getServer().broadcastMessage("§b§l무적 시간 §e§l종료까지 §b§l" + gracePeriodTime + "초 §e§l남았습니다!");
                    plugin.getServer().getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), "ui.button.click", 1, 2));
                }

                plugin.getServer().getOnlinePlayers().forEach(p -> p.sendActionBar("§b무적 시간 : §e" + (gracePeriodTime / 60) + "분 " + (gracePeriodTime % 60) + "초"));

                gracePeriodTime--;
            }
        };

        gracePeriodTask.runTaskTimer(plugin, 0, 20);
    }

    public void endGracePeriod() {
        if (gracePeriodTask != null) {
            gracePeriodTask.cancel();
        }
        setGameState(GameState.ACTIVE);
        plugin.getServer().getOnlinePlayers().forEach(p -> {
            p.sendTitle("§c§l무적 시간이 종료되었습니다!", "§eFIGHT!", 10, 70, 20);
            p.playSound(p.getLocation(), "entity.ender_dragon.growl", 0.7f, 1);
        });
    }

    public void addGraceTime(int minutes) {
        if (getGameState() == GameState.ACTIVE || getGameState() == GameState.GRACE_PERIOD) {
            gracePeriodTime += minutes * 60;
            if (getGameState() != GameState.GRACE_PERIOD) {
                startGracePeriod();
            }
        }
    }

    public void subtractGraceTime(int minutes) {
        if (getGameState() == GameState.GRACE_PERIOD) {
            gracePeriodTime -= minutes * 60;
            if (gracePeriodTime <= 0) {
                gracePeriodTime = 0;
                endGracePeriod();
            }
        }
    }

    public void startGame() {
        new BukkitRunnable() {
            int countdown = 5;

            @Override
            public void run() {
                if (countdown <= 0) {
                    plugin.getServer().broadcastMessage("§e게임이 시작되었습니다!");
                    plugin.getCoreManager().spawnCores();
                    plugin.getTeamManager().getPlayerTeams().forEach((uuid, team) -> {
                        Player player = plugin.getServer().getPlayer(uuid);
                        if (player != null) {
                            Location coreLocation = plugin.getCoreManager().getCoreLocation(team);
                            if (coreLocation != null) {
                                // a random spot within a 10-block radius of their team's Core
                                Location teleportLocation = coreLocation.clone().add(Math.random() * 20 - 10, 0, Math.random() * 20 - 10);
                                player.teleport(teleportLocation);
                                player.setBedSpawnLocation(teleportLocation, true);
                            }
                        }
                    });
                    startGracePeriod();
                    startRevealTask();
                    cancel();
                    return;
                }

                plugin.getServer().broadcastMessage("§e게임 시작까지 §c" + countdown + "§e초...");
                plugin.getServer().getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), "ui.button.click", 1, 1));
                countdown--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    public void startRevealTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getGameState() != GameState.ACTIVE) {
                    cancel();
                    return;
                }

                if (plugin.getServer().getOnlinePlayers().stream().filter(p -> p.getGameMode() == org.bukkit.GameMode.SURVIVAL).count() <= plugin.getConfigManager().getRevealPlayerCount()) {
                    plugin.getServer().broadcastMessage("§f§l--------생존자 위치--------");
                    plugin.getTeamManager().getPlayerTeams().entrySet().stream()
                            .filter(entry -> entry.getValue() == TeamManager.Team.BLUE)
                            .map(entry -> plugin.getServer().getPlayer(entry.getKey()))
                            .filter(java.util.Objects::nonNull)
                            .forEach(player -> {
                                org.bukkit.Location loc = player.getLocation();
                                plugin.getServer().broadcastMessage("§9" + player.getName() + "§f님의 위치: §l" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
                            });
                    plugin.getTeamManager().getPlayerTeams().entrySet().stream()
                            .filter(entry -> entry.getValue() == TeamManager.Team.RED)
                            .map(entry -> plugin.getServer().getPlayer(entry.getKey()))
                            .filter(java.util.Objects::nonNull)
                            .forEach(player -> {
                                org.bukkit.Location loc = player.getLocation();
                                plugin.getServer().broadcastMessage("§c" + player.getName() + "§f님의 위치: §l" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
                            });
                    plugin.getServer().broadcastMessage("§f§l----------------");
                }
            }
        }.runTaskTimer(plugin, 0, plugin.getConfigManager().getRevealDelayMinutes() * 60 * 20);
    }

    public enum GameState {
        WAITING,
        GRACE_PERIOD,
        ACTIVE,
        FINISHED
    }

    public void stopGame(TeamManager.Team winningTeam) {
        setGameState(GameState.FINISHED);

        if (winningTeam != null) {
            plugin.getTeamManager().getPlayerTeams().entrySet().stream()
                    .filter(entry -> entry.getValue() == winningTeam)
                    .map(entry -> plugin.getServer().getPlayer(entry.getKey()))
                    .filter(java.util.Objects::nonNull)
                    .forEach(player -> {
                        for (int i = 0; i < 3; i++) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    org.bukkit.entity.Firework firework = player.getWorld().spawn(player.getLocation(), org.bukkit.entity.Firework.class);
                                    org.bukkit.inventory.meta.FireworkMeta meta = firework.getFireworkMeta();
                                    meta.addEffect(FireworkEffect.builder()
                                            .with(FireworkEffect.Type.BALL_LARGE)
                                            .withColor(winningTeam == TeamManager.Team.BLUE ? org.bukkit.Color.BLUE : org.bukkit.Color.RED)
                                            .trail(true)
                                            .build());
                                    meta.setPower(0);
                                    firework.setFireworkMeta(meta);
                                }
                            }.runTaskLater(plugin, i * 8);
                        }
                    });
        }

        // Post-game summary
        plugin.getServer().broadcastMessage("§l--- Top 3 킬 ---");
        plugin.getStatsManager().getTopKills().entrySet().stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> {
                    plugin.getServer().broadcastMessage(plugin.getServer().getOfflinePlayer(entry.getKey()).getName() + ": " + entry.getValue());
                });

        plugin.getServer().broadcastMessage("§l--- 최다/최소 데스 ---");
        plugin.getStatsManager().getTopDeaths().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(entry -> plugin.getServer().broadcastMessage("최다 데스: " + plugin.getServer().getOfflinePlayer(entry.getKey()).getName() + " (" + entry.getValue() + ")"));
        plugin.getStatsManager().getTopDeaths().entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .ifPresent(entry -> plugin.getServer().broadcastMessage("최소 데스: " + plugin.getServer().getOfflinePlayer(entry.getKey()).getName() + " (" + entry.getValue() + ")"));

        plugin.getServer().broadcastMessage("§l--- Top 코어 데미지 ---");
        plugin.getStatsManager().getTopCoreDamage().entrySet().stream()
                .sorted(Map.Entry.<UUID, Integer>comparingByValue().reversed())
                .limit(1)
                .forEach(entry -> {
                    plugin.getServer().broadcastMessage(plugin.getServer().getOfflinePlayer(entry.getKey()).getName() + ": " + entry.getValue());
                });


        // Teleport all players to spawn
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getServer().getOnlinePlayers().forEach(player -> {
                    player.teleport(plugin.getServer().getWorlds().get(0).getSpawnLocation());
                });
                // Clean up
                plugin.getCoreManager().despawnCores();
                plugin.getTeamManager().clearTeams();
                plugin.getStatsManager().clearStats();
            }
        }.runTaskLater(plugin, 100); // 5 seconds
    }
}

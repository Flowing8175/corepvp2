package com.hagg.corepvp;

import com.hagg.corepvp.commands.CommandManager;
import com.hagg.corepvp.listeners.DamageListener;
import com.hagg.corepvp.listeners.InteractionListener;
import com.hagg.corepvp.listeners.PlayerConnectionListener;
import com.hagg.corepvp.listeners.PlayerDeathListener;
import com.hagg.corepvp.manager.CoreManager;
import com.hagg.corepvp.manager.GameManager;
import com.hagg.corepvp.manager.ScoreboardManager;
import com.hagg.corepvp.manager.StatsManager;
import com.hagg.corepvp.manager.TeamManager;
import com.hagg.corepvp.util.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CorePVP extends JavaPlugin {

    private ConfigManager configManager;
    private GameManager gameManager;
    private TeamManager teamManager;
    private CoreManager coreManager;
    private ScoreboardManager scoreboardManager;
    private StatsManager statsManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        gameManager = new GameManager(this);
        teamManager = new TeamManager(this);
        coreManager = new CoreManager(this);
        scoreboardManager = new ScoreboardManager(this);
        statsManager = new StatsManager(this);

        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        getServer().getPluginManager().registerEvents(new InteractionListener(this), this);

        getCommand("cp").setExecutor(new CommandManager(this));

        // Recipes
        org.bukkit.inventory.ShapedRecipe starRecipe = new org.bukkit.inventory.ShapedRecipe(new org.bukkit.NamespacedKey(this, "star_of_recovery"), com.hagg.corepvp.util.ItemFactory.createStarOfRecovery());
        starRecipe.shape("III", "INI", "III");
        starRecipe.setIngredient('I', org.bukkit.Material.IRON_INGOT);
        starRecipe.setIngredient('N', org.bukkit.Material.NETHER_STAR);
        getServer().addRecipe(starRecipe);

        org.bukkit.inventory.ShapedRecipe beaconRecipe = new org.bukkit.inventory.ShapedRecipe(new org.bukkit.NamespacedKey(this, "beacon_of_life"), com.hagg.corepvp.util.ItemFactory.createBeaconOfLife());
        beaconRecipe.shape("O", "O", "O");
        beaconRecipe.setIngredient('O', org.bukkit.Material.OBSIDIAN);
        getServer().addRecipe(beaconRecipe);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public CoreManager getCoreManager() {
        return coreManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }
}

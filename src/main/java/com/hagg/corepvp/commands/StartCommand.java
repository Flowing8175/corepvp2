package com.hagg.corepvp.commands;

import com.hagg.corepvp.CorePVP;
import com.hagg.corepvp.manager.GameManager;
import org.bukkit.command.CommandSender;

public class StartCommand implements SubCommand {

    private final CorePVP plugin;

    public StartCommand(CorePVP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("corepvp.admin")) {
            if (plugin.getGameManager().getGameState() == GameManager.GameState.WAITING || plugin.getGameManager().getGameState() == GameManager.GameState.FINISHED) {
                plugin.getGameManager().startGame();
            } else {
                sender.sendMessage("The game is already running.");
            }
        } else {
            sender.sendMessage("You don't have permission to do that.");
        }
    }
}

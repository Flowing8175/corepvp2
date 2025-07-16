package com.hagg.corepvp.commands;

import com.hagg.corepvp.CorePVP;
import org.bukkit.command.CommandSender;

public class StopCommand implements SubCommand {

    private final CorePVP plugin;

    public StopCommand(CorePVP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("corepvp.admin")) {
            plugin.getGameManager().stopGame(null);
            sender.sendMessage("Game stopped.");
        } else {
            sender.sendMessage("You don't have permission to do that.");
        }
    }
}

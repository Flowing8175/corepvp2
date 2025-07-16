package com.hagg.corepvp.commands;

import com.hagg.corepvp.CorePVP;
import org.bukkit.command.CommandSender;

public class RollCommand implements SubCommand {

    private final CorePVP plugin;

    public RollCommand(CorePVP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("corepvp.admin")) {
            plugin.getTeamManager().randomizeTeams();
            sender.sendMessage("Teams have been randomized.");
        } else {
            sender.sendMessage("You don't have permission to do that.");
        }
    }
}

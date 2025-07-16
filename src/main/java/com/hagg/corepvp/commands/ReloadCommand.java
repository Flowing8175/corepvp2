package com.hagg.corepvp.commands;

import com.hagg.corepvp.CorePVP;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements SubCommand {

    private final CorePVP plugin;

    public ReloadCommand(CorePVP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("corepvp.admin")) {
            plugin.getConfigManager().reloadConfig();
            sender.sendMessage("CorePVP config reloaded.");
        } else {
            sender.sendMessage("You don't have permission to do that.");
        }
    }
}

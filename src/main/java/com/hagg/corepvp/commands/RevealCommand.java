package com.hagg.corepvp.commands;

import com.hagg.corepvp.CorePVP;
import org.bukkit.command.CommandSender;

public class RevealCommand implements SubCommand {

    private final CorePVP plugin;

    public RevealCommand(CorePVP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("corepvp.admin")) {
            sender.sendMessage("You don't have permission to do that.");
            return;
        }

        if (args.length < 3) {
            sender.sendMessage("Usage: /cp reveal <delay|playercount> <value>");
            return;
        }

        switch (args[1].toLowerCase()) {
            case "delay":
                int delay = Integer.parseInt(args[2]);
                plugin.getConfig().set("reveal-delay-minutes", delay);
                plugin.saveConfig();
                plugin.getConfigManager().reloadConfig();
                sender.sendMessage("Reveal delay set to " + delay + " minutes.");
                break;
            case "playercount":
                int count = Integer.parseInt(args[2]);
                plugin.getConfig().set("reveal-player-count", count);
                plugin.saveConfig();
                plugin.getConfigManager().reloadConfig();
                sender.sendMessage("Reveal player count set to " + count + ".");
                break;
            default:
                sender.sendMessage("Usage: /cp reveal <delay|playercount> <value>");
                break;
        }
    }
}

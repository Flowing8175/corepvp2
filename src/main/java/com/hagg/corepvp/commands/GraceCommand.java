package com.hagg.corepvp.commands;

import com.hagg.corepvp.CorePVP;
import org.bukkit.command.CommandSender;

public class GraceCommand implements SubCommand {

    private final CorePVP plugin;

    public GraceCommand(CorePVP plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("corepvp.admin")) {
            sender.sendMessage("You don't have permission to do that.");
            return;
        }

        if (args.length < 2) {
            sender.sendMessage("Usage: /cp grace <add|subtract|end> [minutes]");
            return;
        }

        switch (args[1].toLowerCase()) {
            case "add":
                if (args.length == 3) {
                    int minutes = Integer.parseInt(args[2]);
                    plugin.getGameManager().addGraceTime(minutes);
                    sender.sendMessage("Added " + minutes + " minutes to grace period.");
                }
                break;
            case "subtract":
                if (args.length == 3) {
                    int minutes = Integer.parseInt(args[2]);
                    plugin.getGameManager().subtractGraceTime(minutes);
                    sender.sendMessage("Subtracted " + minutes + " minutes from grace period.");
                }
                break;
            case "end":
                plugin.getGameManager().endGracePeriod();
                break;
            default:
                sender.sendMessage("Usage: /cp grace <add|subtract|end> [minutes]");
                break;
        }
    }
}

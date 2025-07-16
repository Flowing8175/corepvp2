package com.hagg.corepvp.commands;

import com.hagg.corepvp.CorePVP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class CommandManager implements CommandExecutor {

    private final CorePVP plugin;
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public CommandManager(CorePVP plugin) {
        this.plugin = plugin;
        // Register sub-commands
        subCommands.put("start", new StartCommand(plugin));
        subCommands.put("stop", new StopCommand(plugin));
        subCommands.put("grace", new GraceCommand(plugin));
        subCommands.put("roll", new RollCommand(plugin));
        subCommands.put("reveal", new RevealCommand(plugin));
        subCommands.put("reload", new ReloadCommand(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            // Send help message
            return true;
        }

        String subCommandName = args[0].toLowerCase();
        SubCommand subCommand = subCommands.get(subCommandName);

        if (subCommand == null) {
            // Send unknown command message
            return true;
        }

        subCommand.execute(sender, args);
        return true;
    }
}

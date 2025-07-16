package com.hagg.corepvp.listeners;

import com.hagg.corepvp.CorePVP;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.TabCompleteEvent;

public class TabCompleteListener implements Listener {

    private final CorePVP plugin;

    public TabCompleteListener(CorePVP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        String buffer = event.getBuffer();
        if (buffer.startsWith("/")) {
            String[] args = buffer.substring(1).split(" ");
            if (args.length > 1) {
                // This is a simple implementation. A more robust solution would be to
                // check the command and argument index to determine if a player name is expected.
                event.getCompletions().removeIf(s -> s.startsWith("§"));
            }
        }
    }
}

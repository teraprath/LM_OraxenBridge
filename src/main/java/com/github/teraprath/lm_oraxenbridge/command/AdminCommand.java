package com.github.teraprath.lm_oraxenbridge.command;

import com.github.teraprath.lm_oraxenbridge.LM_OraxenBridge;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class AdminCommand implements CommandExecutor, TabCompleter {

    private final LM_OraxenBridge plugin;

    public AdminCommand(LM_OraxenBridge plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (args[0].equals("reload")) {
                sender.sendMessage("[" + plugin.getName() + "] Reloading config file...");
                plugin.reload();
                sender.sendMessage("[" + plugin.getName() + "] Reload complete.");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        ArrayList<String> list = new ArrayList<>();
        String current = args[args.length - 1].toLowerCase();

        if (args.length == 1) {
            list.add("reload");
        }

        list.removeIf(s -> !s.toLowerCase().startsWith(current));
        return list;
    }

}

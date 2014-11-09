package com.comze_instancelabs.bedwars;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.commands.CommandHandler;
import com.comze_instancelabs.minigamesapi.util.Util;

public class ICommandHandler extends CommandHandler {

	@Override
	public boolean setSpawn(PluginInstance pli, CommandSender sender, String[] args, String uber_permission, String cmd, String action, JavaPlugin plugin, Player p) {
		if (!sender.hasPermission(uber_permission + ".setup")) {
			sender.sendMessage(pli.getMessagesConfig().no_perm);
			return true;
		}
		if (args.length > 2) {
			String team = args[2];
			if (!team.equalsIgnoreCase("red") && !team.equalsIgnoreCase("green") && !team.equalsIgnoreCase("yellow") && !team.equalsIgnoreCase("blue")) {
				sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <team>");
				sender.sendMessage(ChatColor.RED + "Possible teams: Green, Blue, Red, Yellow");
				return true;
			}
			pli.arenaSetup.autoSetSpawn(plugin, args[1], p.getLocation());
			Util.saveComponentForArena(plugin, args[1], "spawns.spawn" + team.toLowerCase(), p.getLocation());
			sender.sendMessage(pli.getMessagesConfig().successfully_set.replaceAll("<component>", "spawn for team " + team));
		} else {
			sender.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "-" + ChatColor.DARK_GRAY + "]" + ChatColor.GRAY + " Usage: " + cmd + " " + action + " <arena> <team>");
		}
		return true;
	}

}

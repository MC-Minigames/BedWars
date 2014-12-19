package com.comze_instancelabs.bedwars.gui;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comze_instancelabs.bedwars.IArena;
import com.comze_instancelabs.bedwars.Main;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.IconMenu;
import com.comze_instancelabs.minigamesapi.util.Util;

public class TeamSelectorGUI {

	Main plugin;
	PluginInstance pli;
	public HashMap<String, IconMenu> lasticonm = new HashMap<String, IconMenu>();

	public TeamSelectorGUI(PluginInstance pli, Main plugin) {
		this.plugin = plugin;
		this.pli = pli;
	}

	public void openGUI(final String playername) {
		IconMenu iconm;
		if (lasticonm.containsKey(playername)) {
			iconm = lasticonm.get(playername);
		} else {
			iconm = new IconMenu("Team", 9, new IconMenu.OptionClickEventHandler() {
				@Override
				public void onOptionClick(IconMenu.OptionClickEvent event) {
					if (event.getPlayer().getName().equalsIgnoreCase(playername)) {
						if (pli.global_players.containsKey(playername)) {
							if (pli.getArenas().contains(pli.global_players.get(playername))) {
								String d = event.getName();
								Player p = event.getPlayer();
								IArena a = (IArena) pli.global_players.get(playername);
								if (Util.isComponentForArenaValid(plugin, a.getInternalName(), "spawns.spawn" + ChatColor.stripColor(d.toLowerCase()))) {
									if (plugin.pteam.containsKey(p.getName())) {
										updateTeamCount(playername, a, -1);
									}
									plugin.pteam.put(p.getName(), ChatColor.stripColor(d.toLowerCase()));
									updateTeamCount(playername, a, +1);
									p.sendMessage(ChatColor.GREEN + "Successfully set team: " + d);
									plugin.scoreboard.updateScoreboard(a);
								} else {
									p.sendMessage(ChatColor.RED + "That team is not enabled on this map: " + d);
								}
							}
						}
					}
					event.setWillClose(true);
				}
			}, plugin);

		}

		iconm.setOption(1, new ItemStack(Material.WOOL, 1, (byte) 14), ChatColor.RED + "RED", "Select the red team.");
		iconm.setOption(3, new ItemStack(Material.WOOL, 1, (byte) 11), ChatColor.BLUE + "BLUE", "Select the blue team.");
		iconm.setOption(5, new ItemStack(Material.WOOL, 1, (byte) 5), ChatColor.GREEN + "GREEN", "Select the green team.");
		iconm.setOption(7, new ItemStack(Material.WOOL, 1, (byte) 4), ChatColor.YELLOW + "YELLOW", "Select the yellow team.");

		iconm.open(Bukkit.getPlayerExact(playername));
		lasticonm.put(playername, iconm);
	}

	public void updateTeamCount(String playername, IArena a, int c) {
		String team = plugin.pteam.get(playername);
		if (team.equalsIgnoreCase("red")) {
			a.red += c;
		} else if (team.equalsIgnoreCase("blue")) {
			a.blue += c;
		} else if (team.equalsIgnoreCase("green")) {
			a.green += c;
		} else if (team.equalsIgnoreCase("yellow")) {
			a.yellow += c;
		}
	}

}

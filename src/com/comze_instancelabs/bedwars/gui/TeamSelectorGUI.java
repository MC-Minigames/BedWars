package com.comze_instancelabs.bedwars.gui;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comze_instancelabs.bedwars.Main;
import com.comze_instancelabs.bedwars.villager.Merchant;
import com.comze_instancelabs.bedwars.villager.MerchantOffer;
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

	public void openGUI(final String p) {
		IconMenu iconm;
		if (lasticonm.containsKey(p)) {
			iconm = lasticonm.get(p);
		} else {
			iconm = new IconMenu("Team", 9, new IconMenu.OptionClickEventHandler() {
				@Override
				public void onOptionClick(IconMenu.OptionClickEvent event) {
					if (event.getPlayer().getName().equalsIgnoreCase(p)) {
						if (pli.global_players.containsKey(p)) {
							if (pli.getArenas().contains(pli.global_players.get(p))) {
								String d = event.getName();
								Player p = event.getPlayer();
								plugin.pteam.put(p.getName(), ChatColor.stripColor(d.toLowerCase()));
								p.sendMessage(ChatColor.GREEN + "Successfully set team: " + d);
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

		iconm.open(Bukkit.getPlayerExact(p));
		lasticonm.put(p, iconm);
	}

}

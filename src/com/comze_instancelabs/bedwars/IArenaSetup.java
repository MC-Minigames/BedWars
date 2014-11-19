package com.comze_instancelabs.bedwars;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectOutputStream;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaSetup;
import com.comze_instancelabs.minigamesapi.ArenaType;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.ArenaBlock;
import com.comze_instancelabs.minigamesapi.util.Cuboid;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class IArenaSetup extends ArenaSetup {

	@Override
	public Arena saveArena(JavaPlugin plugin, String arenaname) {
		if (!Validator.isArenaValid(plugin, arenaname)) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Arena " + arenaname + " appears to be invalid.");
			return null;
		}
		PluginInstance pli = MinigamesAPI.getAPI().pinstances.get(plugin);
		if (pli.getArenaByName(arenaname) != null) {
			pli.removeArenaByName(arenaname);
		}
		IArena a = Main.initArena(arenaname);
		if (a.getArenaType() == ArenaType.REGENERATION) {
			if (Util.isComponentForArenaValid(plugin, arenaname, "bounds")) {
				saveArenaToFile(plugin, arenaname);
			} else {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not save arena to file because boundaries were not set up.");
			}
		}
		this.setArenaVIP(plugin, arenaname, false);
		pli.addArena(a);
		return a;
	}

	public void saveArenaToFile(JavaPlugin plugin, String arena) {
		File f = new File(plugin.getDataFolder() + "/" + arena);
		Cuboid c = new Cuboid(Util.getComponentForArena(plugin, arena, "bounds.low"), Util.getComponentForArena(plugin, arena, "bounds.high"));
		Location start = c.getLowLoc();
		Location end = c.getHighLoc();

		int width = end.getBlockX() - start.getBlockX();
		int length = end.getBlockZ() - start.getBlockZ();
		int height = end.getBlockY() - start.getBlockY();

		MinigamesAPI.getAPI().getLogger().info("BOUNDS: " + Integer.toString(width) + " " + Integer.toString(height) + " " + Integer.toString(length));
		MinigamesAPI.getAPI().getLogger().info("BLOCKS TO SAVE: " + Integer.toString(width * height * length));

		FileOutputStream fos;
		ObjectOutputStream oos = null;
		try {
			fos = new FileOutputStream(f);
			oos = new BukkitObjectOutputStream(fos);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Remove all saved iron, gold and clay blocks for later refresh // TODO Test out
		PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(plugin);
		pli.getArenasConfig().getConfig().set("arenas." + arena + ".iron", null);
		pli.getArenasConfig().getConfig().set("arenas." + arena + ".gold", null);
		pli.getArenasConfig().getConfig().set("arenas." + arena + ".clay", null);
		pli.getArenasConfig().saveConfig();

		int tempid = 0;

		for (int i = 0; i <= width; i++) {
			for (int j = 0; j <= height; j++) {
				for (int k = 0; k <= length; k++) {
					Block change = c.getWorld().getBlockAt(start.getBlockX() + i, start.getBlockY() + j, start.getBlockZ() + k);

					tempid++;
					if (change.getType() == Material.IRON_BLOCK) {
						Util.saveComponentForArena(plugin, arena, "iron.i" + tempid, change.getLocation());
					} else if (change.getType() == Material.HARD_CLAY) {
						Util.saveComponentForArena(plugin, arena, "clay.i" + tempid, change.getLocation());
					} else if (change.getType() == Material.GOLD_BLOCK) {
						Util.saveComponentForArena(plugin, arena, "gold.i" + tempid, change.getLocation());
					}

					ArenaBlock bl = change.getType() == Material.CHEST ? new ArenaBlock(change, true) : new ArenaBlock(change, false);

					try {
						oos.writeObject(bl);
					} catch (IOException e) {
						System.out.println(e.getMessage());
					}
				}
			}
		}

		try {
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		MinigamesAPI.getAPI().getLogger().info("saved");
	}

}

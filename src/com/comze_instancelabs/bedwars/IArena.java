package com.comze_instancelabs.bedwars;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaType;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.util.Util;

public class IArena extends Arena {

	public Main m;

	int blue = 0;
	int red = 0;
	int yellow = 0;
	int green = 0;

	boolean blue_bed = true;
	boolean red_bed = true;
	boolean yellow_bed = true;
	boolean green_bed = true;

	BukkitTask spawn_task = null;

	int c = 0;

	public ArrayList<Location> clay_locs = new ArrayList<Location>();
	public ArrayList<Location> gold_locs = new ArrayList<Location>();
	public ArrayList<Location> iron_locs = new ArrayList<Location>();

	public ArrayList<Item> dropped_items = new ArrayList<Item>();

	public IArena(Main m, String arena_id) {
		super(m, arena_id, ArenaType.REGENERATION);
		this.m = m;
	}

	@Override
	public void joinPlayerLobby(final String playername) {
		Bukkit.getScheduler().runTaskLater(m, new Runnable() {
			public void run() {
				Player p = Bukkit.getPlayer(playername);
				if (p != null) {
					if (m.pli.global_players.containsKey(p.getName())) {
						ItemStack teamselector = new ItemStack(Material.WOOL, 1, (byte) 14);
						ItemMeta itemm = teamselector.getItemMeta();
						itemm.setDisplayName(ChatColor.RED + "Team");
						teamselector.setItemMeta(itemm);
						p.getInventory().setItem(0, teamselector);
						p.updateInventory();
					}
				}
			}
		}, 30L);
		super.joinPlayerLobby(playername);
		selectTeam(playername);

	}

	int tries_temp = 0;

	public void selectTeam(String playername) {
		if (c == 0) {
			if (Util.isComponentForArenaValid(m, this.getName(), "spawns.spawnred")) {
				m.pteam.put(playername, "red");
				Bukkit.getPlayer(playername).sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "RED Team");
				red++;
			} else {
				c++;
				selectTeam(playername);
			}

		} else if (c == 1) {
			if (Util.isComponentForArenaValid(m, this.getName(), "spawns.spawngreen")) {
				m.pteam.put(playername, "green");
				Bukkit.getPlayer(playername).sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "GREEN Team");
				green++;
			} else {
				c++;
				selectTeam(playername);
			}
		} else if (c == 2) {
			if (Util.isComponentForArenaValid(m, this.getName(), "spawns.spawnblue")) {
				m.pteam.put(playername, "blue");
				Bukkit.getPlayer(playername).sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "BLUE Team");
				blue++;
			} else {
				c++;
				selectTeam(playername);
			}
		} else if (c == 3) {
			if (Util.isComponentForArenaValid(m, this.getName(), "spawns.spawnyellow")) {
				m.pteam.put(playername, "yellow");
				Bukkit.getPlayer(playername).sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "YELLOW Team");
				yellow++;
			} else {
				tries_temp++;
				if (tries_temp > 100) {
					return;
				}
				// TODO Infinite loop here, if no spawns set?
				selectTeam(playername);
			}
			c = 0;
		}
		if (c < 3) {
			c++;
		} else {
			c = 0;
		}
	}

	public void spectate(String playername, boolean super_) {
		super.spectate(playername);
	}

	@Override
	public void spectate(final String playername) {
		try {
			this.getClass().getMethod("onEliminated", String.class);
			this.onEliminated(playername);
		} catch (NoSuchMethodException e) {
			System.out.println("Please update your MinigamesLib version to work with this Bowbash version!");
		}
	}

	BukkitTask tt;
	int currentingamecount;

	@Override
	public void start(boolean tp) {
		int t = this.getAllPlayers().size() / 2;

		final IArena a = this;

		for (String p_ : a.getArena().getAllPlayers()) {
			Player p = Bukkit.getPlayer(p_);
			if (m.pteam.containsKey(p_)) {
				Util.teleportPlayerFixed(p, Util.getComponentForArena(m, this.getName(), "spawns.spawn" + m.pteam.get(p_)));
			}
		}

		super.start(false);

		m.scoreboard.updateScoreboard(this);

		// load clay, iron, gold locations
		PluginInstance pli = MinigamesAPI.getAPI().getPluginInstance(m);
		if (pli.getArenasConfig().getConfig().isSet("arenas." + this.getName() + ".clay")) {
			for (String key : pli.getArenasConfig().getConfig().getConfigurationSection("arenas." + this.getName() + ".clay").getKeys(false)) {
				clay_locs.add(Util.getComponentForArena(m, this.getName(), "clay." + key));
			}
		}
		if (pli.getArenasConfig().getConfig().isSet("arenas." + this.getName() + ".iron")) {
			for (String key : pli.getArenasConfig().getConfig().getConfigurationSection("arenas." + this.getName() + ".iron").getKeys(false)) {
				iron_locs.add(Util.getComponentForArena(m, this.getName(), "iron." + key));
			}
		}
		if (pli.getArenasConfig().getConfig().isSet("arenas." + this.getName() + ".gold")) {
			for (String key : pli.getArenasConfig().getConfig().getConfigurationSection("arenas." + this.getName() + ".gold").getKeys(false)) {
				gold_locs.add(Util.getComponentForArena(m, this.getName(), "gold." + key));
			}
		}
	}

	int current_spawn_index_iron = 0;
	int current_spawn_index_gold = 0;

	@Override
	public void started() {
		spawn_task = Bukkit.getScheduler().runTaskTimer(m, new Runnable() {
			public void run() {
				current_spawn_index_iron++;
				current_spawn_index_gold++;
				if (current_spawn_index_iron > 10) {
					for (Location l : iron_locs) {
						if (l != null) {
							dropped_items.add(l.getWorld().dropItemNaturally(l.clone().add(0D, 1D, 0D), new ItemStack(Material.IRON_INGOT)));
						}
					}
					current_spawn_index_iron = 0;
				}
				if (current_spawn_index_gold > 30) {
					for (Location l : gold_locs) {
						if (l != null) {
							dropped_items.add(l.getWorld().dropItemNaturally(l.clone().add(0D, 1D, 0D), new ItemStack(Material.GOLD_INGOT)));
						}
					}
					current_spawn_index_gold = 0;
				}
				for (Location l : clay_locs) {
					if (l != null) {
						dropped_items.add(l.getWorld().dropItemNaturally(l.clone().add(0D, 1D, 0D), new ItemStack(Material.CLAY_BRICK)));
					}
				}
			}
		}, 20L, 20L);

		for (String p_ : this.getAllPlayers()) {
			Util.clearInv(Bukkit.getPlayer(p_));
		}
	}

	@Override
	public void leavePlayer(String p_, boolean arg1, boolean arg2) {
		if (m.pteam.containsKey(p_)) {
			String team = m.pteam.get(p_);
			if (team.equalsIgnoreCase("red")) {
				red--;
			} else if (team.equalsIgnoreCase("blue")) {
				blue--;
			} else if (team.equalsIgnoreCase("green")) {
				green--;
			} else if (team.equalsIgnoreCase("yellow")) {
				yellow--;
			}
		}
		super.leavePlayer(p_, arg1, arg2);
	}

	@Override
	public void stop() {
		tries_temp = 0;
		current_spawn_index_iron = 0;
		current_spawn_index_gold = 0;
		c = 0;
		blue_bed = true;
		red_bed = true;
		green_bed = true;
		yellow_bed = true;
		blue = 0;
		red = 0;
		yellow = 0;
		green = 0;
		if (spawn_task != null) {
			spawn_task.cancel();
		}
		for (Item i : dropped_items) {
			if (i != null) {
				i.remove();
			}
		}
		super.stop();
	}

}

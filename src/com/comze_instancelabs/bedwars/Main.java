package com.comze_instancelabs.bedwars;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.ArenaSetup;
import com.comze_instancelabs.minigamesapi.ArenaState;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.PluginInstance;
import com.comze_instancelabs.minigamesapi.config.ArenasConfig;
import com.comze_instancelabs.minigamesapi.config.DefaultConfig;
import com.comze_instancelabs.minigamesapi.config.MessagesConfig;
import com.comze_instancelabs.minigamesapi.config.StatsConfig;
import com.comze_instancelabs.minigamesapi.util.Util;
import com.comze_instancelabs.minigamesapi.util.Validator;

public class Main extends JavaPlugin implements Listener {

	// TODO
	// teams: no team damage, team chat
	// villager
	// Bugs:
	// - sometimes beds don't reset properly
	// - when doing reload items don't get cleared

	MinigamesAPI api = null;
	PluginInstance pli = null;
	static Main m = null;
	IArenaScoreboard scoreboard = new IArenaScoreboard(this);
	ICommandHandler cmdhandler = new ICommandHandler();

	// Player -> Team
	public static HashMap<String, String> pteam = new HashMap<String, String>();

	public void onEnable() {
		m = this;
		api = MinigamesAPI.getAPI().setupAPI(this, "bedwars", IArena.class, new ArenasConfig(this), new MessagesConfig(this), new IClassesConfig(this), new StatsConfig(this, false), new DefaultConfig(this, false), true);
		PluginInstance pinstance = api.pinstances.get(this);
		pinstance.addLoadedArenas(loadArenas(this, pinstance.getArenasConfig()));
		Bukkit.getPluginManager().registerEvents(this, this);
		pinstance.scoreboardManager = new IArenaScoreboard(this);
		pinstance.arenaSetup = new IArenaSetup();
		IArenaListener listener = new IArenaListener(this, pinstance, "bedwars");
		pinstance.setArenaListener(listener);
		MinigamesAPI.getAPI().registerArenaListenerLater(this, listener);
		try {
			pinstance.getClass().getMethod("setAchievementGuiEnabled", boolean.class);
			pinstance.setAchievementGuiEnabled(true);
		} catch (NoSuchMethodException e) {
			System.out.println("Update your MinigamesLib to the latest version to use the Achievement Gui.");
		}
		pli = pinstance;

		boolean continue_ = false;
		for (Method m : pli.getArenaAchievements().getClass().getMethods()) {
			if (m.getName().equalsIgnoreCase("addDefaultAchievement")) {
				continue_ = true;
			}
		}
		if (continue_) {
			// pli.getArenaAchievements().addDefaultAchievement("destroy_hundred_blocks_with_bow", "Destroy 100 blocks with your bow in one game!",
			// 100);
			// pli.getArenaAchievements().addDefaultAchievement("destroy_thousand_blocks_with_bow_alltime",
			// "Destroy 1000 blocks with your bow all-time!", 1000);
			// pli.getArenaAchievements().addDefaultAchievement("win_game_with_one_life", "Win a game with one life left!", 200);
			// pli.getAchievementsConfig().getConfig().options().copyDefaults(true);
			// pli.getAchievementsConfig().saveConfig();
		}
	}

	public static ArrayList<Arena> loadArenas(JavaPlugin plugin, ArenasConfig cf) {
		ArrayList<Arena> ret = new ArrayList<Arena>();
		FileConfiguration config = cf.getConfig();
		if (!config.isSet("arenas")) {
			return ret;
		}
		for (String arena : config.getConfigurationSection("arenas.").getKeys(false)) {
			if (Validator.isArenaValid(plugin, arena, cf.getConfig())) {
				ret.add(initArena(arena));
			}
		}
		return ret;
	}

	public static IArena initArena(String arena) {
		IArena a = new IArena(m, arena);
		ArenaSetup s = MinigamesAPI.getAPI().pinstances.get(m).arenaSetup;
		a.init(Util.getSignLocationFromArena(m, arena), Util.getAllSpawns(m, arena), Util.getMainLobby(m), Util.getComponentForArena(m, arena, "lobby"), s.getPlayerCount(m, arena, true), s.getPlayerCount(m, arena, false), s.getArenaVIP(m, arena));
		return a;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		boolean ret = cmdhandler.handleArgs(this, "mgbedwars", "/" + cmd.getName(), sender, args);
		if (args[0].equalsIgnoreCase("setupbeds")) {
			if (args.length > 1) {

				// /bw setupbeds <arena>
				if (sender instanceof Player) {
					Player p = (Player) sender;

					p.getInventory().addItem(getSetupBed(args[1], "red"));
					p.getInventory().addItem(getSetupBed(args[1], "blue"));
					p.getInventory().addItem(getSetupBed(args[1], "green"));
					p.getInventory().addItem(getSetupBed(args[1], "yellow"));

					p.updateInventory();
				} else {
					// TODO send player command args
				}
			}
		}
		return ret;
	}

	public ItemStack getSetupBed(String arena, String team) {
		ItemStack item = new ItemStack(Material.BED);
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName("bwbeds:" + team + "#" + arena);
		item.setItemMeta(itemmeta);
		return item;
	}

	@EventHandler
	public void onPlaceBed(BlockMultiPlaceEvent event) {
		if (event.getBlock().getType() == Material.BED_BLOCK) {
			if (event.getItemInHand().hasItemMeta()) {
				if (event.getItemInHand().getItemMeta().hasDisplayName()) {
					String displayname = event.getItemInHand().getItemMeta().getDisplayName();
					if (displayname.startsWith("bwbeds:")) {
						// bwbed:team#arena
						int a = displayname.indexOf("#");
						String arena = displayname.substring(a + 1);
						String team = displayname.substring(displayname.indexOf(":") + 1, a);
						System.out.println("#" + arena + " " + team);
						Util.saveComponentForArena(this, arena, team + "_bed.loc1", event.getBlock().getLocation());
						Location l = event.getBlock().getLocation();
						for (int i = -3; i < 3; i++) {
							for (int j = -3; j < 3; j++) {
								Location l_ = l.clone().add(i, 0, j);
								if (l.getBlockX() == l_.getBlockX() && l.getBlockY() == l_.getBlockY() && l.getBlockZ() == l_.getBlockZ()) {
									// Skip
								} else {
									if (l_.getBlock().getType() == Material.BED_BLOCK) {
										Util.saveComponentForArena(this, arena, team + "_bed.loc2", l_);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.hasItem() && event.hasBlock()) {
			if (event.getItem().getType() == Material.BED) {

			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent event) {
		final Player p = event.getPlayer();
		if (pli.global_players.containsKey(p.getName())) {
			IArena a = (IArena) pli.global_players.get(p.getName());
			if (a.getArenaState() == ArenaState.INGAME) {
				if (p.getLocation().getY() < 0) {
					// player fell
					if (pteam.containsKey(p.getName())) {
						String team = m.pteam.get(p.getName());
						String playername = p.getName();
						Util.clearInv(p);
						if (team == "red") {
							if (a.red_bed) {
								Util.teleportPlayerFixed(p, Util.getComponentForArena(m, a.getName(), "spawns.spawn" + m.pteam.get(playername)));
							} else {
								a.spectate(p.getName());
							}
						} else if (team == "blue") {
							if (a.blue_bed) {
								Util.teleportPlayerFixed(p, Util.getComponentForArena(m, a.getName(), "spawns.spawn" + m.pteam.get(playername)));
							} else {
								a.spectate(p.getName());
							}
						} else if (team == "green") {
							if (a.green_bed) {
								Util.teleportPlayerFixed(p, Util.getComponentForArena(m, a.getName(), "spawns.spawn" + m.pteam.get(playername)));
							} else {
								a.spectate(p.getName());
							}
						} else if (team == "yellow") {
							if (a.yellow_bed) {
								Util.teleportPlayerFixed(p, Util.getComponentForArena(m, a.getName(), "spawns.spawn" + m.pteam.get(playername)));
							} else {
								a.spectate(p.getName());
							}
						}
						scoreboard.updateScoreboard(a);
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (pli.global_players.containsKey(p.getName())) {
				IArena a = (IArena) pli.global_players.get(p.getName());
				if (a.getArenaState() == ArenaState.INGAME) {
					event.getDrops().clear();
					if (m.pteam.containsKey(p.getName())) {
						String team = m.pteam.get(p.getName());
						String playername = p.getName();
						if (team == "red") {
							if (a.red_bed) {
								Util.teleportPlayerFixed(p, Util.getComponentForArena(m, this.getName(), "spawns.spawn" + m.pteam.get(playername)));
							} else {
								a.spectate(p.getName());
							}
						} else if (team == "blue") {
							if (a.blue_bed) {
								Util.teleportPlayerFixed(p, Util.getComponentForArena(m, this.getName(), "spawns.spawn" + m.pteam.get(playername)));
							} else {
								a.spectate(p.getName());
							}
						} else if (team == "green") {
							if (a.green_bed) {
								Util.teleportPlayerFixed(p, Util.getComponentForArena(m, this.getName(), "spawns.spawn" + m.pteam.get(playername)));
							} else {
								a.spectate(p.getName());
							}
						} else if (team == "yellow") {
							if (a.yellow_bed) {
								Util.teleportPlayerFixed(p, Util.getComponentForArena(m, this.getName(), "spawns.spawn" + m.pteam.get(playername)));
							} else {
								a.spectate(p.getName());
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player) {
			final Player p = (Player) event.getEntity();
			if (pli.global_players.containsKey(p.getName())) {
				IArena a = (IArena) pli.global_players.get(p.getName());
				if (a.getArenaState() == ArenaState.INGAME) {
					if (event.getDamager() instanceof Player) {
						Player p2 = (Player) event.getDamager();
						if (m.pteam.get(p.getName()).equalsIgnoreCase(m.pteam.get(p2.getName()))) {
							// same team
							event.setCancelled(true);
						}
					} else if (event.getDamager() instanceof Arrow) {
						Arrow ar = (Arrow) event.getDamager();
						if (ar.getShooter() instanceof Player) {
							Player p2 = (Player) ar.getShooter();
							if (m.pteam.get(p.getName()).equalsIgnoreCase(m.pteam.get(p2.getName()))) {
								// same team
								event.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		final Player p = event.getPlayer();
		if (pli.global_players.containsKey(p.getName())) {
			IArena a = (IArena) pli.global_players.get(p.getName());
			if (a.getArenaState() == ArenaState.INGAME) {
				if (!(event.getBlock().getType() == Material.SANDSTONE)) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		final Player p = event.getPlayer();
		if (pli.global_players.containsKey(p.getName())) {
			IArena a = (IArena) pli.global_players.get(p.getName());
			if (a.getArenaState() == ArenaState.INGAME) {
				System.out.println(event.getBlock().getType());
				if (event.getBlock().getType() == Material.BED_BLOCK) {
					String team = getTeambyBedLocation(a.getName(), event.getBlock().getLocation());
					System.out.println(team);
					if (team == "-") {
						event.setCancelled(true);
						return;
					}
					if (m.pteam.get(p.getName()).equalsIgnoreCase(team)) {
						// don't allow destroying own bed
						event.setCancelled(true);
						return;
					}
					if (team.equalsIgnoreCase("red")) {
						a.red_bed = false;
					} else if (team.equalsIgnoreCase("green")) {
						a.green_bed = false;
					} else if (team.equalsIgnoreCase("blue")) {
						a.blue_bed = false;
					} else if (team.equalsIgnoreCase("yellow")) {
						a.yellow_bed = false;
					}
					return;
				}
				if (event.getBlock().getType() != Material.SANDSTONE) {
					event.setCancelled(true);
				}
			}
		}
	}

	public String getTeambyBedLocation(String arena, Location l) {
		String ret = "-";
		HashMap<String, Location> temp = new HashMap<String, Location>();
		temp.put("yellow_1", Util.getComponentForArena(this, arena, "yellow_bed.loc1"));
		temp.put("yellow_2", Util.getComponentForArena(this, arena, "yellow_bed.loc2"));
		temp.put("red_1", Util.getComponentForArena(this, arena, "red_bed.loc1"));
		temp.put("red_2", Util.getComponentForArena(this, arena, "red_bed.loc2"));
		temp.put("blue_1", Util.getComponentForArena(this, arena, "blue_bed.loc1"));
		temp.put("blue_2", Util.getComponentForArena(this, arena, "blue_bed.loc2"));
		temp.put("green_1", Util.getComponentForArena(this, arena, "green_bed.loc1"));
		temp.put("green_2", Util.getComponentForArena(this, arena, "green_bed.loc2"));

		for (String team : temp.keySet()) {
			if (temp.get(team) != null) {
				Location l_ = temp.get(team);
				if (l.getBlockX() == l_.getBlockX() && l.getBlockY() == l_.getBlockY() && l.getBlockZ() == l_.getBlockZ()) {
					return team.substring(0, team.indexOf("_"));
				}
			}
		}

		return ret;
	}

}

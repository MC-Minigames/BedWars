package com.comze_instancelabs.bedwars;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.comze_instancelabs.minigamesapi.Arena;
import com.comze_instancelabs.minigamesapi.MinigamesAPI;
import com.comze_instancelabs.minigamesapi.util.ArenaScoreboard;

public class IArenaScoreboard extends ArenaScoreboard {

	HashMap<String, Scoreboard> ascore = new HashMap<String, Scoreboard>();
	HashMap<String, Objective> aobjective = new HashMap<String, Objective>();

	JavaPlugin plugin = null;

	public IArenaScoreboard(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void updateScoreboard(final IArena arena) {
		for (String p_ : arena.getAllPlayers()) {
			Player p = Bukkit.getPlayer(p_);
			if (!ascore.containsKey(arena.getName())) {
				ascore.put(arena.getName(), Bukkit.getScoreboardManager().getNewScoreboard());
			}
			if (!aobjective.containsKey(arena.getName())) {
				aobjective.put(arena.getName(), ascore.get(arena.getName()).registerNewObjective(arena.getName(), "dummy"));
			}

			aobjective.get(arena.getName()).setDisplaySlot(DisplaySlot.SIDEBAR);

			aobjective.get(arena.getName()).setDisplayName(MinigamesAPI.getAPI().pinstances.get(plugin).getMessagesConfig().scoreboard_title.replaceAll("<arena>", arena.getName()));

			//ascore.get(arena.getName()).resetScores(Bukkit.getOfflinePlayer(ChatColor.BLUE + Integer.toString(arena.blue - 1) + ChatColor.GRAY + "  :"));
			//ascore.get(arena.getName()).resetScores(Bukkit.getOfflinePlayer(ChatColor.BLUE + Integer.toString(arena.blue + 1) + ChatColor.GRAY + "  :"));
			aobjective.get(arena.getName()).getScore(Bukkit.getOfflinePlayer(ChatColor.BLUE + "BLUE")).setScore(arena.blue);
			aobjective.get(arena.getName()).getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "RED")).setScore(arena.red);
			aobjective.get(arena.getName()).getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "GREEN")).setScore(arena.green);
			aobjective.get(arena.getName()).getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + "YELLOW")).setScore(arena.yellow);

			p.setScoreboard(ascore.get(arena.getName()));
		}
	}

	@Override
	public void updateScoreboard(JavaPlugin plugin, final Arena arena) {
		IArena a = (IArena) MinigamesAPI.getAPI().pinstances.get(plugin).getArenaByName(arena.getName());
		this.updateScoreboard(a);
	}

	@Override
	public void removeScoreboard(String arena, Player p) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard sc = manager.getNewScoreboard();
		sc.clearSlot(DisplaySlot.SIDEBAR);
		p.setScoreboard(sc);
	}

}

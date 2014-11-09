package com.comze_instancelabs.bedwars;

import org.bukkit.plugin.java.JavaPlugin;

import com.comze_instancelabs.minigamesapi.config.ClassesConfig;

public class IClassesConfig extends ClassesConfig {

	public IClassesConfig(JavaPlugin plugin) {
		super(plugin, true);
		this.getConfig().options().header("There are no classes for BedWars.");

		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}

}

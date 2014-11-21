package com.comze_instancelabs.bedwars;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class GUIConfig {

	private FileConfiguration arenaConfig = null;
	private File arenaFile = null;
	private JavaPlugin plugin = null;

	public GUIConfig(JavaPlugin plugin) {
		this.plugin = plugin;
		this.getConfig().options().header("All Gui options:");

		this.getConfig().addDefault("maingui.category_items.Blocks.items", "24*1");
		this.getConfig().addDefault("maingui.category_items.Armor.items", "303*1");
		this.getConfig().addDefault("maingui.category_items.Pickaxes.items", "257*1");
		this.getConfig().addDefault("maingui.category_items.Swords.items", "283*1");
		this.getConfig().addDefault("maingui.category_items.Bows.items", "261*1");
		this.getConfig().addDefault("maingui.category_items.Consumables.items", "364*1");
		this.getConfig().addDefault("maingui.category_items.Chests.items", "54*1");
		this.getConfig().addDefault("maingui.category_items.Potions.items", "374:1*1");
		this.getConfig().addDefault("maingui.category_items.Specials.items", "46*1");

		this.getConfig().addDefault("blocksgui.trades.trade1.items", "24*2;336*1");
		this.getConfig().addDefault("blocksgui.trades.trade2.items", "121*1;336*7");
		this.getConfig().addDefault("blocksgui.trades.trade3.items", "89*4;336*15");
		this.getConfig().addDefault("blocksgui.trades.trade4.items", "42*1;265*3");
		this.getConfig().addDefault("blocksgui.trades.trade5.items", "20*1;336*4");
		this.getConfig().addDefault("blocksgui.trades.trade6.items", "42*1;265*3");

		this.getConfig().addDefault("armorgui.trades.trade1.items", "298#PROTECTION_ENVIRONMENTAL:1#DURABILITY:1*1;336*1");
		this.getConfig().addDefault("armorgui.trades.trade2.items", "300#PROTECTION_ENVIRONMENTAL:1#DURABILITY:1*1;336*1");
		this.getConfig().addDefault("armorgui.trades.trade3.items", "301#PROTECTION_ENVIRONMENTAL:1#DURABILITY:1*1;336*1");
		this.getConfig().addDefault("armorgui.trades.trade4.items", "303#PROTECTION_ENVIRONMENTAL:1#DURABILITY:1*1;265*1");
		this.getConfig().addDefault("armorgui.trades.trade5.items", "303#PROTECTION_ENVIRONMENTAL:2#DURABILITY:1*1;265*3");
		this.getConfig().addDefault("armorgui.trades.trade6.items", "303#PROTECTION_ENVIRONMENTAL:3#DURABILITY:1*1;265*7");

		this.getConfig().addDefault("pickaxesgui.trades.trade1.items", "270#DIG_SPEED:1#DURABILITY:1*1;336*1");
		this.getConfig().addDefault("pickaxesgui.trades.trade2.items", "274#DIG_SPEED:1#DURABILITY:1*1;265*2");
		this.getConfig().addDefault("pickaxesgui.trades.trade3.items", "257#DIG_SPEED:3#DURABILITY:1*1;266*1");

		this.getConfig().addDefault("swordsgui.trades.trade1.items", "280#KNOCKBACK:1*1;336*8");
		this.getConfig().addDefault("swordsgui.trades.trade2.items", "283#DAMAGE_ALL:1#DURABILITY:1*1;265*1");
		this.getConfig().addDefault("swordsgui.trades.trade3.items", "283#DAMAGE_ALL:2#DURABILITY:1*1;265*3");
		this.getConfig().addDefault("swordsgui.trades.trade4.items", "267#DAMAGE_ALL:1#DURABILITY:1*1;266*5");

		this.getConfig().addDefault("bowsgui.trades.trade1.items", "261#ARROW_INFINITE:1*1;266*3");
		this.getConfig().addDefault("bowsgui.trades.trade2.items", "261#ARROW_INFINITE:1#ARROW_DAMAGE:1*1;266*7");
		this.getConfig().addDefault("bowsgui.trades.trade3.items", "261#ARROW_INFINITE:1#ARROW_DAMAGE:1#ARROW_KNOCKBACK:1*1;266*13");
		this.getConfig().addDefault("bowsgui.trades.trade4.items", "262*1;266*1");

		this.getConfig().addDefault("consumablesgui.trades.trade1.items", "260*1;336*1");
		this.getConfig().addDefault("consumablesgui.trades.trade2.items", "320*1;336*2");
		this.getConfig().addDefault("consumablesgui.trades.trade3.items", "354*1;265*1");
		this.getConfig().addDefault("consumablesgui.trades.trade4.items", "322*1;266*2");

		this.getConfig().addDefault("chestsgui.trades.trade1.items", "54*1;265*1");
		this.getConfig().addDefault("chestsgui.trades.trade2.items", "130*1;266*1");

		this.getConfig().addDefault("potionsgui.trades.trade1.items", "373:16341*1;265*3");
		this.getConfig().addDefault("potionsgui.trades.trade2.items", "373:16373*1;265*5");
		this.getConfig().addDefault("potionsgui.trades.trade3.items", "373:16274*1;265*7");
		this.getConfig().addDefault("potionsgui.trades.trade4.items", "373:16281*1;266*7");

		this.getConfig().addDefault("specialsgui.trades.trade1.items", "65*1;336*1");
		this.getConfig().addDefault("specialsgui.trades.trade2.items", "30*1;336*16");
		this.getConfig().addDefault("specialsgui.trades.trade3.items", "346*1;265*6");
		this.getConfig().addDefault("specialsgui.trades.trade4.items", "259*1;265*7");
		this.getConfig().addDefault("specialsgui.trades.trade5.items", "46*1;266*3");
		this.getConfig().addDefault("specialsgui.trades.trade6.items", "368*1;266*13");

		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}

	public FileConfiguration getConfig() {
		if (arenaConfig == null) {
			reloadConfig();
		}
		return arenaConfig;
	}

	public void saveConfig() {
		if (arenaConfig == null || arenaFile == null) {
			return;
		}
		try {
			getConfig().save(arenaFile);
		} catch (IOException ex) {

		}
	}

	public void reloadConfig() {
		if (arenaFile == null) {
			arenaFile = new File(plugin.getDataFolder(), "gui.yml");
		}
		arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);

		InputStream defConfigStream = plugin.getResource("gui.yml");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			arenaConfig.setDefaults(defConfig);
		}
	}
}

package me.limeglass.skrpg;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
public class SkRPG extends JavaPlugin {

	SkriptAddon addon;
	SkRPG instance;
	
	public void onEnable() {
		addon = Skript.registerAddon(this);
		instance = this;
		new Register();
		Bukkit.getServer().getLogger().info("[SkRPG] Plugin has been Enabled");
	}
	
	public SkRPG getInstance() {
		return instance;
	}
	
	public SkriptAddon getAddonInstance() {
		return addon;
	}
}
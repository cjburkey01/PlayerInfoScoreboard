package com.cjburkey.factions.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import com.cjburkey.factions.scoreboard.stat.Metrics;
import net.milkbowl.vault.economy.Economy;

public class FS extends JavaPlugin {
	
	private static FS plugin;
	private static Economy econ = null;
	private static ScoreboardManager sbmanager;
	private static Scoreboard sb;
	private static Objective obj;
	
	public static final FS getPlugin() { return plugin; }
	public static final Economy getEcon() { return econ; }
	public static final ScoreboardManager getSBManager() { return sbmanager; }
	public static final Scoreboard getScoreBoard() { return sb; }
	public static final Objective getObjective() { return obj; }
	public static final String getConfigString(String name) { return Util.color(getPlugin().getConfig().getString(name)); }
	
	public void onEnable() {
		plugin = this;
		sbmanager = Bukkit.getScoreboardManager();
		sb = sbmanager.getNewScoreboard();
		
		try {
			Metrics m = new Metrics(this);
			m.start();
		} catch(Exception e) {
			Util.log("&4Failed to submit stats :(");
		}
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		String name = FS.getConfigString("ServerName");
		obj = FS.getScoreBoard().registerNewObjective(name, "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(name);
		
		getServer().getPluginManager().registerEvents(new Listener() { @EventHandler public void handle(PlayerJoinEvent e) { PlayerUpdate.doit(e.getPlayer()); } }, this);
		
		if(!setupEconomy()) {
			Util.log("&4Econ couldn't be setup!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		} else {
			Util.log("&2Econ setup!");
		}
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() {
				for(Player p : getServer().getOnlinePlayers()) {
					PlayerUpdate.doit(p);
				}
			}
		}, 20, 20 * getConfig().getInt("UpdateDelayInSeconds"));
	}
	
	public void onDisable() {  }
	
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
	
	private static int num = 0;
	public static final void addScore(String name, Player p) {
		Scoreboard b = FS.getScoreBoard();
		Objective o = FS.getObjective();
		Score s = o.getScore(name);
		s.setScore(num);
		p.setScoreboard(b);
		num ++;
	}
	
	public static final void resetNum() { num = 0; }
	
}
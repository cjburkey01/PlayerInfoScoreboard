package com.cjburkey.factions.scoreboard;

import org.bukkit.entity.Player;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

public class PlayerUpdate {
	
	public static final void doit(Player p) {
		MPlayer player = MPlayer.get(p);
		Faction f = player.getFaction();

		FS.addScore(FS.getConfigString("LineBreak") + " ", p);
		FS.addScore(FS.getConfigString("HealthText") + p.getHealth(), p);
		FS.addScore(FS.getConfigString("FoodText") + p.getFoodLevel(), p);
		FS.addScore(FS.getConfigString("MoneyText") + FS.getEcon().format(FS.getEcon().getBalance(p)), p);
		FS.addScore(FS.getConfigString("FactionText") + f.getName(), p);
		FS.addScore(FS.getConfigString("SiteText"), p);
		FS.addScore(FS.getConfigString("LineBreak"), p);
		
		if(FS.getPlugin().getConfig().getBoolean("DebugSpam")) Util.log("&2Updated Display");
		
		FS.resetNum();
		
		p.setScoreboard(FS.getScoreBoard());
	}
	
}
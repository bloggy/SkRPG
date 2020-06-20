package me.limeglass.skrpg.Conditions;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import com.sucy.skill.SkillAPI;

import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;

public class CondHasSkill extends Condition {
	
	private Expression<String> string;
	private Expression<Player> player;
	private Expression<Long> level;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.player = (Expression<Player>) exprs[0];
		this.string = (Expression<String>) exprs[1];
		this.level = (Expression<Long>) exprs[2];
		return true;
	}

	@Override
	public String toString(Event e, boolean debug) {
		// TODO Auto-generated method stub
		return "%player% has skill %string%";
	}

	@Override
	public boolean check(Event e) {
		Player p = (Player)this.player.getSingle(e);
		String s = (String)this.string.getSingle(e);
		Long l = (Long)this.level.getSingle(e);
		boolean hasSkill = SkillAPI.getPlayerData(p).hasSkill(s);
		if (hasSkill) {
		  //If level was specified in the condition:
		  if (l != null) {
		    int skillLevel = SkillAPI.getPlayerData(p).getSkillLevel(s);
		    if (l.intValue() == skillLevel) {
		      // player has skill, and the level matches what was specified.
		      return true;
		    }
		    // player has skill, but the level was wrong.
		    return false;
		  }
		  // player has skill, no level was specified
		  return true;
		}
		// player does not have skill
		return false;
	}	
}

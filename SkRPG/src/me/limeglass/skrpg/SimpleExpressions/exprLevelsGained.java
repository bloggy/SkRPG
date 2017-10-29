package me.limeglass.skrpg.SimpleExpressions;

import org.bukkit.event.Event;

import com.sucy.skill.api.event.PlayerLevelUpEvent;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;

public class exprLevelsGained extends SimpleExpression<Integer> {
	
	@Override
	public boolean init(final Expression<?>[] vars, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
		if (!ScriptLoader.isCurrentEvent(PlayerLevelUpEvent.class)) {
			Skript.error("Cannot use 'SkillAPI gained levels' outside of a level up event", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		return true;
	}
	
	@Override
	protected Integer[] get(final Event e) {
		return new Integer[] {getLevelsGained(e)};
	}
	
	private static Integer getLevelsGained(final Event e) {
		if (e == null)
			return null;
		if (e instanceof PlayerLevelUpEvent) {
			
			final Object o = ((PlayerLevelUpEvent) e).getAmount();
			
			return (Integer) o;
		}
		return null;
	}
	
	@Override
	public Class<? extends Integer> getReturnType() {
		return Integer.class;
	}
	
	@Override
	public String toString(final Event e, final boolean debug) {
		if (e == null)
			return "SkillAPI levels gained";
		return Classes.getDebugMessage(getSingle(e));
	}
	
	@Override
	public boolean isSingle() {
		return true;
	}
	
}
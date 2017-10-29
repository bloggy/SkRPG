package me.limeglass.skrpg.SimpleExpressions;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import com.sucy.skill.api.event.SkillDamageEvent;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.skript.log.ErrorQuality;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.Kleenean;

public class exprDamager extends SimpleExpression<Entity> {
	
	@Override
	public boolean init(final Expression<?>[] vars, final int matchedPattern, final Kleenean isDelayed, final ParseResult parser) {
		if (!ScriptLoader.isCurrentEvent(SkillDamageEvent.class)) {
			Skript.error("Cannot use 'SkillAPI damager' outside of a SkillDamage event", ErrorQuality.SEMANTIC_ERROR);
			return false;
		}
		return true;
	}
	
	@Override
	protected Entity[] get(final Event e) {
		return new Entity[] {getDamager(e)};
	}
	
	private static Entity getDamager(final Event e) {
		if (e == null)
			return null;
		if (e instanceof SkillDamageEvent) {
			
			final Object o = ((SkillDamageEvent) e).getDamager();
			
			return (Entity) o;
		}
		return null;
	}
	
	@Override
	public Class<? extends Entity> getReturnType() {
		return Entity.class;
	}
	
	@Override
	public String toString(final Event e, final boolean debug) {
		if (e == null)
			return "SkillAPI attacker/damager";
		return Classes.getDebugMessage(getSingle(e));
	}
	
	@Override
	public boolean isSingle() {
		return true;
	}
	
}
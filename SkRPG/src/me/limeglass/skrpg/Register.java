package me.limeglass.skrpg;

import javax.annotation.Nullable;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.sucy.skill.api.enums.ExpSource;
import com.sucy.skill.api.event.PlayerCastSkillEvent;
import com.sucy.skill.api.event.PlayerClassChangeEvent;
import com.sucy.skill.api.event.PlayerExperienceGainEvent;
import com.sucy.skill.api.event.PlayerExperienceLostEvent;
import com.sucy.skill.api.event.PlayerLevelUpEvent;
import com.sucy.skill.api.event.PlayerManaGainEvent;
import com.sucy.skill.api.event.PlayerManaLossEvent;
import com.sucy.skill.api.event.PlayerSkillUnlockEvent;
import com.sucy.skill.api.event.SkillDamageEvent;
import com.sucy.skill.api.event.SkillHealEvent;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.util.SimpleEvent;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import me.limeglass.skrpg.Conditions.CondCanProfess;
import me.limeglass.skrpg.Conditions.CondHasSkill;
import me.limeglass.skrpg.Conditions.CondSkillbarIsEnabled;
import me.limeglass.skrpg.Conditions.CondSkillbarIsSetup;
import me.limeglass.skrpg.Effects.EffAddExp;
import me.limeglass.skrpg.Effects.EffAddLevel;
import me.limeglass.skrpg.Effects.EffAddSkillPoints;
import me.limeglass.skrpg.Effects.EffAllowExpSource;
import me.limeglass.skrpg.Effects.EffCastSkill;
import me.limeglass.skrpg.Effects.EffClearBonuses;
import me.limeglass.skrpg.Effects.EffClearPlayerData;
import me.limeglass.skrpg.Effects.EffDisallowExpSource;
import me.limeglass.skrpg.Effects.EffOpenAttributeMenu;
import me.limeglass.skrpg.Effects.EffProfess;
import me.limeglass.skrpg.Effects.EffRefundAttributePoints;
import me.limeglass.skrpg.Effects.EffRegenerateMana;
import me.limeglass.skrpg.Effects.EffToggleSkillbar;
import me.limeglass.skrpg.Expressions.exprAttributePoints;
import me.limeglass.skrpg.Expressions.exprChatColour;
import me.limeglass.skrpg.Expressions.exprClassGroup;
import me.limeglass.skrpg.Expressions.exprExperience;
import me.limeglass.skrpg.Expressions.exprHealthScale;
import me.limeglass.skrpg.Expressions.exprLevel;
import me.limeglass.skrpg.Expressions.exprMainClass;
import me.limeglass.skrpg.Expressions.exprMana;
import me.limeglass.skrpg.Expressions.exprManaRegen;
import me.limeglass.skrpg.Expressions.exprManaScale;
import me.limeglass.skrpg.Expressions.exprMaxHealth;
import me.limeglass.skrpg.Expressions.exprMaxLevel;
import me.limeglass.skrpg.Expressions.exprMaxMana;
import me.limeglass.skrpg.Expressions.exprRequiredExp;
import me.limeglass.skrpg.Expressions.exprTotalExperience;
import me.limeglass.skrpg.SimpleExpressions.exprCastedSkill;
import me.limeglass.skrpg.SimpleExpressions.exprDamage;
import me.limeglass.skrpg.SimpleExpressions.exprDamager;
import me.limeglass.skrpg.SimpleExpressions.exprExpSource;
import me.limeglass.skrpg.SimpleExpressions.exprExperienceGivenLost;
import me.limeglass.skrpg.SimpleExpressions.exprLevelsGained;
import me.limeglass.skrpg.SimpleExpressions.exprManaGivenLost;
import me.limeglass.skrpg.SimpleExpressions.exprNewClassBaseHealth;
import me.limeglass.skrpg.SimpleExpressions.exprNewClassBaseMana;
import me.limeglass.skrpg.SimpleExpressions.exprNewClassGroup;
import me.limeglass.skrpg.SimpleExpressions.exprNewClassName;
import me.limeglass.skrpg.SimpleExpressions.exprPreviousClassBaseHealth;
import me.limeglass.skrpg.SimpleExpressions.exprPreviousClassBaseMana;
import me.limeglass.skrpg.SimpleExpressions.exprPreviousClassGroup;
import me.limeglass.skrpg.SimpleExpressions.exprPreviousClassName;
import me.limeglass.skrpg.SimpleExpressions.exprUnlockedSkill;
import me.limeglass.skrpg.SimpleExpressions.exprVictim;

public class Register {
	
	public Register() {
		Register.Conditions();
		Register.Effects();
		Register.Events();
		Register.Expressions();
		Register.Types();
	}
	
	private static void Conditions() {
		Skript.registerCondition(CondCanProfess.class, "SkillAPI %player% can profess");
		Skript.registerCondition(CondSkillbarIsEnabled.class, "SkillAPI %player% has skillbar enabled");
		Skript.registerCondition(CondSkillbarIsSetup.class, "SkillAPI %player% skillbar is setup");
		Skript.registerCondition(CondHasSkill.class, "SkillAPI %player% has skill %string%");
	}

	private static void Effects() {
		Skript.registerEffect(EffAddLevel.class, new String[] { "SkillAPI add %integer% level[s] to %player% from exp source %expsource%" } );
		Skript.registerEffect(EffAddExp.class, new String[] { "SkillAPI add %integer% Exp[erience] to %player% from exp source %expsource%" } );
		Skript.registerEffect(EffAddSkillPoints.class, new String[] { "SkillAPI add %integer% (sp|skillpoint[s]|skill point[s]) to %player% from exp source %expsource%" } );
		Skript.registerEffect(EffRefundAttributePoints.class, new String[] { "SkillAPI refund %player%'s attribute points", "SkillAPI refund attribute points of %player%"} );
		Skript.registerEffect(EffClearPlayerData.class, new String[] { "SkillAPI clear %player%'s data", "SkillAPI clear data of %player%"} );
		Skript.registerEffect(EffClearBonuses.class, new String[] { "SkillAPI clear %player%'s bonuses", "SkillAPI clear bonuses of %player%"} );
		Skript.registerEffect(EffToggleSkillbar.class, new String[] { "SkillAPI toggle %player%'s skillbar", "SkillAPI toggle skillbar of %player%"} );
		Skript.registerEffect(EffRegenerateMana.class, new String[] { "SkillAPI regen[erate] %player%'s mana", "SkillAPI regen[erate] mana of %player%"} );
		Skript.registerEffect(EffOpenAttributeMenu.class, new String[] { "SkillAPI open attr[ibute] menu (for|of) %player%", "SkillAPI open %player%'s attr[ibute] menu"} );
		Skript.registerEffect(EffAllowExpSource.class, new String[] { "SkillAPI add %ExpSource% (to|from) %string%'s allowed exp[erience] sources"} );
		Skript.registerEffect(EffDisallowExpSource.class, new String[] { "SkillAPI remove %ExpSource% (to|from) %string%'s allowed exp[erience] sources"} );
		Skript.registerEffect(EffCastSkill.class, new String[] { "SkillAPI make %player% cast skill %string%"} );
		Skript.registerEffect(EffProfess.class, new String[] { "SkillAPI profess %player% into %string%" } );
	}
	
	private static void Expressions() {
		//Settable expressions		
		Skript.registerExpression(exprMaxMana.class, Number.class, ExpressionType.PROPERTY, "SkillAPI max mana of %player%", "SkillAPI %player%'s max mana"); // add only
		Skript.registerExpression(exprMainClass.class, String.class, ExpressionType.PROPERTY, "SkillAPI main class of %player%", "SkillAPI %player%'s main class"); // set only
		Skript.registerExpression(exprMaxHealth.class, Number.class, ExpressionType.PROPERTY, "SkillAPI max health of %player%", "SkillAPI %player%'s max health"); // add only
		Skript.registerExpression(exprAttributePoints.class, Number.class, ExpressionType.PROPERTY, "SkillAPI attribute points of %player%", "SkillAPI %player%'s attribute points"); //set and add
		//Non-Settable expressions
		Skript.registerExpression(exprClassGroup.class, String.class, ExpressionType.PROPERTY, "SkillAPI class group of %player%", "SkillAPI %player%'s class group");
		Skript.registerExpression(exprTotalExperience.class, Double.class, ExpressionType.PROPERTY, "SkillAPI total experience of %player%", "SkillAPI %player%'s total experience");
		Skript.registerExpression(exprExperience.class, Double.class, ExpressionType.PROPERTY, "SkillAPI experience of %player%", "SkillAPI %player%'s experience");
		Skript.registerExpression(exprMana.class, Double.class, ExpressionType.PROPERTY, "SkillAPI mana of %player%", "SkillAPI %player%'s mana");
		Skript.registerExpression(exprMaxLevel.class, Integer.class, ExpressionType.PROPERTY, "SkillAPI max level of %player%", "SkillAPI %player%'s max level");
		Skript.registerExpression(exprLevel.class, Integer.class, ExpressionType.PROPERTY, "SkillAPI level of %player%", "SkillAPI %player%'s level");
		Skript.registerExpression(exprRequiredExp.class, Integer.class, ExpressionType.PROPERTY, "SkillAPI required exp[erience] of %player%", "SkillAPI %player%'s required exp[erience]");
		Skript.registerExpression(exprHealthScale.class, Double.class, ExpressionType.PROPERTY, "SkillAPI health scale of %player%", "SkillAPI %player%'s health scale");
		Skript.registerExpression(exprManaScale.class, Double.class, ExpressionType.PROPERTY, "SkillAPI mana scale of %player%", "SkillAPI %player%'s mana scale");
		Skript.registerExpression(exprChatColour.class, ChatColor.class, ExpressionType.PROPERTY, "SkillAPI (chat colo[u]r|chatcolo[u]r) of %player%", "SkillAPI %player%'s (chat colo[u]r|chatcolo[u]r)");
		Skript.registerExpression(exprManaRegen.class, Double.class, ExpressionType.PROPERTY, "SkillAPI mana regen of %player%", "SkillAPI %player%'s mana regen");
		//Simple expressions
		Skript.registerExpression(exprDamager.class, Entity.class, ExpressionType.SIMPLE, "SkillAPI attacker");
		Skript.registerExpression(exprVictim.class, Entity.class, ExpressionType.SIMPLE, "SkillAPI victim");
		Skript.registerExpression(exprDamage.class, Double.class, ExpressionType.SIMPLE, "SkillAPI Damage done");
		Skript.registerExpression(exprExpSource.class, ExpSource.class, ExpressionType.SIMPLE, "SkillAPI ExpSource");
		Skript.registerExpression(exprExperienceGivenLost.class, Double.class, ExpressionType.SIMPLE, "SkillAPI Exp[erience] (Gained|Lost)");
		Skript.registerExpression(exprManaGivenLost.class, Double.class, ExpressionType.SIMPLE, "SkillAPI mana (gained|Lost)");
		Skript.registerExpression(exprCastedSkill.class, String.class, ExpressionType.SIMPLE, "SkillAPI skill casted");
		Skript.registerExpression(exprLevelsGained.class, Integer.class, ExpressionType.SIMPLE, "SkillAPI levels gained");
		Skript.registerExpression(exprPreviousClassBaseHealth.class, Double.class, ExpressionType.SIMPLE, "SkillAPI prev[ious] class base health");
		Skript.registerExpression(exprPreviousClassBaseMana.class, Double.class, ExpressionType.SIMPLE, "SkillAPI prev[ious] class base mana");
		Skript.registerExpression(exprPreviousClassName.class, String.class, ExpressionType.SIMPLE, "SkillAPI prev[ious] class");
		Skript.registerExpression(exprPreviousClassGroup.class, String.class, ExpressionType.SIMPLE, "SkillAPI prev[ious] class group");
		Skript.registerExpression(exprNewClassBaseHealth.class, Double.class, ExpressionType.SIMPLE, "SkillAPI new class base health");
		Skript.registerExpression(exprNewClassBaseMana.class, Double.class, ExpressionType.SIMPLE, "SkillAPI new class base mana");
		Skript.registerExpression(exprNewClassName.class, String.class, ExpressionType.SIMPLE, "SkillAPI new class");
		Skript.registerExpression(exprNewClassGroup.class, String.class, ExpressionType.SIMPLE, "SkillAPI new class group");
		Skript.registerExpression(exprUnlockedSkill.class, String.class, ExpressionType.SIMPLE, "SkillAPI unlocked skill");
	}
	
	private static void Events() {
		Skript.registerEvent("SkillAPI level up", SimpleEvent.class, PlayerLevelUpEvent.class, "SkillAPI level up");
		Skript.registerEvent("SkillAPI exp[erience] gain", SimpleEvent.class, PlayerExperienceGainEvent.class, "SkillAPI exp[erience] gain");
		Skript.registerEvent("SkillAPI exp[erience] loss", SimpleEvent.class, PlayerExperienceLostEvent.class, "SkillAPI exp[erience] loss");
		Skript.registerEvent("SkillAPI class change", SimpleEvent.class, PlayerClassChangeEvent.class, "SkillAPI class change");
		Skript.registerEvent("SkillAPI skill cast", SimpleEvent.class, PlayerCastSkillEvent.class, "SkillAPI skill cast");
		Skript.registerEvent("SkillAPI mana gain", SimpleEvent.class, PlayerManaGainEvent.class, "SkillAPI mana gain");
		Skript.registerEvent("SkillAPI mana loss", SimpleEvent.class, PlayerManaLossEvent.class, "SkillAPI mana loss");
		Skript.registerEvent("SkillAPI skill unlock", SimpleEvent.class, PlayerSkillUnlockEvent.class, "SkillAPI skill unlock");
		Skript.registerEvent("SkillAPI skill heal", SimpleEvent.class, SkillHealEvent.class, "SkillAPI skill heal");
		Skript.registerEvent("SkillAPI skill damage", SimpleEvent.class, SkillDamageEvent.class, "SkillAPI skill damage");
		EventValues.registerEventValue(PlayerLevelUpEvent.class, Player.class, new Getter<Player, PlayerLevelUpEvent>() {
			public Player get(PlayerLevelUpEvent e) {
				return e.getPlayerData().getPlayer().getPlayer();
			}
		}, 0);	
		EventValues.registerEventValue(PlayerExperienceGainEvent.class, Player.class, new Getter<Player, PlayerExperienceGainEvent>() {
			public Player get(PlayerExperienceGainEvent e) {
				return e.getPlayerData().getPlayer().getPlayer();
			}
		}, 0);
		EventValues.registerEventValue(PlayerExperienceLostEvent.class, Player.class, new Getter<Player, PlayerExperienceLostEvent>() {
			public Player get(PlayerExperienceLostEvent e) {
				return e.getPlayerData().getPlayer().getPlayer();
			}
		}, 0);
		EventValues.registerEventValue(PlayerClassChangeEvent.class, Player.class, new Getter<Player, PlayerClassChangeEvent>() {
			public Player get(PlayerClassChangeEvent e) {
				return e.getPlayerData().getPlayer().getPlayer();
			}
		}, 0);
		EventValues.registerEventValue(PlayerCastSkillEvent.class, Player.class, new Getter<Player, PlayerCastSkillEvent>() {
			public Player get(PlayerCastSkillEvent e) {
				return e.getPlayerData().getPlayer().getPlayer();
			}
		}, 0);
		EventValues.registerEventValue(PlayerManaGainEvent.class, Player.class, new Getter<Player, PlayerManaGainEvent>() {
			public Player get(PlayerManaGainEvent e) {
				return e.getPlayerData().getPlayer().getPlayer();
			}
		}, 0);
		EventValues.registerEventValue(PlayerManaLossEvent.class, Player.class, new Getter<Player, PlayerManaLossEvent>() {
			public Player get(PlayerManaLossEvent e) {
				return e.getPlayerData().getPlayer().getPlayer();
			}
		}, 0);
		EventValues.registerEventValue(PlayerSkillUnlockEvent.class, Player.class, new Getter<Player, PlayerSkillUnlockEvent>() {
			public Player get(PlayerSkillUnlockEvent e) {
				return e.getPlayerData().getPlayer().getPlayer();
			}
		}, 0);
	}
	
	private static void Types() {
		Classes.registerClass(new ClassInfo<ExpSource>(ExpSource.class, "expsource").parser(new Parser<ExpSource>() { 
			@Override
			@Nullable
			public ExpSource parse(String s, ParseContext context) {
				try {
					
					return ExpSource.valueOf( s.replace(" ", "_").trim().toUpperCase());
					
				} catch (IllegalArgumentException ex) {
					
					return null;
				}
			}
		
			@Override
			public String toVariableNameString(ExpSource expsource) {
					
				return expsource.name().toLowerCase();
			}
			
			@Override
			public String toString(ExpSource expsource, int flags) {
				
				return expsource.name().toLowerCase();
			}
			
			@Override
			public String getVariableNamePattern() {
				return ".+";
			}
		}));
	}
}

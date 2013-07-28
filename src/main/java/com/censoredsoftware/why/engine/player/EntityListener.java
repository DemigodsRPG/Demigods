package com.censoredsoftware.why.engine.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;

import com.censoredsoftware.why.engine.Demigods;
import com.censoredsoftware.why.engine.battle.Battle;
import com.censoredsoftware.why.engine.language.TranslationManager;
import com.censoredsoftware.why.engine.util.MessageUtility;

public class EntityListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public static void damageEvent(EntityDamageEvent event)
	{
		// Define variables
		LivingEntity entity;
		if(event.getEntityType().equals(EntityType.PLAYER)) // If it's a player
		{
			// Define entity as player and other variables
			entity = (LivingEntity) event.getEntity();

			// NO DAMAGE IN NO PVP ZONES FOR PLAYERS TODO Do we want to keep it that way?
			if(!Battle.Util.canTarget(Battle.Util.defineParticipant(entity))) event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public static void damageByEntityEvent(EntityDamageByEntityEvent event)
	{
		Entity attacked = event.getEntity();
		Entity attacker = event.getDamager();

		if(attacker instanceof Player)
		{
			Player hitting = (Player) attacker;

			// NO PVP
			if(!Battle.Util.canTarget(Battle.Util.defineParticipant(attacked)))
			{
				hitting.sendMessage(ChatColor.GRAY + Demigods.text.getText(TranslationManager.Text.NO_PVP_ZONE));
				event.setCancelled(true);
				return;
			}

			if(attacked instanceof Tameable && ((Tameable) attacked).isTamed() && Pet.Util.getTameable((LivingEntity) attacked) != null && DPlayer.Util.getPlayer(hitting).getCurrent() != null && DCharacter.Util.areAllied(DPlayer.Util.getPlayer(hitting).getCurrent(), Pet.Util.getTameable((LivingEntity) attacked).getOwner()))
			{
				event.setCancelled(true);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public static void entityDeath(EntityDeathEvent event)
	{
		if(event.getEntity() instanceof Player)
		{
			Player player = (Player) event.getEntity();
			DCharacter playerChar = DPlayer.Util.getPlayer(player).getCurrent();

			EntityDamageEvent damageEvent = player.getLastDamageCause();

			if(damageEvent instanceof EntityDamageByEntityEvent)
			{
				EntityDamageByEntityEvent damageByEvent = (EntityDamageByEntityEvent) damageEvent;
				Entity damager = damageByEvent.getDamager();

				if(damager instanceof Player)
				{
					Player attacker = (Player) damager;
					DCharacter attackChar = DPlayer.Util.getPlayer(attacker).getCurrent();
					if(attackChar != null && playerChar != null && DCharacter.Util.areAllied(attackChar, playerChar)) DCharacter.Util.onCharacterBetrayCharacter(attackChar, playerChar);
					else DCharacter.Util.onCharacterKillCharacter(attackChar, playerChar);
				}
			}
		}
		else if(event.getEntity() instanceof Tameable && ((Tameable) event.getEntity()).isTamed())
		{
			LivingEntity entity = event.getEntity();
			Pet wrapper = Pet.Util.getTameable(entity);
			if(wrapper == null) return;
			DCharacter owner = wrapper.getOwner();
			if(owner == null) return;
			String damagerMessage = "";
			if(entity.getLastDamageCause() instanceof EntityDamageByEntityEvent && ((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager() instanceof Player)
			{
				DCharacter damager = DPlayer.Util.getPlayer((Player) ((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager()).getCurrent();
				if(damager != null) damagerMessage = " by " + damager.getDeity().getInfo().getColor() + damager.getName();
			}
			if(entity.getCustomName() != null) MessageUtility.broadcast(owner.getDeity().getInfo().getColor() + owner.getName() + "'s " + ChatColor.YELLOW + entity.getType().getName().replace("Entity", "").toLowerCase() + ", " + owner.getDeity().getInfo().getColor() + entity.getCustomName() + ChatColor.YELLOW + ", was slain" + damagerMessage + ChatColor.YELLOW + ".");
			else MessageUtility.broadcast(owner.getDeity().getInfo().getColor() + owner.getName() + "'s " + ChatColor.YELLOW + entity.getType().getName().replace("Entity", "").toLowerCase() + " was slain" + damagerMessage + ChatColor.YELLOW + ".");
			wrapper.delete();
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onTame(EntityTameEvent event)
	{
		LivingEntity entity = event.getEntity();
		AnimalTamer owner = event.getOwner();
		Pet.Util.create(entity, DPlayer.Util.getPlayer(Bukkit.getOfflinePlayer(owner.getName())).getCurrent());
	}
}

package com.censoredsoftware.Demigods.Engine.Event.Ability;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.Engine.Ability.AbilityInfo;
import com.censoredsoftware.Demigods.Engine.PlayerCharacter.PlayerCharacter;

// TODO Remove this event and replace with a method to be called when this event would be triggered.

/*
 * Represents an event that is called when an ability is executed.
 */
public class AbilityTargetEvent extends Event implements Cancellable
{
	private static final HandlerList handlers = new HandlerList();
	private final PlayerCharacter character;
	private final LivingEntity target;
	private final AbilityInfo info;
	private boolean cancelled = false;

	public AbilityTargetEvent(final PlayerCharacter character, final LivingEntity target, AbilityInfo info)
	{
		this.character = character;
		this.target = target;
		this.info = info;
	}

	public PlayerCharacter getCharacter()
	{
		return this.character;
	}

	public LivingEntity getTarget()
	{
		return this.target;
	}

	public AbilityInfo getInfo()
	{
		return this.info;
	}

	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	@Override
	public boolean isCancelled()
	{
		return this.cancelled;
	}

	@Override
	public synchronized void setCancelled(boolean cancelled)
	{
		this.cancelled = cancelled;
	}
}

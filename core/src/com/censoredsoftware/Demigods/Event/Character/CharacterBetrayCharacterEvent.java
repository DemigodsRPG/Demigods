package com.censoredsoftware.Demigods.Event.Character;

import org.bukkit.event.HandlerList;

import com.censoredsoftware.Demigods.PlayerCharacter.PlayerCharacter;

/*
 * Represents an event that is called when a player is killed by another player.
 */
public class CharacterBetrayCharacterEvent extends CharacterKillCharacterEvent
{
	private static final HandlerList handlers = new HandlerList();
	private PlayerCharacter killedChar;
	private String alliance;

	public CharacterBetrayCharacterEvent(final PlayerCharacter attacker, final PlayerCharacter killedChar, final String alliance)
	{
		super(attacker, killedChar);
		this.alliance = alliance;
	}

	/*
	 * getAlliance() : Gets the alliance involved.
	 */
	public String getAlliance()
	{
		return this.alliance;
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
}
package com.censoredsoftware.demigods;

import com.censoredsoftware.demigods.data.DataManager;
import com.censoredsoftware.demigods.data.ThreadManager;
import com.censoredsoftware.demigods.player.DCharacter;
import com.censoredsoftware.demigods.player.DPlayer;
import com.censoredsoftware.demigods.util.Messages;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Class for all plugins of demigods.
 */
public class DemigodsPlugin extends JavaPlugin
{
	/**
	 * The Bukkit enable method.
	 */
	@Override
	public void onEnable()
	{
		// Load the game engine.
		Demigods.load();

		// Handle online characters
		for(DCharacter character : DCharacter.Util.loadAll())
			character.getMeta().cleanSkills();

		// Print success!
		Messages.info("Successfully enabled.");
	}

	/**
	 * The Bukkit disable method.
	 */
	@Override
	public void onDisable()
	{
		// Save all the data.
		DataManager.save();

		// Handle online characters
		for(DCharacter character : DCharacter.Util.getOnlineCharacters())
			DPlayer.Util.togglePrayingSilent(character.getOfflinePlayer().getPlayer(), false, false);

		// Cancel all threads, Event calls, and connections.
		ThreadManager.stopThreads();
		HandlerList.unregisterAll(this);

		Messages.info("Successfully disabled.");
	}
}

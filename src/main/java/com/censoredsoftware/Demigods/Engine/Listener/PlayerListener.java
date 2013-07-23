package com.censoredsoftware.Demigods.Engine.Listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Module.QuitReasonHandler;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerCharacter;
import com.censoredsoftware.Demigods.Engine.Object.Player.PlayerWrapper;
import com.censoredsoftware.Demigods.Engine.Utility.DataUtility;
import com.censoredsoftware.Demigods.Engine.Utility.ZoneUtility;

public class PlayerListener implements Listener
{
	private static QuitReasonHandler quitReasonFilter = new QuitReasonHandler();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// Define Variables
		Player player = event.getPlayer();
		PlayerWrapper wrapper = PlayerWrapper.getPlayer(player);
		PlayerCharacter character = wrapper.getCurrent();

		// Set their lastlogintime
		Long now = System.currentTimeMillis();
		wrapper.setLastLoginTime(now);

		// Set Displayname
		if(character != null && wrapper.canUseCurrent())
		{
			String name = character.getName();
			ChatColor color = character.getDeity().getInfo().getColor();
			player.setDisplayName(color + name + ChatColor.RESET);
			player.setPlayerListName(color + name + ChatColor.RESET);
			event.getPlayer().setMaxHealth(character.getMaxHealth());
			event.getPlayer().setHealth(character.getHealth());
		}

		if(Demigods.config.getSettingBoolean("misc.welcome_message"))
		{
			player.sendMessage(ChatColor.GRAY + "This server is running Demigods version: " + ChatColor.YELLOW + Demigods.plugin.getDescription().getVersion());
			player.sendMessage(ChatColor.GRAY + "Type " + ChatColor.GREEN + "/dg" + ChatColor.GRAY + " for more information.");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		// Define variables
		final Player player = event.getPlayer();
		Location to = event.getTo();
		Location from = event.getFrom();
		int delayTime = Demigods.config.getSettingInt("zones.pvp_area_delay_time");

		// No-PVP Zones
		onPlayerLineJump(player, to, from, delayTime);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		// Define variables
		final Player player = event.getPlayer();
		Location to = event.getTo();
		Location from = event.getFrom();
		int delayTime = Demigods.config.getSettingInt("zones.pvp_area_delay_time");

		if(PlayerWrapper.isPraying(player)) PlayerWrapper.togglePraying(player, false);

		// No-PVP Zones
		if(event.getCause() == TeleportCause.ENDER_PEARL || DataUtility.hasKeyTemp(player.getName(), "teleport_ability"))
		{
			onPlayerLineJump(player, to, from, delayTime);
		}
		else if(ZoneUtility.enterZoneNoPVP(to, from))
		{
			DataUtility.removeTemp(player.getName(), "was_PVP");
			player.sendMessage(ChatColor.GRAY + "You are now safe from all PVP!");
		}
		else if(ZoneUtility.exitZoneNoPVP(to, from))
		{
			player.sendMessage(ChatColor.GRAY + "You can now PVP!");
			return;
		}
	}

	public void onPlayerLineJump(final OfflinePlayer player, Location to, Location from, int delayTime)
	{
		// NullPointer Check
		if(to == null || from == null || !player.isOnline()) return;

		if(DataUtility.hasKeyTemp(player.getName(), "was_PVP")) return;

		// No Spawn Line-Jumping
		if(ZoneUtility.enterZoneNoPVP(to, from) && delayTime > 0)
		{
			DataUtility.saveTemp(player.getName(), "was_PVP", true);
			if(DataUtility.hasKeyTemp(player.getName(), "teleport_ability")) DataUtility.removeTemp(player.getName(), "teleport_ability");

			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Demigods.plugin, new Runnable()
			{
				@Override
				public void run()
				{
					if(!player.isOnline()) return;
					DataUtility.removeTemp(player.getPlayer().getName(), "was_PVP");
					if(ZoneUtility.zoneNoPVP(player.getPlayer().getLocation())) player.getPlayer().sendMessage(ChatColor.GRAY + "You are now safe from all PVP!");
				}
			}, (delayTime * 20));
		}

		// Let players know where they can PVP
		if(!DataUtility.hasKeyTemp(player.getName(), "was_PVP"))
		{
			if(ZoneUtility.exitZoneNoPVP(to, from)) player.getPlayer().sendMessage(ChatColor.GRAY + "You can now PVP!");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		String name = event.getPlayer().getName();
		String message = ChatColor.YELLOW + name + " has left the game.";
		switch(quitReasonFilter.getLatestQuitReason())
		{
			case GENERIC_REASON:
				message = ChatColor.YELLOW + name + " has either quit or crashed.";
				break;
			case SPAM:
				message = ChatColor.YELLOW + name + " has disconnected due to spamming.";
				break;
			case END_OF_STREAM:
				message = ChatColor.YELLOW + name + " has lost connection.";
				break;
			case OVERFLOW:
				message = ChatColor.YELLOW + name + " has disconnected due to overload.";
				break;
			case QUITTING:
				// TODO Battle PvP-Logging.
				break;
			case TIMEOUT:
				message = ChatColor.YELLOW + name + " has disconnected due to timeout.";
				break;
		}
		event.setQuitMessage(message);
		PlayerCharacter loggingOff = PlayerWrapper.getPlayer(event.getPlayer()).getCurrent();
		if(loggingOff != null) loggingOff.setLocation(event.getPlayer().getLocation());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		PlayerWrapper wrapper = PlayerWrapper.getPlayer(event.getPlayer());
		if(wrapper.getCurrent() != null)
		{
			double maxhealth = wrapper.getCurrent().getMaxHealth();
			event.getPlayer().setMaxHealth(maxhealth);
			event.getPlayer().setHealth(maxhealth);
		}
	}
}

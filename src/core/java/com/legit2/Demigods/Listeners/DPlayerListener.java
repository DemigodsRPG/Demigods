/*
	Copyright (c) 2013 The Demigods Team
	
	Demigods License v1
	
	This plugin is provided "as is" and without any warranty.  Any express or
	implied warranties, including, but not limited to, the implied warranties
	of merchantability and fitness for a particular purpose are disclaimed.
	In no event shall the authors be liable to any party for any direct,
	indirect, incidental, special, exemplary, or consequential damages arising
	in any way out of the use or misuse of this plugin.
	
	Definitions
	
	 1. This Plugin is defined as all of the files within any archive
	    file or any group of files released in conjunction by the Demigods Team,
	    the Demigods Team, or a derived or modified work based on such files.
	
	 2. A Modification, or a Mod, is defined as this Plugin or a derivative of
	    it with one or more Modification applied to it, or as any program that
	    depends on this Plugin.
	
	 3. Distribution is defined as allowing one or more other people to in
	    any way download or receive a copy of this Plugin, a Modified
	    Plugin, or a derivative of this Plugin.
	
	 4. The Software is defined as an installed copy of this Plugin, a
	    Modified Plugin, or a derivative of this Plugin.
	
	 5. The Demigods Team is defined as Alexander Chauncey and Alex Bennett
	    of http://www.clashnia.com/.
	
	Agreement
	
	 1. Permission is hereby granted to use, copy, modify and/or
	    distribute this Plugin, provided that:
	
	    a. All copyright notices within source files and as generated by
	       the Software as output are retained, unchanged.
	
	    b. Any Distribution of this Plugin, whether as a Modified Plugin
	       or not, includes this license and is released under the terms
	       of this Agreement. This clause is not dependant upon any
	       measure of changes made to this Plugin.
	
	    c. This Plugin, Modified Plugins, and derivative works may not
	       be sold or released under any paid license without explicit 
	       permission from the Demigods Team. Copying fees for the 
	       transport of this Plugin, support fees for installation or
	       other services, and hosting fees for hosting the Software may,
	       however, be imposed.
	
	    d. Any Distribution of this Plugin, whether as a Modified
	       Plugin or not, requires express written consent from the
	       Demigods Team.
	
	 2. You may make Modifications to this Plugin or a derivative of it,
	    and distribute your Modifications in a form that is separate from
	    the Plugin. The following restrictions apply to this type of
	    Modification:
	
	    a. A Modification must not alter or remove any copyright notices
	       in the Software or Plugin, generated or otherwise.
	
	    b. When a Modification to the Plugin is released, a
	       non-exclusive royalty-free right is granted to the Demigods Team
	       to distribute the Modification in future versions of the
	       Plugin provided such versions remain available under the
	       terms of this Agreement in addition to any other license(s) of
	       the initial developer.
	
	    c. Any Distribution of a Modified Plugin or derivative requires
	       express written consent from the Demigods Team.
	
	 3. Permission is hereby also granted to distribute programs which
	    depend on this Plugin, provided that you do not distribute any
	    Modified Plugin without express written consent.
	
	 4. The Demigods Team reserves the right to change the terms of this
	    Agreement at any time, although those changes are not retroactive
	    to past releases, unless redefining the Demigods Team. Failure to
	    receive notification of a change does not make those changes invalid.
	    A current copy of this Agreement can be found included with the Plugin.
	
	 5. This Agreement will terminate automatically if you fail to comply
	    with the limitations described herein. Upon termination, you must
	    destroy all copies of this Plugin, the Software, and any
	    derivatives within 48 hours.
 */

package com.legit2.Demigods.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.legit2.Demigods.Demigods;
import com.legit2.Demigods.Libraries.Objects.PlayerCharacter;

public class DPlayerListener implements Listener
{
	public static final Demigods API = Demigods.INSTANCE;
	public static boolean filterCheckGeneric = false;
	public static boolean filterCheckStream = false;
	public static boolean filterCheckOverflow = false;
	public static boolean filterCheckQuitting = false;
	public static boolean filterCheckTimeout = false;

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		Player player = event.getPlayer();
		try
		{
			// Check to see if the player exists
			if(!API.data.newPlayer(player))
			{
				API.misc.info(player.getName() + " already has a player save.");
				return;
			}

			// If not, check whitelist details and add the player accordingly
			if(Bukkit.getServer().hasWhitelist())
			{
				if(player.isWhitelisted()) API.player.createNewPlayer(player);
			}
			else API.player.createNewPlayer(player);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// Define Variables
		Player player = event.getPlayer();
		PlayerCharacter character = API.player.getCurrentChar(player);

		// Set their lastlogintime
		API.data.savePlayerData(player, "player_lastlogin", System.currentTimeMillis());

		// Set Displayname
		if(character != null)
		{
			String name = character.getName();
			ChatColor color = API.deity.getDeityColor(character.getDeity());
			player.setDisplayName(color + name + ChatColor.WHITE);
			player.setPlayerListName(color + name + ChatColor.WHITE);
		}

		if(API.config.getSettingBoolean("motd"))
		{
			player.sendMessage(ChatColor.GRAY + "This server is running Demigods version: " + ChatColor.YELLOW + API.getDescription().getVersion());
			player.sendMessage(ChatColor.GRAY + "Type " + ChatColor.GREEN + "/dg" + ChatColor.GRAY + " for more information.");
		}

		/*
		 * if((!DConfigUtilUtil.getSettingBoolean("auto_update")) && (DUpdate.shouldUpdate()) && DMiscUtil.hasPermissionOrOP(player, "demigods.admin"))
		 * {
		 * player.sendMessage(ChatColor.RED + "There is a new, stable release for API.");
		 * player.sendMessage(ChatColor.RED + "Please update ASAP.");
		 * player.sendMessage(ChatColor.RED + "Latest: " + ChatColor.GREEN + "dev.bukkit.org/server-mods/demigods");
		 * }
		 */
	}

	/*
	 * @EventHandler(priority = EventPriority.HIGHEST)
	 * public void onPlayerCraft(CraftItemEvent event)
	 * {
	 * // Define variables
	 * Player player = (Player) event.getWhoClicked();
	 * InventoryType invType = event.getInventory().getType();
	 * ArrayList<ItemStack> allSouls = DSouls.returnAllSouls();
	 * 
	 * if(invType.equals(InventoryType.CRAFTING) || invType.equals(InventoryType.WORKBENCH))
	 * {
	 * ItemStack[] invItems = event.getInventory().getContents();
	 * 
	 * for(ItemStack soul : allSouls)
	 * {
	 * for(ItemStack invItem : invItems)
	 * {
	 * if(invItem.isSimilar(soul))
	 * {
	 * event.setCancelled(true);
	 * player.sendMessage(ChatColor.RED + "You cannot craft with souls!");
	 * }
	 * }
	 * }
	 * }
	 * }
	 */

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event)
	{
		// Define variables
		final Player player = event.getPlayer();
		Location to = event.getTo();
		Location from = event.getFrom();
		int delayTime = API.config.getSettingInt("pvp_area_delay_time");

		// No-PVP Zones
		onPlayerLineJump(player, to, from, delayTime);

		// Player Hold
		if(API.data.hasPlayerData(player, "temp_player_hold"))
		{
			if(from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ())
			{
				event.setCancelled(true);
				player.teleport(from);
				API.data.savePlayerData(player, "temp_player_held", true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerTeleport(PlayerTeleportEvent event)
	{
		// Define variables
		final Player player = event.getPlayer();
		Location to = event.getTo();
		Location from = event.getFrom();
		int delayTime = API.config.getSettingInt("pvp_area_delay_time");

		// No-PVP Zones
		if(event.getCause() == TeleportCause.ENDER_PEARL || API.data.hasPlayerData(player, "temp_teleport_ability"))
		{
			onPlayerLineJump(player, to, from, delayTime);
		}
		else if(API.zone.enterZoneNoPVP(to, from))
		{
			API.data.removePlayerData(player, "temp_was_PVP");
			player.sendMessage(ChatColor.GRAY + "You are now safe from all PVP!");
		}
		else if(API.zone.exitZoneNoPVP(to, from))
		{
			player.sendMessage(ChatColor.GRAY + "You can now PVP!");
			return;
		}

		// Player Hold
		if(API.data.hasPlayerData(player, "temp_player_held")) API.data.removePlayerData(player, "temp_player_held");
		else if(API.data.hasPlayerData(player, "temp_player_hold")) event.setCancelled(true);
	}

	public void onPlayerLineJump(final Player player, Location to, Location from, int delayTime)
	{
		// NullPointer Check
		if(to == null || from == null) return;

		if(API.data.hasPlayerData(player, "temp_was_PVP")) return;

		// No Spawn Line-Jumping
		if(API.zone.enterZoneNoPVP(to, from) && delayTime > 0)
		{
			API.data.savePlayerData(player, "temp_was_PVP", true);
			if(API.data.hasPlayerData(player, "temp_teleport_ability")) API.data.removePlayerData(player, "temp_teleport_ability");

			API.getServer().getScheduler().scheduleSyncDelayedTask(API, new Runnable()
			{
				@Override
				public void run()
				{
					API.data.removePlayerData(player, "temp_was_PVP");
					if(API.zone.zoneNoPVP(player.getLocation())) player.sendMessage(ChatColor.GRAY + "You are now safe from all PVP!");
				}
			}, (delayTime * 20));
		}

		// Let players know where they can PVP
		if(!API.data.hasPlayerData(player, "temp_was_PVP"))
		{
			if(API.zone.exitZoneNoPVP(to, from)) player.sendMessage(ChatColor.GRAY + "You can now PVP!");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		String name = event.getPlayer().getName();
		if(filterCheckGeneric)
		{
			String message = ChatColor.YELLOW + name + " has lost connection to the game for a generic reason.";
			event.setQuitMessage(message);
		}
		else if(filterCheckStream)
		{
			String message = ChatColor.YELLOW + name + " has lost connection to the game.";
			event.setQuitMessage(message);
		}
		else if(filterCheckOverflow)
		{
			String message = ChatColor.YELLOW + name + " has disconnected due to overload.";
			event.setQuitMessage(message);
		}
		else if(filterCheckQuitting)
		{
			if(API.zone.canTarget(event.getPlayer()) && API.battle.isInAnyActiveBattle(API.player.getCurrentChar(event.getPlayer())))
			{
				String message = ChatColor.YELLOW + name + " has PvP Logged."; // TODO
				event.setQuitMessage(message);
				return;
			}
			String message = ChatColor.YELLOW + name + " has left the game.";
			event.setQuitMessage(message);
		}
		else if(filterCheckTimeout)
		{
			String message = ChatColor.YELLOW + name + " has disconnected due to timeout.";
			event.setQuitMessage(message);
		}
	}
}

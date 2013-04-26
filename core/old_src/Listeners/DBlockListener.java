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
	
	 5. The Demigods Team is defined as Alex Bennett and Alexander Chauncey
	    of http://www.censoredsoftware.com/.
	
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

package com.censoredsoftware.Demigods.Listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.censoredsoftware.Demigods.Definitions.Database.DFlatFile;
import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.Events.Altar.AltarCreateEvent;
import com.censoredsoftware.Demigods.Events.Altar.AltarCreateEvent.AltarCreateCause;
import com.censoredsoftware.Demigods.Events.Altar.AltarRemoveEvent;
import com.censoredsoftware.Demigods.Events.Altar.AltarRemoveEvent.AltarRemoveCause;
import com.censoredsoftware.Demigods.Objects.Altar;
import com.censoredsoftware.Demigods.Objects.Character.PlayerCharacter;
import com.censoredsoftware.Demigods.Objects.Shrine;

public class DBlockListener implements Listener
{
	public static final Demigods API = Demigods.INSTANCE;

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(API.block.isProtected(location))
		{
			event.setCancelled(true);
			event.getPlayer().sendMessage(ChatColor.YELLOW + "That block is protected by the Deities!");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockIgnite(BlockIgniteEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(API.block.isProtected(location)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockDamage(BlockDamageEvent event)
	{
		Location location = event.getBlock().getLocation();
		if(API.block.isProtected(location)) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPistonExtend(BlockPistonExtendEvent event)
	{
		List<Block> blocks = event.getBlocks();

		for(Block block : blocks)
		{
			Location location = block.getLocation();

			if(API.block.isProtected(location))
			{
				event.setCancelled(true);
				break;
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockPistonRetract(BlockPistonRetractEvent event)
	{
		final Block block = event.getBlock().getRelative(event.getDirection(), 2);

		if(API.block.isProtected(block.getLocation()) && event.isSticky())
		{
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void divineBlockExplode(final EntityExplodeEvent event) // TODO: Clean up and make it generic to Protected Blocks
	{
		final ArrayList<Block> savedBlocks = new ArrayList<Block>();
		final ArrayList<Material> savedMaterials = new ArrayList<Material>();
		final ArrayList<Byte> savedBytes = new ArrayList<Byte>();

		List<Block> blocks = event.blockList();
		for(Block block : blocks)
		{
			if(block.getType() == Material.TNT) continue;
			if(API.block.isProtected(block.getLocation()))
			{
				savedBlocks.add(block);
				savedMaterials.add(block.getType());
				savedBytes.add(block.getData());
			}
		}

		API.getServer().getScheduler().scheduleSyncDelayedTask(API, new Runnable()
		{
			@Override
			public void run()
			{
				// Regenerate blocks
				int i = 0;
				for(Block block : savedBlocks)
				{
					block.setTypeIdAndData(savedMaterials.get(i).getId(), savedBytes.get(i), true);
					i++;
				}

				// Remove all drops from explosion zone
				for(Item drop : event.getLocation().getWorld().getEntitiesByClass(Item.class))
				{
					Location location = drop.getLocation();
					if(API.zone.zoneAltar(location) != null)
					{
						drop.remove();
						continue;
					}

					if(API.zone.zoneShrine(location) != null)
					{
						drop.remove();
					}
				}
			}
		}, 1);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void stopDestroyEnderCrystal(EntityDamageEvent event)
	{
		if(API.block.isProtected(event.getEntity().getLocation().subtract(0.5, 1.0, 0.5)))
		{
			event.setDamage(0);
			event.setCancelled(true);
		}
	}

	/*
	 * --------------------------------------------
	 * Handle Miscellaneous Divine Block Events
	 * --------------------------------------------
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void demigodsAdminWand(PlayerInteractEvent event)
	{
		if(event.getClickedBlock() == null) return;

		// Define variables
		Block clickedBlock = event.getClickedBlock();
		Location location = clickedBlock.getLocation();
		Player player = event.getPlayer();

		// Return if the player does not qualify for use of the admin wand
		if(!API.admin.useWand(player)) return;

		if(clickedBlock.getType().equals(Material.EMERALD_BLOCK))
		{
			AltarCreateEvent altarEvent = new AltarCreateEvent(location, AltarCreateCause.ADMIN_WAND);
			API.getServer().getPluginManager().callEvent(altarEvent);

			player.sendMessage(ChatColor.GRAY + "Generating new Altar...");
			new Altar(location);
			player.sendMessage(ChatColor.GREEN + "Altar created!");
		}

		if(API.block.isAltar(location))
		{
			if(API.data.hasTimedData(player, "temp_destroy_altar"))
			{
				AltarRemoveEvent altarRemoveEvent = new AltarRemoveEvent(location, AltarRemoveCause.ADMIN_WAND);
				API.getServer().getPluginManager().callEvent(altarRemoveEvent);
				if(altarRemoveEvent.isCancelled()) return;

				// We can destroy the Altar
				API.block.getAltar(location).remove();
				API.data.removeTimedData(player, "temp_destroy_altar");

				// Save Divine Blocks
				DFlatFile.saveBlocks();
				player.sendMessage(ChatColor.GREEN + "Altar removed!");
			}
			else
			{
				API.data.saveTimedData(player, "temp_destroy_altar", true, 5);
				player.sendMessage(ChatColor.RED + "Right-click this Altar again to remove it.");
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void divineBlockAlerts(PlayerMoveEvent event)
	{
		if(event.getFrom().distance(event.getTo()) < 0.1) return;

		// Define variables
		Player player = event.getPlayer();
		Location to = event.getTo();
		Location from = event.getFrom();
		Shrine shrine = null;
		PlayerCharacter character = null;

		/*
		 * ------------------------------------
		 * Altar Zone Messages
		 * -----------------------------------
		 * -> Entering Altar
		 */
		if(API.zone.enterZoneAltar(to, from) && !API.warp.hasWarp(API.zone.zoneAltar(to), API.player.getCurrentChar(player)))
		{
			player.sendMessage(ChatColor.GRAY + "You have entered an undocumented Altar.");
			player.sendMessage(ChatColor.GRAY + "You should set a warp at it!");
			return;
		}
	}
}
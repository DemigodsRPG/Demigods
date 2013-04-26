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

package com.censoredsoftware.Demigods.Theogony.Titans;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.Events.Ability.AbilityEvent.AbilityType;
import com.censoredsoftware.Demigods.Objects.Character.PlayerCharacter;
import com.censoredsoftware.Demigods.Theogony.Theogony;

public class Prometheus_deity implements Listener
{
	private static final Demigods API = Theogony.INSTANCE;

	// Create required universal deity variables
	private static final String DEITYNAME = "Prometheus";
	private static final String DEITYALLIANCE = "Titan";
	private static final ChatColor DEITYCOLOR = ChatColor.GOLD;

	/*
	 * Set deity-specific ability variable(s).
	 */
	// "/fireball" Command:
	private static final String FIREBALL_NAME = "Fireball"; // Sets the name of this command
	private static long FIREBALL_TIME; // Creates the variable for later use
	private static final int FIREBALL_COST = 100; // Cost to run command in "favor"
	private static final int FIREBALL_DELAY = 5; // In milliseconds

	// "/blaze" Command:
	private static final String BLAZE_NAME = "Blaze"; // Sets the name of this command
	private static long BLAZE_TIME; // Creates the variable for later use
	private static final int BLAZE_COST = 400; // Cost to run command in "favor"
	private static final int BLAZE_DELAY = 15; // In milliseconds

	// "/firestorm" Command:
	@SuppressWarnings("unused")
	private static String ULTIMATE_NAME = "Firestorm";
	private static long ULTIMATE_TIME; // Creates the variable for later use
	private static final int ULTIMATE_COST = 5500; // Cost to run command in "favor"
	private static final int ULTIMATE_COOLDOWN_MAX = 600; // In seconds
	private static final int ULTIMATE_COOLDOWN_MIN = 60; // In seconds

	public ArrayList<Material> getClaimItems()
	{
		ArrayList<Material> claimItems = new ArrayList<Material>();

		// Add new items in this format: claimItems.add(Material.NAME_OF_MATERIAL);
		// claimItems.add(Material.CLAY_BALL);
		// claimItems.add(Material.MAGMA_CREAM);
		claimItems.add(Material.DIRT);

		return claimItems;
	}

	public ArrayList<String> getInfo(Player player)
	{
		ArrayList<String> toReturn = new ArrayList<String>();

		if(API.misc.canUseDeitySilent(player, DEITYNAME))
		{
			toReturn.add(" "); // TODO
			toReturn.add(ChatColor.AQUA + " Demigods > " + ChatColor.RESET + DEITYCOLOR + DEITYNAME);
			toReturn.add(ChatColor.RESET + "-----------------------------------------------------");
			toReturn.add(ChatColor.YELLOW + " Active:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/fireball" + ChatColor.WHITE + " - Shoot a fireball at the cursor's location.");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/blaze" + ChatColor.WHITE + " - Ignite the ground at the target location.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Passive:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.WHITE + "None.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Ultimate:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/firestorm" + ChatColor.WHITE + " - Prometheus rains fire on nearby enemies.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " You are a follower of " + DEITYNAME + "!");
			toReturn.add(" ");

			return toReturn;
		}
		else
		{
			toReturn.add(" "); // TODO
			toReturn.add(ChatColor.AQUA + " Demigods > " + ChatColor.RESET + DEITYCOLOR + DEITYNAME);
			toReturn.add(ChatColor.RESET + "-----------------------------------------------------");
			toReturn.add(ChatColor.YELLOW + " Active:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/fireball" + ChatColor.WHITE + " - Shoot a fireball at the cursor's location.");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/blaze" + ChatColor.WHITE + " - Ignite the ground at the target location.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Passive:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.WHITE + "None.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Ultimate:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/firestorm" + ChatColor.WHITE + " - Prometheus rains fire on nearby enemies.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Claim Items:");
			for(Material item : getClaimItems())
			{
				toReturn.add(ChatColor.GRAY + " -> " + ChatColor.WHITE + item.name());
			}
			toReturn.add(" ");

			return toReturn;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onPlayerInteract(PlayerInteractEvent interactEvent)
	{
		// Set variables
		Player player = interactEvent.getPlayer();
		PlayerCharacter character = API.player.getCurrentChar(player);

		if(!API.ability.isClick(interactEvent)) return;

		if(!API.misc.canUseDeitySilent(player, DEITYNAME)) return;

		if(character.isEnabledAbility(FIREBALL_NAME) || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == character.getBind(FIREBALL_NAME))))
		{
			if(!API.character.isCooledDown(player, FIREBALL_NAME, FIREBALL_TIME, false)) return;

			fireball(player);
		}
		else if(character.isEnabledAbility(BLAZE_NAME) || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == character.getBind(BLAZE_NAME))))
		{
			if(!API.character.isCooledDown(player, BLAZE_NAME, BLAZE_TIME, false)) return;

			blaze(player);
		}
	}

	/*
	 * ------------------
	 * Command Handlers
	 * ------------------
	 * 
	 * Command: "/fireball"
	 */
	public static void fireballCommand(Player player, String[] args)
	{
		PlayerCharacter character = API.player.getCurrentChar(player);

		if(!API.misc.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "." + DEITYNAME)) return;

		if(!API.misc.canUseDeity(player, DEITYNAME)) return;

		if(args.length == 2 && args[1].equalsIgnoreCase("bind"))
		{
			// Bind item
			character.setBound(FIREBALL_NAME, player.getItemInHand().getType());
		}
		else
		{
			if(character.isEnabledAbility(FIREBALL_NAME))
			{
				character.toggleAbility(FIREBALL_NAME, false);
				player.sendMessage(ChatColor.YELLOW + FIREBALL_NAME + " is no longer active.");
			}
			else
			{
				character.toggleAbility(FIREBALL_NAME, true);
				player.sendMessage(ChatColor.YELLOW + FIREBALL_NAME + " is now active.");
			}
		}
	}

	// The actual ability command
	public static void fireball(Player player)
	{
		// Define variables
		PlayerCharacter character = API.player.getCurrentChar(player);
		LivingEntity target = API.ability.autoTarget(player);

		if(!API.ability.doAbilityPreProcess(player, target, "fireball", BLAZE_COST, AbilityType.OFFENSE)) return;
		FIREBALL_TIME = System.currentTimeMillis() + FIREBALL_DELAY;
		character.subtractFavor(FIREBALL_COST);

		if(!API.ability.targeting(player, target)) return;

		if(target.getEntityId() != player.getEntityId())
		{
			shootFireball(player.getEyeLocation(), target.getLocation(), player);
		}
	}

	public static void shootFireball(Location from, Location to, Player player)
	{
		player.getWorld().spawnEntity(from, EntityType.FIREBALL);
		for(Entity entity : player.getNearbyEntities(2, 2, 2))
		{
			if(!(entity instanceof Fireball)) continue;

			Fireball fireball = (Fireball) entity;
			to.setX(to.getX() + .5);
			to.setY(to.getY() + .5);
			to.setZ(to.getZ() + .5);
			Vector path = to.toVector().subtract(from.toVector());
			Vector victor = from.toVector().add(from.getDirection().multiply(2));
			fireball.teleport(new Location(player.getWorld(), victor.getX(), victor.getY(), victor.getZ()));
			fireball.setDirection(path);
			fireball.setShooter(player);
		}
	}

	/*
	 * Command: "/blaze"
	 */
	public static void blazeCommand(Player player, String[] args)
	{
		PlayerCharacter character = API.player.getCurrentChar(player);

		if(!API.misc.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "." + DEITYNAME)) return;

		if(!API.misc.canUseDeity(player, DEITYNAME)) return;

		if(args.length == 2 && args[1].equalsIgnoreCase("bind"))
		{
			// Bind item
			character.setBound(BLAZE_NAME, player.getItemInHand().getType());
		}
		else
		{
			if(character.isEnabledAbility(BLAZE_NAME))
			{
				character.toggleAbility(BLAZE_NAME, false);
				player.sendMessage(ChatColor.YELLOW + BLAZE_NAME + " is no longer active.");
			}
			else
			{
				character.toggleAbility(BLAZE_NAME, true);
				player.sendMessage(ChatColor.YELLOW + BLAZE_NAME + " is now active.");
			}
		}
	}

	// The actual ability command
	public static void blaze(Player player)
	{
		// Define variables
		PlayerCharacter character = API.player.getCurrentChar(player);
		int power = character.getPower(AbilityType.OFFENSE);
		int diameter = (int) Math.ceil(1.43 * Math.pow(power, 0.1527));
		if(diameter > 12) diameter = 12;

		LivingEntity target = API.ability.autoTarget(player);

		if(!API.ability.doAbilityPreProcess(player, target, "blaze", BLAZE_COST, AbilityType.OFFENSE)) return;
		BLAZE_TIME = System.currentTimeMillis() + BLAZE_DELAY;
		character.subtractFavor(BLAZE_COST);

		if(!API.ability.targeting(player, target)) return;

		if(target.getEntityId() != player.getEntityId())
		{
			for(int X = -diameter / 2; X <= diameter / 2; X++)
			{
				for(int Y = -diameter / 2; Y <= diameter / 2; Y++)
				{
					for(int Z = -diameter / 2; Z <= diameter / 2; Z++)
					{
						Block block = target.getWorld().getBlockAt(target.getLocation().getBlockX() + X, target.getLocation().getBlockY() + Y, target.getLocation().getBlockZ() + Z);
						if((block.getType() == Material.AIR) || (((block.getType() == Material.SNOW)) && !API.zone.zoneNoBuild(player, block.getLocation()))) block.setType(Material.FIRE);
					}
				}
			}
		}
	}

	/*
	 * Command: "/firestorm"
	 */
	public static void firestormCommand(Player player, String[] args)
	{
		if(!API.misc.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "." + DEITYNAME + ".ultimate")) return;

		// Define variables
		PlayerCharacter character = API.player.getCurrentChar(player);

		// Check the player for DEITYNAME
		if(!character.hasDeity(DEITYNAME)) return;

		// Check if the ultimate has cooled down or not
		if(System.currentTimeMillis() < ULTIMATE_TIME)
		{
			player.sendMessage(ChatColor.YELLOW + "You cannot use the " + DEITYNAME + " ultimate again for " + ChatColor.WHITE + ((((ULTIMATE_TIME) / 1000) - (System.currentTimeMillis() / 1000))) / 60 + " minutes");
			player.sendMessage(ChatColor.YELLOW + "and " + ChatColor.WHITE + ((((ULTIMATE_TIME) / 1000) - (System.currentTimeMillis() / 1000)) % 60) + " seconds.");
			return;
		}

		// Perform ultimate if there is enough favor
		if(!API.ability.doAbilityPreProcess(player, "firestorm", ULTIMATE_COST, AbilityType.OFFENSE)) return;

		firestorm(player);
		player.sendMessage(ChatColor.YELLOW + "Prometheus has reigned fire down on your enemies.");

		// Set favor and cooldown
		character.subtractFavor(ULTIMATE_COST);
		player.setNoDamageTicks(1000);
		int cooldownMultiplier = (int) (ULTIMATE_COOLDOWN_MAX - ((ULTIMATE_COOLDOWN_MAX - ULTIMATE_COOLDOWN_MIN) * ((double) character.getAscensions() / 100)));
		ULTIMATE_TIME = System.currentTimeMillis() + cooldownMultiplier * 1000;
	}

	// The actual ability command
	public static void firestorm(final Player player)
	{
		// Define variables
		PlayerCharacter character = API.player.getCurrentChar(player);
		int power = character.getPower(AbilityType.OFFENSE);
		int total = 20 * (int) Math.round(2 * Math.pow(power, 0.15));
		Vector playerLocation = player.getLocation().toVector();
		final ArrayList<LivingEntity> entityList = new ArrayList<LivingEntity>();
		for(Entity entity : player.getNearbyEntities(50, 50, 50))
		{
			if(!(entity instanceof LivingEntity)) continue;
			if(entity instanceof Player) if(API.player.getCurrentChar((Player) entity) != null) if(API.player.areAllied(player, (Player) entity)) continue;
			if(!API.zone.canTarget(entity)) continue;
			entityList.add((LivingEntity) entity);
		}
		for(int i = 0; i <= total; i += 20)
		{
			API.getServer().getScheduler().scheduleSyncDelayedTask(API, new Runnable()
			{
				@Override
				public void run()
				{
					for(LivingEntity entity : entityList)
					{
						Location up = new Location(entity.getWorld(), entity.getLocation().getX() + Math.random() * 5, 256, entity.getLocation().getZ() + Math.random() * 5);
						up.setPitch(90);
						shootFireball(up, new Location(entity.getWorld(), entity.getLocation().getX() + Math.random() * 5, entity.getLocation().getY(), entity.getLocation().getZ() + Math.random() * 5), player);
					}
				}
			}, i);
		}
	}

	// Don't touch these, they're required to work.
	public String loadDeity()
	{
		API.getServer().getPluginManager().registerEvents(this, API);
		ULTIMATE_TIME = System.currentTimeMillis();
		FIREBALL_TIME = System.currentTimeMillis();
		BLAZE_TIME = System.currentTimeMillis();
		return DEITYNAME + " loaded.";
	}

	public static ArrayList<String> getCommands()
	{
		ArrayList<String> COMMANDS = new ArrayList<String>();

		// List all commands
		COMMANDS.add("fireball");
		COMMANDS.add("blaze");
		COMMANDS.add("firestorm");

		return COMMANDS;
	}

	public static String getName()
	{
		return DEITYNAME;
	}

	public static String getAlliance()
	{
		return DEITYALLIANCE;
	}

	public static ChatColor getColor()
	{
		return DEITYCOLOR;
	}
}
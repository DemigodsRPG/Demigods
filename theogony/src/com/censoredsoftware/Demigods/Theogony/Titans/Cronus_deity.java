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

import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.Events.Ability.AbilityEvent.AbilityType;
import com.censoredsoftware.Demigods.Libraries.Objects.PlayerCharacter;
import com.censoredsoftware.Demigods.Theogony.Theogony;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Cronus_deity implements Listener
{
	private static final Demigods API = Theogony.INSTANCE;

	// Create required universal deity variables
	private static final String DEITYNAME = "Cronus";
	private static final String DEITYALLIANCE = "Titan";
	private static final ChatColor DEITYCOLOR = ChatColor.DARK_PURPLE;

	/*
	 * Set deity-specific ability variable(s).
	 */
	// "/cleave" Command:
	private static final String CLEAVE_NAME = "Cleave"; // Sets the name of this command
	private static long CLEAVE_TIME; // Creates the variable for later use
	private static final int CLEAVE_COST = 100; // Cost to run command in "favor"
	private static final int CLEAVE_DELAY = 1000; // In milliseconds

	// "/slow" Command:
	private static final String SLOW_NAME = "Slow"; // Sets the name of this command
	private static long SLOW_TIME; // Creates the variable for later use
	private static final int SLOW_COST = 180; // Cost to run command in "favor"
	private static final int SLOW_DELAY = 1000; // In milliseconds

	// "/timestop" Command:
	@SuppressWarnings("unused")
	private static String ULTIMATE_NAME = "Timestop";
	private static long ULTIMATE_TIME; // Creates the variable for later use
	private static final int ULTIMATE_COST = 3700; // Cost to run command in "favor"
	private static final int ULTIMATE_COOLDOWN_MAX = 600; // In seconds
	private static final int ULTIMATE_COOLDOWN_MIN = 60; // In seconds

	public ArrayList<Material> getClaimItems()
	{
		ArrayList<Material> claimItems = new ArrayList<Material>();

		// Add new items in this format: claimItems.add(Material.NAME_OF_MATERIAL);
		// claimItems.add(Material.SOUL_SAND);
		// claimItems.add(Material.WATCH);
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
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/cleave" + ChatColor.WHITE + " - Do damage to your target.");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/slow" + ChatColor.WHITE + " - Slow your target.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Passive:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.WHITE + "None.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Ultimate:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/timestop" + ChatColor.WHITE + " - Stop time for your enemies while you plan your next attack.");
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
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/cleave" + ChatColor.WHITE + " - Do damage to your target.");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/slow" + ChatColor.WHITE + " - Slow your target.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Passive:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.WHITE + "None.");
			toReturn.add(" ");
			toReturn.add(ChatColor.YELLOW + " Ultimate:");
			toReturn.add(ChatColor.GRAY + " -> " + ChatColor.GREEN + "/timestop" + ChatColor.WHITE + " - Stop time for our enemies while you plan your next attack.");
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

	// This sets the particular passive ability for the Cronus deity.
	@EventHandler(priority = EventPriority.MONITOR)
	public static void onEntityDamange(EntityDamageByEntityEvent damageEvent)
	{
		if(damageEvent.getDamager() instanceof Player)
		{
			Player player = (Player) damageEvent.getDamager();
			PlayerCharacter character = API.player.getCurrentChar(player);

			if(!API.misc.canUseDeitySilent(player, DEITYNAME)) return;

			if(!API.zone.canTarget(damageEvent.getEntity())) return;

			if(!player.getItemInHand().getType().name().contains("_HOE")) return;

			if(damageEvent.getEntity() instanceof Player)
			{
				Player attacked = (Player) damageEvent.getEntity();

				// Cronus Passive: Stop movement
				if(!API.player.areAllied(player, attacked)) attacked.setVelocity(new Vector(0, 0, 0));
			}

			if(character.isEnabledAbility(CLEAVE_NAME))
			{
				if(!API.character.isCooledDown(player, CLEAVE_NAME, CLEAVE_TIME, false)) return;

				cleave(damageEvent);
			}
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

		if(character.isEnabledAbility(SLOW_NAME) || ((player.getItemInHand() != null) && (player.getItemInHand().getType() == character.getBind(SLOW_NAME))))
		{
			if(!API.character.isCooledDown(player, SLOW_NAME, SLOW_TIME, false)) return;

			slow(player);
		}
	}

	/*
	 * ------------------
	 * Command Handlers
	 * ------------------
	 * 
	 * Command: "/cleave"
	 */
	public static void cleaveCommand(Player player, String[] args)
	{
		PlayerCharacter character = API.player.getCurrentChar(player);

		if(!API.misc.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "." + DEITYNAME)) return;

		if(!API.misc.canUseDeity(player, DEITYNAME)) return;

		if(character.isEnabledAbility(CLEAVE_NAME))
		{
			character.toggleAbility(CLEAVE_NAME, false);
			player.sendMessage(ChatColor.YELLOW + CLEAVE_NAME + " is no longer active.");
		}
		else
		{
			character.toggleAbility(CLEAVE_NAME, true);
			player.sendMessage(ChatColor.YELLOW + CLEAVE_NAME + " is now active.");
		}
	}

	// The actual ability command
	public static void cleave(EntityDamageByEntityEvent damageEvent)
	{
		// Define variables
		Player player = (Player) damageEvent.getDamager();
		Entity attacked = damageEvent.getEntity();
		PlayerCharacter character = API.player.getCurrentChar(player);

		if(!API.ability.doAbilityPreProcess(player, attacked, "cleave", CLEAVE_COST, AbilityType.OFFENSE)) return;
		CLEAVE_TIME = System.currentTimeMillis() + CLEAVE_DELAY;
		character.subtractFavor(CLEAVE_COST);

		for(int i = 1; i <= 31; i += 4)
			attacked.getWorld().playEffect(attacked.getLocation(), Effect.SMOKE, i);

		API.misc.customDamage(player, (LivingEntity) attacked, (int) Math.ceil(Math.pow(character.getPower(AbilityType.OFFENSE), 0.35)), DamageCause.ENTITY_ATTACK);

		if((LivingEntity) attacked instanceof Player)
		{
			Player attackedPlayer = (Player) attacked;

			attackedPlayer.setFoodLevel(attackedPlayer.getFoodLevel() - (damageEvent.getDamage() / 2));

			if(attackedPlayer.getFoodLevel() < 0) attackedPlayer.setFoodLevel(0);
		}
	}

	/*
	 * Command: "/slow"
	 */
	public static void slowCommand(Player player, String[] args)
	{
		PlayerCharacter character = API.player.getCurrentChar(player);

		if(!API.misc.hasPermissionOrOP(player, "demigods." + DEITYALLIANCE + "." + DEITYNAME)) return;

		if(!API.misc.canUseDeity(player, DEITYNAME)) return;

		if(args.length == 2 && args[1].equalsIgnoreCase("bind"))
		{
			// Bind item
			character.setBound(SLOW_NAME, player.getItemInHand().getType());
		}
		else
		{
			if(character.isEnabledAbility(SLOW_NAME))
			{
				character.toggleAbility(SLOW_NAME, false);
				player.sendMessage(ChatColor.YELLOW + SLOW_NAME + " is no longer active.");
			}
			else
			{
				character.toggleAbility(SLOW_NAME, true);
				player.sendMessage(ChatColor.YELLOW + SLOW_NAME + " is now active.");
			}
		}
	}

	// The actual ability command
	public static void slow(Player player)
	{
		// Define variables
		PlayerCharacter character = API.player.getCurrentChar(player);
		int power = character.getDevotion();
		int duration = (int) Math.ceil(3.635 * Math.pow(power, 0.2576)); // seconds
		int strength = (int) Math.ceil(1.757 * Math.pow(power, 0.097));
		Player target = null;
		if(API.ability.autoTarget(player) instanceof Player) target = (Player) API.ability.autoTarget(player);

		if(!API.ability.doAbilityPreProcess(player, target, "slow", SLOW_COST, AbilityType.SUPPORT)) return;
		SLOW_TIME = System.currentTimeMillis() + SLOW_DELAY;
		character.subtractFavor(SLOW_COST);

		if(!API.ability.targeting(player, target)) return;

		if(target.getEntityId() != player.getEntityId())
		{
			target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration * 20, strength));
			player.sendMessage(ChatColor.YELLOW + target.getName() + " has been slowed.");
			target.sendMessage(ChatColor.RED + "You have been slowed for " + duration + " seconds.");
		}
	}

	/*
	 * Command: "/timestop"
	 */
	public static void timestopCommand(Player player, String[] args)
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
		if(!API.ability.doAbilityPreProcess(player, ULTIMATE_COST)) return;

		int duration = (int) Math.round(9.9155621 * Math.pow(character.getAscensions(), 0.459019));
		player.sendMessage(ChatColor.YELLOW + "Cronus has stopped time for " + duration + " seconds, for " + timestop(player, duration) + " enemies!");

		// Set favor and cooldown
		character.subtractFavor(ULTIMATE_COST);
		player.setNoDamageTicks(1000);
		int cooldownMultiplier = (int) (ULTIMATE_COOLDOWN_MAX - ((ULTIMATE_COOLDOWN_MAX - ULTIMATE_COOLDOWN_MIN) * ((double) character.getAscensions() / 100)));
		ULTIMATE_TIME = System.currentTimeMillis() + cooldownMultiplier * 1000;
	}

	// The actual ability command
	public static int timestop(Player player, int duration)
	{
		// Define variables
		PlayerCharacter character = API.player.getCurrentChar(player);

		int slowamount = (int) Math.round(4.77179 * Math.pow(character.getAscensions(), 0.17654391));
		int count = 0;

		for(Player onlinePlayer : player.getWorld().getPlayers())
		{
			if(!(onlinePlayer.getLocation().toVector().isInSphere(player.getLocation().toVector(), 70))) continue;

			if(!API.zone.canTarget(onlinePlayer)) continue;

			if(API.player.areAllied(player, onlinePlayer)) continue;

			onlinePlayer.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration * 20, slowamount));

			count++;
		}

		return count;
	}

	// Don't touch these, they're required to work.
	public String loadDeity()
	{
		API.getServer().getPluginManager().registerEvents(this, API);
		ULTIMATE_TIME = System.currentTimeMillis();
		CLEAVE_TIME = System.currentTimeMillis();
		SLOW_TIME = System.currentTimeMillis();
		return DEITYNAME + " loaded.";
	}

	public static ArrayList<String> getCommands()
	{
		ArrayList<String> COMMANDS = new ArrayList<String>();

		// List all commands
		COMMANDS.add("cleave");
		COMMANDS.add("slow");
		COMMANDS.add("timestop");

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

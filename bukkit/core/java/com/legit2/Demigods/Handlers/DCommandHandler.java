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

package com.legit2.Demigods.Handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.legit2.Demigods.Demigods;
import com.legit2.Demigods.Events.Ability.AbilityEvent.AbilityType;
import com.legit2.Demigods.Handlers.Database.DFlatFile;
import com.legit2.Demigods.Libraries.Objects.PlayerCharacter;
import com.legit2.Demigods.Libraries.Objects.SerialItemStack;

public class DCommandHandler implements CommandExecutor
{
	private static final Demigods API = Demigods.INSTANCE;

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(command.getName().equalsIgnoreCase("dg")) return dg(sender, args);
		else if(command.getName().equalsIgnoreCase("check")) return check(sender, args);
		else if(command.getName().equalsIgnoreCase("owner")) return owner(sender, args);

		// TESTING ONLY
		else if(command.getName().equalsIgnoreCase("test1")) return test1(sender);
		else if(command.getName().equalsIgnoreCase("removechar")) return removeChar(sender, args);

		// Debugging
		else if(command.getName().equalsIgnoreCase("viewmaps")) return viewMaps(sender);
		else if(command.getName().equalsIgnoreCase("viewblocks")) return viewBlocks(sender);
		else if(command.getName().equalsIgnoreCase("viewtasks")) return viewTasks(sender);

		return false;
	}

	/*
	 * Command: "test1"
	 */
	public static boolean test1(CommandSender sender)
	{
		Player player = (Player) sender;

		ItemStack axe = new ItemStack(Material.DIAMOND_AXE, 1);

		ArrayList<String> lore = new ArrayList<String>();
		lore.add("The axe of the Gods!");
		ItemMeta axeMeta = axe.getItemMeta();
		axeMeta.setDisplayName("Penis Chopper");
		axeMeta.setLore(lore);
		axe.setItemMeta(axeMeta);

		SerialItemStack item = new SerialItemStack(axe);
		player.getWorld().dropItem(player.getLocation(), item.toItemStack());

		Firework firework = (Firework) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
		FireworkMeta fireworkmeta = firework.getFireworkMeta();
		Type type = Type.BALL_LARGE;
		FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.AQUA).withFade(Color.FUCHSIA).with(type).trail(true).build();
		fireworkmeta.addEffect(effect);
		fireworkmeta.setPower(2);
		firework.setFireworkMeta(fireworkmeta);
		return true;
	}

	/*
	 * Command: "viewblocks"
	 */
	public static boolean viewBlocks(CommandSender sender)
	{
		for(Entry<String, HashMap<Integer, Object>> block : API.data.getAllBlockData().entrySet())
		{
			String blockID = block.getKey();
			HashMap<Integer, Object> blockData = block.getValue();

			sender.sendMessage(blockID + ": ");

			for(Entry<Integer, Object> blockDataEntry : blockData.entrySet())
			{
				sender.sendMessage("  - " + blockDataEntry.getKey() + ": " + blockDataEntry.getValue());
			}
		}
		return true;
	}

	/*
	 * Command: "viewtasks"
	 */
	public static boolean viewTasks(CommandSender sender)
	{
		for(Entry<Integer, HashMap<String, Object>> task : API.data.getAllTasks().entrySet())
		{
			int taskID = task.getKey();
			HashMap<String, Object> taskData = task.getValue();

			sender.sendMessage(taskID + ": ");

			for(Entry<String, Object> blockDataEntry : taskData.entrySet())
			{
				sender.sendMessage("  - " + blockDataEntry.getKey() + ": " + blockDataEntry.getValue());
			}
		}
		return true;
	}

	/*
	 * Command: "dg"
	 */
	public static boolean dg(CommandSender sender, String[] args)
	{
		if(args.length > 0)
		{
			dg_extended(sender, args);
			return true;
		}

		// Define Player
		Player player = (Player) API.player.definePlayer(sender.getName());

		// Check Permissions
		if(!API.misc.hasPermissionOrOP(player, "demigods.basic")) return API.misc.noPermission(player);

		API.misc.taggedMessage(sender, "Documentation");
		for(String alliance : API.deity.getLoadedDeityAlliances())
			sender.sendMessage(ChatColor.GRAY + " /dg " + alliance.toLowerCase());
		sender.sendMessage(ChatColor.GRAY + " /dg info");
		sender.sendMessage(ChatColor.GRAY + " /dg commands");
		if(API.misc.hasPermissionOrOP(player, "demigods.admin")) sender.sendMessage(ChatColor.RED + " /dg admin");
		sender.sendMessage(" ");
		sender.sendMessage(ChatColor.WHITE + " Use " + ChatColor.YELLOW + "/check" + ChatColor.WHITE + " to see your player information.");
		return true;
	}

	/*
	 * Command: "dg_extended"
	 */
	@SuppressWarnings("unchecked")
	public static boolean dg_extended(CommandSender sender, String[] args)
	{
		// Define Player
		Player player = (Player) API.player.definePlayer(sender.getName());

		// Define args
		String category = args[0];
		String option1 = null, option2 = null, option3 = null, option4 = null;
		if(args.length >= 2) option1 = args[1];
		if(args.length >= 3) option2 = args[2];
		if(args.length >= 4) option3 = args[3];
		if(args.length >= 5) option4 = args[4];

		// Check Permissions
		if(!API.misc.hasPermissionOrOP(player, "demigods.basic")) return API.misc.noPermission(player);

		if(category.equalsIgnoreCase("admin"))
		{
			dg_admin(sender, option1, option2, option3, option4);
		}
		else if(category.equalsIgnoreCase("save"))
		{
			if(!API.misc.hasPermissionOrOP(player, "demigods.admin")) return API.misc.noPermission(player);

			API.misc.serverMsg(ChatColor.RED + "Manually forcing Demigods save...");
			if(DFlatFile.save()) API.misc.serverMsg(ChatColor.GREEN + "Save complete!");
			else
			{
				API.misc.serverMsg(ChatColor.RED + "There was a problem with saving...");
				API.misc.serverMsg(ChatColor.RED + "Check the log immediately.");
			}
		}
		else if(category.equalsIgnoreCase("commands"))
		{
			API.misc.taggedMessage(sender, "Command Directory");
			sender.sendMessage(ChatColor.GRAY + " There's nothing here...");
		}
		else if(category.equalsIgnoreCase("info"))
		{
			if(option1 == null)
			{
				API.misc.taggedMessage(sender, "Information Directory");
				sender.sendMessage(ChatColor.GRAY + " /dg info characters");
				sender.sendMessage(ChatColor.GRAY + " /dg info shrines");
				sender.sendMessage(ChatColor.GRAY + " /dg info tributes");
				sender.sendMessage(ChatColor.GRAY + " /dg info players");
				sender.sendMessage(ChatColor.GRAY + " /dg info pvp");
				sender.sendMessage(ChatColor.GRAY + " /dg info stats");
				sender.sendMessage(ChatColor.GRAY + " /dg info rankings");
				sender.sendMessage(ChatColor.GRAY + " /dg info demigods");
			}
			else if(option1.equalsIgnoreCase("demigods"))
			{
				API.misc.taggedMessage(sender, "About the Plugin");
				sender.sendMessage(ChatColor.WHITE + " Not to be confused with other RPG plugins that focus on skills and classes alone, " + ChatColor.GREEN + "Demigods" + ChatColor.WHITE + " adds culture and conflict that will keep players coming back even after they've maxed out their levels and found all of the diamonds in a 50km radius.");
				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.GREEN + " Demigods" + ChatColor.WHITE + " is unique in its system of rewarding players for both adventuring (tributes) and conquering (PvP) with a wide array of fun and usefull skills.");
				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.WHITE + " Re-enact mythological battles and rise from a Demigod to a full-fledged Olympian as you form new Alliances with mythical groups and battle to the bitter end.");
				sender.sendMessage(" ");
				sender.sendMessage(ChatColor.GRAY + " Developed by: " + ChatColor.GREEN + "_Alex" + ChatColor.GRAY + " and " + ChatColor.GREEN + "HmmmQuestionMark");
				sender.sendMessage(ChatColor.GRAY + " Website: " + ChatColor.GREEN + "https://github.com/Clashnia/Minecraft-Demigods");
			}
			else if(option1.equalsIgnoreCase("characters"))
			{
				API.misc.taggedMessage(sender, "Characters");
				sender.sendMessage(ChatColor.GRAY + " This is some info about Characters.");
			}
			else if(option1.equalsIgnoreCase("shrine"))
			{
				API.misc.taggedMessage(sender, "Shrines");
				sender.sendMessage(ChatColor.GRAY + " This is some info about Shrines.");
			}
			else if(option1.equalsIgnoreCase("tribute"))
			{
				API.misc.taggedMessage(sender, "Tributes");
				sender.sendMessage(ChatColor.GRAY + " This is some info about Tributes.");
			}
			else if(option1.equalsIgnoreCase("player"))
			{
				API.misc.taggedMessage(sender, "Players");
				sender.sendMessage(ChatColor.GRAY + " This is some info about Players.");
			}
			else if(option1.equalsIgnoreCase("pvp"))
			{
				API.misc.taggedMessage(sender, "PVP");
				sender.sendMessage(ChatColor.GRAY + " This is some info about PVP.");
			}
			else if(option1.equalsIgnoreCase("stats"))
			{
				API.misc.taggedMessage(sender, "Stats");
				sender.sendMessage(ChatColor.GRAY + " Read some server-wide stats for Demigods.");
			}
			else if(option1.equalsIgnoreCase("rankings"))
			{
				API.misc.taggedMessage(sender, "Rankings");
				sender.sendMessage(ChatColor.GRAY + " This is some ranking info about Demigods.");
			}
		}

		for(String alliance : API.deity.getLoadedDeityAlliances())
		{
			if(category.equalsIgnoreCase(alliance))
			{
				if(args.length < 2)
				{
					API.misc.taggedMessage(sender, alliance + " Directory");
					for(String deity : API.deity.getAllDeitiesInAlliance(alliance))
						sender.sendMessage(ChatColor.GRAY + " /dg " + alliance.toLowerCase() + " " + deity.toLowerCase());
				}
				else
				{
					for(String deity : API.deity.getAllDeitiesInAlliance(alliance))
					{
						assert option1 != null;
						if(option1.equalsIgnoreCase(deity))
						{
							try
							{
								for(String toPrint : (ArrayList<String>) API.deity.invokeDeityMethodWithPlayer(API.deity.getDeityClass(deity), API.deity.getClassLoader(deity), "getInfo", player))
									sender.sendMessage(toPrint);
								return true;
							}
							catch(Exception e)
							{
								sender.sendMessage(ChatColor.RED + "(ERR: 3001)  Please report this immediatly.");
								e.printStackTrace(); // DEBUG
								return true;
							}
						}
					}
					sender.sendMessage(ChatColor.DARK_RED + " No such deity, please try again.");
					return false;
				}
			}
		}

		return true;
	}

	// Admin Directory
	private static boolean dg_admin(CommandSender sender, String option1, String option2, String option3, String option4)
	{
		Player player = (Player) API.player.definePlayer(sender.getName());
		Player toEdit;
		PlayerCharacter character;
		int amount;

		if(!API.misc.hasPermissionOrOP(player, "demigods.admin")) return API.misc.noPermission(player);

		if(option1 == null)
		{
			API.misc.taggedMessage(sender, "Admin Directory");
			sender.sendMessage(ChatColor.GRAY + " /dg admin wand");
			sender.sendMessage(ChatColor.GRAY + " /dg admin debug");
			sender.sendMessage(ChatColor.GRAY + " /dg admin check <player> <character>");
			sender.sendMessage(ChatColor.GRAY + " /dg admin set [favor|devotion|ascensions] <player> <amount>");
			sender.sendMessage(ChatColor.GRAY + " /dg admin add [favor|devotion|ascensions] <player> <amount>");
			sender.sendMessage(ChatColor.GRAY + " /dg admin sub [favor|devotion|ascensions] <player> <amount>");
		}

		if(option1 != null)
		{
			if(option1.equalsIgnoreCase("wand"))
			{
				if(!API.admin.wandEnabled(player))
				{
					API.data.savePlayerData(player, "temp_admin_wand", true);
					player.sendMessage(ChatColor.RED + "Your admin wand has been enabled for " + Material.getMaterial(API.config.getSettingInt("admin_wand_tool")));
				}
				else if(API.admin.wandEnabled(player))
				{
					API.data.removePlayerData(player, "temp_admin_wand");
					player.sendMessage(ChatColor.RED + "You have disabled your admin wand.");
				}
				return true;
			}
			else if(option1.equalsIgnoreCase("debug"))
			{
				if(!API.data.hasPlayerData(player, "temp_admin_debug") || API.data.getPlayerData(player, "temp_admin_debug").equals(false))
				{
					API.data.savePlayerData(player, "temp_admin_debug", true);
					player.sendMessage(ChatColor.RED + "You have enabled debugging.");
				}
				else if(API.data.hasPlayerData(player, "temp_admin_debug") && API.data.getPlayerData(player, "temp_admin_debug").equals(true))
				{
					API.data.removePlayerData(player, "temp_admin_debug");
					player.sendMessage(ChatColor.RED + "You have disabled debugging.");
				}
			}
			else if(option1.equalsIgnoreCase("check"))
			{
				if(option2 == null)
				{
					sender.sendMessage(ChatColor.RED + "You need to specify a player.");
					sender.sendMessage("/dg admin check <player>");
					return true;
				}

				// Define variables
				Player toCheck = Bukkit.getPlayer(option2);

				if(option3 == null)
				{
					API.misc.taggedMessage(sender, ChatColor.RED + toCheck.getName() + " Player Check");
					sender.sendMessage(" Characters:");

					List<Integer> chars = API.player.getChars(toCheck);

					for(Integer checkingCharID : chars)
					{
						PlayerCharacter checkingChar = API.character.getChar(checkingCharID);
						player.sendMessage(ChatColor.GRAY + "   (#: " + checkingCharID + ") Name: " + checkingChar.getName() + " / Deity: " + checkingChar.getDeity());
					}
				}
				else
				{
					// TODO: Display specific character information when called for.
				}
			}
			else if(option1.equalsIgnoreCase("set"))
			{
				if(option2 == null || option3 == null)
				{
					sender.sendMessage(ChatColor.RED + "You need to specify a player and amount.");
					sender.sendMessage("/dg admin set [favor|devotion|ascensions] <player> <amount>");
					return true;
				}
				else
				{
					// Define variables
					toEdit = Bukkit.getPlayer(option3);
					character = API.player.getCurrentChar(toEdit);
					amount = API.object.toInteger(option4);
				}

				if(option2.equalsIgnoreCase("favor"))
				{
					// Set the favor
					character.setFavor(amount);

					sender.sendMessage(ChatColor.GREEN + "Favor set to " + amount + " for " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character's favor has been set to " + amount + ".");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("devotion"))
				{
					// Set the devotion
					character.setDevotion(amount);

					sender.sendMessage(ChatColor.GREEN + "Devotion set to " + amount + " for " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character's devotion has been set to " + amount + ".");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("ascensions"))
				{
					// Set the ascensions
					character.setAscensions(amount);

					sender.sendMessage(ChatColor.GREEN + "Ascensions set to " + amount + " for " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character's Ascensions have been set to " + amount + ".");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
			}
			else if(option1.equalsIgnoreCase("add"))
			{
				if(option2 == null)
				{
					sender.sendMessage(ChatColor.RED + "You need to be more specific.");
					sender.sendMessage("/dg admin add [favor|devotion|ascensions] <player> <amount>");
					return true;
				}
				else if(option3 == null)
				{
					sender.sendMessage(ChatColor.RED + "You must select a player and amount.");
					sender.sendMessage("/dg admin add [favor|devotion|ascensions] <player> <amount>");
					return true;
				}
				else
				{
					// Define variables
					toEdit = Bukkit.getPlayer(option3);
					character = API.player.getCurrentChar(toEdit);
					amount = API.object.toInteger(option4);
				}

				if(option2.equalsIgnoreCase("favor"))
				{
					// Set the favor
					character.setFavor(amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " favor added to " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character has been given " + amount + " favor.");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("devotion"))
				{
					// Set the devotion
					character.setDevotion(amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " devotion added to " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character has been given " + amount + " devotion.");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("ascensions"))
				{
					// Set the ascensions
					character.giveAscensions(amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " Ascension(s) added to " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.GREEN + "Your current character has been given " + amount + " Ascensions.");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
			}
			else if(option1.equalsIgnoreCase("sub"))
			{
				if(option2 == null)
				{
					sender.sendMessage(ChatColor.RED + "You need to be more specific.");
					sender.sendMessage("/dg admin sub [favor|devotion|ascensions] <player> <amount>");
					return true;
				}
				else if(option3 == null)
				{
					sender.sendMessage(ChatColor.RED + "You must select a player and amount.");
					sender.sendMessage("/dg admin sub [favor|devotion|ascensions] <player> <amount>");
					return true;
				}
				else
				{
					// Define variables
					toEdit = Bukkit.getPlayer(option3);
					character = API.player.getCurrentChar(toEdit);
					amount = API.object.toInteger(option4);
				}

				if(option2.equalsIgnoreCase("favor"))
				{
					// Set the favor
					character.subtractFavor(amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " favor removed from " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.RED + "Your current character has had " + amount + " favor removed.");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("devotion"))
				{
					// Set the devotion
					character.subtractDevotion(amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " devotion removed from " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.RED + "Your current character has had " + amount + " devotion removed.");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
				else if(option2.equalsIgnoreCase("ascensions"))
				{
					// Set the ascensions
					character.subtractAscensions(amount);

					sender.sendMessage(ChatColor.GREEN + "" + amount + " Ascension(s) removed from " + toEdit.getName() + "'s current character.");

					// Tell who was edited
					toEdit.sendMessage(ChatColor.RED + "Your current character has had " + amount + " Ascension(s) removed.");
					toEdit.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "This was performed by " + sender.getName() + ".");
					return true;
				}
			}
			else
			{
				sender.sendMessage(ChatColor.RED + "Invalid category selected.");
				sender.sendMessage("/dg admin [set|add|sub] [favor|devotion|ascensions] <player> <amount>");
				return true;
			}
		}

		return true;
	}

	/*
	 * Command: "check"
	 */
	public static boolean check(CommandSender sender, String[] args)
	{
		Player player = (Player) API.player.definePlayer(sender.getName());
		PlayerCharacter character = API.player.getCurrentChar(player);

		if(character == null || !character.isImmortal())
		{
			player.sendMessage(ChatColor.RED + "You cannot use that command, mortal.");
			return true;
		}

		// Define variables
		int kills = API.player.getKills(player);
		int deaths = API.player.getDeaths(player);
		int killstreak = character.getKillstreak();
		String charName = character.getName();
		String deity = character.getDeity();
		String alliance = character.getAlliance();
		int favor = character.getFavor();
		int maxFavor = character.getMaxFavor();
		int devotion = character.getDevotion();
		int ascensions = character.getAscensions();
		int devotionGoal = character.getDevotionGoal();
		int powerOffense = character.getPower(AbilityType.OFFENSE);
		int powerDefense = character.getPower(AbilityType.DEFENSE);
		int powerStealth = character.getPower(AbilityType.STEALTH);
		int powerSupport = character.getPower(AbilityType.SUPPORT);
		int powerPassive = character.getPower(AbilityType.PASSIVE);
		ChatColor deityColor = (ChatColor) API.data.getPluginData("temp_deity_colors", deity);
		ChatColor favorColor = character.getFavorColor();

		if(args.length == 1 && (args[0].equalsIgnoreCase("level") || args[0].equalsIgnoreCase("levels")))
		{
			// Send the user their info via chat
			API.misc.taggedMessage(sender, "Levels Check");

			sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Offense: " + ChatColor.GREEN + powerOffense);
			sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Defense: " + ChatColor.GREEN + powerDefense);
			sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Stealth: " + ChatColor.GREEN + powerStealth);
			sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Support: " + ChatColor.GREEN + powerSupport);
			sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Passive: " + ChatColor.GREEN + powerPassive);

			return true;
		}

		// Send the user their info via chat
		API.misc.taggedMessage(sender, "Player Check");

		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Character: " + ChatColor.AQUA + charName);
		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Deity: " + deityColor + deity + ChatColor.WHITE + " of the " + ChatColor.GOLD + API.object.capitalize(alliance) + "s");
		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Favor: " + favorColor + favor + ChatColor.GRAY + " (of " + ChatColor.GREEN + maxFavor + ChatColor.GRAY + ")");
		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Ascensions: " + ChatColor.GREEN + ascensions);
		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Devotion: " + ChatColor.GREEN + devotion + ChatColor.GRAY + " (" + ChatColor.YELLOW + (devotionGoal - devotion) + ChatColor.GRAY + " until next Ascension)");
		sender.sendMessage(ChatColor.GRAY + " -> " + ChatColor.RESET + "Kills: " + ChatColor.GREEN + kills + ChatColor.WHITE + " / Deaths: " + ChatColor.RED + deaths + ChatColor.WHITE + " / Killstreak: " + ChatColor.RED + killstreak);

		return true;
	}

	/*
	 * Command: "owner"
	 */
	public static boolean owner(CommandSender sender, String[] args)
	{
		Player player = (Player) API.player.definePlayer(sender.getName());

		if(args.length < 1)
		{
			player.sendMessage(ChatColor.RED + "You must select a character.");
			player.sendMessage(ChatColor.RED + "/owner <character>");
			return true;
		}

		PlayerCharacter charToCheck = API.character.getCharByName(args[0]);

		if(charToCheck.getName() == null)
		{
			player.sendMessage(ChatColor.RED + "That character doesn't exist.");
			return true;
		}
		else
		{
			player.sendMessage(API.deity.getDeityColor(charToCheck.getDeity()) + charToCheck.getName() + ChatColor.YELLOW + " belongs to " + charToCheck.getOwner().getName() + ".");
			return true;
		}
	}

	/*
	 * Command: "viewMaps"
	 */
	public static boolean viewMaps(CommandSender sender)
	{
		sender.sendMessage("-- Players ------------------");
		sender.sendMessage(" ");

		for(Entry<String, HashMap<String, Object>> player : API.data.getAllPlayers().entrySet())
		{

			String playerName = player.getKey();
			HashMap<String, Object> playerData = player.getValue();

			sender.sendMessage(playerName + ": ");

			for(Entry<String, Object> playerDataEntry : playerData.entrySet())
			{
				sender.sendMessage("  - " + playerDataEntry.getKey() + ": " + playerDataEntry.getValue());
			}
		}

		sender.sendMessage(" ");
		sender.sendMessage("-- Characters ---------------");
		sender.sendMessage(" ");

		for(Entry<Integer, HashMap<String, Object>> character : API.data.getAllPlayerChars((Player) sender).entrySet())
		{
			int charID = character.getKey();
			HashMap<String, Object> charData = character.getValue();

			sender.sendMessage(charID + ": ");

			for(Entry<String, Object> charDataEntry : charData.entrySet())
			{
				sender.sendMessage("  - " + charDataEntry.getKey() + ": " + charDataEntry.getValue());
			}
		}
		return true;
	}

	/*
	 * Command: "removeChar"
	 */
	public static boolean removeChar(CommandSender sender, String[] args)
	{
		if(args.length != 1) return false;

		// Define args
		Player player = (Player) API.player.definePlayer(sender.getName());
		String charName = args[0];

		if(API.player.hasCharName(player, charName))
		{
			PlayerCharacter character = API.character.getCharByName(charName);
			int charID = character.getID();
			API.data.removeChar(charID);

			sender.sendMessage(ChatColor.RED + "Character removed!");
		}
		else sender.sendMessage(ChatColor.RED + "There was an error while removing your character.");

		return true;
	}
}

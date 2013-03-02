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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.legit2.Demigods.Libraries.Objects.SerialLocation;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.legit2.Demigods.Demigods;
import com.legit2.Demigods.Events.Character.CharacterCreateEvent;
import com.legit2.Demigods.Events.Character.CharacterSwitchEvent;
import com.legit2.Demigods.Libraries.Objects.PlayerCharacter;

public class DAltarListener implements Listener
{
	public static final Demigods API = Demigods.INSTANCE;

	/*
	 * --------------------------------------------
	 * Handle Altar Interactions
	 * --------------------------------------------
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void altarInteract(PlayerInteractEvent event)
	{
		if(event.getClickedBlock() == null) return;

		// Define variables
		Player player = event.getPlayer();

		// First we check if the player is in an Altar and return if not
		if(API.block.isAltar(event.getClickedBlock().getLocation()))
		{
			// Player is in an altar, let's do this
			if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

			if(event.getClickedBlock().getType().equals(Material.ENCHANTMENT_TABLE) && !API.player.isPraying(player))
			{
				API.player.togglePraying(player, true);

				// First we clear chat
				clearChat(player);

				// Tell nearby players that the user is praying
				for(Entity entity : player.getNearbyEntities(16, 16, 16))
				{
					if(entity instanceof Player) ((Player) entity).sendMessage(ChatColor.AQUA + player.getName() + " has knelt at a nearby Altar.");
				}

				player.sendMessage(ChatColor.AQUA + " -- Prayer Menu --------------------------------------");
				altarMenu(player);

				event.setCancelled(true);
			}
			else if(event.getClickedBlock().getType().equals(Material.ENCHANTMENT_TABLE) && API.player.isPraying(player))
			{
				API.player.togglePraying(player, false);

				// Clear whatever is being worked on in this Pray session
				API.data.removePlayerData(player, "temp_createchar");

				player.sendMessage(ChatColor.AQUA + "You are no longer praying.");
				player.sendMessage(ChatColor.GRAY + "Your movement and chat have been re-enabled.");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void altarChatEvent(AsyncPlayerChatEvent event)
	{
		// Define variables
		Player player = event.getPlayer();
		Location location = player.getLocation();

		// First we check if the player is in an Altar and currently praying, if not we'll return
		if(API.zone.zoneAltar(location) != null && API.player.isPraying(player))
		{
			// Cancel their chat
			event.setCancelled(true);

			// Define variables
			String message = event.getMessage();

			// Return to main menu
			if(message.equalsIgnoreCase("menu") || message.equalsIgnoreCase("exit"))
			{
				// Remove now useless data
				API.data.removePlayerData(player, "temp_createchar");

				clearChat(player);

				player.sendMessage(ChatColor.YELLOW + " -> Main Menu ----------------------------------------");
				player.sendMessage(" ");

				altarMenu(player);
				return;
			}

			// Create Character
			if(message.equals("1") || message.contains("create") && message.contains("character"))
			{
				clearChat(player);

				player.sendMessage(ChatColor.YELLOW + " -> Creating Character --------------------------------");
				player.sendMessage(" ");

				chooseName(player);
				return;
			}

			/*
			 * Character creation sub-steps
			 */
			if(API.data.hasPlayerData(player, "temp_createchar"))
			{
				// Step 1 of character creation
				if(API.data.getPlayerData(player, "temp_createchar").equals("choose_name"))
				{
					confirmName(player, message);
					return;
				}

				// Step 2 of character creation
				if(API.data.getPlayerData(player, "temp_createchar").equals("confirm_name"))
				{
					if(message.equalsIgnoreCase("y") || message.contains("yes"))
					{
						chooseDeity(player);
						return;
					}
					else
					{
						chooseName(player);
						return;
					}
				}

				// Step 3 of character creation
				if(API.data.getPlayerData(player, "temp_createchar").equals("choose_deity"))
				{
					confirmDeity(player, message);
					return;
				}

				// Step 4 of character creation
				if(API.data.getPlayerData(player, "temp_createchar").equals("confirm_deity"))
				{
					if(message.equalsIgnoreCase("y") || message.contains("yes"))
					{
						deityConfirmed(player);
						return;
					}
					else
					{
						chooseDeity(player);
						return;
					}
				}

				// Step 5 of character creation
				if(API.data.getPlayerData(player, "temp_createchar").equals("confirm_all"))
				{
					if(message.equalsIgnoreCase("y") || message.contains("yes"))
					{
						Inventory ii = API.getServer().createInventory(player, 27, "Place Your Tributes Here");
						player.openInventory(ii);
					}
					else
					{
						player.sendMessage(ChatColor.AQUA + "  Once you have the items return here again.");
						return;
					}
				}
			}

			// Finish Create Character
			if(message.equalsIgnoreCase("a") || message.contains("confirm") && message.contains("character") && API.data.hasPlayerData(player, "temp_createchar_finalstep"))
			{
				clearChat(player);

				finalConfirmDeity(player);
			}

			// Remove Character
			else if(message.equals("2") || message.contains("remove") || message.contains("remove") && message.contains("character"))
			{
				clearChat(player);

				player.sendMessage(ChatColor.RED + " -> Removing Characters -------------------------------");
				player.sendMessage(" ");
				player.sendMessage(ChatColor.GRAY + "  Currently Unavailable. Use /removechar <name>");
				player.sendMessage(" ");
			}

			// View Characters
			else if(message.equals("3") || message.contains("view") && message.contains("characters"))
			{
				clearChat(player);

				player.sendMessage(ChatColor.YELLOW + " -> Viewing Characters --------------------------------");
				player.sendMessage(" ");

				viewChars(player);
			}
            // View Characters
            else if(message.equals("4") || message.contains("view") && message.contains("warps"))
            {
                if(API.player.getCurrentChar(player) == null) return;

                clearChat(player);

                player.sendMessage(ChatColor.YELLOW + " -> Viewing Warps --------------------------------");
                player.sendMessage(" ");

                viewWarps(player);
            }
			else if(message.contains("info"))
			{
				clearChat(player);

				// Define variables
				String charName = message.replace(" info", "").trim();
				PlayerCharacter character = API.character.getCharByName(charName);

				viewChar(player, character);
			}

			// Switch Character
			else if(message.contains("switch to"))
			{
				clearChat(player);

				// Define variables
				String charName = message.replace("switch to ", "").trim();

				switchChar(player, charName);
			}

            // Warp Character
            else if(message.contains("name warp"))
            {
                // Define variables
                String name = message.replace("name warp", "").trim();

                nameAltar(player, API.zone.zoneAltar(player.getLocation()).getID(),name);
            }

            // Warp Character
            else if(message.contains("warp to"))
            {
                // Define variables
                String warpName = message.replace("warp to ", "").trim();

                warpChar(player, warpName);
            }
		}
	}

	// Method for use within Altars
	private void altarMenu(Player player)
	{
		player.sendMessage(" ");
		player.sendMessage(ChatColor.GRAY + " While using an Altar you are unable to move or chat.");
		player.sendMessage(ChatColor.GRAY + " You can return to the main menu at anytime by typing \"menu\".");
		player.sendMessage(ChatColor.GRAY + " Right-click the Altar again to stop Praying.");
		player.sendMessage(" ");
		player.sendMessage(ChatColor.GRAY + " To begin, choose an option by entering it's number in the chat:");
		player.sendMessage(" ");
		if(API.data.hasPlayerData(player, "temp_createchar_finalstep") && API.data.getPlayerData(player, "temp_createchar_finalstep").equals(true))
		{
			player.sendMessage(ChatColor.GRAY + "   [A.] " + ChatColor.GREEN + "Confirm New Character");
		}
		else player.sendMessage(ChatColor.GRAY + "   [1.] " + ChatColor.GREEN + "Create New Character");
		player.sendMessage(ChatColor.GRAY + "   [2.] " + ChatColor.RED + "Remove Character");
		player.sendMessage(ChatColor.GRAY + "   [3.] " + ChatColor.YELLOW + "View Characters");
        if(API.player.getCurrentChar(player) != null) player.sendMessage(ChatColor.GRAY + "   [4.] " + ChatColor.BLUE + "View Warps");
		player.sendMessage(" ");
	}

	// View characters
	private void viewChars(Player player)
	{
		List<Integer> chars = API.player.getChars(player);
		if(chars.isEmpty())
		{
			player.sendMessage(ChatColor.GRAY + "  You have no characters. Why not go make one?");
			player.sendMessage(ChatColor.GRAY + "  Type" + ChatColor.YELLOW + " create character" + ChatColor.GRAY + " to do so.");
			player.sendMessage(" ");
			return;
		}

		player.sendMessage(ChatColor.LIGHT_PURPLE + "  Light purple " + ChatColor.GRAY + "represents your current character.");
		player.sendMessage(" ");

		for(Integer charID : chars)
		{
			PlayerCharacter character = API.character.getChar(charID);
			String color = "";
			String name = character.getName();
			String deity = character.getDeity();
			int favor = character.getFavor();
			int maxFavor = character.getMaxFavor();
			ChatColor favorColor = character.getFavorColor();
			int devotion = character.getDevotion();
			int ascensions = character.getAscensions();

			if(character.isActive()) color = ChatColor.LIGHT_PURPLE + "";

			player.sendMessage(ChatColor.GRAY + "  " + ChatColor.GRAY + color + name + ChatColor.GRAY + " [" + API.deity.getDeityColor(deity) + deity + ChatColor.GRAY + " / Favor: " + favorColor + favor + ChatColor.GRAY + " (of " + ChatColor.GREEN + maxFavor + ChatColor.GRAY + ") / Ascensions: " + ChatColor.GREEN + ascensions + ChatColor.GRAY + "]");
		}

		player.sendMessage(" ");

		player.sendMessage(ChatColor.GRAY + "  Type" + ChatColor.YELLOW + " <character name> info" + ChatColor.GRAY + " for detailed information. ");
		player.sendMessage(" ");
		player.sendMessage(ChatColor.GRAY + "  Type" + ChatColor.YELLOW + " switch to <character name> " + ChatColor.GRAY + "to change your current");
		player.sendMessage(ChatColor.GRAY + "  character.");
		player.sendMessage(" ");
	}

    // View characters
    private void viewWarps(Player player)
    {
        if(API.warp.getWarps(API.player.getCurrentChar(player)) == null || API.warp.getWarps(API.player.getCurrentChar(player)).isEmpty())
        {
            player.sendMessage(ChatColor.GRAY + "  You have no Altar warps. Why not go make one?");
            player.sendMessage(ChatColor.GRAY + "  Type" + ChatColor.YELLOW + " name warp <warp name>" + ChatColor.GRAY + " to name a warp here.");
            player.sendMessage(" ");
            return;
        }

        player.sendMessage(ChatColor.LIGHT_PURPLE + "  Light purple " + ChatColor.GRAY + "represents the closest warp.");
        player.sendMessage(" ");
        boolean hasWarp = false;

        for(SerialLocation warp : API.warp.getWarps(API.player.getCurrentChar(player)))
        {
            Location playerLocation = player.getLocation();
            String color = "";
            String name = warp.getName();
            int X = (int) warp.unserialize().getX();
            int Y = (int) warp.unserialize().getY();
            int Z = (int) warp.unserialize().getZ();
            String world = warp.unserialize().getWorld().getName().toUpperCase();

            if(API.zone.zoneAltar(warp.unserialize()) == API.zone.zoneAltar(player.getLocation()))
            {
                color = ChatColor.LIGHT_PURPLE + "";
                hasWarp = true;
            }

            player.sendMessage("  " + color + name + ChatColor.GRAY + " [" + "X: " + ChatColor.GREEN +  X + ChatColor.GRAY + " / Y: " + ChatColor.GREEN + Y + ChatColor.GRAY + " / Z: " + ChatColor.GREEN + Z + ChatColor.GRAY + " / World: " + ChatColor.GREEN + world + ChatColor.GRAY + "]");
        }

        player.sendMessage(" ");

        player.sendMessage(ChatColor.GRAY + "  Type" + ChatColor.YELLOW + " warp to <warp name> " + ChatColor.GRAY + "to warp.");
        if(!hasWarp) player.sendMessage(ChatColor.GRAY + "  Type" + ChatColor.YELLOW + " name warp <warp name>" + ChatColor.GRAY + " to name a warp here.");
        else  player.sendMessage(ChatColor.GRAY + "  Type" + ChatColor.YELLOW + " name warp <warp name>" + ChatColor.GRAY + " to rename this warp.");
        player.sendMessage(" ");
    }

	private void viewChar(Player player, PlayerCharacter character)
	{
		player.sendMessage(ChatColor.YELLOW + " -> Viewing Character ---------------------------------");
		player.sendMessage(" ");

		String currentCharMsg = ChatColor.RED + "" + ChatColor.ITALIC + "(Inactive) " + ChatColor.RESET;
		String name = character.getName();
		String deity = character.getDeity();
		ChatColor deityColor = API.deity.getDeityColor(deity);
		String alliance = character.getAlliance();
		int hp = character.getHealth();
		int maxHP = character.getMaxHealth();
		ChatColor hpColor = character.getHealthColor();
		int exp = Math.round(character.getExp());
		int favor = character.getFavor();
		int maxFavor = character.getMaxFavor();
		ChatColor favorColor = character.getFavorColor();
		int devotion = character.getDevotion();
		int devotionGoal = character.getDevotionGoal();
		int ascensions = character.getAscensions();

		if(character.isActive()) currentCharMsg = ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + "(Current) " + ChatColor.RESET;

		player.sendMessage("    " + currentCharMsg + ChatColor.YELLOW + name + ChatColor.GRAY + " > Allied to " + deityColor + deity + ChatColor.GRAY + " of the " + ChatColor.GOLD + alliance + "s");
		player.sendMessage(ChatColor.GRAY + "  --------------------------------------------------");
		player.sendMessage(ChatColor.GRAY + "    Health: " + ChatColor.WHITE + hpColor + hp + ChatColor.GRAY + " (of " + ChatColor.GREEN + maxHP + ChatColor.GRAY + ")");
		player.sendMessage(ChatColor.GRAY + "    Experience: " + ChatColor.WHITE + exp);
		player.sendMessage(" ");
		player.sendMessage(ChatColor.GRAY + "    Ascensions: " + ChatColor.GREEN + ascensions);
		player.sendMessage(ChatColor.GRAY + "    Devotion: " + ChatColor.WHITE + devotion + ChatColor.GRAY + " (" + ChatColor.YELLOW + (devotionGoal - devotion) + ChatColor.GRAY + " until next Ascension)");
		player.sendMessage(ChatColor.GRAY + "    Favor: " + favorColor + favor + ChatColor.GRAY + " (of " + ChatColor.GREEN + maxFavor + ChatColor.GRAY + ")");
		player.sendMessage(" ");

	}

	private void switchChar(Player player, String charName)
	{
		PlayerCharacter newChar = API.character.getCharByName(charName);

		if(newChar != null)
		{
			CharacterSwitchEvent event = new CharacterSwitchEvent(player, API.player.getCurrentChar(player), newChar);
			API.misc.callEvent(event);

			if(!event.isCancelled())
			{
				API.player.changeCurrentChar(player, newChar.getID());

				player.setDisplayName(API.deity.getDeityColor(newChar.getDeity()) + newChar.getName() + ChatColor.WHITE);
				player.setPlayerListName(API.deity.getDeityColor(newChar.getDeity()) + newChar.getName() + ChatColor.WHITE);

				// Save their previous character and chat number for later monitoring
				API.data.savePlayerData(player, "previous_char", event.getCharacterFrom().getID());
				API.data.savePlayerData(player, "temp_chat_number", 0);

				// Disable prayer
				API.player.togglePraying(player, false);
				player.sendMessage(ChatColor.AQUA + "You are no longer praying.");
				player.sendMessage(ChatColor.GRAY + "Your movement and chat have been re-enabled.");
			}
		}
		else
		{
			player.sendMessage(ChatColor.RED + "Your current character couldn't be changed...");
			player.sendMessage(ChatColor.RED + "Please let an admin know.");
			API.player.togglePraying(player, false);
		}
	}

	// Choose name
	private void chooseName(Player player)
	{
		API.data.savePlayerData(player, "temp_createchar", "choose_name");
		player.sendMessage(ChatColor.AQUA + "  Enter a name: " + ChatColor.GRAY + "(Alpha-Numeric Only)");
		player.sendMessage(" ");
	}

	// Name confirmation
	private void confirmName(Player player, String message)
	{
		if(message.length() >= 15 || !StringUtils.isAlphanumeric(message) || API.player.hasCharName(player, message))
		{
			// Validate the name
			API.data.savePlayerData(player, "temp_createchar", "choose_name");
			if(message.length() >= 15) player.sendMessage(ChatColor.RED + "  That name is too long.");
			if(!StringUtils.isAlphanumeric(message)) player.sendMessage(ChatColor.RED + "  You can only use Alpha-Numeric characters.");
			if(API.player.hasCharName(player, message)) player.sendMessage(ChatColor.RED + "  You already have a character with that name.");
			player.sendMessage(ChatColor.AQUA + "  Enter a different name: " + ChatColor.GRAY + "(Alpha-Numeric Only)");
			player.sendMessage(" ");
		}
		else
		{
			API.data.savePlayerData(player, "temp_createchar", "confirm_name");
			String chosenName = message.replace(" ", "");
			player.sendMessage(ChatColor.AQUA + "  Are you sure you want to use " + ChatColor.YELLOW + chosenName + ChatColor.AQUA + "?" + ChatColor.GRAY + " (y/n)");
			player.sendMessage(" ");
			API.data.savePlayerData(player, "temp_createchar_name", chosenName);
		}
	}

	// Choose deity
	private void chooseDeity(Player player)
	{
		player.sendMessage(ChatColor.AQUA + "  Please choose a Deity: " + ChatColor.GRAY + "(Type in the name of the Deity)");
		for(String alliance : API.deity.getLoadedDeityAlliances())
		{
			for(String deity : API.deity.getAllDeitiesInAlliance(alliance))
				player.sendMessage(ChatColor.GRAY + "  -> " + ChatColor.YELLOW + API.object.capitalize(deity) + ChatColor.GRAY + " (" + alliance + ")");
		}
		player.sendMessage(" ");

		API.data.savePlayerData(player, "temp_createchar", "choose_deity");
	}

	// Deity confirmation
	private void confirmDeity(Player player, String message)
	{
		// Check their chosen Deity
		for(String alliance : API.deity.getLoadedDeityAlliances())
		{
			for(String deity : API.deity.getAllDeitiesInAlliance(alliance))
			{
				if(message.equalsIgnoreCase(deity))
				{
					// Their chosen deity matches an existing deity, ask for confirmation
					String chosenDeity = message.replace(" ", "");
					player.sendMessage(ChatColor.AQUA + "  Are you sure you want to use " + ChatColor.YELLOW + API.object.capitalize(chosenDeity) + ChatColor.AQUA + "?" + ChatColor.GRAY + " (y/n)");
					player.sendMessage(" ");
					API.data.savePlayerData(player, "temp_createchar_deity", chosenDeity);
					API.data.savePlayerData(player, "temp_createchar", "confirm_deity");
					return;
				}
			}
		}
		if(message.equalsIgnoreCase("_Alex"))
		{
			player.sendMessage(ChatColor.AQUA + "  Well you can't be _Alex... but he is awesome!");
			player.sendMessage(" ");

			// They can't be _Alex silly! Make them re-choose
			chooseDeity(player);
		}
	}

	// Confirmed deity
	@SuppressWarnings("unchecked")
	private void deityConfirmed(Player player)
	{
		// Define variables
		String chosenDeity = (String) API.data.getPlayerData(player, "temp_createchar_deity");

		// They accepted the Deity choice, now ask them to input their items so they can be accepted
		player.sendMessage(ChatColor.AQUA + "  Before you can confirm your lineage with " + ChatColor.YELLOW + chosenDeity + ChatColor.AQUA + ", you must");
		player.sendMessage(ChatColor.AQUA + "  first sacrifice the following items:");
		player.sendMessage(" ");
		for(Material item : (ArrayList<Material>) API.data.getPluginData("temp_deity_claim_items", chosenDeity))
		{
			player.sendMessage(ChatColor.GRAY + "  -> " + ChatColor.YELLOW + item.name());
		}
		player.sendMessage(" ");
		player.sendMessage(ChatColor.GRAY + "  After you obtain these items, return to an Altar and select");
		player.sendMessage(ChatColor.GRAY + "  the option to confirm your new character.");
		player.sendMessage(" ");

		API.data.savePlayerData(player, "temp_createchar_finalstep", true);
	}

	// Final confirmation of deity
	@SuppressWarnings("unchecked")
	private void finalConfirmDeity(Player player)
	{
		// Define variables
		String chosenDeity = (String) API.data.getPlayerData(player, "temp_createchar_deity");

		// Save data
		API.data.savePlayerData(player, "temp_createchar_finalstep", true);
		API.data.savePlayerData(player, "temp_createchar", "confirm_all");

		// Send them the chat
		player.sendMessage(ChatColor.GREEN + " -> Confirming Character -------------------------------");
		player.sendMessage(" ");
		player.sendMessage(ChatColor.AQUA + "  Do you have the following items in your inventory?" + ChatColor.GRAY + " (y/n)");
		player.sendMessage(" ");
		for(Material item : (ArrayList<Material>) API.data.getPluginData("temp_deity_claim_items", chosenDeity))
		{
			player.sendMessage(ChatColor.GRAY + "  -> " + ChatColor.YELLOW + item.name());
		}
		player.sendMessage(" ");
	}

	@SuppressWarnings("unchecked")
	@EventHandler(priority = EventPriority.MONITOR)
	public void createCharacter(InventoryCloseEvent event)
	{
		try
		{
			if(!(event.getPlayer() instanceof Player)) return;
			Player player = (Player) event.getPlayer();

			// If it isn't a confirmation chest then exit
			if(!event.getInventory().getName().contains("Place Your Tributes Here")) return;

			// Exit if this isn't for character creation
			if(!API.player.isPraying(player) || !API.data.hasPlayerData(player, "temp_createchar_finalstep") || API.data.getPlayerData(player, "temp_createchar_finalstep").equals(false))
			{
				player.sendMessage(ChatColor.RED + "(ERR: 2003) Please report this to an admin immediately.");
				return;
			}

			// Define variables
			String chosenName = (String) API.data.getPlayerData(player, "temp_createchar_name");
			String chosenDeity = (String) API.data.getPlayerData(player, "temp_createchar_deity");
			String deityAlliance = API.object.capitalize(API.deity.getDeityAlliance(chosenDeity));

			// Check the chest items
			int items = 0;
			int neededItems = ((ArrayList<Material>) API.data.getPluginData("temp_deity_claim_items", chosenDeity)).size();

			for(ItemStack ii : event.getInventory().getContents())
			{
				if(ii != null)
				{
					for(Material item : (ArrayList<Material>) API.data.getPluginData("temp_deity_claim_items", chosenDeity))
					{
						if(ii.getType().equals(item))
						{
							items++;
						}
					}
				}
			}

			player.sendMessage(ChatColor.YELLOW + "The " + deityAlliance + "s are pondering your offerings...");
			if(neededItems == items)
			{
				// They were accepted, finish everything up!
				CharacterCreateEvent characterEvent = new CharacterCreateEvent(player, chosenName, chosenDeity);
				API.getServer().getPluginManager().callEvent(characterEvent);

				// Stop their praying, enable movement, enable chat
				API.player.togglePraying(player, false);

				// Remove old data now
				API.data.removePlayerData(player, "temp_createchar_finalstep");
				API.data.removePlayerData(player, "temp_createchar_name");
				API.data.removePlayerData(player, "temp_createchar_deity");
			}
			else
			{
				player.sendMessage(ChatColor.RED + "You have been denied entry into the lineage of " + chosenDeity + "!");
			}

			// Clear the confirmation case
			event.getInventory().clear();
		}
		catch(Exception e)
		{
			// Print error for debugging
			e.printStackTrace();
		}
	}

	/*
	 * --------------------------------------------
	 * Miscellaneous Methods
	 * --------------------------------------------
	 */
	private void clearChat(Player player)
	{
		for(int x = 0; x < 120; x++)
			player.sendMessage(" ");
	}

    private void nameAltar(Player player, int blockID, String name)
    {
        if(API.warp.getWarps(API.player.getCurrentChar(player)) == null || API.warp.getWarps(API.player.getCurrentChar(player)).isEmpty())
        {
            // Save named SerialLocation for warp.
            API.data.saveWarpData(API.player.getCurrentChar(player), new SerialLocation(player.getLocation(), name));
            player.sendMessage(ChatColor.GRAY + "Your warp to this altar was named: " + ChatColor.YELLOW + name.toUpperCase() + ChatColor.GRAY + ".");
            return;
        }

        // Check for same names
        for(SerialLocation warp : API.warp.getWarps(API.player.getCurrentChar(player)))
        {
            if(warp.getName() == name.toUpperCase())
            {
                player.sendMessage(ChatColor.GRAY + "A warp by that name already exists.");
                return;
            }
        }

        // Check for same altars
        for(SerialLocation warp : API.warp.getWarps(API.player.getCurrentChar(player)))
        {
            if(API.zone.zoneAltar(warp.unserialize()) == API.zone.zoneAltar(player.getLocation()))
            {
               API.data.removeWarpData(API.player.getCurrentChar(player), warp);
            }
        }

        // Save named SerialLocation for warp.
        API.data.saveWarpData(API.player.getCurrentChar(player), new SerialLocation(player.getLocation(), name));
        player.sendMessage(ChatColor.GRAY + "Your warp to this Altar was named: " + ChatColor.YELLOW + name.toUpperCase() + ChatColor.GRAY + ".");
    }

    private void warpChar(Player player, String warpName)
    {
        for(SerialLocation warp : API.warp.getWarps(API.player.getCurrentChar(player)))
        {
            if(warp.getName().equals(warpName.toUpperCase()))
            {
                API.player.togglePraying(player, false);
                clearChat(player);

                player.teleport(warp.unserialize());

                player.sendMessage(ChatColor.GRAY + "Warp to " + ChatColor.YELLOW + warpName.toUpperCase() + ChatColor.GRAY + " complete.");
                return;
            }
        }
        player.sendMessage(ChatColor.GRAY + "No warp by that name exists, try again.");
    }
}

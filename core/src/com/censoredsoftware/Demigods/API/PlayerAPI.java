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

package com.censoredsoftware.Demigods.API;

import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.Libraries.Objects.PlayerCharacter;
import com.censoredsoftware.Demigods.Libraries.Objects.SerialPlayerInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class PlayerAPI
{
	private static final Demigods API = Demigods.INSTANCE;

	/*
	 * createPlayer() : Adds (Player)player to the database.
	 */
	public boolean createNewPlayer(Player player)
	{
		API.misc.info("Saving new player: " + player.getName());

		// Define variables
		int playerID = API.object.generateInt(5);

		// Create their HashMap data
		API.data.addPlayer(player, playerID);
		API.data.savePlayerData(player, "player_id", playerID);
		API.data.savePlayerData(player, "player_kills", 0);
		API.data.savePlayerData(player, "player_deaths", 0);
		API.data.savePlayerData(player, "player_characters", null);
		API.data.savePlayerData(player, "current_char", null);
		API.data.savePlayerData(player, "previous_char", null);

		return true;
	}

	/*
	 * getPlayerID() : Returns the ID of (Player)player.
	 */
	public int getPlayerID(OfflinePlayer player)
	{
		return API.object.toInteger(API.data.getPlayerData(player, "player_id"));
	}

	/*
	 * getPlayerFromID() : Returns the (Player)player for (int)player_id.
	 */
	public OfflinePlayer getPlayerFromID(int playerID)
	{
		for(Entry<String, HashMap<String, Object>> player : API.data.getAllPlayers().entrySet())
		{
			if(player.getValue().get("player_id").equals(playerID)) return Bukkit.getOfflinePlayer(player.getKey());
		}
		return null;
	}

	/*
	 * definePlayer() : Defines a Player from sender.
	 */
	public OfflinePlayer definePlayer(String name)
	{
		return Bukkit.getOfflinePlayer(name);
	}

	/*
	 * getCurrentChar() : Returns the current charID for (Player)player.
	 */
	public PlayerCharacter getCurrentChar(OfflinePlayer player)
	{
		try
		{
			return API.character.getChar(API.object.toInteger(API.data.getPlayerData(player, "current_char")));
		}
		catch(Exception e)
		{
			return null;
		}
	}

	/*
	 * getCurrentAlliance() : Returns the current alliance for (Player)player.
	 */
	public String getCurrentAlliance(OfflinePlayer player)
	{
		PlayerCharacter character = getCurrentChar(player);
		if(character == null || !character.isImmortal()) return "Mortal";
		return character.getAlliance();
	}

	/*
	 * areAllied() : Returns true if (Player)player1 is allied with (Player)player2.
	 */
	public boolean areAllied(Player player1, Player player2)
	{
		String player1Alliance = getCurrentAlliance(player1);
		String player2Alliance = getCurrentAlliance(player2);

		return player1Alliance.equalsIgnoreCase(player2Alliance);
	}

	/*
	 * getChars() : Returns an ArrayList of (Player)player's characters.
	 */
	public List<Integer> getChars(OfflinePlayer player)
	{
		List<Integer> charArray = new ArrayList<Integer>();
		HashMap<Integer, HashMap<String, Object>> characters = API.data.getAllPlayerChars(player);

		if(characters == null) return charArray;
		for(Entry<Integer, HashMap<String, Object>> character : characters.entrySet())
		{
			int charID = character.getKey();
			charArray.add(charID);
		}
		return charArray;
	}

	/*
	 * changeCurrentChar() : Changes the (Player)player's current character to the one passed in.
	 */
	public boolean changeCurrentChar(OfflinePlayer offlinePlayer, int charID)
	{
		// Define variables
		Player player = offlinePlayer.getPlayer();

		if(!getChars(offlinePlayer).contains(charID))
		{
			player.sendMessage(ChatColor.RED + "You can't do that.");
			return false;
		}

		// Disable prayer just to be safe
		togglePraying(player, false);

		// Update the current character (if it exists)
		PlayerCharacter currentChar = getCurrentChar(player);
		if(currentChar != null)
		{
			// Save info
			currentChar.setHealth(player.getHealth());
			currentChar.setFoodLevel(player.getFoodLevel());
			currentChar.setLevel(player.getLevel());
			currentChar.setExp(player.getExp());
			currentChar.setLocation(player.getLocation());
			currentChar.saveInventory();

			// Set them to inactive
			currentChar.toggleActive(false);
		}

		// Everything is good, let's switch
		API.data.savePlayerData(player, "current_char", charID);
		PlayerCharacter character = API.character.getChar(charID);
		character.toggleActive(true);

		// If it's their first character save their inventory
		if(API.player.getChars(player).size() <= 1) character.saveInventory();

		// Update inventory
		player.getInventory().clear();
		player.getInventory().setBoots(new ItemStack(Material.AIR));
		player.getInventory().setChestplate(new ItemStack(Material.AIR));
		player.getInventory().setLeggings(new ItemStack(Material.AIR));
		player.getInventory().setBoots(new ItemStack(Material.AIR));

		if(character.getInventory() != null)
		{
			SerialPlayerInventory charInv = character.getInventory();
			charInv.setToPlayer(player);
		}

		// Update health and experience
		player.setHealth(character.getHealth());
		player.setFoodLevel(character.getFoodLevel());
		player.setExp(character.getExp());

		// Teleport them
		player.teleport(character.getLocation());

		// Enable movement and chat to be safe
		togglePlayerChat(player, true);
		togglePlayerMovement(player, true);

		return true;
	}

	/*
	 * isImmortal() : Returns the current alliance for (Player)player.
	 */
	public boolean isImmortal(OfflinePlayer player)
	{
		PlayerCharacter character = getCurrentChar(player);
		return !(character == null || !character.isImmortal());
	}

	/*
	 * hasCharID() : Checks to see if (OfflinePlayer)player has (int)charID.
	 */
	public boolean hasCharID(OfflinePlayer player, int charID)
	{
		return getChars(player) != null && getChars(player).contains(charID);
	}

	/*
	 * hasCharName() : Checks to see if (OfflinePlayer)player has (String)charName.
	 */
	public boolean hasCharName(OfflinePlayer player, String charName)
	{
		List<Integer> characters = getChars(player);

		for(int charID : characters)
		{
			PlayerCharacter character = API.character.getChar(charID);
			if(character == null || !character.isImmortal()) continue;
			if(character.getName().equalsIgnoreCase(charName)) return true;
		}
		return false;
	}

	/*
	 * togglePraying() : Toggles prayer status for player.
	 */
	public void togglePraying(OfflinePlayer player, boolean option)
	{
		if(option)
		{
			togglePlayerChat(player, false);
			togglePlayerMovement(player, false);
			API.data.savePlayerData(player, "temp_praying", option);
		}
		else
		{
			togglePlayerChat(player, true);
			togglePlayerMovement(player, true);
			API.data.removePlayerData(player, "temp_praying");
		}
	}

	/*
	 * isPraying() : Returns a boolean for if the player is currently praying.
	 */
	public boolean isPraying(OfflinePlayer player)
	{
		return API.data.getPlayerData(player, "temp_praying") != null && API.object.toBoolean(API.data.getPlayerData(player, "temp_praying"));
	}

	/*
	 * regenerateAllFavor() : Regenerates favor for every player based on their stats.
	 */
	public void regenerateAllFavor()
	{
		ArrayList<Player> onlinePlayers = getOnlinePlayers();

		for(Player player : onlinePlayers)
		{
			PlayerCharacter character = getCurrentChar(player);
			if(character == null || !character.isImmortal()) continue;
			int regenRate = (int) Math.ceil(API.config.getSettingDouble("global_favor_multiplier") * character.getAscensions());
			if(regenRate < 1) regenRate = 1;
			character.giveFavor(regenRate);
		}
	}

	/*
	 * getNumberOfSouls() : Returns the number of souls (Player)player has in their inventory.
	 * 
	 * public int getNumberOfSouls(OfflinePlayer player)
	 * {
	 * // Define inventory contents & other variables
	 * ItemStack[] inventory = player.getInventory().getContents();
	 * ArrayList<ItemStack> allSouls = DSouls.returnAllSouls();
	 * int numberOfSouls = 0;
	 * 
	 * for(ItemStack soul : allSouls)
	 * {
	 * for(ItemStack inventoryItem : inventory)
	 * {
	 * if(inventoryItem != null && inventoryItem.isSimilar(soul))
	 * {
	 * // Find amount of souls and subtract 1 upon use
	 * int amount = inventoryItem.getAmount();
	 * 
	 * numberOfSouls = numberOfSouls + amount;
	 * }
	 * }
	 * }
	 * return numberOfSouls;
	 * }
	 * 
	 * 
	 * /*
	 * useSoul() : Uses first soul found in (Player)player's inventory.
	 * 
	 * public ItemStack useSoul(OfflinePlayer player)
	 * {
	 * if(getNumberOfSouls(player) == 0) return null;
	 * // Define inventory contents
	 * ItemStack[] inventory = player.getInventory().getContents();
	 * ArrayList<ItemStack> allSouls = DSouls.returnAllSouls();
	 * 
	 * for(ItemStack soul : allSouls)
	 * {
	 * for(ItemStack inventoryItem : inventory)
	 * {
	 * if(inventoryItem != null && inventoryItem.isSimilar(soul))
	 * {
	 * // Find amount of souls and subtract 1 upon use
	 * int amount = inventoryItem.getAmount();
	 * player.getInventory().removeItem(inventoryItem);
	 * inventoryItem.setAmount(amount - 1);
	 * player.getInventory().addItem(inventoryItem);
	 * 
	 * return inventoryItem;
	 * }
	 * }
	 * }
	 * return null;
	 * }
	 */

	/*
	 * getKills() : Returns (int)kills for (Player)player.
	 */
	public int getKills(OfflinePlayer player)
	{
		if(API.data.getPlayerData(player, "player_kills") != null) return Integer.parseInt(API.data.getPlayerData(player, "player_kills").toString());
		return -1;
	}

	/*
	 * setKills() : Sets the (Player)player's kills to (int)amount.
	 */
	public void setKills(OfflinePlayer player, int amount)
	{
		API.data.savePlayerData(player, "player_kills", amount);
	}

	/*
	 * addKill() : Gives (Player)player 1 kill.
	 */
	public void addKill(OfflinePlayer player)
	{
		API.data.savePlayerData(player, "player_kills", getKills(player) + 1);
	}

	/*
	 * getDeaths() : Returns (int)deaths for (Player)player.
	 */
	public int getDeaths(OfflinePlayer player)
	{
		if(API.data.getPlayerData(player, "player_deaths") != null) return Integer.parseInt(API.data.getPlayerData(player, "player_deaths").toString());
		return -1;
	}

	/*
	 * setDeaths() : Sets the (Player)player's deaths to (int)amount.
	 */
	public void setDeaths(OfflinePlayer player, int amount)
	{
		API.data.savePlayerData(player, "player_deaths", amount);
	}

	/*
	 * addDeath() : Gives (Player)player 1 death.
	 */
	public void addDeath(OfflinePlayer player)
	{
		API.data.savePlayerData(player, "player_deaths", getDeaths(player) + 1);
	}

	/*
	 * getOnlineAdmins() : Returns a ArrayList of all online admins.
	 */
	public ArrayList<Player> getOnlineAdmins()
	{
		ArrayList<Player> toReturn = new ArrayList<Player>();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(API.misc.hasPermissionOrOP(player, "demigods.admin")) toReturn.add(player);
		}
		return toReturn;
	}

	/*
	 * getOnlinePlayers() : Returns a ArrayList of all online players.
	 */
	public ArrayList<Player> getOnlinePlayers()
	{
		ArrayList<Player> toReturn = new ArrayList<Player>();
		Collections.addAll(toReturn, Bukkit.getOnlinePlayers());
		return toReturn;
	}

	/*
	 * getOfflinePlayers() : Returns a ArrayList of all offline players.
	 */
	public ArrayList<OfflinePlayer> getOfflinePlayers()
	{
		ArrayList<OfflinePlayer> toReturn = getAllPlayers();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			toReturn.remove(player);
		}
		return toReturn;
	}

	/*
	 * getAllPlayers() : Returns a ArrayList of all offline players.
	 */
	public ArrayList<OfflinePlayer> getAllPlayers()
	{
		ArrayList<OfflinePlayer> toReturn = new ArrayList<OfflinePlayer>();
		for(String playerName : API.data.getAllPlayers().keySet())
		{
			toReturn.add(Bukkit.getServer().getOfflinePlayer(playerName));
		}
		return toReturn;
	}

	/*
	 * togglePlayerMovement() : Toggles holding a player's feet in place.
	 */
	public void togglePlayerMovement(OfflinePlayer player, boolean option)
	{
		if(API.data.hasPlayerData(player, "temp_player_hold") && option) API.data.removePlayerData(player, "temp_player_hold");
		else API.data.savePlayerData(player, "temp_player_hold", true);
	}

	/*
	 * togglePlayerChat() : Toggles chat for a player.
	 */
	public void togglePlayerChat(OfflinePlayer player, boolean option)
	{
		if(API.data.hasPlayerData(player, "temp_no_chat") && option) API.data.removePlayerData(player, "temp_no_chat");
		else API.data.savePlayerData(player, "temp_no_chat", true);
	}
}

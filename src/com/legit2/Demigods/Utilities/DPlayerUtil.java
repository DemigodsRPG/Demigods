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

package com.legit2.Demigods.Utilities;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.legit2.Demigods.Database.DDatabase;
import com.legit2.Demigods.Libraries.DCharacter;

public class DPlayerUtil
{
	/*
	 *  createPlayer() : Adds (Player)player to the database.
	 */
	public static boolean createNewPlayer(Player player)
	{
		// First check to see if the player exists
		if(!DDataUtil.newPlayer(player))
		{
			DMiscUtil.info(player.getName() + " already exists in the database.");
			return false;
		}
		
		DMiscUtil.info("Adding new player (" + player.getName() + ") to the database.");
		
		// Define variables
		int playerID = DObjUtil.generateInt(5);

		// Create their HashMap data
		DDataUtil.addPlayer(player, playerID);
		DDataUtil.savePlayerData(player, "player_id", playerID);
		DDataUtil.savePlayerData(player, "player_kills", 0);
		DDataUtil.savePlayerData(player, "player_deaths", 0);
		DDataUtil.savePlayerData(player, "player_characters", null);
		
		// Add them to the database
		try
		{
			DDatabase.addPlayerToDB(player);
		}
		catch(SQLException e)
		{
			player.kickPlayer("Please contact an admin and give them this error code: " + ChatColor.RED + "2001");
		}
		
		return true;
	}
	
	/*
	 *  getPlayerID() : Returns the ID of (Player)player.
	 */
	public static int getPlayerID(OfflinePlayer player)
	{
		return DObjUtil.toInteger(DDataUtil.getPlayerData(player, "player_id"));
	}
	
	/*
	 *  getPlayerFromID() : Returns the (Player)player for (int)player_id.
	 */
	public static OfflinePlayer getPlayerFromID(int playerID)
	{
		for(Entry<String, HashMap<String, Object>> player : DDataUtil.getAllPlayers().entrySet())
		{
			if(player.getValue().get("player_id").equals(playerID)) return Bukkit.getOfflinePlayer(player.getKey());
		}
		return null;
	}
	
	/*
	 *  definePlayer() : Defines a Player from sender.
	 */
	public static OfflinePlayer definePlayer(String name)
	{
		return Bukkit.getOfflinePlayer(name);
	}
	
	/*
	 *  getCurrentChar() : Returns the current charID for (Player)player.
	 */
	public static DCharacter getCurrentChar(OfflinePlayer player)
	{
		try
		{
			return (DCharacter) DCharUtil.getChar(DObjUtil.toInteger(DDataUtil.getPlayerData(player, "current_char")));
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	/*
	 *  getCurrentAlliance() : Returns the current alliance for (Player)player.
	 */
	public static String getCurrentAlliance(OfflinePlayer player)
	{
		DCharacter character = getCurrentChar(player);
		if(character == null || !character.isImmortal()) return "Mortal";
		return character.getAlliance();
	}
	
	/*
	 *  getChars() : Returns an ArrayList of (Player)player's characters.
	 */
	public static List<Integer> getChars(OfflinePlayer player)
	{	
		List<Integer> charArray = new ArrayList<Integer>();
		HashMap<Integer, HashMap<String, Object>> characters = DDataUtil.getAllPlayerChars(player);
		
		if(characters == null) return charArray;
		for(Entry<Integer, HashMap<String, Object>> character : characters.entrySet())
		{
			int charID = character.getKey();
			charArray.add(charID);
		}
		return charArray;
	}
	
	/*
	 *  isImmortal() : Returns the current alliance for (Player)player.
	 */
	public static boolean isImmortal(OfflinePlayer player)
	{
		DCharacter character = getCurrentChar(player);
		if(character == null || !character.isImmortal()) return false;
		return true;
	}
	
	/*
	 *  hasCharID() : Checks to see if (OfflinePlayer)player has (int)charID.
	 */
	public static boolean hasCharID(OfflinePlayer player, int charID)
	{
		if(getChars(player) != null && getChars(player).contains(charID)) return true;
		return false;
	}
	
	/*
	 *  hasCharName() : Checks to see if (OfflinePlayer)player has (String)charName.
	 */
	public static boolean hasCharName(OfflinePlayer player, String charName)
	{
		List<Integer> characters = DPlayerUtil.getChars(player);
		
		for(int charID : characters)
		{
			DCharacter character = DCharUtil.getChar(charID);
			if(character == null || !character.isImmortal()) continue;
			if(character.getName().equalsIgnoreCase(charName)) return true;
		}
		return false;
	}
	
	/*
	 *  togglePraying() : Toggles prayer status for player.
	 */
	public static void togglePraying(OfflinePlayer player, boolean option)
	{
		if(!option)	DDataUtil.removePlayerData(player, "temp_praying");
		else DDataUtil.savePlayerData(player, "temp_praying", option);
	}
	
	/*
	 *  isPraying() : Returns a boolean for if the player is currently praying.
	 */
	public static boolean isPraying(OfflinePlayer player)
	{
		if(DDataUtil.getPlayerData(player, "temp_praying") == null) return false;
		else return DObjUtil.toBoolean(DDataUtil.getPlayerData(player, "temp_praying"));
	}
	
	/*
     *  regenerateAllFavor() : Regenerates favor for every player based on their stats.
     */
	public static void regenerateAllFavor()
	{
		ArrayList<Player> onlinePlayers = getOnlinePlayers();
		
		for(Player player : onlinePlayers)
		{
			DCharacter character = getCurrentChar(player);
			if(character == null || !character.isImmortal()) continue;
			int regenRate = (int) Math.ceil(DConfigUtil.getSettingDouble("global_favor_multiplier") * character.getAscensions());
			if(regenRate < 1) regenRate = 1;
			character.giveFavor(regenRate);
		}
	}
	
	/*
	 *  getNumberOfSouls() : Returns the number of souls (Player)player has in their inventory.
	 *
	public static int getNumberOfSouls(OfflinePlayer player)
	{
		// Define inventory contents & other variables
		ItemStack[] inventory = player.getInventory().getContents();
		ArrayList<ItemStack> allSouls = DSouls.returnAllSouls();
		int numberOfSouls = 0;
		
		for(ItemStack soul : allSouls)
		{
			for(ItemStack inventoryItem : inventory)
			{
				if(inventoryItem != null && inventoryItem.isSimilar(soul))
				{
					// Find amount of souls and subtract 1 upon use
					int amount = inventoryItem.getAmount();
					
					numberOfSouls = numberOfSouls + amount;
				}
			}
		}
		return numberOfSouls;
	}

	
	/*
	 *  useSoul() : Uses first soul found in (Player)player's inventory.
	 *
	public static ItemStack useSoul(OfflinePlayer player)
	{	
		if(getNumberOfSouls(player) == 0) return null;
		// Define inventory contents
		ItemStack[] inventory = player.getInventory().getContents();
		ArrayList<ItemStack> allSouls = DSouls.returnAllSouls();
		
		for(ItemStack soul : allSouls)
		{
			for(ItemStack inventoryItem : inventory)
			{
				if(inventoryItem != null && inventoryItem.isSimilar(soul))
				{
					// Find amount of souls and subtract 1 upon use
					int amount = inventoryItem.getAmount();
					player.getInventory().removeItem(inventoryItem);
					inventoryItem.setAmount(amount - 1);
					player.getInventory().addItem(inventoryItem);
					
					return inventoryItem;
				}
			}
		}
		return null;
	}
	*/
	
	/*
	 *  getKills() : Returns (int)kills for (Player)player.
	 */
	public static int getKills(OfflinePlayer player)
	{
		if(DDataUtil.getPlayerData(player, "player_kills") != null) return Integer.parseInt(DDataUtil.getPlayerData(player, "player_kills").toString());
		return -1;
	}
	
	/*
	 *  setKills() : Sets the (Player)player's kills to (int)amount.
	 */
	public static void setKills(OfflinePlayer player, int amount)
	{
		DDataUtil.savePlayerData(player, "player_kills", amount);
	}
	
	/*
	 *  addKill() : Gives (Player)player 1 kill.
	 */
	public static void addKill(OfflinePlayer player)
	{
		DDataUtil.savePlayerData(player, "player_kills", getKills(player) + 1);
	}
	
	/*
	 *  getDeaths() : Returns (int)deaths for (Player)player.
	 */
	public static int getDeaths(OfflinePlayer player)
	{
		if(DDataUtil.getPlayerData(player, "player_deaths") != null) return Integer.parseInt(DDataUtil.getPlayerData(player, "player_deaths").toString());
		return -1;
	}
	
	/*
	 *  setDeaths() : Sets the (Player)player's deaths to (int)amount.
	 */
	public static void setDeaths(OfflinePlayer player, int amount)
	{
		DDataUtil.savePlayerData(player, "player_deaths", amount);
	}
	
	/*
	 *  addDeath() : Gives (Player)player 1 death.
	 */
	public static void addDeath(OfflinePlayer player)
	{
		DDataUtil.savePlayerData(player, "player_deaths", getDeaths(player) + 1);
	}
	
	/*
	 *  getOnlinePlayers() : Returns a string array of all online players.
	 */
	public static ArrayList<Player> getOnlinePlayers()
	{
		ArrayList<Player> toReturn = new ArrayList<Player>();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			toReturn.add(player);
		}
		return toReturn;
	}
	
	/*
	 *  getOfflinePlayers() : Returns a string array of all offline players.
	 */
	public static ArrayList<OfflinePlayer> getOfflinePlayers()
	{
		ArrayList<OfflinePlayer> toReturn = getAllPlayers();
		for(Player player : Bukkit.getOnlinePlayers())
		{
			toReturn.remove((OfflinePlayer) player);
		}
		return toReturn;
	}
	
	/*
	 *  getAllPlayers() : Returns a string array of all offline players.
	 */
	public static ArrayList<OfflinePlayer> getAllPlayers()
	{
		ArrayList<OfflinePlayer> toReturn = new ArrayList<OfflinePlayer>();
		for(String playerName : DDataUtil.getAllPlayers().keySet())
		{
			toReturn.add(Bukkit.getServer().getOfflinePlayer(playerName));
		}
		return toReturn;
	}
}

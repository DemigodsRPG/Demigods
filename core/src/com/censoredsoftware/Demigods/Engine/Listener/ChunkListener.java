package com.censoredsoftware.Demigods.Engine.Listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemStack;

import com.censoredsoftware.Demigods.Demo.Data.Item.Baetylus;
import com.censoredsoftware.Demigods.Demo.Data.Item.Books;
import com.censoredsoftware.Demigods.Engine.Demigods;
import com.censoredsoftware.Demigods.Engine.Event.Misc.ChestSpawnEvent;
import com.censoredsoftware.Demigods.Engine.Utility.ItemUtility;
import com.censoredsoftware.Demigods.Engine.Utility.MiscUtility;

public class ChunkListener implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public void onChunkLoad(ChunkLoadEvent event)
	{
		// Define variables
		Location location = MiscUtility.randomChunkLocation(event.getChunk());

		// Let's randomly create chests
		if(MiscUtility.randomPercentBool(Demigods.config.getSettingDouble("generation.chest_chance")) && location.clone().subtract(0, 1, 0).getBlock().getType().isSolid())
		{
			ChestSpawnEvent chestSpawnEvent = new ChestSpawnEvent(location);
			Bukkit.getServer().getPluginManager().callEvent(chestSpawnEvent);
			if(chestSpawnEvent.isCancelled()) return;

			// Define variables
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();

			// Books!
			for(Books book : Books.values())
			{
				if(MiscUtility.randomPercentBool(book.getBook().getSpawnChance())) items.add(book.getBook().getItem());
			}

			items.add(Baetylus.NORMAL_SHARD.getShard().getItem());

			// Generate the chest
			ItemUtility.createChest(location, items);
		}
	}
}

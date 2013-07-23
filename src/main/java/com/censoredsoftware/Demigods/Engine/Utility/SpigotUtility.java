package com.censoredsoftware.Demigods.Engine.Utility;

import org.bukkit.Effect;
import org.bukkit.Location;

import com.censoredsoftware.Demigods.Engine.Demigods;

public class SpigotUtility
{
	public static void playParticle(Location location, Effect effect, int offsetX, int offsetY, int offsetZ, float speed, int particles, int viewRadius)
	{
		if(!Demigods.runningSpigot()) throw new IllegalArgumentException("Spigot is required to use this feature.");
		location.getWorld().spigot().playEffect(location, effect, 1, 1, offsetX, offsetY, offsetZ, speed, particles, viewRadius);
	}

	public static void drawCircle(Location center, Effect effect, double radius, int points)
	{
		if(!Demigods.runningSpigot()) throw new IllegalArgumentException("Spigot is required to use this feature.");
		for(Location point : MiscUtility.getCirclePoints(center, radius, points))
		{
			playParticle(new Location(point.getWorld(), point.getBlockX(), point.getWorld().getHighestBlockYAt(point), point.getBlockZ()), effect, 0, 6, 0, 1F, 15, (int) (radius * 2.5));
		}
	}

}

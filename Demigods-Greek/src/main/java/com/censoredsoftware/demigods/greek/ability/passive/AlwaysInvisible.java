package com.censoredsoftware.demigods.greek.ability.passive;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.censoredsoftware.demigods.engine.data.DCharacter;
import com.censoredsoftware.demigods.engine.data.Skill;
import com.censoredsoftware.demigods.engine.mythos.Ability;
import com.censoredsoftware.demigods.engine.util.Zones;
import com.google.common.collect.Lists;

public class AlwaysInvisible implements Ability
{
	private final static String name = "Invisible", command = null;
	private final static int cost = 0, delay = 0, repeat = 20;
	private final static List<String> details = Lists.newArrayList("They'll never see you coming.");
	private String deity, permission;
	private final static Skill.Type type = Skill.Type.PASSIVE;

	public AlwaysInvisible(String deity, String permission)
	{
		this.deity = deity;
		this.permission = permission;
	}

	@Override
	public String getDeity()
	{
		return deity;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public String getCommand()
	{
		return command;
	}

	@Override
	public String getPermission()
	{
		return permission;
	}

	@Override
	public int getCost()
	{
		return cost;
	}

	@Override
	public int getDelay()
	{
		return delay;
	}

	@Override
	public int getRepeat()
	{
		return repeat;
	}

	@Override
	public List<String> getDetails()
	{
		return details;
	}

	@Override
	public Skill.Type getType()
	{
		return type;
	}

	@Override
	public Material getWeapon()
	{
		return null;
	}

	@Override
	public boolean hasWeapon()
	{
		return getWeapon() != null;
	}

	@Override
	public Listener getListener()
	{
		return null;
	}

	@Override
	public BukkitRunnable getRunnable()
	{
		return new BukkitRunnable()
		{
			@Override
			public void run()
			{
				for(DCharacter character : DCharacter.Util.getOnlineCharactersWithAbility(name))
				{
					if(Zones.inNoDemigodsZone(character.getOfflinePlayer().getPlayer().getLocation())) continue;
					Player player = character.getOfflinePlayer().getPlayer();
					potionEffect(player);
					if(player.isInsideVehicle() && player.getVehicle().getType().equals(EntityType.HORSE)) potionEffect((LivingEntity) player.getVehicle());
				}
			}

			private void potionEffect(LivingEntity entity)
			{
				entity.removePotionEffect(PotionEffectType.INVISIBILITY);
				entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 120, 1));
			}
		};
	}
}

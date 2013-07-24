package com.censoredsoftware.Demigods.Engine.Object.Ability;

import java.util.Set;

import org.bukkit.inventory.ItemStack;

import redis.clients.johm.*;

import com.censoredsoftware.Demigods.Engine.Object.General.DemigodsItemStack;
import com.censoredsoftware.Demigods.Engine.Utility.MiscUtility;

@Model
public class AbilityBind
{
	@Id
	private Long id;
	@Attribute
	@Indexed
	private String identifier;
	@Attribute
	private String ability;
	@Attribute
	private Integer slot;
	@Reference
	private DemigodsItemStack item;

	void setIdentifier(String identifier)
	{
		this.identifier = identifier;
	}

	void setAbility(String ability)
	{
		this.ability = ability;
	}

	void setSlot(Integer slot)
	{
		this.slot = slot;
	}

	void setItem(ItemStack item)
	{
		this.item = DemigodsItemStack.create(item);
	}

	public static AbilityBind create(String ability, int slot, ItemStack item)
	{
		AbilityBind bind = new AbilityBind();
		bind.setIdentifier(MiscUtility.generateString(6));
		bind.setAbility(ability);
		bind.setSlot(slot);
		bind.setItem(item);
		AbilityBind.save(bind);
		return bind;
	}

	public ItemStack getRawItem()
	{
		return new ItemStack(this.item.toItemStack().getType());
	}

	public DemigodsItemStack getItem()
	{
		return this.item;
	}

	public String getAbility()
	{
		return this.ability;
	}

	public String getIdentifier()
	{
		return this.identifier;
	}

	public int getSlot()
	{
		return this.slot;
	}

	public static void save(AbilityBind item)
	{
		JOhm.save(item);
	}

	public static AbilityBind load(long id)
	{
		return JOhm.get(AbilityBind.class, id);
	}

	public static Set<AbilityBind> loadAll()
	{
		return JOhm.getAll(AbilityBind.class);
	}

}
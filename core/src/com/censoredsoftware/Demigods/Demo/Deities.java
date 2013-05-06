package com.censoredsoftware.Demigods.Demo;

import com.censoredsoftware.Demigods.Demo.Data.Deity.God.Zeus;
import com.censoredsoftware.Demigods.Engine.Deity.Deity;
import com.censoredsoftware.Demigods.Engine.Demigods;

public enum Deities implements Demigods.ListedDeity
{
	ZEUS(new Zeus());

	private Deity deity;

	private Deities(Deity deity)
	{
		this.deity = deity;
	}

	@Override
	public Deity getDeity()
	{
		return deity;
	}
}
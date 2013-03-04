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

package com.censoredsoftware.Demigods.Theogony.Quests.Gods.Poseidon.Test_Quest;

import com.censoredsoftware.Demigods.Demigods;
import com.censoredsoftware.Demigods.Handlers.Abstract.TaskHandler;
import com.censoredsoftware.Demigods.Libraries.Objects.PlayerCharacter;
import com.censoredsoftware.Demigods.Libraries.Objects.Task;
import com.censoredsoftware.Demigods.Theogony.Theogony;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.ArrayList;

public class Task1_task extends TaskHandler implements Listener
{
    private static final Demigods API = Theogony.INSTANCE;
    private static final String INVOKEID = "Gods.Poseidon.Test_Quest.Task1";

    @Override
    public void create(PlayerCharacter character)
    {
        if(!API.task.taskExists("Test Quest", 0, character))
        {
            int taskID = API.object.generateInt(5);
            ArrayList<String> description = new ArrayList<String>();
            description.add(ChatColor.YELLOW + " This is a test task.");
            description.add(ChatColor.YELLOW + " Click anywhere to complete this quest!");

            Task task1 = new Task(character, description, "Test Quest", 0, true, true, API.plugin.getPlugin("Theogony"), "com.censoredsoftware.Demigods.Theogony.Quests.Gods.Poseidon.Test_Quest.Task1_task", taskID, INVOKEID);
            if(character.getOwner().isOnline())
            {
                API.misc.taggedMessage(character.getOwner().getPlayer(), task1.getQuest());
                for(String out : task1.getDescription())
                {
                    character.getOwner().getPlayer().sendMessage(out);
                }
            }

            onInvoke();
        }
    }

    @Override
    public void onInvoke()
    {
        API.getServer().getPluginManager().registerEvents(this, API);
    }

    @Override
    public void onComplete(Task task)
    {
        Player player = task.getCharacter().getOwner().getPlayer();
        API.misc.taggedMessage(player, task.getQuest());
        player.sendMessage(ChatColor.YELLOW + " Quest complete!");
        task.setActive(false);
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        PlayerCharacter character = API.player.getCurrentChar(player);

        ArrayList<Task> tasks = API.task.getTasks(character);

        if(tasks.isEmpty()) return;

        for(Task task : tasks)
        {
            if(task.getInvokeID().equals(INVOKEID))
            {
                Firework firework = (Firework) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
                FireworkMeta fireworkmeta = firework.getFireworkMeta();
                FireworkEffect.Type type = FireworkEffect.Type.BALL_LARGE;
                FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.AQUA).withFade(Color.FUCHSIA).with(type).trail(true).build();
                fireworkmeta.addEffect(effect);
                fireworkmeta.setPower(2);
                firework.setFireworkMeta(fireworkmeta);

                onComplete(task);
            }
        }
    }
}

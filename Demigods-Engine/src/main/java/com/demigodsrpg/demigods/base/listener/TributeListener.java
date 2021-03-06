package com.demigodsrpg.demigods.base.listener;

import com.demigodsrpg.demigods.engine.data.DemigodsWorld;
import com.demigodsrpg.demigods.engine.data.TempDataManager;
import com.demigodsrpg.demigods.engine.deity.Deity;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsCharacter;
import com.demigodsrpg.demigods.engine.entity.player.DemigodsPlayer;
import com.demigodsrpg.demigods.engine.language.English;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructure;
import com.demigodsrpg.demigods.engine.structure.DemigodsStructureType;
import com.demigodsrpg.demigods.engine.tribute.TributeManager;
import com.demigodsrpg.demigods.engine.util.Configs;
import com.demigodsrpg.demigods.engine.util.Zones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.UUID;

public class TributeListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTributeInteract(PlayerInteractEvent event) {
        if (Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;

        // Return if the player is mortal
        if (!DemigodsPlayer.isImmortal(event.getPlayer())) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        // Define variables
        Location location = event.getClickedBlock().getLocation();
        Player player = event.getPlayer();
        DemigodsCharacter character = DemigodsCharacter.of(player);

        if (DemigodsStructureType.Util.partOfStructureWithFlag(location, DemigodsStructureType.Flag.TRIBUTE_LOCATION)) {
            // Cancel the interaction
            event.setCancelled(true);

            // Define the shrine
            DemigodsStructure save = DemigodsStructureType.Util.getStructureRegional(location);

            // Return if they aren't clicking the gold block
            if (!save.getClickableBlocks().contains(event.getClickedBlock().getLocation())) return;

            // Return if the player is mortal
            if (!DemigodsPlayer.isImmortal(player)) {
                player.sendMessage(ChatColor.RED + English.DISABLED_MORTAL.getLine());
                return;
            } else if (save.getOwner() != null && !character.getDeity().equals(DemigodsCharacter.get(save.getOwner()).getDeity())) {
                player.sendMessage(English.MUST_BE_ALLIED_TO_TRIBUTE.getLine().replace("{deity}", DemigodsCharacter.get(save.getOwner()).getDeity().getName()));
                return;
            }
            tribute(character, save);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTribute(InventoryCloseEvent event) {
        if (Zones.inNoDemigodsZone(event.getPlayer().getLocation())) return;

        // Define player and character
        Player player = (Player) event.getPlayer();
        DemigodsCharacter character = DemigodsCharacter.of(player);

        // Make sure they have a character and are immortal
        if (character == null) return;

        // If it isn't a tribute chest then break the method
        if (!event.getInventory().getName().contains("Tribute to") /* TODO make this work with translations, I'm sleepy */ || !DemigodsStructureType.Util.partOfStructureWithFlag(player.getTargetBlock((Set) null, 10).getLocation(), DemigodsStructureType.Flag.TRIBUTE_LOCATION))
            return;

        // Get the creator of the shrine
        DemigodsStructure save = DemigodsStructure.get(DemigodsWorld.of(player.getWorld()), UUID.fromString(TempDataManager.getValueTemp(player.getName(), character.getName()).toString()));

        // Calculate the tribute value
        int tributeValue = 0, items = 0;
        for (ItemStack item : event.getInventory().getContents()) {
            if (item != null) {
                tributeValue += TributeManager.processTribute(item);
                items += item.getAmount();
            }
        }

        // Return if it's empty
        if (items == 0) return;

        // Handle the multiplier
        tributeValue *= Configs.getSettingDouble("multipliers.favor");

        // Get the current favor for comparison
        int favorBefore = character.getMeta().getFavor();
        int maxFavorBefore = character.getMeta().getMaxFavor();

        // Update the character's favor
        character.getMeta().addFavor(tributeValue / 3);
        character.getMeta().addMaxFavor(tributeValue);

        // Define the shrine owner
        if (save.getOwner() != null) {
            DemigodsCharacter shrineOwner = DemigodsCharacter.get(save.getOwner());
            OfflinePlayer shrineOwnerPlayer = shrineOwner.getBukkitOfflinePlayer();

            if (character.getMeta().getMaxFavor() >= Configs.getSettingInt("caps.favor") && !player.getName().equals(shrineOwnerPlayer.getName())) {
                // Give them some of the blessings
                shrineOwner.getMeta().addMaxFavor(tributeValue / 5);

                // Message them
                if (shrineOwnerPlayer.isOnline() && DemigodsCharacter.of(shrineOwner.getBukkitOfflinePlayer()).getId().equals(shrineOwner.getId())) {
                    ((Player) shrineOwnerPlayer).sendMessage(English.EXTERNAL_SHRINE_TRIBUTE.getLine());
                    ((Player) shrineOwnerPlayer).sendMessage(English.FAVOR_CAP_INCREASED.getLine().replace("{cap}", shrineOwner.getMeta().getMaxFavor().toString()));
                }
            } else if (character.getMeta().getMaxFavor() > maxFavorBefore && !player.getName().equals(shrineOwnerPlayer.getName())) {
                // Define variables
                int ownerFavorBefore = shrineOwner.getMeta().getMaxFavor();

                // Give them some of the blessings
                shrineOwner.getMeta().addMaxFavor(tributeValue / 5);

                // Message them
                if (shrineOwnerPlayer.isOnline() && DemigodsCharacter.of(shrineOwner.getBukkitOfflinePlayer()).getId().equals(shrineOwner.getId())) {
                    ((Player) shrineOwnerPlayer).sendMessage(English.EXTERNAL_SHRINE_TRIBUTE.getLine());
                    if (shrineOwner.getMeta().getMaxFavor() > ownerFavorBefore)
                        ((Player) shrineOwnerPlayer).sendMessage(English.FAVOR_CAP_INCREASED.getLine().replace("{cap}", shrineOwner.getMeta().getMaxFavor().toString()));
                }
            }

            // Sanctify the Shrine
            save.sanctify(character, 1F);
        }

        // Handle messaging and Shrine owner updating
        if (character.getMeta().getMaxFavor() >= Configs.getSettingInt("caps.favor")) {
            // They have already met the max favor cap
            player.sendMessage(English.DEITY_PLEASED.getLine().replace("{deity}", character.getDeity().getName()));
            if (character.getMeta().getFavor() > favorBefore)
                player.sendMessage(English.BLESSED_WITH_FAVOR.getLine().replace("{favor}", "" + (character.getMeta().getFavor() - favorBefore)));
        } else if (character.getMeta().getMaxFavor() > maxFavorBefore) {
            // Message the tributer
            player.sendMessage(English.DEITY_PLEASED.getLine().replace("{deity}", character.getDeity().getName()));
            player.sendMessage(English.FAVOR_CAP_INCREASED.getLine().replace("{cap}", character.getMeta().getMaxFavor().toString()));
        } else if (items > 0) {
            // They aren't good enough, let them know!
            player.sendMessage(English.INSUFFICIENT_TRIBUTES.getLine().replace("{deity}", character.getDeity().getName()));
        }

        // Clear the tribute case
        event.getInventory().clear();
    }

    private static void tribute(DemigodsCharacter character, DemigodsStructure save) {
        Player player = character.getBukkitOfflinePlayer().getPlayer();
        Deity shrineDeity = character.getDeity();

        // Open the tribute inventory
        Inventory ii = Bukkit.getServer().createInventory(player, 27, English.CHEST_TRIBUTE_TO.getLine().replace("{deity}", shrineDeity.getName()));
        player.openInventory(ii);
        TempDataManager.saveTemp(player.getName(), character.getName(), save.getId());
    }
}

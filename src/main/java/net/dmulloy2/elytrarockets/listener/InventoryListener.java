/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.elytrarockets.listener;

import java.util.Map;

import net.dmulloy2.elytrarockets.types.RefuelInventory;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * @author dmulloy2
 */
public class InventoryListener implements Listener {
	// Handles the bulk of the refueling logic
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory inventory = event.getClickedInventory();
		if (inventory == null) {
			return;
		}

		InventoryHolder holder = inventory.getHolder();
		if (holder instanceof RefuelInventory) {
			RefuelInventory refuel = (RefuelInventory) holder;
			refuel.handleClick(event);
		}
	}

	// Prevent shift clicking ... hopefully
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onInventoryMoveItem(InventoryMoveItemEvent event) {
		Inventory inventory = event.getDestination();
		InventoryHolder holder = inventory.getHolder();
		if (holder instanceof RefuelInventory) {
			event.setCancelled(true);
		}
	}

	// Empty refueling inventories when it closes
	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClose(InventoryCloseEvent event) {
		Inventory inventory = event.getInventory();
		InventoryHolder holder = inventory.getHolder();
		if (holder instanceof RefuelInventory) {
			HumanEntity player = event.getPlayer();
			for (ItemStack item : inventory.getContents()) {
				if (item != null && item.getType() != Material.STAINED_GLASS_PANE) {
					Map<Integer, ItemStack> remains = player.getInventory().addItem(item);
					for (ItemStack remain : remains.values()) {
						player.getWorld().dropItemNaturally(player.getLocation(), remain);
					}
				}
			}
		}
	}
}
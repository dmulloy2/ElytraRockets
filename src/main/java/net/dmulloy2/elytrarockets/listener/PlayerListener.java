/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.elytrarockets.listener;

import net.dmulloy2.elytrarockets.ElytraRockets;
import net.dmulloy2.elytrarockets.ElytraRocketsAPI;
import net.dmulloy2.elytrarockets.config.Config;
import net.dmulloy2.elytrarockets.handlers.ItemHandler;
import net.dmulloy2.util.ListUtil;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import lombok.AllArgsConstructor;

/**
 * @author dmulloy2
 */
@AllArgsConstructor
public class PlayerListener implements Listener {
	private final ElytraRockets plugin;

	private boolean hasElytra(Player player) {
		ItemStack chestplate = player.getInventory().getChestplate();
		return chestplate != null && chestplate.getType() == Material.ELYTRA;
	}

	private void boost(Player player, ItemStack item, Cancellable event) {
		ItemHandler handler = plugin.getItemHandler();

		rocketCheck: {
			if (!handler.isFuelRocket(item)) {
				// This only runs with the drop event,
				// so if someone drops something in the arena,
				// they boost. Interact events shouldn't get here.
				if (ElytraRocketsAPI.alwaysActivates(player)) {
					for (ItemStack check : player.getInventory().getStorageContents()) {
						if (check != null && handler.isFuelRocket(check)) {
							item = check;
							break rocketCheck;
						}
					}
				}
			}

			return;
		}

		event.setCancelled(true);

		if (! player.isGliding()) {
			return;
		}

		Location location = player.getLocation();
		int maxHeight = Config.maxHeight;
		if (maxHeight > 0 && location.getBlockY() > maxHeight) {
			return;
		}

		int fuel = handler.getFuel(item);
		if (fuel > 0) {
			Vector velocity = player.getLocation().getDirection();
			double y = velocity.getY();
			velocity.setY(y >= 0 ? y + Config.yOffset : y - Config.yOffset);

			velocity.multiply(handler.getMultiplier(item));
			player.setVelocity(velocity);

			if (handler.isUnlimited(item) || (Config.hubModeEnabled && handler.isHubRocket(item))) {
				return;
			}

			plugin.getItemHandler().changeFuel(item, fuel, -1);
			player.updateInventory();
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (Config.activateOnDrop) {
			Player player = event.getPlayer();
			if (hasElytra(player)) {
				ItemStack item = event.getItemDrop().getItemStack();
				boost(player, item, event);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (Config.activateOnInteract) {
			Player player = event.getPlayer();
			if (hasElytra(player)) {
				ItemStack item = event.getItem();
				if (item != null && plugin.getItemHandler().isFuelRocket(item)) {
					boost(player, item, event);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (Config.hubModeEnabled) {
			if (ListUtil.containsIgnoreCase(Config.hubWorlds, player.getWorld().getName())) {
				plugin.getItemHandler().giveHubRocket(player);
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (Config.hubModeEnabled) {
			Player player = event.getPlayer();
			if (ListUtil.containsIgnoreCase(Config.hubWorlds, player.getWorld().getName())) {
				plugin.getItemHandler().removeHubRockets(event.getPlayer());
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		if (Config.hubModeEnabled) {
			Player player = event.getPlayer();
			String from = event.getFrom().getName();
			String to = player.getWorld().getName();

			boolean fromIsHub = ListUtil.containsIgnoreCase(Config.hubWorlds, from);
			boolean toIsHub = ListUtil.containsIgnoreCase(Config.hubWorlds, to);

			if (fromIsHub && toIsHub) {
				return;
			}

			if (fromIsHub) {
				plugin.getItemHandler().removeHubRockets(player);
			} else if (toIsHub) {
				plugin.getItemHandler().giveHubRocket(player);
			}
		}
	}
}
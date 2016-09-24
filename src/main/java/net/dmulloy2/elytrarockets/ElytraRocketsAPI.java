/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.elytrarockets;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author dmulloy2
 */
public final class ElytraRocketsAPI {
	private static ElytraRockets plugin;
	private static Set<String> alwaysActivate = null;

	protected static void init(ElytraRockets plugin) {
		ElytraRocketsAPI.plugin = plugin;
	}

	public static boolean isRocket(ItemStack item) {
		return plugin.getItemHandler().isFuelRocket(item);
	}

	public static boolean hasRocket(Player player) {
		for (ItemStack item : player.getInventory().getStorageContents()) {
			if (item != null && isRocket(item)) {
				return true;
			}
		}

		return false;
	}

	public static int getFuel(ItemStack item) {
		return plugin.getItemHandler().getFuel(item);
	}

	public static void setUnlimited(ItemStack item) {
		plugin.getItemHandler().markUnlimited(item);
	}

	public static ItemStack create(int tier, int fuel) {
		return plugin.getItemHandler().create(tier, fuel);
	}

	public static void setAlwaysActivate(Player player, boolean state) {
		if (alwaysActivate == null) {
			alwaysActivate = new HashSet<>();
		}

		if (state) {
			alwaysActivate.add(player.getName());
		} else {
			alwaysActivate.remove(player.getName());
		}
	}

	public static boolean alwaysActivates(Player player) {
		return alwaysActivate != null && alwaysActivate.contains(player.getName());
	}
}
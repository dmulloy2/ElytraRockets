/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.nbt;

import org.bukkit.inventory.ItemStack;

/**
 * @author dmulloy2
 */
public class ItemNBT {
	private static NBTProvider provider;

	static {
		try {
			provider = new NMSProvider();
		} catch (Throwable ex) {
			provider = new ReflectionProvider();
		}
	}

	private ItemNBT() { }

	public static boolean hasKeys(ItemStack item, String... keys) {
		return provider.hasKeys(item, keys);
	}

	public static int getInt(ItemStack item, String key) {
		return provider.getInt(item, key);
	}

	public static ItemStack setInt(ItemStack item, String key, int value) {
		return provider.setInt(item, key, value);
	}

	public static boolean getBoolean(ItemStack item, String key) {
		return provider.getBoolean(item, key);
	}

	public static ItemStack setBoolean(ItemStack item, String key, boolean value) {
		return provider.setBoolean(item, key, value);
	}
}
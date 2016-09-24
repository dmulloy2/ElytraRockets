/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.nbt;

import org.bukkit.inventory.ItemStack;

/**
 * @author dmulloy2
 */
public interface NBTProvider {

	public Object getHandle(ItemStack item);

	public boolean hasTag(Object handle);

	public boolean hasKeys(ItemStack item, String... keys);

	public int getInt(ItemStack item, String key);

	public ItemStack setInt(ItemStack item, String key, int value);

	public boolean getBoolean(ItemStack item, String key);

	public ItemStack setBoolean(ItemStack item, String key, boolean value);
}
/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.nbt;

import java.lang.reflect.Field;

import net.minecraft.server.v1_11_R1.NBTTagCompound;

import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * @author dmulloy2
 */
public class NMSProvider implements NBTProvider {
	private Field getHandle;

	NMSProvider() { }

	@Override
	public Object getHandle(ItemStack item) {
		if (item instanceof CraftItemStack) {
			try {
				if (getHandle == null) {
					getHandle = CraftItemStack.class.getDeclaredField("handle");
					getHandle.setAccessible(true);
				}

				return getHandle.get(item);
			} catch (ReflectiveOperationException ex) {
				throw new RuntimeException(ex);
			}
		}

		return CraftItemStack.asNMSCopy(item);
	}

	@Override
	public boolean hasTag(Object handle) {
		return ((net.minecraft.server.v1_11_R1.ItemStack) handle).hasTag();
	}

	@Override
	public boolean hasKeys(ItemStack item, String... keys) {
		net.minecraft.server.v1_11_R1.ItemStack handle = (net.minecraft.server.v1_11_R1.ItemStack) getHandle(item);
		if (handle.hasTag()) {
			NBTTagCompound tag = handle.getTag();
			for (String key : keys) {
				if (!tag.hasKey(key)) {
					return false;
				}
			}

			return true;
		}

		return false;
	}

	@Override
	public int getInt(ItemStack item, String key) {
		net.minecraft.server.v1_11_R1.ItemStack handle = (net.minecraft.server.v1_11_R1.ItemStack) getHandle(item);
		if (handle.hasTag()) {
			NBTTagCompound tag = handle.getTag();
			return tag.getInt(key);
		}

		return -1;
	}

	@Override
	public ItemStack setInt(ItemStack item, String key, int value) {
		net.minecraft.server.v1_11_R1.ItemStack handle = (net.minecraft.server.v1_11_R1.ItemStack) getHandle(item);
		if (!handle.hasTag()) {
			handle.setTag(new NBTTagCompound());
		}

		NBTTagCompound tag = handle.getTag();
		tag.setInt(key, value);
		handle.setTag(tag);
		return CraftItemStack.asCraftMirror(handle);
	}

	@Override
	public boolean getBoolean(ItemStack item, String key) {
		net.minecraft.server.v1_11_R1.ItemStack handle = (net.minecraft.server.v1_11_R1.ItemStack) getHandle(item);
		if (handle.hasTag()) {
			NBTTagCompound tag = handle.getTag();
			return tag.getBoolean(key);
		}

		return false;
	}

	@Override
	public ItemStack setBoolean(ItemStack item, String key, boolean value) {
		net.minecraft.server.v1_11_R1.ItemStack handle = (net.minecraft.server.v1_11_R1.ItemStack) getHandle(item);
		if (!handle.hasTag()) {
			handle.setTag(new NBTTagCompound());
		}

		NBTTagCompound tag = handle.getTag();
		tag.setBoolean(key, value);
		handle.setTag(tag);
		return CraftItemStack.asCraftMirror(handle);
	}
}
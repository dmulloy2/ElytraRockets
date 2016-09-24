/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.nbt;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

/**
 * @author dmulloy2
 */
public class ReflectionProvider implements NBTProvider {
	// ---- Classes
	private static Class<?> craftItemStack;
	private static Class<?> nmsItemStack;
	private static Class<?> nbtTagCompound;

	// ---- ItemStack members
	private static Field getHandle;
	private static Method hasTag;
	private static Method getTag;
	private static Method setTag;

	private static Method asNMSCopy;
	private static Method asCraftMirror;

	// ---- NBTTagCompound members
	private static Constructor<?> compoundConstructor;
	private static Method hasKey;

	private static Method setInt;
	private static Method getInt;

	private static Method setBoolean;
	private static Method getBoolean;

	static {
		try {
			String serverPackage = Bukkit.getServer().getClass().getPackage().getName();
			String version = serverPackage.substring(serverPackage.lastIndexOf('.') + 1);
			String obc = "org.bukkit.craftbukkit." + version + ".";

			craftItemStack = Class.forName(obc + "inventory.CraftItemStack");

			getHandle = craftItemStack.getDeclaredField("handle");
			getHandle.setAccessible(true);

			nmsItemStack = getHandle.getType();

			asNMSCopy = craftItemStack.getMethod("asNMSCopy", ItemStack.class);
			asCraftMirror = craftItemStack.getMethod("asCraftMirror", nmsItemStack);

			hasTag = nmsItemStack.getMethod("hasTag", new Class<?>[0]);
			getTag = nmsItemStack.getMethod("getTag", new Class<?>[0]);

			nbtTagCompound = getTag.getReturnType();
			compoundConstructor = nbtTagCompound.getConstructor(new Class<?>[0]);

			setTag = nmsItemStack.getMethod("setTag", nbtTagCompound);

			hasKey = nbtTagCompound.getMethod("hasKey", String.class);

			setInt = nbtTagCompound.getMethod("setInt", String.class, int.class);
			getInt = nbtTagCompound.getMethod("getInt", String.class);

			setInt = nbtTagCompound.getMethod("setBoolean", String.class, boolean.class);
			getInt = nbtTagCompound.getMethod("getBoolean", String.class);
		} catch (ReflectiveOperationException ex) {
			ex.printStackTrace();
		}
	}

	ReflectionProvider() { }

	@Override
	public Object getHandle(ItemStack stack) {
		if (craftItemStack.isInstance(stack)) {
			try {
				Object handle = getHandle.get(stack);
				if (handle != null) {
					return handle;
				}
			} catch (ReflectiveOperationException ex) {
				throw new RuntimeException(ex);
			}
		}

		try {
			return asNMSCopy.invoke(null, stack);
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public boolean hasTag(Object handle) {
		try {
			return (boolean) hasTag.invoke(handle);
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public boolean hasKeys(ItemStack stack, String... keys) {
		if (keys.length == 0) {
			return false;
		}

		Object handle = getHandle(stack);
		if (hasTag(handle)) {
			try {
				Object tag = getTag.invoke(handle);

				for (String key : keys) {
					if (! (boolean) hasKey.invoke(tag, key)) {
						return false;
					}
				}

				return true;
			} catch (ReflectiveOperationException ex) {
				throw new RuntimeException(ex);
			}
		}

		return false;
	}

	@Override
	public int getInt(ItemStack stack, String key) {
		Object handle = getHandle(stack);
		if (hasTag(handle)) {
			try {
				Object tag = getTag.invoke(handle);
				if ((boolean) hasKey.invoke(tag, key)) {
					return (int) getInt.invoke(tag, key);
				}
			} catch (ReflectiveOperationException ex) {
				throw new RuntimeException(ex);
			}
		}

		return -1;
	}

	@Override
	public ItemStack setInt(ItemStack stack, String key, int value) {
		Object handle = getHandle(stack);
		if (!hasTag(handle)) {
			try {
				Object tag = compoundConstructor.newInstance();
				setTag.invoke(handle, tag);
			} catch (ReflectiveOperationException ex) {
				throw new RuntimeException(ex);
			}
		}

		try {
			Object tag = getTag.invoke(handle);
			setInt.invoke(tag, key, value);
			setTag.invoke(handle, tag);
			return (ItemStack) asCraftMirror.invoke(null, handle);
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public boolean getBoolean(ItemStack item, String key) {
		Object handle = getHandle(item);
		if (hasTag(handle)) {
			try {
				Object tag = getTag.invoke(handle);
				if ((boolean) hasKey.invoke(tag, key)) {
					return (boolean) getBoolean.invoke(tag, key);
				}
			} catch (ReflectiveOperationException ex) {
				throw new RuntimeException(ex);
			}
		}

		return false;
	}

	@Override
	public ItemStack setBoolean(ItemStack item, String key, boolean value) {
		Object handle = getHandle(item);
		if (!hasTag(handle)) {
			try {
				Object tag = compoundConstructor.newInstance();
				setTag.invoke(handle, tag);
			} catch (ReflectiveOperationException ex) {
				throw new RuntimeException(ex);
			}
		}

		try {
			Object tag = getTag.invoke(handle);
			setBoolean.invoke(tag, key, value);
			setTag.invoke(handle, tag);
			return (ItemStack) asCraftMirror.invoke(null, handle);
		} catch (ReflectiveOperationException ex) {
			throw new RuntimeException(ex);
		}
	}
}
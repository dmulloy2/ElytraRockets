/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.elytrarockets.handlers;

import java.util.List;

import net.dmulloy2.elytrarockets.config.Config;
import net.dmulloy2.elytrarockets.config.Messages;
import net.dmulloy2.nbt.ItemNBT;
import net.dmulloy2.util.FormatUtil;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

/**
 * @author dmulloy2
 */
public class FuelHandler {
	private static final String NBT_KEY_FUEL = "fuelcell.fuel";

	public ItemStack create(int fuel) {
		ItemStack item = ItemNBT.setInt(new ItemStack(Config.cellMaterial), NBT_KEY_FUEL, fuel);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Messages.normalCell);

		List<String> lore = Lists.newArrayList();
		lore.add(FormatUtil.format(Messages.baseColor + Messages.cellFuelFormat
				.replace("{cells}", Integer.toString(fuel))
		));
		meta.setLore(lore);

		item.setItemMeta(meta);
		return item;
	}

	public void setFuel(ItemStack item, int fuel) {
		ItemNBT.setInt(item, NBT_KEY_FUEL, fuel);

		ItemMeta meta = item.getItemMeta();

		if (fuel == 0) {
			meta.setDisplayName(Messages.emptyCell);
		} else {
			meta.setDisplayName(Messages.normalCell);
		}

		List<String> lore = Lists.newArrayList();
		lore.add(FormatUtil.format(Messages.baseColor + Messages.cellFuelFormat
				.replace("{cells}", Integer.toString(fuel))
		));
		meta.setLore(lore);

		item.setItemMeta(meta);
	}

	public boolean isFuelCell(ItemStack item) {
		return ItemNBT.hasKeys(item, NBT_KEY_FUEL);
	}

	public int getFuel(ItemStack item) {
		return ItemNBT.getInt(item, NBT_KEY_FUEL);
	}
}
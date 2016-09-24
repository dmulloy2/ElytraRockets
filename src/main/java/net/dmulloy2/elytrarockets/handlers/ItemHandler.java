/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.elytrarockets.handlers;

import java.util.List;

import net.dmulloy2.elytrarockets.config.Config;
import net.dmulloy2.elytrarockets.config.Messages;
import net.dmulloy2.nbt.ItemNBT;
import net.dmulloy2.util.FormatUtil;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

/**
 * @author dmulloy2
 */
public class ItemHandler {
	private static final String NBT_KEY_FUEL = "rockets.fuel";
	private static final String NBT_KEY_TIER = "rockets.tier";
	private static final String NBT_KEY_HUB = "rockets.hub";
	private static final String NBT_KEY_UNLIM = "rockets.unlim";

	public ItemStack create(int tier, int fuel) {
		ItemStack item = ItemNBT.setInt(new ItemStack(Config.material), NBT_KEY_FUEL, fuel);
		ItemNBT.setInt(item, NBT_KEY_TIER, tier);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getDisplay(tier, fuel));
		meta.setLore(getLore(fuel, false));

		item.setItemMeta(meta);
		return item;
	}

	public void changeFuel(ItemStack item, int fuel, int change) {
		setFuel(item, fuel, fuel + change);
	}

	public void setFuel(ItemStack item, int fuel) {
		setFuel(item, ItemNBT.getInt(item, NBT_KEY_FUEL), fuel);
	}

	public void setFuel(ItemStack item, final int oldFuel, int fuel) {
		if (fuel >= Config.maxFuel) {
			fuel = Config.maxFuel;
		} else if (fuel < 0) {
			fuel = 0;
		}

		ItemNBT.setInt(item, NBT_KEY_FUEL, fuel);

		ItemMeta meta = item.getItemMeta();

		if (fuel == 0 || oldFuel == 0) {
			meta.setDisplayName(getDisplay(getTier(item), fuel));
		}

		List<String> lore = getLore(fuel, false);
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	public void changeTier(ItemStack item, int tier, int change) {
		tier += change;

		if (tier < 1) {
			tier = 1;
		}

		ItemNBT.setInt(item, NBT_KEY_TIER, tier);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getDisplay(tier, getFuel(item)));
		item.setItemMeta(meta);
	}

	public int getFuel(ItemStack item) {
		return ItemNBT.getInt(item, NBT_KEY_FUEL);
	}

	public int getTier(ItemStack item) {
		return ItemNBT.getInt(item, NBT_KEY_TIER);
	}

	public double getMultiplier(ItemStack item) {
		return (getTier(item) * Config.tierMultiplier) + 1;
	}

	public boolean isFuelRocket(ItemStack item) {
		return ItemNBT.hasKeys(item, NBT_KEY_TIER, NBT_KEY_FUEL);
	}

	public boolean isHubRocket(ItemStack item) {
		return item != null && ItemNBT.hasKeys(item, NBT_KEY_HUB);
	}

	public void markHub(ItemStack item) {
		ItemNBT.setBoolean(item, NBT_KEY_HUB, true);
	}

	public void markUnlimited(ItemStack item) {
		ItemNBT.setBoolean(item, NBT_KEY_UNLIM, true);
		ItemNBT.setInt(item, NBT_KEY_FUEL, Config.maxFuel);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getDisplay(getTier(item), Config.maxFuel));
		meta.setLore(getLore(0, true));

		item.setItemMeta(meta);
	}

	public boolean isUnlimited(ItemStack item) {
		return ItemNBT.getBoolean(item, NBT_KEY_UNLIM);
	}

	public void removeHubRockets(Player player) {
		boolean removed = false;
		for (ItemStack item : player.getInventory().getStorageContents()) {
			if (isHubRocket(item)) {
				player.getInventory().remove(item);
				removed = true;
			}
		}

		if (removed)
			player.updateInventory();
	}

	public void giveHubRocket(Player player) {
		ItemStack item = create(Config.hubModeTier, Config.hubModeFuel);
		markHub(item);

		player.getInventory().addItem(item);
		player.updateInventory();
	}

	private String getDisplay(int tier, int fuel) {
		String display = fuel > 0 ? Messages.normalRocket : Messages.depletedRocket;
		return display.replace("{tier}", toNumeral(tier));
	}

	private List<String> getLore(int fuel, boolean unlimited) {
		List<String> lore = Lists.newArrayList();

		if (unlimited) {
			lore.add(FormatUtil.format(Messages.baseColor + Messages.scaleFormat
					.replace("{bar}", Messages.highFuel + "Unlimited")));
			return lore;
		}

		switch (Messages.fuelDisplay) {
			case SCALE:
				StringBuilder bar = new StringBuilder();
				bar.append(getColor(fuel));

				int scale = 10;
				double percentage = (double) fuel / (double) Config.maxFuel;
				int bars = (int) Math.round(scale * percentage);

				for (int i = 0; i < bars; i++)
					bar.append(Messages.scaleCharacter);

				bar.append(ChatColor.RESET);
				bar.append(Messages.baseColor);

				int left = scale - bars;
				for (int i = 0; i < left; i++)
					bar.append(Messages.scaleCharacter);

				lore.add(FormatUtil.format(Messages.baseColor + Messages.scaleFormat
						.replace("{bar}", bar)
				));
				break;
			case NUMBER:
				lore.add(FormatUtil.format(Messages.baseColor + Messages.numberFormat
						.replace("{color}", getColor(fuel))
						.replace("{cells}", Integer.toString(fuel))
				));
				break;

		}

		return lore;
	}

	private String getColor(int fuel) {
		double percentage = (double) fuel / (double) Config.maxFuel;
		if (percentage > .66) {
			return Messages.highFuel;
		} else if (percentage > .33) {
			return Messages.mediumFuel;
		} else {
			return Messages.lowFuel;
		}
	}

	private String toNumeral(int number) {
		switch (number) {
			case 1: return "I";
			case 2: return "II";
			case 3: return "III";
			case 4: return "IV";
			case 5: return "V";
			case 6: return "VI";
			case 7: return "VII";
			case 8: return "VIII";
			case 9: return "IX";
			case 10: return "X";
			default: return Integer.toString(number);
		}
	}
}

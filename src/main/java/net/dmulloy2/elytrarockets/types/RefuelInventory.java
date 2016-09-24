/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.elytrarockets.types;

import net.dmulloy2.elytrarockets.ElytraRockets;
import net.dmulloy2.elytrarockets.config.Config;
import net.dmulloy2.elytrarockets.config.Messages;
import net.dmulloy2.elytrarockets.handlers.FuelHandler;
import net.dmulloy2.elytrarockets.handlers.ItemHandler;
import net.dmulloy2.util.FormatUtil;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author dmulloy2
 */
public class RefuelInventory implements InventoryHolder {
	private static final int ROCKET_SLOT = 10;
	private static final int FUEL_SLOT = 13;
	private static final int RESULT_SLOT = 16;

	private boolean finished = false;

	private final Player player;
	private final Inventory inventory;
	private final ElytraRockets plugin;

	public static void open(ElytraRockets plugin, Player player) {
		new RefuelInventory(plugin, player).open();
	}

	public RefuelInventory(ElytraRockets plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
		this.inventory = create();
	}

	public void open() {
		player.openInventory(inventory);
	}

	public Inventory create() {
		Inventory inventory = plugin.getServer().createInventory(this, InventoryType.CHEST,
				FormatUtil.replaceColors(Messages.refuelTitle));

		Material material = Material.STAINED_GLASS_PANE;
		DyeColor color = DyeColor.BLUE;

		// 1, 4, 7
		for (int index = 0; index < 27; index++) {
			int rel = index % 9;
			if (rel == 3 || rel == 4 || rel == 5) {
				color = DyeColor.BLACK;
			} else {
				color = DyeColor.BLUE;
			}

			@SuppressWarnings("deprecation")
			ItemStack item = new ItemStack(material, 1, color.getWoolData());
			if (index == 10 || index == 13 || index == 16) {
				item = null;
			}

			if (index == 1) setDisplay(item, Messages.refuelRocket);
			if (index == 4) setDisplay(item, Messages.refuelFuel);
			if (index == 7) setDisplay(item, Messages.refuelResult);

			inventory.setItem(index, item);
		}

		return inventory;
	}

	private void setDisplay(ItemStack item, String display) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(FormatUtil.replaceColors(display));
		item.setItemMeta(meta);
	}

	private void err(String message, Object... args) {
		player.sendMessage(FormatUtil.format(plugin.getCommandProps().getErrorPrefix() + message, args));
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	public ItemStack getRocket() {
		return inventory.getItem(ROCKET_SLOT);
	}

	public ItemStack getFuel() {
		return inventory.getItem(FUEL_SLOT);
	}

	public ItemStack getResult() {
		return inventory.getItem(RESULT_SLOT);
	}

	public boolean isValidClick(int slot) {
		return slot == ROCKET_SLOT || slot == FUEL_SLOT || slot == RESULT_SLOT;
	}

	public void handleClick(InventoryClickEvent event) {
		event.setCancelled(!onClick(event));
	}

	public boolean onClick(InventoryClickEvent event) {
		// They're clicking in their own inventory or outside the window,
		// don't worry about it

		if (!inventory.equals(event.getClickedInventory())) {
			return true;
		}

		int slot = event.getSlot();
		if (!isValidClick(slot)) {
			return false;
		}

		ItemStack item = event.getCursor();
 		ItemStack result = getResult();
 		ItemStack current = event.getCurrentItem();
 
 		// Allow them to take it back before refueling
 		if (!finished && item == null && current != null && current.getType() != Material.AIR) {
 			return true;
 		}

 		if ((current != null && current.getType() != Material.AIR) ||
 				(result != null && result.getType() != Material.AIR)) {
			return finished || slot == RESULT_SLOT;
		}

		if (item == null) {
			return false;
		}

		ItemHandler itemHandler = plugin.getItemHandler();
		FuelHandler fuelHandler = plugin.getFuelHandler();

		boolean isRocket = itemHandler.isFuelRocket(item);
		if (!isRocket) {
			err(Messages.refuelNotRocket);
			return false;
		}

		boolean isFuel = fuelHandler.isFuelCell(item);
		if (!isFuel) {
			err(Messages.refuelNotFuel);
			return false;
		}

		ItemStack rocket = getRocket();
		if (isRocket) {
			if (slot != ROCKET_SLOT || rocket != null || itemHandler.getFuel(item) == Config.maxFuel) {
				err(Messages.refuelInvalidSlot, "rocket");
				return false;
			}
		}

		ItemStack fuel = getFuel();
		if (isFuel) {
			if (slot != FUEL_SLOT || fuel != null || fuelHandler.getFuel(item) == 0) {
				err(Messages.refuelInvalidSlot, "fuel cell");
				return false;
			}
		}

		new BukkitRunnable() {
			@Override
			public void run() {
				finished = refuel();
			}
		}.runTaskLater(plugin, 20L);

		return true;
	}

	private boolean refuel() {
		ItemStack rocket = getRocket();
		ItemStack fuel = getFuel();
		ItemStack result = getResult();

		if (rocket == null || fuel == null || result != null) {
			return false;
		}

		ItemHandler itemHandler = plugin.getItemHandler();
		FuelHandler fuelHandler = plugin.getFuelHandler();

		if (!itemHandler.isFuelRocket(rocket) || !fuelHandler.isFuelCell(fuel)) {
			return false;
		}

		int rocketFuel = itemHandler.getFuel(rocket);
		int cells = fuelHandler.getFuel(fuel);
		if (cells == 0) {
			err(Messages.emptyCell);
			return false;
		}

		result = rocket.clone();
		rocket = null;

		itemHandler.changeFuel(result, rocketFuel, cells);
		fuelHandler.setFuel(fuel, 0);

		inventory.setItem(ROCKET_SLOT, rocket);
		inventory.setItem(FUEL_SLOT, fuel);
		inventory.setItem(RESULT_SLOT, result);
		return true;
	}
}
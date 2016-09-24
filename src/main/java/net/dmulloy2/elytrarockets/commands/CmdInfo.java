/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.elytrarockets.commands;

import net.dmulloy2.elytrarockets.ElytraRockets;
import net.dmulloy2.elytrarockets.types.Permission;
import net.dmulloy2.util.CompatUtil;
import net.dmulloy2.util.FormatUtil;

import org.bukkit.inventory.ItemStack;

import lombok.Getter;

/**
 * @author dmulloy2
 */
public class CmdInfo extends ElytraRocketsCommand {

	public CmdInfo(ElytraRockets plugin) {
		super(plugin);
		this.name = "info";
		this.description = "Shows information about your rocket";
		this.permission = Permission.CMD_INFO;
		this.mustBePlayer = true;
	}

	@Override
	public void perform() {
		ItemStack item = CompatUtil.getItemInMainHand(player);
		checkNotNull(item, "You must have an item in your hand!");
		checkArgument(plugin.getItemHandler().isFuelRocket(item), "Your in-hand item isn't a fuel rocket!");

		sendMessage("&3---- &eRocket Info &3----");

		RocketInfo info = new RocketInfo(item);
		sendMessage("Tier: &3{0}", info.getTier());

		if (info.isUnlim()) {
			sendMessage("Fuel: &3Unlimited");
		} else {
			sendMessage("Fuel: &3{0} {1}", info.getFuel(), FormatUtil.getPlural("cell", info.getFuel()));
		}

		if (info.isHub()) {
			sendMessage("Hub Rocket: &3true");
		}
	}

	@Getter
	public class RocketInfo {
		private int tier;
		private int fuel;
		private boolean hub;
		private boolean unlim;

		public RocketInfo(ItemStack item) {
			this.tier = plugin.getItemHandler().getTier(item);
			this.fuel = plugin.getItemHandler().getFuel(item);
			this.hub = plugin.getItemHandler().isHubRocket(item);
			this.unlim = plugin.getItemHandler().isUnlimited(item);
		}
	}
}
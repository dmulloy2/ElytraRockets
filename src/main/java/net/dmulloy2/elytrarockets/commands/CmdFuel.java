/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.elytrarockets.commands;

import net.dmulloy2.elytrarockets.ElytraRockets;
import net.dmulloy2.elytrarockets.handlers.ItemHandler;
import net.dmulloy2.elytrarockets.types.Permission;
import net.dmulloy2.util.InventoryUtil;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author dmulloy2
 */
public class CmdFuel extends ElytraRocketsCommand {

	public CmdFuel(ElytraRockets plugin) {
		super(plugin);
		this.name = "fuel";
		this.syntaxes = new SyntaxBuilder()
				.requiredArg("set")
				.requiredArg("fuel")
				.newSyntax()
				.requiredArg("give")
				.requiredArg("cells")
				.optionalArg("player")
				.build();
		this.description = "Manages rocket fuel";
		this.permission = Permission.CMD_FUEL;
	}

	@Override
	public void perform() {
		switch (args[0].toLowerCase()) {
			case "set":
				set();
				break;
			case "give":
				give();
				break;
			default:
				err("Invalid action \"&c{0}&4\"!", args[0]);
				break;
		}
	}

	private void set() {
		checkArgument(isPlayer(), "You must be a player to perform this command!");
		checkPermission(player, Permission.CMD_FUEL_SET);

		ItemHandler handler = plugin.getItemHandler();
		ItemStack rocket = player.getInventory().getItemInMainHand();
		checkArgument(rocket != null && handler.isFuelRocket(rocket), "You must havea rocket in your hand to do this!");

		int fuel = argAsInt(0, true);
		handler.setFuel(rocket, fuel);

		sendpMessage("Rocket fuel set to &b{0} &ecells.", handler.getFuel(rocket));
	}

	private void give() {
		checkPermission(player, Permission.CMD_FUEL_GIVE);

		int fuel = argAsInt(0, true);
		Player target = getPlayer(1, true);

		ItemStack item = plugin.getFuelHandler().create(fuel);
		if (InventoryUtil.giveItem(target, item).isEmpty()) {
			sendpMessage("You have given &b{0} &ea level &b{1} &efuel cell.", getName(sender, target, false), fuel);
		} else {
			err("No inventory space!");
		}
	}
}
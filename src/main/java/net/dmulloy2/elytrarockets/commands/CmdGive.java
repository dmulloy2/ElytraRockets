/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.elytrarockets.commands;

import net.dmulloy2.elytrarockets.ElytraRockets;
import net.dmulloy2.elytrarockets.config.Config;
import net.dmulloy2.elytrarockets.types.Permission;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author dmulloy2
 */
public class CmdGive extends ElytraRocketsCommand {

	public CmdGive(ElytraRockets plugin) {
		super(plugin);
		this.name = "give";
		this.addRequiredArg("player", "Player to give rocket to");
		this.addOptionalArg("tier", "Tier of the rocket");
		this.addOptionalArg("fuel", "Starting rocket fuel");
		this.description = "Gives a rocket to a player";
		this.permission = Permission.CMD_GIVE;
	}

	@Override
	public void perform() {
		Player player = getPlayer(0);

		int tier = Config.defaultTier;
		if (args.length > 1)
			tier = argAsInt(1, true);

		checkArgument(tier > 0, "Tier must be greater than &c0&4!");

		int maxTier = Config.maxTier;
		checkArgument(maxTier < 0 || tier <= maxTier, "Tier is greater than the maximum: &c{0}", maxTier);

		boolean unlimited = false;
		int fuel = Config.maxFuel;
		if (args.length > 2) {
			if (args[2].equalsIgnoreCase("unlimited"))
				unlimited = true;
			else
				fuel = argAsInt(2, true);
		}

		checkArgument(tier > 0, "Fuel must be greater than &c0!");

		int maxFuel = Config.maxFuel;
		checkArgument(maxFuel < 0 || fuel <= maxFuel, "Fuel is greater than the maximum: &c{0}", maxFuel);

		ItemStack rocket = plugin.getItemHandler().create(tier, fuel);

		if (unlimited)
			plugin.getItemHandler().markUnlimited(rocket);

		player.getInventory().addItem(rocket);
		player.updateInventory();

		String name = player.equals(sender) ? "yourself" : player.getName();
		sendpMessage("You have given &b{0} &ea tier &b{1} &erocket with &b{2} &efuel cells.", name, tier, fuel);
	}
}
/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.elytrarockets.commands;

import net.dmulloy2.elytrarockets.ElytraRockets;
import net.dmulloy2.elytrarockets.types.Permission;
import net.dmulloy2.elytrarockets.types.RefuelInventory;

/**
 * @author dmulloy2
 */
public class CmdRefuel extends ElytraRocketsCommand {

	public CmdRefuel(ElytraRockets plugin) {
		super(plugin);
		this.name = "refuel";
		this.description = "Refuel your rocket";
		this.permission = Permission.CMD_REFUEL;
		this.mustBePlayer = true;
	}

	@Override
	public void perform() {
		RefuelInventory.open(plugin, player);
	}
}
/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.elytrarockets.commands;

import net.dmulloy2.commands.Command;
import net.dmulloy2.elytrarockets.ElytraRockets;

/**
 * @author dmulloy2
 */
public abstract class ElytraRocketsCommand extends Command {
	protected final ElytraRockets plugin;

	public ElytraRocketsCommand(ElytraRockets plugin) {
		super(plugin);
		this.plugin = plugin;
		this.usesPrefix = true;
	}
}
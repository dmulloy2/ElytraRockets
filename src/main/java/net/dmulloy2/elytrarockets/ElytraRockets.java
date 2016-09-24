/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.elytrarockets;

import net.dmulloy2.SwornPlugin;
import net.dmulloy2.commands.CmdHelp;
import net.dmulloy2.commands.CmdReload;
import net.dmulloy2.elytrarockets.commands.CmdBuy;
import net.dmulloy2.elytrarockets.commands.CmdGive;
import net.dmulloy2.elytrarockets.commands.CmdInfo;
import net.dmulloy2.elytrarockets.commands.CmdRefuel;
import net.dmulloy2.elytrarockets.config.Config;
import net.dmulloy2.elytrarockets.config.Messages;
import net.dmulloy2.elytrarockets.handlers.FuelHandler;
import net.dmulloy2.elytrarockets.handlers.ItemHandler;
import net.dmulloy2.elytrarockets.listener.InventoryListener;
import net.dmulloy2.elytrarockets.listener.PlayerListener;
import net.dmulloy2.elytrarockets.types.Permission;
import net.dmulloy2.handlers.CommandHandler;
import net.dmulloy2.handlers.LogHandler;
import net.dmulloy2.handlers.PermissionHandler;
import net.dmulloy2.integration.VaultHandler;
import net.dmulloy2.util.ListUtil;
import net.dmulloy2.util.Util;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import lombok.Getter;

/**
 * @author dmulloy2
 */
public class ElytraRockets extends SwornPlugin {
	private @Getter ItemHandler itemHandler;
	private @Getter FuelHandler fuelHandler;

	private @Getter VaultHandler vaultHandler;

	@Override
	public void onLoad() {
		ElytraRocketsAPI.init(this);
	}

	@Override
	public void onEnable() {
		logHandler = new LogHandler(this);

		saveDefaultConfig();
		reloadConfig();

		permissionHandler = new PermissionHandler(this);
		commandHandler = new CommandHandler(this);

		getCommandProps().
			setReloadPerm(Permission.CMD_RELOAD);

		commandHandler.setCommandPrefix("rocket");
		commandHandler.registerPrefixedCommand(new CmdBuy(this));
		commandHandler.registerPrefixedCommand(new CmdHelp(this));
		commandHandler.registerPrefixedCommand(new CmdGive(this));
		commandHandler.registerPrefixedCommand(new CmdInfo(this));
		commandHandler.registerPrefixedCommand(new CmdRefuel(this));
		commandHandler.registerPrefixedCommand(new CmdReload(this));

		itemHandler = new ItemHandler();
		fuelHandler = new FuelHandler();

		try {
			vaultHandler = new VaultHandler(this);
		} catch (Throwable ex) {
		}

		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new InventoryListener(), this);
		pm.registerEvents(new PlayerListener(this), this);

		logHandler.log("Thank you for buying ElytraRockets by dmulloy2!");
	}

	@Override
	public void onDisable() {
	}

	@Override
	public void reloadConfig() {
		super.reloadConfig();
		Config.load(this);
		Messages.load(this);
	}

	@Override
	public void reload() {
		boolean hubMode = Config.hubModeEnabled;

		reloadConfig();

		if (hubMode && !Config.hubModeEnabled) {
			for (Player player : Util.getOnlinePlayers()) {
				if (ListUtil.containsIgnoreCase(Config.hubWorlds, player.getWorld().getName())) {
					itemHandler.removeHubRockets(player);
				}
			}
		}
	}

	public boolean isEconEnabled() {
		return vaultHandler != null && vaultHandler.isEconPresent();
	}
}
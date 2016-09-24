/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.elytrarockets.commands;

import net.dmulloy2.elytrarockets.ElytraRockets;
import net.dmulloy2.elytrarockets.config.Config;
import net.dmulloy2.elytrarockets.types.Permission;
import net.dmulloy2.integration.VaultHandler;

import org.bukkit.inventory.ItemStack;

/**
 * @author dmulloy2
 */
public class CmdBuy extends ElytraRocketsCommand {

	public CmdBuy(ElytraRockets plugin) {
		super(plugin);
		this.name = "buy";
		this.aliases.add("purchase");
		this.addOptionalArg("tier", "Tier of the rocket");
		this.description = "Buy an elytra rocket";
		this.permission = Permission.CMD_BUY;
		this.mustBePlayer = true;
	}

	@Override
	public void perform() {
		checkArgument(Config.econEnabled, "Buying rockets is disabled!");
		checkArgument(plugin.isEconEnabled(), "Vault economy is missing!");

		int tier = Config.defaultTier;
		if (args.length > 0)
			tier = argAsInt(0, true);

		checkArgument(tier > 0, "Tier must be greater than &c0&4!");

		int max = Config.maxTier;
		checkArgument(max < 0 || tier <= max, "&c{0} &4is higher than the maximum: &c{1}", tier, max);

		int cost = Config.baseCost + (tier * Config.tierCost);
		VaultHandler vault = plugin.getVaultHandler();
		checkArgument(vault.has(player, cost), "You need &c{0} &4to purchase this!", vault.format(cost));

		String result = vault.withdrawPlayer(player, cost);
		checkArgument(result == null, result);

		ItemStack rocket = plugin.getItemHandler().create(tier, Config.maxFuel);
		player.getInventory().addItem(rocket);
		player.updateInventory();
	}
}
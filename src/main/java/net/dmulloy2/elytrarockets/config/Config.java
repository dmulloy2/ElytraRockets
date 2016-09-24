/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.elytrarockets.config;

import java.util.List;

import net.dmulloy2.config.ConfigParser;
import net.dmulloy2.config.Key;
import net.dmulloy2.config.ValueOptions;
import net.dmulloy2.config.ValueOptions.ValueOption;
import net.dmulloy2.elytrarockets.ElytraRockets;

import org.bukkit.Material;

import com.google.common.collect.Lists;

/**
 * @author dmulloy2
 */
public class Config {

	public static void load(ElytraRockets plugin) {
		ConfigParser.parse(plugin, Config.class);
	}

	@Key("maxFuel")
	public static int maxFuel = 100;

	@Key("material")
	@ValueOptions(ValueOption.PARSE_MATERIAL)
	public static Material material = Material.FIREWORK;

	@Key("cellMaterial")
	@ValueOptions(ValueOption.PARSE_MATERIAL)
	public static Material cellMaterial = Material.FIREWORK_CHARGE;

	// ----- Tiers

	@Key("tier.default")
	public static int defaultTier = 1;

	@Key("tier.max")
	public static int maxTier = 10;

	@Key("tier.multiplier")
	public static double tierMultiplier = 0.25D;

	// ---- Mechanics

	@Key("mechanics.yOffset")
	public static double yOffset = 0.1D;

	@Key("mechanics.maxHeight")
	public static int maxHeight = 256;

	// ---- Activation

	@Key("activation.onInteract")
	public static boolean activateOnInteract = true;

	@Key("activation.onDrop")
	public static boolean activateOnDrop = true;

	// ---- Hub Mode

	@Key("hubMode.enabled")
	public static boolean hubModeEnabled = false;

	@Key("hubMode.unlimited")
	public static boolean hubFuelUnlimited = true;

	@Key("hubMode.tier")
	public static int hubModeTier = defaultTier;

	@Key("hubMode.fuel")
	public static int hubModeFuel = maxFuel;

	@Key("hubMode.worlds")
	public static List<String> hubWorlds = Lists.newArrayList();

	// ---- Econ

	@Key("econ.enabled")
	public static boolean econEnabled = true;

	@Key("econ.baseCost")
	public static int baseCost = 50_000;

	@Key("econ.tierCost")
	public static int tierCost = 25_000;
}
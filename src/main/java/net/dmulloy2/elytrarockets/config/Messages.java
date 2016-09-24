/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.elytrarockets.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import net.dmulloy2.config.ConfigParser;
import net.dmulloy2.config.Key;
import net.dmulloy2.config.ValueOptions;
import net.dmulloy2.config.ValueOptions.ValueOption;
import net.dmulloy2.elytrarockets.ElytraRockets;
import net.dmulloy2.util.FormatUtil;
import net.dmulloy2.util.Util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author dmulloy2
 */
public class Messages {
	private static final String FILE_NAME = "messages.yml";
	
	private static File file;
	private static YamlConfiguration config;

	public static void load(ElytraRockets plugin) {
		if (file == null) {
			file = new File(plugin.getDataFolder(), FILE_NAME);
		}
		if (!file.exists()) {
			plugin.saveResource(FILE_NAME, false);
		}
		config = new YamlConfiguration();
		try {
			config.load(file); 
		} catch (IOException | InvalidConfigurationException ex) {
			plugin.getLogHandler().log(Level.WARNING, Util.getUsefulStack(ex, "loading {0}", FILE_NAME));
			return;
		}
		ConfigParser.parse(plugin, config, Messages.class);
	}

	@Key("display.rockets.normal")
	@ValueOptions(ValueOption.FORMAT)
	public static String normalRocket = FormatUtil.format("&e&lElytra Rocket {tier}");

	@Key("display.rockets.depleted")
	@ValueOptions(ValueOption.FORMAT)
	public static String depletedRocket = FormatUtil.format("&c&lDepleted Rocket {tier}");

	@Key("display.fuel.type")
	@ValueOptions(ValueOption.PARSE_ENUM)
	public static FuelDisplay fuelDisplay = FuelDisplay.NUMBER;

	@Key("display.fuel.numberFormat")
	public static String numberFormat = "Fuel: {color}{cells} cells";

	@Key("display.fuel.scaleFormat")
	public static String scaleFormat = "Fuel: {bar}";

	@Key("display.fuel.scaleChar")
	public static String scaleCharacter = "&l|";

	@Key("display.cells.normal")
	@ValueOptions(ValueOption.FORMAT)
	public static String normalCell = FormatUtil.format("&a&lFuel Cell");

	@Key("display.cells.empty")
	@ValueOptions(ValueOption.FORMAT)
	public static String emptyCell = FormatUtil.format("&c&lEmpty Cell");

	@Key("display.cells.format")
	public static String cellFuelFormat = "Fuel: {cells} cells";

	@Key("display.colors.baseColor")
	public static String baseColor = "&7";

	@Key("display.colors.highFuel")
	public static String highFuel = "&a";

	@Key("display.colors.mediumFuel")
	public static String mediumFuel = "&e";

	@Key("display.colors.lowFuel")
	public static String lowFuel = "&c";

	@Key("display.refuel.title")
	public static String refuelTitle = "&8&nRefueling Station";

	@Key("display.refuel.rocket")
	public static String refuelRocket = "&e&lInsert Rocket";

	@Key("display.refuel.fuel")
	public static String refuelFuel = "&a&lInsert Fuel";

	@Key("display.refuel.result")
	public static String refuelResult = "&e&lTake Your Rocket";

	public static String refuelNotRocket = "You must supply a valid rocket!";

	public static String refuelNotFuel = "You must supply valid rockert fuel!";

	public static String refuelInvalidSlot = "You can't put your &c{0} &4there!";

	public static String refuelEmptyCell = "This fuel cell is empty!";
}
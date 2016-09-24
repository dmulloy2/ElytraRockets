/**
 * (c) 2016 dmulloy2
 */
package net.dmulloy2.elytrarockets.types;

import net.dmulloy2.types.IPermission;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dmulloy2
 */
@Getter
@AllArgsConstructor
public enum Permission implements IPermission {
	CMD_BUY("cmd.buy"),
	CMD_FUEL("cmd.fuel"),
	CMD_FUEL_SET("cmd.fuel.set"),
	CMD_FUEL_GIVE("cmd.fuel.give"),
	CMD_GIVE("cmd.give"),
	CMD_INFO("cmd.info"),
	CMD_REFUEL("cmd.refuel"),
	CMD_RELOAD("cmd.reload"),
	;

	private String node;
}
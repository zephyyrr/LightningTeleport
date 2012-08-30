package swe.kbk.LightningTeleport;

import org.bukkit.Bukkit;
import org.bukkit.Server;

public class LTPParser {
	
	private static Server server = Bukkit.getServer();
	
	public static Destination parse(String[] args) {
		
		if (args.length > 3) {
			return parse3Args(args);
		}
		
		switch (args.length) {
		case 3: return parse3Args(args);
		case 2: return parse2Args(args);
		case 1: return parse1Args(args);
		default: return Destination.NIL;
		}
	}

	private static Destination parse3Args(String[] args) {
		if (isNumber(args[0]) && isNumber(args[1]) && isNumber(args[2])) {
			return Destination.XYZCOORDINATES;
		}
		return parse2Args(args);
	}

	private static Destination parse2Args(String[] args) {
		if (isNumber(args[0]) && isNumber(args[1])) {
			return Destination.XZCOORDINATES;
		}
		return parse1Args(args);
	}

	private static Destination parse1Args(String[] args) {
		if (args[0].toLowerCase().equals("spawn")) {
			return Destination.SPAWN;
		}
		
		if (server != null && server.getWorld(args[0]) != null) {
			return Destination.WORLD;
		}
		
		if (server != null && server.getPlayer(args[0]) != null) {
				return Destination.PLAYER;
		}
		return Destination.NIL;
	}

	private static boolean isNumber(String s) {
		if (s.toLowerCase().equals("nan")) {
			return false;
		}
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}

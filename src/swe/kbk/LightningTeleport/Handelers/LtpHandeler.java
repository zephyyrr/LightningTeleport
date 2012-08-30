/**
 * 
 */
package swe.kbk.LightningTeleport.Handelers;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import swe.kbk.LightningTeleport.LTPParser;
import swe.kbk.LightningTeleport.LightningTeleport;

/**
 * @author johan
 *
 */
public class LtpHandeler implements CommandExecutor {

	public static final int DROPDISTANCE = 20;
	LightningTeleport plugin;

	public LtpHandeler(LightningTeleport plugin) {
		this.plugin = plugin;
	}

	/* (non-Javadoc)
	 * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLable,
			String[] args) {

		if (hasPermission(sender)) {
			Player p = (Player) sender;
			switch (LTPParser.parse(args)) {
			case SPAWN: isSpawn(p); break;
			case PLAYER: isPlayer(p, args[0]); break;
			case XYZCOORDINATES: isXYZCoordinates(p, args[0], args[1], args[2]); break;
			case XZCOORDINATES: isXZCoordinates(p, args[0], args[1]); break;
			case WORLD: isWorld(p, args[0]); break;
			case NIL: isNil(sender); return false;
			default: isDefault(sender); break;
			}
			return true;

		} else if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage("This command only makes sense for players.");
			return true;
		}
		return false;
	}

	private void isSpawn(Player sender) {
		teleport(sender, sender.getWorld().getSpawnLocation());
	}

	private void isPlayer(Player sender, String name) {
		Location loc = plugin.getServer().getPlayer(name).getLocation();
		loc.add(loc.getDirection().multiply(5));
		float rotation = 0;
		if (loc.getYaw() < 0) {
			rotation = 180;
		} else {
			rotation = -180;
			
		}
		loc.setYaw(loc.getYaw()+rotation);
		teleport(sender, loc);
	}

	private void isXYZCoordinates(Player sender, String sx,
			String sy, String sz) {
		double y = toDouble(sy);
		Location loc = new Location(sender.getWorld(), toDouble(sx), (y > 0) ? y : 0, toDouble(sz));
		teleport(sender, loc);
	}

	private void isXZCoordinates(Player sender, String sx,
			String sz) {
		double x = toDouble(sx);
		double z = toDouble(sz);
		Location loc = new Location(sender.getWorld(), x, getGround(x, z, sender.getWorld()), z);
		teleport(sender, loc);
	}

	private void isWorld(Player sender, String name) {
		teleport(sender, plugin.getServer().getWorld(name).getSpawnLocation());
	}

	private void isNil(CommandSender sender) {
		sender.sendMessage("I have no idea of where you want to go.");
	}

	private void isDefault(CommandSender sender) {
		sender.sendMessage("Not implemented yet!");
	}

	private void teleport(Player player, Location tpLoc) {
		//save flightstatus and set it to false;
		GameMode gm = player.getGameMode();
		player.setGameMode(GameMode.SURVIVAL);
		setSafeSpot(tpLoc);

		Vector offset = getSafeOffset(tpLoc);
		Location offsettedLocation = tpLoc.clone().add(offset);
		
		player.teleport(offsettedLocation);

		//Schedule strikes
		BukkitScheduler sched = plugin.getServer().getScheduler();
		DropLightningTask task = new DropLightningTask(player, tpLoc);
		int id = sched.scheduleSyncRepeatingTask(plugin, task, 5, 5);
		task.setId(id);

		//restore the power of flight
		player.setGameMode(gm);
	}



	private Vector getSafeOffset(Location tpLoc) {
		double roofHeight =  getRoofHeight(tpLoc);
		if (roofHeight > DROPDISTANCE) {
			return new Vector(0,DROPDISTANCE,0);
		} else {
			return new Vector(0,roofHeight,0);
		}
	}

	private void setSafeSpot(Location tpLoc) {
		while (tpLoc.getY() < 256) {
			if (tpLoc.add(0, 1, 0).getBlock().getTypeId() == 0) {
				break;
			}
		}
	}

	private double getRoofHeight(Location tpLoc) {
		int height = 0;
		Location testLoc = tpLoc.clone();
		//Needs to check blocks above to the highest layer
		final int maxHeight = testLoc.getWorld().getMaxHeight() - tpLoc.getBlockY();
		while (height < maxHeight) {
			if (testLoc.add(0, 1, 0).getBlock().getTypeId() != 0) {
				return height-1;
			}
			height++;
		}
		return maxHeight;
	}

	private double toDouble(String s) {
		try {
			return Double.parseDouble(s);
		} catch (Exception e) {
			return 0;
		}
	}

	private double getGround(double x, double z, World world) {
		int height = world.getMaxHeight();
		Location testLoc = new Location(world, x, height, z);
		while (testLoc.getY() > 0) {
			if (testLoc.subtract(0, 1, 0).getBlock().getTypeId() != 0) {
				return testLoc.getY()+1;
			}
		}
		return 0;
	}

	private boolean hasPermission(CommandSender sender) {
		return (sender instanceof Player && ((Player) sender).hasPermission("LightningTeleport.tp"));
	}

}

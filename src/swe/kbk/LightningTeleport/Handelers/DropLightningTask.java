package swe.kbk.LightningTeleport.Handelers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class DropLightningTask implements Runnable {
	
	private static final double HEIGHTTOBELOW = 2.0;
	private static final int MAXZAPS = 30;
	private static final double MAXDIFFSQUARED = 0.2f;
	final Player player;
	final Location destination;
	final World world;
	private Integer id;
	private int amount;
	
	private Location lastPlace;
	
	
	public DropLightningTask(Player dropper, Location dest) {
		player = dropper;
		destination = dest;
		world = destination.getWorld();
		id = null;
	}

	@Override
	public void run() {
		if (!onGround(player) && amount < MAXZAPS) {
			world.strikeLightningEffect(destination);
			amount++;
		} else if (id != null) {
			Bukkit.getScheduler().cancelTask(id);
		}
	}
	
	private boolean onGround(Player player) {
		Block below = player.getLocation().subtract(0, HEIGHTTOBELOW, 0).getBlock();
		return below.getTypeId() != 0;
	}
	
	private boolean stoppedMoving(Player player) {
		Location diff = player.getLocation().clone().subtract(lastPlace);
		return (diff.lengthSquared() < MAXDIFFSQUARED);
	}

	public void setId(int id) {
		this.id = id;
	}

}

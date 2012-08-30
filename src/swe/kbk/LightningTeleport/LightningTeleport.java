package swe.kbk.LightningTeleport;

import org.bukkit.plugin.java.JavaPlugin;

import swe.kbk.LightningTeleport.Handelers.LtpHandeler;

public class LightningTeleport extends JavaPlugin {

	public void onEnable() {
		getCommand("lightningtp").setExecutor(new LtpHandeler(this));
	}

	public void onDisable() {
		//What is there to do?
	}

}

package main;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Hellmode extends JavaPlugin implements Listener {
	
	/*
	 * all player information is saved inside config.yml
	 * more features and input arguments can be added in the future if needed
	 * 
	 * current config file structure:
	 * 
	 * <PLAYER DISPLAY NAME>
	 *   hellmode: <boolean>
	 *   lasttoggle <date and time represented as dd/MM/yyyy HH:mm:ss>
	 */
	
	public FileConfiguration config;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	
	@Override
	public void onEnable() {
		getLogger().info("loading hellmode, created by EggyRepublic!");
		config = this.getConfig();
		getServer().getPluginManager().registerEvents(this, this);
		loadConfiguration();
		this.getCommand("hellmode").setExecutor(new Commands(this));
		getLogger().info("successfully loaded hellmode!");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("successfully unloaded hellmode, thank you com again");
		this.saveConfig();
	}
	
    public void loadConfiguration() {
        config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    //HELPER FUNCTIONS
    
    public void toggleHellmode(Player p) {
    	if (!isHellmode(p)) {
    		//if not already Hellmode, turn it on by turning anti-regen on
    		
    		//sets hellmode and lasttoggle fields in config
    		config.set(p.getDisplayName() + ".hellmode", true);
    		config.set(p.getDisplayName() + ".lasttoggle", formatter.format(new Date()));
			this.saveConfig();
			
			//turns on antiregen
			antiregenOn(p);
    	} else {
    		//turning Hellmode off
    		
    		//sets hellmode and lasttoggle fields in config
    		config.set(p.getDisplayName() + ".hellmode", false);
    		config.set(p.getDisplayName() + ".lasttoggle", formatter.format(new Date()));
			this.saveConfig();
			
			//turns off antiregen
			antiregenOff(p);
    	}
    }
    
    /*
     * checks configuration file for player's hellmode status (boolean)
     * returns false if player not found
     */
    public boolean isHellmode(Player p) {
    	return config.getBoolean(p.getDisplayName() + ".hellmode");
    }
    
    /*
     * checks the last time a player have toggled hellmode and the current status in the config
     */
    public String checkToggle(Player p) {
    	if (isHellmode(p)) {
    		return p.getDisplayName() + " last toggled on hellmode on " + config.getString(p.getDisplayName() + ".lasttoggle");
    	} else {
    		return p.getDisplayName() + " last toggled off hellmode on " + config.getString(p.getDisplayName() + ".lasttoggle");
    	}
    }
    
    
    //ASSORTED FEATURES
    
    public void antiregenOn(Player p) {
    	p.sendMessage("toggled hellmode on for " + p.getName());
		p.setSaturatedRegenRate(Integer.MAX_VALUE);
		p.setUnsaturatedRegenRate(Integer.MAX_VALUE);
    }
    
    public void antiregenOff(Player p) {
    	p.sendMessage("toggled hellmode off for " + p.getName());
		p.setSaturatedRegenRate(20);
		p.setUnsaturatedRegenRate(80);
    }
    
    
    //EVENTS
    
    @EventHandler
    public void joinEvent(PlayerJoinEvent event) {
    	Player p = event.getPlayer();
    	p.sendMessage("hellmode: " + isHellmode(p));
    	p.sendMessage("hellmode enables anti-regen");
    	checkVar(p);
    }

    /*
     * cancels all regeneration events
     */
    @EventHandler
    public void healEventBlock(EntityRegainHealthEvent event) {
    	Entity entity = event.getEntity();
    	if (entity instanceof Player) {
    		Player player = (Player) entity;
    		if (isHellmode(player)) {
	    		event.setCancelled(true);
    		}
    	}
    }
    
    /*
     * ensures consistency between player in game status and config file status
     */
    public void checkVar(Player p) {
    	if (isHellmode(p)) {
    		antiregenOn(p);
    	} else {
    		antiregenOff(p);
    	}
    }
}
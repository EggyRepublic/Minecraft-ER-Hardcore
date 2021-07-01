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

public class Main extends JavaPlugin implements Listener {
	
	public FileConfiguration config;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
	
	@Override
	public void onEnable() {
		getLogger().info("loading anti-heal, created by EggyRepublic!");
		config = this.getConfig();
		getServer().getPluginManager().registerEvents(this, this);
		loadConfiguration();
		this.getCommand("hardcore").setExecutor(new Commands(this));
		getLogger().info("successfully loaded anti-heal!");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("successfully unloaded anti-heal, thank you com again");
		this.saveConfig();
	}
	
    public void loadConfiguration() {
        config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    /*
     * changes player natural regen rate based on "isHardcore" status
     */
    public void toggleHardcore(Player p) {
    	if (!isHardcore(p)) {
    		config.set(p.getDisplayName() + ".hardcore", true);
    		config.set(p.getDisplayName() + ".lasttoggle", formatter.format(new Date()));
    		p.sendMessage("toggled anti-regen on for " + p.getName());
    		p.setSaturatedRegenRate(Integer.MAX_VALUE);
			p.setUnsaturatedRegenRate(Integer.MAX_VALUE);
			this.saveConfig();
    	} else {
    		config.set(p.getDisplayName() + ".hardcore", false);
    		config.set(p.getDisplayName() + ".lasttoggle", formatter.format(new Date()));
    		p.sendMessage("toggled anti-regen off for " + p.getName());
    		p.setSaturatedRegenRate(20);
			p.setUnsaturatedRegenRate(80);
			this.saveConfig();
    	}
    }
    
    /*
     * ensures regen rate are set to correct values
     */
    public void affirmHardcore(Player p) {
    	if (isHardcore(p)) {
    		p.setSaturatedRegenRate(Integer.MAX_VALUE);
			p.setUnsaturatedRegenRate(Integer.MAX_VALUE);
    	} else {
    		p.setSaturatedRegenRate(20);
			p.setUnsaturatedRegenRate(80);
    	}
    }
    
    /*
     * checks configuration file for player's boolean value
     */
    public boolean isHardcore(Player p) {
    	return config.getBoolean(p.getDisplayName() + ".hardcore");
    }
    
    public String checkStatus(Player p) {
    	if (isHardcore(p)) {
    		return p.getDisplayName() + " last toggled on anti-regen on " + config.getString(p.getDisplayName() + ".lasttoggle");
    	}
    	return p.getDisplayName() + " last toggled off anti-regen on " + config.getString(p.getDisplayName() + ".lasttoggle");
    }
    
    @EventHandler
    public void join(PlayerJoinEvent event) {
    	Player p = event.getPlayer();
    	p.sendMessage("anti-regen : " + isHardcore(p));
    	affirmHardcore(p);
    }

    /*
     * cancels all regen 
     */
    @EventHandler
    public void heal(EntityRegainHealthEvent event) {
    	Entity entity = event.getEntity();
    	if (entity instanceof Player) {
    		Player player = (Player) entity;
    		if (isHardcore(player)) {
	    		event.setCancelled(true);
    		}
    	}
    }
}
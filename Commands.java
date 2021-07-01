package main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	
	Main m;
	
	public Commands(Main m) {
		this.m = m;
	}

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if (label.equalsIgnoreCase("hardcore")) {
    		if (args.length == 1) {
    			String s = args[0];
    			Player p = Bukkit.getPlayer(s);
    			if (p != null) {
    				m.toggleHardcore(p);
    				return true;
    			} else {
    				sender.sendMessage("player not found");
    				return false;
    			}
    		} 
    		
    		if (args.length == 2 && args[0].equalsIgnoreCase("info")) {
    			String s = args[1];
    			Player p = Bukkit.getPlayer(s);
    			if (p != null) {
    				String stat = m.checkStatus(p);
    				sender.sendMessage(stat);
    				return true;
    			} else {
    				sender.sendMessage("player not found");
    				return false;
    			}
    		}
    	}
    	return false;
    }
}
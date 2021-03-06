package me.elgamer.earthserver.commands.navigation;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.elgamer.earthserver.Main;
import me.elgamer.earthserver.sql.LocationSQL;

public class Requests implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage("This command cannot be sent from the console!");
			return false;
		}

		Player p = (Player) sender;

		if (!(p.hasPermission("earthserver.location.requests"))) {
			p.sendMessage(ChatColor.RED + "You do not have permission for this command!");
			return true;
		}

		LocationSQL locationSQL = Main.getInstance().locationData;
		
		if (!(locationSQL.requestExists())) {
			p.sendMessage(ChatColor.RED + "There are no location requests");
			return true;
		}

		int i = 0;

		HashMap<String, Location> requests = locationSQL.getRequests();
		p.sendMessage(ChatColor.GREEN + "Requests:");
		if (requests.size() > 9) {

			for (Entry<String, Location> entry : requests.entrySet()) {

				i++;

				if (i == 9) {
					break;
				}
				p.sendMessage(ChatColor.GRAY + entry.getKey());

			}

		} else {
			for (Entry<String, Location> entry : requests.entrySet()) {

				p.sendMessage(ChatColor.GRAY + entry.getKey());

			}
		}


		return true;
	}

}

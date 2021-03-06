package me.elgamer.earthserver.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.elgamer.earthserver.Main;
import me.elgamer.earthserver.sql.MemberData;
import me.elgamer.earthserver.sql.OwnerData;
//import me.elgamer.earthserver.sql.PlayerData;
import me.elgamer.earthserver.sql.RegionData;
import me.elgamer.earthserver.sql.RegionLogs;
import me.elgamer.earthserver.sql.RequestData;

public class User {

	public Player p;
	public String uuid;
	public String name;
	public String builder_role;

	public String current_region;
	public boolean hasWorldEdit;

	public World current_world;

	public int buildingTime;

	public String region_requester;
	public String region_name;
	public int gui_slot;
	public int gui_page;
	public boolean staff_request;
	
	public String previous_gui;
	
	public String member_name;

	public User(Player p) {
		this.p = p;
		uuid = p.getUniqueId().toString();
		name = p.getName();

		//Rather than use building time from this plugin, use the points plugin.
		//this.buildingTime = PlayerData.getBuildingTime(uuid);
		this.buildingTime = me.elgamer.btepoints.utils.PlayerData.getBuildTime(uuid);

		current_world = p.getWorld();

		if (current_world.getName().equals(Main.getInstance().getConfig().getString("World_Name"))) {
			current_region = getRegion(p);
		} else {
			current_region = "buildhub";
		}

		if (p.hasPermission("group.builder")) {
			builder_role = "builder";
			if (!(current_region.equals("buildhub"))) {
				hasWorldEdit = updatePerms(this, current_region);
			}
		} else if (p.hasPermission("group.jrbuilder")) {
			builder_role = "jrbuilder";
			if (!(current_region.equals("buildhub"))) {
				hasWorldEdit = updatePerms(this, current_region);
			}
		} else if (p.hasPermission("group.apprentice")) {
			builder_role = "apprentice";
		} else {
			builder_role = "guest";
		}

	}

	public static String getRegion(Player p) {
		Location l = p.getLocation();
		double x = l.getX();
		double z = l.getZ();
		int rX = (int) Math.floor((x/512));
		int rZ = (int) Math.floor((z/512));
		return (rX + "," + rZ);
	}
	
	public static String getRegion(Location l) {
		double x = l.getX();
		double z = l.getZ();
		int rX = (int) Math.floor((x/512));
		int rZ = (int) Math.floor((z/512));
		return (rX + "," + rZ);
	}

	public static boolean updatePerms(User u, String region) {

		RegionData regionData = Main.getInstance().regionData;
		OwnerData ownerData = Main.getInstance().ownerData;
		MemberData memberData = Main.getInstance().memberData;
		RegionLogs regionLogs = Main.getInstance().regionLogs;
		RequestData requestData = Main.getInstance().requestData;
		
		if (regionData.isLocked(region)) {
			if (u.hasWorldEdit) {
				Permissions.removeWorldedit(u.uuid);
			}
			return false;
		}

		if (regionData.isOpen(region)) {
			
			if (!u.hasWorldEdit) {
				Permissions.giveWorldedit(u.uuid);
			}
			return true;
		} else if (ownerData.isOwner(u.uuid, region)) {
			if (!u.hasWorldEdit) {
				Permissions.giveWorldedit(u.uuid);
			}
			ownerData.updateTime(u.uuid, region);
			return true;
		} else if (memberData.isMember(u.uuid, region)) {

			if (!u.hasWorldEdit) {
				Permissions.giveWorldedit(u.uuid);
				memberData.updateTime(u.uuid, region);
			}

			if (!(ownerData.hasOwner(region))) {
				ownerData.addOwner(region, u.uuid);
				memberData.removeMember(region, u.uuid);
				regionLogs.closeLog(region, u.uuid);
				regionLogs.newLog(region, u.uuid, "owner");
				requestData.updateRegionOwner(region, u.uuid);
			}

			return true;
		} else {
			if (u.hasWorldEdit) {
				Permissions.removeWorldedit(u.uuid);
			}
			return false;
		}

	}
	
	public static void updateRole(User u) {
		if (u.p.hasPermission("group.builder")) {
			u.builder_role = "builder";
			if (!(u.current_region.equals("buildhub"))) {
				u.hasWorldEdit = updatePerms(u, u.current_region);
			}
		} else if (u.p.hasPermission("group.jrbuilder")) {
			u.builder_role = "jrbuilder";
			if (!(u.current_region.equals("buildhub"))) {
				u.hasWorldEdit = updatePerms(u, u.current_region);
			}
		} else if (u.p.hasPermission("group.apprentice")) {
			u.builder_role = "apprentice";
		} else {
			u.builder_role = "guest";
		}
	}

}

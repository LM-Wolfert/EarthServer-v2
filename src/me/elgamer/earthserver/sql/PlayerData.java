package me.elgamer.earthserver.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.elgamer.earthserver.Main;
import me.elgamer.earthserver.utils.Time;
import me.elgamer.earthserver.utils.User;

public class PlayerData {

	//Create player instance
	public static boolean addPlayer(User u) {

		Main instance = Main.getInstance();

		try {
			PreparedStatement statement = instance.getConnection().prepareStatement
					("INSERT INTO " + instance.playerData + " (UUID,NAME,BUILDER_ROLE,LAST_ONLINE,BUILDING_TIME) VALUE (?,?,?,?,?)");
			statement.setString(1, u.uuid);
			statement.setString(2, u.name);
			statement.setString(3, u.builder_role);
			statement.setLong(4, Time.currentTime());
			statement.setInt(5, 0);

			statement.executeUpdate();

			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	//Check is player has instance in table
	public static boolean hasPlayer(User u) {

		Main instance = Main.getInstance();

		try {
			PreparedStatement statement = instance.getConnection().prepareStatement
					("SELECT * FROM " + instance.playerData + " WHERE UUID=?");
			statement.setString(1, u.uuid);

			ResultSet results = statement.executeQuery();

			if (results.next()) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;

	}
	
	//Update player data
	public static void updatePlayer(User u) {
		
		Main instance = Main.getInstance();

		try {
			PreparedStatement statement = instance.getConnection().prepareStatement
					("UPDATE " + instance.playerData + " SET NAME=?,BUILDER_ROLE=?,LAST_ONLINE=?,BUILDING_TIME=? WHERE UUID=?");
			statement.setString(1, u.name);
			statement.setString(2, u.builder_role);
			statement.setLong(3, Time.currentTime());
			statement.setString(4, u.uuid);
			statement.setInt(5, u.buildingTime);
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	//Get building time
	public static int getBuildingTime(String uuid) {
		
		Main instance = Main.getInstance();

		try {
			PreparedStatement statement = instance.getConnection().prepareStatement
					("SELECT * FROM " + instance.playerData + " WHERE UUID=?");
			statement.setString(1, uuid);

			ResultSet results = statement.executeQuery();
			
			if (results.next()) {
				return (results.getInt("BUILDING_TIME"));
			} else {
				return 0;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}		
		
	}

}
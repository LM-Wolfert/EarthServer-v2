package me.elgamer.earthserver.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import me.elgamer.earthserver.Main;
import me.elgamer.earthserver.utils.OldClaim;

public class RegionData {

	public static boolean hasEntry() {

		Main instance = Main.getInstance();

		try {
			PreparedStatement statement = instance.getConnection().prepareStatement
					("SELECT * FROM " + instance.regionData);

			ResultSet results = statement.executeQuery();

			return (results.next());

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}			
	}

	public static void convertRegions(ArrayList<OldClaim> claims) {

		Main instance = Main.getInstance();

		PreparedStatement statement;

		int x;
		int z;

		try {
			
			for (OldClaim claim : claims) {

				statement = instance.getConnection().prepareStatement
						("INSERT INTO " + instance.regionData + " (REGION_ID,REGION_X,REGION_Z,PUBLIC,LOCKED,OPEN) VALUE (?,?,?,?,?,?)");
				statement.setString(1, claim.region);

				String[] region = claim.region.split(",");
				x = Integer.parseInt(region[0]);
				z = Integer.parseInt(region[1]);

				statement.setInt(2, x);
				statement.setInt(3, z);
				
				statement.setBoolean(4, claim.public_private);
				statement.setBoolean(5, false);
				statement.setBoolean(6, false);

				statement.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}	

	}
	
	public static boolean isOpen(String region) {
		
		Main instance = Main.getInstance();

		try {
			PreparedStatement statement = instance.getConnection().prepareStatement
					("SELECT * FROM " + instance.regionData + " WHERE REGION_ID=?,OPEN=?");
			statement.setString(1, region);
			statement.setBoolean(2, true);
			
			ResultSet results = statement.executeQuery();

			return (results.next());

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}	
		
	}



}
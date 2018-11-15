import java.sql.*;
import java.util.ArrayList;

public class Matching {
		ConnectionMaker forcreation = new ConnectionMaker();
		//Matching useguy=new Matching();//not populated used for method calls
	private Rider getriderDestination(int input) {
		java.sql.Connection con = forcreation.MakeConnection();
		Rider returnthing = new Rider();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from riderpost WHERE postID = "+input);
			
			if(rs.next()){
			returnthing.destination=rs.getString("DestinationID");
			returnthing.riderId=rs.getInt("RiderID");
			//System.out.println( "getriderdestination debug");
			//System.out.println(returnthing.destination + " + " + rs.getString("DestinationID"));
			}
			else 
				return null;
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			con.close();
		}catch (SQLException e) {
			//System.out.println("get Rider Destination failed to close connection");
			e.printStackTrace();
		}
		return returnthing;
	}
	
	ArrayList<Driver> getdrivers(Rider stats){
		java.sql.Connection con = forcreation.MakeConnection();
		
		
		ArrayList<Driver> returninglist = new ArrayList<Driver>();
		
		
		String query = "select * from driverpost where DestinationID='" + stats.destination+"'" +"AND Date="+stats.date+"";
		if(stats.destination==null)
			return null;
		//System.out.println(query);
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Driver temp=new Driver();
				temp.date=rs.getString("Date");
				temp.destination=rs.getString("DestinationID");
				temp.driverId=rs.getInt("driverID");
				temp.seats=rs.getInt("Seats");
				temp.time=rs.getString("Time");
				temp.vehicle=rs.getString("Vehicle");
				temp.tripId=rs.getInt("TripId");
				returninglist.add(temp);
			}
		} catch (SQLException e) {
			//System.out.println("get driverpost failed");
			e.printStackTrace();
		}
		return returninglist;
	}
	
	 boolean Match(int postid) {	
		Matching useguy=new Matching();
		//java.sql.Connection con = forcreation.MakeConnection();
		Rider rider=new Rider();
		rider = useguy.getriderDestination(postid);
		if (rider==null) //if that postid has already been matched;
			return true;
		
		//System.out.println(rider.destination);
		ArrayList<Driver> driver = useguy.getdrivers(rider);
		if (driver.isEmpty())
			return false;
		Driver guy= driver.get(0);
		try {
			//System.out.println(guy.destination);
		insert(guy.tripId,rider.riderId, guy.driverId, guy.destination);
		return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	private void insert(int tripID, int riderID, int driverID, String destination) {
		java.sql.Connection con = forcreation.MakeConnection();
//		
//		Rider rider;
//		Driver driver;
//		
//		destination = driver.destination;
//		riderID = rider.riderId;
//		driverID = driver.driverId;
//		tripID = driver.tripId;
//		
	
		String sql = "insert into matching(TripID, DriverID, RiderID, DestinationID) VALUES (?,?,?,?)";
		//System.out.println(sql);
		
		try (
			PreparedStatement pstmt = con.prepareStatement(sql)){
			pstmt.setInt(1, tripID);
			pstmt.setInt(3, riderID);
			pstmt.setInt(2, driverID);
			pstmt.setString(4, destination);
			pstmt.executeUpdate();
		} catch (SQLException e) {
		System.out.println(e.getMessage());
		}
	}
}

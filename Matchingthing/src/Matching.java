import java.sql.*;
import java.util.ArrayList;

public class Matching {
		ConnectionMaker forcreation = new ConnectionMaker();

	
	private Rider getriderDestination(int input) {
		java.sql.Connection con = forcreation.MakeConnection();
		Rider returnthing = new Rider();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from riderpost WHERE postID in (select major from profile where idProfile=)"+input);
			
			rs.next();
			returnthing.destination=rs.getString("DestinationID");
			returnthing.riderId=rs.getInt("RiderID");
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			con.close();
		}catch (SQLException e) {
			System.out.println("get Rider Destination failed to close connection");
			e.printStackTrace();
		}
		return returnthing;
	}
	
	ArrayList<Driver> getdrivers(String destination, String date){
		java.sql.Connection con = forcreation.MakeConnection();
		
		
		ArrayList<Driver> returninglist = new ArrayList<Driver>();
		
		
		String query = "select * from driverpost where DestinationID='" + destination+"' and Date =" +date;
		if(destination==null)
			return returninglist;
		System.out.println(query);
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Driver temp=new Driver();
				temp.date=rs.getString("Date");
				temp.destination=destination;
				temp.driverId=rs.getInt("driverID");
				temp.seats=rs.getInt("Seats");
				temp.time=rs.getString("Time");
				temp.vehicle=rs.getString("Vehicle");
				temp.tripId=rs.getInt("TripId");
				returninglist.add(temp);
			}
		} catch (SQLException e) {
			System.out.println("get driverpost failed");
			e.printStackTrace();
		}
		return returninglist;
	}
	
	 boolean Match(int postid) {	
		Matching useguy=new Matching();
		//java.sql.Connection con = forcreation.MakeConnection();
		Rider rider= useguy.getriderDestination(postid) ;
		ArrayList<Driver> driver = useguy.getdrivers(rider.destination, rider.date);
		if (driver.isEmpty())
			return false;
		Driver guy= driver.get(0);
		try {
			System.out.println(guy.destination);
		insert(guy.tripId,rider.riderId, guy.driverId, guy.destination, guy.date);
		return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	private void insert(int tripID, int riderID, int driverID, String destination, String Date) {
		java.sql.Connection con = forcreation.MakeConnection();

	
		String sql = "insert into matching(TripID, DriverID, RiderID, DestinationID, Date) VALUES (?,?,?,?,?)";
		System.out.println(sql);
		
		try (
			PreparedStatement pstmt = con.prepareStatement(sql)){
			pstmt.setInt(1, tripID);
			pstmt.setInt(3, riderID);
			pstmt.setInt(2, driverID);
			pstmt.setString(4, destination);
			pstmt.setString(5, Date);
			pstmt.executeUpdate();
		} catch (SQLException e) {
		System.out.println(e.getMessage());
		}
	}
}
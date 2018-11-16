import java.sql.*;
import java.util.ArrayList;

public class Matching {
		ConnectionMaker forcreation = new ConnectionMaker();

	
	private Rider getrider(int input) {
		java.sql.Connection con = forcreation.MakeConnection();
		Rider returnthing = new Rider();
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from riderpost where postID =" +input);
			
			rs.next();
			returnthing.destination=rs.getString("DestinationID");
			returnthing.riderId=rs.getInt("RiderID");
			returnthing.date=rs.getString("Date");
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			con.close();
		}catch (SQLException e) {
			System.out.println("get Rider failed to close connection");
			e.printStackTrace();
		}
		return returnthing;
	}
	
	private String getMajor(int idProf) {
		java.sql.Connection con = forcreation.MakeConnection();
		String major = new String();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from profile where idProfile='"+idProf+"'");
			
			rs.next();
			major=rs.getString("Major");
			
		} catch (SQLException e) {
			System.out.println("get Major failed to close connection");
			e.printStackTrace();
		}
		return major;
	}
	
	ArrayList<Driver> getdrivers(String destination, String date, String major){
		java.sql.Connection con = forcreation.MakeConnection();
		
		
		ArrayList<Driver> returninglist = new ArrayList<Driver>();
		
		System.out.println(date);
		String query = "select * from driverpost where DestinationID='" + destination+"' and Date = '" +date+ "' and DriverID in (select idProfile from profile where Major= '"+major+"')";
		if(destination==null)
			return returninglist;
		System.out.println(query);
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				Driver temp=new Driver();
				temp.date=date;
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
		
		String query1 = "select * from driverpost where DestinationID='" + destination+"' and Date = '" +date+"'";
		System.out.println(query1);
		try {
			Statement stmt = con.createStatement();
			ResultSet rs1 = stmt.executeQuery(query1);
			while(rs1.next()) {
				Driver temp=new Driver();
				temp.date=date;
				temp.destination=destination;
				temp.driverId=rs1.getInt("driverID");
				temp.seats=rs1.getInt("Seats");
				temp.time=rs1.getString("Time");
				temp.vehicle=rs1.getString("Vehicle");
				temp.tripId=rs1.getInt("TripId");
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
		Rider rider= useguy.getrider(postid);
		String major = useguy.getMajor(rider.riderId);
		System.out.println(rider.riderId+"inside match");
		ArrayList<Driver> driver = useguy.getdrivers(rider.destination, rider.date, major);
		if (driver.isEmpty()) {
			System.out.println("driver is empty");
			return false;
			
		}
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
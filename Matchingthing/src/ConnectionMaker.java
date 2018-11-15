import java.sql.*;

@SuppressWarnings("serial")
public class ConnectionMaker extends Throwable{

	//public static void main(String[] args) {
			    java.sql.Connection MakeConnection() {
			    	java.sql.Connection con = null;
			        try {
			            Class.forName("com.mysql.cj.jdbc.Driver");
			            con = DriverManager.getConnection("jdbc:mysql://75.111.163.91:3306/codemalone?useSSL=false", "general", "password");//?useSSL=false
			            //System.out.print("worked\n");
			            //Statement stmt = con.createStatement();
			        } catch (Exception e) {
			            System.out.println(e);
			            String error="Connection could not be created";
			            throw new IllegalArgumentException(error);
			        }
			        return con;
			    }
	}
import java.sql.*;

public class ActionListener {
	static int highestidchecked = 0;
	ConnectionMaker forcreation = new ConnectionMaker();
	java.sql.Connection con = forcreation.MakeConnection();

	public static void main(String[] args) {
		for(;;) {//infinite loop that will sleep after each loop then recheck the database
		ActionListener actionlistner = new ActionListener();
		int highestnow = actionlistner.CheckForNewPost();//checks if any new posts have been added to riderpost table
		System.out.println(highestnow-highestidchecked);//debugging
		for (int x = highestnow - highestidchecked; x >= 0; x--) {//for ever postid that is higher than the current known highest create a thread and start matching
			System.out.println(highestnow-x+"In main");//the value of the postid that will be given a thread to match
			thread testerguy = new thread(highestnow - x);//constructor so the post can know what id to match with
			testerguy.start();//start the thread
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		highestidchecked = highestnow;//identify the new highest known thread
		System.out.println(highestidchecked);//debugging
		
		try {
			Thread.sleep(40000);//put the main thread to sleep for a quantity of time 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("starting over again my dude");
		}
	}// end of main

	int CheckForNewPost() {
		
		int highestnow = 0;
		String query = "SELECT postID FROM riderpost ORDER BY postID DESC";
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			highestnow = rs.getInt("postID");
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("failed");
			e.printStackTrace();
		}
//		try {
//			//con.close();
//			System.out.println("connection closed");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return highestnow;
	}

}

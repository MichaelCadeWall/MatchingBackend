
public class thread extends Thread {
	int postID = 0;
	Matching holder = new Matching();
	boolean alreadymatched=false;
	public thread(int id) {
		postID = id;
	}

	testing tester = new testing();

	public void run() {
		for (;;) {
			System.out.println("Thread for postID " + postID + " " + alreadymatched);
			try {
			if (holder.Match(postID)) {
				System.out.println(postID);
				alreadymatched=true;
				System.out.println("Killing thread "+postID+" hopefully");
				return;
			}
			else {
				System.out.println("sleeping on " + postID);
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("waking on " + postID);
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		}
	}
}

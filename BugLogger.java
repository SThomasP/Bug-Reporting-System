import java.io.*;
import java.rmi.RemoteException;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class BugLogger {

	private NotificationSink sink;
	private String saveLocation;
	
	// some defaults for the output
	private static final ZoneId zone = ZoneId.systemDefault();
	private static final DateTimeFormatter formatter = DateTimeFormatter
			.ofPattern("kk:mm:ss dd/MM/YYYY");

	// log the bug to a file
	private void logBug(Bug bug, int bugNo) {
		try {
			//create a buffered writer
			BufferedWriter bw;
			// get the csv file for the bug's program
			File f = new File(saveLocation + "\\" + bug.getProgram() + ".csv");
			if (!f.exists()) {
				//if f doesn't exist, create the file and add in the headers
				bw = new BufferedWriter(new FileWriter(f,false));
				bw.write("\"Bug Number\",\"Reported at\",\"Reported by\",\"Description\"");
				bw.newLine();
			}
			else {
				// else just append the file
				bw = new BufferedWriter(new FileWriter(f, true));
			}
			//write output to file
			bw.write(bugNo+",");
			String time = bug.getTimeSent().withZoneSameInstant(zone)
					.format(formatter);
			bw.write(time+",");
			bw.write("\""+bug.getAuthor()+"\",");
			bw.write("\""+bug.getDescription()+"\"");
			bw.newLine();
			//close the buffered writer
			bw.close();
			// show that the bug has been logged
			System.out.println("Bug no. " + bugNo + " logged.");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void logBugs() {
		//create the bug id
		int bugID = 1;
		//start an infinite loop
		while (true) {
			//get the bug from the NotificationSink
			Bug bug = (Bug) sink.readNotification();
			//log it
			logBug(bug, bugID);
			//increase the bugID 
			bugID++;
		}
	}

	public BugLogger(int port, String saveLocation) {
		try {
			//create the NotificationSink
			sink = new NotificationSink();
			//bind it up at a port
			sink.bindUp(port);
			//create the save folder
			this.saveLocation = saveLocation;
			new File(saveLocation).mkdir();
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//create a BugLogger and tell it to Log Bugs
		BugLogger bugLogger = new BugLogger(Integer.parseInt(args[0]), args[1]);
		bugLogger.logBugs();
	}

}

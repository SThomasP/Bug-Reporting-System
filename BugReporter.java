import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BugReporter {

	private NotificationSource source;
	private BufferedReader consoleReader;
	private boolean connectedToASink;

	public BugReporter(String[] sinks) {
		// create a BufferedReader to read from the console
		consoleReader = new BufferedReader(new InputStreamReader(System.in));
		// create a NotificationSource
		source = new NotificationSource();
		connectedToASink = false;
		// if it manages to connect to a NotificationSink it will set
		// connectedToASink to true, else it will be false
		for (String sink : sinks) {
			connectedToASink = source.addSink("localhost:" + sink)
					|| connectedToASink;
		}
	}

	public void reportBugs() throws IOException {
		// if it is connected to sink
		if (connectedToASink) {
			// get the user's details
			System.out.println("What is your name?");
			String name = consoleReader.readLine();
			boolean reporting = true;
			while (reporting && connectedToASink) {
				// then get the details on the bug
				System.out.println("Which piece of software has a bug?");
				String software = consoleReader.readLine();
				System.out.println(
						"Describe the bug in as much detail as possible?");
				String description = consoleReader.readLine();
				Bug bug = new Bug(name, software, description);
				// if there are no longer any available NotificationSinks,
				// report so
				connectedToASink = source.sendNotification(bug);
				if (connectedToASink) {
					// else report that the bug has been reported
					System.out
							.println("Thank you, your bug has been reported.");
					// and ask if the user wants to report another bug
					System.out.println("Would you like to report another bug?");
					String response = consoleReader.readLine();
					if (response.equals("no")) {
						reporting = false;
					}
				}
				else {
					System.out.println(
							"Unable to report bug, the server has gone down.");
				}
			}
		}
		else {
			System.out.println(
					"Unable to connect to a bug logger, shutting down");
		}
	}

	public static void main(String[] args) {
		BugReporter br = new BugReporter(args);
		try {
			br.reportBugs();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

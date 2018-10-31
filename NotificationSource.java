import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;

/*
 * NotificationSource, for sending Notifications to sinks
 */

public class NotificationSource {

	private ArrayList<String> sinks;
	private HashMap<String, SinkInterface> sinkMap;

	// create the source
	public NotificationSource() {
		// create an array list for the sinks
		sinks = new ArrayList<>();
		sinkMap = new HashMap<>();
	}

	// add a sink to the source
	public boolean addSink(String address) {
		try {
			// lookup the sink
			Object sink = Naming.lookup("rmi://" + address + "/the_sink");
			// if it is not already in the sink list, add it
			if (sink instanceof SinkInterface) {
				if (!sinks.contains(address)) {
					sinks.add(address);
				}
				// add or replace the sink to the map
				sinkMap.put(address, (SinkInterface) sink);
				return true;
			}
			else {
				return false;
			}
		}
		catch (RemoteException | MalformedURLException | NotBoundException e) {
			// if an exception is thrown, then return false
			return false;
		}
	}

	// send a notification to all sinks stored in the list
	public boolean sendNotification(Notification note) {
		// create an array list for the removed sources
		ArrayList<String> toRemove = new ArrayList<>();
		for (String address : sinks) {
			// get a sink
			SinkInterface sink = sinkMap.get(address);
			try {
				// try to send an notification to it
				sink.sendNotification(note);
				// if an exception is thrown
			}
			catch (RemoteException e) {
				// try to re-add the sink
				if (addSink(address)) {
					// if it works, try again
					sink = sinkMap.get(address);
					try {
						sink.sendNotification(note);
					}
					catch (RemoteException e2) {
						// and if it then fails again, add it to the remove list
						toRemove.add(address);
					}
					// else, add it to the remove list
				}
				else {
					toRemove.add(address);
				}
			}
		}
		// remove the sinks on the remove list from the main list
		for (String remove : toRemove) {
			sinks.remove(remove);
			sinkMap.remove(remove);
		}
		return sinks.size() > 0;
	}

}

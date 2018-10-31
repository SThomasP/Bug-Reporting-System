import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

/*
 * Notification Sink, for receiving notifications.
 */

public class NotificationSink extends UnicastRemoteObject
		implements
			SinkInterface {

	private static final long serialVersionUID = 1L;
	private LinkedList<Notification> notificationQueue;

	public NotificationSink() throws RemoteException {
		super();
		notificationQueue = new LinkedList<>();
	}

	public boolean bindUp(int port) {
		try {
			LocateRegistry.createRegistry(port);
			// bind the source to the rmi registry
			Naming.rebind("rmi://localhost:" + port + "/the_sink", this);
			return true;
		}
		catch (MalformedURLException | RemoteException e) {
			// if an exception is thrown, the method returns false
			return false;
		}
	}

	// return the latest notification
	public Notification readNotification() {
		// if there are no notifications in the queue
		if (notificationQueue.size() == 0) {
			try {
				synchronized (this) {
					// wait for a notification to arrive
					wait();
				}
				// and then return the top notification
				synchronized (notificationQueue) {
					return notificationQueue.removeFirst();
				}
				// if the wait is interrupted, return null
			}
			catch (InterruptedException e) {
				return null;
			}
		}
		else {
			// if there is initially a notification in the queue
			// then return it.
			synchronized (notificationQueue) {
				return notificationQueue.removeFirst();
			}
		}
	}

	// close the sink, so the program can exit properly
	public void close() {
		try {
			// unexport the sink
			UnicastRemoteObject.unexportObject(this, true);
		}
		catch (NoSuchObjectException e) {
			e.printStackTrace();
		}
	}

	// send a notification to the sink, mostly called by the sources
	public void sendNotification(Notification n) throws RemoteException {
		synchronized (notificationQueue) {
			notificationQueue.add(n);
		}
		// notify the other threads
		synchronized (this) {
			notifyAll();
		}

	}

}

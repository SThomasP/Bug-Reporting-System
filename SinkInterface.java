import java.rmi.*;

public interface SinkInterface extends Remote {
    /*
     * Interface for the NotificationSink Class
     */

    // server side, send a notification
    public void sendNotification(Notification n) throws RemoteException;

}

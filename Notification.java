import java.io.Serializable;
import java.time.ZonedDateTime;

public abstract class Notification implements Serializable {

	/*
	 * Notification class, to be extended based on the application's needs.
	 * It must always have an author, and a time stamp for when it was created.
	 */

	private static final long serialVersionUID = 1L;
	private String author;
	private ZonedDateTime time;

	public Notification(String source) {
		this.author = source;
		this.time = ZonedDateTime.now();
	}

	public String getAuthor() {
		return author;
	}

	public ZonedDateTime getTimeSent() {
		return time;
	}
}

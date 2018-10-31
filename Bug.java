public final class Bug extends Notification {

	/*
	 * Immutable class to store information on the bug, extension of the
	 * Notification class
	 */
	private static final long serialVersionUID = -2907824875313824360L;
	private final String program;
	private final String description;

	//take the author, program and description
	public Bug(String author, String program, String description) {
		//pass the author up to the Notification constructor
		super(author);
		//and then assign the other two to the variable
		this.program = program;
		this.description = description;
	}

	/*
	 * Getters for the variables, it's not a very complex class
	 */
	
	public String getProgram() {
		return program;
	}

	public String getDescription() {
		return description;
	}

}

package exception;

import java.util.Set;

import domain.Task;

/**
 * Thrown to indicate that a conflict has occurred.
 * 
 * @author 	Frederic, Mathias, Pieter-Jan 
 *
 */
public class ConflictException extends Exception {

	private static final long serialVersionUID = -6949314560090380755L;
	
	private final Set<Task> conflictingTasks;
	
	/**
	 * Constructs an {@code ConflictException} with the specified detail message 
	 * and set of conflicting tasks.
	 * 
	 * @param 	message
	 *       	The detail message for this exception.
	 * @param 	conflictingTasks
	 *       	The tasks responsible for the conflict.
	 */
	public ConflictException(String message, Set<Task> conflictingTasks) {
		super(message);
		this.conflictingTasks = conflictingTasks;
	}

	/**
	 * @return the conflicting tasks
	 */
	public Set<Task> getConflictingTasks() {
		return conflictingTasks;
	}

}

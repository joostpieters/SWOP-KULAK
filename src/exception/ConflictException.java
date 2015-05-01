package exception;

import domain.task.Task;
import java.util.Set;

/**
 * Thrown to indicate that a conflict has occurred.
 * 
 * @author 	Frederic, Mathias, Pieter-Jan 
 *
 */
public class ConflictException extends RuntimeException {

	private static final long serialVersionUID = -6949314560090380755L;
	
	private final Task currentTask;
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
	public ConflictException(String message, Task currentTask, Set<Task> conflictingTasks) {
		super(message);
		this.currentTask = currentTask;
		this.conflictingTasks = conflictingTasks;
	}
	
	/**
	 * @return the current task;
	 */
	public Task getCurrentTask() {
		return currentTask;
	}

	/**
	 * @return the conflicting tasks
	 */
	public Set<Task> getConflictingTasks() {
		return conflictingTasks;
	}

}

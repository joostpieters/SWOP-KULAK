package domain.command;

/**
 * TODO
 * @author Frederic
 *
 */
public interface Command {
	/**
	 * Executes this command.
	 */
	void execute();
	
	/**
	 * Reverts the changes caused by the execution of this command.
	 */
	void revert();
}

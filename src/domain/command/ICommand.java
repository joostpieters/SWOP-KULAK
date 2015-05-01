package domain.command;

/**
 * TODO
 * @author Frederic
 *
 */
public interface ICommand {
	/**
	 * Executes this command.
	 */
	void execute();
	
	/**
	 * Reverts the changes caused by the last execution of this command.
	 */
	void revert();
}

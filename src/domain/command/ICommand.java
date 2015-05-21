package domain.command;

/**
 * This class represents a command which can be executed and can revert the last execution.
 * 
 * @author Frederic, Pieter-Jan, Mathias
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

package domain.command;

import exception.ConflictException;

/**
 * TODO
 * @author Frederic
 *
 */
public interface ICommand {
	/**
	 * Executes this command.
	 * @throws ConflictException TODO ConflictException is een checked exception, dit moet nog gefixed worden.
	 */
	void execute() throws ConflictException;
	
	/**
	 * Reverts the changes caused by the last execution of this command.
	 */
	void revert();
}

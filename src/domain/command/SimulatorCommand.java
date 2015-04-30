package domain.command;

import java.util.Stack;

import exception.ConflictException;

/**
 * This SimulatorCommand is an aggregator of commands.
 * 
 * @author Pieter-Jan, Mathias, Frederic
 *
 */
public class SimulatorCommand implements ICommand {
	private Stack<ICommand> commandStack;
	
	/**
	 * Initializes this SimulatorCommand.
	 */
	public SimulatorCommand()
	{
		commandStack = new Stack<>();
	}
	
	/**
	 * Adds the given command to this SimulatorCommand
	 * 
	 * @param command The command to add.
	 */
	public void add(ICommand command)
	{
		commandStack.add(command);
	}
	
	/**
	 * Executes the given command and adds it to this simulatorCommand
	 * @param command
	 * @throws ConflictException
	 */
	public void addAndExecute(ICommand command) throws ConflictException
	{
		command.execute();
		add(command);
	}
	
	/**
	 * Removes the given command from this SimulatorCommand
	 * 
	 * @param command The command to be added.
	 */
	public void remove(ICommand command)
	{
		commandStack.remove(command);
	}
	
	/**
	 * Executes all the commands in the same order as they were added.
	 */
	@Override
	public void execute() throws ConflictException
	{
		for(ICommand command : commandStack)
			command.execute();
	}
	
	/**
	 * Reverts and removes the commands which were added to this simulatorCommand, in reverse order
	 */
	@Override
	public void revert() {
		while(!commandStack.isEmpty())
			commandStack.pop().revert();
	}
}

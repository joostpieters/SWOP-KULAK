package controller;

import domain.Acl;
import domain.Auth;
import domain.Database;
import domain.ProjectContainer;
import domain.command.ICommand;
import domain.time.Clock;
import exception.ConflictException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PlanTaskSimulatorHandler extends PlanTaskHandler {

	private Stack<ICommand> commandStack;
	
	public PlanTaskSimulatorHandler(ProjectContainer manager, Clock clock,
			Auth auth, Acl acl, Database db, Stack<ICommand> commandStack) {
		super(manager, clock, auth, acl, db);
		this.commandStack = commandStack;
	}
	
    /**
     * @see controller.PlanTaskHandler#planTask(int, int, java.time.LocalDateTime, java.util.List)
     */
        @Override
    public void planTask(int pId, int tId, LocalDateTime startTime, List<Integer> resources) throws ConflictException, RuntimeException {
        commandStack.push(manager.getProject(pId).getTask(tId).plan(startTime, new ArrayList<>()));
    }

}

package controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import domain.Acl;
import domain.Auth;
import domain.ProjectContainer;
import domain.Resource;
import domain.command.Command;
import domain.time.Clock;
import exception.ConflictException;

public class PlanTaskSimulatorHandler extends PlanTaskHandler {

	private Stack<Command> commandStack;
	
	public PlanTaskSimulatorHandler(ProjectContainer manager, Clock clock,
			Auth auth, Acl acl, Stack<Command> commandStack) {
		super(manager, clock, auth, acl);
		this.commandStack = commandStack;
	}
	
    /**
     * @see controller.PlanTaskHandler#planTask(int, int, java.time.LocalDateTime, java.util.List)
     */
    public void planTask(int pId, int tId, LocalDateTime startTime, List<Resource> resources) throws ConflictException, RuntimeException {
        commandStack.push(manager.getProject(pId).getTask(tId).plan(startTime, new ArrayList<>()));
    }

}

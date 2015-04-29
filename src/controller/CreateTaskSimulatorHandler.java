package controller;

import java.util.List;
import java.util.Map;

import domain.Acl;
import domain.Auth;
import domain.Database;
import domain.ProjectContainer;

public class CreateTaskSimulatorHandler extends CreateTaskHandler {
	
	public CreateTaskSimulatorHandler(ProjectContainer manager, Auth auth, Acl acl, Database db)
	{
		super(manager, auth, acl, db);
	}
	
	@Override
	public void createTask(int pId, String description, int accDev, List<Integer> prereq, int estDurMinutes, int altfor, Map<Integer, Integer> requiredResources)
	{
		super.createTask(pId, description, accDev, prereq, estDurMinutes, altfor, requiredResources);
	}
	
}

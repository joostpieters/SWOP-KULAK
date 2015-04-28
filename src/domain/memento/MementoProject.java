package domain.memento;

import java.util.HashMap;
import java.util.Map;

import domain.Task;

public class MementoProject {
	private final boolean isFinished;
	private final Map<Integer, Task> tasks;
	
	public MementoProject(boolean isFinished, Map<Integer, Task> tasks)
	{
		this.isFinished = isFinished;
		this.tasks = new HashMap<Integer, Task>(tasks);
	}
	
	public boolean getIsFinished()
	{
		return this.isFinished;
	}
	
	public Map<Integer, Task> getTasks()
	{
		return new HashMap<Integer,Task>(this.tasks);
	}
	
}

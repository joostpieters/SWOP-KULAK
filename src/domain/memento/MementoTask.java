package domain.memento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import domain.Status;
import domain.Task;
import domain.time.Timespan;

/** TODO commentaar
 * 
 * @author Frederic
 *
 */
public class MementoTask {
	
	private final Timespan timespan;
	private final Task alternativeTask;
	private final List<Task> prerequisiteTasks;
	private final Status status;
	private final LocalDateTime plannedStartTime;
	
	public Timespan getTimespan()
	{
		return this.timespan;
	}
	
	public Task getAlternativeTask()
	{
		return this.alternativeTask;
	}
	
	public List<Task> getPrerequisiteTasks()
	{
		return new ArrayList<Task>(this.prerequisiteTasks);
	}
	
	public Status getStatus()
	{
		return this.status;
	}
	
	public LocalDateTime GetPlannedStartTime()
	{
		return this.plannedStartTime;
	}
	
	/**
	 * Initializes this memento based on the given state.
	 * 
	 * @param timespan The time span of the originator task.
	 * @param alternativeTask The alternative task of the originator task.
	 * @param prerequisiteTasks The list of prerequisite tasks of the originator task.
	 * @param status The status of the originator task.
	 * @param plannedStartTime The planned start time of the originator task.
	 */
	public MementoTask(Timespan timespan, Task alternativeTask, List<Task> prerequisiteTasks, Status status, LocalDateTime plannedStartTime)
	{
		this.timespan = timespan;
		this.alternativeTask = alternativeTask;
		this.prerequisiteTasks = new ArrayList<Task>(prerequisiteTasks);
		this.status = status;
		this.plannedStartTime = plannedStartTime;
	}
}

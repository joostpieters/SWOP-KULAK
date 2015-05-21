package domain;

import domain.dto.DetailedProject;
import domain.task.Task;
import domain.time.Duration;
import domain.time.Timespan;
import exception.ObjectNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class represents a project with an id, a name, a description, a creation
 * time, a due time and a list of tasks. This project also contains a system
 * clock to indicate the current system time. Some constants have been defined
 * to be used as default values for task-id's.
 *
 * @author Frederic, Mathias, Pieter-Jan
 *
 */
public class Project implements DetailedProject {

    /**
     * Constant to use when a task has no dependencies.
     */
    public static final List<Integer> NO_DEPENDENCIES = new ArrayList<>(0);
    /**
     * Constant to use when a project has no alternative. The value of this
     * constant is {@value}.
     */
    public static final int NO_ALTERNATIVE = -1;

    private static int nextId = 0;

    private boolean isFinished;									//performance-variable

    private final int id;
    private final String name;
    private final String description;
    private final Timespan creationDueTime;
    private final Map<Integer, Task> tasks = new TreeMap<>();

    /**
     * Construct a new project, with given id, name, description, creation time
     * and due time.
     *
     * @param name The name for this project.
     * @param descr The description for this project.
     * @param creation The creation time for this project.
     * @param due The due time for this project.
     * @throws	IllegalArgumentException if id &lt; 0 or if name or descr = null
     * or if creation and due form an invalid time pair.
     */
    public Project(String name, String descr, LocalDateTime creation, LocalDateTime due) {
        if (!canHaveAsName(name) || !canHaveAsDescription(descr)) {
            throw new IllegalArgumentException("Both name (at least one character) and description "
                    + "(at least one character) are expected.");
        }

        this.id = Project.generateId();
        this.name = name;
        this.description = descr;
        this.creationDueTime = new Timespan(creation, due);
    }

    /**
     * **************************************
     * Getters & setters	* **************************************
     */
    /**
     * @return	a newly generated id for the next task.
     */
    private static int generateId() {
        return Project.nextId++;
    }

    /**
     * @return the id of this project.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * @return the name of this project.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Check whether this project can have a given name.
     *
     * @param name The name to be checked.
     *
     * @return	name != null &amp;&amp; name.length() &gt; 0
     */
    public final boolean canHaveAsName(String name) {
        return name != null && name.length() > 0;
    }

    /**
     * @return the description of this project.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Check whether this project can have a given description.
     *
     * @param descr The description to be checked.
     *
     * @return	descr != null &amp;&amp; descr.length() &gt; 0
     */
    public final boolean canHaveAsDescription(String descr) {
        return descr != null && descr.length() > 0;
    }

    /**
     * @return the creationTime of this project.
     */
    @Override
    public LocalDateTime getCreationTime() {
        return creationDueTime.getStartTime();
    }

    /**
     * @return the dueTime of this project.
     */
    @Override
    public LocalDateTime getDueTime() {
        return creationDueTime.getEndTime();
    }

    /**
     *
     * @return	a list of tasks contained in this project.
     */
    @Override
    public List<Task> getTasks() {
        return new LinkedList<>(tasks.values());
    }

    /**
     * Checks whether this project contains the task with the given task id.
     *
     * @param tid The id of the task to be checked.
     *
     * @return true if this project contains a task with the given task id,
     * false otherwise.
     */
    public boolean hasTask(int tid) {
        return tasks.containsKey(tid);
    }
    
     /**
     * Checks whether this project contains the given task.
     *
     * @param task The task to be checked.
     *
     * @return true if this project contains a the given task,
     * false otherwise.
     */
    public boolean hasTask(Task task) {
        return tasks.containsValue(task);
    }

    /**
     * Get a task with given id contained in this project.
     *
     * @param tid The id of the task to be returned.
     *
     * @return	The task with the given id if it is contained in this project.
     * @throws ObjectNotFoundException if this project doesn't contain the task
     * with the given id.
     *
     * @see	#hasTask(int)
     */
    public Task getTask(int tid) {
        if (!hasTask(tid)) {
            throw new ObjectNotFoundException("Task with tid " + tid + " doesn't exist in this project (" + id + ").", tid);
        }

        return tasks.get(tid);
    }

    /**
     * Check whether this project can have a given task.
     *
     * @param t The task to be checked.
     *
     * @return	true if t is not null and is not in this project already, and if
     * the alternative tasks for t are in this project, and if the project of
     * the given task is the same as this project and if the prerequisite tasks
     * for t are in this project, false otherwise.
     */
    public boolean canHaveAsTask(Task t) {
        if (t == null) {
            return false;
        }
        boolean taskCheck = !tasks.containsKey(t.getId()) && !t.isFulfilled() && t.getProject() == (this);
        
        return taskCheck;
    }

    /**
     * **************************************
     * Task-management	* **************************************
     */
    /**
     * Add a task to the list of tasks for this project.
     *
     * @param t The task to be added.
     *
     * @throws	IllegalStateException if this project is already finished.
     * @throws	NullPointerException if t == null.
     * @throws	IllegalArgumentException if this project can't have the given
     * task as task.
     *
     * @see	#canHaveAsTask(Task)
     */
    public void addTask(Task t) {
        if (isFinished()) {
            throw new IllegalStateException("This project has already been finished.");
        }
        if (!canHaveAsTask(t)) {
            throw new IllegalArgumentException("The given task can't be a part of this project.");
        }

        this.tasks.put(t.getId(), t);
    }

    /**
     * Create a new task and add it to this project.
     *
     * @param descr The description for the new task.
     * @param estdur The estimated duration of the new task.
     * @param accdev The acceptable deviation for the new task in %.
     * @param altFor The id this task is an alternative for.
     * @param prereqs An array of id's of tasks that the new task will depend
     * on.
     * @param requiredResources The required resourcetypes for this task
     *
     * @return	The task that has been created.
     * @throws	IllegalStateException if this project is already finished.
     *
     *
     */
    public Task createTask(String descr, Duration estdur, int accdev, int altFor, List<Integer> prereqs, Map<ResourceType, Integer> requiredResources) {
        if (isFinished()) {
            throw new IllegalStateException("This project has already been finished.");
        }

        if (altFor < 0) {
            altFor = Project.NO_ALTERNATIVE;
        }
        if (prereqs == null) {
            prereqs = Project.NO_DEPENDENCIES;
        }

        Task t;
        if (prereqs.equals(Project.NO_DEPENDENCIES)) {
            t = new Task(descr, estdur, accdev, requiredResources, this);
        } else {
            List<Task> taskList = new ArrayList<>();
            for (Integer tId : prereqs) {
                taskList.add(getTask(tId));
            }

            t = new Task(descr, estdur, accdev, taskList, requiredResources, this);
        }

        if (altFor != Project.NO_ALTERNATIVE) {
            getTask(altFor).setAlternativeTask(t);
        }

        return t;
    }

    /**
     * Return all tasks from this project which are available.
     *
     * @return	a list of tasks which are available, an empty list if this
     * project is already finished.
     *
     * @see	Task#isAvailable()
     */
    public List<Task> getAvailableTasks() {
        List<Task> result = new LinkedList<>();
        for (Task t : getTasks()) {
            if (t.isAvailable()) {
                result.add(t);
            }
        }
        return result;
    }

    /**
     * Return all tasks from this project which are available.
     *
     * @return	a list of tasks which are available to be planned, an empty list
     * if this project does not contain such tasks.
     *
     * @see	Task#hasPlanning()
     */
    public List<Task> getCanBePlannedTasks() {
        List<Task> result = new LinkedList<>();
        for (Task t : getTasks()) {
            if (t.hasPlanning()) {
                result.add(t);
            }
        }
        return result;
    }
    
    /**
     * Returns a list of all the unplanned tasks belonging to this project.
     * 
     * @return The list of tasks belonging to this project which are unplanned.
     * 
     * @see Task#hasPlanning()
     */
    public List<Task> getUnplannedTasks() {
    	List<Task> unplannedTasks = new ArrayList<>();
    	for(Task t : getTasks())
    		if (t.isUnplanned())
    			unplannedTasks.add(t);
    	return unplannedTasks;
    }

    /**
     * Return all tasks which can cause this project to get overdue and the
     * percentage the project will be late because of the task.
     *
     * @param now The time to use to check overdue
     * @return	a map of tasks for which the work time needed to their corresponding
     * percentage by which they are over time. If this project is finished, an
     * empty map is returned.
     *
     * @see	Task#estimatedWorkTimeNeeded()
     * @see	Duration#percentageOver(Duration)
     */
    @Override
    public Map<Task, Double> getUnacceptablyOverdueTasks(LocalDateTime now) {
        Map<Task, Double> result = new HashMap<>();

        for (Task t : getTasks()) {
            if (t.isFulfilled()) {
                continue;
            }
            LocalDateTime estFinTime = t.getEstimatedEndTime(now);
            if (estFinTime.isAfter(getDueTime())) {
                result.put(t, new Duration(getCreationTime(), estFinTime).percentageOver(new Duration(getCreationTime(), getDueTime())));
            }
        }

        return result;
    }

    /**
     * Check whether this project is finished or not.
     *
     * @return	true if this all tasks in this project have been finished and if
     * this project contains at least one task, false otherwise.
     *
     * @see	Task#isFulfilled()
     */
    @Override
    public boolean isFinished() {
        if (isFinished) {
            return true;
        }
        if (getTasks().isEmpty()) {
            return false;
        }

        for (Task t : getTasks()) {
            if (!t.isFulfilled()) {
                return false;
            }
        }

        return (isFinished = true);
    }

    /**
     * **************************************
     * Time-management	* **************************************
     */
    /**
     * Get the time details for this project.
     *
     * @return	true if this project finished on time or if this project is
     * estimated to finish on time, false otherwise.
     */
    @Override
    public boolean isOnTime(LocalDateTime now) {
        if (isFinished()) {
            for (Task t : getTasks()) {
                if (t.getTimeSpan().endsAfter(this.getDueTime())) {
                    return false;
                }
            }
            return true;
        } else {
            Duration temp, max = Duration.ZERO;
            Duration temp2, max2 = Duration.ZERO;
            for (Task t : getTasks()) {
                temp = t.estimatedWorkTimeNeeded();
                if (temp.compareTo(max) > 0) {
                    max = temp;
                }
                temp2 = t.getTimeSpent();
                if (temp2.compareTo(max2) > 0) {
                    max2 = temp2;
                }
            }
            LocalDateTime end = max.add(max2).getEndTimeFrom(getCreationTime());
            if (now.isAfter(end)) {
            	end = now;
            }
            return !end.isAfter(getDueTime());
        }
    }

    /**
     * Gets the total delay of this project.
     *
     * @param now The time to reltively check the delay to.
     * @return the sum of the delays of the tasks within this project if
     * not is on time, null otherwise.
     */
    @Override
    public Duration getDelay(LocalDateTime now) {
        if (isOnTime(now)) {
            return Duration.ZERO;
        }
        Duration totalDuration = Duration.ZERO;
        for (Task t : getTasks()) {
            totalDuration = totalDuration.add(t.getDelay());
        }
        return totalDuration;
    }

    /**
     * Get the amount of working hours that have been put into this project thus
     * far.
     *
     * @return	the sum of durations of the time spans of all tasks in this
     * project.
     *
     * @see	Task#getTimeSpan()
     */
    @Override
    public Duration getTotalExecutionTime() {
        Duration res = Duration.ZERO;
        for (Task t : getTasks()) {
            if (t.hasTimeSpan()) {
                res = res.add(t.getTimeSpan().getDuration());
            }
        }
        return res;
    }

    /**
     * Creates a memento for this project.
     *
     * @return A memento which stores the state of this project.
     */
    public Memento createMemento() {
        return new Memento(isFinished, tasks);
    }

    /**
     * Sets the state of this project to the state stored inside the given
     * memento.
     *
     * @param memento The memento containing the new state of this project.
     */
    public void setMemento(Memento memento) {
        this.isFinished = memento.getIsFinished();
        this.tasks.clear();
        this.tasks.putAll(memento.getTasks());
    }

    /**
     * @return a string, representing the name of this project
     */
    @Override
    public String toString() {
        return this.name;
    }
    

    /**
     * This memento represents the internal state of this project
     */
    public class Memento {

        private final boolean isFinished;
        private final Map<Integer, Task> tasks;

        private Memento(boolean isFinished, Map<Integer, Task> tasks) {
            this.isFinished = isFinished;
            this.tasks = new HashMap<>(tasks);
        }

        private boolean getIsFinished() {
            return this.isFinished;
        }

        private Map<Integer, Task> getTasks() {
            return new HashMap<>(this.tasks);
        }

    }
}

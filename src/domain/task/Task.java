package domain.task;

import domain.Planning;
import domain.Project;
import domain.Resource;
import domain.ResourceType;
import domain.command.PlanTaskCommand;
import domain.dto.DetailedTask;
import domain.time.Clock;
import domain.time.Duration;
import domain.time.Timespan;
import domain.time.WorkWeekConfiguration;
import exception.ConflictException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents a task
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class Task implements DetailedTask {

    private static int nextId = 0;

    private final int id;
    private String description;
    private int acceptableDeviation;
    private Timespan timespan;
    private Duration estimatedDuration;
    private Task alternativeTask;
    private List<Task> prerequisiteTasks;
    private Status status;
    private final Map<ResourceType, Integer> requiredResources;
    private final Project project;
    private static Map<ResourceType, Integer> standardRequiredResources = new HashMap<>();

    /**
     * A constant to indicate that a task requires no resources
     */
    public static Map<ResourceType, Integer> NO_REQUIRED_RESOURCE_TYPES = new HashMap<>();
    private Planning planning;

    /**
     * **************************************
     * Constructors	*
     ***************************************
     */
    /**
     * Initializes this task based on the given description, estimated duration,
     * acceptable deviation and prerequisite tasks.
     *
     * @param description The description of this task.
     * @param duration The estimated duration of this task
     * @param accDev The acceptable deviation of this task expressed as an
     * integer between 0 and 100.
     * @param prereq The list of prerequisite tasks for this task.
     * @param resources The resources this task requires to be performed
     * @param project The project this task belongsto
     */
    public Task(String description, Duration duration, int accDev, List<Task> prereq, Map<ResourceType, Integer> resources, Project project) {
        this.id = generateId();
        if (project == null) {
            throw new IllegalArgumentException("A task cannot exist without a project");
        }
        this.project = project;
        setDescription(description);

        setAcceptableDeviation(accDev);
        if (prereq == null) {
            setPrerequisiteTasks(new ArrayList<>());
        } else {
            setPrerequisiteTasks(prereq);
        }
        
        Map<ResourceType, Integer> allResourceTypes = new HashMap<>(resources);
        allResourceTypes.putAll(standardRequiredResources);
        if (!canHaveAsResourceTypes(allResourceTypes)) {
            throw new IllegalArgumentException("This combination of resourcetypes is not valid.");
        }
        this.requiredResources = new HashMap<>(resources);

        initDuration(duration);

        Status initStatus = new Available();
        setStatus(initStatus);
        initStatus.update(this);
        

        
        this.project.addTask(this);
    }

    /**
     * Initializes this task based on the given description, estimated duration
     * and acceptable deviation.
     *
     * @param description The description of this task.
     * @param duration The estimated duration of this task
     * @param accDev The acceptable deviation of this task expressed as an
     * integer between 0 and 100.
     * @param resources The resources this task requires to be performed
     * @param project The project this task belongs to
     */
    public Task(String description, Duration duration, int accDev, Map<ResourceType, Integer> resources, Project project) {
        this(description, duration, accDev, null, resources, project);
    }

    /**
     * **************************************
     * Getters & Setters & Checkers	*
     ***************************************
     */
    /**
     * Generates an id for a new task.
     *
     * @return The id to be used for a newly created task.
     */
    private static int generateId() {
        return nextId++;
    }

    /**
     * @return The identification number of this task.
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * @return The description of this task.
     */
    @Override
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description of this task to the given description.
     *
     * @param description The new description of this task.
     * @throws IllegalArgumentException If this task can't have the given
     * description as its description.
     */
    private void setDescription(String description) throws IllegalArgumentException {
        if (!canHaveAsDescription(description)) {
            throw new IllegalArgumentException("The given description is uninitialized.");
        } else {
            this.description = description;
        }
    }

    /**
     * Checks whether this task can have the given description as its
     * description.
     *
     * @param description The description to check.
     * @return True if and only if the given description is not equal to null
     * and the given is not empty.
     */
    public boolean canHaveAsDescription(String description) {
        return description != null && !description.isEmpty();
    }

    /**
     * @return The estimated duration of this task.
     */
    @Override
    public Duration getEstimatedDuration() {
        return this.estimatedDuration;
    }

    /**
     * @return The timeSpan indicating the actual start and end time if this
     * task has one, null otherwise.
     */
    @Override
    public Timespan getTimeSpan() {
        return this.timespan;
    }

    /**
     * Sets the time span of this task to the given time span. (this method must
     * only be used by a subclass of status.)
     *
     * @param timeSpan The new time span of this task.
     */
    void setTimeSpan(Timespan timeSpan) {
        this.timespan = timeSpan;
    }

    /**
     * @return The acceptable deviation of this task expressed as an integer
     * between 0 and 100.
     */
    @Override
    public int getAcceptableDeviation() {
        return this.acceptableDeviation;
    }

    /**
     * Sets this task his acceptable deviation to the given acceptable
     * deviation.
     *
     * @param accDev The new acceptable deviation of this task.
     * @throws IllegalArgumentException If this task can't have the given
     * acceptable deviation as its acceptable deviation.
     */
    private void setAcceptableDeviation(int accDev) throws IllegalArgumentException {
        if (!canHaveAsAcceptableDeviation(accDev)) {
            throw new IllegalArgumentException(
                    "This task can't have the given acceptable deviation as its acceptable deviation.");
        }
        this.acceptableDeviation = accDev;
    }

    /**
     * Checks whether this task can have the given acceptable deviation as its
     * acceptable deviation.
     *
     * @param accDev The acceptable deviation.
     * @return True if and only if the given acceptable deviation is between 0
     * and 100.
     */
    public boolean canHaveAsAcceptableDeviation(int accDev) {
        return (0 <= accDev) && (accDev <= 100);
    }

    /**
     * @return The alternative task for this task.
     */
    @Override
    public Task getAlternativeTask() {
        return this.alternativeTask;
    }

    /**
     * Sets the alternative task of this task to the given alternative task.
     *
     * @param alternativeTask The alternative task for this task.
     * @throws IllegalStateException
     * @see Status#setAlternativeTask(domain.Task, domain.Project)
     * @see canHaveAsAlternativeTask
     */
    public void setAlternativeTask(Task alternativeTask) throws IllegalStateException, IllegalArgumentException {
        status.setAlternativeTask(this, alternativeTask);
    }

    /**
     * This method sets the alternative of this task without any checks (Must
     * only be used by subclasses of status.)
     *
     * @param task The alternative task.
     */
    void setAlternativeTaskRaw(Task task) {
        alternativeTask = task;
    }

    /**
     * @return The list of prerequisite tasks for this task.
     */
    @Override
    public List<Task> getPrerequisiteTasks() {
        return new ArrayList<>(this.prerequisiteTasks);
    }

    /**
     * Sets the list of prerequisite tasks to the given list of prerequisite
     * tasks.
     *
     * @param prereq The new list of prerequisite tasks for this task.
     * @throws IllegalArgumentException If this task can't have the given list
     * of prerequisite tasks as its list of prerequisite tasks.
     */
    private void setPrerequisiteTasks(List<Task> prereq) throws IllegalArgumentException {
        if (!canHaveAsPrerequisiteTasks(prereq)) {
            throw new IllegalArgumentException(
                    "This task can't have the given list of prerequisite tasks as its prerequisite tasks.");
        }
        this.prerequisiteTasks = prereq;
    }

    /**
     * Checks whether this task can have the given list of prerequisite tasks as
     * its prerequisite tasks.
     *
     * @param prereq The list of prerequisite tasks to check.
     * @return False if the list is null. False if any of the tasks in the given
     * list is null. False if any of the tasks in the given list is equal to
     * this task. False if any of the tasks in the given list depends on this
     * task. False if one of the given tasks doesn't belong to this project.
     * True otherwise.
     * @see dependsOn
     */
    public boolean canHaveAsPrerequisiteTasks(List<Task> prereq) {
        if (prereq == null) {
            return false;
        }
        
        for (Task t : prereq) {
            if (t == null || t == this || t.dependsOn(this) || !project.hasTask(t)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check whether the given list of resource types is mutually compatible
     *
     * @param resources The resource to check
     * @return {@code true} if and only if all the given resource types don't
     * mutually conflict and for each resource type, its requirements are
     * included in the given list.
     */
    public static boolean canHaveAsResourceTypes(Map<ResourceType, Integer> resources) {
        if (resources == null) {
            return false;
        }
        for (ResourceType type : resources.keySet()) {
            if (!type.canHaveAsCombination(resources)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets a map of resourcetypes acociated with their minimal quantity that
     * are required for each task. This method should only be used once, before
     * you start to create tasks.
     *
     * @param resourceTypes The resourceypes with their necessary quantity
     */
    public static void setStandardRequiredResources(Map<ResourceType, Integer> resourceTypes) {
        
        if (!canHaveAsResourceTypes(resourceTypes)) {
            throw new IllegalArgumentException("This combination of resourcetypes is not valid.");
        }
        standardRequiredResources = new HashMap<>(resourceTypes);
    }

    /**
     * @return The status of this task.
     */
    @Override
    public final Status getStatus() {
        status.update(this);
        return this.status;
    }

    /**
     * Sets the status of this task to the given status. Must only be accessed
     * by objects that are subclasses of status
     *
     * @param status The new status of this task.
     */
    final void setStatus(Status status) {
        this.status = status;
    }

    /**
     * **************************************
     * Other methods	*
     ***************************************
     */
    /**
     * Calculates the duration by which this task been delayed.
     *
     * @return The duration by which this task has been delayed if this task has
     * a time span, based on the maximum duration of this task. If this task
     * doesn't have a time span then the result is a duration of 0 minutes.
     */
    @Override
    public Duration getDelay() {
        if (getTimeSpan() == null) {
            return new Duration(0);
        }

        return getTimeSpan().getExcess(calculateMaxDuration());
    }

    /**
     * @return the requiredResources of this specific task, combined with the
     * standard required resources that are equal for all classes.
     */
    public Map<ResourceType, Integer> getRequiredResources() {
        Map<ResourceType, Integer> allResourceTypes = new HashMap<>(requiredResources);
        allResourceTypes.putAll(standardRequiredResources);
        return allResourceTypes;
    }

    /**
     * Calculates the maximum duration of this task by which this task will
     * still be finished on time.
     *
     * @return The estimated duration of this task multiplied by (100 + (the
     * acceptable deviation of this task))/100
     */
    Duration calculateMaxDuration() {
        return getEstimatedDuration().multiplyBy((100d + getAcceptableDeviation()) / 100d);
    }

    /**
     * Fail this task
     *
     * @param timespan The timespan of this failed task
     * @param currentTime The current time when this task is changed to finished
     *
     */
    public void fail(Timespan timespan, LocalDateTime currentTime) {
        if (timespan == null) {
            throw new IllegalArgumentException("The given timespan is not initialized.");
        }
        
        if (timespan.endsAfter(currentTime)) {
            throw new IllegalArgumentException("The given timespan is after the current time.");
        }
        if (timespan.startsBefore(project.getCreationTime())) {
            throw new IllegalArgumentException("The given timespan is before the project creation time.");
        }
        getStatus().fail(this, timespan);
        if(isPlanned()){
            planning.clearFutureReservations(timespan.getEndTime());
        }
    }

    /**
     * Finish this task
     *
     * @param timespan The timespan of this finished task
     * @param currentTime The current time when this task is changed to finished
     *
     */
    public void finish(Timespan timespan, LocalDateTime currentTime) {
        if (timespan == null) {
            throw new IllegalArgumentException("The given timespan is not initialized.");
        }

        if (timespan.endsAfter(currentTime)) {
            throw new IllegalArgumentException("The given timespan is after the current time.");
        }
        if (timespan.startsBefore(project.getCreationTime())) {
            throw new IllegalArgumentException("The given timespan is before the project creation time.");
        }
        
        getStatus().finish(this, timespan);
        if(isPlanned()){
            planning.clearFutureReservations(timespan.getEndTime());
        }
        
    }

    /**
     * Move this task to the executing state
     *
     * @param clock The clock to use to execute from
     */
    public void execute(Clock clock) {
        getStatus().execute(this, clock);
    }

    /**
     * Checks whether this task is available.
     *
     * @return True if and only if the status of this task is available.
     */
    public boolean isAvailable() {
        return (getStatus() instanceof Available);
    }

    /**
     * Checks whether this task has been fulfilled.
     *
     * @return True if and only if this task is finished or if it has an
     * alternative task and this alternative task is fulfilled.
     */
    public boolean isFulfilled() {
        return getStatus().isFulfilled(this);
    }

    /**
     * Checks whether this task ends before the given time span.
     *
     * @param timeSpan The time span to check.
     * @return True if the time span of this task ends before the start of the
     * given time span.
     * @throws IllegalStateException If this task does not have a time span.
     */
    public boolean endsBefore(Timespan timeSpan) throws IllegalStateException {
        if (getTimeSpan() == null) {
            throw new IllegalStateException(
                    "Tried to check whether this task ends before the given time while this task doesn't have a time span.");
        }
        return getTimeSpan().endsBefore(timeSpan);
    }

    /**
     * Checks whether this task has a time span.
     *
     * @return True if and only if the time span of this task is not null.
     */
    public boolean hasTimeSpan() {
        return getTimeSpan() != null;
    }

    /**
     * Checks whether this task directly or indirectly depends on the given
     * task.
     *
     * @param task The task to check.
     * @return True if this task has an alternative task and the alternative
     * task is equal to the given task. True if a prerequisite task of this task
     * is equal to the given task. True if a prerequisite task depends on the
     * given task. False otherwise.
     */
    public boolean dependsOn(Task task) {
        if (getAlternativeTask() != null && getAlternativeTask().equals(task)) {
            return true;
        }
        for (Task prereqTask : getPrerequisiteTasks()) {
            if (prereqTask.equals(task)) {
                return true;
            }

            if (prereqTask.dependsOn(task)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether the status of this task is equal to finished.
     *
     * @return True if and only if the status of this task is equal to finished.
     */
    public boolean isFinished() {
        return getStatus() instanceof Finished;
    }

    /**
     * The estimated amount of work time needed for this task.
     *
     * @return If this task is finished or failed, a duration of 0. If this task
     * is unavailable, the sum of the amounts of estimated work time needed of
     * the prerequisite tasks of this task + the estimated duration of this task
     * + the estimated durations of the alternatives of the prerequisites of
     * this task. If this task is available, the estimated duration of this
     * task.
     *
     */
    public Duration estimatedWorkTimeNeeded() {
        return getStatus().estimatedWorkTimeNeeded(this);
    }
    
    /**
     * @see Status.isFulfilledBefore
     */
    public boolean isFulfilledBefore(Task task, Timespan timeSpan)
    {
    	return getStatus().isFulfilledBefore(task, timeSpan);
    }
    /**
     * Checks whether this task can be fulfilled or already is fulfilled.
     *
     * @return True if this task is finished or available. False if this task is
     * failed and doesn't have an alternative task. True if this task is failed
     * and has an alternative task that can be fulfilled. False if this task is
     * unavailable and any of the prerequisite tasks can't be fulfilled. True if
     * this task is unavailable and all prerequisite tasks can be fulfilled.
     */
    public boolean canBeFulfilled() {
        return getStatus().canBeFulfilled(this);
    }

    /**
     * Calculates the amount of time spent on this task.
     *
     * @return If this task has a time span then the result is equal to the sum
     * of the maximum amount of time spent on a prerequisite task of this task
     * and the alternative task if this task has an alternative task and the
     * duration of the time span of this task. Otherwise a duration of 0 minutes
     * is returned.
     */
    public Duration getTimeSpent() {
        return getStatus().getTimeSpent(this);
    }

    /**
     * Checks whether this task has an alternative task.
     *
     * @return True if and only if this task has an alternative task.
     */
    public boolean hasAlternativeTask() {
        return getAlternativeTask() != null;
    }

    /**
     * Returns the etimated end time of this task
     *
     * @param now The clock to use to determine the start time of this task
     * @return The time of the given clock + the estimated work time needed by
     * this task.
     */
    public LocalDateTime getEstimatedEndTime(LocalDateTime now) {
        return estimatedWorkTimeNeeded().getEndTimeFrom(now);
    }

    /**
     * Check whether this task is planned
     *
     * @return True if and only if this task has a planned start time.
     */
    public boolean isPlanned() {
        return planning != null;
    }

    /**
     * Plan this task at the given start time
     *
     * @param startTime The time this task is planned to start
     * @param resources The resources to assign to this task
     * @param clock The clock the planning has to observe
     * @return The plan command that has been executed by this method
     * @throws exception.ConflictException The task's reservations conflict with
     * another task
     */
    public PlanTaskCommand plan(LocalDateTime startTime, List<Resource> resources, Clock clock) throws ConflictException {
        Map<ResourceType, Integer> required = getRequiredResources();
        for (ResourceType type : required.keySet()) {
            if (type.numberOfResources(resources) < required.get(type)) {
                throw new IllegalArgumentException("The given set of resources does not match the requirements.");
            }
        }

        PlanTaskCommand planCommand = new PlanTaskCommand(new Timespan(startTime, estimatedDuration), resources, this, clock);
        planCommand.execute();
        return planCommand;
    }
    
    /**
     * Get a set of certain number of possible starting times for this task from a certain
     * point in time.
     *
     * @param from The time after which the task should be started.
     * @param n The number of starting times to be returned.
     * @return	a sorted set of possible points in time this task may be started.
     *          The size of the set is defined n.
     */
    public SortedSet<LocalDateTime> nextAvailableStartingTimes(LocalDateTime from, int n) {
        SortedSet<LocalDateTime> result = new TreeSet<>();
        Map<ResourceType, Integer> required = getRequiredResources();
        
        if(required.isEmpty()) {
        	result.add(from);
        	return result;
        }
        
        SortedSet<Timespan> freeMoments;
        SortedSet<LocalDateTime> temp;
        LocalDateTime last = LocalDateTime.MIN;

        for (ResourceType type : required.keySet()) {
            temp = new TreeSet<>();
            freeMoments = type.nextAvailableTimespans(from);
            for (Timespan span : freeMoments) {
                Timespan rounded = span.roundStartingTime();

                //oneindige lus vermijden
                if (rounded.isInfinite()) {

                    //vanaf de laatste tijd zijn alle resources altijd beschikbaar.
                    if (rounded.startsAfter(last)) {
                        last = rounded.getStartTime();
                        temp.add(rounded.getStartTime());
                    }
                } else {
                    while (rounded != null && type.hasAvailableResources(rounded, required.get(type))) {
                        temp.add(rounded.getStartTime());
                        rounded = rounded.postponeHours(1);
                    }
                }
            }

        	
            if(result.isEmpty()) {
            	result = temp;
            } else if(temp.isEmpty()) {
            	continue;
            } else if(result.last().isBefore(temp.last())) {
                result.retainAll(temp);
                result.addAll(temp.tailSet(result.last()));
            } else {
                temp.retainAll(result);
                temp.addAll(result.tailSet(temp.last()));
                result = temp;
            }
        }
        
        if(result.size() > n) {
        	int count = 1;
        	Iterator<LocalDateTime> it = result.iterator();
        	while(it.hasNext()) {
        		it.next();
        		if(count++ > n)
        			it.remove();
        	}
        } else {
        	int count = 1;
        	while(result.size() < n)
        		result.add(result.last().plusHours(count++));
        }

        return result;
    }

    /**
     * Creates a memento for this task.
     *
     * @return A memento which stores the the state of this task.
     */
    public Memento createMemento() {
        return new Memento(timespan, alternativeTask, prerequisiteTasks, status, planning);
    }

    /**
     * Sets the state of this task to the state stored inside the given memento.
     *
     * @param memento The memento containing the new state of this task.
     */
    public void setMemento(Memento memento) {
        this.timespan = memento.getTimespan();
        this.alternativeTask = memento.getAlternativeTask();
        this.prerequisiteTasks = memento.getPrerequisiteTasks();
        this.status = memento.getStatus();
        this.planning = memento.getPlanning();
    }

    /**
     *
     * @return The project this task belongs to
     */
    @Override
    public Project getProject() {
        return project;
    }

    /**
     * Init the estimated duration of a task with configuration of the least
     * available resourcetype.
     */
    private void initDuration(Duration dur) {
        WorkWeekConfiguration minconf = WorkWeekConfiguration.ALWAYS;

        for (Entry<ResourceType, Integer> entry : requiredResources.entrySet()) {
            if (entry.getKey().getAvailability().compareTo(minconf) < 0) {
                minconf = entry.getKey().getAvailability();
            }
        }

        estimatedDuration = new Duration(dur.toMinutes(), minconf);
    }

    /**
     *
     * @return The planning of this task
     */
    public Planning getPlanning() {
        return this.planning;
    }
    
    /**
     * Sets the planning of this task to the given planning.
     */
    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    /**
     * This mememnto represents the internal state of this task
     */
    public class Memento {

        private final Timespan timespan;
        private final Task alternativeTask;
        private final List<Task> prerequisiteTasks;
        private final Status status;
        private final Planning planning;

        private Timespan getTimespan() {
            return this.timespan;
        }

        private Task getAlternativeTask() {
            return this.alternativeTask;
        }

        private List<Task> getPrerequisiteTasks() {
            return new ArrayList<>(this.prerequisiteTasks);
        }

        private Status getStatus() {
            return this.status;
        }

        private Planning getPlanning() {
            return this.planning;
        }

        /**
         * Initializes this memento based on the given state.
         *
         * @param timespan The time span of the originator task.
         * @param alternativeTask The alternative task of the originator task.
         * @param prerequisiteTasks The list of prerequisite tasks of the
         * originator task.
         * @param status The status of the originator task.
         * @param planning The planning of the originator
         * task.
         */
        private Memento(Timespan timespan, Task alternativeTask, List<Task> prerequisiteTasks, Status status, Planning planning) {
            this.timespan = timespan;
            this.alternativeTask = alternativeTask;
            this.prerequisiteTasks = new ArrayList<>(prerequisiteTasks);
            this.status = status;
            this.planning = planning;
        }
    }
}

package init;

/**
 * This class reads input from a filereader to initialize a projectcontainer
 * with the appropriate data.
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
import domain.Database;
import domain.Developer;
import domain.Project;
import domain.ProjectContainer;
import domain.Resource;
import domain.ResourceType;
import domain.Task;
import domain.time.Clock;
import domain.time.Duration;
import domain.time.Timespan;
import exception.ConflictException;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectContainerFileInitializor extends StreamTokenizer {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private final ProjectContainer manager;
    private final Clock clock;
    private final Database db;

    /**
     * Initialize this ProjectConainerFileInitializor with the given reader and
     * projectcontainer
     *
     * @param r The reader to use to read in the file
     * @param manager The projectContainer to initialize
     * @param clock The clock to use
     * @param db The database to initialize
     */
    public ProjectContainerFileInitializor(Reader r, ProjectContainer manager, Clock clock, Database db) {
        super(r);
        this.manager = manager;
        this.clock = clock;
        this.db = db;
    }

    /**
     *
     * @return The next token
     * @see java.io.StreamTokenizer#nextToken()
     */
    @Override
    public int nextToken() {
        try {
            return super.nextToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void error(String msg) {
        throw new RuntimeException("Line " + lineno() + ": " + msg);
    }

    boolean isWord(String word) {
        return ttype == TT_WORD && sval.equals(word);
    }

    void expectChar(char c) {
        if (ttype != c) {
            error("'" + c + "' expected");
        }
        nextToken();
    }

    void expectLabel(String name) {
        if (!isWord(name)) {
            error("Keyword '" + name + "' expected");
        }
        nextToken();
        expectChar(':');
    }

    LocalDateTime expectDateField(String label) {
        String date = expectStringField(label);
        return LocalDateTime.parse(date, dateTimeFormatter);
    }

    LocalTime expectTimeField(String label) {
        String date = expectStringField(label);
        return LocalTime.parse(date, timeFormatter);
    }

    String expectStringField(String label) {
        expectLabel(label);
        if (ttype != '"') {
            error("String expected");
        }
        String value = sval;
        nextToken();
        return value;
    }

    int expectInt() {
        if (ttype != TT_NUMBER || nval != (double) (int) nval) {
            error("Integer expected");
        }
        int value = (int) nval;
        nextToken();
        return value;
    }

    int expectIntField(String label) {
        expectLabel(label);
        return expectInt();
    }

    List<Integer> expectIntList() {
        ArrayList<Integer> list = new ArrayList<>();
        expectChar('[');
        while (ttype == TT_NUMBER) {
            list.add(expectInt());
            if (ttype == ',') {
                expectChar(',');
            } else if (ttype != ']') {
                error("']' (end of list) or ',' (new list item) expected");
            }
        }
        expectChar(']');
        return list;
    }

    public class IntPair {

        public int first;
        public int second;
    }

    List<IntPair> expectLabeledPairList(String first, String second) {
        ArrayList<IntPair> list = new ArrayList<>();
        expectChar('[');
        while (ttype == '{') {
            if (ttype == '{') {
                expectChar('{');
                int f = expectIntField(first);
                expectChar(',');
                int s = expectIntField(second);
                expectChar('}');
                IntPair p = new IntPair();
                p.first = f;
                p.second = s;
                list.add(p);
            }
            if (ttype == ',') {
                expectChar(',');
            } else if (ttype != ']') {
                error("']' (end of list) or ',' (new list item) expected");
            }
        }
        expectChar(']');
        return list;
    }
    
    /**
     * Processes the input file and inits all necessary structures
     */
    public void processFile() throws ConflictException {
        slashSlashComments(false);
        slashStarComments(false);
        ordinaryChar('/'); // otherwise "//" keeps treated as comments.
        commentChar('#');

        nextToken();

        LocalDateTime systemTime = expectDateField("systemTime");
        
        clock.advanceTime(systemTime);
        
        // TODO 
        expectLabel("dailyAvailability");
        while (ttype == '-') {
            expectChar('-');
            LocalTime creationTime = expectTimeField("startTime");
            LocalTime dueTime = expectTimeField("endTime");
        }

        expectLabel("resourceTypes");
        while (ttype == '-') {
            expectChar('-');
            String name = expectStringField("name");
            expectLabel("requires");
            List<Integer> requirementIds = expectIntList();
            expectLabel("conflictsWith");
            List<Integer> conflictIds = expectIntList();
            expectLabel("dailyAvailability");
            if (ttype == TT_NUMBER) {
                int availabilityIndex = expectInt();
            }

            List<ResourceType> requirements = new ArrayList<>();

            // transform ids to objects
            for (Integer i : requirementIds) {
                requirements.add(db.getResourceTypes().get(i));
            }

            List<ResourceType> conflicts = new ArrayList<>();

            // transform ids to objects
            for (Integer i : conflictIds) {
                requirements.add(db.getResourceTypes().get(i - 1));
            }

            db.addResourceType(new ResourceType(name, requirements, conflicts));
        }

        expectLabel("resources");
        ArrayList<Resource> resourcesList = new ArrayList<>();
        while (ttype == '-') {
            expectChar('-');
            String name = expectStringField("name");
            expectLabel("type");
            int typeIndex = expectInt();
            // add to resourcetype
            Resource res = new Resource(name, clock);
            db.getResourceTypes().get(typeIndex).addResource(res);
            // add to db
            db.addResource(res);
            // add to temp list
            resourcesList.add(res);
        }

        expectLabel("developers");
        while (ttype == '-') {
            expectChar('-');
            String name = expectStringField("name");
            //TODO resourcetype voor developers?
            db.addUser(new Developer(name, clock));
            
        }

        expectLabel("projects");

        while (ttype == '-') {
            expectChar('-');
            String name = expectStringField("name");
            String description = expectStringField("description");
            LocalDateTime creationTime = expectDateField("creationTime");
            LocalDateTime dueTime = expectDateField("dueTime");
            manager.createProject(name, description, creationTime, dueTime);
        }

        expectLabel("plannings");
        List<HashMap<ResourceType, Integer>> requiredResourceMaps = new ArrayList<>();
        while (ttype == '-') {
            expectChar('-');
            LocalDateTime dueTime = expectDateField("plannedStartTime");
            expectLabel("developers");
            List<Integer> developers = expectIntList();
            expectLabel("resources");
            List<IntPair> resources = expectLabeledPairList("type", "quantity");
            HashMap<ResourceType, Integer> resourceMap = new HashMap<>();
            for(IntPair pair : resources){
                resourceMap.put(db.getResourceTypes().get(pair.first), pair.second);
            }
            requiredResourceMaps.add(resourceMap);
        }

        expectLabel("tasks");
        List<Task> taskList = new ArrayList<>();
        while (ttype == '-') {
            expectChar('-');
            int projectId = expectIntField("project");
            String description = expectStringField("description");

            int estimatedDuration = expectIntField("estimatedDuration");
            int acceptableDeviation = expectIntField("acceptableDeviation");
            int alternativeFor = Project.NO_ALTERNATIVE;
            expectLabel("alternativeFor");
            if (ttype == TT_NUMBER) {
                alternativeFor = expectInt();
            }
            List<Integer> prerequisiteTasks = new ArrayList<>();
            expectLabel("prerequisiteTasks");
            if (ttype == '[') {
                prerequisiteTasks = expectIntList();
            }

            if (prerequisiteTasks.isEmpty()) {
                prerequisiteTasks = Project.NO_DEPENDENCIES;
            }

            Duration duration = new Duration(estimatedDuration);
            
            expectLabel("planning");
            Integer planning;
            Task task;
            if (ttype == TT_NUMBER) {
                planning = expectInt();
                task = manager.getProject(projectId).createTask(description, duration, acceptableDeviation, alternativeFor, prerequisiteTasks, requiredResourceMaps.get(planning));
            }else{
                task = manager.getProject(projectId).createTask(description, duration, acceptableDeviation, alternativeFor, prerequisiteTasks, Task.NO_REQUIRED_RESOURCE_TYPES);
            }
            // add to temporary list
            taskList.add(task);

            expectLabel("status");
            String status = "";
            if (isWord("finished")) {
                nextToken();
                status = "finished";
            } else if (isWord("failed")) {
                nextToken();
                status = "failed";
            }
            if (isWord("executing")) {
                nextToken();
                status = "executing";
            }
            if (!"".equals(status) && !(status.equalsIgnoreCase("executing"))) {
                LocalDateTime startTime = expectDateField("startTime");
                LocalDateTime endTime = expectDateField("endTime");
                
                if(status.equalsIgnoreCase("failed")){
                    manager.getProject(projectId).getTask(task.getId()).fail(new Timespan(startTime, endTime), clock.getTime());
                }else if(status.equalsIgnoreCase("finished")){
                    manager.getProject(projectId).getTask(task.getId()).finish(new Timespan(startTime, endTime), clock.getTime());
                }
              
            }

        }

        expectLabel("reservations");
        while (ttype == '-') {
            expectChar('-');
            int resource = expectIntField("resource");
            int task = expectIntField("task");
            LocalDateTime startTime = expectDateField("startTime");
            LocalDateTime endTime = expectDateField("endTime");
            resourcesList.get(resource).makeReservation(taskList.get(task), new Timespan(startTime, endTime));
        }
        if (ttype != TT_EOF) {
            error("End of file or '-' expected");
        }
    }
}

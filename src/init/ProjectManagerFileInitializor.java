package init;

/**
 * This class reads input from a filereader to initialize a projectmanager with 
 * the appropriate data.
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
import domain.Duration;
import domain.Failed;
import domain.Finished;
import domain.Project;
import domain.ProjectManager;
import domain.Status;
import domain.Task;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProjectManagerFileInitializor extends StreamTokenizer {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final ProjectManager manager;
    
    /**
     * Initialize this ProjectManagerFileInitializor with the given reader and
     * projectmanager
     * 
     * @param r The reader to use to read in the file
     * @param manager The projectmanager to initialize
     */
    public ProjectManagerFileInitializor(Reader r, ProjectManager manager) {
        super(r);
        this.manager = manager;
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

    String expectStringField(String label) {
        expectLabel(label);
        if (ttype != '"') {
            error("String expected");
        }
        String value = sval;
        nextToken();
        return value;
    }

    LocalDateTime expectDateField(String label) {
        String date = expectStringField(label);
        return LocalDateTime.parse(date, dateTimeFormatter);
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
    
    /**
     * Parse the file and populate this manager with the appropriate data.
     */
    public void processFile() {
        slashSlashComments(false);
        slashStarComments(false);
        ordinaryChar('/'); // otherwise "//" keeps treated as comments.
        commentChar('#');

        nextToken();
        expectLabel("projects");

        while (ttype == '-') {
            expectChar('-');
            String name = expectStringField("name");
            String description = expectStringField("description");
            LocalDateTime creationTime = expectDateField("creationTime");
            LocalDateTime dueTime = expectDateField("dueTime");
            manager.createProject(name, description, creationTime, dueTime);
        }
        expectLabel("tasks");
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
            Task task = manager.getProject(projectId).createTask(description, duration, acceptableDeviation, alternativeFor, prerequisiteTasks);

            expectLabel("status");
            Status status = null;
            if (isWord("finished")) {
                nextToken();
                status = new Finished();
            } else if (isWord("failed")) {
                nextToken();
                status = new Failed();
            }
            if (status != null) {
                LocalDateTime startTime = expectDateField("startTime");
                LocalDateTime endTime = expectDateField("endTime");
                manager.advanceSystemTime(endTime);
                manager.getProject(projectId).updateTask(task.getId(), startTime, endTime, status);
            }

        }
        if (ttype != TT_EOF) {
            error("End of file or '-' expected");
        }
    }
}

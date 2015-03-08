package domain;

/**
 * This class reads input from a file to initialize a projectmanager
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
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
    

    public ProjectManagerFileInitializor(Reader r, ProjectManager manager) {
        super(r);
        this.manager = manager;
    }

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
            int project = expectIntField("project");
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
            int[] prereq = new int[prerequisiteTasks.size()];
            int i = 0;
            for (Integer e : prerequisiteTasks)  
                prereq[i++] = e;
            // TODO better create task and return task
            manager.getProject(project).createTask(description, estimatedDuration, acceptableDeviation, alternativeFor, prereq);
            Task task = manager.getProject(project).getTask(0);
            expectLabel("status");
            Status status = null;
            if (isWord("finished")) {
                nextToken();
                status = Status.FINISHED;
            } else if (isWord("failed")) {
                nextToken();
                status = Status.FAILED;
            }
            if (status != null) {
                LocalDateTime startTime = expectDateField("startTime");
                LocalDateTime endTime = expectDateField("endTime");
                task.update(startTime, endTime, status);
            }
            
        }
        if (ttype != TT_EOF) {
            error("End of file or '-' expected");
        }
    }
}

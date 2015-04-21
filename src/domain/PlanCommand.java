package domain;

import java.util.List;

/**
 * This class represents the action of doing a reservation
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public class PlanCommand {
    private final Task task;
    private final List<Resource> resources;
    private final Timespan timespan;
    private List<ReservationCommand> reservations;
    
    public PlanCommand(Timespan timespan, List<Resource> resources, Task task){
        this.task = task;
        this.resources = resources;
        this.timespan = timespan;
    }
    
    
    public void execute(){
        if(task.isPlanned()){
            moveTask();
        }else{
            planTask();
        }
    }
    
    private void moveTask(){
        
    }
    
    private void planTask(){
        for(Resource resource : resources){
            reservations.add(new ReservationCommand(timespan, resource, task));
        }
    }
    
    private void revert(){
        for(ReservationCommand command : reservations){
            command.revert();
        }
    }
}

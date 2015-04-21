package domain;

import exception.ConflictException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<Resource, Reservation> oldReservations;

    public PlanCommand(Timespan timespan, List<Resource> resources, Task task) {
        this.task = task;
        this.resources = resources;
        this.timespan = timespan;
        for (Resource resource : resources) {
            reservations.add(new ReservationCommand(timespan, resource, task));
        }
        oldReservations = new HashMap<Resource, Reservation>();
    }

    public void execute() throws ConflictException {
        if (task.isPlanned()) {
            moveTask();
        } else {
            planTask();
        }
    }

    private void moveTask() throws ConflictException {
        for(Resource res : resources){
            oldReservations.put(res, res.getReservation(task));
        }
        
        try {
            for (ReservationCommand command : reservations) {
                command.execute();
            }
        } catch (ConflictException ex) {
            revert();
            revertMove();
            
            throw ex;
        }
        
    }

    private void planTask() throws ConflictException {
        try {
            for (ReservationCommand command : reservations) {
                command.execute();
            }
        } catch (ConflictException ex) {
            revert();
            
            throw ex;
        }
    }

    private void revert() {
        for (ReservationCommand command : reservations) {
            command.revert();
        }
    }
    
    private void revertMove() {
        int i =0;
        for(Resource res : oldReservations.keySet()){
        	try
        	{
            	res.makeReservation(oldReservations.get(res));
        	}
        	catch(Exception e) {}
        }
        /*for(Resource res : resources){
        	Reservation oldReservation = oldReservations.get(i);
        	if(oldReservation != null)
        		res.makeReservation(oldReservation);
            i++;
        }*/
    }
}

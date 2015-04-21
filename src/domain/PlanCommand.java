package domain;

import exception.ConflictException;
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
    private List<Reservation> oldReservations;

    public PlanCommand(Timespan timespan, List<Resource> resources, Task task) {
        this.task = task;
        this.resources = resources;
        this.timespan = timespan;
        for (Resource resource : resources) {
            reservations.add(new ReservationCommand(timespan, resource, task));
        }
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
            oldReservations.add(res.getReservation(task));
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
        for(Resource res : resources){
            res.makeReservation(oldReservations.get(i));
            i++;
        }
    }
}

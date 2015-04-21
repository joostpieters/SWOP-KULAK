package domain;

import java.util.List;

import exception.ConflictException;

/**
 * This class represents a planning.
 * 
 * @author Mathias, Pieter-Jan, Frederic
 */
public class Planning {
    public Planning(Timespan timespan, List<Resource> resources) throws ConflictException {
        for(Resource resource : resources){
            resource.makeReservation(null, timespan);        
            
        }
        
        
    }
    
}

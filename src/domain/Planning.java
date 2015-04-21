package domain;

import java.util.List;

/**
 * This class represents a planning.
 * 
 * @author Mathias, Pieter-Jan, Frederic
 */
public class Planning {
    public Planning(Timespan timespan, List<Resource> resources) {
        for(Resource resource : resources){
            resource.makeReservation(null, timespan);        
            
        }
        
        
    }
    
}

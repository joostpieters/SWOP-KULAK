package domain.dto;

import domain.dto.DetailedResource;
import domain.time.Timespan;
import java.util.List;

/**
 * An interface to retrieve data from a planning
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public interface DetailedPlanning {

    /**
     *
     * @return The resources assigned to this planning
     */
    List<? extends DetailedResource > getResources();

    /**
     *
     * @return The timespan of this planning
     */
    Timespan getTimespan();
    
}

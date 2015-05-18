package domain.dto;

import domain.ResourceType;

/**
 * This interface provides access to the properties of resourcetype, without 
 * exposing bussiness logic to the UI
 *  
 * @author Mathias, Frederic, Pieter-Jan
 */
public interface DetailedResource {

    /**
     * @return the id of this resource
     */
    int getId();

    /**
     * @return the name of this resource
     */
    String getName();
    
    /**
     * @return the type of this resource
     */
    ResourceType getType();
    
}

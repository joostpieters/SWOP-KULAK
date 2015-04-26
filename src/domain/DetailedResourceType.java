package domain;

import java.util.Set;

/**
 * This interface provides access to the properties of resourcetype, without 
 * exposing bussiness logic to the UI
 *  
 * @author Mathias, Frederic, Pieter-Jan
 */
public interface DetailedResourceType {

    /**
     *
     * @return The name of this resourcetype
     */
    String getName();
    
    /**
     * @return the resources
     */
    public Set<? extends DetailedResource> getResources();
    
}

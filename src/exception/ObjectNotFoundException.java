package exception;

/**
 * This exception is thrown when a specified object isn't found.
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class ObjectNotFoundException extends IllegalArgumentException{
    
	private static final long serialVersionUID = -6476237094197413847L;
	
	private int id;
    
    /**
     * @see IllegalArgumentException
     * @param message @see IllegalArgumentException
     */
    public ObjectNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Initialize this exception with the given message and id.
     * 
     * @param message  @see IllegalArgumentException
     * @param id This is the id by which the object that isn't found is represented.
     */
    public ObjectNotFoundException(String message, int id) {
        super(message);
        this.id = id;
    }
    
    /**
     * 
     * @return The id by which the object that isn't found is represented.
     */
    public int getId() {
        return id;
    }
    
    
    
}

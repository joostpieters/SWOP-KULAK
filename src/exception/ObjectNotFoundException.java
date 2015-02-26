package exception;

/**
 * This exception is thrown when a specified object isn't found.
 * 
 * @author Frederic, Mathias, Pieter-Jan
 */
public class ObjectNotFoundException extends Exception{
    
    private int id;
    
    /**
     * @see Exception
     * @param message 
     */
    public ObjectNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Initialize this exception with the given message and id.
     * 
     * @param message @see Exception
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

package domain;

/**
 * This class represents the status of a task, all satuses are mutually exclusive.
 * 
 * @author Frederic, Mathias, Pieter-Jan 
 */
public enum Status {

    /**
     * The status for a failed task
     */
    FAILED,

    /**
     * The status for a finished task
     */
    FINISHED,

    /**
     * The status for an available task, all prerequisites are fullfilled
     */
    AVAILABLE,

    /**
     * The status for an unavailable task, not all prerequisites are fullfilled
     */
    UNAVAILABLE
}

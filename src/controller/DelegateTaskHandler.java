package controller;

import domain.Database;
import domain.BranchOffice;
import domain.command.SimulatorCommand;
import domain.time.Clock;
import domain.user.Acl;
import domain.user.Auth;

/**
 * This handler, handles the delegate task use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class DelegateTaskHandler extends Handler {

    protected final BranchOffice manager;
    private final Clock clock;
    private final Database db;
    
	private SimulatorCommand simulatorCommand;

    /**
     * Initialize a new create task handler with the given projectContainer.
     *
     * @param manager The projectContainer to use in this handler.
     * @param clock The clock to use in this handler
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     * @param db The database to use in this handler
     */
    public DelegateTaskHandler(BranchOffice manager, Clock clock, Auth auth, Acl acl, Database db) {
    	this(manager, clock, auth, acl, db, new SimulatorCommand());
    }
    
    /**
     * Initialize a new create task handler with the given projectContainer.
     *
     * @param manager The projectContainer to use in this handler.
     * @param clock The clock to use in this handler
     * @param auth The authorization manager to use
     * @param acl The action control list to use
     * @param db The database to use in this handler
     * @param simulatorCommand The simulator command to which commands are added.
     */
    public DelegateTaskHandler(BranchOffice manager, Clock clock, Auth auth, Acl acl, Database db, SimulatorCommand simulatorCommand)
    {
        super(auth, acl);
        this.manager = manager;
        this.clock = clock;
        this.db = db;
        this.simulatorCommand = simulatorCommand;
    }
    
}

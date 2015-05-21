package controller;

import domain.BranchOffice;
import domain.Company;
import domain.Resource;
import domain.dto.DetailedBranchOffice;
import domain.time.WorkWeekConfiguration;
import domain.user.Auth;
import domain.user.User;
import exception.NoAccessException;
import java.awt.HeadlessException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * This handler, handles the login use case
 *
 * @author Frederic, Mathias, Pieter-Jan
 */
public class LoginHandler {

    private final Auth auth;
    private final Company db;
    private final HandlerFactory factory;

    /**
     * Initialize a new create task handler with the given projectContainer.
     *
     * @param auth The authorization manager to use
     * @param db The database to use
     * @param factory The handler factory
     */
    public LoginHandler(Auth auth, Company db, HandlerFactory factory) {
        this.auth = auth;
        this.db = db;
        this.factory = factory;
    }

    /**
     * Logs the user in with the given username
     *
     * @param username The username of the user to login
     * @throws NoAccessException The login failed
     */
    public void login(String username) throws NoAccessException {
        if (!auth.login(username)) {
            throw new NoAccessException("The login failed");
        }

        factory.setManager(auth.getUser().getBranchOffice());
    }

    /**
     * Set the start of the one hour lunchbreak for the currently loggedin
     * developer.
     *
     * @param begintime The start time of the lunchbreak.
     */
    public void setLunchbreak(String begintime) {
        if (auth.loggedIn() && auth.getUser().getRole().equals("developer")) {

            if (begintime != null) {

                try {

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    LocalTime start = LocalTime.parse(begintime, formatter);
                    LocalTime end = start.plusHours(1);
                    ((Resource) auth.getUser()).setAvailability(new WorkWeekConfiguration(LocalTime.of(8, 00), LocalTime.of(17, 00), start, end));

                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("The given time should be of the form (hh:mm).");
                } catch (HeadlessException | IllegalArgumentException exception) {
                    throw new IllegalArgumentException("This lunchbreak is not allowed.");
                }

            }

        }
    }

    public boolean askLunchbreak() {
        return auth.loggedIn() && auth.getUser().getRole().equals("developer");
    }

    /**
     * Logs out the currently loggedin user
     *
     */
    public void logout() {
        auth.logout();
    }

    /**
     *
     *
     * @return True if and only if a user is logged in
     */
    public boolean loggedIn() {
        return auth.loggedIn();
    }

    /**
     *
     * @return A list of all brancoffices in this company
     */
    public List<DetailedBranchOffice> getOffices() {
        return new ArrayList<>(db.getOffices());
    }

    /**
     * Returns all the developers of the given branchoffice
     *
     * @param officeId The id of the branchoffice in the database
     * @return A list of developers of the office with the given id.
     */
    public List<User> getUsers(int officeId) {
        BranchOffice office = db.getOffices().get(officeId);

        return new ArrayList<>(office.getUsers());
    }

}

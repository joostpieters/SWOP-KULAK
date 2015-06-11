package controller;

import domain.BranchOffice;
import domain.Company;
import domain.Resource;
import domain.dto.DetailedBranchOffice;
import domain.time.WorkWeekConfiguration;
import domain.user.Auth;
import domain.user.Role;
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

    private final Company company;
    private final Auth auth;

    /**
     * Initialize a new login handler for the given company.
     * @param company The company to use
     * @param auth The authorization manager to use
     */
    public LoginHandler(Company company, Auth auth) {
        this.company = company;
        this.auth = auth;
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
    }

    /**
     * Set the start of the one hour lunch break for the currently logged-in
     * developer.
     *
     * @param begintime The start time of the lunch break.
     */
    public void setLunchbreak(String begintime) {
        if (auth.loggedIn() && auth.getUser().getRole().equals(Role.DEVELOPER)) {

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

    /**
     * @return {@code true} if a developer is logged in to the authentication manager.
     */
    public boolean askLunchbreak() {
        return auth.loggedIn() && auth.getUser().getRole().equals(Role.DEVELOPER);
    }

    /**
     * Logs out the currently logged-in user
     */
    public void logout() {
        auth.logout();
    }

    /**
     * @return {@code true} if and only if a user is logged in
     */
    public boolean loggedIn() {
        return auth.loggedIn();
    }

    /**
     * @return A list of all branch offices in this company
     */
    public List<DetailedBranchOffice> getOffices() {
        return new ArrayList<>(company.getOffices());
    }

    /**
     * Returns all the developers of the given branch office
     *
     * @param officeId The id of the branch office in the company
     * @return A list of users of the office with the given id.
     */
    public List<User> getUsers(int officeId) {
        BranchOffice office = company.getOffices().get(officeId);
        return new ArrayList<>(office.getUsers());
    }
}

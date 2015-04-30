package controller;

import domain.user.Acl;
import domain.user.Auth;
import exception.NoAccessException;

/**
 * This class represents an abstract handler from which all handlers inherit.
 * 
 * @author Mathias, Frederic, Pieter-Jan
 */
public abstract class Handler {
    protected Auth auth;
    protected Acl acl;
    
    
    public Handler(Auth auth, Acl acl) throws NoAccessException{
        this.auth = auth;
       this.acl = acl;
        checkLogin();
        checkPermission();        
    }
    
    
    protected void checkLogin() throws NoAccessException{
        if(!auth.loggedIn()){
            throw new NoAccessException("Sorry you have to be logged in to perform this action.");
        }
    }
    
    protected void checkPermission() throws NoAccessException{
        String permission = this.getClass().getSimpleName().replaceAll("Handler", "");
        if(!acl.hasPermission(auth.getUser(), permission)){
            throw new NoAccessException("Sorry you don't have the right permission to perform this action.");
        }
    }
}

package at.qe.skeleton.ui.beans;

import at.qe.skeleton.model.Userx;
import at.qe.skeleton.model.UserxRole;
import at.qe.skeleton.services.UserxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Session information bean to retrieve session-specific parameters.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * course "Software Architecture" offered by Innsbruck University.
 */
@Component
@Scope("session")
public class SessionInfoBean implements Serializable {

    @Autowired
    private UserxService userService;

    /**
     * Attribute to cache the current user.
     */
    private Userx currentUser;

    /**
     * Returns the currently logged on user, null if no user is authenticated
     * for this session.
     *
     * @return
     */
    public Userx getCurrentUser() {
        if (currentUser == null) {
            String currentUserName = userService.getCurrentUserName();
            if (currentUserName.isEmpty()) {
                return null;
            }
            currentUser = userService.loadUser(currentUserName);
        }
        return currentUser;
    }

    /**
     * Returns the username of the user for this session, empty string if no
     * user has been authenticated for this session.
     *
     * @return
     */
    public String getCurrentUserName() {
        return userService.getCurrentUserName();
    }


    /**
     * Returns the roles of the user for this session as space-separated list,
     * empty string if no user has been authenticated for this session-
     *
     * @return
     */
    public String getCurrentUserRoles() {
        if (!isLoggedIn()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority role : auth.getAuthorities()) {
            sb.append(role.getAuthority());
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * Checks if a user is authenticated for this session.
     *
     * @return true if a non-anonymous user has been authenticated, false
     * otherwise
     */
    public boolean isLoggedIn() {
        return userService.isLoggedIn();
    }

    /**
     * Checks if the user for this session has the given role (c.f.
     * {@link UserxRole}).
     *
     * @param role the role to check for as string
     * @return true if a user is authenticated and the current user has the
     * given role, false otherwise
     */
    public boolean hasRole(String role) {
        if (role == null || role.isEmpty() || !isLoggedIn()) {
            return false;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority ga : auth.getAuthorities()) {
            if (role.equals(ga.getAuthority())) {
                return true;
            }
        }
        return false;
    }

}


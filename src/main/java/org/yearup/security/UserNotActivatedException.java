package org.yearup.security;
import org.springframework.security.core.AuthenticationException;

/**
 * Custom exception thrown when a user attempts to authenticate
 * but their account has not been activated.
 * This exception integrates directly with Spring Security's
 * authentication flow.
 */

public class UserNotActivatedException extends AuthenticationException {
    // Required for serialization compatibility
    private static final long serialVersionUID = -1126699074574529145L;
   //Creates a new UserNotActivatedException with a custom message
    public UserNotActivatedException(String message) {
        super(message);
    }
}

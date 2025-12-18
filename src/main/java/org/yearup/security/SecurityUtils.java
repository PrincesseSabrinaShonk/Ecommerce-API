package org.yearup.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class SecurityUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityUtils.class);

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUsername() {
        // Get the current Authentication object from Spring Security
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // If there is no authentication, the user is not logged in
        if (authentication == null) {
            LOG.debug("no authentication context found");
            return Optional.empty();
        }
        String username = null;
        // Principal is a Spring Security UserDetails object
        // (most common when using JWT or form login)

        if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            username = springSecurityUser.getUsername();

            // Principal is stored directly as a String (username)
        } else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }
        // Log the discovered username for debugging purposes
        LOG.debug("found username '{}' in context", username);
        // Return the username wrapped in an Optional to avoid null checks
        return Optional.ofNullable(username);
    }
}

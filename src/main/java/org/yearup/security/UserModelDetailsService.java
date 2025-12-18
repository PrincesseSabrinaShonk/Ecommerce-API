package org.yearup.security;


import org.yearup.data.UserDao;
import org.yearup.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
//Create this class as a managed Spring bean and register it under the name userDetailsService
@Component("userDetailsService")
public class UserModelDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserModelDetailsService.class);
    // DAO used to retrieve users from the database
    private final UserDao userDao;
    // Constructor injection of UserDao
    public UserModelDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(final String login) {
        // Log authentication attempt (useful for debugging)
        log.debug("Authenticating user '{}'", login);

        // Normalize username to lowercase to ensure consistency
        String lowercaseLogin = login.toLowerCase();

        // Retrieve the user from the database and convert it
        // into a Spring Security-compatible UserDetails object
        return createSpringSecurityUser
                (lowercaseLogin,
                        userDao.getByUserName(lowercaseLogin));
    }
    //Converts an application User model into a Spring Security User.
    // This method validates activation status and maps user roles, into Spring Security authorities.
    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {

        // Prevent login if the account has not been activated
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        // Prevent login if the account has not been activated
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());

        // Return a Spring Security User object containing
        // username, encrypted password, and granted authorities
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                grantedAuthorities);
    }
}


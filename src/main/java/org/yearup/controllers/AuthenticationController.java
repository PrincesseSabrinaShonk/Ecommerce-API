package org.yearup.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.authentication.LoginDto;
import org.yearup.models.authentication.LoginResponseDto;
import org.yearup.models.authentication.RegisterUserDto;
import org.yearup.models.User;
import org.yearup.security.jwt.JWTFilter;
import org.yearup.security.jwt.TokenProvider;

@RestController
@CrossOrigin
@PreAuthorize("permitAll()")
// Handles authentication (login) and user registration logic
public class AuthenticationController {
 // // Generates JWT tokens after successful authentication
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserDao userDao;
    private ProfileDao profileDao;

    public AuthenticationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserDao userDao, ProfileDao profileDao) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
        this.profileDao = profileDao;
    }

    // Authenticates user credentials and returns a JWT token on success
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginDto loginDto) {

        // Create an authentication token using the provided username and password
        UsernamePasswordAuthenticationToken authenticationToken =
                // Authenticate the credentials using Spring Security
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // Store the authenticated user in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Generate a JWT token for the authenticated user
        String jwt = tokenProvider.createToken(authentication, false);

        try
        { // Retrieve the user record from the database
            User user = userDao.getByUserName(loginDto.getUsername());
            // If user does not exist, return 404
            if (user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

            // Add the JWT token to the response headers
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
            // Return token and user details in the response body
            return new ResponseEntity<>(new LoginResponseDto(jwt, user), httpHeaders, HttpStatus.OK);

        } catch (ResponseStatusException ex) {
            throw ex;// Re-throw known HTTP errors
        } catch (Exception ex) {   // Catch unexpected errors and return a generic server error
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.", ex);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUserDto newUser) {
        try
        {  // normalize username so exist+ create are consistent with login service
            String username = newUser.getUsername().toLowerCase().trim();
            // Verify that password and confirm password match
            if (!newUser.getPassword().equals(newUser.getConfirmPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match.");
            }
            // Check if the username already exists
            boolean exists = userDao.exists(newUser.getUsername());
            if (exists)
            { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User Already Exists.");
            }
            // Create the user record in the database
            User user = userDao.create(new User(0, username, newUser.getPassword(), newUser.getRole()));

            // Create a blank profile linked to the new user
            Profile profile = new Profile();
            profile.setUserId(user.getId());
            profileDao.create(profile);

            // Return the newly created user
            return new ResponseEntity<>(user, HttpStatus.CREATED);
    } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception e) {
            { throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");

            }
        }
    }
}





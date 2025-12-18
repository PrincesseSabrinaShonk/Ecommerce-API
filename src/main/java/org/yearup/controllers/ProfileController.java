package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/profile")         // Base route for profile-related endpoints
@CrossOrigin                        // Allows requests from frontend apps or tools like Insomnia
@PreAuthorize("isAuthenticated()")  // All endpoints require the user to be logged in
public class ProfileController
{
    private ProfileDao profileDao;   // DAO for profile database operations
    private UserDao userDao;         // DAO for user lookups

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao) //Constructor injection for ProfileDao and UserDao
    {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    // GET /profile
    // Retrieves the profile of the currently authenticated user
    @GetMapping("")
    public Profile getProfile(Principal principal)
    {
        try
        {
            String userName = principal.getName();   // Get currently logged-in username
            User user = userDao.getByUserName(userName);   // Find user in database
            if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

            int userId = user.getId();
            Profile profile = profileDao.getByUserId(userId); // Retrieve user's profile

            if(profile == null) // If no profile exists, return 404
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            return profile;
        }
        catch(ResponseStatusException ex)  // Catch unexpected errors and return a generic server response
        { throw ex;
        }  catch(Exception e)
        {  throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // PUT /profile
    // Updates the profile of the currently authenticated user
    @PutMapping("")
    public Profile updateProfile(@RequestBody Profile profile, Principal principal)
    {
        try
        {
            String userName = principal.getName();            // Get currently logged-in username
            // Verify the user exists
            User user = userDao.getByUserName(userName);
            // If there is no user, then stop what you’re doing and return an HTTP 401 Unauthorized response.
            if (user == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

            int userId = user.getId();
            // FIX: update should call update, not create
            profileDao.update(userId, profile);  // Update user's profile using their userID

            return profileDao.getByUserId(userId); // Return updated profile
        }
        catch(ResponseStatusException ex)
        {  throw ex;
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }
}
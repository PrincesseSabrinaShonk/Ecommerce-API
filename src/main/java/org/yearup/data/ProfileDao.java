package org.yearup.data;


import org.yearup.models.Profile;

public interface ProfileDao
{
    Profile create(Profile profile); // Creates a new profile record in the database.
    Profile getByUserId(int userId);   //Creates a new profile record in the database.
    void update(int userId, Profile profile);    // Updates an existing user's profile in the database

}
// need to fix

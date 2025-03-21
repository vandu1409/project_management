package com.project_managament.services;

import com.project_managament.models.UserProfile;

import java.util.List;
import java.util.Optional;

public interface UserProfileService {
    int addUserProfile(UserProfile userProfile);
    boolean updateUserProfile(UserProfile userProfile);
    boolean deleteUserProfile(int id);
    Optional<UserProfile> getUserProfileById(int id);
    List<UserProfile> getAllUserProfiles();
}

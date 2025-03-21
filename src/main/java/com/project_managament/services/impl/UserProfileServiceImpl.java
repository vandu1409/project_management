package com.project_managament.services.impl;

import com.project_managament.models.UserProfile;
import com.project_managament.repositories.UserProfileRepository;
import com.project_managament.services.UserProfileService;
import com.project_managament.utils.ValidationUtil;

import java.util.List;
import java.util.Optional;

public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public int addUserProfile(UserProfile userProfile) {
        validateUserProfile(userProfile);
        return userProfileRepository.insert(userProfile);
    }

    @Override
    public boolean updateUserProfile(UserProfile userProfile) {
        validateUserProfile(userProfile);
        return userProfileRepository.update(userProfile);
    }

    @Override
    public boolean deleteUserProfile(int id) {
        ValidationUtil.requirePositive(id, "Invalid user profile ID!");
        return userProfileRepository.delete(id);
    }

    @Override
    public Optional<UserProfile> getUserProfileById(int id) {
        ValidationUtil.requirePositive(id, "Invalid user profile ID!");
        return Optional.ofNullable(userProfileRepository.getById(id));
    }

    @Override
    public List<UserProfile> getAllUserProfiles() {
        return userProfileRepository.getAll();
    }

    private void validateUserProfile(UserProfile userProfile) {
        ValidationUtil.requireNonNull(userProfile, "User profile cannot be null!");
        ValidationUtil.validateUserProfile(userProfile.getFullname(), userProfile.getPhone());
    }
}

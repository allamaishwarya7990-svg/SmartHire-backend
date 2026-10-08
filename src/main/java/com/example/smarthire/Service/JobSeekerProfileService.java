package com.example.smarthire.Service;

import com.example.smarthire.Dto.JobSeekerProfileRequest;
import com.example.smarthire.Dto.JobSeekerProfileResponse;
import com.example.smarthire.Entity.JobSeekerProfile;
import com.example.smarthire.Entity.User;
import com.example.smarthire.Repository.JobSeekerProfileRepository;
import com.example.smarthire.Repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobSeekerProfileService {

    @Autowired
    private JobSeekerProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;


    public JobSeekerProfileResponse saveOrUpdateProfile(
            String email,
            JobSeekerProfileRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));


        if (!"JOB_SEEKER".equals(user.getRole())) {
            throw new RuntimeException(
                    "Only job seekers can create a job seeker profile"
            );
        }


        // Update basic user information
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        userRepository.save(user);


        // Find existing profile
        JobSeekerProfile profile =
                profileRepository.findByUser(user)
                        .orElse(new JobSeekerProfile());


        profile.setUser(user);

        profile.setLocation(request.getLocation());
        profile.setQualification(request.getQualification());
        profile.setDegree(request.getDegree());
        profile.setCollege(request.getCollege());
        profile.setGraduationYear(request.getGraduationYear());
        profile.setSkills(request.getSkills());
        profile.setExperienceType(request.getExperienceType());
        profile.setExperienceYears(request.getExperienceYears());


        JobSeekerProfile savedProfile =
                profileRepository.save(profile);


        return convertToResponse(savedProfile);
    }


    public JobSeekerProfileResponse getProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));


        JobSeekerProfile profile =
                profileRepository.findByUser(user)
                        .orElse(null);


        if (profile == null) {

            JobSeekerProfileResponse response =
                    new JobSeekerProfileResponse();

            response.setFullName(user.getFullName());
            response.setEmail(user.getEmail());
            response.setPhone(user.getPhone());

            return response;
        }


        return convertToResponse(profile);
    }


    private JobSeekerProfileResponse convertToResponse(
            JobSeekerProfile profile) {

        JobSeekerProfileResponse response =
                new JobSeekerProfileResponse();

        User user = profile.getUser();

        response.setId(profile.getId());

        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());

        response.setLocation(profile.getLocation());
        response.setQualification(profile.getQualification());
        response.setDegree(profile.getDegree());
        response.setCollege(profile.getCollege());
        response.setGraduationYear(profile.getGraduationYear());

        response.setSkills(profile.getSkills());

        response.setExperienceType(profile.getExperienceType());
        response.setExperienceYears(profile.getExperienceYears());

        return response;
    }
}
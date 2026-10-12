package com.example.smarthire.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.smarthire.Entity.User;
import com.example.smarthire.Entity.UserSettings;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {

    Optional<UserSettings> findByUser(User user);

    Optional<UserSettings> findByUserId(Long userId);
}

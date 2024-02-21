package com.threepmanagerapi.threepmanagerapi.settings.Repository;

import com.threepmanagerapi.threepmanagerapi.settings.Model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
}

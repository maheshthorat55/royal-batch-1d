package com.skillup.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.skillup.entity.GeneralGameSettings;

@Component
@Service
public interface GeneralGameSettingsService {

	GeneralGameSettings getSettings();

	void save(GeneralGameSettings setting);

}

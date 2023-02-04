package com.skillup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillup.entity.GeneralGameSettings;
import com.skillup.repos.GeneralGameSettingsRepository;

@Service
public class GeneralGameSettingsServiceImpl implements GeneralGameSettingsService {

	@Autowired
	private GeneralGameSettingsRepository generalGameSettings;
	
	@Override
	public GeneralGameSettings getSettings() {
		return generalGameSettings.findAll().get(0);
	}

	@Override
	public void save(GeneralGameSettings setting) {
		this.generalGameSettings.save(setting);
	}

}

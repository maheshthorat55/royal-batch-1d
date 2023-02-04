package com.skillup.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillup.entity.PGame;
import com.skillup.repos.PGameRepository;

@Service
public class PGameServiceImpl implements PGameService {

	@Autowired
	private PGameRepository gameRepo;
	
	@Override
	public PGame getGameByDateAndTime(Date date, String time) {
		return gameRepo.findByDateAndTime(date, time);
	}

	@Override
	public PGame getGameByDateAndTimeAndType(Date date, String time, int type) {
		return gameRepo.findByDateAndTimeAndType(date, time, type);
	}

}

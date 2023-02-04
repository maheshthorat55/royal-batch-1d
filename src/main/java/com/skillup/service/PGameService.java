package com.skillup.service;

import java.util.Date;

import com.skillup.entity.PGame;

public interface PGameService {

	PGame getGameByDateAndTime(Date dateFromYYYYMMDD, String time);

	PGame getGameByDateAndTimeAndType(Date dateFromYYYYMMDD, String time, int i);

}

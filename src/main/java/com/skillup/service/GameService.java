package com.skillup.service;

import java.util.Date;
import java.util.List;

import com.skillup.entity.Game;

public interface GameService {

	Game addgame(Game game);
	List<Game> getAllGames();
	Game getGameById(Long id);
	List<Game> getAllGamesByDate(Date date);
	boolean deleteGameById(Long id);
	Game updateGame(Long id, Game game);
	List<Game> addAllgames(List<Game> games);
	Game getGameByDateAndTime(Date dateFromYYYYMMDD, String time);
}

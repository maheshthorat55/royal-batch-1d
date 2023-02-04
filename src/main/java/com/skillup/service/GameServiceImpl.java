package com.skillup.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.skillup.entity.Game;
import com.skillup.repos.GameRepository;

@Service
@Component
public class GameServiceImpl implements GameService {

	@Autowired
	private GameRepository gameRepo;
	
	@Override
	public Game addgame(Game game) {
		return gameRepo.save(game);
	}

	@Override
	public List<Game> getAllGames() {
		return gameRepo.findAll();
	}

	@Override
	public Game getGameById(Long id) {
		return gameRepo.getById(id);
	}

	@Override
	public boolean deleteGameById(Long id) {
		gameRepo.deleteById(id);
		return true;
	}

	@Override
	public Game updateGame(Long id, Game game) {
		return gameRepo.save(game);
	}

	@Override
	public List<Game> getAllGamesByDate(Date date) {
		return gameRepo.findAllByDate(date);
	}

	@Override
	public List<Game> addAllgames(List<Game> games) {
		return gameRepo.saveAll(games);
	}

	@Override
	public Game getGameByDateAndTime(Date date, String time) {
		return gameRepo.findByDateAndTime(date, time);
	}

}

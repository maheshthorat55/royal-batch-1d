package com.skillup.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillup.entity.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>{

	Game findByDate(Date date);

	List<Game> findAllByDate(Date date);

	Game findByDateAndTime(Date date, String time);

}

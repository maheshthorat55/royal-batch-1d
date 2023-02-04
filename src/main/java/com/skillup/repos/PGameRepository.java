package com.skillup.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillup.entity.PGame;

@Repository
public interface PGameRepository extends JpaRepository<PGame, Long>{

	PGame findByDate(Date date);

	List<PGame> findAllByDate(Date date);

	PGame findByDateAndTime(Date date, String time);

	PGame findByDateAndTimeAndType(Date date, String time, int type);

	List<PGame> findAllByDateAndType(Date dateFromYYYYMMDD, int type);

}

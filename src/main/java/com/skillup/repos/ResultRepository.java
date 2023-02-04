package com.skillup.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillup.entity.Result;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {

	List<Result> findAllByGameId(Long gameId);

	List<Result> findAllByCreatedDate(Date date);

	Result findByGameId(long gameId);

	Result findByDateAndResultTime(Date date, String resultTime);

	List<Result> findAllByDate(Date date);
}

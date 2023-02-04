package com.skillup.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillup.entity.PResult;

@Repository
public interface PResultRepository extends JpaRepository<PResult, Long> {

	List<PResult> findAllByGameId(Long gameId);

	List<PResult> findAllByCreatedDate(Date date);

	PResult findByGameId(long gameId);

	PResult findByDateAndResultTime(Date date, String resultTime);

	List<PResult> findAllByDate(Date date);

	Page<PResult> findAllByType(int type, Pageable of);

	PResult findByDateAndResultTimeAndType(Date date, String time, int type);
}

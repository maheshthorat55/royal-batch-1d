package com.skillup.service;

import java.util.Date;
import java.util.List;

import com.skillup.entity.Result;

public interface ResultService {
	Result addResult(Result result);
	List<Result> getAllResult();
	Result getResultByGameId(Long gameId);
	List<Result> getAllResultByCreatedDate(Date date);
	Result getResultById(Long id);
	boolean deleteResultById(Long id);
	Result updateResult(Long id, Result result);
	Result getLastResult();
	Result getAllResultByDateAndTime(Date dateFromYYYYMMDD, String time);
	List<Result> getAllResultByDate(Date date);
}

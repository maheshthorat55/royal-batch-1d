package com.skillup.service;

import java.util.Date;
import java.util.List;

import com.skillup.dto.UserPreviousWinning;
import com.skillup.dto.UserSaleBean;
import com.skillup.entity.PResult;

public interface PResultService {

	PResult getAllResultByDateAndTime(Date dateFromYYYYMMDD, String time);

	PResult getResultByGameId(long gameId);

	List<UserSaleBean> getUserWiseSale(long gameId);

	List<UserPreviousWinning> getUserPreviousWinningBetweenGames(Long gameId, Long previourGameId, int type);

	void addResult(PResult res);

	PResult getLastResult();

	PResult getLastResultByType(int type);

	PResult getAllResultByDateAndTimeAndType(Date dateFromYYYYMMDD, String time, int i);

}

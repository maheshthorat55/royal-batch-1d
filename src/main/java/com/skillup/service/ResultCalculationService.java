package com.skillup.service;

import java.util.List;

import com.skillup.entity.ResultCalculations;

public interface ResultCalculationService {

	List<ResultCalculations> findResultCalculations(int gameId);
	double getTotalPurchasePointsByGame(long gameId);
	void addAll(List<ResultCalculations> resultCalculationTickets); 
}

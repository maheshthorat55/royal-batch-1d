package com.skillup.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillup.entity.ResultCalculations;
import com.skillup.repos.ResultCalculationsrepository;

@Service
public class ResultCalculationServiceImpl implements ResultCalculationService {

	@Autowired
	private ResultCalculationsrepository resultCalRepo;
	
	public List<ResultCalculations> findResultCalculations(int gameId) {
		return resultCalRepo.findAllResultCalculations(gameId);
	}

	@Override
	public double getTotalPurchasePointsByGame(long gameId) {
		Optional<Double> purchasePoints = resultCalRepo.getTotalPurchasePointsByGame(gameId);
		return purchasePoints.isPresent() ? purchasePoints.get() : 0;
	}
	
	@Override
	public void addAll(List<ResultCalculations> resultCalculationTickets) {
		resultCalRepo.saveAll(resultCalculationTickets);
	}
	
}

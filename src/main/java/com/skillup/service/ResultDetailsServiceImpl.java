package com.skillup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.skillup.entity.Result;
import com.skillup.entity.ResultDetails;
import com.skillup.repos.ResultDetailsRepository;

@Service
public class ResultDetailsServiceImpl implements ResultDetailsService {

	@Autowired
	private ResultDetailsRepository resultDetailsRepository;
	
	@Override
	@Async
	public List<ResultDetails> addResultDetails(List<ResultDetails> resultDetails) {
		return resultDetailsRepository.saveAll(resultDetails);
	}

	@Override
	public List<ResultDetails> getAllResultDetailsByResult(Result result) {
		return resultDetailsRepository.findAllByResult(result);
	}

}

package com.skillup.service;

import java.util.List;

import com.skillup.entity.Result;
import com.skillup.entity.ResultDetails;

public interface ResultDetailsService {
	List<ResultDetails> addResultDetails(List<ResultDetails> resultDetails);
	
	List<ResultDetails> getAllResultDetailsByResult(Result result);
}

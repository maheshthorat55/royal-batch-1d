package com.skillup.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillup.entity.ResultHelper;
import com.skillup.repos.ResultHelperRepository;

@Service
public class ResultHelperServiceImpl implements ResultHelperService {

	@Autowired
	private ResultHelperRepository resultHelperRepo;
	
	@Override
	public List<ResultHelper> findTicketsWithNoSelections(int gameId) {
		return resultHelperRepo.findTicketsWithNoSelections(gameId);
	}

}

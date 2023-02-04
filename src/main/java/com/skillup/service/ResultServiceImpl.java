package com.skillup.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.skillup.entity.Result;
import com.skillup.repos.ResultRepository;

@Service
public class ResultServiceImpl implements ResultService {

	@Autowired
	private ResultRepository resultRepo;
	
	@Override
	public Result addResult(Result result) {
		return resultRepo.save(result);
	}

	@Override
	public List<Result> getAllResult() {
		return resultRepo.findAll();
	}

	@Override
	public Result getResultByGameId(Long gameId) {
		return resultRepo.findByGameId(gameId);
	}

	@Override
	public List<Result> getAllResultByCreatedDate(Date date) {
		return resultRepo.findAllByCreatedDate(date);
	}

	@Override
	public Result getResultById(Long id) {
		return resultRepo.findById(id).orElseThrow(null);
	}

	@Override
	public boolean deleteResultById(Long id) {
		resultRepo.deleteById(id);
		return true;
	}

	@Override
	public Result updateResult(Long id, Result result) {
		return resultRepo.save(result);
	}

	@Override
	public Result getLastResult() {
		Page<Result> page = resultRepo.findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "resultId")));
		if(page != null && page.getContent() != null && page.getContent().size() > 0) {
			return page.getContent().get(0);	
		} else {
			return null;
		}
	}

	@Override
	public Result getAllResultByDateAndTime(Date date, String resultTime) {
		return resultRepo.findByDateAndResultTime(date, resultTime);
	}

	@Override
	public List<Result> getAllResultByDate(Date date) {
		return resultRepo.findAllByDate(date);
	}

}

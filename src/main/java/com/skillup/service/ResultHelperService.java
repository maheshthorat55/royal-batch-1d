package com.skillup.service;

import java.util.List;

import com.skillup.entity.ResultHelper;

public interface ResultHelperService {

	List<ResultHelper> findTicketsWithNoSelections(int id);

}

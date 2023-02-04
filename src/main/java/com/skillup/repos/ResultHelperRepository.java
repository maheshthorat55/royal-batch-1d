package com.skillup.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.skillup.entity.ResultHelper;

public interface ResultHelperRepository extends JpaRepository<ResultHelper, Long>{
	
	@Query(value = "CALL ticketnumbers_no_selection(:gameId);", nativeQuery = true)
	List<ResultHelper> findTicketsWithNoSelections(int gameId);

}

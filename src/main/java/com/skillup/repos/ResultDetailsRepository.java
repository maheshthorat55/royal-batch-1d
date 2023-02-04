package com.skillup.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillup.entity.Result;
import com.skillup.entity.ResultDetails;

@Repository
public interface ResultDetailsRepository extends JpaRepository<ResultDetails, Long>{

	List<ResultDetails> findAllByResult(Result result);

}

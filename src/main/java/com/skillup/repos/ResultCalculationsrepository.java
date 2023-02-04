package com.skillup.repos;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.skillup.entity.ResultCalculations;

@Repository
public interface ResultCalculationsrepository extends JpaRepository<ResultCalculations, Long> {

	@Query(value = "CALL ticket_summary_by_game_id(:gameId);", nativeQuery = true)
	List<ResultCalculations> findAllResultCalculations(int gameId);
	
	@Query(value = "CALL ticketnumbers_with_no_selection(:gameId);", nativeQuery = true)
	List<ResultCalculations> findTicketsWithNoSelections(int gameId);
	
	@Query(value = "CALL salereport(:fdate, :tdate, :ruserId, :userType);", nativeQuery = true)
	List<Object> findResult(Date fdate, Date tdate, int ruserId, int userType);
	
	@Query(value = "CALL resultreport(:fdate, :tdate);", nativeQuery = true)
	List<Object> findWiningResult(Date fdate, Date tdate);

	@Query(value = "CALL gameresult(:fdate, :tdate);", nativeQuery = true)
	List<Object> findGameResult(Date fdate, Date tdate);

	@Query(value = "CALL salereportmanager(:fdate, :tdate, :ruserId, :userType);", nativeQuery = true)
	List<Object> findResultForManager(Date fdate, Date tdate, int ruserId, int userType);
	
	@Query(value = "select sum(purchase_points) as totalSalePoints FROM ticket where canceled=0 and game_game_id=?1", nativeQuery = true)
	Optional<Double> getTotalPurchasePointsByGame(long gameId);
	
	
}

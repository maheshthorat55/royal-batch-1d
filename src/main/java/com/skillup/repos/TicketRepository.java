package com.skillup.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.skillup.entity.Game;
import com.skillup.entity.Ticket;
import com.skillup.entity.User;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

	List<Ticket> findAllByUser(User user);

	List<Ticket> findAllByUserAndCanceled(User user, boolean canceled);
	
	@Modifying
	@Query(value = "update ticket set barcode=?2 where ticket_id = ?1", nativeQuery = true)
	void updateTicket(Long ticketId, String barcode);

	Ticket findByTicketIdAndGame(Long ticketId, Game game);

	Ticket findByGame(Game game);

	List<Ticket> findAllByGame(Game game);

	List<Ticket> findAllByUserAndDateBetween(User user, Date fDate, Date tDate);
	
	@Query(value = "CALL pointsummary_user(:fDate, :tDate, :userId);", nativeQuery = true)
	Object getPointSummaryResult(Date fDate, Date tDate, long userId);
	
	@Query(value = "CALL pointsummary_user_admin(:fDate, :tDate, :userId);", nativeQuery = true)
	List<Object> getPointSummaryResultAdmin(Date fDate, Date tDate, long userId);
	
	@Query(value = "CALL pointsummary_user_dstbtr(:fDate, :tDate, :userId);", nativeQuery = true)
	List<Object> getPointSummaryResultForDstbtr(Date fDate, Date tDate, long userId);
	
	//for Manager only
	@Query(value = "CALL pointsummary_user_ntpbr_for_mgrview(:fDate, :tDate, :userId);", nativeQuery = true)
	List<Object> getPointSummaryResultForNTPBR(Date fDate, Date tDate, long userId);
	
	
	@Query(value = "CALL pointsummary_user_mngr_summary(:fDate, :tDate);", nativeQuery = true)
	List<Object> getPointSummaryForForMngrDash(Date fDate, Date tDate);
	
	@Query(value = "CALL pointsummary_user_dstbtr_summary(:fDate, :tDate, :userId);", nativeQuery = true)
	List<Object> getPointSummaryForDstbtrDash(Date fDate, Date tDate, long userId);

	Page<Ticket> findByUser(User byId, Pageable pageRequest);
	
	@Query(value = "SELECT ticket_id, date, draw_time, purchase_points, game_game_id FROM ticket where user_user_id = ?1 order by ticket_id desc limit ?2", nativeQuery = true)
	List<Object> findByUserAndSize(long useId, int size);

	List<Ticket> findAllByUserAndCanceledAndDate(User user, boolean canceled, Date dateToday);

	List<Ticket> findAllByTicketIdIn(List<Long> ticketIds);
	
	@Query(value = "CALL dailywinningsummary(:fDate, :tDate);", nativeQuery = true)
	List<Object> dailyWinningSummary(Date fDate, Date tDate);

	Page<Ticket> findByUserInAndDateBetween(List<User> users, Date fDate, Date tDate, Pageable paging);

	@Query(value = "CALL winning_for_ticket(:fdate,:time,:winningNumber);", nativeQuery = true)
	List<Object> winningNumberSummary(Date fdate, String time, int winningNumber);
	
	Page<Ticket> findByDateBetween(Date fDate, Date tDate, Pageable paging);
	
	@Query(value = "CALL find_ticket_for_series(:gameId, :series);", nativeQuery = true)
	List<Object> ticketForSeries(Long gameId, String series);

	@Query(value = "CALL dailywinningsummary_new(:fDate, :tDate, :gameId);", nativeQuery = true)
	List<Object> dailyWinningSummary(Date fDate, Date tDate, Long gameId);

	@Query(value = "CALL userwinning_number_game(:gameId);", nativeQuery = true)
	List<Object> ticketsForGameWithUser(long gameId);
	
	@Query(value = "CALL tickets_by_user_for_game(:gameId);", nativeQuery = true)
	List<Object> ticketsGameWithUser(long gameId);

	@Query(value = "CALL dailywinningsummary_fromgame_to_game(:fDate, :tDate, :gameId, :previousGameId);", nativeQuery = true)
	List<Object> dailyWinningSummaryFromToGame(Date fDate, Date tDate, long gameId, long previousGameId);

}

package com.skillup.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.skillup.entity.Game;
import com.skillup.entity.Ticket;
import com.skillup.entity.User;

public interface TicketService {
	List<Ticket> getAllTicket();
	Ticket getTicketById(Long id);
	boolean deleteTicketById(Long id);
	Ticket updateTicket(Long id, Ticket ticketPurchange);
	List<Ticket> getAllTicketByUser(User user);
	List<Ticket> getAllTicketByUserAndCanceled(User user, boolean b) throws ParseException;
	void updateTicket(Long ticketId, String barcode);
	Ticket getTicketByIdAndGame(Long ticketId, Game game);
	List<Ticket> getTicketByGame(Game gameById);
	List<Ticket> getAllTicketByUserAndDateBetween(User user, Date fDate, Date tDate);
	Object getPointSummaryResult(long userId, Date dateFromYYYYMMDD, Date dateFromYYYYMMDD2);
	List<Object> getAllTicketByUserIdAndSize(long userId, int size);
	List<Object> getPointSummaryResultAdmin(long userId, Date dateFromYYYYMMDD, Date dateFromYYYYMMDD2);
	List<Ticket> getTicketByIdIn(List<Long> ticketIds);
	Page<Ticket> getTicketHistory(Pageable paging, Date fDate, Date tDate, List<User> users);
	Page<Ticket> getTicketHistoryForManager(Pageable paging, Date fDate, Date tDate);
	List<Object> ticketsForSeries(Long gameId, String series);
	List<Object> dailyWinningSummary(Date date, Date date2, long gameId);
	List<Object> ticketsForGameWithUser(long gameId);
	List<Object> ticketsGameWithUser(long gameId);
	List<Object> dailyWinningSummaryFromToGame(Date fdate, Date tdate, long gameId, long previousGameId);
}

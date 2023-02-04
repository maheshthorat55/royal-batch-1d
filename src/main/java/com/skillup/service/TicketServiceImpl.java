package com.skillup.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.skillup.entity.Game;
import com.skillup.entity.Ticket;
import com.skillup.entity.User;
import com.skillup.repos.TicketRepository;
import com.skillup.utility.DateUtility;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketRepository ticketRepo;
	
	
	@Override
	public List<Ticket> getAllTicket() {
		return ticketRepo.findAll();
	}

	@Override
	public Ticket getTicketById(Long id) {
		return ticketRepo.getById(id);
	}

	@Override
	public boolean deleteTicketById(Long id) {
		ticketRepo.deleteById(id);		
		return true;
	}

	@Override
	public Ticket updateTicket(Long id, Ticket ticketPurchange) {
		return ticketRepo.save(ticketPurchange);
	}

	@Override
	public List<Ticket> getAllTicketByUser(User user) {
		return ticketRepo.findAllByUser(user);
	}

	@Override
	public List<Ticket> getAllTicketByUserAndCanceled(User user, boolean canceled) throws ParseException {
		return ticketRepo.findAllByUserAndCanceledAndDate(user, canceled,DateUtility.getDateToday());
	}


	@Override
	@Transactional
	public void updateTicket(Long ticketId, String barcode) {
		ticketRepo.updateTicket(ticketId, barcode);
	}

	@Override
	public Ticket getTicketByIdAndGame(Long ticketId, Game game) {
		return ticketRepo.findByTicketIdAndGame(ticketId, game);
	}

	@Override
	public List<Ticket> getTicketByGame(Game game) {
		return ticketRepo.findAllByGame(game);
	}

	@Override
	public List<Ticket> getAllTicketByUserAndDateBetween(User user, Date fDate, Date tDate) {
		return ticketRepo.findAllByUserAndDateBetween(user, fDate, tDate);
	}

	@Override
	public Object getPointSummaryResult(long userId, Date fdate, Date tdate) {
		return ticketRepo.getPointSummaryResult(fdate, tdate, userId);
	}
	
	public List<Object> getAllTicketByUserIdAndSize(long userId, int size) {
		return ticketRepo.findByUserAndSize(userId, size);
	}

	@Override
	public List<Object> getPointSummaryResultAdmin(long userId, Date fdate, Date tdate) {
		return ticketRepo.getPointSummaryResultAdmin(fdate, tdate, userId);
	}

	@Override
	public List<Ticket> getTicketByIdIn(List<Long> ticketIds) {
		return ticketRepo.findAllByTicketIdIn(ticketIds);
	}

//	@Override
//	public List<Object> dailyWinningSummary(Date fdate, Date tdate) {
//		return ticketRepo.dailyWinningSummary(fdate, tdate);
//	}

	@Override
	public Page<Ticket> getTicketHistory(Pageable paging, Date fDate, Date tDate, List<User> users) {
		return ticketRepo.findByUserInAndDateBetween(users, fDate, tDate, paging);
	}
	
	@Override
	public Page<Ticket> getTicketHistoryForManager(Pageable paging, Date fDate, Date tDate) {
		return ticketRepo.findByDateBetween(fDate, tDate, paging);
	}
	
	@Override
	public List<Object> ticketsForSeries(Long gameId, String series) {
		return ticketRepo.ticketForSeries(gameId, series);
	}

	@Override
	public List<Object> dailyWinningSummary(Date fdate, Date tdate, long gameId) {
		return ticketRepo.dailyWinningSummary(fdate, tdate, gameId);
	}

	@Override
	public List<Object> ticketsForGameWithUser(long gameId) {
		return ticketRepo.ticketsForGameWithUser(gameId);
	}
	
	@Override
	public List<Object> ticketsGameWithUser(long gameId) {
		return ticketRepo.ticketsGameWithUser(gameId);
	}

	@Override
	public List<Object> dailyWinningSummaryFromToGame(Date fdate, Date tdate, long gameId, long previousGameId) {
		return ticketRepo.dailyWinningSummaryFromToGame(fdate, tdate, gameId, previousGameId);
	}


}

package com.skillup.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skillup.entity.Ticket;
import com.skillup.entity.TicketDetails;
import com.skillup.repos.TicketDetailsRepository;

@Service
public class TicketDetailsServiceImpl implements TicketDetailsService {

	@Autowired
	private TicketDetailsRepository ticketDetailsRepository;
	
	@Override
	public List<TicketDetails> getTicketDetailsByTicketDetailsIdIn(Set<Long> ticketDetailsId, long gameId) {
		return ticketDetailsRepository.findAllByTicketDetailsIdInAndGame(ticketDetailsId, gameId);
	}

	@Override
	public void saveAll(List<TicketDetails> tickets) {
		ticketDetailsRepository.saveAll(tickets);
	}

	@Override
	public List<TicketDetails> findAllTicketsByGameId(long gameId) {
		return ticketDetailsRepository.findAllByGameId(gameId);
	}

	@Override
	public List<TicketDetails> getAllTicketsByTicketId(Ticket ticketId) {
		return ticketDetailsRepository.findAllByTicket(ticketId);
	}

}

package com.skillup.service;

import java.util.List;
import java.util.Set;

import com.skillup.entity.Ticket;
import com.skillup.entity.TicketDetails;

public interface TicketDetailsService {
	
	void saveAll(List<TicketDetails> tickets);

	List<TicketDetails> getTicketDetailsByTicketDetailsIdIn(Set<Long> ticketIds, long gameId);

	List<TicketDetails> findAllTicketsByGameId(long gameId);

	List<TicketDetails> getAllTicketsByTicketId(Ticket ticketId);

}

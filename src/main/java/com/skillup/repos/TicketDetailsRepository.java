package com.skillup.repos;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.skillup.entity.Ticket;
import com.skillup.entity.TicketDetails;

@Repository
public interface TicketDetailsRepository extends JpaRepository<TicketDetails, Long> {

	List<TicketDetails> findAllByTicketDetailsIdIn(long[] ticketDetailsId);

	@Query("select td from Game g INNER JOIN g.tickets t INNER JOIN t.tickets td where td.ticketDetailsId in:ticketDetailsId and g.gameId=:gameId")
	List<TicketDetails> findAllByTicketDetailsIdInAndGame(Set<Long> ticketDetailsId, long gameId);
	
	
	@Query("select td from TicketDetails td INNER JOIN td.ticket t INNER JOIN t.game g where g.gameId=:gameId")
	List<TicketDetails> findAllByGameId(long gameId);

	List<TicketDetails> findAllByTicket(Ticket ticketId);

}

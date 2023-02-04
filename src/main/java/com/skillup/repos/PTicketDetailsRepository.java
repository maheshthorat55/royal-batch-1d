package com.skillup.repos;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.skillup.entity.PTicket;
import com.skillup.entity.PTicketDetails;
import com.skillup.entity.TicketDetails;

@Repository
public interface PTicketDetailsRepository extends JpaRepository<PTicketDetails, Long> {

	List<TicketDetails> findAllByTicketDetailsIdIn(long[] ticketDetailsId);

	@Query("select td from PGame g INNER JOIN g.tickets t INNER JOIN t.tickets td where td.ticketDetailsId in:ticketDetailsId and g.gameId=:gameId")
	List<PTicketDetails> findAllByTicketDetailsIdInAndGame(Set<Long> ticketDetailsId, long gameId);
	
	
	@Query("select td from PTicketDetails td INNER JOIN td.ticket t INNER JOIN t.game g where g.gameId=:gameId")
	List<PTicketDetails> findAllByGameId(long gameId);

	List<PTicketDetails> findAllByTicket(PTicket ticketId);

}

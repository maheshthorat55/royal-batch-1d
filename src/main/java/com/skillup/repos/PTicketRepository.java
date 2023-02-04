package com.skillup.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillup.entity.PTicket;
import com.skillup.entity.Ticket;
import com.skillup.entity.User;

@Repository
public interface PTicketRepository extends JpaRepository<PTicket, Long> {

	List<Ticket> findAllByUser(User user);

	List<Ticket> findAllByUserAndCanceled(User user, boolean canceled);

}

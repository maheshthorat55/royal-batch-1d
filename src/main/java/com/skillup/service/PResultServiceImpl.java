package com.skillup.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.skillup.dto.UserPreviousWinning;
import com.skillup.dto.UserSaleBean;
import com.skillup.entity.PResult;
import com.skillup.repos.PResultRepository;

@Service
public class PResultServiceImpl implements PResultService {

	@Autowired
	private PResultRepository presultRepo;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public PResult getAllResultByDateAndTime(Date date, String time) {
		return presultRepo.findByDateAndResultTime(date, time);
	}

	@Override
	public PResult getResultByGameId(long gameId) {
		return presultRepo.findByGameId(gameId);
	}

	@Override
	public List<UserSaleBean> getUserWiseSale(long gameId){
		String query = "SELECT sum(total_sale_points) as sale, sum(wining_points) as winning, ticket_number as ticketNumber, user_id as userId FROM presult_calculations where game_id = "+ gameId +"\r\n"
				+ "group by user_id, ticket_number";
		return jdbcTemplate.query(query, BeanPropertyRowMapper.newInstance(UserSaleBean.class));
	}
	
	@Override
	public List<UserPreviousWinning> getUserPreviousWinningBetweenGames(Long gameId, Long previourGameId, int type){
		return jdbcTemplate.query("select tt.user_user_id as userId, sum(t.quantity * t.multiplier) as salePoints, sum(t.wining_points) as winning,  (sum(t.quantity * t.multiplier) / 100) * u.commition as commition, \r\n"
				+ "     IFNULL(u.winning_percent, 0) as winningPercent, \r\n"
				+ "    IFNULL(u.winning_limit_upto, 0) as winningLimitUpto, 0 as target, (sum(t.quantity * t.multiplier) - sum(t.wining_points)) as differanceWinning FROM pticket_details t \r\n"
				+ "    inner join pticket tt on tt.ticket_id = t.ticket_ticket_id \r\n"
				+ "    and tt.canceled = 0 and tt.type = "+ type +" and tt.game_game_id >= "+ previourGameId +" and tt.game_game_id <= "+ gameId +"\r\n"
				+ "	inner join user u on tt.user_user_id = u.user_id group by tt.user_user_id", BeanPropertyRowMapper.newInstance(UserPreviousWinning.class));
	}

	@Override
	public void addResult(PResult res) {
		presultRepo.save(res);
	}

	@Override
	public PResult getLastResult() {
		Page<PResult> page = presultRepo.findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "resultId")));
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			return page.getContent().get(0);
		} else {
			return null;
		}
	}

	@Override
	public PResult getLastResultByType(int type) {
		Page<PResult> page = presultRepo.findAllByType(type, PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "resultId")));
		if (page != null && page.getContent() != null && page.getContent().size() > 0) {
			return page.getContent().get(0);
		} else {
			return null;
		}
	}

	@Override
	public PResult getAllResultByDateAndTimeAndType(Date date, String time, int type) {
		return presultRepo.findByDateAndResultTimeAndType(date, time, type);
	}
	
}

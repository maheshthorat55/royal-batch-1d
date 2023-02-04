package com.skillup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameTableMgtServiceImpl implements GameTableMgtService {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public void updatePGame(long gameId) {
		jdbcTemplate.execute("update game_table_mgt set pgame_id = " + gameId + " where id = 2");
	}
	
	@Override
	public void update3DPGame(long gameId) {
		jdbcTemplate.execute("update game_table_mgt set p3dgame_id = " + gameId + " where id = 2");
	}

}

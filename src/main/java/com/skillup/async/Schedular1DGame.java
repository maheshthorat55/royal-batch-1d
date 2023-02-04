package com.skillup.async;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.skillup.dto.ResultTicket;
import com.skillup.dto.UserPreviousWinning;
import com.skillup.dto.UserSaleBean;
import com.skillup.entity.GeneralGameSettings;
import com.skillup.entity.PGame;
import com.skillup.entity.PResult;
import com.skillup.entity.PTicketDetails;
import com.skillup.repos.PGameRepository;
import com.skillup.repos.PTicketDetailsRepository;
import com.skillup.service.GameTableMgtService;
import com.skillup.service.GeneralGameSettingsService;
import com.skillup.service.PGameService;
import com.skillup.service.PResultService;
import com.skillup.utility.DateUtility;
import com.skillup.utility.ResultTenCalculation;
import com.skillup.utility.SchedularSharedClass;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Schedular1DGame {

	@Autowired
	private GeneralGameSettingsService generalGameSettingsServiceImpl;

	@Autowired
	private PResultService presultService;

	@Autowired
	private PGameService pgameService;

	@Autowired
	private PGameRepository pgameRepo;

	@Autowired
	private PTicketDetailsRepository pTicketDetailsRepository;

	@Autowired
	GameTableMgtService gameTableMgtService;

	@Value("${skillup.2d-multiplier}")
	private Integer multiplier;

	@Scheduled(fixedRate = 1000)
	public void calculateResultAsync() throws InterruptedException {
		LocalDate localDate = LocalDate.now();
		LocalTime time = LocalTime.now().plusSeconds(10);
		String result = localDate + "-" + String.format("%02d", time.getHour()) + ":"
				+ String.format("%02d", time.getMinute());
		if (SchedularSharedClass.getSchedularSharedInstance().getTimeToAddTenKaDum().contains(time.getMinute() + "")) {
			log.info("Calculation result called Das Ka Dum [{}]", result);
			calculateResultTenKaDum(localDate + "",
					String.format("%02d", time.getHour()) + ":" + String.format("%02d", time.getMinute()),
					String.format("%02d", time.getHour()) + ":00", result);
			updateTicketsWithWinningForTen();
			Thread.sleep(10000);
		}
		Thread.sleep(2000);
	}

	@Async
	@Scheduled(fixedRate = 1000 * 60 * 4)
	public void scheduleFixedRateTaskCreatePGameAsync() throws InterruptedException {
		LocalDate localDate = LocalDate.now();
		List<PGame> games;
		try {
			if (!StringUtils.equalsIgnoreCase(localDate + "",
					SchedularSharedClass.getSchedularSharedInstance().getLastGameCreationDate())) {
				games = pgameRepo.findAllByDateAndType(DateUtility.getDateFromYYYYMMDD(localDate + ""), 1);
				if (ObjectUtils.isEmpty(games)) {
					createPGamesForDay(5, 1);
					System.out.println("Game created for the day 10 ka dum");
				} else {
					SchedularSharedClass.getSchedularSharedInstance().setLastGameCreationDate(localDate + "");
				}
			}

		} catch (ParseException e) {
			log.error("Exception: ", e);
		}
	}

	@Async
	@Scheduled(fixedRate = 1000 * 60 * 4)
	public void scheduleFixedRateTenUpdateWinningAsync() throws InterruptedException {
		try {
			updateTicketsWithWinningForTen();
		} catch (Exception e) {
			log.error("Exception: ", e);
		}
	}

	private void calculateResultTenKaDum(String localDate, String time, String hourStartGameTime, String storeResult) {
		try {
			PResult existResult = presultService
					.getAllResultByDateAndTimeAndType(DateUtility.getDateFromYYYYMMDD(localDate), time, 1);
			if (existResult == null) {

				long timemilies = System.currentTimeMillis();

				PGame game = pgameService.getGameByDateAndTimeAndType(DateUtility.getDateFromYYYYMMDD(localDate), time,
						1);

				PGame hourStartGame = pgameService
						.getGameByDateAndTimeAndType(DateUtility.getDateFromYYYYMMDD(localDate), hourStartGameTime, 1);
				log.info("time taken in milies for fetch game {}", System.currentTimeMillis() - timemilies);
				timemilies = System.currentTimeMillis();
				if (game == null) {
					log.info("Calculation result game not created [{}]", storeResult);
					return;
				}
				existResult = presultService.getResultByGameId(game.getGameId());
				log.info("time taken in milies for fetch existResult {}", System.currentTimeMillis() - timemilies);
				timemilies = System.currentTimeMillis();
				if (existResult != null) {
					log.info("Result already generated game id {} stored result [{}]", game.getGameId(), storeResult);
					return;
				}
				gameTableMgtService.updatePGame(game.getGameId());
				GeneralGameSettings setting = generalGameSettingsServiceImpl.getSettings();
				log.info("time taken in milies for fetch setting {}", System.currentTimeMillis() - timemilies);
				timemilies = System.currentTimeMillis();

				List<UserSaleBean> saleForGame = presultService.getUserWiseSale(game.getGameId());
				log.info("time taken in milies for fetch user sale {}", System.currentTimeMillis() - timemilies);
				timemilies = System.currentTimeMillis();

				List<UserPreviousWinning> userPreviousWinning = presultService
						.getUserPreviousWinningBetweenGames(game.getGameId(), hourStartGame.getGameId(), 1);
				log.info("time taken in milies for fetch previous hour game sale and winning {}",
						System.currentTimeMillis() - timemilies);
				timemilies = System.currentTimeMillis();

				ResultTenCalculation calculator = ResultTenCalculation.builder().saleForGame(saleForGame)
						.userPreviousWinning(userPreviousWinning).setting(setting).build();
				calculator.calculateResult();
				ResultTicket result = calculator.getResult();

				log.info("time taken in milies for calculate result {}", System.currentTimeMillis() - timemilies);
				timemilies = System.currentTimeMillis();

				PResult res = new PResult();
				res.setGameId(game.getGameId());
				res.setDate(DateUtility.getDateFromYYYYMMDD(localDate));
				res.setResultTime(time);
				res.setType(1);
				res.setTicketNumbers(result.getTicketNumder());
				log.info("Result details [{}]", result);
				presultService.addResult(res);
				setting.setWinningNumber1D("");
				generalGameSettingsServiceImpl.save(setting);
				log.info("time taken in milies for save result ten ka dum {}", System.currentTimeMillis() - timemilies);
			}
		} catch (ParseException e) {
			log.error("Exception: ", e);
		}
	}

	private void updateTicketsWithWinningForTen() {
		log.info("Winning updated 10 ka Dum");
		PResult result = presultService.getLastResultByType(1);
		if (ObjectUtils.isNotEmpty(result)) {
			List<PTicketDetails> ticket = pTicketDetailsRepository.findAllByGameId(result.getGameId());
			List<PTicketDetails> tickets = ticket.stream()
					.filter(p -> result.getTicketNumbers() == p.getTicketNumbers()).collect(Collectors.toList());
			if (tickets.size() > 0) {
				tickets.stream().forEach(s -> s.setWiningPoints(s.getQuantity() * s.getMultiplier() * multiplier));
				pTicketDetailsRepository.saveAll(tickets);
			}
		}
	}

	private void createPGamesForDay(int mins, int type) {
		DateTimeFormatter simpleDateFormat = new DateTimeFormatterFactory("HH:mm").createDateTimeFormatter();
		LocalTime time = LocalTime.of(00, 00, 00);
		List<PGame> games = new ArrayList<PGame>(289);
		for (int i = 1; i <= ((288 * 5) / mins); i++) {
			games.add(new PGame(i, time.format(simpleDateFormat), new Date(), type));
			time = time.plusMinutes(mins);
		}
		pgameRepo.saveAll(games);
	}
}

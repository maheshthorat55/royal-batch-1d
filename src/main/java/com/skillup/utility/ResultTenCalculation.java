package com.skillup.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.skillup.dto.ResultTicket;
import com.skillup.dto.UserPreviousWinning;
import com.skillup.dto.UserSaleBean;
import com.skillup.entity.GeneralGameSettings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class ResultTenCalculation {
	
	private int target;
	private List<UserSaleBean> saleForGame;
	private List<UserPreviousWinning> userPreviousWinning;
	private GeneralGameSettings setting;
	private ResultTicket result;
	
	public void calculateResult() {
		this.claculateGameResult(target);
	}
	
	public void claculateGameResult(int targetSum) {

		List<Integer> availableWithNoSelections = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
		
		try {
			if(StringUtils.isNumeric(setting.getWinningNumber1D())) {
				log.info("Generel setting number {}", setting.getWinningNumber1D());
				Integer winnNumber = Integer.parseInt(setting.getWinningNumber1D());
				if(availableWithNoSelections.contains(winnNumber)) {
					this.result = new ResultTicket(winnNumber, 1, Integer.parseInt("0"));
					return;
				}
			}
		} catch (Exception e) {
			log.error("Error in setting number");
		}
		
		Map<Integer, Long> numberWiseWinning = saleForGame.stream().collect(
				Collectors.groupingBy(UserSaleBean::getTicketNumber, Collectors.summingLong(UserSaleBean::getWinning)));
		
		Map<Long, List<Integer>> numberWiseWinningSortedDesc = numberWiseWinning.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.groupingBy(e->e.getValue(), Collectors.mapping(
                        e->e.getKey(),
                        Collectors.toList()
                   )));
		
		availableWithNoSelections = availableWithNoSelections.stream().filter(n->!numberWiseWinning.containsKey(n)).collect(Collectors.toList());

		Map<Integer, List<UserSaleBean>> numberAndUserWiseWinning = saleForGame.stream().collect(
				Collectors.groupingBy(UserSaleBean::getTicketNumber, Collectors.toList()));
		
//		Map<Long, List<UserSaleBean>> userWiseWinning = saleForGame.stream().collect(
//				Collectors.groupingBy(UserSaleBean::getUserId, Collectors.toList()));
		
		Map<Long, UserPreviousWinning> validForWinings = userPreviousWinning.stream().filter(isValidUserForWin())
					.map(s->addTarget(s)).sorted(Comparator.comparing(UserPreviousWinning::getTarget).reversed()).collect(Collectors.toMap(UserPreviousWinning::getUserId, Function.identity()));
		
		Map<Long, Long> validUserWithWinings = userPreviousWinning.stream().filter(isValidUserForWin())
				.map(s->addTarget(s)).sorted(Comparator.comparing(UserPreviousWinning::getTarget).reversed()).collect(Collectors.toMap(UserPreviousWinning::getUserId, UserPreviousWinning::getTarget));
		
		List<Integer> numbers = new ArrayList<Integer>();
		for (Entry<Long, List<Integer>> numberList : numberWiseWinningSortedDesc.entrySet()) {
			numbers = numberList.getValue();
			Integer number = 0;
			while(!numbers.isEmpty()) {
				int index = ThreadLocalRandom.current().nextInt(0, numbers.size());
				number = numbers.get(index);
				
				List<UserSaleBean> numberWiseUsers = numberAndUserWiseWinning.get(number);
				
				log.info("user check for number {} ", number);
				if(validForWinings.size() < numberWiseUsers.size()) {
					log.info("user check for number {} failed contain more users than valid", number);
					numbers.remove(index);
					continue;
				}
				
				Optional<UserSaleBean> invalidUser = numberWiseUsers.stream().filter(u->!validUserWithWinings.containsKey(u.getUserId()) || u.getWinning() > validUserWithWinings.get(u.getUserId())).findAny();
				
				if(invalidUser.isPresent()) {
					log.info("user check for number {} failed contain invalid user {} target {}", number, invalidUser.get(), validForWinings.get(invalidUser.get().getUserId()));
					numbers.remove(index);
					continue;
				}
				
				numberWiseUsers.stream().forEach(u->{
					log.info("valid user {} target {} ", u, validForWinings.get(u.getUserId()));
				});
				
				this.result = new ResultTicket(number, 1,
						Integer.parseInt(numberList.getKey() + ""));
				break;
			
			}
			
			if(ObjectUtils.isNotEmpty(this.result)) {
				return;
			}
			
		}
		
//		for (Entry<Long, List<Integer>> number : numberWiseWinningSortedDesc.entrySet()) {
//			
//			
//			
//			
//			List<UserSaleBean> numberWiseUsers = numberAndUserWiseWinning.get(number.getKey());
//			
//			log.info("user check for number {} ", number.getKey());
//			if(validForWinings.size() < numberWiseUsers.size()) {
//				log.info("user check for number {} failed contain more users than valid", number.getKey());
//				continue;
//			}
//			
//			Optional<UserSaleBean> invalidUser = numberWiseUsers.stream().filter(u->!validUserWithWinings.containsKey(u.getUserId()) || u.getWinning() > validUserWithWinings.get(u.getUserId())).findAny();
//			
//			if(invalidUser.isPresent()) {
//				log.info("user check for number {} failed contain invalid user {} target {}", number.getKey(), invalidUser.get(), validForWinings.get(invalidUser.get().getUserId()));
//				continue;
//			}
//			
//			numberWiseUsers.stream().forEach(u->{
//				log.info("valid user {} target {} ", u, validForWinings.get(u.getUserId()));
//			});
//			
//			this.result = new ResultTicket(number.getKey(), 1,
//					Integer.parseInt(number.getValue() + ""));
//			break;
//		}
		
		
//		validForWinings.entrySet().forEach(validUser -> {
//			if (userWiseWinning.containsKey(validUser.getKey())) {
//				log.info("user check for user {} ", validUser.getValue());
//
//				Long targetWin = validUser.getValue().getTarget();
//
//				List<UserSaleBean> userWin = userWiseWinning.get(validUser.getKey()).stream()
//						.filter(l -> l.getWinning() <= targetWin)
//						.sorted(Comparator.comparing(UserSaleBean::getWinning).reversed()).collect(Collectors.toList());
//				for (UserSaleBean win : userWin) {
//					if (win.getWinning() + validUser.getValue().getWinning() <= validUser.getValue().getTarget()) {
//						List<UserSaleBean> otherWinning = numberAndUserWiseWinning.get(win.getTicketNumber());
//						if (otherWinning.size() > 1) {
//							boolean valid = checkValidityForOthers(otherWinning, validForWinings);
//							if (valid) {
//								this.result = new ResultTicket(win.getTicketNumber(), 1,
//										Integer.parseInt(win.getWinning() + ""));
//								log.info("found result {} ", this.result);
//								break;
//							}
//						} else {
//							this.result = new ResultTicket(win.getTicketNumber(), 1,
//									Integer.parseInt(win.getWinning() + ""));
//							log.info("found result {} ", this.result);
//							break;
//						}
//					}
//				}
//			}
//		});
		
		if(ObjectUtils.isNotEmpty(this.result)) {
			return;
		}
		
		log.info("Available no selection {}", availableWithNoSelections);
		
		if(ObjectUtils.isNotEmpty(availableWithNoSelections)) {
			int index = ThreadLocalRandom.current().nextInt(0, availableWithNoSelections.size());
			this.result = new ResultTicket(availableWithNoSelections.get(index), 1, Integer.parseInt("0"));
			return;
		}
		
		Map<Long, List<Integer>> numberWiseWinningSorted = numberWiseWinning.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                .collect(Collectors.groupingBy(e->e.getValue(), Collectors.mapping(
                        e->e.getKey(),
                        Collectors.toList()
                   )));
		
		numberWiseWinningSorted.entrySet().forEach(first -> {
			log.info("selected min winning key [{}] value [{}]", first.getKey(), first.getValue());
			List<Integer> numbervals = first.getValue();
			int index = ThreadLocalRandom.current().nextInt(0, numbervals.size());
			this.result = new ResultTicket(numbervals.get(index), 1, Integer.parseInt("0"));
			return;
		});
		
		
	}
	
//	private boolean checkValidityForOthers(List<UserSaleBean> otherWinning, Map<Long, UserPreviousWinning> validForWinings) {
//		for(UserSaleBean win: otherWinning) {
//			UserPreviousWinning prevWin = validForWinings.get(win.getUserId());
//			if(ObjectUtils.isNotEmpty(prevWin) && prevWin.getTarget() <= prevWin.getWinning() + win.getWinning()) {
//				log.info("Valid win == {}", win);
//			} else {
//				log.info("In-Valid win == {}", win);
//				return false;
//			}
//		};
//		return true;
//	}

	private UserPreviousWinning addTarget(UserPreviousWinning user) {
		int winPercentage = setting.getPwinningPercentage();
		if(user.getWinningPercent() > 0) {
			winPercentage = user.getWinningPercent();
		}
		
		int tWinning = (int) ((user.getSalePoints() * (winPercentage) / 100)
				- user.getWinning());
		
		if(user.getWinningPercent() > 100 && user.getWinningLimitUpto() > 0) {
			int cMargin = (int) (user.getSalePoints() - (user.getWinning() + tWinning));
			if(cMargin < 0) {
				if((cMargin*-1) > user.getWinningLimitUpto()) {
					tWinning = tWinning - ((cMargin*-1) - user.getWinningLimitUpto());
				}
			}
		}
		
		if(user.getCommition() > 0) {
			tWinning = (int) (tWinning - user.getCommition());
		}
		user.setTarget((long) tWinning);
		return user;
	}


	public static Predicate<UserPreviousWinning> isValidUserForWin() {
		return n -> Long.parseLong(n.getSalePoints() + "") - n.getWinning() > 100 && (n.getWinningPercent() == 0
				|| (Long.parseLong(n.getSalePoints() + "") / 100) * n.getWinningPercent() - n.getWinning() > 100);
	}
	
}

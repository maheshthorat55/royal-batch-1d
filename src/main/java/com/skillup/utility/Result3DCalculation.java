package com.skillup.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
public class Result3DCalculation {
	
	private int target;
	private List<UserSaleBean> saleForGame;
	private List<UserPreviousWinning> userPreviousWinning;
	private GeneralGameSettings setting;
	private ResultTicket result;
	
	public void calculateResult() {
		this.claculateGameResult(target);
	}
	
	public void claculateGameResult(int targetSum) {

		List<Integer> availableWithNoSelections = Arrays.asList(777, 119, 155, 227, 335, 344, 399, 588, 669, 100, 137, 128, 146, 236, 245, 290, 380, 470, 489, 560, 678, 579,
				000, 118, 226, 244, 299, 334, 488, 668, 677, 550, 127, 136, 145, 190, 235, 280, 370, 389, 460, 479, 569, 578,
				444, 110, 228, 255, 336, 499, 660, 688, 778, 200, 129, 138, 147, 156, 237, 246, 345, 390, 480, 570, 589, 679,
				111, 166, 229, 337, 355, 445, 599, 779, 788, 300, 120, 139, 148, 157, 238, 247, 256, 346, 490, 580, 670, 689,
				888, 112, 220, 266, 338, 446, 455, 699, 770, 400, 130, 149, 158, 167, 239, 248, 257, 347, 356, 590, 680, 789,
				555, 113, 122, 177, 339, 366, 447, 799, 889, 500, 140, 159, 168, 230, 249, 258, 267, 348, 357, 456, 690, 780,
				222, 114, 277, 330, 448, 466, 556, 880, 899, 600, 123, 150, 169, 178, 240, 259, 268, 349, 358, 367, 457, 790,
				999, 115, 133, 188, 223, 377, 449, 557, 566, 700, 124, 160, 179, 250, 269, 278, 340, 359, 368, 458, 467, 890,
				666, 116, 224, 233, 288, 440, 477, 558, 990, 800, 125, 134, 170, 189, 260, 279, 350, 369, 378, 459, 468, 567,
				333, 117, 144, 199, 225, 388, 559, 577, 667, 900, 126, 135, 180, 234, 270, 289, 360, 379, 450, 469, 478, 568);
		
		try {
			if(StringUtils.isNumeric(setting.getWinningNumber3D())) {
				log.info("Generel setting number {}", setting.getWinningNumber3D());
				Integer winnNumber = Integer.parseInt(setting.getWinningNumber3D());
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
		
		Map<Integer, Long> numberWiseWinningSortedDesc = numberWiseWinning.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
		
		availableWithNoSelections = availableWithNoSelections.stream().filter(n->!numberWiseWinning.containsKey(n)).collect(Collectors.toList());

		Map<Integer, List<UserSaleBean>> numberAndUserWiseWinning = saleForGame.stream().collect(
				Collectors.groupingBy(UserSaleBean::getTicketNumber, Collectors.toList()));
		
		Map<Long, UserPreviousWinning> validForWinings = userPreviousWinning.stream().filter(isValidUserForWin())
					.map(s->addTarget(s)).sorted(Comparator.comparing(UserPreviousWinning::getTarget).reversed()).collect(Collectors.toMap(UserPreviousWinning::getUserId, Function.identity()));
		
		Map<Long, Long> validUserWithWinings = userPreviousWinning.stream().filter(isValidUserForWin())
				.map(s->addTarget(s)).sorted(Comparator.comparing(UserPreviousWinning::getTarget).reversed()).collect(Collectors.toMap(UserPreviousWinning::getUserId, UserPreviousWinning::getTarget));
		
		Map<Long, List<Integer>> winMap = numberWiseWinningSortedDesc.entrySet().stream().collect(Collectors.groupingBy(e->e.getValue(), Collectors.mapping(
                e->e.getKey(),
                Collectors.toList()
           )));

		List<Integer> numbers = new ArrayList<Integer>();
		for (Entry<Long, List<Integer>> numberList : winMap.entrySet()) {
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
		
//		for (Entry<Integer, Long> number : numberWiseWinningSortedDesc.entrySet()) {
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
		

		if(ObjectUtils.isNotEmpty(this.result)) {
			return;
		}
		
		log.info("Available no selection {}", availableWithNoSelections);
		
		if(ObjectUtils.isNotEmpty(availableWithNoSelections)) {
			int index = ThreadLocalRandom.current().nextInt(0, availableWithNoSelections.size());
			this.result = new ResultTicket(availableWithNoSelections.get(index), 1, Integer.parseInt("0"));
			return;
		}
		
		Map<Integer, Long> numberWiseWinningSorted = numberWiseWinning.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, HashMap<Integer, Long>::new));
		
		numberWiseWinningSorted.entrySet().forEach(first -> {
			log.info("selected min winning key [{}] value [{}]", first.getKey(), first.getValue());
			this.result = new ResultTicket(first.getKey(), 1, Integer.parseInt("0"));
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

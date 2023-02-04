package com.skillup.utility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.skillup.dto.ResultTicket;
import com.skillup.dto.SaleResult;
import com.skillup.dto.TicketResult;
import com.skillup.dto.UserWin;
import com.skillup.dto.UserWinning;
import com.skillup.entity.ResultCalculations;
import com.skillup.entity.ResultHelper;
import com.skillup.service.TicketService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ResultCalculator {
	private List<ResultCalculations> numbers;
	private int target;
	private List<ResultTicket> result;
	private List<ResultHelper> unSelectedTickets;
	private List<SaleResult> salesResults;
	private int winningPercentage;
	private String advanceNumber;
	private int resultType = 0;
	private TicketService ticketService;
	private long gameId;
	Map<Integer, List<UserWin>> userTicketMap;
	Map<Long, Set<Integer>> ticketsByUsernew;
	Map<Long, UserWinning> userWinning;
	
	public void calculateResult() {
		this.claculateGameResult(numbers, target);
	}
	
	public void claculateGameResult(List<ResultCalculations> list, int targetSum) {
		int sum = 0;
		
		Map<Long, List<ResultHelper>> unselected = 
				unSelectedTickets.stream().collect(Collectors.groupingBy(t->t.getCompId(), Collectors.toList()));
		
		Map<String, List<ResultHelper>> unselectedByseries = unSelectedTickets.stream()
				.collect(Collectors.groupingBy(
						t -> String.format("%04d", t.getTicketNumber()).substring(0, 2).toString(),
						Collectors.toList()));
		
		Set<String> resultSeries = new HashSet<String>();
		for(int i=0; i < 100;i++) {
			resultSeries.add(String.format("%02d", i));
		}
		for (Entry<String, List<ResultHelper>> resultCalculations : unselectedByseries.entrySet()) {
			resultSeries.remove(resultCalculations.getKey());
		}
		unselectedByseries = null;
		List<ResultCalculations> resultSeriesTickets;
		
		Map<Integer, Integer> forNoTicketList = list.stream().collect(Collectors.groupingBy(ResultCalculations::getTicketNumber, 
				Collectors.summingInt(ResultCalculations::getWiningPoints)));
		
		List<ResultCalculations> ticketList = forNoTicketList.entrySet().stream().map(s->ResultCalculations.builder().ticketNumber(s.getKey()).winingPoints(s.getValue()).build()).collect(Collectors.toList());
		
		if(resultSeries.size() > 0) {
			Map<String, List<ResultCalculations>> ticketsBySeries = 
					ticketList.stream().collect(Collectors.groupingBy(t->String.format("%04d", t.getTicketNumber()).substring(0, 2).toString(), Collectors.toList()));
			for (String string : resultSeries) {
				resultSeriesTickets = ticketsBySeries.get(string);
				if(resultSeriesTickets != null) {
					resultSeriesTickets.sort(Comparator.comparing(ResultCalculations::getWiningPoints));
					ResultCalculations resultCalculations = resultSeriesTickets.get(0);
					int winingPrice = resultCalculations.getWiningPoints();
					resultCalculations = getResultCalculationRandom(resultSeriesTickets, winingPrice, string);
					ResultTicket resultDto = new ResultTicket();
					resultDto.setTicketNumder(resultCalculations.getTicketNumber());
					resultDto.setCompId(findComp(resultCalculations.getTicketNumber()));
					resultDto.setWiningPoints(resultCalculations.getWiningPoints());
					sum = sum + resultCalculations.getWiningPoints();
					log.info("Unselected ticket added {}", resultDto);
					result.add(resultDto);
				}
			}
			ticketsBySeries = null;
		}
		resultSeries=null;
		
		double salePoint =  salesResults.stream().mapToDouble(s->s.getSalePoints()).sum();

		double winningMargin =  salesResults.stream().mapToDouble(s->s.getTotalWinning()).sum();
		
		if (StringUtils.isNotEmpty(advanceNumber)) {
			log.info("Advance winning numbers [{}]", advanceNumber);
			String[] advanceNumbers = advanceNumber.split(",");
			for(int i=0; i<advanceNumbers.length;i++){
				try {
					if(StringUtils.isNumeric(advanceNumbers[i])) {
						int adNum = Integer.parseInt(advanceNumbers[i]);
						Optional<ResultCalculations> optionalResult = list.stream()
								.filter(s -> s.getTicketNumber() == adNum).findFirst();
						if(optionalResult.isPresent()) {
							ResultTicket resultDto = new ResultTicket();
							resultDto.setTicketNumder(optionalResult.get().getTicketNumber());
							resultDto.setCompId(findComp(optionalResult.get().getTicketNumber()));
							resultDto.setWiningPoints(optionalResult.get().getWiningPoints());
							Optional<ResultTicket> optionalResultPresent = result.stream()
									.filter(s -> s.getTicketNumder() == resultDto.getTicketNumder()).findFirst();
							if(!optionalResultPresent.isPresent()) {
								sum = sum + optionalResult.get().getWiningPoints();
								result.add(resultDto);
							}
						}
					}
					
				} catch (Exception e) {
					
				}
			}
		}
		
		double winningP = winningMargin / salePoint * 100;
		int winningDeficiat = 5;
		if(winningP < winningPercentage - winningDeficiat) {
			try {
				List<Long> negativeUsers = this.salesResults.stream().filter(r -> ((r.getTotalWinning() / r.getSalePoints() * 100) >= (r.getWinPercentage()> 0 ? r.getWinPercentage() : winningPercentage)))
						.map(r -> r.getUserId()).collect(Collectors.toList());
				Set<Integer> filterNumber = new HashSet<Integer>();
				ticketsByUsernew.entrySet().stream().filter(e -> negativeUsers.contains(e.getKey())).forEach(e -> {
					filterNumber.addAll(e.getValue());
				});
				
				addDefaultWinnings(targetSum, winningDeficiat);
				
				this.salesResults.stream()
				.filter(s -> s.getTotalWinning() == 0
						|| (s.getTotalWinning() / s.getSalePoints() * 100) < winningPercentage - winningDeficiat
						|| true)
				.forEach(user -> {
					int winPercentage = winningPercentage;
					if(user.getWinPercentage() > 0) {
						winPercentage = user.getWinPercentage();
					}
					
					int tWinning = (int) ((user.getSalePoints() * (winPercentage - winningDeficiat) / 100)
							- user.getTotalWinning());
					
					if(user.getWinPercentage() > 100 && user.getMaxWinning() > 0) {
						int cMargin = (int) (user.getSalePoints() - (user.getTotalWinning() + tWinning));
						if(cMargin < 0) {
							if((cMargin*-1) > user.getMaxWinning()) {
								tWinning = tWinning - ((cMargin*-1) - user.getMaxWinning());
							}
						}
					}
					
					if(user.getCommition() > 0) {
						tWinning = (int) (tWinning - user.getCommition());
					}
																			
					if (ticketsByUsernew.containsKey(user.getUserId()) && tWinning > 179) {
						boolean errorToSubstractPreviousBalance = false;
						Set<Integer> tickets = ticketsByUsernew.get(user.getUserId());
						try {
							int previousWin = result.stream().filter(res->tickets.contains(res.getTicketNumder())).mapToInt(p->p.getWiningPoints()).sum();
							if(previousWin > 0 ) {
								UserWinning userPrevWin = this.userWinning.get(user.getUserId());
								if(userPrevWin != null && userPrevWin.getActualWinning() > previousWin) {
									tWinning = tWinning - userPrevWin.getActualWinning();
								} else {
									tWinning = tWinning - previousWin;
								}
							}
						} catch (Exception e) {
							errorToSubstractPreviousBalance = true;
							log.info("Error in substract previous balance for user {}", user.getUserId());
						}
						if(tWinning > 179) {
							addToResult(list,
									tWinning,
									tickets, filterNumber, user.getUserId(), errorToSubstractPreviousBalance);
						}
						
					}
				});
				
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Set<ResultTicket> dResult = new HashSet<ResultTicket>(result);	
		for (Entry<Long, List<ResultHelper>> resultCalculations : unselected.entrySet()) {
			findTenResultsForComp(resultCalculations.getValue(), dResult);
		}
		
		for (Entry<Long, UserWinning> user : this.userWinning.entrySet()) {
			log.info("User {} : Tarrgeted Sum: {} Actual Winning: {}", user.getKey(), user.getValue().getTargetSum(), user.getValue().getActualWinning());
		}
		
//		for (Entry<Long, List<ResultHelper>> resultCalculations : unselected.entrySet()) {
//			int index = ThreadLocalRandom.current().nextInt(2, 4);
//			int startIndex = ThreadLocalRandom.current().nextInt(0, 8);
//			addExtraResult(resultCalculations.getValue(), index, startIndex);
//		}
		
	}

	private void addDefaultWinnings(int targetSum, int winningDeficiat) {
		this.salesResults.stream()
				.filter(s -> s.getTotalWinning() == 0
						|| (s.getTotalWinning() / s.getSalePoints() * 100) < winningPercentage - winningDeficiat
						|| true)
				.forEach(user -> {
					int winPercentage = winningPercentage;
					if(user.getWinPercentage() > 0) {
						winPercentage = user.getWinPercentage();
					}
					
					int tWinning = (int) ((user.getSalePoints() * (winPercentage - winningDeficiat) / 100)
							- user.getTotalWinning());
					
					if(user.getWinPercentage() > 100 && user.getMaxWinning() > 0) {
						int cMargin = (int) (user.getSalePoints() - (user.getTotalWinning() + tWinning));
						if(cMargin < 0) {
							if((cMargin*-1) > user.getMaxWinning()) {
								tWinning = tWinning - ((cMargin*-1) - user.getMaxWinning());
							}
						}
					}
					
					if(user.getCommition() > 0) {
						tWinning = (int) (tWinning - user.getCommition());
					}
																			
					if (ticketsByUsernew.containsKey(user.getUserId()) && tWinning > 179) {
						this.userWinning.put(user.getUserId(), UserWinning.builder().targetSum(tWinning).actualWinning(0).build());
					}
				});
	}

	private void addToResult(List<ResultCalculations> list, int targetSum, Set<Integer> tickets, Set<Integer> filterNumbers, Long userId, boolean errorToSubstractPreviousBalance) {
		if(errorToSubstractPreviousBalance) {
			try {
				targetSum = targetSum - this.userWinning.get(userId).getActualWinning();
			} catch (Exception e) {
				log.error("Error in adjust target sum for user {}", userId);
			}
		}
		int fSum = 0;
		if(targetSum < 800 && resultType == 1) {
			return;
		}
		BiPredicate<Integer, Set<Integer>> biPredicate1 = (n, s) ->
	    {
	    	return s.contains(n);
	    };
		Set<Integer> ticketDetails = tickets;
		ticketDetails.removeAll(filterNumbers);
		Map<Long, List<ResultCalculations>> ticketsByCompForUser = 
				list.stream().filter(s->s.getUserId() == userId).filter(s->biPredicate1.test(s.getTicketNumber(), ticketDetails)).collect(Collectors.groupingBy(t->t.getCompId(), Collectors.toList()));
		
		List<ResultTicket> userTickets = new ArrayList<ResultTicket>();
		
		if(ticketsByCompForUser != null && !ticketsByCompForUser.isEmpty()) {
			log.info("first result try user {} target sum {}", userId, targetSum - fSum);
			findSingleResultChangeSort(targetSum, fSum, ticketsByCompForUser, userTickets, userId);
			//Check for second line result
			for (int i = 1; i < 3; i++) {
				fSum = userTickets.stream().mapToInt(p->p.getWiningPoints()).sum();
				if(fSum < targetSum && ( targetSum - fSum > 180 )) {
					log.info("for second result try user {} target sum {}", userId, targetSum - fSum);
					findSingleResultChangeSort(targetSum, fSum, ticketsByCompForUser, userTickets, userId);
				} else {
					break;
				}
			}
		} else {
			log.info("not tickets for user {} ", userId);
		}
		int actualWin = userTickets.stream().mapToInt(p->p.getWiningPoints()).sum();
		UserWinning userWin = this.userWinning.get(userId);
		userWin.setActualWinning(userWin.getActualWinning() + actualWin);
		log.info("for user {} targeted sum {} winning {}", userId, targetSum, userWin.getActualWinning());
		if(userWin.getTargetSum() < userWin.getActualWinning()) {
			log.info("========================= Need corrective action ===============================");
		}
	}
	
	private ResultCalculations getResultCalculationRandom(List<ResultCalculations> resultSeriesTickets,
			int winingPrice, String string) {
		if(winingPrice <= 360) {
			List<ResultCalculations> result = resultSeriesTickets.stream().filter(s->s.getWiningPoints()<=winingPrice).collect(Collectors.toList());
			int index = ThreadLocalRandom.current().nextInt(0, result.size());
			return result.get(index);	
		} else {
			try {
				List<TicketResult> tickets = getticketResult(ticketService.ticketsForSeries(gameId, string));
				for(TicketResult t :tickets) {
					List<Integer> longIds = Stream.of(t.getTickets().split(",")).map(Integer::parseInt).collect(Collectors.toList());
					List<ResultCalculations> result = resultSeriesTickets.stream()
								.filter(s->s.getWiningPoints()<=t.getTotalWin() && longIds.contains(s.getTicketNumber()))
								.collect(Collectors.toList());
					if(result.size() > 0) {
						int index = ThreadLocalRandom.current().nextInt(0, result.size());
						return result.get(index);
					}
				}
				
				List<ResultCalculations> result = resultSeriesTickets.stream().filter(s->s.getWiningPoints()<=winingPrice).collect(Collectors.toList());
				int index = ThreadLocalRandom.current().nextInt(0, result.size());
				return result.get(index);	
				
			} catch (Exception e) {
				List<ResultCalculations> result = resultSeriesTickets.stream().filter(s->s.getWiningPoints()<=winingPrice).collect(Collectors.toList());
				int index = ThreadLocalRandom.current().nextInt(0, result.size());
				return result.get(index);	
			}
		}
	}
	
	private List<TicketResult> getticketResult(List<Object> result) {
		List<TicketResult> salesResults = new ArrayList<TicketResult>(result.size());
		result.stream().forEach(obj -> {
			Object[] row = (Object[]) obj;
			try {
				salesResults.add(TicketResult.builder()
						.totalSale(Long.parseLong(row[0].toString()))
						.totalWin(Long.parseLong(row[1].toString()))
						.tickets(row[2].toString())
						.userId(Long.parseLong(row[3].toString()))
						.winDiff(Long.parseLong(row[4].toString()))
						.build());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		});
		return salesResults;
	}

	private void findTenResultsForComp(List<ResultHelper> resultInput, Set<ResultTicket> dResult) {
		Map<String, List<ResultHelper>> unselected = resultInput.stream()
				.collect(Collectors.groupingBy(
						t -> String.format("%04d", t.getTicketNumber()).substring(1, 2).toString(),
						Collectors.toList()));
		int resultCount = 0;
		int index = 0;
		for (Entry<String, List<ResultHelper>> resultCalculations : unselected.entrySet()) {
			resultCount = 0;
			for (ResultHelper results : resultCalculations.getValue()) {
				index = ThreadLocalRandom.current().nextInt(0, resultCalculations.getValue().size());
				results = resultCalculations.getValue().get(index);
				ResultTicket resultDto = new ResultTicket();
				resultDto.setTicketNumder(results.getTicketNumber());
				resultDto.setCompId(findComp(results.getTicketNumber()));
				resultDto.setWiningPoints(0);
	
				if(checkFullResultPresent(resultDto)) {
					continue;
				}
	
				if(resultCount >= 1 || checkSeriesResultPresent(resultDto, dResult)) {
					break;
				}
				result.add(resultDto);
				resultCount++;
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void addExtraResult(List<ResultHelper> resultInput, int index2, int startIndex) {
		Map<String, List<ResultHelper>> unselected = resultInput.stream()
				.collect(Collectors.groupingBy(
						t -> String.format("%04d", t.getTicketNumber()).substring(1, 2).toString(),
						Collectors.toList()));
		int resultCount = 0;
		int index = 0;
		int additionalResult = 0;
		int comIndex = 0;
		for (Entry<String, List<ResultHelper>> resultCalculations : unselected.entrySet()) {
			resultCount = 0;
			if(comIndex++ < startIndex) {
				continue;
			}
			for (ResultHelper results : resultCalculations.getValue()) {
				index = ThreadLocalRandom.current().nextInt(0, resultCalculations.getValue().size());
				results = resultCalculations.getValue().get(index);
				ResultTicket resultDto = new ResultTicket();
				resultDto.setTicketNumder(results.getTicketNumber());
				resultDto.setCompId(findComp(results.getTicketNumber()));
				resultDto.setWiningPoints(0);
	
				if(checkFullResultPresent(resultDto)) {
					continue;
				}
	
				if(resultCount >= 1 || checkSeriesResult(resultDto, new HashSet<ResultTicket>(result))) {
					break;
				}
				additionalResult++;
				result.add(resultDto);
				resultCount++;
			}
			if(additionalResult == index2) {
				break;
			}
		}
	}
	
	private boolean checkFullResultPresent(ResultTicket resultDto) {
		return result.parallelStream()
				.filter(s -> s.getTicketNumder() == resultDto.getTicketNumder())
				.findFirst().isPresent();
	}
	
	private boolean checkSeriesResultPresent(ResultTicket resultDto, Set<ResultTicket> dResult) {
		String ticket = String.format("%04d", resultDto.getTicketNumder()).substring(0, 2);
		return dResult.parallelStream()
				.filter(s -> String.format("%04d", s.getTicketNumder()).substring(0, 2).equals(ticket))
				.findFirst().isPresent();
	}
	
	private boolean checkSeriesResult(ResultTicket resultDto, Set<ResultTicket> dResult) {
		String ticket = String.format("%04d", resultDto.getTicketNumder()).substring(1, 4);
		return dResult.parallelStream()
				.filter(s -> String.format("%04d", s.getTicketNumder()).substring(1, 4).equals(ticket))
				.findFirst().isPresent();
	}

	
	private void findSingleResultChangeSort(int targetSum, int sum, Map<Long, List<ResultCalculations>> ticketsByComp, List<ResultTicket> userTickets, Long userId) {
		Comparator<ResultCalculations> sortingComparator = Comparator.comparing(ResultCalculations::getWiningPoints);
		List<ResultCalculations> tempResult;
		long[] result = ticketsByComp.keySet().stream().mapToLong(l -> l).toArray();
		for (long i = 0; i < result.length; i++) {
			int index = ThreadLocalRandom.current().nextInt(0, result.length);
			long comNumber = result[index];
			if (ticketsByComp.get(comNumber) != null) {
				tempResult = ticketsByComp.get(comNumber);
				for (long j = 0; j < 5; j++) {
					sortingComparator = getComparator(sortingComparator);
					sum = findTicketForComputer(targetSum, sum,
							tempResult.stream()
							.sorted(sortingComparator)
									.collect(Collectors.toList()), userTickets, userId);
					if (sum >= targetSum) {
						break;
					}
					sum = sum + sum;
				}
			}
			if (sum >= targetSum) {
				break;
			}
		}
	}

	private int findTicketForComputer(int targetSum, int sum, List<ResultCalculations> tickets, List<ResultTicket> userTickets, Long userId) {
		tickets.sort(Comparator.comparing(ResultCalculations::getWiningPoints).reversed());
		for (ResultCalculations resultCalculations : tickets) {
			int caseNo = ThreadLocalRandom.current().nextInt(1, 10);
			if(sum + resultCalculations.getWiningPoints() <= targetSum) {
				ResultTicket resultDto = new ResultTicket();
				resultDto.setTicketNumder(resultCalculations.getTicketNumber());
				resultDto.setCompId(findComp(resultCalculations.getTicketNumber()));
				resultDto.setWiningPoints(resultCalculations.getWiningPoints());
				if(checkResultPresent(resultDto) || skipQuantity(resultCalculations.getQuantity(), caseNo)) {
					continue;
				}
				if(userTicketMap.get(resultDto.getTicketNumder()).size() > 1) {
					boolean checkForOther = checkForOtherUser(userId, userTicketMap.get(resultDto.getTicketNumder()));
					if(checkForOther) {
						log.info("called for user {} multiple users with ticket valid {}", userId, userTicketMap.get(resultDto.getTicketNumder()));
					} else {
						continue;
					}
				}
				log.info("Number added [{}], target [{}], total winning [{}]",resultDto.getTicketNumder(), targetSum, resultCalculations.getWiningPoints());
				sum = sum + resultCalculations.getWiningPoints();
				result.add(resultDto);
				userTickets.add(resultDto);
				break;
			} else if (sum + 180 < targetSum){
				continue;
			} else {
				break;
			}
		}
		return sum;
	}

	private boolean checkForOtherUser(Long userId, List<UserWin> users) {
		boolean result = true;
		for (UserWin user : users) {
			if(user.getUserId() == userId) {
				UserWinning userWin = this.userWinning.get(user.getUserId());
				if(userWin != null && (userWin.getTargetSum() >= (userWin.getActualWinning() + user.getWinning()))) {
					continue;
				} else {
					result = false;
					break;
				}
			}
			UserWinning userWin = this.userWinning.get(user.getUserId());
			if(userWin != null && (userWin.getTargetSum() >= (userWin.getActualWinning() + user.getWinning()))) {
				result = true;
			} else {
				result = false;
				break;
			}
		}
		if(result) {
			for (UserWin user : users) {
				if(user.getUserId() == userId) {
					continue;
				}
				UserWinning userWin = this.userWinning.get(user.getUserId());
				userWin.setActualWinning(userWin.getActualWinning() + user.getWinning());
			}
		}
		
		return result;
	}

	private Comparator<ResultCalculations> getComparator(Comparator<ResultCalculations> sortingComparator) {
		int caseNo = ThreadLocalRandom.current().nextInt(1, 10);
		switch (caseNo) {
		case 1:
			sortingComparator = Comparator.comparing(ResultCalculations::getWiningPoints)
			.thenComparing(ResultCalculations::getResultCalId);
			break;
		case 2:
			sortingComparator = Comparator.comparing(ResultCalculations::getWiningPoints)
			.thenComparing(ResultCalculations::getTicketNumber).reversed();
			break;	
		case 3:
			sortingComparator = Comparator.comparing(ResultCalculations::getWiningPoints)
			.thenComparing(ResultCalculations::getUserId);
			break;	
		case 4:
			sortingComparator = Comparator.comparing(ResultCalculations::getResultCalId).reversed();
			break;
		case 5:
			sortingComparator = Comparator.comparing(ResultCalculations::getWiningPoints)
					.thenComparing(ResultCalculations::getResultCalId).reversed();
			break;	
		case 6:
			sortingComparator = Comparator.comparing(ResultCalculations::getWiningPoints)
					.thenComparing(ResultCalculations::getTicketNumber).reversed();
			break;		
		default:
			sortingComparator = Comparator.comparing(ResultCalculations::getWiningPoints).reversed()
			.thenComparing(ResultCalculations::getResultCalId);
			break;
		}
		return sortingComparator;
	}

	private boolean skipQuantity(int quantity, int caseNo) {
		if (this.resultType == 0) {
			switch (caseNo) {
			case 1:
				return quantity < 1 || quantity > 10;
			case 2:
				return quantity < 1 || quantity > 20;
			case 3:
				return quantity < 2 || quantity > 30;
			case 4:
				return quantity < 5 || quantity > 40;
			case 5:
				return quantity < 10 || quantity > 50;
			case 6:
				return quantity < 10 || quantity > 60;
			case 7:
				return quantity < 10 || quantity > 75;
			case 8:
				return quantity < 10 || quantity > 80;
			case 9:
				return quantity < 10 || quantity > 100;
			default:
				return true;
			}
		} else {
			switch (caseNo) {
			case 1:
				return quantity < 1 || quantity > 10;
			case 2:
				return quantity < 4 || quantity > 20;
			case 3:
				return quantity < 5 || quantity > 30;
			case 4:
				return quantity < 5 || quantity > 40;
			case 5:
				return quantity < 10 || quantity > 50;
			case 6:
				return quantity < 10 || quantity > 60;
			case 7:
				return quantity < 10 || quantity > 75;
			case 8:
				return quantity < 10 || quantity > 80;
			case 9:
				return quantity < 10 || quantity > 100;
			default:
				return true;
			}

		}

	}

	public boolean checkResultPresent(ResultTicket resultDto) {
		return result.parallelStream()
				.filter(s -> compaireTickets(resultDto, s))
				.findFirst().isPresent();
	}

	private boolean compaireTickets(ResultTicket resultDto, ResultTicket s) {
		return StringUtils.equals(getCompareForCompString(s.getTicketNumder()),
				getCompareForCompString(resultDto.getTicketNumder())) 
				|| StringUtils.equals(getCompareString(s.getTicketNumder()),
				getCompareString(resultDto.getTicketNumder()))
				|| StringUtils.equals(getCompareForSeriesString(s.getTicketNumder()),
						getCompareForSeriesString(resultDto.getTicketNumder()));
	}

	private String getCompareString(int number) {
		return String.format("%04d", number).substring(0, 1) + String.format("%04d", number).substring(2, 4);
	}
	
	private String getCompareForCompString(int number) {
		return String.format("%04d", number).substring(0, 2);
	}
	
	private String getCompareForSeriesString(int number) {
		return String.format("%04d", number).substring(1, 4);
	}
	
	public int findComp(int ticketNumber) {
		if (ticketNumber >= 9000) {
			return 9;
		} else if (ticketNumber >= 8000) {
			return 8;
		} else if (ticketNumber >= 7000) {
			return 7;
		} else if (ticketNumber >= 6000) {
			return 6;
		} else if (ticketNumber >= 5000) {
			return 5;
		} else if (ticketNumber >= 4000) {
			return 4;
		} else if (ticketNumber >= 3000) {
			return 3;
		} else if (ticketNumber >= 2000) {
			return 2;
		} else if (ticketNumber >= 1000) {
			return 1;
		} else {
			return 0;
		}
	}
	
}

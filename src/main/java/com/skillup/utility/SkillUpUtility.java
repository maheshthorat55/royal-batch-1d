package com.skillup.utility;

import java.util.HashMap;
import java.util.Map;

public class SkillUpUtility {
	
	public static Map<String, Integer> seriesMultiplier;
    
    static {
    	//[2,2,4,6, 10,10,20,40,50,50]
    	seriesMultiplier = new HashMap<>();
    	seriesMultiplier.put("0", 2);
    	seriesMultiplier.put("1", 2);
    	seriesMultiplier.put("2", 4);
    	seriesMultiplier.put("3", 6);
    	seriesMultiplier.put("4", 10);
    	seriesMultiplier.put("5", 10);
    	seriesMultiplier.put("6", 20);
    	seriesMultiplier.put("7", 40);
    	seriesMultiplier.put("8", 50);
    	seriesMultiplier.put("9", 50);
    }
    
    public static int getMultiplier(String series) {
    	return seriesMultiplier.get(series);
    }
	
	public static int findComp(int ticketNumber) {
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

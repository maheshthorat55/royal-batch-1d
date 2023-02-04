package com.skillup.utility;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class SchedularSharedClass {
	
	private static SchedularSharedClass instance = null;
	
	private Set<String> timeToAdd = Collections.unmodifiableSet(
	        new HashSet<>(Arrays.asList("15","30","45","0")));
	
	private Set<String> timeToAddTenKaDum = Collections.unmodifiableSet(
	        new HashSet<>(Arrays.asList("5","10","15","20","25","30","35","40","45","50","55","0")));
	
	private Set<String> timeToAdd3DGame = Collections.unmodifiableSet(
	        new HashSet<>(Arrays.asList("10","20","30","40","50","0")));
	
	private String lastGameCreationDate = "";
	
	private String lastGameCreation3DDate = "";
	
	static {
		instance = new SchedularSharedClass();
	}

	public static SchedularSharedClass getSchedularSharedInstance() {
		return instance;
	}
	
}

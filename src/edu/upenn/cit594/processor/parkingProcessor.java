package edu.upenn.cit594.processor;

import java.math.*;
import java.util.*;
import java.util.Map.*;

import edu.upenn.cit594.data.*;
import edu.upenn.cit594.datamanagement.*;

public class parkingProcessor {
	private parkingReader parkingReader;
	private static List<Parking> parkingViolationList;
	private static Map<String, Integer> populationMap;
	private static Map<String, Integer> parkingViolationFineMap;
	private static Map<String, BigDecimal> avgFineMap;
	private static Map<String, String> violationReasonMap;
	private static String avgFineResult = "";
	private populationTextReader populationTextReader;
	
	public parkingProcessor(String format, String input) {
		if(format.equals("json")) {
			parkingReader = new parkingJsonReader(input);
		}
		if(format.equals("csv")) {
			parkingReader = new parkingCsvReader(input);
		}
		parkingProcessor.parkingViolationList = parkingReader.getParkingViolation();
		parkingProcessor.parkingViolationFineMap = new HashMap<>();
		parkingProcessor.avgFineMap = new TreeMap<>();
		parkingProcessor.violationReasonMap = new HashMap<>();
		populationTextReader = new populationTextReader();
		populationMap = populationTextReader.getPopulationMap();
	}
	
	private static Map<String, Integer>  generateParkingFineMap() {
		for(Parking item: parkingViolationList) {
			String zipCode = item.getZip();
			int fine = item.getFine();
			String state = item.getState();
			if(state.equals("PA") && !zipCode.isBlank() && !zipCode.isEmpty()) {
				if(parkingViolationFineMap.containsKey(zipCode)) {
					int fineUpdate = parkingViolationFineMap.get(zipCode) + fine;
					parkingViolationFineMap.put(zipCode, fineUpdate);
				}
				else {
					parkingViolationFineMap.put(zipCode, fine);
				}
			}	
		}
		return parkingViolationFineMap;
	}
	
	private static Map<String, BigDecimal> getAvgFineMap() {
		if(parkingViolationFineMap.isEmpty()) {
			parkingViolationFineMap = generateParkingFineMap();
			for(Entry<String, Integer> item: populationMap.entrySet()) {
				String zip = item.getKey();
				int population = item.getValue();
				double avgFine = 0;
				if(parkingViolationFineMap.containsKey(zip)) {
					avgFine = (double) parkingViolationFineMap.get(zip)/ (double) population;
					BigDecimal avgFineUpdate = BigDecimal.valueOf(avgFine);
					avgFineUpdate = avgFineUpdate.setScale(4, RoundingMode.DOWN);
					avgFineMap.put(zip, avgFineUpdate);
				} 
			}
		}
		return avgFineMap;
	}
	
	public static String getAvgFine() {
		if(avgFineMap.isEmpty()) {
			avgFineMap = getAvgFineMap();
			for(Entry<String, BigDecimal> item: avgFineMap.entrySet()) {
				String zip = item.getKey();
				BigDecimal avgFine = item.getValue(); 
				avgFineResult = avgFineResult + zip + " " + avgFine + '\n';
			}
		} 
		return avgFineResult;
	}
	
	public static Map<String, String>  getViolationReasonMap() {
		Map<String, Map<String, Integer>> violationCountMap = new HashMap<>();
		for (Parking item: parkingViolationList) {
			String zip = item.getZip();
			String reason = item.getReason();
			String state = item.getState();
			int count = 0;
			if(!state.equals("PA")) {
				continue;
			}
			if(zip.isBlank() || zip.isEmpty()) {
				continue;
			}
			if(violationCountMap.containsKey(zip)) {
				Map<String, Integer> tempMap = violationCountMap.get(zip);
				if(tempMap.containsKey(reason)) {
					count = tempMap.get(reason) + 1;
				}
				tempMap.put(reason, count);
				violationCountMap.put(zip, tempMap);
			}
			else {
				Map<String, Integer> tempMap = new HashMap<>();
				tempMap.put(reason, 0);
				violationCountMap.put(zip, tempMap);
			}
		}
		for(String zipKey: violationCountMap.keySet()) {
			Map<String, Integer> tempMap = violationCountMap.get(zipKey);
			int max = -1;
			String topReason = null;
			for(String reasonKey: tempMap.keySet()) {
				int count = tempMap.get(reasonKey);
				if(count > max) {
					max = count;
					topReason = reasonKey;
				}
			}
			violationReasonMap.put(zipKey, topReason);
		}
		return violationReasonMap;
	}
}
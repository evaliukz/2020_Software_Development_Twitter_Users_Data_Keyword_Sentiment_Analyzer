package edu.upenn.cit594.ui;

import java.util.*;
import java.util.Map.*;
import java.util.regex.*;

import edu.upenn.cit594.data.*;
import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.processor.*;
import edu.upenn.cit594.logging.*;

public class userInterface {
	private static userInterface obj;
	private populationTextReader populationTextReader = new populationTextReader();

	private userInterface() {
		start();
	}
	
	public static userInterface getInstance() {
		if (obj == null) obj = new userInterface();
		return obj;
	}
	
	private void start() {
		Scanner scanInput = new Scanner(System.in);
		System.out.println("Please enter a number between 0 and 6 to conduct one of following actions:\n"
				+ "0: Exit\n"
				+ "1: Total population for all zip codes\n"
				+ "2: Total parking fines per capita for each zip code\n"
				+ "3: Average market value for residences in a specified zip code\n"
				+ "4: The average total livable area for residences in a specified zip code\n"
				+ "5: The total residential market value per capita for a specified zip code\n"
				+ "6: The top parking violation reason for the top five least livable areas\n");
		String userInput = scanInput.nextLine();
		Logger.getInstance().log(userInput);
		if (isValid(userInput)) {
			switch(userInput) {
			case "0":
				System.exit(0);
				break;
			case "1":
				System.out.println(populationTextReader.getTotalPopulation());
				break;
			case "2":
				System.out.println(parkingProcessor.getAvgFine());
				break;
			case "3":
				System.out.println("Please enter a zip code:\n");
				if (scanInput.hasNextLine()) {
					String zip = scanInput.nextLine();
					Logger.getInstance().log(zip);
					double result = caseThree(zip);
					System.out.printf("The average residential market value for Zip '%s' is %d\n", zip, (int)result);
				}
				break;
			case "4":
				System.out.println("Please enter a zip code:\n");
				if (scanInput.hasNextLine()) {
					String zip = scanInput.nextLine();
					Logger.getInstance().log(zip);
					double result = caseFour(zip);
					System.out.printf("The average residential total livable area for Zip '%s' is %d\n", zip, (int)result);
				}
				break;
			case "5":
				System.out.println("Please enter a ZIP code:\n");
				if (scanInput.hasNextLine()) {
					String zip = scanInput.nextLine();
					Logger.getInstance().log(zip);
					double result = caseFive(zip);
					System.out.printf("The total residential market value per capita for Zip %s is %d\n", zip, (int)result);
				}
				break;
			case "6":
				System.out.println(caseSix());
				break;
			default:
				System.out.println("Invalid\n");
			}
		}
		scanInput.close();
	}
	
	private boolean isValid(String input) {
		return Pattern.matches("\\d", input);
	}
	
	Map<String, Double> caseThree = new HashMap<>();
	private double caseThree(String zip) {
		double result = 0;
		if (isValidZip(zip)) {
			if (caseThree.keySet().contains(zip)) {
				result = caseThree.get(zip);
			}
			else {
				propertyMarketValue marketValue = new propertyMarketValue();
				propertyProcessor propertyProcessor = new propertyProcessor(marketValue);
				double sumCount[] = propertyProcessor.process(zip);
				if (sumCount[1] != 0) {
					result = sumCount[0] / sumCount[1];
				}
				caseThree.put(zip, result);
			}
		}
		return result;
	}
	
	Map<String, Double> caseFour = new HashMap<>();
	private double caseFour(String zip) {
		double result = 0;
		if (isValidZip(zip)) {
			if (caseFour.keySet().contains(zip)) {
				result = caseFour.get(zip);
			}
			else {
				propertyLivableArea livableArea = new propertyLivableArea();
				propertyProcessor propertyProcessor = new propertyProcessor(livableArea);
				double sumCount[] = propertyProcessor.process(zip);
				if (sumCount[1]!= 0) {
					result = sumCount[0] / sumCount[1];
				}
				caseFour.put(zip, result);
			}
		}
		return result;
	}
	
	Map<String, Double> caseFive = new HashMap<>();
	private double caseFive(String zip) {
		double result = 0;
		if (isValidZip(zip)) {
			if (caseFive.keySet().contains(zip)) {
				result = caseFive.get(zip);
			}
			else {
				populationTextReader populationTextReader = new populationTextReader();
				int populationPerZip = populationTextReader.getPopulationPerZip(zip);
				propertyMarketValue marketValue = new propertyMarketValue();
				propertyProcessor propertyProcessor = new propertyProcessor(marketValue);
				double sumCount[] = propertyProcessor.process(zip);
				if (populationPerZip != 0 && sumCount[1]!=0) {
					result = sumCount[0] / populationPerZip;
				}
				caseFive.put(zip, result);
			}
		}
		return result;
	}
	
	String result = "";
	private String caseSix() {
		HashMap<String, Double> avgLivableMap = new HashMap<>();
		if (result == null || result.isEmpty()) {
			double livableAreaPerCapita = 0;
			String violationReason = "no parking violation in this area";
			populationTextReader populationTextReader = new populationTextReader();
			Map<String, Double> totalLivableMap = propertyProcessor.getPropertyMap();
			if(totalLivableMap == null || totalLivableMap.isEmpty()) {
				result = "no information available\n";
			}
			for(Entry<String, Double> item: totalLivableMap.entrySet()) {
				String zip = item.getKey();
				double totalLivableArea = item.getValue();
				int population = populationTextReader.getPopulationPerZip(zip);
				if (population != 0 && totalLivableArea != 0) {
					livableAreaPerCapita = totalLivableArea / population;
					avgLivableMap.put(zip, livableAreaPerCapita);
				}
			}
			int i = 5;
			for(Entry<String, Double> item : sortHashMapByValues(avgLivableMap).entrySet()) {
				String zipCode = item.getKey();
				Map<String, String> violationMap = parkingProcessor.getViolationReasonMap();
				if(violationMap.containsKey(zipCode)) {
					violationReason = violationMap.get(zipCode);
				}
				result += "The top parking violation reason for the least livable area per capita "
				+ zipCode + " is " + violationReason + '\n';
				i--;
				if (i == 0) {
					break;
				}
			}
		}
		return result;
	}
	
	private boolean isValidZip(String input) {
		return Pattern.matches("\\d{5}", input);
	}
	
	private LinkedHashMap<String, Double> sortHashMapByValues(HashMap<String, Double> unsortedMap) {
		List<String> mapKey = new ArrayList<>(unsortedMap.keySet());
		List<Double> mapValue = new ArrayList<>(unsortedMap.values());
		Collections.sort(mapValue);
		Collections.sort(mapKey);
		LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();
		Iterator<Double> valueIt = mapValue.iterator();
		while (valueIt.hasNext()) {
			Double value = valueIt.next();
			Iterator<String> keyIt = mapKey.iterator();
			while (keyIt.hasNext()) {
				String key = keyIt.next();
				Double varOne = unsortedMap.get(key);
				Double varTwo = value;
				if (varOne.equals(varTwo)) {
					keyIt.remove();
					sortedMap.put(key, value);
					break;
				}
			}
		}
		return sortedMap;
	}
}
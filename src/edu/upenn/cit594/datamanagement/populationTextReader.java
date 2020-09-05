package edu.upenn.cit594.datamanagement;

import java.io.*;
import java.util.*;

import edu.upenn.cit594.logging.*;

public class populationTextReader {
	private String fileName;
    private static Map<String, Integer> populationMap = new HashMap<>();
    private int totalPopulation;
    public populationTextReader(){};

    public populationTextReader (String fileName) {
        this.fileName = fileName;
        try {
            populationMap = this.read();
        }
        catch (FileNotFoundException e) {
            System.out.println("Error\n");
            System.exit(0);
        }
    }
    
    public Map<String, Integer> read() throws FileNotFoundException {
        File newFile = new File(fileName);
        Logger.getInstance().log(fileName);
        BufferedReader br = new BufferedReader(new FileReader(newFile));
        String line;
        try {
            while ((line = br.readLine()) != null) {
                String[] lineElements = line.split(" ");
                int population = Integer.parseInt(lineElements[1]);
                totalPopulation = getTotalPopulation() + population;
                populationMap.put(lineElements[0], population);
            }
        }
        catch (IOException e) {
            System.out.println("Error\n");
        }
        finally {
            try {
                br.close();
            }
            catch (IOException e) {
                System.out.println("Error\n");
            }
        }
        return populationMap;
    }
    
    public int getPopulationPerZip(String zip) {
        int  populationPerZip = 0;
        if (populationMap.keySet().contains(zip)) {
            populationPerZip = populationMap.get(zip);
        }
        return populationPerZip;
    }
    
	public int getTotalPopulation() {
		return totalPopulation;
	}
	
    public Map<String, Integer> getPopulationMap(){
    	return populationMap;
    }
}
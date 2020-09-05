package edu.upenn.cit594.processor;

import java.io.*;
import java.util.*;

import edu.upenn.cit594.data.*;
import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.datamanagement.Reader;

public class propertyProcessor {
	
	private Reader Reader;
    private static List<Property> propertyList;
    private static HashMap<String, Double> propertyMap;
    private propertyReader propertyReader;
    
    public propertyProcessor(Reader Reader) {
        this.Reader = Reader;
    }
    
    public propertyProcessor(propertyReader pr) {
        this.propertyReader = pr;
        try {
            propertyList = propertyReader.read();
        }
        catch (IOException e) {
            System.out.println("Error\n");
            System.exit(0);
        }
    }
    
    public double[] process(String zip) {
        List<Property> selectedByZip = selectByZip(zip);
        double[] information = Reader.getInformation(selectedByZip);
        int len = information.length;
        double sum = 0;
        for (int i = 0; i < len; i++) {
            sum += information[i];
        }
        return new double[]{sum, len};
    }
    
    private List<Property> selectByZip(String zip) {
        List<Property> propertyListZip = new ArrayList<>();
        for (Property p : propertyList) {
            if (p.getZip().equals(zip)) {
                propertyListZip.add(p);
            }
        }
        return propertyListZip;
    }
    
    private static void generatepropertyMap() {
    	propertyMap = new HashMap<>();
    	double sumLivableArea = 0;
    	for (Property p : propertyList) {
    		String zip = p.getZip();
    		Double livableArea = p.getTotalLivableArea();
    		if (propertyMap.keySet().contains(zip)) {
    			sumLivableArea = propertyMap.get(zip) + livableArea;
    			propertyMap.put(zip, sumLivableArea);
    		}
    		else {
    			propertyMap.put(zip, livableArea);
    		}
    	}
    }
    
    public static Map<String, Double> getPropertyMap(){
    	if(propertyMap == null) {
    		generatepropertyMap();
    	}
    	return propertyMap;
    }
    
    public List<Property> getPropertyList() {
        return propertyList;
    }
}
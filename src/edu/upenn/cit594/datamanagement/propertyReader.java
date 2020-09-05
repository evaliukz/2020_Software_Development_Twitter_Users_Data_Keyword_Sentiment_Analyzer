package edu.upenn.cit594.datamanagement;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import edu.upenn.cit594.data.*;
import edu.upenn.cit594.logging.*;

public class propertyReader {
	private String fileName;
    public propertyReader(String fileName){
        this.fileName = fileName;
    }
    
    public List<Property> read() throws IOException {
        List<Property> propertyList = new LinkedList<>();
        File newFile = new File(fileName);
        Logger.getInstance().log(fileName);
        BufferedReader br = new BufferedReader(new FileReader(newFile));
        String firstLine;
        firstLine = br.readLine();
        if (firstLine == null || firstLine.isEmpty()) {
        	br.close();
        	return propertyList;
        }
        int[] informationColumn = findColumn(firstLine);
        String line;
        while((line = br.readLine()) != null) {
        	String[] lineElements = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
            String livableAreaString = lineElements[informationColumn[0]].strip();
            String marketValueString = lineElements[informationColumn[1]].strip();
            String zipString = lineElements[informationColumn[2]].strip();
            if (isValidProperty(livableAreaString, marketValueString, zipString)) {
                double livableArea = Double.parseDouble(livableAreaString);
                double marketValue = Double.parseDouble(marketValueString);
                String zip = zipString.substring(0,5);
                Property current = new Property(livableArea, marketValue, zip);
                propertyList.add(current);
            }
        }
        br.close();
        return propertyList;
    }
    
    private boolean isValidProperty(String livableArea, String marketValue, String zip) {
        boolean validLivableArea = Pattern.matches("^\\d+\\.?\\d*$", livableArea);
        boolean validMarketValue = Pattern.matches("^\\d+\\.?\\d*$", marketValue);
        boolean validZip = Pattern.matches("^[0-9]{5}.*$", zip);
        return validLivableArea && validMarketValue && validZip;
    }
    
    private int[] findColumn(String firstLine) {
        int[] columns = new int[3];
        String[] headers = firstLine.split(",");
        for (int i = 0; i < headers.length;i++) {
            if (headers[i].equals("total_livable_area")) {
                columns[0] = i;
            }
            else if (headers[i].equals("market_value")) {
                columns[1] = i;
            }
            else  if (headers[i].equals("zip_code")) {
                columns[2] = i;
            }
        }
        return columns;
    }
}
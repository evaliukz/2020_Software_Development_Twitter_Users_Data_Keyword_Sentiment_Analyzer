package edu.upenn.cit594.datamanagement;

import java.io.*;
import java.util.*;

import edu.upenn.cit594.data.*;
import edu.upenn.cit594.logging.*;

public class parkingCsvReader implements parkingReader {
	private String fileName;
	private List<Parking> parkingViolationList;
	
	public parkingCsvReader(String input){
		fileName = input;
		parkingViolationList = new LinkedList<>();
	}
	
	@Override
	public List<Parking> getParkingViolation() {
		String row = null;
		try {
			FileReader fileReader = new FileReader(fileName);
			Logger.getInstance().log(fileName);
			BufferedReader csvReader = new BufferedReader(fileReader);
			try {
				while ((row = csvReader.readLine()) != null) {
					String[] data = row.split(",");
					if(data.length != 7) {
						continue;
					}
					String date = data[0];
					int fine = Integer.parseInt(data[1]);
					String violation = data[2];
					String plateId = data[3];
					String state = data[4];
					String ticketNumber = data[5];
					String zip = data[6];
					parkingViolationList.add(new Parking(ticketNumber, plateId, date, zip, violation, fine, state));
				}
				csvReader.close();
			}
			catch (NumberFormatException | IOException e) {
				System.out.println("Invalid\n");
				System.exit(0);
			}
		}
		catch (FileNotFoundException e1) {
			System.out.println("Error\n");
			System.exit(0);
		}
		return parkingViolationList;
	}
}
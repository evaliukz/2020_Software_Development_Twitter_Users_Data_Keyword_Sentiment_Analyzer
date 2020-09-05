package edu.upenn.cit594.datamanagement;

import java.io.*;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import edu.upenn.cit594.data.*;
import edu.upenn.cit594.logging.*;

public class parkingJsonReader implements parkingReader {
	private String fileName;
	private List<Parking> parkingViolationsList;
	public parkingJsonReader(String input){
		fileName = input;
		parkingViolationsList = new LinkedList<>();
	}
	
	@Override
	public List<Parking> getParkingViolation() {
		JSONParser parser = new JSONParser();
		JSONArray parkingViolationArray;
		try {
			FileReader fileReader = new FileReader(fileName);
			Logger.getInstance().log(fileName);
			Object obj = parser.parse(fileReader);
			parkingViolationArray = (JSONArray) obj;
			Iterator<?> iter = parkingViolationArray.iterator();
			while (iter.hasNext()) {
				JSONObject parkingViolation = (JSONObject) iter.next();
				String ticketNumber = String.valueOf(parkingViolation.get("ticket_number"));
				String date = (String) parkingViolation.get("date");
				String plateId = (String) parkingViolation.get("plate_id");
				String zip = (String) parkingViolation.get("zip");
				String violation = (String) parkingViolation.get("violation");
				int fine = Integer.valueOf(String.valueOf(parkingViolation.get("fine")));
				String state = (String) parkingViolation.get("state");
				parkingViolationsList.add(new Parking(ticketNumber, plateId, date, zip, violation, fine, state));
			}
		}
		catch (IOException | ParseException e) {
			System.out.println("Parking violation file does not exist. Program exit.");
            System.exit(0);
		}
		return parkingViolationsList;
	}
}
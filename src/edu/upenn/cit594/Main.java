package edu.upenn.cit594;

import edu.upenn.cit594.datamanagement.*;
import edu.upenn.cit594.logging.*;
import edu.upenn.cit594.processor.*;
import edu.upenn.cit594.ui.*;

public class Main {
	public static void main(String[] args) {
		if (args.length != 5) {
            System.out.println("Error\n");
            System.exit(0);
        }
		if (!checkExtension(args[0])) {
            System.out.println("Error\n");
            System.exit(0);
        }
		String ticketFormat = args[0];
        String ticketFileName = args[1];
        String propertyFileName = args[2];
        String populationFileName = args[3];
        String logFileName = args[4];
        Logger.getInstance(logFileName).log(String.join(" ", args));
        propertyReader propertyReader = new propertyReader(propertyFileName);
        propertyProcessor propertyProcessor = new propertyProcessor(propertyReader);
        populationTextReader populationTextReader = new populationTextReader(populationFileName);
        parkingProcessor parkingViolationProcessor = new parkingProcessor(ticketFormat, ticketFileName);
        userInterface user = userInterface.getInstance();
    }
	
	private static boolean checkExtension(String extension) {
		return extension.equals("json") || extension.equals("csv");
	}
}
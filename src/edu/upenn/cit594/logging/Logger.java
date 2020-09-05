package edu.upenn.cit594.logging;

import java.io.*;

public class Logger {
	private static Logger obj;
    private static File file;
    private Logger(String fileName) {
        file = new File(fileName);
    }
    
    public static Logger getInstance() {
        return obj;
    }
    
    public static Logger getInstance(String fileName) {
        if (obj == null) {
            obj = new Logger(fileName);
        }
        return obj;
    }
    
    public void log(String message) {
        long time = System.currentTimeMillis();
        try {
            FileWriter out = new FileWriter(Logger.file, true);
            out.write(time + " " + message + "\n");
            out.close();
        }
        catch (IOException e) {
            System.err.println("Error\n");
        }
    }
}
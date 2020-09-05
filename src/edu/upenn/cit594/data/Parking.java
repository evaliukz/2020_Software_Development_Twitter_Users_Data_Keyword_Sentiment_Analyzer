package edu.upenn.cit594.data;

public class Parking {
	private String ticketNumber;
    private String plateId;
    private String date;
    private String zip;
    private String violation;
    private int fine;
    private String state;
    
    public Parking(String ticketNumber, String plateId, String date, String zip, String violation, int fine, String state) {
        this.ticketNumber = ticketNumber;
        this.plateId = plateId;
        this.date = date;
        this.zip = zip;
        this.violation = violation;
        this.fine = fine;
        this.state = state;
    }
    
    public int getFine() {
    	return this.fine;
    }
    
    public String getZip() {
    	return this.zip;
    }
    
    public String getReason() {
    	return this.violation;
    }
    
    public String getState() {
    	return this.state;
    }

	public String getTicketNumber() {
		return ticketNumber;
	}

	public String getPlateId() {
		return plateId;
	}

	public String getDate() {
		return date;
	}
}
package edu.upenn.cit594.data;

public class Property {
	private double totalLivableArea;
    private double marketValue;
    private String zip;
    
    public Property(double totalLivableArea, double marketValue, String zip){
        this.totalLivableArea = totalLivableArea;
        this.marketValue = marketValue;
        this.zip = zip;
    }
    
    public String toString() {
        return totalLivableArea + "-" + marketValue + "-" + zip;
    }
    
    public double getTotalLivableArea() {
        return totalLivableArea;
    }
    
    public double getMarketValue() {
        return marketValue;
    }
    
    public String getZip() {
        return zip;
    }
}
package edu.upenn.cit594.data;

import java.util.List;

import edu.upenn.cit594.datamanagement.*;

public class propertyMarketValue implements Reader {
	@Override
    public double[] getInformation(List<Property> listOfProperty) {
        double[] marketValuePerZip = new double[listOfProperty.size()];
        int i = 0;
        for (Property r: listOfProperty) {
            marketValuePerZip[i] = r.getMarketValue();
            i++;
        }
    return marketValuePerZip;
    }
}
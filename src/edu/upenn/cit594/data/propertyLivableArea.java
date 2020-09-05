package edu.upenn.cit594.data;

import java.util.List;

import edu.upenn.cit594.datamanagement.*;

public class propertyLivableArea implements Reader {
	@Override
    public double[] getInformation(List<Property> listOfProperty) {
        double[] livableAreaPerZip = new double[listOfProperty.size()];
        int i = 0;
        for (Property p : listOfProperty) {
            livableAreaPerZip[i] = p.getTotalLivableArea();
            i++;
        }
        return livableAreaPerZip;
    }
}
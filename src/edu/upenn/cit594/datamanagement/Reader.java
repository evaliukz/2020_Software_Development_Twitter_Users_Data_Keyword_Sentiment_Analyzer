package edu.upenn.cit594.datamanagement;

import java.util.List;

import edu.upenn.cit594.data.*;

public interface Reader {
	public double[] getInformation(List<Property> listOfProperty);
}
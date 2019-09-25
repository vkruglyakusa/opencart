package com.opencart.automation.utility;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderHeaderAware;

public class CsvToDataArray {

	static PropertiesReader prop;

	private static String file = ""; 
	private static int rowNumber = 0;
	private static int columnsNumber = 0;
	private static CSVReader reader;

	
	public static Object[][] readCsvData(String fileName) throws IOException {
		prop = new PropertiesReader();
		String path = prop.getProperty("testDataDir");
		file = System.getProperty("user.dir") + path + fileName;
		getArraySize();
		reader = new CSVReaderHeaderAware(new FileReader(file));
		Object[][] data = new Object[rowNumber -1][columnsNumber];
		Object[] values = null;
		int i = 0;
	    while ((values = reader.readNext()) != null) {
	    	data[i] = values;
	    	i ++;
	    }
		return data;
	}
	
	private static void getArraySize() throws IOException {
		reader = new CSVReader(new FileReader(file));
		String[] values = null;
		while ((values = reader.readNext()) != null) {
			rowNumber++;
			if(rowNumber==1 && values != null) {
				columnsNumber = values.length;
			}
		}
	}
	
		
	
	public static void main(String[] args) throws IOException {
		Object[][] test = readCsvData("TestingData.csv");
		int numberOfRow = test.length;
		int nuberOfColumns = test[0].length;
		System.out.println(numberOfRow + "/" + nuberOfColumns);
		System.out.println(Arrays.deepToString(test));
		
	}
}

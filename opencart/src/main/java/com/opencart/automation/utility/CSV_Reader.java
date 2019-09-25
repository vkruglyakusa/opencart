package com.opencart.automation.utility;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.opencart.automation.utility.pojoClasses.Content;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

public class CSV_Reader {


	/**
	 * Method will return a list of zone objects stored in CSV
	 * 
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
//	public ArrayList<?> GetSubwayStationList(String fileName) throws FileNotFoundException {
//
//		// downloadFileFromURL(URL);
//
//		Reader reader = new FileReader(fileName);
//		// let it map the csv column headers to properties
//		CsvToBean<SubwayStationPOJO> csvZones = new CsvToBean<SubwayStationPOJO>();
//		HeaderColumnNameMappingStrategy<SubwayStationPOJO> strategy = new HeaderColumnNameMappingStrategy<SubwayStationPOJO>();
//		strategy.setType(SubwayStationPOJO.class);
//
//		// parse the file and get a list of zones
//		List<SubwayStationPOJO> zones = csvZones.parse(strategy, reader);
//		return (ArrayList<?>) zones;
//
//	}

	/**
	 * Method will return a list of content objects stored in CSV
	 * 
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public ArrayList<?> GetContentList(String fileName) throws FileNotFoundException {

		Reader reader = new FileReader(fileName);
		// let it map the csv column headers to properties
		CsvToBean<Content> csvContent = new CsvToBean<Content>();
		HeaderColumnNameMappingStrategy<Content> strategy = new HeaderColumnNameMappingStrategy<Content>();
		strategy.setType(Content.class);

		// parse the file and get a list of Content values
		List<Content> content = csvContent.parse(strategy, reader);
		return (ArrayList<?>) content;

	}
}

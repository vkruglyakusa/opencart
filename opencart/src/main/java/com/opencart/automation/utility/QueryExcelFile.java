package com.opencart.automation.utility;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

public class QueryExcelFile {

	public static final Logger log = Logger.getLogger( QueryExcelFile.class.getName());
	
	public static Object[][] getDataFromSheet(String excelFile,String sheetName, String sqlQuery) throws ClassNotFoundException, SQLException, InvalidFormatException, IOException {
		
		String log4jConfPath = "configuration/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		
		/**
		 * Create a CSV file from XLS
		 */
		CSVUtils.convertExcelToCSV(excelFile,sheetName);
		
		/**
		 * Create a connection to CSV file
		 **/
		Connection conn = DriverManager.getConnection(
				"jdbc:relique:csv:" + System.getProperty("user.dir") + "/src/main/java/com/automation/cpui/testData/dynamicCsv/");

		/**
		 * Create a Statement object to execute the query with. A Statement is not
		 * thread-safe.
		 **/
		Class.forName("org.relique.jdbc.csv.CsvDriver");
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		/** Select data from .csv file **/
		log.info("SQL query for test-data fetching is: " + sqlQuery);
		ResultSet rs = stmt.executeQuery(sqlQuery);
		int numberOfRows = 0;
		int row = 0;
		int columnCount;
        if (rs.last()) {//make cursor to point to the last row in the ResultSet object
        	numberOfRows = rs.getRow();
            rs.beforeFirst(); //make cursor to point to the front of the ResultSet object, just before the first row.
          }
		columnCount = rs.getMetaData().getColumnCount();
		Object[][] ResultListArray = new Object[numberOfRows][columnCount];
		while (rs.next()) {
			
			for (int i = 0; i < columnCount; i++) {
				ResultListArray[row][i] = rs.getObject(i+1);
			}
			++row;
		}
		log.info("Testing Data Array"+ "[" + ResultListArray.length +"]" + "[" + columnCount + "]" + " is created" );

		return ResultListArray; 

	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException, InvalidFormatException, IOException {
		//String quary = "SELECT TestCaseName,Email,Password,RunMode FROM CPUI_NONPROD_TestData";
		String quary = "SELECT TestCaseName,Email,Password,RunMode FROM CPUI_PROD_TestData";
		String sheetName = "SocialMedia";
		String documentName = System.getProperty("user.dir") + "/src/main/java/com/automation/cpui/testData/" + "CPUI_NONPROD_TestData.xlsx";
		
		Object[][] data = getDataFromSheet(documentName,sheetName,quary);
	}
}

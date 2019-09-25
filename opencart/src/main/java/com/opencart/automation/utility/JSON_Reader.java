package com.opencart.automation.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.opencart.automation.testBase.TestBase;
import com.opencart.automation.utility.pojoClasses.Property;


public class JSON_Reader {
	
	public JSON_Reader() {
		 String log4jConfPath = "configuration/log4j.properties";
		 PropertyConfigurator.configure(log4jConfPath);
	}
	
	public static final Logger log = Logger.getLogger(JSON_Reader.class.getName());
	static Property prop = null;
	static List<Property> listOfProperties = null;
	
	/**
	 * 
	 * @param url to web resource
	 * @return This method will return HTTP connection for reading JSON stream
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static Reader getResponse(String url) throws MalformedURLException, IOException {
		log.info("Establish connection to json resource");
		InetSocketAddress proxyInet = new InetSocketAddress("bcpxy.nycnet", 8080);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyInet);
		URLConnection connection = new URL(url).openConnection(proxy);
		InputStream response = connection.getInputStream();
		return new InputStreamReader(response);
	}
	
	/**
	 * @param urlStr
	 * @param mainJsonElement
	 * @return Method will return list of JSON object under main element
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static ArrayList<JSONObject> getListOfJsonObjects(String urlStr, String mainJsonElement) throws MalformedURLException, IOException {
		log.info("Creating list of JSON object");
		Reader reader = getResponse(urlStr);
		JSONObject jsonObject = (JSONObject) JSONValue.parse(reader);
		JSONArray jsonArray = (JSONArray) jsonObject.get(mainJsonElement);
		List<JSONObject> listOfJsonObjects = new ArrayList<JSONObject>();
		for(Object propertisObject: jsonArray){
			listOfJsonObjects.add((JSONObject) propertisObject);
		}
		return (ArrayList<JSONObject>) listOfJsonObjects;
	}

	
	/**
	 * 
	 * @return Method will return list of properties from JSON stream
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public ArrayList<Property> getListOfProperties(String urlStr) throws MalformedURLException, IOException{
	//	String urlStr = "https://services3.arcgis.com/A6Zjpzrub8ESZ3c7/arcgis/rest/services/vwLatestDateLocator/FeatureServer/0/query?f=pgeojson&outSR=2263&outFields=ActivePOD%20as%20activepod,DOHMHPODLink%20as%20lnk,DOECode%20as%20id,PODSiteName%20as%20name,Address%20as%20addr,Borough%20as%20boro,ZIP%20as%20zip,Ops_status%20as%20status,OpeningTime%20as%20opening,wait_time%20as%20wait,LatestDate%20as%20updated,%20LabelPos%20as%20labelpos&where=activepod%3D1";
		log.info("Creating list of Property objects");
		String mainElement = "features";
		List<JSONObject> propertyList = getListOfJsonObjects(urlStr,mainElement);
		
		listOfProperties = new ArrayList<Property>();
		for (JSONObject prperty : propertyList) {
			prop = new Property();
			JSONObject propGeometry = (JSONObject) prperty.get("geometry");
			JSONArray coordinates =  (JSONArray) propGeometry.get("coordinates");
			JSONObject propertiesObject = (JSONObject) prperty.get("properties");
			
			prop.setX(coordinates.get(0).toString());
			prop.setY(coordinates.get(1).toString());
			prop.setActivepod(propertiesObject.get("activepod").toString());
			prop.setLnk(propertiesObject.get("lnk").toString());
			prop.setId(propertiesObject.get("id").toString());
			prop.setName(propertiesObject.get("name").toString());
			prop.setAddr(propertiesObject.get("addr").toString());
			prop.setBoro(propertiesObject.get("boro").toString());
			prop.setZip(propertiesObject.get("zip").toString());
			prop.setStatus(propertiesObject.get("status").toString());
			prop.setUpdated(propertiesObject.get("updated").toString());
			prop.setLabelpos(propertiesObject.get("labelpos").toString());
			
			listOfProperties.add(prop);
		}
		
		return (ArrayList<Property>) listOfProperties;
	}
	
//	public static void main(String[] args) throws IOException {
//		String urlStr = "https://services3.arcgis.com/A6Zjpzrub8ESZ3c7/arcgis/rest/services/vwLatestDateLocator/FeatureServer/0/query?f=pgeojson&outSR=2263&outFields=ActivePOD%20as%20activepod,DOHMHPODLink%20as%20lnk,DOECode%20as%20id,PODSiteName%20as%20name,Address%20as%20addr,Borough%20as%20boro,ZIP%20as%20zip,Ops_status%20as%20status,OpeningTime%20as%20opening,wait_time%20as%20wait,LatestDate%20as%20updated,%20LabelPos%20as%20labelpos&where=activepod%3D1";
//
//		ArrayList<Property> propertiesList = getListOfProperties(urlStr);
//		System.out.println(propertiesList.size());
//		
//		for (Property property : propertiesList) {
//			
//			System.out.println(property.toString());
//		}
//		
//	}
}

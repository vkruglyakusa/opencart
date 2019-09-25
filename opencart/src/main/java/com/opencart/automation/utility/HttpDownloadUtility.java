package com.opencart.automation.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import org.apache.log4j.Logger;


public class HttpDownloadUtility {
	
	public static final Logger log = Logger.getLogger(HttpDownloadUtility.class.getName());
	private static final int BUFFER_SIZE = 4096;

	/**
	 * Downloads a file from a URL
	 * 
	 * @param fileURL
	 *            HTTP URL of the file to be downloaded
	 * @param saveDir
	 *            path of the directory to save the file
	 * @throws IOException
	 */
	public static void downloadFile(String fileURL, String saveDir) throws IOException {

		URL url = new URL(fileURL);
		InetSocketAddress proxyInet = new InetSocketAddress("bcpxy.nycnet", 8080);
		Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyInet);
		//String httpsURL = "https://maps.nyc.gov/hurricane/data/order.csv";
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection(proxy);

		int responseCode = httpConn.getResponseCode();
		// always check HTTP response code first
		if (responseCode == HttpURLConnection.HTTP_OK) {

			String fileName = "";
			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			int contentLength = httpConn.getContentLength();

			if (disposition != null) {
				// extracts file name from header field
				int index = disposition.indexOf("filename=");
				if (index > 0) {
					fileName = disposition.substring(index + 10, disposition.length() - 1);
				}
			} else {
				// extracts file name from URL
				fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
			}

			log.info("Content-Type = " + contentType);
			log.info("Content-Disposition = " + disposition);
			log.info("Content-Length = " + contentLength);
			log.info("fileName = " + fileName);
			

			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			String saveFilePath = saveDir + File.separator + fileName;

			// opens an output stream to save into file
			FileOutputStream outputStream = new FileOutputStream(saveFilePath);

			int bytesRead = -1;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.close();
			inputStream.close();
			log.info("File downloaded");
		} else {
			log.info("No file to download. Server replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();
	}
	
	public static void main(String[] args) {
		String httpsURL = "https://csgis-stg-prx.csc.nycnet/3k/data/pka.csv";
		String downlaodDir = "C:\\java_projects\\3KFinder\\src\\main\\java\\com\\preKFinder\\automation\\testData\\";
		try {
			downloadFile(httpsURL,downlaodDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

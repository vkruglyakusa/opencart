package com.opencart.automation.testBase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.*;

import com.opencart.automation.utility.QueryExcelFile;
import com.opencart.automation.utility.pojoClasses.Content;
import com.opencart.automation.utility.pojoClasses.Property;
import com.opencart.automation.customListeners.WebEventListener;
import com.opencart.automation.utility.CSV_Reader;
import com.opencart.automation.utility.Excel_Reader;
import com.opencart.automation.utility.HttpDownloadUtility;
import com.opencart.automation.utility.JSON_Reader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.annotations.Optional;

/**
 * 
 * @author vkruglyak
 *
 */

public class TestBase {

	// String log4jConfPath = "configuration/log4j.properties";
	// PropertyConfigurator.configure(log4jConfPath);
	public static final Logger log = Logger.getLogger(TestBase.class.getName());
	public static WebDriver driver;
	Object[][] excelData;
	static Excel_Reader testData;
	public File f;
	public FileInputStream FI;
	public WebEventListener eventListener;
	public static Properties Prop = new Properties();
	public static ExtentReports extent;
	public static ExtentTest test;
	public ITestResult result;
	public static String baseURL;
	public String browser;
	String ExcelName;

	public WebDriver getDriver() {
		return driver;
	}

	static {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

		extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/pom_framework_output/reports/test"
				+ formater.format(calendar.getTime()) + ".html", false);
	}

	public void loadData() throws IOException {
		
		f = new File(System.getProperty("user.dir") + "/configuration/config.properties");
		FI = new FileInputStream(f);
		Prop.load(FI);

	}

	public void setDriver(EventFiringWebDriver driver) {
		this.driver = driver;
	}

	/**
	 * Init methods will be initialize specific webDriver for specific web browser
	 * with predefined based URL
	 * 
	 * @throws IOException
	 *             d
	 */
	@Deprecated
	public void init() throws IOException {
		loadData();
		String log4jConfPath = "configuration/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		// System.out.println(Prop.getProperty("browser"));
		selectBrowser(Prop.getProperty("browser"));
		getUrl(Prop.getProperty("url"));
	}

	/**
	 * Init methods will be initialize specific webDriver for specific web browser
	 * with predefined based URL
	 * 
	 * @param browser
	 * @throws IOException
	 */
	@Deprecated
	public void init(String browser) throws IOException {
		loadData();
		String log4jConfPath = "configuration/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		selectBrowser(browser);
		getUrl(Prop.getProperty("url"));
	}

	/**
	 * Init method will initialized specific webDriver for specific web browser with
	 * predefined based URL
	 * 
	 * @param browser
	 * @param basedUrl
	 * @throws IOException
	 */
	@Deprecated
	public void init(String browser, String basedUrl) throws IOException {
		loadData();
		String log4jConfPath = "configuration/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		selectBrowser(browser);
		getUrl(basedUrl);
	}

	/**
	 * Method will be initialize session of WebDriver and Web Browser based on
	 * supplied parameters or parameters readed from configuration file
	 * 
	 * @param browser
	 * @param baseUrl
	 * @throws IOException
	 */
	public void initialization(String browser, String baseUrl) throws IOException {

		if (browser.isEmpty() && baseUrl.isEmpty()) {
			loadData();
			String log4jConfPath = "configuration/log4j.properties";
			PropertyConfigurator.configure(log4jConfPath);
			selectBrowser(Prop.getProperty("browser"));
			getUrl(Prop.getProperty("tst_base_url"));
			this.baseURL = Prop.getProperty("tst_base_url");
			this.browser = Prop.getProperty("browser");
			log.info("Session has been initialized with parameters that are readed from the configuration file");
		} else if (!browser.isEmpty() && baseUrl.isEmpty()) {
			loadData();
			String log4jConfPath = "configuration/log4j.properties";
			PropertyConfigurator.configure(log4jConfPath);
			selectBrowser(browser);
			getUrl(Prop.getProperty("tst_base_url"));
			this.baseURL = Prop.getProperty("tst_base_url");
			this.browser = browser;
			log.info(
					"Session has been initialized with parameter Based_URL read from configuration file and supplied Browser_Name- "
							+ browser);
		} else if (browser.isEmpty() && !baseUrl.isEmpty()) {
			loadData();
			String log4jConfPath = "configuration/log4j.properties";
			PropertyConfigurator.configure(log4jConfPath);
			selectBrowser(Prop.getProperty("browser"));
			getUrl(baseUrl);
			this.baseURL = baseUrl;
			this.browser = Prop.getProperty("browser");
			log.info(
					"Session has been initialized with parameter Based_URL read from configuration file and supplied Browser_Name- "
							+ browser);
		} else {
			loadData();
			String log4jConfPath = "configuration/log4j.properties";
			PropertyConfigurator.configure(log4jConfPath);
			selectBrowser(browser);
			getUrl(baseUrl);
			this.baseURL = baseUrl;
			this.browser = browser;
			log.info(
					"The browser session has been initialized with supplied parameters - " + browser + " / " + baseUrl);
		}
	}

	/**
	 * Method will initializing browser based on submitted parameters (browser name,
	 * Operation System)
	 * 
	 * @param browser
	 * @return
	 */
	public WebDriver selectBrowser(String browser) {
		if (browser.equals("chrome")) {
			log.info("Test with CHROME browser");
			System.setProperty("webdriver.chrome.driver", "webDrivers\\chromedriver.exe");
			Proxy proxy = new Proxy();
			proxy.setProxyType(ProxyType.MANUAL);
			proxy.setHttpProxy("bcpxy.nycnet:8080");
			proxy.setSslProxy("bcpxy.nycnet:8080");
			proxy.setNoProxy("127.0.0.1,localhost,10.*;192.168.*");
			ChromeOptions options = new ChromeOptions();
			// Create object of HashMap Class
			Map<String, Object> prefs = new HashMap<String, Object>();
			// Set the notification setting it will override the default setting
			prefs.put("profile.default_content_setting_values.notifications", 2);
			options.setExperimentalOption("prefs", prefs);
			options.addArguments("test-type");
			options.addArguments("enable-automation");
			//options.addArguments("--headless");
			options.addArguments("--window-size=1920,1080");
			options.addArguments("--no-sandbox");
			options.addArguments("--disable-extensions");
			options.addArguments("--dns-prefetch-disable");
			options.addArguments("--disable-gpu");
			options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			options.addArguments("--force-device-scale-factor=1");
			options.addArguments("--disable-features=VizDisplayCompositor");
			options.setCapability("proxy", proxy);
			options.setPageLoadStrategy(PageLoadStrategy.NONE);
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();     
			capabilities.setCapability (CapabilityType.ACCEPT_SSL_CERTS, true);
			options.merge(capabilities);
			driver = new ChromeDriver(options);
			Capabilities caps = ((RemoteWebDriver) driver).getCapabilities();
			String browserVersion = caps.getVersion();
			log.info("Browser Version - " + browserVersion);
			EventFiringWebDriver eventFireDriver = new EventFiringWebDriver(driver);
			eventListener = new WebEventListener();
			eventFireDriver.register(eventListener);
			driver = eventFireDriver;
		} else if (browser.equals("firefox") || browser.equals("FIFEFOX")) {
			log.info("Test with FIREFOX browser");
			System.setProperty("webdriver.gecko.driver", "WebDrivers\\geckodriver.exe");
			Proxy proxy = new Proxy();
			proxy.setProxyType(ProxyType.MANUAL);
			proxy.setHttpProxy("bcpxy.nycnet:8080");
			proxy.setSslProxy("bcpxy.nycnet:8080");
			proxy.setNoProxy("127.0.0.1,localhost,10.*;192.168.*");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-extensions");
			options.addArguments("test-type");
			options.addArguments("start-maximized");
			options.addArguments("--ignore-certificate-errors");
			options.addArguments("--headless");
			options.setAcceptInsecureCerts(true);
			options.setCapability("proxy", proxy);
			driver = new FirefoxDriver(options);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			driver = new EventFiringWebDriver(driver);
			eventListener = new WebEventListener();
			((EventFiringWebDriver) driver).register(eventListener);
		} else if (browser.equals("ie") || browser.equals("IE")) {
			log.info("Test with IE browser");
			System.setProperty("webdriver.ie.driver", "webDrivers\\IEDriverServer.exe");
			InternetExplorerOptions options = new InternetExplorerOptions();
			options.enableNativeEvents().enablePersistentHovering().equals(false);
			options.requireWindowFocus().equals(true);
			options.introduceFlakinessByIgnoringSecurityDomains().equals(true);
			driver = new InternetExplorerDriver(options);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			driver = new EventFiringWebDriver(driver);
			eventListener = new WebEventListener();
			((EventFiringWebDriver) driver).register(eventListener);
		}

		return driver;
	}

	public void getUrl(String url) {
		log.info("navigating to :-" + url);
		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	}


	public void waitForElement(WebDriver driver, int timeOutInSeconds, WebElement element) {
		log.info("wait for element ...");
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		wait.until(ExpectedConditions.visibilityOf(element));
	}

	public void getScreenShot(String name) {

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		log.info("Getting screenshot ...");

		try {
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath()
					+ "/test-output/pom_framework_output/screenshots/";
			File destFile = new File(
					(String) reportDirectory + name + "_" + formater.format(calendar.getTime()) + ".png");
			FileUtils.copyFile((File) scrFile, destFile);
			Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath()
					+ "' height='100' width='100'/> </a>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method will highlite specified element
	 * 
	 * @param driver
	 * @param element
	 * @throws InterruptedException
	 */
	public static void highlightMe(WebDriver driver, WebElement element) throws InterruptedException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].style.border='4px solid yellow'", element);
	}

	public Iterator<String> getAllWindows() {
		Set<String> windows = driver.getWindowHandles();
		Iterator<String> itr = windows.iterator();
		return itr;
	}
	
	public void navigateToBaseUrl() {
		log.info("Navigate to base URL");
		driver.get(baseURL);
	}

	public void getScreenShot(WebDriver driver, ITestResult result, String folderName) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

		String methodName = result.getName();

		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath()
					+ "/test-output/pom_framework_output/screenshots/";
			File destFile = new File((String) reportDirectory + "/" + folderName + "/" + methodName + "_"
					+ formater.format(calendar.getTime()) + ".png");
			FileUtils.copyFile(scrFile, destFile);

			Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath()
					+ "' height='100' width='100'/> </a>");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String captureScreen(String fileName) {
		if (fileName == "") {
			fileName = "blank";
		}
		log.info("Capturing screenshot .... ");
		File destFile = null;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");

		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

		try {
			String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath()
					+ "/test-output/pom_framework_output/screenshots/";
			destFile = new File(
					(String) reportDirectory + fileName + "_" + formater.format(calendar.getTime()) + ".png");
			FileUtils.copyFile(scrFile, destFile);
			// This will help us to link the screen shot in testNG report
			Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath()
					+ "' height='100' width='100'/> </a>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return destFile.toString();
	}

	public void log(String data) {
		log.info(data);
		Reporter.log(data);
		test.log(LogStatus.INFO, data);
	}

	public void getresult(ITestResult result) {
		if (result.getStatus() == ITestResult.SUCCESS) {
			test.log(LogStatus.PASS, result.getName() + " test is pass");
		} else if (result.getStatus() == ITestResult.SKIP) {
			test.log(LogStatus.SKIP,
					result.getName() + " test is skipped and skip reason is:-" + result.getThrowable());
		} else if (result.getStatus() == ITestResult.FAILURE) {
			test.log(LogStatus.ERROR, result.getName() + " test is failed" + result.getThrowable());
			String screen = captureScreen("");
			test.log(LogStatus.FAIL, test.addScreenCapture(screen));
		} else if (result.getStatus() == ITestResult.STARTED) {
			test.log(LogStatus.INFO, result.getName() + " test is started");
		}
	}

	@AfterMethod()
	public void afterMethod(ITestResult result) {
		getresult(result);
	}
	
	@Parameters({ "environment", "browserName" })
	@BeforeClass
	public void setUp(@Optional String environment, @Optional String browserName) throws IOException {
		if (environment.isEmpty()) {
			setXlsName("tst");
			setBaseUrl("tst");
		} else {
			setXlsName(environment);
			setBaseUrl(environment);
		}

		initialization(browserName, baseURL);
	}

	@BeforeMethod()
	public void beforeMethod(Method result) {
		// log.info("\n\n -- Method ends -- \n");
		test = extent.startTest(result.getName());
		test.log(LogStatus.INFO, "----" + result.getName() + " - test Started ----");
		log.info("------" + result.getName() + " - test Started ------");
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws IOException {
			closeBrowser();
	}

	@AfterTest
	public void afterTest(ITestContext result) throws IOException {
		test.log(LogStatus.INFO, "\\n\\n --" + result.getName() + " - test Ended -- \\n");
		log.info("-----" + result.getName() + " - test Ended -----");
		extent.flush();
		extent.close();
	}

	/**
	 * method will set .xls file name for test data provider
	 * 
	 * @param data_source
	 * @return
	 * @throws IOException
	 */
	public String setXlsName(String data_source) throws IOException {
		String log4jConfPath = "configuration/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		log.info("Testing data set loading for [" + data_source + "] environment ");
		loadData();
		if (data_source.equals("dev")) {
			ExcelName = Prop.getProperty("dev_data_set");
			log.info("Test data source file is - " + ExcelName);
		} else if (data_source.equals("tst")) {
			ExcelName = Prop.getProperty("tst_data_set");
			log.info("Test data source file is - " + ExcelName);
		} else if (data_source.equals("stg")) {
			ExcelName = Prop.getProperty("stg_data_set");
			log.info("Test data source file is - " + ExcelName);
		} else if (data_source.equals("prod")) {
			ExcelName = Prop.getProperty("prod_data_set");
			log.info("Test data source file is - " + ExcelName);
		}
		return ExcelName;
	}

	public String setBaseUrl(String environment) throws IOException {
		String log4jConfPath = "configuration/log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		loadData();
		if (environment.equals("dev")) {
			baseURL = Prop.getProperty("dev_base_url");
			log.info("Specified BaseURL is - " + baseURL);
		} else if (environment.equals("tst")) {
			baseURL = Prop.getProperty("tst_base_url");
			log.info("Specified BaseURL is - " + baseURL);
		} else if (environment.equals("stg")) {
			baseURL = Prop.getProperty("stg_base_url");
			log.info("Specified BaseURL is - " + baseURL);
		} else if (environment.equals("prod")) {
			baseURL = Prop.getProperty("prod_base_url");
			log.info("Defined test-Base URL is - " + baseURL);
		}
		return baseURL;
	}

	public void closeBrowser() throws IOException {
		log.info("browser is closing");
		//if (driver != null) {
			try {
				extent.endTest(test);
				extent.flush();
				driver.close();
				driver.quit();
				driver = null;
				log.info("browser is closed");
			} catch (Exception e) {
				System.out.println("Caught message " + e.getMessage());
			}

		//}

	}

	/**
	 * This method will compare of text from supplied WebElement with expected text
	 * 
	 * @param desiredText
	 * @param actualTextFromWebElement
	 */
	public void verifyText(String desiredText, String actualTextFromWebElement) throws IOException {
		log.info("Compare expected text with text from Webelement where Expected Text - [" + desiredText
				+ "] and Actual Text - [" + actualTextFromWebElement + "]");
		Assert.assertEquals(actualTextFromWebElement, desiredText);

	}
	
	/**
	 * Method is returning page title
	 * @return
	 */
	public String getTitle() {
		return driver.getTitle();	
	}
	
	/**
	 * explicit wait
	 * 
	 * @param driver
	 * @param element
	 * @param timeOutInSeconds
	 * @return
	 */
	public WebElement waitForElement(WebDriver driver, WebElement element, long timeOutInSeconds) {
		WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
		WebElement until = wait.until(ExpectedConditions.elementToBeClickable(element));
		return element;
	}

	/**
	 * Method will select option from drop_down box with specified text_value
	 * 
	 * @param element
	 * @param value
	 * @throws InterruptedException
	 */
	public void selectOptionInDropDownBox(WebElement element, String value) throws InterruptedException {
		Select select = new Select(element);
		List<WebElement> allOptions = select.getOptions();
		System.out.println("Number of options in DropBox - " + allOptions.size());
		for (WebElement option : allOptions) {
			System.out.println("DropDown Itesms: ");
			if (option.getText().trim().equals(value)) {
				System.out.println(option.getText().trim());
				option.click();
			}
		}
	}

	/**
	 * Method will select text from specified web element (select-box) by visible
	 * text that is defined by the Text_Value parameter
	 * 
	 * @param element
	 * @param textValue
	 * @throws InterruptedException
	 */
	public void visibleInDropDownList(WebElement element, String textValue) throws InterruptedException {
		Select droplist = new Select(element);
		System.out.println(droplist.getFirstSelectedOption().getText());
		droplist.selectByVisibleText(textValue);
	}

	/**
	 * Method will generate random email address
	 * 
	 * @return
	 */
	public String rundomEmailAddress() {
		String emailAddress = null;
		Random random = new Random();
		int number = random.nextInt(100000);
		String randoms = String.format("%06d", number);
		emailAddress = "test" + randoms + "@test.com";
		log.info("System has generated password: " + emailAddress);
		return emailAddress;
	}

	/**
	 * Method will generate random email address for the specified domain name
	 */
	public String rundomEmailAddress(String emailDomainName) {
		String emailAddress = null;
		Random random = new Random();
		int number = random.nextInt(100000);
		String randoms = String.format("%06d", number);
		emailAddress = "test" + randoms + "@" + emailDomainName;
		log.info("System has generated email address: " + emailAddress);
		return emailAddress;
	}

	/**
	 * Method will generate random password address
	 * 
	 * @return
	 */
	public String rundomPassword() {
		Random random = new Random();
		int number = random.nextInt(100000);
		String randoms = "test" + String.format("%06d", number);
		return randoms;
	}



	/**
	 * Method will put test execution on hold ( for up to 60 sec) until desired web
	 * element is loaded
	 * 
	 * @param element
	 * @return
	 * @throws Exception 
	 */
	public WebElement waitForElementPresent(WebElement element) throws Exception {

		WebDriverWait wait = new WebDriverWait(driver, 60);
		long startTime = System.currentTimeMillis();
		wait.until(ExpectedConditions.visibilityOf(element));
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);
		log.info("waiting for loading of element :" + element);
		log.info("Waiting time for element is - " + duration + " milliseconds");
		return element;

	}

	/**
	 * Method will put test execution on hold until desired webelement loaded or
	 * timeout expired
	 * 
	 * @param pageName
	 * @param timeout
	 * @return
	 */
	public WebElement waitForElementPresent(WebElement pageName, int timeout) {
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		long startTime = System.currentTimeMillis();
		WebElement element = wait.until(ExpectedConditions.visibilityOf(pageName));
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);
		log.info("waiting for loading of element :" + element);
		log.info("Waiting time for element is - " + duration + " milliseconds");
		return element;
	}


	/**
	 * Method will put test execution on hold until page is loaded
	 * 
	 * @param timeOutSeconds
	 */
	public void waitForPageToLoad(long timeOutSeconds) {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {

			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		try {
			log.info("Waiting for page to load....");
			WebDriverWait wait = new WebDriverWait(driver, timeOutSeconds);
			wait.until(expectation);
		} catch (Throwable error) {
			log.info("Timeout waiting for Page Laod Requeste to complete after " + timeOutSeconds + " seconds");
			Assert.assertFalse(true);
		}

	}

	/**
	 * implicit wait
	 * 
	 * @param sec
	 * @throws InterruptedException
	 */
	public void implicitWait(int sec) throws InterruptedException {
		System.out.println( driver );
		driver.manage().timeouts().implicitlyWait(sec, TimeUnit.SECONDS);
	}
	
	/**
	 * Fluent wait for element to be clickable
	 * @param element
	 * @return
	 */
	public static WebElement fluentWaitUntilElementToBeClickable(WebElement element) {
		log.info("waiting for state of element to be clickable :" + element.toString());
	    FluentWait<WebDriver> fWait = new FluentWait<WebDriver>(driver).withTimeout(120,TimeUnit.SECONDS)
		        .pollingEvery(5, TimeUnit.SECONDS)
		        .ignoring(NoSuchElementException.class, TimeoutException.class).ignoring(StaleElementReferenceException.class);

		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 2; i++) {
			try {
				fWait.until(ExpectedConditions.visibilityOf(element));
				fWait.until(ExpectedConditions.elementToBeClickable(element));
			} catch (Exception e) {

				log.info("Element Not found trying again - " + element.toString());
				e.printStackTrace();

			}
		}
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);
		log.info("Waiting time for element to become clickable is - " + duration + " milliseconds");
		return element;
	}

	/**
	 * Method will first switch to the parent frame and then we need to switch to
	 * the child frame.
	 */
	public void switchToChildFrame(String ParentFrame, String ChildFrame) {
		try {
			driver.switchTo().frame(ParentFrame).switchTo().frame(ChildFrame);
			System.out.println("Navigated to innerframe with id " + ChildFrame + "which is present on frame with id"
					+ ParentFrame);
		} catch (NoSuchFrameException e) {
			System.out
					.println("Unable to locate frame with id " + ParentFrame + " or " + ChildFrame + e.getStackTrace());
		} catch (Exception e) {
			System.out.println("Unable to navigate to innerframe with id " + ChildFrame
					+ "which is present on frame with id" + ParentFrame + e.getStackTrace());
		}
	}

	/**
	 * Code snippet to switch back to the default content
	 * driver.switchTo.frame("Frame_ID");
	 */
	public void switchtoDefaultFrame() {
		try {
			driver.switchTo().defaultContent();
			System.out.println("Navigated back to webpage from frame");
		} catch (Exception e) {
			System.out.println("unable to navigate back to main webpage from frame" + e.getStackTrace());
		}
	}

	/**
	 * Method switch drive to specific frame
	 * 
	 * @param frameName
	 */
	public void switchToFrame(String frameName) {
		try {
			driver.switchTo().defaultContent();
			driver.switchTo().frame(frameName);
			System.out.println("Navigated to frame " + frameName);
		} catch (Exception e) {
			System.out.println("unable switch to frame" + frameName + e.getStackTrace());
		}
	}

	/**
	 * Method verifying if the text present on the page
	 */
	protected boolean verifyTextOnThePage(String TextForVerification) {
		try {
			boolean b = driver.getPageSource().contains(TextForVerification);
			return b;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * method will refresh a page until specified text is visible
	 * 
	 * @param textForVerificatin
	 * @throws InterruptedException
	 */
	public void reloadPageUntilTextVisible(String textForVerificatin) throws InterruptedException {
		int reloadCounter = 0;
		do {
			driver.navigate().refresh();
			log.info("Page is reloading until text [" + textForVerificatin + "] is available");
			Thread.sleep(5000);
			reloadCounter = reloadCounter + 1;
		} while (!driver.getPageSource().contains(textForVerificatin) && reloadCounter < 100);
	}

	/**
	 * method will refresh a page until specified WebElement is visible
	 * 
	 * @param element
	 */
	public void reloadPageUntilWebElementVisible(WebElement element) {
		int reloadCounter = 0;
		do {
			driver.navigate().refresh();
			log.info("Page is reloading until WebElement [" + element.toString() + "] is available");
			waitForElementPresent(element, 5000);
			reloadCounter = reloadCounter + 1;
		} while (!element.isDisplayed() && reloadCounter < 100);
	}
	

	/**
	 * Method will wait until text is present.
	 * 
	 * @param driver
	 * @param element
	 * @param address
	 * @return WebElement element
	 */
	public WebElement waitForTextPresent(WebDriver driver, WebElement element, String address) {

			WebDriverWait wait = new WebDriverWait(driver, 60);
			long startTime = System.currentTimeMillis();
			wait.until(ExpectedConditions.textToBePresentInElement(element, address));
			long endTime = System.currentTimeMillis();
			long duration = (endTime - startTime);
			log.info("waiting for loading of element :" + element);
			log.info("Waiting time for element is - " + duration + " milliseconds");
			return element;
	}

	/**
	 * Method will put test execution on hold until page is loaded
	 */
    public static void waitUntillPageLoad() {
    	log.info("Waiting for page [" + driver.getCurrentUrl() + "] to load....");
    	long startTime = System.currentTimeMillis();
        ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
                    }
                };
      
        WebDriverWait wait = new WebDriverWait(driver, 120);
        wait.until(pageLoadCondition);

		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);
		log.info("Page loading time is: " + duration + " ms");
    }
    
	/**
	 * Method will wait until title Contains.
	 * 
	 * @param title
	 * @return WebElement element
	 */
	public void waitUntilTitleContain(String title) {
		WebDriverWait wait = new WebDriverWait(driver, 60);
		long startTime = System.currentTimeMillis();
		wait.until(ExpectedConditions.titleContains(title));
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);
		log.info("waiting for loading of title :" + title);
		log.info("Waiting time for title present is - " + duration + " milliseconds");
	}

	/**
	 * Method will click WebElement element
	 * 
	 * @param element
	 */
	public void clickOnElement(WebElement element) {
		element.click();
		log("Successfully clicked on element: " + element);
	}

	/**
	 * Method will return upperCaseAllFirst
	 * 
	 * @param value
	 * @return upperCaseAllFirst String(Array)
	 */
	public void clickByJavaScript(String elementBy, String value) {
		JavascriptExecutor js = (JavascriptExecutor) driver;

		switch (elementBy) {
			case "id":
				js.executeScript( "document.getElementById(" + "\"" + value + "\"" + ").click();" );
				break;

			case "class":
				js.executeScript( "document.getElementsByClassName(" + "\"" + value + "\"" + ")[0].click();" );
				break;

			case "name":
				js.executeScript( "document.getElementsByName(" + "\"" + value + "\"" + ")[0].click();" );
				break;

			case "tagname":
				js.executeScript( "document.getElementsByTagName(" + "\"" + value + "\"" + ")[0].click();" );
				break;

			default:
				log.info( "Clicking is not performed" );
				break;
		}

	}


	public void clickOnElementbyJavaScript(WebElement element)
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", element  );

	}


	/**
	 * Method will get Text for perticular element
	 * 
	 * @param element
	 * @return WebElement element
	 */
	public String getTextOnElement(WebElement element) {
		log(element.getText()+ " for the element: " + element);
		return element.getText();
	}

	/**
	 *
	 * @param element
	 * @param text
	 */
	public void inputText(WebElement element, String text)
	{
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript( "arguments[0].value=arguments[1]", element, text );
	}

	/**
	 * Wait
	 * @param timeInMS
	 */
	public void setTimeOut(long timeInMS)
	{

		long start = System.currentTimeMillis();
		JavascriptExecutor js = (JavascriptExecutor) driver;

		String jsSyntax = "window.setTimeout(arguments[arguments.length - 1], "+timeInMS+");";
		js.executeAsyncScript( jsSyntax );
		log( "Elapsed time: " + (System.currentTimeMillis() - start));


	}

	/**
	 * Method will put execution on hold and Wait until element to be clickable 
	 * @param element
	 * @param timeoutSeconds
	 * @return
	 */
	public WebElement waitUntilElementClickable(WebElement element, long timeoutSeconds  ) {

		try
		{
			WebDriverWait wait = new WebDriverWait(driver, timeoutSeconds);
			long startTime = System.currentTimeMillis();
			wait.until(ExpectedConditions.elementToBeClickable(element));
			long endTime = System.currentTimeMillis();
			long duration = (endTime - startTime);
			log.info("waiting for loading of element :" + element);
			log.info("Waiting time for element is - " + duration + " milliseconds");
			return element;

		}
		catch(Exception e)
		{
			log("Throws an Exception");
			throw e;
		}
	}
	
	/**
	 * Method is validating run mode flag, in case of flag has value "No" test will be skipped 
	 * @param runMode
	 */
	public void runModeValidator(String runMode) {
		log.info("Validation of run mode flag");
		if (runMode.equals("N")) { 
			throw new SkipException("Skip the test"); 
		}	
	}
	
	//--------------------------------Get testing data section ------------------
	
	/**
	 * Method is reading excel data file and return array of testing data
	 * @param ExcelFileName
	 * @param sheetName
	 * @return
	 * @throws IOException
	 */
	public Object[][] getData(String ExcelFileName, String sheetName) throws IOException {
		loadData();
		log.info("read excel file - " + ExcelFileName + "and sheet name is " + sheetName);
		String path = System.getProperty("user.dir") + Prop.getProperty("testDataDir") + ExcelFileName;
		excelData = Excel_Reader.getDataFromSheet(path, sheetName);
		return excelData;
	}

	/**
	 * Method is reading excel data file and return array of testing data
	 * @param sheetname
	 * @param sheetname
	 * @return
	 * @throws IOException
	 */
	public Object[][] getData(String sheetname) throws IOException {
		loadData();
		log.info("read excel file - " + ExcelName + " and sheet name is " + sheetname);
		String path = System.getProperty("user.dir") + Prop.getProperty("testDataDir") + ExcelName;
		excelData = Excel_Reader.getDataFromSheet(path, sheetname);
		return excelData;
	}
	/**
	 * Method returns Array List of java objects fetched from CSV
	 * @param fn
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<?> getCsvData(String fn) throws IOException {
		// Get base url from properties file
		String fileURL = baseURL + Prop.getProperty("csvDirectory");

		// Directory where CSV file will be stored
		String saveDir = System.getProperty("user.dir") + Prop.getProperty("csvFileDirectory");
		String file = saveDir + fn;
		CSV_Reader reader = new CSV_Reader();
	
		if (fn.equals("content.csv")) {
			fileURL = fileURL + fn;
			HttpDownloadUtility.downloadFile(fileURL, saveDir);
			List<Content> contentData = (List<Content>) reader.GetContentList(file);
			return (ArrayList<?>) contentData;
		} else {
			return null;
		}
	}
	
	/**
	 * Method will return Array[][] with set of testing data that will be queried for .xls file
	 * @param excelFile
	 * @param sheetName
	 * @param sqlQuery
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InvalidFormatException
	 * @throws SQLException
	 */
	public Object[][] selectExcelData(String sheetName, String sqlQuery)
			throws IOException, ClassNotFoundException, InvalidFormatException, SQLException {
		loadData();
		String pathToTestData = Prop.getProperty("testDataDir");
		Object[][] data = null;
		/**
		 * Check if excelFile contains external path. 
		 */
		if (new File(ExcelName).isDirectory()) {
			data = QueryExcelFile.getDataFromSheet(ExcelName, sheetName, sqlQuery);
		} else {
			String documentName = System.getProperty("user.dir") + pathToTestData + ExcelName;
			data = QueryExcelFile.getDataFromSheet(documentName, sheetName, sqlQuery);
		}

		return data;

	}
	
	/**
	 * Method returns Array List of java objects fetched from CSV
	 * 
	 * @param fn
	 * @return
	 * @throws IOException
	 */
	public  ArrayList<?> getJsonData() throws IOException {
		// Get json url from properties file
		loadData();
		String jsonURL = Prop.getProperty("jsonUrl");
		JSON_Reader reader = new JSON_Reader();
		ArrayList<Property> propertiesData = reader.getListOfProperties(jsonURL);
		return propertiesData;
	}

}

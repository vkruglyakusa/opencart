package com.opencart.automation.uiActions;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.opencart.automation.testBase.TestBase;

public class LogoutPage extends TestBase{
	WebDriver driver;
	public static final Logger log = Logger.getLogger(LogoutPage.class.getName());
	
	public LogoutPage(WebDriver driver){
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}
	
	@FindBy(linkText = "Continue")
	WebElement Continue;
	
	
	public void clickOnLogoutLink() throws Exception {
		log.info("Click on [My Account Link From Navigation menu] and page object is - " + Continue.toString());
		waitForElementPresent(Continue);
		Continue.click();
	}
}

package com.opencart.automation.uiActions;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.opencart.automation.testBase.TestBase;

public class HomePage extends TestBase{
	
	WebDriver driver;
	public static final Logger log = Logger.getLogger(HomePage.class.getName());
	
	public HomePage(WebDriver driver){
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}
	
	@FindBy(linkText = "My Account")
	WebElement myAccount;
	
	@FindBy(linkText = "Login")
	WebElement LogInNavigationLink;
	
	
	public void clickOnMyAccountLink() throws Exception {
		log.info("Click on [My Account Link From Navigation menu] and page object is - " + myAccount.toString());
		waitForElementPresent(myAccount);
		myAccount.click();
	}
	
	public void clickOnLoginLink() throws Exception {
		log.info("Click on [My Account Link From Navigation menu] and page object is - " + LogInNavigationLink.toString());
		waitForElementPresent(LogInNavigationLink);
		LogInNavigationLink.click();
	}

}
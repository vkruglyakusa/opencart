package com.opencart.automation.uiActions;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.opencart.automation.testBase.TestBase;

public class AccountPage extends TestBase{
	WebDriver driver;
	public static final Logger log = Logger.getLogger(AccountPage.class.getName());
	
	public AccountPage(WebDriver driver){
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}
	
	@FindBy(linkText = "Logout")
	WebElement Logout;
	
	
	public void clickOnLogoutLink() throws Exception {
		log.info("Click on [My Account Link From Navigation menu] and page object is - " + Logout.toString());
		waitForElementPresent(Logout);
		Logout.click();
	}
	
	
}

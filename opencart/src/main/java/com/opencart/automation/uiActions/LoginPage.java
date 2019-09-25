/**
 * 
 */
package com.opencart.automation.uiActions;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.opencart.automation.testBase.TestBase;



/**
 * @author vkruglyak
 *
 */
public class LoginPage extends TestBase{
	
	WebDriver driver;
	public static final Logger log = Logger.getLogger(LoginPage.class.getName());
	
	public LoginPage(WebDriver driver){
		this.driver = driver;
		PageFactory.initElements(driver, this);	
	}
	
	
	@FindBy(how = How.XPATH, using = ".//*[@id='content']/div/div[1]/div/a")
	WebElement newCustomerRegisterButton;
	
	@FindBy(xpath = "/html[1]/body[1]/div[2]/div[1]")
	public WebElement textWrongLoginCredentials;
	
	@FindBy(xpath = ".//*[@id='input-email']")
	public WebElement emailsAddressTextBox;
	
	@FindBy(id = "input-email")
	WebElement emailAddressLable;
	
	@FindBy(id = "input-email")
	WebElement emailTextBox;
	
	@FindBy(id = "input-password")
	WebElement passwordTexBox;
	
	@FindBy(xpath = ".//*[@id='content']/div/div[2]/div/form/div[2]/a")
	WebElement forgetPasswordLink;
	
	@FindBy(xpath = ".//*[@id='content']/div/div[1]/div/a")
	WebElement registrationButton;
	
	@FindBy(linkText = "My Account")
	WebElement MyAccountNavigationLink;
	
	@FindBy(linkText = "Login")
	WebElement LogInNavigationLink;
	
	@FindBy(xpath = ".//*[@id='content']/div/div[2]/div/form/input")
	WebElement loginButton;
	
	public boolean verifyEmailAddressLable() {
		System.out.println("Verifying of existence of lable [E-Mail Address]");
		try {
			emailAddressLable.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean verifyVisabilityOfLoginButton(){
		System.out.println("Verifying of existence of lable [E-Mail Address]");
		try{
			LogInNavigationLink.isDisplayed();
			return true;
		} catch(Exception e){
			return false;
		}
		
	}
	
	public boolean verifyPasswordLable() {
		System.out.println("Verifying of existence of lable [E-Mail Address]");
		try {
			emailAddressLable.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void enterEmailAddress(String email){
		System.out.println("intere value to e-mail address field");
		emailsAddressTextBox.clear();
		emailsAddressTextBox.sendKeys(email);
	}
	
	public void enterPassowrd(String password){
		System.out.println("intere value to password field");
		passwordTexBox.clear();
		passwordTexBox.sendKeys(password);
	}
	
	public void clickOnMyAccountLink() {
		//System.out.println("Click on [My Account Link From Navigation menu]");
		log.info("Click on [My Account Link From Navigation menu] and page object is - " + MyAccountNavigationLink.toString());
		MyAccountNavigationLink.click();
	}
	
	public void clickOnLogInLink(){
		//System.out.println("Click on login button");
		log.info("Click on login button and page object is - " + LogInNavigationLink.toString());
		LogInNavigationLink.click();
	}
	
	public void clickOnRegistrationLink(){
		System.out.println("click on account registration button");
	
		registrationButton.click();
	}
	
	public String getWrongLoginVerificationText() {
		log.info("Get Error login Message and page obect is - " + textWrongLoginCredentials);
		return textWrongLoginCredentials.getText();
	}
	
	public void AccountLogIn(String email, String password) {
		//System.out.println("intere value to e-mail address field");
		log.info("intere value to e-mail address field and page object is - " + emailsAddressTextBox.toString());
		emailsAddressTextBox.clear();
		emailsAddressTextBox.sendKeys(email);
		//System.out.println("intere value to password field");
		log.info("intere value to password field ana page object is - " + passwordTexBox.toString());
		passwordTexBox.clear();
		passwordTexBox.sendKeys(password);
		//System.out.println("Click on login button");
		log.info("\"Click on login button\"" + loginButton.toString());
		loginButton.click();
	}


}

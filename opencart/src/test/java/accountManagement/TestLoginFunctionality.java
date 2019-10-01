package accountManagement;

import org.testng.annotations.Test;

import com.opencart.automation.testBase.TestBase;
import com.opencart.automation.uiActions.AccountPage;
import com.opencart.automation.uiActions.HomePage;
import com.opencart.automation.uiActions.LoginPage;
import com.opencart.automation.uiActions.LogoutPage;
import com.opencart.automation.utility.CsvToDataArray;

import java.io.IOException;

import org.junit.Assert;
import org.testng.annotations.DataProvider;

public class TestLoginFunctionality extends TestBase {

	HomePage homePage;
	LoginPage loginPage;
	AccountPage accauntPage;
	LogoutPage logoutPage;

	@Test(dataProvider = "getExcelData")
	public void LoginToStore(String testCase, String loginName, String password, String runMode) throws Exception {

		navigateToBaseUrl();
		runModeValidator(runMode);

		homePage = new HomePage(driver);
		homePage.clickOnMyAccountLink();
		homePage.clickOnLoginLink();

		loginPage = new LoginPage(driver);
		loginPage.AccountLogIn(loginName, password);
		waitUntillPageLoad();
		Assert.assertEquals(getTitle(), "My Account");

		accauntPage = new AccountPage(driver);
		accauntPage.clickOnLogoutLink();
		waitUntillPageLoad();
		Assert.assertEquals(getTitle(), "Account Logout");

		logoutPage = new LogoutPage(driver);
		logoutPage.clickOnLogoutLink();
	}

	@DataProvider
	//csv data provider
	public Object[][] getCSVData() throws IOException {
		return CsvToDataArray.readCsvData("TestingData.csv");
	}

	@DataProvider
	public Object[][] getExcelData() throws IOException {
		return getData("Login_Data");
	}
}
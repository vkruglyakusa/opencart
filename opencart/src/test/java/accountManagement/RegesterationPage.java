package accountManagement;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class RegesterationPage {

	public static String randomAddress() {
		int AddresName = 10000 + (int) (Math.random() * ((10000 - 100) + 12));

		return AddresName + " Test Random Street Number";
	}

	protected static String getSaltString() {

		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 10) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	public static void main(String[] args) {

		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/webDrivers/chromedriver.exe");

		WebDriver driver = new ChromeDriver();

		driver.manage().window().maximize();
		//test//

		driver.get("http://usablackcaviar.com");
		driver.findElement(By.linkText("My Account")).click();
		driver.findElement(By.linkText("Register")).click();
		driver.findElement(By.id("input-firstname")).clear();
		driver.findElement(By.id("input-firstname")).sendKeys("Test Name");
		driver.findElement(By.id("input-lastname")).clear();
		driver.findElement(By.id("input-lastname")).sendKeys("Test Last Name");
		driver.findElement(By.xpath("//*[@id='input-email']")).clear();
		driver.findElement(By.xpath("//*[@id='input-email']")).sendKeys(getSaltString() + ("@gmail.com"));
		driver.findElement(By.id("input-telephone")).clear();
		driver.findElement(By.id("input-telephone")).sendKeys("7181888888888888");
		driver.findElement(By.id("input-fax")).clear();
		driver.findElement(By.id("input-fax")).sendKeys("718999999999999");
		driver.findElement(By.id("input-company")).clear();
		driver.findElement(By.id("input-company")).sendKeys("Test Usa Black Caviar Company name");
		driver.findElement(By.id("input-address-1")).clear();
		driver.findElement(By.id("input-address-1")).sendKeys(randomAddress());
		driver.findElement(By.id("input-address-2")).clear();
		driver.findElement(By.id("input-address-2")).sendKeys(randomAddress());
		driver.findElement(By.id("input-city")).clear();
		driver.findElement(By.id("input-city")).sendKeys("Brooklyn");
		driver.findElement(By.id("input-postcode")).clear();
		driver.findElement(By.id("input-postcode")).sendKeys("11230");
		driver.findElement(By.id("input-country")).click();
		Select country = new Select(driver.findElement(By.id("input-country")));
		country.selectByVisibleText("Togo");
		driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);
		driver.findElement(By.id("input-zone")).click();
		Select region = new Select(driver.findElement(By.id("input-zone")));
		driver.manage().timeouts().implicitlyWait(200, TimeUnit.SECONDS);
		region.selectByVisibleText("Kara");
		driver.findElement(By.xpath("//*[@id='input-password']")).sendKeys("password123");
		driver.findElement(By.id("input-confirm")).sendKeys("password123");
		driver.findElement(By.xpath("//*[@id='content']/form/fieldset[4]/div/div/label[1]/input")).click();
		driver.findElement(By.xpath("//*[@id=\"content\"]/form/div/div/input[1]")).click();
		driver.findElement(By.cssSelector("#content > form > div > div > input.btn.btn-primary")).click();

		driver.manage().timeouts().implicitlyWait(2000, TimeUnit.SECONDS);

		if (driver.getTitle().equals("Your Account Has Been Created!")) {
			System.out.println("Registration Test Passed");

		} else {
			System.out.println("Registration Test Faild");
		}
		driver.findElement(By.xpath("//*[@id=\"top-links\"]/ul/li[2]/a/i")).click();
		driver.findElement(By.linkText("Logout")).click();
		if (driver.getTitle().equals("Account Logout")) {
			System.out.println("Account Logout PASS PASS PASS");
		} else {
			System.out.println("Account log out FAILL FAIIL FAILL");
		}
		driver.close();

	}
}

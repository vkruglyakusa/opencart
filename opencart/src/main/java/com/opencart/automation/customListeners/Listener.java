package com.opencart.automation.customListeners;

import org.openqa.selenium.WebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.opencart.automation.testBase.TestBase;


public class Listener extends TestBase implements ITestListener, ISuiteListener, IInvokedMethodListener {

	WebDriver driver;

	public void onFinish(ITestContext arg0) {
		Reporter.log("\n ========Test is finished:" + arg0.getName()+"=========== \n", true);
		//log.info("========Test is finished:" + arg0.getName()+"===========");
	}

	public void onStart(ITestContext arg0) {
		Reporter.log("\n ========About to begin executing Test " + arg0.getName()+"========= \n", true);
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		
		printTestResults(arg0);

	}

	public void onTestFailure(ITestResult result) {
		Object currentClass = result.getInstance();
		driver = ((TestBase) currentClass).getDriver();		
		getScreenShot(driver, result, "failed_tests");
		printTestResults(result);
	}

	public void onTestSkipped(ITestResult arg0) {
		Reporter.log("Test is skipped:" + arg0.getMethod().getMethodName());
	}

	public void onTestStart(ITestResult arg0) {
	}

	public void onTestSuccess(ITestResult arg0) {
		printTestResults(arg0);
	}
	
	private void printTestResults(ITestResult result) {

		Reporter.log("Test Method resides in " + result.getTestClass().getName(), true);

		if (result.getParameters().length != 0) {

			String params = "";

			Reporter.log("Test Method had the following parameters : " + params, true);
			Reporter.log("Test Method had the following parameters : " + result.getParameters().toString(), true);

		}

		String status = null;

		switch (result.getStatus()) {

		case ITestResult.SUCCESS:

			status = "Pass";

			break;

		case ITestResult.FAILURE:

			status = "Failed";

			break;

		case ITestResult.SKIP:

			status = "Skipped";

		}

		Reporter.log("Test Status: " + status, true);

	}


	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {}


	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {}


	public void onStart(ISuite suite) {
		Reporter.log("About to begin executing Suite " + suite.getName(), true);	
	}


	public void onFinish(ISuite suite) {
		Reporter.log("==========About to end executing Suite " + suite.getName()+"==============", true);
	}
}
package framework;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Chart;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;


public class Reporter extends Configuration  {
	
	private static final ThreadLocal<Reporter> T=new ThreadLocal<Reporter>();
	
	public static Reporter get() {
		return T.get();
	}
	public static void set(Reporter reporter) {
		T.set(reporter);
	}
	
//*******************************************************************************************************
	@BeforeTest
	public static void initiateReport(ITestContext ctx) {
		UtilityMethods.set(new UtilityMethods());
		UtilityMethods.get().makePath(System.getProperty("user.dir")+Data.Common.executionConfig.get("screenshotPath"));
		String timeStamp=new SimpleDateFormat("yyyy.MM.dd.hh.mm.ss").format(new Date());
		String logFilePath=System.getProperty("user.dir")+"\\"+Data.Common.executionConfig.get("extentReportPath")+"\\"+ctx.getSuite().getName()+"-"+timeStamp+".html";
		Data.Common.curTestName=ctx.getName();
		Data.Common.htmlreporter=new ExtentHtmlReporter(logFilePath);
		Data.Common.extent=new ExtentReports();
		Data.Common.extent.attachReporter(Data.Common.htmlreporter);
		try {
			Data.Common.extent.setSystemInfo("Host Name",InetAddress.getLocalHost().getHostName());
			Data.Common.extent.setSystemInfo("IP Address",InetAddress.getLocalHost().getHostAddress());
			Data.Common.extent.setSystemInfo("User Name",System.getProperty("user.name"));
		}catch(UnknownHostException e) {
			e.printStackTrace();
		}
		Data.Common.htmlreporter.config().setDocumentTitle("Execution results for suite"+ctx.getSuite().getName()+"-"+timeStamp);
		Data.Common.htmlreporter.config().setReportName("Report for the Test"+ctx.getSuite().getName());
		Data.Common.htmlreporter.config().setTestViewChartLocation(ChartLocation.TOP);
		Data.Common.htmlreporter.config().setTheme(Theme.STANDARD);
		
	}
//****************************************************************************************************************************************
	@AfterTest(alwaysRun=true)
	public void afterTest(ITestContext ctx) {
		Status currentResults=Data.Common.logger.getStatus();
		
		switch (currentResults) {
		case PASS:
			Data.Common.logger.log(Status.PASS,MarkupHelper.createLabel("Test case has been Passed.", ExtentColor.GREEN));
			Assert.assertTrue(true);
			break;
		case FAIL:
			Data.Common.logger.log(Status.FAIL, MarkupHelper.createLabel("Test case has been Failed.",ExtentColor.RED));
			Assert.fail("Test"+ctx.getName()+"has failed.");
			break;
		case WARNING:
			Data.Common.logger.log(Status.WARNING, MarkupHelper.createLabel("Test case has been Passed.",ExtentColor.YELLOW));
			Assert.assertTrue(true,"Test"+ctx.getName()+"passed with Warning.");

		default:
			break;
			}
		Data.Common.extent.flush();
	}
//****************************************************************************************************************************************	
	/**
	 * Takes a  screenshot when there is an assertion failure
	 * @param locator - The current ID, Xpath etc. that has not been found on the page
	 */
	public String  getScreenshots(String locator) {
		
		String locatorName=locator.toString().replaceAll("[^a-zA-Z0-9//._", "");
		String screenShotdir=System.getProperty("user.dir")+"\\"+Data.Common.executionConfig.get("screenshot")+"\\";
		File screenshot=((TakesScreenshot)Driver.get()).getScreenshotAs(OutputType.FILE);
		String newFilePath=screenShotdir+locatorName+"_"+ new SimpleDateFormat("MM.dd_HH.mm.ss:SSS").format(new Date())+"_"+Data.Common.curTestName+".png";
		
		File newFileName=new File(newFilePath);
		try {
			FileUtils.moveFile(screenshot, newFileName);
		}catch(IOException io) {
			System.out.println("ERROR: Occured moving file.");
		}
		return newFilePath;
		
	}
//***************************************************************************************************************************
	public static void highlightElement(By by,String stepName,int maxTimeToWait) {
		
	}
}

package framework;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.w3c.dom.Document;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class Data {
	private static final ThreadLocal<Data> T=new ThreadLocal<Data>();
	
	public static Data get() {
		return T.get();
	}
	public static void set(Data data) {
		T.set(data);
	}
	
	public static class Common{
		
		public static WebDriver driver;
		public static ExtentHtmlReporter htmlreporter;
		public static ExtentReports extent;
		public static ExtentTest logger;
		public static HashMap<String, String> envConfigData;
		public static HashMap<String, String> executionConfig;
		public static Logger log4logger;
		public static Document objectRepository;
		public static String curTestName;
		public static String mainWindowHandle;
	}
}

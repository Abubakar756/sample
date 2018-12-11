package framework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CommonMethods {

	private static final ThreadLocal<CommonMethods> T = new ThreadLocal<CommonMethods>();

	public static void set(CommonMethods commonMethods) {
		T.set(commonMethods);
	}

	public static CommonMethods get() {
		return T.get();
	}
	
//*************************************************************************************************************
	/**
	 * switching to Main window
	 * @author Abubakar
	 */
	public void switchToMainWindow() {
		try {
			Driver.get().switchTo().window(Data.Common.mainWindowHandle);	
		}catch(NoSuchWindowException nsw) {
			System.out.println("Main browser is closed.Unable to switch window");
		}
	}
//*********************************************************************************************************
	/**
	 * Switching frame
	 * @param by
	 * @author Abubakar
	 */
	public void switchToFrame(By by) {
		try {
			WebElement element=Driver.get().findElement(by);
			Driver.get().switchTo().frame(element);
		}catch(NoSuchFrameException nsf) {
			System.out.println("");
		}
		
	}
//**************************************************************************************************************************
	
	public static void closeBrowser() {
		try {
			Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
			Runtime.getRuntime().exec("taskkill /F /IM firefox.exe");
			Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe");
			Runtime.getRuntime().exec("taskkill /F /IM microsoftedge.exe");
			Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
			Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
			Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
			Runtime.getRuntime().exec("taskkill /F /IM edgedriver.exe");
		} catch (IOException e) {

			System.out.println("Exception while closing the browsers.");
		}
	}
//***********************************************************************************************************************************************
	
	public static void launchApplication(String browser, String url) {

		browser = (browser == null) ? "chrome" : browser;
		url = (url == null) ? "https://www.freecrm.com/index.html" : url;
		switch (browser.toLowerCase()) {
		case "chrome":
			System.setProperty("webdriver.chrome.driver", "Drivers\\chromedriver.exe");
			Driver.set(new ChromeDriver());
			break;
		case "firefox":
			System.setProperty("webdriver.gecko.driver", "Drivers\\geckodriver.exe");
			Driver.set(new FirefoxDriver());

		default:
			break;
		}

		Driver.get().get(url);

		WebElement element = Driver.get().findElement(By.xpath("//img[@alt='free crm logo']"));
		if (element.isDisplayed()) {
			System.out.println("Navigated login Page.");
		} else {
			System.out.println("Not Navigated login page.");
		}
	}

//************************************************************************************************************************************
	
	public static void login() {
		FileReader fr;
		Properties p;
		String UserName = "";
		String Password = "";
		try {
			fr = new FileReader("TestData\\cridentials.properties");
			p = new Properties();
			p.load(fr);
			UserName = p.getProperty("username");
			Password = p.getProperty("password");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Driver.get().findElement(By.name("username")).sendKeys(UserName);
		Driver.get().findElement(By.name("password")).sendKeys(Password);

		WebElement LoginButton = Driver.get().findElement(By.xpath("//input[@value='Login']"));

		JavascriptExecutor jse = (JavascriptExecutor) Driver.get();
		jse.executeScript("arguments[0].click();", LoginButton);

		Driver.get().switchTo().frame("mainpanel");
		List<WebElement> elem = Driver.get().findElements(By.xpath("//a[@title='Home']"));

		System.out.println(elem.size());
		if (elem.size() > 0) {
			System.out.println("Application launched.");
		} else {
			System.out.println("Application not launched.");
		}

	}
//***************************************************************************************************************************************************
	 public void GenerateReport()
	    {
	        try {
	            //define a HTML String Builder
	            StringBuilder htmlStringBuilder=new StringBuilder();
	            //append html header and title
	            htmlStringBuilder.append("<html><head><title>Selenium Test </title></head>");
	            //append body
	            htmlStringBuilder.append("<body>");
	            //append table
	            htmlStringBuilder.append("<table border=\"1\" bordercolor=\"#000000\">");
	            //append row
	            htmlStringBuilder.append("<tr><td><b>TestId</b></td><td><b>TestName</b></td><td><b>TestResult</b></td></tr>");
	            //append row
	            htmlStringBuilder.append("<tr><td>001</td><td>Login</td><td>Passed</td></tr>");
	            //append row
	            htmlStringBuilder.append("<tr><td>002</td><td>Logout</td><td>Passed</td></tr>");
	            //close html file
	            htmlStringBuilder.append("</table></body></html>");
	            //write html string content to a file
	            WriteToFile(htmlStringBuilder.toString(),"testfile.html");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
//*****************************************************************************************************************************************
	 public void WriteToFile(String fileContent, String fileName) throws IOException {
	        String projectPath = System.getProperty("user.dir");
	        String tempFile = projectPath + File.separator+fileName;
	        File file = new File(tempFile);
	        // if file does exists, then delete and create a new file
	        if (file.exists()) {
	            try {
	                File newFileName = new File(projectPath + File.separator+ "backup_"+fileName);
	                file.renameTo(newFileName);
	                file.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        //write to file with OutputStreamWriter
	        OutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
	        Writer writer=new OutputStreamWriter(outputStream);
	        writer.write(fileContent);
	        writer.close();

	    }
	    

}

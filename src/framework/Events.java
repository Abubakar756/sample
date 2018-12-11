package framework;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class Events {

	private static final ThreadLocal<Events> T=new ThreadLocal<Events>();
	
	public static Events get() {
		return T.get();
	}
	public static void set(Events events) {
		T.set(events);
	}
	
//*******************************************************************************************************
public void launchApplication(String browser, String url){
		
		switch (browser.toLowerCase()) {
		case "chrome":
			System.setProperty("webdriver.chrome.driver", "Drivers\\chromedriver.exe");
			ChromeOptions chromeOptions = new ChromeOptions();
				chromeOptions.addArguments("--ignore-certificate-errors", "--disable-extensions", "--dns-prefetch-disable", "lang=en_US.UTF-8","--disable-infobars","--new-window","--start-maximized");
				chromeOptions.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
				HashMap<String, Object> prefs = new HashMap<String,Object>();
				prefs.put("credentials_enable_service", false);
				prefs.put("profile.password_manager_enabled", false);
				chromeOptions.setExperimentalOption("prefs", prefs);
				//chromeOptions.addArguments("--app="+ url);
				//Data.Common.driver = new ChromeDriver(chromeOptions);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
				Driver.set(new ChromeDriver(chromeOptions));
				Driver.get().get(url);
				
			break;
		case "ie":
				InternetExplorerOptions ieoptions = new InternetExplorerOptions();
				ieoptions.ignoreZoomSettings();
				ieoptions.destructivelyEnsureCleanSession();
		//		UtilityMethods.enableProtectedMode();
				Driver.set(new InternetExplorerDriver(ieoptions));
			//	Data.Common.driver = new InternetExplorerDriver(ieoptions);
				Driver.get().get(url);
				
		case "firefox":
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.setProperty("webdriver.gecko.driver", "Drivers\\geckodriver.exe");
			FirefoxOptions opts = new FirefoxOptions().setLogLevel(FirefoxDriverLogLevel.TRACE);
				Driver.set(new FirefoxDriver(opts));			
				Driver.get().get(url);
		default:
			break;
		}
		
		
		/*WebElement element = waitForElementToDisplay(UtilityMethods.getBy_from_Repository("objectAfterLaunch"), "Launch the Application", 40);
		
		if (element != null){
			Reporter.get().writeLog("pass", "Launch application", "Application has been launched.","Launch Application");
		} else {
			Reporter.get().writeLog("fail", "Launch application", "Application WAS NOT launched.","Launch Application");
			Assert.assertTrue(false,"Test Failed as Application was not launched.");
		}*/
		
		Data.Common.mainWindowHandle = Driver.get().getWindowHandle();
	}
	
//*****************************************************************************************************
	/**
	 * Getting collection of the web elements
	 * @param by
	 * @return
	 * @author Abubakar
	 */
	public static boolean verifyElementExists(By by) {
		
		List<WebElement> elemColl=Driver.get().findElements(by);
		if(elemColl.size()==0) {
			return false;
		}else {
			return true;
		}
	}
//******************************************************************************************************
	/**
	 * closing browser
	 * @author Abubakar
	 */
	public void closeBrowser() {
		try {
			Driver.get().close();
		}catch(Exception e) {
			System.out.println("Exception generated while closing browser.");
		}
	}
//********************************************************************************************************
	/**
	 * Verify WebElement based on WebDriverwait.
	 * @param by
	 * @param maxTime
	 * @return
	 * @author Abubakar
	 */
	public static boolean verifyElementNotExists(By by,int maxTime) {
		try {
			WebDriverWait wait=new WebDriverWait(Driver.get(), maxTime);
			wait.pollingEvery(Duration.ofMillis(300));
			wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
		}catch(Exception e) {
			System.out.println("Exception while waiting for the element to be invisible");
		}
		List<WebElement> element=Driver.get().findElements(by);
		if(element.size()==0) {
			return true;
		}else {
			return false;
		}
	}
//********************************************************************************************************
	/**
	 * Enter values in text fields.
	 * @param by
	 * @param strVal
	 * @param stepName
	 */
	public static void enterValue(By by,String strVal,String stepName) {
		try {
			WebElement element=Driver.get().findElement(by);
			element.clear();
			element.sendKeys(strVal);
		}catch(NoSuchElementException nse) {
			System.out.println(stepName+": no Element found application with given locator"+by.toString());
		}
	}
//**********************************************************************************************************
	/**
	 * Get text from web element
	 * @param by
	 * @param stepName
	 * @return
	 * @author Abubakar
	 */
	public String getTextFromElement(By by,String stepName) {
	
		WebElement element=waitForElementToDisplay(by, stepName,20);
		String strElementTxt="";
		try {
			strElementTxt=element.getText();
		}catch(NullPointerException n) {
			n.printStackTrace();
		}
		return strElementTxt;
	}
//************************************************************************************************************
	/**
	 * Waiting for WebElement to dispaly
	 * @param by
	 * @param stepName
	 * @param maxTime
	 * @return
	 */
	public  WebElement waitForElementToDisplay(By by,String stepName,int maxTime) {
	
		WebElement element=null;
		try {
			WebDriverWait wait=new WebDriverWait(Driver.get(),maxTime);
			wait.pollingEvery(Duration.ofMillis(300));
			element=wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		}catch(NoSuchElementException nse) {
			System.out.println("Element locator"+by.toString()+"is not displayed in the application even after waiting for " + maxTime + " seconds.; Browser - " + Data.Common.curTestName);
		}
		return element;
	}
//******************************************************************************************************************
	/**
	 * clicking operation on button
	 * @param by
	 * @param stepName
	 * @author Abubakar
	 */
	public void clickElement(By by,String stepName) {
		WebElement element=waitForElementToDisplay(by, stepName, 20);
		try {
			if(element.isEnabled()) {
				element.click();
			}else {
				System.out.println("Failed"+stepName+"unable to click element Element is dispalyed.");
			}
			
		}catch(NullPointerException n) {
			Assert.fail("Test case has failed.");
		}
	}
//********************************************************************************************************************
	/**
	 * get item from index
	 * @param element
	 * @param strVal
	 * @return
	 * @author Abubakar
	 */
	public static int getItemIndexList(WebElement element,String strVal) {
		List<WebElement> allOptions=element.findElements(By.tagName("option"));
		int itemIndex=-1;
		for(int i=0;i<allOptions.size();i++) {
			String optionText=allOptions.get(i).getText();
			if(optionText.equalsIgnoreCase(strVal)) {
				itemIndex=i;
				break;
			}
		}
		return itemIndex;
	}
//**********************************************************************************************************************
	/**
	 * selecting value from the listbox
	 * @param by
	 * @param valueToSelect
	 * @param stepName
	 */
	public static void selectByVisibleText(By by,String valueToSelect,String stepName) {
		try {
			WebElement element=Driver.get().findElement(by);
			Select listBox=new Select(element);
			int itemIndex=getItemIndexList(element, valueToSelect);
			if(itemIndex >=0) {
				listBox.selectByIndex(itemIndex);
			}else {
				System.out.println("Failed."+stepName+"the value"+valueToSelect+"is not present in the listbox");
				System.exit(0);
			}
		}catch(NoSuchElementException nse) {
			System.out.println("Failed."+stepName+"unable to select the given value as the listbox with locator "+by.toString()+"is not found application");
		}
	}
//*****************************************************************************************************************************
	/**
	 * Wait the to Dispaly
	 * @param by
	 * @param stepName
	 * @param maxTime
	 * @return
	 * @author Abubakar
	 */
	public boolean  verifyElementDisplayed(By by,String stepName,int maxTime) {
		WebElement element=waitForElementToDisplay(by,stepName,maxTime);
		if(element!=null) {
			return true;
		}else {
			return false;
		}
	}
//*****************************************************************************************************************************
	/**
	 * verifying alert messages
	 * @param strMessage
	 * @author Abubakar
	 */
	public void verifyAlertMessage(String strMessage) {
		
		Alert a=Data.Common.driver.switchTo().alert();
		String alertText=a.getText();
		if(alertText.equalsIgnoreCase(strMessage)) {
			System.out.println("SUCCESS: The text in alert message is matched expected message"+strMessage);
		}else {
			System.out.println("FAILED: The text in alert message is not matched expected message"+'\n'+"expected Message"+strMessage+"actual message"+alertText);
		}
	}
//*******************************************************************************************************************************
	/**
	 * Accept the alert 
	 * @author Abubakar
	 */
	public void acceptAlert() {
		Alert a=Data.Common.driver.switchTo().alert();
		if(a!=null) {
			a.accept();
		}else {
			System.out.println("Fail : no alert is found in application");
		}
	}
//*******************************************************************************************************************************
	/**
	 * Decline alert 
	 * @author Abubakar
	 */
	public void declineAlert() {
		Alert a=Data.Common.driver.switchTo().alert();
		if(a!=null) {
			a.dismiss();
		}else {
			System.out.println("Fail: No alert is found in application");
		}
	}
//********************************************************************************************************************************
	/**
	 * Move mouse on the WebElement
	 * @param element
	 * @param stepName
	 * @author Abubakar
	 */
	public void moveMouseOnToElement(WebElement element,String stepName) {
		Actions action=new Actions(Data.Common.driver);
		if(element!=null) {
			action.moveToElement(element);
		}else {
			System.out.println("Fail:"+'\n'+"stepName:"+stepName+"cause for Fail: unable to perform mouse move operation on element is null.");
		}
	}
//******************************************************************************************************************************************
	/**
	 * Move mouse on the element by using locator
	 * @param by
	 * @param stepName
	 * @author Abubakar
	 */
	public void moveMouseOnToElement(By by,String stepName) {
		Actions action=new Actions(Data.Common.driver);
		try {
			WebElement element=Data.Common.driver.findElement(by);
			action.moveToElement(element);
		}catch(NoSuchElementException nse) {
			System.out.println("Fail:"+'\n'+"stepName:"+stepName+"Reason for failed : no element found given locatior"+by.toString());
		}
	}
//******************************************************************************************************************************************
	/**
	 * Double click on element 
	 * @param element
	 * @param stepName
	 * @author Abubakar
	 */
	public void doubleClickOnElement(WebElement element,String stepName) {
		Actions action=new Actions(Data.Common.driver);
		if(element!=null) {
			action.doubleClick(element);
		}else {
			System.out.println("Fail:"+'\n'+"stepName:"+stepName+"Reason for Failed given element is null.");
		}
	}
//*********************************************************************************************************************************************
	/**
	 * Double click on element using locator
	 * @param by
	 * @param stepName
	 */
	public void doubleClickOnElement(By by,String stepName) {
		Actions action=new Actions(Data.Common.driver);
		try {
			WebElement element=Data.Common.driver.findElement(by);
			action.doubleClick(element);
		}catch(NoSuchElementException nse) {
			System.out.println("Fail:"+'\n'+"stepName"+stepName+"Reason for failed No element found given locator :"+by.toString());
		}
	}
//**************************************************************************************************************************************************
	/**
	 * right click on element using web element
	 * @param element
	 * @param stepName
	 */
	public void rightClickOnElement(WebElement element,String stepName) {
		Actions action=new Actions(Data.Common.driver);
		if(element!=null) {
			action.contextClick(element);
		}else {
			System.out.println("fail:"+'\n'+"stepName:"+stepName+"Reason for failed: given element is null.");
		}
	}
//*****************************************************************************************************************************************
	/**
	 * right click on element using locatior
	 * @param by
	 * @param stepName
	 */
	public void rightClickOnElement(By by,String stepName) {
		Actions action=new Actions(Data.Common.driver);
		try {
			WebElement element=Data.Common.driver.findElement(by);
			action.contextClick(element);
		}catch(NoSuchElementException nse) {
			System.out.println("Fail:"+'\n'+"stepName:"+stepName+"Reason for failure no such element given locator."+by.toString());
		}
	}
//****************************************************************************************************************************************************
	/**
	 * Explicit wait
	 * @param maxTime
	 * @author Abubakar
	 */
	public void checkAlertPresent(int maxTime) {
		try {
			WebDriverWait wait=new WebDriverWait(Data.Common.driver,maxTime);
			wait.pollingEvery(Duration.ofMillis(200));
			wait.until(ExpectedConditions.alertIsPresent());

		}catch(NoSuchElementException e) {
			System.out.println();
		}
	}
	
}


package framework;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class Configuration {

	@BeforeSuite
	public void beforeMethod() {
		
		CommonMethods.closeBrowser();
		UtilityMethods.createFolder(System.getProperty("user.dir")+"//ExecutionResults");
		UtilityMethods.createFolder(System.getProperty("user.dir")+"//ExecutionResults//screenshots");
	}
	
	@Parameters({"browser","url"})
	@BeforeTest
	public void beforeTest(@Optional String browser,@Optional String url) {
		
		CommonMethods.launchApplication(browser, url);
	}
	
	/*@AfterTest
	public  void logout() {
		
		Driver.get().findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
		Driver.get().close();
		Driver.get().quit();
	}*/
}

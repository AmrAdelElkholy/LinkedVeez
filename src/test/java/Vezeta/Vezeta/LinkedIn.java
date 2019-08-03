package Vezeta.Vezeta;

//TestNG
import org.testng.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

//Selenium Packages
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class LinkedIn {
//Generate initial driver Object
	private static WebDriver driver;

	public List<String> ProxyList()
	{
		List<String> ProxyList = new ArrayList<String>();
		ProxyList.add("188.214.232.2:8080");
		ProxyList.add("187.243.248.86:8080");
		ProxyList.add("5.190.92.25:6660");
		ProxyList.add("91.225.78.13:8080");
		ProxyList.add("79.104.197.58:8080");
		ProxyList.add("200.87.75.174:8080");
		ProxyList.add("36.89.149.251:8080");
		ProxyList.add("200.94.140.50:46285");
		ProxyList.add("167.249.181.191:3128");
		//ProxyList.add("201.234.253.24:54184");
		//ProxyList.add("180.248.171.206:8080");
		ProxyList.add("118.140.150.74:3128");
		ProxyList.add("182.253.94.155:8080");
		ProxyList.add("45.166.244.175:8080");
		ProxyList.add("89.29.100.225:3128");
		ProxyList.add("180.180.156.15:43100");
		ProxyList.add("190.152.0.130:55211");


		return ProxyList;
	}

	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	public WebDriver waitForPageLoaded(WebDriver driver) {
		ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
						.equals("complete");
			}
		};
		try {
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver, 30);
			wait.until(expectation);
		} catch (Throwable error) {
			
			org.testng.Assert.fail("Timeout waiting for Page Load Request to complete.");
		}
		return driver;
	}
@BeforeTest
public void setupDriver()
 {
 //Load Chrome Driver location from Command Line of Maven Job
	System.setProperty("webdriver.chrome.driver","chromedriver");
	
	//Map<String, Object> preferences = new Hashtable<String, Object>();
	//preferences.put("profile.default_content_settings.popups", 0);
	// preferences.put("download.default_directory", downloadFilepath);
	
	List<String> ProxyList = ProxyList();
	
	ChromeOptions options = new ChromeOptions();
	//get server randomlly
	int ServerNum = ThreadLocalRandom.current().nextInt(0, (ProxyList.size()-1));
	System.err.println(">>>>>Server"+ProxyList.get(ServerNum));
	options.addArguments("--proxy-server=http://"+ProxyList.get(ServerNum));
	//options.setBinary("/opt/google/chrome/google-chrome");
	System.err.print(">>>>>>>>>>>>>>>>>"+System.getProperty("test.user.profile"));
	options.addArguments("chrome.switches", "--disable-extensions");
	//options.addArguments("--profile-directory");
	options.addArguments("user-agent="+System.currentTimeMillis());
	
	//Define Profile to chrome to start with logged in mail
	options.addArguments("user-data-dir=/home/amr/.config/google-chrome/Profile 1");
	//options.setExperimentalOption("useAutomationExtension", false);
	//options.setExperimentalOption("prefs", preferences);
	options.addArguments("--start-maximized");
	
	driver = new ChromeDriver(options);
 }

@Test(priority = 0)
public void CreateProfile() throws InterruptedException
{
	driver.get("https://www.linkedin.com/");
	
	//wait Join Now button
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find Join now button");
			break;
		}
		try {
			if (isElementPresent(By.xpath("//a[text()='Join now']")))
				break;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	//click join now button
	driver.findElement(By.xpath("//a[text()='Join now']")).click();
	
	
	//wait for first name textbox
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find Join now button");
			break;
		}
		try {
			if (isElementPresent(By.id("first-name")))
				break;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	//send user name
	driver.findElement(By.id("first-name")).sendKeys("Test");
	//Send Last Name
	driver.findElement(By.id("last-name")).sendKeys("Veez");
	//Send Email
	driver.findElement(By.id("join-email")).sendKeys("test.linked.veezeee@gmail.com");
	//Type password
	driver.findElement(By.id("join-password")).sendKeys("Qaz123!@#");
	//Click Submit
	driver.findElement(By.id("submit-join-form-text")).click();
	//>>>Go to Next Page
	
	//wait for first name Location
		for (int second = 0;; second++) {
			if (second >= 30) {
				org.testng.Assert.fail(">>Failed to find Country select");
				break;
			}
			try {
				if (isElementPresent(By.id("location-country")))
					break;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}
	
	//Select Company
	new Select(driver.findElement(By.id("location-country"))).selectByVisibleText("Egypt");
	
	//Type Postal Code
	driver.findElement(By.id("location-postal")).sendKeys("22111");
	
	//Click Next
	driver.findElement(By.xpath("//button[text()='Next']")).click();
	
	//Next page
	//wait for first Position textbox
			for (int second = 0;; second++) {
				if (second >= 30) {
					org.testng.Assert.fail(">>Failed to find Position textbox");
					break;
				}
				try {
					if (isElementPresent(By.id("typeahead-input-for-title")))
						break;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					throw new RuntimeException(e);
				}
			}
			
	//Type Position
	driver.findElement(By.id("typeahead-input-for-title")).sendKeys("Senior SW QC Engineer");
	
	//Type Position
	driver.findElement(By.id("typeahead-input-for-company")).sendKeys("Dummy");
	//Click to remove autocomplete menu
	driver.findElement(By.id("typeahead-input-for-title")).click();
	
	//Select Industry
	new Select(driver.findElement(By.id("work-industry"))).selectByVisibleText("Computer Software");
	
	
	//Click Continue
	driver.findElement(By.xpath("//button[@data-control-name='continue']")).click();
	
	//Go to Next page
	
	//wait for Email confirmation box
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find Email confirmation box");
			break;
		}
		try {
			if (isElementPresent(By.id("email-confirmation-input")))
				break;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		}
	}
	
	
	
	//driver.get("https://mail.google.com");
	//Thread.sleep(60000);
	//driver.close();
	//driver.quit();
}
	
}

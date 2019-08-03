package Vezeta.Vezeta;

//TestNG
import org.testng.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class LinkedInFF {
//Generate initial driver Object
	private static WebDriver driver;
	private static WebDriver driver2;
	DesiredCapabilities capabilities ;
	public String ConfCode="";
	public String ProfilePic=System.getProperty("test.case.ProfilePic");
	//String UserEmail="veez.linked.asl@gmail.com";
	String UserEmail=System.getProperty("test.case.email");
	String Browser=System.getProperty("test.case.browser");
	String Profile =System.getProperty("test.case.profile");

	public List<String> ProxyList()
	{
		List<String> ProxyList = new ArrayList<String>();
		
		ProxyList.add("5.190.92.25:6660");
		
		ProxyList.add("79.104.197.58:8080");
		//ProxyList.add("200.87.75.174:8080");
		//ProxyList.add("36.89.149.251:8080");
		ProxyList.add("167.249.181.191:3128");
		//ProxyList.add("201.234.253.24:54184");
		//ProxyList.add("180.248.171.206:8080");
	
		//ProxyList.add("182.253.94.155:8080");
		ProxyList.add("45.166.244.175:8080");
		ProxyList.add("89.29.100.225:3128");
		//ProxyList.add("180.180.156.15:43100");
		ProxyList.add("190.152.0.130:55211");


		return ProxyList;
	}

	public boolean isElementPresent(By by ,WebDriver driver) {
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
	
	
	
	
	if(Browser.equalsIgnoreCase("chrome"))
	{
		ChromeOptions options = new ChromeOptions();
		//get server randomlly
		List<String> ProxyList = ProxyList();
		int ServerNum = ThreadLocalRandom.current().nextInt(0, (ProxyList.size()-1));
		System.err.println(">>>>>Server"+ProxyList.get(ServerNum));
		options.addArguments("--proxy-server=http://"+ProxyList.get(ServerNum));
		//options.setBinary("/opt/google/chrome/google-chrome");
		System.err.print(">>>>>>>>>>>>>>>>>"+System.getProperty("test.user.profile"));
		//options.addArguments("chrome.switches", "--disable-extensions");
		options.addArguments("profile-directory=Profile 4");
		options.addArguments("user-agent="+System.currentTimeMillis());
		
		//Define Profile to chrome to start with logged in mail
		//options.addArguments("user-data-dir="+Profile);
		//options.setExperimentalOption("useAutomationExtension", false);
		//options.setExperimentalOption("prefs", preferences);
		options.addArguments("--start-maximized");
		driver=new ChromeDriver(options);
	
		driver2=new ChromeDriver(options);
		
		
	}
	else if(Browser.equalsIgnoreCase("firefox")){
		try {
			List<String> ProxyList = ProxyList();
			int ServerNum = ThreadLocalRandom.current().nextInt(0, (ProxyList.size()-1));
			String [] proxy =ProxyList.get(ServerNum).split(":");
			System.setProperty("webdriver.gecko.driver","geckodriver");
			  capabilities = DesiredCapabilities.firefox();
			ProfilesIni profileIni = new ProfilesIni();
			FirefoxProfile fp = profileIni.getProfile(Profile);
			//fp.setAcceptUntrustedCertificates(true);
			//fp.addExtension(new File("/home/amr/EclipseWS/Vezeta/ex1.xpi"));
			//fp.addExtension(new File("/home/amr/EclipseWS/Vezeta/ex2.xpi"));
			fp.setPreference("network.proxy.type", 1);
			fp.setPreference("network.proxy.http", proxy[0]);
			fp.setPreference("network.proxy.http_port", proxy[1]);
			capabilities.setCapability(FirefoxDriver.PROFILE, fp);
			capabilities.setCapability("marionette", true);	
			
			
			
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			driver = new FirefoxDriver(capabilities);
			driver2 = new FirefoxDriver(capabilities);
	}
	
	 
 }

@Test(priority = 0)
public void CreateProfile() throws InterruptedException
{
	driver.get("https://mail.google.com");
	
	//wait Join Now button
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find Join now button");
			break;
		}
		try {
			if (isElementPresent(By.xpath("//a[text()='Join now']"),driver))
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
			if (isElementPresent(By.id("first-name"),driver))
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
	driver.findElement(By.id("join-email")).sendKeys(UserEmail);
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
				if (isElementPresent(By.id("location-country"),driver))
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
					if (isElementPresent(By.id("typeahead-input-for-title"),driver))
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
			if (isElementPresent(By.id("email-confirmation-input"),driver))
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
@Test(priority = 1)
public void getConfCode() throws InterruptedException
{
	
	driver2.get("https://mail.google.com");
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find Email from LinkedIn");
			break;
		}
		try {
			if (isElementPresent(By.xpath("//span[@name='LinkedIn Messages'][@email='security-noreply@linkedin.com'])[2]"),driver2))
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
	
	driver2.findElement(By.xpath("(//span[@name='LinkedIn Messages'][@email='security-noreply@linkedin.com'])[2]"));
	
	for (int second = 0;; second++) {
		if (second >=40 ) {
			org.testng.Assert.fail(">>Failed to find LinkedIn Confirmation Code");
			break;
		}
		try {
			if (isElementPresent(By.xpath("//table/tbody/tr/td[@align='center']/h2[not(contains(text(),'Thank'))]'])[2]"),driver2))
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
	
	ConfCode= driver2.findElement(By.xpath("//table/tbody/tr/td[@align='center']/h2[not(contains(text(),'Thank'))]")).getText();
	Thread.sleep(3000);
		driver2.close();
		driver2.quit();
	
}

@Test(priority = 2)
public void linkedinConf()

{
	driver.findElement(By.id("email-confirmation-input")).sendKeys(ConfCode);
	driver.findElement(By.xpath("//button[@data-control-name='verify']")).click();
}

@Test(priority = 3)
public void editProfile()
{
	driver.get("https://www.linkedin.com");
	
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find Profile Icon");
			break;
		}
		try {
			if (isElementPresent(By.cssSelector("#nav-settings__dropdown-trigger [alt]"),driver))
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
	
	
	// click profile icon
	
	driver.findElement(By.cssSelector("#nav-settings__dropdown-trigger [alt]")).click();
	
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find view profile");
			break;
		}
		try {
			if (isElementPresent(By.xpath("//span[@innertext='View profile']"),driver))
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
	
	//Edit Intro
	driver.findElement(By.xpath("//span[@innertext='View profile']")).click();
	
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find Edit Icon");
			break;
		}
		try {
			if (isElementPresent(By.cssSelector(".pv-top-card-v2-section__edit .large-icon"),driver))
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
	
	driver.findElement(By.cssSelector(".pv-top-card-v2-section__edit .large-icon")).click();
	
	
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find First Name");
			break;
		}
		try {
			if (isElementPresent(By.id("topcard-firstname"),driver))
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
	
	driver.findElement(By.id("topcard-firstname")).sendKeys("FirstEdit");
	driver.findElement(By.id("topcard-lastname")).sendKeys("LastEdit");
	
	driver.findElement(By.xpath("//div/button[@innertext='Save']")).click();
	
	//Add Start Date
	driver.findElement(By.xpath("//button[@data-control-name='add_start_date']")).click();
	
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find start Month");
			break;
		}
		try {
			if (isElementPresent(By.id("position-start-month"),driver))
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
	new Select(driver.findElement(By.id("position-start-month"))).selectByVisibleText("September");
	new Select(driver.findElement(By.id("position-start-year"))).selectByVisibleText("2012");
	
	new Select(driver.findElement(By.id("position-end-month"))).selectByVisibleText("September");
	new Select(driver.findElement(By.id("position-end-month"))).selectByVisibleText("2014");
	
	
	//Upload Photo
	
		for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find start Month");
			break;
		}
		try {
			if (isElementPresent(By.xpath("//button[@data-control-name='add_profile_photo_ge']"),driver))
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
	
		//Upload Photo
		driver.findElement(By.xpath("//button[@data-control-name='add_profile_photo_ge']")).click();
		
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find start Month");
			break;
		}
		try {
			if (isElementPresent(By.xpath("//label[@class='image-selector__file-upload-label artdeco-button ml1 mv0']"),driver))
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
	
	//excute javascript to show hidden upload photo element by remove class 
	
	JavascriptExecutor js = (JavascriptExecutor) driver;  
	js.executeScript("document.getElementById('image-selector__file-upload-input').setAttribute('class', '');");
	
	//Pass Image file
	driver.findElement(By.id("image-selector__file-upload-input")).sendKeys(ProfilePic);
	
		for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find save photo");
			break;
		}
		try {
			if (isElementPresent(By.xpath("//button/span[@innertext='Save photo']"),driver))
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
	
		driver.findElement(By.xpath("//button/span[@innertext='Save photo']")).click();
		
			for (int second = 0;; second++) {
		if (second >= 30) {
			break;
		}
		try {
			if (isElementPresent(By.cssSelector("[class='artdeco-modal__dismiss artdeco-button artdeco-button--circle artdeco-button--muted artdeco-button--2 artdeco-button--tertiary ember-view'] .artdeco-icon"),driver))
			{ 
				driver.findElement(By.cssSelector("[class='artdeco-modal__dismiss artdeco-button artdeco-button--circle artdeco-button--muted artdeco-button--2 artdeco-button--tertiary ember-view'] .artdeco-icon")).click();
				break;}
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
			
// Add Position
			driver.findElement(By.cssSelector(".add-position .artdeco-icon")).click();
			
			for (int second = 0;; second++) {
				if (second >= 30) {
					org.testng.Assert.fail(">>Failed to Position title");
					break;
				}
				try {
					if (isElementPresent(By.id("position-title-typeahead"),driver))
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
			
			driver.findElement(By.id("position-title-typeahead")).sendKeys("Senior SW QC");
			driver.findElement(By.id("position-company-typeahead")).sendKeys("SQQC testCom");
			driver.findElement(By.id("position-title-typeahead")).click();
			driver.findElement(By.id("position-location-typeahead")).sendKeys("Egypt");
			driver.findElement(By.id("position-title-typeahead")).click();
			new Select(driver.findElement(By.id("position-start-month"))).selectByVisibleText("September");
			new Select(driver.findElement(By.id("position-start-year"))).selectByVisibleText("2018");
			driver.findElement(By.cssSelector(".pe-form-footer__actions--space-between [type]")).click();
			
	
}

@Test(priority = 4)
public void LinkedInSignOutForgetPass() throws InterruptedException
{
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find Profile Icon");
			break;
		}
		try {
			if (isElementPresent(By.cssSelector("#nav-settings__dropdown-trigger [alt]"),driver))
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
	
	
	// click profile icon
	
	driver.findElement(By.cssSelector("#nav-settings__dropdown-trigger [alt]")).click();
	
	
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find Profile Icon");
			break;
		}
		try {
			if (isElementPresent(By.xpath("//a[@innertext='Sign out']"),driver))
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
	
	driver.findElement(By.xpath("//a[@innertext='Sign out']")).click();
	
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find forgot password link");
			break;
		}
		try {
			if (isElementPresent(By.xpath("//a[@class='sign-in-form__forgot-password']"),driver))
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
	
	driver.findElement(By.xpath("//a[@class='sign-in-form__forgot-password']")).click();
	
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find Email to send forget password");
			break;
		}
		try {
			if (isElementPresent(By.xpath("//input[@name='userName'][@placeholder='Enter your email or phone']"),driver))
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
	
	driver.findElement(By.xpath("//input[@name='userName'][@placeholder='Enter your email or phone']")).sendKeys(UserEmail);
	
	driver.findElement(By.id("reset-password-submit-button")).click();
	
	Thread.sleep(20000);
	//Got To Gmail to Validate Link
	driver.get("https://mail.google.com");
	
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find Email to send forget password");
			break;
		}
		try {
			if (isElementPresent(By.xpath("//span[contains(text(),'This link will expire in 24 hours LinkedIn')]"),driver))
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
	
	driver.findElement(By.xpath("//span[contains(text(),'This link will expire in 24 hours LinkedIn')]")).click();
	
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find resetpassword link");
			break;
		}
		try {
			if (isElementPresent(By.xpath("//a[@innertext='Reset my password']"),driver))
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
	
	String ForgetPasswordLink = driver.findElement(By.xpath("//a[@innertext='Reset my password']")).getAttribute("href");
	driver.get(ForgetPasswordLink);
	
	for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find resetpassword link");
			break;
		}
		try {
			if (isElementPresent(By.id("newPassword"),driver))
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
	
	driver.findElement(By.id("newPassword")).sendKeys("Wsx123!@#");
	driver.findElement(By.id("confirmPassword")).sendKeys("Wsx123!@#");
	
	driver.findElement(By.id("reset-password-submit-button")).click();
		for (int second = 0;; second++) {
		if (second >= 30) {
			org.testng.Assert.fail(">>Failed to find resetpassword Confirmation");
			break;
		}
		try {
			if (isElementPresent(By.xpath("//h1[@innertext='Your password has been changed successfully']"),driver))
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
}
	
}

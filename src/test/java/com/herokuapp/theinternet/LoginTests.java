package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LoginTests {

	private WebDriver driver;

	@BeforeMethod(alwaysRun = true)
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
		driver = new ChromeDriver();
		// maximize window
		driver.manage().window().maximize();
		// implicit wait
		// driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
	}

	@Test(priority = 1, groups = { "positiveTests", "smokeTests" })
	public void positiveLoginTest() {
		System.out.println("Let's start positiveLogintest!");
		// create driver

		// set url
		String url = "https://the-internet.herokuapp.com/login";
		// open test page
		driver.get(url);
		// sleep(1000);
		String curUrl = driver.getCurrentUrl();
		System.out.println("curURL is: " + curUrl);
//		Create wait variable
		WebDriverWait wait = new WebDriverWait(driver, 5);
		// enter username
		WebElement username = driver.findElement(By.cssSelector("#username"));
		username.sendKeys("tomsmith");
		// enter password
		WebElement password = driver.findElement(By.cssSelector("#password"));
		password.sendKeys("SuperSecretPassword!");
		// click login button
		WebElement loginButton = driver.findElement(By.xpath("//button[@class='radius']"));
		//Actions builder = new Actions(driver);
		//builder.moveToElement(loginButton).build().perform();
		wait.until(ExpectedConditions.elementToBeClickable(loginButton));
		loginButton.click();
		System.out.println("Button clicked");
//		verifications:
//			new url
		String expectedUrl = "https://the-internet.herokuapp.com/secure";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl);
//			successfull login message
		WebElement loggedInMessage = driver.findElement(By.xpath("//div[@id='flash']"));

		if (loggedInMessage.getText().contains("You logged into a secure area!")) {
			System.out.println("text is correct");
		} else
			System.out.println(loggedInMessage.getText());
//			visible logout button
		WebElement logoutButton = driver.findElement(By.cssSelector(".button.secondary.radius"));
//		Assert.assertEquals(logoutButton.isEnabled(), true, "LogOut button is not enabled!!");
		Assert.assertTrue(logoutButton.isDisplayed(), "LogOut button is not displayed");
	}

	@Parameters({ "username", "password", "expectedMessage" })
	@Test(priority = 2, groups = { "negativeTests", "smokeTests" })
	public void negativeLoginTest(String username, String password, String message) {

		System.out.println(
				"Starting negativeLoginTest with parameters: \nusername = " + username + "; password = " + password);
		// maximize window
		driver.manage().window().maximize();
		// set url
		String url = "https://the-internet.herokuapp.com/login";
		// open test page
		driver.get(url);
		// sleep(1000);
		String curUrl = driver.getCurrentUrl();
		System.out.println("curURL is: " + curUrl);

		// enter invalid username
		// String invalidName = "Ivan";
		driver.findElement(By.xpath("//input[@id='username']")).sendKeys(username);
		// enter valid Password
		// String validPassword = "SuperSecretPasswo rd!";
		driver.findElement(By.xpath("//div[@class='large-6 small-12 columns']/input[@id='password']"))
				.sendKeys(password);
		// click on Login button
		driver.findElement(By.cssSelector("button.radius")).click();
		// verifications
		// url is not changed
		Assert.assertTrue(driver.getCurrentUrl().equals(curUrl),
				"URL is invalid\n" + "Expected:" + curUrl + "\nBut got: " + driver.getCurrentUrl());
		// Invalid credentials message appears
		// String expInvalidCredentialsMessage = "Your username is invalid!";
		String actInvalidCredentialsMessage = driver.findElement(By.cssSelector("#flash")).getText();
		int len = actInvalidCredentialsMessage.length();
//		Get the needed text without any extra symbols 		
		String formatedActInvalidCredentialsMessage = actInvalidCredentialsMessage.substring(0, len - 1).trim();
		Assert.assertEquals(formatedActInvalidCredentialsMessage, message, "Incorrect error message is shown.");
		// Assert.assertTrue(driver.findElement(By.cssSelector("#flash")).getText().contains(invalidCredentialsMessage),
		// "The message may be incorrect");
		// assert formatedActInvalidCredentialsMessage.equals(message);
		System.out.println("Error Message is: " + formatedActInvalidCredentialsMessage);
		System.out.println("Test Passed!");

	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		driver.quit();
	}
}

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

public class ExceptionsTests {

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

	@Test(priority = 1)
	public void notVisibleTest() {
		var url = "https://the-internet.herokuapp.com/dynamic_loading/1";
		var expectedText = "Hello World!";
		WebDriverWait wait = new WebDriverWait(driver, 5);
		System.out.println("Let's start exceptions test!");
//		open the page
		driver.get(url);
//		find and click Start button
		sleep(2000);
		driver.findElement(By.xpath("//button[text()='Start']")).click();
//		get the finish element Text
		WebElement finish = driver.findElement(By.id("finish"));
		wait.until(ExpectedConditions.visibilityOf(finish));
		var finishText = driver.findElement(By.xpath("//div[@id='finish']/h4")).getText();

//		compare the actual text with the expected one
		Assert.assertTrue(expectedText.equals(finishText), "Text doesn't match!");
	}

	@Test(priority = 2)
	public void timeoutExceptionTest() {
		var url = "https://the-internet.herokuapp.com/dynamic_loading/1";
		var expectedText = "Hello World!";
		WebDriverWait wait = new WebDriverWait(driver, 2);
		System.out.println("Let's start exceptions test!");
//		open the page
		driver.get(url);
//		find and click Start button
//		sleep (2000);
		driver.findElement(By.xpath("//button[text()='Start']")).click();
//		get the finish element Text
		WebElement finish = driver.findElement(By.id("finish"));
		try {
			wait.until(ExpectedConditions.visibilityOf(finish));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Ecxeption:" + e.getMessage());
		}
		var finishText = driver.findElement(By.xpath("//div[@id='finish']/h4")).getText();

//		compare the actual text with the expected one
		Assert.assertTrue(expectedText.equals(finishText), "Text doesn't match!");
	}

	@Test(priority = 3)
	public void noSuchElementExceptionTest() {
		var url = "https://the-internet.herokuapp.com/dynamic_loading/2";
		var expectedText = "Hello Worldd!";
		WebDriverWait wait = new WebDriverWait(driver, 5);
		System.out.println("Let's start exceptions test!");
//		open the page
		driver.get(url);
//		find and click Start button
//		sleep (2000);
		driver.findElement(By.xpath("//button[text()='Start']")).click();
//		get the finish element Text
		Assert.assertTrue(wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("finish"), expectedText)),
				"Text found but it doesn't match the expected one ");
		/*
		 * WebElement finish =
		 * wait.until(ExpectedConditions.presenceOfElementLocated(By.id("finish")));
		 * 
		 * var finishText =
		 * driver.findElement(By.xpath("//div[@id='finish']/h4")).getText();
		 * 
		 * // compare the actual text with the expected one
		 * Assert.assertTrue(expectedText.equals(finishText), "Text doesn't match!")
		 */;
	}

	@Test
	public void StaleElementReferenceException() {
		var url = "https://the-internet.herokuapp.com/dynamic_controls";
		driver.get(url);
		var removeButton = driver.findElement(By.xpath("//button[text()='Remove']"));
		var checkBox = driver.findElement(By.cssSelector("#checkbox"));
		WebDriverWait wait = new WebDriverWait(driver, 10);
		removeButton.click();
		// wait.until(ExpectedConditions.invisibilityOf(checkBox));
		
		//causes StaleElementReferenceException because checkBox is removed already
//		Assert.assertFalse(checkBox.isDisplayed());
		//To avoid error, the following can be used
//		Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(checkBox)),
//				"CheckBox is still visible, but shouldn't be");
		
		//But the best way is to use .stalnessOf method:
		Assert.assertTrue(wait.until(ExpectedConditions.stalenessOf(checkBox)),
				"CheckBox is still visible, but shouldn't be");
		var addButton = driver.findElement(By.xpath("//button[text()='Add']"));
		addButton.click();
		//Will cause StaleElementExceptions because 'checkBox' references the old checkbox, 
		//while the new checkbox with the same locator has appeared. It needs to be re-defined
		//wait.until(ExpectedConditions.visibilityOf(checkBox));
		WebElement newCheckBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#checkbox")));
		Assert.assertTrue(newCheckBox.isDisplayed(), "CheckBox is not displayed but it should be");
	}
	
	@Test
	public void disabledElementTest( ) {
		String url = "https://the-internet.herokuapp.com/dynamic_controls";
		WebDriverWait wait = new WebDriverWait(driver, 10);
		var expectedText = "It's enabled!";
		driver.get(url);
		//"Enable" button makes the textField enabled and user can type text into it
		WebElement enableButton = driver.findElement(By.xpath("//button[contains(text(), 'Enable')]")); //or ("(//button)[2]") can be used
		enableButton.click();
		
		WebElement textField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//form[@id='input-example']/input")));
		textField.sendKeys("Hello!");
		Assert.assertEquals(textField.getAttribute("value"), "Hello!");
		System.out.println("Entered text: " + textField.getAttribute("value"));
		
		WebElement messageText = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#message")));
		Assert.assertEquals(messageText.getText(), expectedText);
		
		
	}

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		driver.quit();
	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

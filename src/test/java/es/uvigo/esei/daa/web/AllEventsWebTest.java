package es.uvigo.esei.daa.web;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.BeforeTransaction;

import es.uvigo.esei.daa.AbstractTestCase;
import es.uvigo.esei.daa.TestUtils;

public class AllEventsWebTest extends AbstractTestCase {
	private static final int DEFAULT_WAIT_TIME = 5;
	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestUtils.createFakeContext();
		TestUtils.clearTestDatabase();
	}
	
	@BeforeTransaction
	public void beforeTransaction() throws Exception {
		TestUtils.clearTestDatabase();
		TestUtils.initTestDatabase();
	}
	
	@Before
	public void setUp() throws Exception {
		final String baseUrl = "http://localhost:9080/HYS1/";
		
		driver = new FirefoxDriver();
		driver.get(baseUrl);
		driver.manage().addCookie(new Cookie("token", "T21hcjpMdWNhcw=="));
		
		// Driver will wait DEFAULT_WAIT_TIME if it doesn't find and element.
		driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_TIME, TimeUnit.SECONDS);
		
		driver.get(baseUrl + "showEvents.jsp");
		//driver.findElement(By.id("people-list"));
	}
	
	@After
	public void tearDown() throws Exception {
		//TestUtils.clearTestDatabase();
		
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	@Test
	public void testListAll() {
		driver.findElement(By.id("dropdown-toggle-filters")).click();
		driver.findElement(By.id("showEvents.cancelled")).click();
		driver.findElement(By.id("showEvents.completed")).click();
		
		assertEquals(true, driver.findElement(By.id("showEvents.programmed")).isSelected());
		assertEquals(true, driver.findElement(By.id("showEvents.completed")).isSelected());
		assertEquals(true, driver.findElement(By.id("showEvents.cancelled")).isSelected());
		
		verifyXpathCount("//div[contains(@class, 'event-item')]", 15);
	}
	
	@Test
	public void testOnlyCompletedEvents() {
		driver.findElement(By.id("dropdown-toggle-filters")).click();
		driver.findElement(By.id("showEvents.programmed")).click();
		driver.findElement(By.id("showEvents.completed")).click();
		
		assertEquals(false, driver.findElement(By.id("showEvents.programmed")).isSelected());
		assertEquals(true, driver.findElement(By.id("showEvents.completed")).isSelected());
		assertEquals(false, driver.findElement(By.id("showEvents.cancelled")).isSelected());
		
		verifyXpathCount("//div[contains(@class, 'event-item')]", 7);
	}

	@Test
	public void testOnlyCancelledEvents() {
		driver.findElement(By.id("dropdown-toggle-filters")).click();
		driver.findElement(By.id("showEvents.programmed")).click();
		driver.findElement(By.id("showEvents.cancelled")).click();
		
		assertEquals(false, driver.findElement(By.id("showEvents.completed")).isSelected());
		assertEquals(false, driver.findElement(By.id("showEvents.programmed")).isSelected());
		assertEquals(true, driver.findElement(By.id("showEvents.cancelled")).isSelected());
		
		verifyXpathCount("//div[contains(@class, 'event-item')]", 3);
	}

	@Test
	public void testOnlyProgrammedEvents() {
		assertEquals(true, driver.findElement(By.id("showEvents.programmed")).isSelected());
		assertEquals(false, driver.findElement(By.id("showEvents.completed")).isSelected());
		assertEquals(false, driver.findElement(By.id("showEvents.cancelled")).isSelected());
		
		verifyXpathCount("//div[contains(@class, 'event-item')]", 15);
	}
	
	@Test
	public void testListItem() {
		verifyXpathCount("//div[contains(@class, 'event-item')]//h4[contains(.,'Retrospectiva sobre el cine de terror')]", 1);
		verifyXpathCount("//div[contains(@class, 'event-item')]//p[contains(@class, 'list-group-item-text')][contains(.,'Evento cinematográfico sobre las mejores películas de terror hechas en la historia del cine.')]", 1);
	}
	
	@Test
	public void testAllEventsInThirdPage(){
		driver.findElement(By.id("dropdown-toggle-filters")).click();
		driver.findElement(By.id("showEvents.completed")).click();
		driver.findElement(By.id("showEvents.cancelled")).click();
		assertEquals(true, driver.findElement(By.id("showEvents.programmed")).isSelected());
		assertEquals(true, driver.findElement(By.id("showEvents.completed")).isSelected());
		assertEquals(true, driver.findElement(By.id("showEvents.cancelled")).isSelected());
		driver.findElement(By.id("page3")).click();
		verifyXpathCount("//div[contains(@class, 'event-item')]", 1);
	}
	
	@Test
	public void testSearchOnlyProgrammedEventsAndNotFound() {
		assertEquals(true, driver.findElement(By.id("showEvents.programmed")).isSelected());
		assertEquals(false, driver.findElement(By.id("showEvents.completed")).isSelected());
		assertEquals(false, driver.findElement(By.id("showEvents.cancelled")).isSelected());
		driver.findElement(By.id("search")).sendKeys("Nicolas Cage");
		driver.findElement(By.id("submit-search")).click();
		verifyXpathCount("//div[contains(@class, 'event-not-found')]", 1);
	}
	
	@Test
	public void testSearchProgrammedAndCompletedEvents() {
		driver.findElement(By.id("dropdown-toggle-filters")).click();
		driver.findElement(By.id("showEvents.completed")).click();
		assertEquals(true, driver.findElement(By.id("showEvents.programmed")).isSelected());
		assertEquals(true, driver.findElement(By.id("showEvents.completed")).isSelected());
		assertEquals(false, driver.findElement(By.id("showEvents.cancelled")).isSelected());
		driver.findElement(By.id("search")).sendKeys("Charlie Sheen");
		driver.findElement(By.id("submit-search")).click();
		verifyXpathCount("//div[contains(@class, 'event-item')]", 1);
	}

	private boolean waitUntilNotFindElement(By by) {
	    return new WebDriverWait(driver, DEFAULT_WAIT_TIME)
	    	.until(ExpectedConditions.invisibilityOfElementLocated(by));
	}
	
	private boolean waitForTextInElement(By by, String text) {
	    return new WebDriverWait(driver, DEFAULT_WAIT_TIME)
	    	.until(ExpectedConditions.textToBePresentInElementLocated(by, text));
	}

	private void verifyXpathCount(String xpath, int count) {
		try {
			assertEquals(count, driver.findElements(By.xpath(xpath)).size());
		} catch (Error e) {
			verificationErrors.append(e.toString());
		}
	}
}

package tqs.ua.tqs01proj.functional;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Web02CheckSearchResultIT {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @LocalServerPort
  int randomServerPort;

  @BeforeEach
  public void setUp() throws Exception {
    ChromeOptions opt = new ChromeOptions();
    opt.setHeadless(true);
    driver = new ChromeDriver(opt);
    baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void test02CheckSearchResult() throws Exception {
    driver.get("http://localhost:" + randomServerPort + "/");
    driver.findElement(By.id("name")).clear();
    driver.findElement(By.id("name")).sendKeys("Viseu");
    driver.findElement(By.xpath("//input[@value='Go!']")).click();
    driver.findElement(By.xpath("//div/h4")).click();
    Assertions.assertThat(driver.findElement(By.xpath("//div/h4")).getText()).contains("for the city of viseu");
//    assertEquals("Taken at: 2020-04-08T16:00, for the city of viseu", driver.findElement(By.xpath("//div/h4")).getText());
    System.out.println(">> " + driver.findElement(By.xpath("//div/h4")).getText());

  }

  @AfterEach
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}

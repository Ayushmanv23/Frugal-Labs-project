package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Random;

class ShineJobAutomation {

    private static void typeText(WebElement element, String text) throws InterruptedException {
        Random r = new Random();
        for (char c : text.toCharArray()) {
            element.sendKeys(String.valueOf(c));
            Thread.sleep(r.nextInt(120) + 30);
        }
    }

    private static void takeScreenshot(WebDriver driver, String fileName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(fileName));
        } catch (IOException | WebDriverException e) {
            System.err.println("Screenshot failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        WebDriver driver = null;
        try {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized", "--incognito", "--disable-blink-features=AutomationControlled",
                    "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36");

            driver = new ChromeDriver(options);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            JavascriptExecutor executor = (JavascriptExecutor) driver;

            driver.get("https://www.shine.com/");
            wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

            try {
                WebElement initialLoginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class, 'btn-outline-secondary') and contains(text(),'Login')]")));
                initialLoginButton.click();
            } catch (Exception e) {
                try {
                    WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn-close")));
                    executor.executeScript("arguments[0].click();", closeButton);
                } catch (Exception ignored) {}

                WebElement topLoginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.track_login_click")));
                executor.executeScript("arguments[0].click();", topLoginButton);
            }

            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_email_login")));
            WebElement passwordField = driver.findElement(By.id("id_password"));
            typeText(emailField, "ayushmanvohra7@gmail.com");
            typeText(passwordField, "Mahadev23@");

            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.cls_base_1_pw_login_btn")));
            executor.executeScript("arguments[0].click();", loginButton);

            wait.until(ExpectedConditions.urlContains("/myshine/mydashboard/"));
            takeScreenshot(driver, "post_login_screenshot.png");

            try {
                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                WebElement skillSubmitButton = shortWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.skillUpdateNova_modalInnerBottomBtn__fKhUK")));
                skillSubmitButton.click();
            } catch (Exception ignored) {}

            WebElement mainSearchBar = wait.until(ExpectedConditions.elementToBeClickable(By.id("id_searchBase_new")));
            mainSearchBar.click();

            WebElement jobTitleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_q")));
            WebElement locationInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_loc")));
            WebElement experienceInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("searchBar_experience")));
            typeText(jobTitleInput, "Software Tester");
            typeText(locationInput, "Hyderabad");
            typeText(experienceInput, "2");

            WebElement searchButton = driver.findElement(By.id("id_new_search_submit_button"));
            searchButton.click();
            wait.until(ExpectedConditions.titleContains("Software Tester Jobs in Hyderabad"));
            takeScreenshot(driver, "job_search_details_screenshot.png");

            Thread.sleep(20000);

            try {
                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                WebElement popUp1 = shortWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.modalNova_modalClose__sxVHP")));
                executor.executeScript("arguments[0].click();", popUp1);
            } catch (Exception ignored) {}

            try {
                WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
                WebElement popUp2 = shortWait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn-close")));
                executor.executeScript("arguments[0].click();", popUp2);
            } catch (Exception ignored) {}

            List<WebElement> jobListings = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("div.jobCardNova_bigCard__W2xn3"), 1));
            WebElement secondJob = jobListings.get(1);

            secondJob.click();
            takeScreenshot(driver, "job_selection_screenshot.png");

            WebElement applyOnDetailsPageButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button[id^='id_apply_']")));
            executor.executeScript("arguments[0].click();", applyOnDetailsPageButton);

            WebElement getCallButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.interviewAssuredModalNova_interviewModalInnerBottomBtn__w6zor")));
            getCallButton.click();

            WebElement doneButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.youAreAllSetModalNova_youAreModalInnerBottomBtn__2vgYS")));
            doneButton.click();

            WebElement confirmation = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[contains(text(), 'Applied')]")));
            takeScreenshot(driver, "application_successful_screenshot.png");

        } catch (Exception e) {
            System.err.println("ERROR: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            if (driver != null) takeScreenshot(driver, "error_screenshot_" + System.currentTimeMillis() + ".png");
        } finally {
            if (driver != null) driver.quit();
        }
    }
}

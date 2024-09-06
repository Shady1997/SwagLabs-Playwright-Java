package testcases;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitUntilState;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Project Name    : Swags-Labs
 * Developer       : Shady Ahmed
 * Version         : 1.0.0
 * Date            : 06/09/2024
 * Time            : 7:27 PM
 * Description     : Converted BaseTest class for Playwright Java
 **/

public class BaseTest {

    protected Browser browser;
    protected Page page;
    protected Playwright playwright;
    protected BrowserType browserType;

    // Extent report
    protected static ExtentSparkReporter htmlReporter;
    protected static ExtentReports extent;
    protected static ExtentTest test;
    private static String PROJECT_NAME = null;
    private static String PROJECT_URL = null;

    // Define Suite Elements
    static Properties prop;
    static FileInputStream readProperty;

    @BeforeSuite
    public void defineSuiteElements() throws IOException {
        // Initialize the HtmlReporter
        htmlReporter = new ExtentSparkReporter(System.getProperty("user.dir") + "/testReport.html");

        // Initialize ExtentReports and attach the HtmlReporter
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        setProjectDetails();

        // Initialize test in the report
        test = extent.createTest(PROJECT_NAME + " Test Automation Project");

        // Configure the report's appearance
        htmlReporter.config().setDocumentTitle(PROJECT_NAME + " Test Automation Report");
        htmlReporter.config().setReportName(PROJECT_NAME + " Test Report");
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
    }

    @BeforeMethod
    @Parameters("browser")
    public void setupDriver(@Optional String browserName) {
        // Initialize Playwright
        playwright = Playwright.create();
        browserType = null;

        // Setup Browser based on input parameters
        if (browserName.equalsIgnoreCase("chromium")) {
            browserType = playwright.chromium();
        } else if (browserName.equalsIgnoreCase("firefox")) {
            browserType = playwright.firefox();
        } else if (browserName.equalsIgnoreCase("webkit")) {
            browserType = playwright.webkit();
        }

        // Launch browser
        browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false));

        // Create a context with video recording enabled
        BrowserContext context = browser.newContext(new Browser.NewContextOptions()
                .setRecordVideoDir(Paths.get("videos/"))  // Directory to save video recordings
                .setRecordVideoSize(1280, 720)) ;   // Resolution for video recording

        // Create a new page in the context
        page = context.newPage();

        // Maximize window (Playwright automatically launches maximized)
        page.navigate(PROJECT_URL, new Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE));

        test.log(Status.INFO, "Browser launched and URL loaded");

    }

    private void setProjectDetails() throws IOException {
        // Read properties from the configuration file
        readProperty = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/properties/environment.properties");
        prop = new Properties();
        prop.load(readProperty);

        // Set project name and URL from properties file
        PROJECT_NAME = prop.getProperty("projectName");
        PROJECT_URL = prop.getProperty("url");
    }

    @AfterSuite
    public void tearDown() throws IOException {
        extent.flush();
        // Optionally, start HTML report after test suite execution (custom logic can be used here)
        // Runtime.getRuntime().exec("open " + System.getProperty("user.dir") + "/testReport.html");
    }

    @AfterMethod
    public void getResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, result.getName() + " failed with error: " + result.getThrowable());
            Reporter.log("Failed to perform " + result.getName(), 10, true);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, result.getName() + " passed successfully.");
            Reporter.log("Successfully performed " + result.getName(), 10, true);
        } else {
            test.log(Status.SKIP, result.getName() + " skipped.");
            Reporter.log("Skipped " + result.getName(), 10, true);
        }
    }

    @AfterTest
    public void quit() {
        if (page != null) {
            page.close();
        }
        if (browser != null) {
            browser.close();
        }
    }
}

package reports;

import org.testng.*;
import org.testng.xml.XmlSuite;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyListener extends TestListenerAdapter implements IReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        //Iterating over each suite included in the test
        for (ISuite suite : suites) {
            //Following code gets the suite name
            String suiteName = suite.getName();

            //Getting the results for the said suite
            Map<String, ISuiteResult> testSuiteResult = suite.getResults();
            for (ISuiteResult sr : testSuiteResult.values()) {
                ITestContext tc = sr.getTestContext();
                System.out.println("Passed tests for suite '" + suiteName +
                        "' is:" + tc.getPassedTests().getAllResults().size());
                System.out.println("Failed tests for suite '" + suiteName +
                        "' is:" + tc.getFailedTests().getAllResults().size());
                System.out.println("Skipped tests for suite '" + suiteName +
                        "' is:" + tc.getSkippedTests().getAllResults().size());
            }
            // TODO Auto-generated method stub
            suite = suites.get(0);
            Map<String, Collection<ITestNGMethod>> methodsByGroup = suite.getMethodsByGroups();
            Map<String, ISuiteResult> tests = suite.getResults();

            for (String key : tests.keySet()) {
                System.out.println("Key: " + key + ", Value: " + tests.get(key));
            }

            Collection<ISuiteResult> suiteResults = tests.values();
            ISuiteResult suiteResult = suiteResults.iterator().next();
            ITestContext testContext = suiteResult.getTestContext();

            // Check if the "smoke" group exists in methodsByGroup
            Collection<ITestNGMethod> perfMethods = methodsByGroup.get("smoke");
            if (perfMethods != null) {
                IResultMap failedTests = testContext.getFailedTests();
                for (ITestNGMethod perfMethod : perfMethods) {
                    Set<ITestResult> testResultSet = failedTests.getResults(perfMethod);
                    for (ITestResult testResult : testResultSet) {
                        System.out.println("Test " + testResult.getName() + " failed, error " + testResult.getThrowable());
                    }
                }

                IResultMap passedTests = testContext.getPassedTests();
                for (ITestNGMethod perfMethod : perfMethods) {
                    Set<ITestResult> testResultSet = passedTests.getResults(perfMethod);
                    for (ITestResult testResult : testResultSet) {
                        System.out.println("Test " + testResult.getName() + " passed, time took "
                                + (testResult.getEndMillis() - testResult.getStartMillis()));
                    }
                }
            } else {
                System.out.println("No methods found in the 'smoke' group.");
            }
        }
    }
}
package de.friedenhagen.test;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlRunListenerTest {

    final ByteArrayOutputStream out = new ByteArrayOutputStream();

    final XmlRunListener sut = new XmlRunListener(out);
    private final Result result = new Result();
    private final RunListener listener = result.createListener();

    @After
    public void closeOut() throws IOException {
        out.close();
    }

    @Test
    public void testTestRunStarted() throws Exception {
        sut.testRunStarted(Description.EMPTY);
        sut.testRunFinished(new Result());
        assertThat(out.toString("UTF-8"))
                .startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>")
                .contains("tests=\"0\"")
                .endsWith("</testsuite>");
    }

    @Test
    public void testTestStarted() throws Exception {
        listener.testStarted(Description.TEST_MECHANISM);
        listener.testFinished(Description.TEST_MECHANISM);
        sut.testRunStarted(Description.EMPTY);
        sut.testStarted(Description.TEST_MECHANISM);
        sut.testFinished(Description.TEST_MECHANISM);
        sut.testRunFinished(result);
        final String actual = out.toString("UTF-8");
        assertThat(actual)
                .startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>")
                .contains("tests=\"1\"")
                .contains("failures=\"0\"")
                .endsWith("</testsuite>");
    }

    @Test
    public void testTestIgnored() throws Exception {
        final Description ignore = Description.createTestDescription(XmlRunListenerTest.class, "Ignore", new Ignore() {
                    public Class<? extends Annotation> annotationType() {
                        return Ignore.class;
                    }

                    public String value() {
                        return "Ignore it";
                    }
                }
        );
        listener.testIgnored(ignore);
        listener.testFinished(ignore);
        sut.testRunStarted(Description.EMPTY);
        sut.testStarted(ignore);
        sut.testIgnored(ignore);
        sut.testRunFinished(result);
        final String actual = out.toString("UTF-8");
        assertThat(actual)
                .startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>")
                .contains("tests=\"1\"")
                .contains("skip=\"1\"")
                .endsWith("</testsuite>");

    }

    @Test
    public void testTestAssumptionFailure() throws Exception {

    }

    @Test
    public void testTestFailure() throws Exception {
        listener.testStarted(Description.TEST_MECHANISM);
        final Failure failure = new Failure(Description.TEST_MECHANISM, new RuntimeException());
        listener.testFailure(failure);
        listener.testFinished(Description.TEST_MECHANISM);
        sut.testRunStarted(Description.EMPTY);
        sut.testStarted(Description.TEST_MECHANISM);
        sut.testFailure(failure);
        sut.testRunFinished(result);
        final String actual = out.toString("UTF-8");
        assertThat(actual)
                .startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>")
                .contains("tests=\"1\"")
                .contains("failures=\"1\"")
                .endsWith("</testsuite>");
    }

    @Test
    public void testTestFinished() throws Exception {

    }
}
package de.friedenhagen.test;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by mifr on 14.10.15.
 */
public class XmlRunListenerTest {

    final ByteArrayOutputStream out = new ByteArrayOutputStream();

    final XmlRunListener sut = new XmlRunListener(out);

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
        sut.testRunStarted(Description.EMPTY);
        sut.testStarted(Description.EMPTY);
        sut.testFinished(Description.EMPTY);
        sut.testRunFinished(new Result());
        assertThat(out.toString("UTF-8"))
                .startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>")
                .contains("tests=\"0\"")
                .endsWith("</testsuite>");
    }

    @Test
    public void testTestIgnored() throws Exception {

    }

    @Test
    public void testTestAssumptionFailure() throws Exception {

    }

    @Test
    public void testTestFailure() throws Exception {

    }

    @Test
    public void testTestFinished() throws Exception {

    }
}
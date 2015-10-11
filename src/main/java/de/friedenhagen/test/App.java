package de.friedenhagen.test;

import org.junit.experimental.ParallelComputer;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws FileNotFoundException {
        final XmlRunListener runListener = new XmlRunListener(new FileOutputStream("target/out.xml"));
        final JUnitCore core = new JUnitCore();
        core.addListener(runListener);
        core.addListener(new TextListener(System.out));
        final Result run = core.run(ParallelComputer.methods(), MyDataDrivenTest.class);
        System.out.printf("Count %d, Time: %.3f, Failures: %s\n", run.getRunCount(), run.getRunTime()/1000.0, run.getFailures());
    }
}

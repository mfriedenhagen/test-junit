package de.friedenhagen.test;

import org.junit.experimental.ParallelComputer;
import org.junit.internal.TextListener;
import org.junit.runner.Computer;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws FileNotFoundException {
        final XmlRunListener xmlRunListener = new XmlRunListener(new FileOutputStream("target/out.xml"));
        final JUnitCore core = new JUnitCore();
        core.addListener(xmlRunListener);
        core.addListener(new TextListener(System.out){
            @Override
            public void testAssumptionFailure(Failure failure) {
                System.out.print("I");
            }
        });
        final Computer computer = ParallelComputer.methods();
        //final Computer computer = Computer.serial();
        final Result run = core.run(computer, MyDataDrivenTest.class);
        System.out.printf("Count %d, Time: %.3f, Failures: %s\n", run.getRunCount(), run.getRunTime()/1000.0, run.getFailures());
    }
}

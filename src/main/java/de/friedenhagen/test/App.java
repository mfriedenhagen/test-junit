package de.friedenhagen.test;

import org.junit.internal.TextListener;
import org.junit.runner.Computer;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Locale;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws FileNotFoundException {
        Locale.setDefault(Locale.ENGLISH);
        final XmlRunListener xmlRunListener = new XmlRunListener(new FileOutputStream("target/out.xml"));
        final JUnitCore core = new JUnitCore();
        core.addListener(xmlRunListener);
        core.addListener(new MyTextListener(System.out));

        final Boolean runParallel = Boolean.valueOf(System.getProperty("runParallel", "true"));
        final Computer computer = new FixedThreadParallelComputer(10);
        //final Computer computer = Computer.serial();
        // final Computer computer = runParallel ? ParallelComputer.methods() : Computer.serial();
        final Result run = core.run(computer, MyDataDrivenTest.class);
        System.out.printf(
            Locale.ENGLISH,
            "Count %d, Time: %.3f, Failures:\n%s\n",
            run.getRunCount(),
            run.getRunTime() / 1000.0,
            run.getFailures());
    }

    private static class MyTextListener extends TextListener {

        private final PrintStream out;
        private final ThreadLocal<StringBuilder> message = new ThreadLocal<>();

        public MyTextListener(PrintStream out) {
            super(out);
            this.out = out;
        }

        @Override
        public void testStarted(Description description) {
            message.set(new StringBuilder(description.getDisplayName()));
        }

        @Override
        public void testFailure(Failure failure) {
            message.get().append(String.format(Locale.ENGLISH, " \u001B[31m%s\u001B[0m", failure.getMessage()));
        }

        @Override
        public void testFinished(Description description) throws Exception {
            out.printf(Locale.ENGLISH, "%s\n", (message.get().toString()));
            message.set(null);
        }

        @Override
        public void testAssumptionFailure(Failure failure) {
            message.get().append(String.format(Locale.ENGLISH, " \u001B[33m%s\u001B[0m", failure.getMessage()));
        }
    }
}

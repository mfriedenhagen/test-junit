package de.friedenhagen.test;

import org.junit.runner.Computer;
import org.junit.runner.Runner;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.RunnerBuilder;
import org.junit.runners.model.RunnerScheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by mirko on 08.06.16.
 */
public class MyParallelComputer extends Computer {

    private final int threadCount;
    public MyParallelComputer() {
        this(5);
    }
    public MyParallelComputer(int threadCount) {
        this.threadCount = threadCount;
    }
    private Runner parallelize(Runner runner) {
        if (runner instanceof ParentRunner) {
            ((ParentRunner<?>) runner).setScheduler(new RunnerScheduler() {
                private final ExecutorService fService = Executors.newFixedThreadPool(threadCount);

                public void schedule(Runnable childStatement) {
                    fService.submit(childStatement);
                }

                public void finished() {
                    try {
                        fService.shutdown();
                        fService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace(System.err);
                    }
                }
            });
        }
        return runner;
    }
    @Override
    protected Runner getRunner(RunnerBuilder builder, Class<?> testClass)
            throws Throwable {
        Runner runner = super.getRunner(builder, testClass);
        return parallelize(runner);
    }

}

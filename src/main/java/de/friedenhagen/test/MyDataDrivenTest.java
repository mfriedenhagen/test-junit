package de.friedenhagen.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assume.assumeTrue;

/**
 * Created by mirko on 07.10.15.
 */
@RunWith(Parameterized.class)
public class MyDataDrivenTest {

    private final int current;

    @Parameterized.Parameters
    public static Iterable data() {
        return new Iterable<Object[]>() {
            public Iterator<Object[]> iterator() {
                return new Iterator<Object[]>() {

                    volatile int i = 0;

                    public boolean hasNext() {
                        return i < 102;
                    }

                    public Object[] next() {
                        return new Object[]{i++};
                    }
                };
            }
        };
    }

    public MyDataDrivenTest(int current) {
        this.current = current;
    }

    @Test
    public void thirteenIsTheEvilNumber() {
        assumeTrue("Skip numbers divisable by 13", current % 13 != 0);
        assertNotEquals("Must not be divisable by 101", 0, current % 101);
    }
}

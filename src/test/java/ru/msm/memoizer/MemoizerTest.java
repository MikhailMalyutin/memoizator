package ru.msm.memoizer;

import org.junit.Test;
import org.testng.Assert;
import ru.msm.memoizer.cache.HashMapCacheService;

import java.util.function.Function;

public class MemoizerTest {
    class Inner{
        private int i;

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public void add(int i) {
            this.i += i;
        }
    }

    @Test
    public void testAll() throws Throwable {
        Inner inner = new Inner();
        inner.setI(2);

        Function<Integer, Integer> a = (Integer i) -> {
            inner.add(i);
            return inner.i;
        };

        Memoizer memoizer = new Memoizer(new HashMapCacheService());
        memoizer.memoize(a, 5, false, false);
        memoizer.memoize(a, 5, false, false);
        Assert.assertEquals(inner.getI(), 7);
        memoizer.memoize(a, 6, false, false);
        Assert.assertEquals(inner.getI(), 13);
    }
}

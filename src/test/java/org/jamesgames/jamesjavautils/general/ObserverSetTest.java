package org.jamesgames.jamesjavautils.general;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ObserverSetTest {

    private static interface ExampleObserver {
    }

    private final ObserverSet<ExampleObserver> observerSet = new ObserverSet<>();
    private final ExampleObserver exampleObserverA = new ExampleObserver() {
    };
    private final ExampleObserver exampleObserverB = new ExampleObserver() {
    };

    @Test
    public void testAddObserver() throws Exception {
        observerSet.addObserver(exampleObserverA);
    }

    @Test
    public void testAddTwoObserver() throws Exception {
        observerSet.addObserver(exampleObserverA);
        observerSet.addObserver(exampleObserverB);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddSameObserverTwice() throws Exception {
        observerSet.addObserver(exampleObserverA);
        observerSet.addObserver(exampleObserverA);
    }

    @Test
    public void testRemoveObserver() throws Exception {
        observerSet.addObserver(exampleObserverA);
        observerSet.removeObserver(exampleObserverA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveObserverTwice() throws Exception {
        observerSet.addObserver(exampleObserverA);
        observerSet.removeObserver(exampleObserverA);
        observerSet.removeObserver(exampleObserverA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveObserverWithoutAdding() throws Exception {
        observerSet.removeObserver(exampleObserverA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveObserverThatIsDifferentThanOneAdded() throws Exception {
        observerSet.addObserver(exampleObserverA);
        observerSet.removeObserver(exampleObserverB);
    }


    @Test
    public void testIteratorZeroObservers() throws Exception {
        assertEquals(countObservers(observerSet), 0);
    }

    public int countObservers(ObserverSet set) {
        int observerCount = 0;
        for (Object b : set) {
            observerCount++;
        }
        return observerCount;
    }

    @Test
    public void testIteratorOneObserver() throws Exception {
        observerSet.addObserver(exampleObserverA);
        assertEquals(1, countObservers(observerSet));
    }

    @Test
    public void testIteratorTwoObservers() throws Exception {
        observerSet.addObserver(exampleObserverA);
        observerSet.addObserver(exampleObserverB);
        assertEquals(2, countObservers(observerSet));
    }

    @Test
    public void testIteratorOneObserverAfterRemovingAnother() throws Exception {
        observerSet.addObserver(exampleObserverA);
        observerSet.addObserver(exampleObserverB);
        observerSet.removeObserver(exampleObserverB);
        assertEquals(1, countObservers(observerSet));
    }

    @Test
    public void testIterator() throws Exception {
        observerSet.addObserver(exampleObserverA);
        for (Object b : observerSet) {
            assertSame(exampleObserverA, b);
        }
    }

    @Test
    public void testThreadSafeIterator() throws Exception {
        observerSet.addObserver(exampleObserverA);
        Iterator<ExampleObserver> iterator = observerSet.getThreadSafeIterator();
        while (iterator.hasNext()) {
            assertSame(exampleObserverA, iterator.next());
        }
    }
}
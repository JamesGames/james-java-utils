package org.jamesgames.jamesjavautils.general;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObserverSetTest {

    private static interface ExampleObserver {
        public void someEvent();
    }

    private static class ExampleObserverImp implements ExampleObserver {
        public int timesInformed;

        @Override
        public void someEvent() {
            timesInformed++;
        }
    }

    private final ObserverSet<ExampleObserver> observerSet = new ObserverSet<>();
    private final ExampleObserverImp exampleObserverA = new ExampleObserverImp() {
    };
    private final ExampleObserverImp exampleObserverB = new ExampleObserverImp() {
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
    public void testInformObservers() throws Exception {
        observerSet.addObserver(exampleObserverA);
        observerSet.addObserver(exampleObserverB);
        observerSet.informObservers(observer -> observer.someEvent());
        assertEquals(1, exampleObserverA.timesInformed);
        assertEquals(1, exampleObserverB.timesInformed);
        observerSet.removeObserver(exampleObserverA);
        observerSet.informObservers(observer -> observer.someEvent());
        assertEquals(1, exampleObserverA.timesInformed);
        assertEquals(2, exampleObserverB.timesInformed);
        observerSet.removeObserver(exampleObserverB);
        observerSet.informObservers(observer -> observer.someEvent());
        assertEquals(1, exampleObserverA.timesInformed);
        assertEquals(2, exampleObserverB.timesInformed);
    }
}
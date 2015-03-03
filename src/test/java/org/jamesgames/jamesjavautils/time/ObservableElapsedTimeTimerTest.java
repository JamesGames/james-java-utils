package org.jamesgames.jamesjavautils.time;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObservableElapsedTimeTimerTest {

    private static final int oneThousandMilliseconds = 1000;
    private static final int fiveHundredMilliseconds = 500;
    private static final long nanosecondsInMillisecond = 1_000_000;
    private static final long oneThousandMillisecondsInNanoSeconds = oneThousandMilliseconds * nanosecondsInMillisecond;

    private static class CountingObserver implements ElapsedTimeTimerObserver {
        public int timesInformed;

        @Override
        public void targetTimePassed(ObservableElapsedTimeTimer timer) {
            timesInformed++;
        }
    }

    private static class CounterThatAlsoResetTo500MillisecondsObserver extends CountingObserver {
        @Override
        public void targetTimePassed(ObservableElapsedTimeTimer timer) {
            super.targetTimePassed(timer);
            timer.setTargetTimeInMilliseconds(fiveHundredMilliseconds);
        }
    }

    private final ObservableElapsedTimeTimer timerWithCountingObserver =
            new ObservableElapsedTimeTimer(oneThousandMillisecondsInNanoSeconds);
    private final ObservableElapsedTimeTimer timerWithResetObserver =
            new ObservableElapsedTimeTimer(oneThousandMillisecondsInNanoSeconds);
    private final CountingObserver countingObserver = new CountingObserver();
    private final CounterThatAlsoResetTo500MillisecondsObserver resetTo500MsObserver =
            new CounterThatAlsoResetTo500MillisecondsObserver();

    @Before
    public void setUp() {
        timerWithCountingObserver.addElapsedTimeTimerObserver(countingObserver);
        timerWithResetObserver.addElapsedTimeTimerObserver(resetTo500MsObserver);
    }

    @Test
    public void testSetTargetTimeInMilliseconds() throws Exception {
        timerWithCountingObserver.setTargetTimeInMilliseconds(10);
        timerWithCountingObserver.addElapsedTimeInMilliseconds(oneThousandMilliseconds);
        // Tests the auto reset functionality as well
        assertEquals(100, countingObserver.timesInformed);

        timerWithResetObserver.setTargetTimeInMilliseconds(10);
        timerWithResetObserver.addElapsedTimeInMilliseconds(10);
        assertEquals(1, resetTo500MsObserver.timesInformed);
        // Tests if an observer's event can modify target time on timer without it being reset by automatic resetting
        timerWithResetObserver.addElapsedTimeInMilliseconds(499);
        assertEquals(1, resetTo500MsObserver.timesInformed);
        timerWithResetObserver.addElapsedTimeInMilliseconds(1);
        assertEquals(2, resetTo500MsObserver.timesInformed);
    }

    @Test
    public void testSetTargetTimeInNanoSeconds() throws Exception {
        timerWithCountingObserver.setTargetTimeInNanoSeconds(10 * nanosecondsInMillisecond);
        timerWithCountingObserver.addElapsedTimeInNanoSeconds(oneThousandMilliseconds * nanosecondsInMillisecond);
        // Tests the auto reset functionality as well
        assertEquals(100, countingObserver.timesInformed);

        timerWithResetObserver.setTargetTimeInNanoSeconds(1000 * nanosecondsInMillisecond);
        timerWithResetObserver.addElapsedTimeInNanoSeconds(1000 * nanosecondsInMillisecond);
        assertEquals(1, resetTo500MsObserver.timesInformed);
        // Tests if an observer's event can modify target time on timer without it being reset by automatic resetting
        timerWithResetObserver.addElapsedTimeInNanoSeconds(499 * nanosecondsInMillisecond);
        assertEquals(1, resetTo500MsObserver.timesInformed);
        timerWithResetObserver.addElapsedTimeInNanoSeconds(1 * nanosecondsInMillisecond);
        assertEquals(2, resetTo500MsObserver.timesInformed);
    }

    @Test
    public void testRemovingObserver() throws Exception {
        timerWithCountingObserver.removeElapsedTimeTimerObserver(countingObserver);
        timerWithCountingObserver.addElapsedTimeInMilliseconds(oneThousandMilliseconds);
        assertEquals(0, countingObserver.timesInformed);
    }

    @Test
    public void testSetTargetTimeToRandomTimeSpecifiedInNanoSeconds() throws Exception {
        timerWithCountingObserver.setTargetTimeToRandomTimeSpecifiedInNanoSeconds(2500 * nanosecondsInMillisecond,
                3000 * nanosecondsInMillisecond);
        timerWithCountingObserver.addElapsedTimeInNanoSeconds(2499 * nanosecondsInMillisecond);
        assertEquals(0, countingObserver.timesInformed);
        timerWithCountingObserver.addElapsedTimeInNanoSeconds(501 * nanosecondsInMillisecond);
        assertEquals(1, countingObserver.timesInformed);
    }

    @Test
    public void testResetTargetTimeToRandomTimeSpecifiedInMilliseconds() throws Exception {
        timerWithCountingObserver.resetTargetTimeToRandomTimeSpecifiedInMilliseconds(2500,
                3000);
        timerWithCountingObserver.addElapsedTimeInMilliseconds(2499);
        assertEquals(0, countingObserver.timesInformed);
        timerWithCountingObserver.addElapsedTimeInMilliseconds(501);
        assertEquals(1, countingObserver.timesInformed);
    }

    @Test
    public void testNewTimerWithRandomTargetTime() throws Exception {
        ObservableElapsedTimeTimer randomTargetTimer = new ObservableElapsedTimeTimer(2500 * nanosecondsInMillisecond,
                3000 * nanosecondsInMillisecond);
        randomTargetTimer.addElapsedTimeTimerObserver(countingObserver);
        randomTargetTimer.addElapsedTimeInMilliseconds(2499);
        assertEquals(0, countingObserver.timesInformed);
        randomTargetTimer.addElapsedTimeInMilliseconds(501);
        assertEquals(1, countingObserver.timesInformed);
    }
}
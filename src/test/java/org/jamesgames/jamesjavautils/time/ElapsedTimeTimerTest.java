package org.jamesgames.jamesjavautils.time;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class ElapsedTimeTimerTest {

    private final int oneThousandMilliseconds = 1000;
    private final int nanosecondsInMillisecond = 1_000_000;
    private final int oneThousandMillisecondsInNanoSeconds = oneThousandMilliseconds * nanosecondsInMillisecond;
    private final int oneThousandMinusOneMilliseconds = oneThousandMilliseconds - 1;
    private final int oneThousandPlusOneMilliseconds = oneThousandMilliseconds + 1;
    private final ElapsedTimeTimer thousandMillisecondsTimer =
            new ElapsedTimeTimer(oneThousandMillisecondsInNanoSeconds);

    @Test
    public void testCounterWithNoTimePassed() throws Exception {
        assertEquals(false, thousandMillisecondsTimer.isTimerFinished());
    }

    @Test
    public void testCounterWithNoTimePassedWithRandomTimeTargetConstructor() throws Exception {
        ElapsedTimeTimer randomTimerFrom100To199Milliseconds =
                new ElapsedTimeTimer(100 * nanosecondsInMillisecond, 200 * nanosecondsInMillisecond);
        assertEquals(false, randomTimerFrom100To199Milliseconds.isTimerFinished());
        randomTimerFrom100To199Milliseconds.addElapsedTimeInMilliseconds(99);
        assertEquals(false, randomTimerFrom100To199Milliseconds.isTimerFinished());
        randomTimerFrom100To199Milliseconds.addElapsedTimeInMilliseconds(100);
        assertEquals(true, randomTimerFrom100To199Milliseconds.isTimerFinished());
    }

    @Test
    public void testCounterWithSetTimeTargetThenWithRandomTimeTarget() throws Exception {
        ElapsedTimeTimer timer = new ElapsedTimeTimer(100 * nanosecondsInMillisecond);
        timer.resetTargetTimeToRandomTimeSpecifiedInMilliseconds(150, 200);
        timer.addElapsedTimeInMilliseconds(149);
        assertEquals(false, timer.isTimerFinished());
        timer.addElapsedTimeInMilliseconds(50);
        assertEquals(true, timer.isTimerFinished());
    }

    @Test
    public void testCounterWithSetTimeTargetThenWithRandomTimeTargetInNanoSeconds() throws Exception {
        ElapsedTimeTimer timer = new ElapsedTimeTimer(100 * nanosecondsInMillisecond);
        timer.resetTargetTimeToRandomTimeSpecifiedInNanoSeconds(150 * nanosecondsInMillisecond,
                200 * nanosecondsInMillisecond);
        timer.addElapsedTimeInNanoSeconds(149 * nanosecondsInMillisecond);
        assertEquals(false, timer.isTimerFinished());
        timer.addElapsedTimeInNanoSeconds(50 * nanosecondsInMillisecond);
        assertEquals(true, timer.isTimerFinished());
    }

    @Test
    public void testCounterWithOneMillisecondBeforeTarget() throws Exception {
        thousandMillisecondsTimer.addElapsedTimeInMilliseconds(oneThousandMinusOneMilliseconds);
        assertEquals(false, thousandMillisecondsTimer.isTimerFinished());
    }

    @Test
    public void testCounterWithTargetTimePassed() throws Exception {
        thousandMillisecondsTimer.addElapsedTimeInMilliseconds(oneThousandMilliseconds);
        assertEquals(true, thousandMillisecondsTimer.isTimerFinished());
    }

    @Test
    public void testCounterWithTargetTimePassedButWithTwoAdds() throws Exception {
        ElapsedTimeTimer timer = new ElapsedTimeTimer(oneThousandMilliseconds);
        timer.addElapsedTimeInMilliseconds(600);
        timer.addElapsedTimeInMilliseconds(400);
        assertEquals(true, timer.isTimerFinished());
    }

    @Test
    public void testCounterWithMoreThanTargetTimePassed() throws Exception {
        thousandMillisecondsTimer.addElapsedTimeInMilliseconds(oneThousandPlusOneMilliseconds);
        assertEquals(true, thousandMillisecondsTimer.isTimerFinished());
    }

    @Test
    public void testCounterWithTargetTimePassedButReset() throws Exception {
        thousandMillisecondsTimer.addElapsedTimeInMilliseconds(oneThousandMilliseconds);
        thousandMillisecondsTimer.resetElapsedTime();
        assertEquals(false, thousandMillisecondsTimer.isTimerFinished());
    }

    @Test
    public void testCounterWithTargetTimePassedButResetThenAlmostEnoughTimeForAnotherTarget() throws Exception {
        thousandMillisecondsTimer.addElapsedTimeInMilliseconds(oneThousandMilliseconds);
        thousandMillisecondsTimer.resetElapsedTime();
        thousandMillisecondsTimer.addElapsedTimeInMilliseconds(oneThousandMinusOneMilliseconds);
        assertEquals(false, thousandMillisecondsTimer.isTimerFinished());
    }

    @Test
    public void testCounterWithTimePassedThenResetThenTimePassed() throws Exception {
        thousandMillisecondsTimer.addElapsedTimeInMilliseconds(oneThousandMilliseconds);
        thousandMillisecondsTimer.resetElapsedTime();
        thousandMillisecondsTimer.addElapsedTimeInMilliseconds(oneThousandMilliseconds);
        assertEquals(true, thousandMillisecondsTimer.isTimerFinished());
    }

    @Test
    @Parameters(method = "resetWithNewTargetTimeTestValues")
    public void testCounterWithTimePassedThenResetThenSetWithNewTargetThenTimePassed(long originalTargetTimeMs,
            long newTargetTimeMs, long timePassedAfterNewTargetMs, boolean expectedFinishedOnNewTarget)
            throws Exception {
        ElapsedTimeTimer timer = new ElapsedTimeTimer(originalTargetTimeMs * nanosecondsInMillisecond);
        timer.addElapsedTimeInMilliseconds(originalTargetTimeMs);
        timer.resetElapsedTime();
        timer.setTargetTimeInMilliseconds(newTargetTimeMs);
        timer.addElapsedTimeInMilliseconds(timePassedAfterNewTargetMs);
        assertEquals(expectedFinishedOnNewTarget, timer.isTimerFinished());
    }

    @Test
    @Parameters(method = "resetWithNewTargetTimeTestValues")
    public void testCounterWithTimePassedThenResetThenSetWithNewTargetThenTimePassedInNanoSeconds(
            long originalTargetTimeMs,
            long newTargetTimeMs, long timePassedAfterNewTargetMs, boolean expectedFinishedOnNewTarget)
            throws Exception {
        ElapsedTimeTimer timer = new ElapsedTimeTimer(originalTargetTimeMs * nanosecondsInMillisecond);
        timer.addElapsedTimeInNanoSeconds(originalTargetTimeMs * nanosecondsInMillisecond);
        timer.resetElapsedTime();
        timer.setTargetTimeInNanoSeconds(newTargetTimeMs * nanosecondsInMillisecond);
        timer.addElapsedTimeInNanoSeconds(timePassedAfterNewTargetMs * nanosecondsInMillisecond);
        assertEquals(expectedFinishedOnNewTarget, timer.isTimerFinished());
    }

    private Object[] resetWithNewTargetTimeTestValues() {
        return $(
                $(1000, 500, 500, true), // shorter target hit
                $(1000, 500, 499, false), // shorter target not hit
                $(1000, 1500, 1500, true), // longer target hit
                $(1000, 1500, 1499, false) // longer target not hit
        );
    }

    @Test
    public void testCounterWithTimeResetPastTarget() {
        thousandMillisecondsTimer.addElapsedTimeInMilliseconds(1999);
        thousandMillisecondsTimer.resetElapsedTimeToTimePastCurrentTarget();
        assertEquals(false, thousandMillisecondsTimer.isTimerFinished());
        thousandMillisecondsTimer.addElapsedTimeInMilliseconds(1);
        assertEquals(true, thousandMillisecondsTimer.isTimerFinished());
    }
}
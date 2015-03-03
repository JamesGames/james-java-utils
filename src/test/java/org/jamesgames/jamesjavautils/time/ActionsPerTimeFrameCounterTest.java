package org.jamesgames.jamesjavautils.time;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActionsPerTimeFrameCounterTest {

    private final int oneThousandMilliseconds = 1000;
    private final int oneThousandMillisecondsInNanoSeconds = oneThousandMilliseconds * 1_000_000;
    private final int oneThousandMinusOneMilliseconds = oneThousandMilliseconds - 1;
    private final int oneThousandPlusOneMilliseconds = oneThousandMilliseconds + 1;
    private final int noActions = 0;
    private final int oneAction = 1;
    private final int oneThousandActions = 1000;
    private final ActionsPerTimeFrameCounter counterWith1000MillisecondTimeFrame =
            new ActionsPerTimeFrameCounter(oneThousandMillisecondsInNanoSeconds);
    private final float actionCountEpsilon = 0.001f;

    @Test
    public void testCounterWithNoTimePassed() throws Exception {
        assertEquals(noActions, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
    }

    @Test
    public void testCounterWithAlmostFullTimeFramePassed() throws Exception {
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(oneAction, oneThousandMinusOneMilliseconds);
        assertEquals(noActions, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
    }

    @Test
    public void testCounterWithFullTimeFramePassed() throws Exception {
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(oneAction, oneThousandMilliseconds);
        assertEquals(noActions, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
    }

    @Test
    public void testCounterWithJustAfterAFullTimeFramePassed() throws Exception {
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(oneAction, oneThousandPlusOneMilliseconds);
        assertEquals((oneAction / (oneThousandPlusOneMilliseconds * 1.0)) * oneThousandMilliseconds,
                counterWith1000MillisecondTimeFrame
                        .getActionCountPerTimeFrame(), actionCountEpsilon);
    }

    @Test
    public void testCounterWithOneFullTimeFramePassedAndAlmostAnotherFull() throws Exception {
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(oneAction, oneThousandMilliseconds);
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(oneThousandActions,
                oneThousandMinusOneMilliseconds);
        assertEquals(oneAction, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
    }

    @Test
    public void testCounterWithTwoFullTimeFramesPassed() throws Exception {
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(oneAction, oneThousandMilliseconds);
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(oneThousandActions, oneThousandMilliseconds);
        assertEquals(oneAction, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(0, 1);
        assertEquals(oneThousandActions, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(),
                actionCountEpsilon);
    }

    @Test
    public void testCounterWithActionsSplitOverTimeFrame() throws Exception {
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(noActions, 600);
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(oneThousandActions, 500);
        assertEquals(800, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(noActions, 900);
        assertEquals(800, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(noActions, 1);
        assertEquals(200, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
    }

    @Test
    public void testCounterWithActionsAddedOverMultipleTimeFrames() throws Exception {
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(100, 5500);
        assertEquals(18.1818, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(200, 5500);
        assertEquals(36.3636, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(100, 750);
        // Still 40 because haven't passed a full frame yet
        assertEquals(36.3636, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(100, 5000);
        assertEquals(20, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(0, 250);
        assertEquals(20, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
        counterWith1000MillisecondTimeFrame.addActionsInMilliseconds(0, 1);
        assertEquals(15, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
    }

    @Test
    public void testCounterWithActionsSplitOverTimeFrameUsingNanoSeconds() throws Exception {
        counterWith1000MillisecondTimeFrame.addActionsInNanoseconds(noActions, 600 * 1_000_000);
        counterWith1000MillisecondTimeFrame.addActionsInNanoseconds(oneThousandActions, 500 * 1_000_000);
        assertEquals(800, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
        counterWith1000MillisecondTimeFrame.addActionsInNanoseconds(noActions, 900 * 1_000_000);
        assertEquals(800, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
        counterWith1000MillisecondTimeFrame.addActionsInNanoseconds(noActions, 1 * 1_000_000);
        assertEquals(200, counterWith1000MillisecondTimeFrame.getActionCountPerTimeFrame(), actionCountEpsilon);
    }

    @Test
    public void testCounterWithoutSpecifyingTimeElapsed() throws Exception {
        float generousEpsilonForSleepTests = 200.0f;
        ActionsPerTimeFrameCounter counter = new ActionsPerTimeFrameCounter(oneThousandMillisecondsInNanoSeconds);
        counter.addActions(0); // Timer doesn't start counting until first action has been added
        Thread.sleep(800);
        counter.addActions(1000);
        assertEquals(0, counter.getActionCountPerTimeFrame(), actionCountEpsilon);
        Thread.sleep(500); // Plenty of time for 1000 actions to come visible
        counter.addActions(0); // Timer won't update it's position of what time frame it's in without an action added
        assertEquals(1000, counter.getActionCountPerTimeFrame(), generousEpsilonForSleepTests);
        Thread.sleep(1100); // Enough time for another frame to come and go and have no actions visible
        counter.addActions(0); // Timer won't update it's position of what time frame it's in without an action added
        assertEquals(0, counter.getActionCountPerTimeFrame(), generousEpsilonForSleepTests);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNegativeAmountOfTime() {
        counterWith1000MillisecondTimeFrame.addActionsInNanoseconds(100, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNegativeAmountOfActionsWithTime() {
        counterWith1000MillisecondTimeFrame.addActionsInNanoseconds(-1, 100);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNegativeAmountOfActions() {
        counterWith1000MillisecondTimeFrame.addActions(-1);
    }
}
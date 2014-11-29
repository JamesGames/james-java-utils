package org.jamesgames.jamesjavautils.time;

import net.jcip.annotations.NotThreadSafe;

/**
 * ActionsPerTimeFrameCounter counts how many actions occur in a certain amount of time and publishes a value of how
 * many times the action happened in the past fully counted time frame. ActionsPerTimeFrameCounter does not know of any
 * actions itself or observes any, the user of ActionsPerTimeFrameCounter has to make a method call when one or many
 * happen. ActionsPerTimeFrameCounter does not publish how many times an action occurred in the last x amount of time
 * where x is the supplied time frame value, but publishes many times actions occurred in the full past time frame, so
 * if the time frame was 1000 milliseconds and 5 actions happened, then the first 1000 milliseconds of usage
 * ActionsPerTimeFrameCounter would publish a value of 0, the next 1000 milliseconds it would publish 5, and if 7 other
 * actions happened during the second 1000 milliseconds then the ActionsPerTimeFrameCounter would still publish 7 during
 * the next (third set of) 1000 milliseconds. The output of the 5 and 7 is assuming that no actions happened on a time
 * frame border, where one passed how many actions happened and how much time elapsed, and the time elapsed was split
 * between one time frame and another. If such a split happened, the number of actions would be split fractionally
 * depending on how much of the elapsed time is going to one time frame to another. An example of using this class would
 * be calculating frames per second in a graphical application..
 *
 * @author James Murphy
 */
@NotThreadSafe
public class ActionsPerTimeFrameCounter {

    private static final int numberOfNanoSecondsInMillisecond = 1_000_000;

    /**
     * Holds a value to signal that the application has not began counting actions per second yet
     */
    private final static long notKeepingTrackOfTimeYet = -1;

    /**
     * Time frame in nanoseconds
     */
    private final long timeFrameInNanoSeconds;

    /**
     * Holds the amount of computed actions in the currently timed time frame
     */
    private float actionCountInCurrentTimeFrame;

    /**
     * Holds the last computed actions per time frame value, this is a floating point value because if the number of
     * actions is updated over a time frame boundary, then a weighted split of the number of actions is added for each
     * time frame.
     */
    private float actionCountInLastTimeFrame = 0;

    /**
     * Holds the time that the next actions per second value counting started
     */
    private long elapsedTimeFrameTimeInNanoSeconds =
            notKeepingTrackOfTimeYet;

    private long systemNanoTimeAtLastActionUpdate = notKeepingTrackOfTimeYet;

    /**
     * Creates a ActionsPerTimeFrameCounter with specified time frame on how long to calculate each new actions per time
     * frame value
     */
    public ActionsPerTimeFrameCounter(long timeFrameInNanoSeconds) {
        this.timeFrameInNanoSeconds = timeFrameInNanoSeconds;
    }

    /**
     * Adds a number of actions to the current time frame (possibly some of the actions will be added to next time frame
     * if time frame ends here)
     *
     * @param numberOfActions
     *         Number of actions done
     * @param timeElapsedInMilliseconds
     *         Amount of time that has elapsed since the last addActions call
     */
    public void addActionsInMilliseconds(int numberOfActions, long timeElapsedInMilliseconds) {
        addActionsInNanoseconds(numberOfActions, millisecondsToNanoSeconds(timeElapsedInMilliseconds));
    }

    /**
     * Adds a number of actions to the current time frame (possibly some of the actions will be added to next time frame
     * if time frame ends here), uses the current system time of System.NanoTime to calculate elapsed time.
     *
     * @param numberOfActions
     *         Number of actions done
     */
    public void addActions(int numberOfActions) {
        if (numberOfActions < 0)
            throw new IllegalArgumentException(
                    "Number of actions has to be non negative (you passed " + numberOfActions + ")");

        if (elapsedTimeFrameTimeInNanoSeconds == notKeepingTrackOfTimeYet) {
            setInitialStartTimeForActionCounting();
        }
        // Obtaining the max of 0 and System time minus a recently calculated system time because System.nanoTime
        // may have returned an earlier time on the second call.
        // Some of the value from System.nanoTime is approximated, so I am unsure if the later time could possibly
        // have an earlier approximation portion than another recent System.nanoTime call.
        // In addition, it has been known that sometimes System.nanoTime on different JVM implementations returns a time
        // specific to a CPU, where one CPU may be a time less than another, which can cause a negative value from
        // the subtraction from the buggy implementation. This has been fixed in Oracle JVMs from what I can see though.
        addActionsInNanoseconds(numberOfActions, Math.max(0, System.nanoTime() - systemNanoTimeAtLastActionUpdate));
    }

    private void setInitialStartTimeForActionCounting() {
        elapsedTimeFrameTimeInNanoSeconds = 0;
        systemNanoTimeAtLastActionUpdate = System.nanoTime();
    }

    private static long millisecondsToNanoSeconds(long milliseconds) {
        return milliseconds * numberOfNanoSecondsInMillisecond;
    }

    /**
     * Adds a number of actions to the current time frame (possibly some of the actions will be added to next time frame
     * if time frame ends here)
     *
     * @param numberOfActions
     *         Number of actions done
     * @param timeElapsedInNanoSeconds
     *         Amount of time that has elapsed since the last addActions call
     */
    public void addActionsInNanoseconds(int numberOfActions, long timeElapsedInNanoSeconds) {
        if (timeElapsedInNanoSeconds < 0) {
            throw new IllegalArgumentException(
                    "Time elapsed has to be non negative (you passed " + timeElapsedInNanoSeconds + ")");
        }

        if (elapsedTimeFrameTimeInNanoSeconds == notKeepingTrackOfTimeYet) {
            setInitialStartTimeForActionCounting();
        }

        long potentialNewElapsedTimeFrameTime = elapsedTimeFrameTimeInNanoSeconds + timeElapsedInNanoSeconds;
        if (potentialNewElapsedTimeFrameTime > timeFrameInNanoSeconds) {
            // Need to split the action count and elapsed time over two or more time frames
            updateActionCountAndTimeOnNewTimeFrame(numberOfActions, timeElapsedInNanoSeconds,
                    potentialNewElapsedTimeFrameTime);
        } else {
            // Can add all actions and elapsed time to current time frame
            updateActionCountAndTime(numberOfActions, potentialNewElapsedTimeFrameTime);
        }

        // Update system time of last action update with just how much time has elapsed instead of re-requesting it
        systemNanoTimeAtLastActionUpdate += timeElapsedInNanoSeconds;
    }

    /**
     * Wait wait wait, this method could probably be much simpler... should probably be rewritten
     */
    private void updateActionCountAndTimeOnNewTimeFrame(float numberOfActions, long timeElapsedInNanoSeconds,
            long exceededElapsedTimeFrameTime) {
        // Basically, if the time that was added overlaps more than 2 time frames, edit how much time has passed
        // by repositioning this counter to start on a new frame with 0 nanoseconds passed and with an adjusted
        // amount of actions
        if (exceededElapsedTimeFrameTime > timeFrameInNanoSeconds * 2) {
            long originalExceedTime = timeElapsedInNanoSeconds;
            // Time elapsed crosses three or more time borders
            // Remove time for current time frame (before first border crossing)
            exceededElapsedTimeFrameTime =
                    timeElapsedInNanoSeconds - (timeFrameInNanoSeconds - elapsedTimeFrameTimeInNanoSeconds);
            // Next, remove time for any time frames before the second to last
            exceededElapsedTimeFrameTime -=
                    ((exceededElapsedTimeFrameTime / timeFrameInNanoSeconds) - 1) * timeFrameInNanoSeconds;
            // Since we are starting the processing of the number of actions from the beginning of a new frame
            // for this double overlap situation that's being handled here, then change how much time has elapsed
            timeElapsedInNanoSeconds = exceededElapsedTimeFrameTime;
            // There should now be timeFrameInNanoSeconds + some amount of elapsed time
            // But since we skipped past the oldest time frames, all values must be reset
            actionCountInCurrentTimeFrame = 0;
            elapsedTimeFrameTimeInNanoSeconds = 0;
            // We also have to take a fraction of the number of actions to the time frame that was skipped entirely
            actionCountInLastTimeFrame = numberOfActions * (timeFrameInNanoSeconds / (float) originalExceedTime);
            // And also have to change number of actions to reflect the amount for the current time frame
            numberOfActions = numberOfActions * (exceededElapsedTimeFrameTime / (float) originalExceedTime);
        }

        long amountOfTimeAllottedToOldTimeFrame = timeFrameInNanoSeconds - elapsedTimeFrameTimeInNanoSeconds;
        long amountOfTimeAllottedToNewTimeFrame = exceededElapsedTimeFrameTime - timeFrameInNanoSeconds;
        float actionsForOldTimeFrame =
                (amountOfTimeAllottedToOldTimeFrame / (float) timeElapsedInNanoSeconds) * numberOfActions;
        float actionsForNewTimeFrame = numberOfActions - actionsForOldTimeFrame;

        // Update how many actions were in current time frame
        actionCountInCurrentTimeFrame += actionsForOldTimeFrame;
        // We passed over a time boundary so also change the published value of actions for the last time frame
        updateActionCountForLastTimeFrameAndResetCurrentCount();
        // Update how many actions in the new current time frame
        actionCountInCurrentTimeFrame = actionsForNewTimeFrame;
        // In addition, also change elapsed time in current frame to represent the split of time on the
        // time frame boundaries
        elapsedTimeFrameTimeInNanoSeconds = amountOfTimeAllottedToNewTimeFrame;
    }

    private void updateActionCountAndTime(int numberOfActions, long potentialNewElapsedTimeFrameTime) {
        elapsedTimeFrameTimeInNanoSeconds = potentialNewElapsedTimeFrameTime;
        actionCountInCurrentTimeFrame += numberOfActions;
    }

    private void updateActionCountForLastTimeFrameAndResetCurrentCount() {
        actionCountInLastTimeFrame = actionCountInCurrentTimeFrame;
        actionCountInCurrentTimeFrame = 0;
    }

    public float getActionCountPerTimeFrame() {
        return actionCountInLastTimeFrame;
    }

}

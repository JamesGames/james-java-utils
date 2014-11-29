package org.jamesgames.jamesjavautils.time;

import net.jcip.annotations.NotThreadSafe;

import java.util.Random;

/**
 * ElapsedTimeTimer is used to time events based on a target time some amount of milliseconds or nanoseconds in the
 * future. The basic usage of the class is that the user sets a target time of some amount of time in the future, and
 * then the user adds time and a method that can return whether the target time was achieved or not will return true or
 * false. ElapsedTimeTimer does not run in it's own thread of any kind, and must be updated by some other class with
 * some amount of time that has passed. A possible improvement to ElapsedTimeTimer would be to create the ability for
 * observers to observe when the time has elapsed, and possibly other events (like when the timer was reset).
 *
 * @author James Murphy
 */
@NotThreadSafe
public class ElapsedTimeTimer {
    private static final int numberOfNanoSecondsInMillisecond = 1_000_000;
    private static final Random randomTargetTimeGenerator = new Random();

    private long totalElapsedTimeInNanoSeconds;
    private long targetTimeInNanoSeconds;
    private boolean timerFinished;


    /**
     * Creates a ElapsedTimeTimer with a specific target time.
     */
    public ElapsedTimeTimer(long targetTimeInNanoSeconds) {
        this.timerFinished = false;
        setTargetTimeInNanoSeconds(targetTimeInNanoSeconds);
    }

    /**
     * Creates a ElapsedTimeTimer with a target time randomly between two times..
     */
    public ElapsedTimeTimer(long inclusiveLowerBoundInNanoseconds,
            long exclusiveUpperBoundInNanoseconds) {
        resetTargetTimeToRandomTimeSpecifiedInNanoSeconds(inclusiveLowerBoundInNanoseconds,
                exclusiveUpperBoundInNanoseconds);
    }

    public void setTargetTimeInNanoSeconds(long targetTimeInNanoSeconds) {
        if (targetTimeInNanoSeconds < 0) {
            throw new IllegalArgumentException("time to reach cannot be less than zero");
        }
        this.targetTimeInNanoSeconds = targetTimeInNanoSeconds;
    }

    public void setTargetTimeInMilliseconds(long targetTimeInMilliseconds) {
        setTargetTimeInNanoSeconds(millisecondsToNanoSeconds(targetTimeInMilliseconds));
    }

    private static long millisecondsToNanoSeconds(long milliseconds) {
        return milliseconds * numberOfNanoSecondsInMillisecond;
    }

    public void resetTargetTimeToRandomTimeSpecifiedInNanoSeconds(long inclusiveLowerBound,
            long exclusiveUpperBound) {
        if (inclusiveLowerBound < 0) {
            throw new IllegalArgumentException("inclusive bound must be equal to zero or greater");
        } else if (exclusiveUpperBound < 1) {
            throw new IllegalArgumentException("exclusive bound must be equal to one or greater");
        } else if (exclusiveUpperBound <= inclusiveLowerBound) {
            throw new IllegalArgumentException("exclusive bound cannot be less than or equal to inclusive bound");
        }
        setTargetTimeInNanoSeconds(inclusiveLowerBound +
                ((long) (randomTargetTimeGenerator.nextDouble() * (exclusiveUpperBound - inclusiveLowerBound))));

    }

    public void resetTargetTimeToRandomTimeSpecifiedInMilliseconds(long inclusiveLowerBound,
            long exclusiveUpperBound) {
        resetTargetTimeToRandomTimeSpecifiedInNanoSeconds(millisecondsToNanoSeconds(inclusiveLowerBound),
                millisecondsToNanoSeconds(exclusiveUpperBound));
    }

    public void addElapsedTimeInNanoSeconds(long elapsedTimeInNanoSeconds) {
        if (elapsedTimeInNanoSeconds < 0) {
            throw new IllegalArgumentException("elapsed time cannot be less than zero");
        }

        totalElapsedTimeInNanoSeconds += elapsedTimeInNanoSeconds;

        timerFinished = totalElapsedTimeInNanoSeconds >= targetTimeInNanoSeconds;
    }

    public void addElapsedTimeInMilliseconds(long elapsedTimeInMilliseconds) {
        addElapsedTimeInNanoSeconds(millisecondsToNanoSeconds(elapsedTimeInMilliseconds));
    }

    private void resetElapsedTimeInNanoSeconds(long newElapsedTimeInNanoSeconds) {
        this.totalElapsedTimeInNanoSeconds = 0;
        addElapsedTimeInNanoSeconds(newElapsedTimeInNanoSeconds);
    }

    public void resetElapsedTime() {
        resetElapsedTimeInNanoSeconds(0);
    }

    public void resetElapsedTimeToTimePastCurrentTarget() {
        if (totalElapsedTimeInNanoSeconds < targetTimeInNanoSeconds) {
            throw new IllegalArgumentException("total time elapsed so far is not more than current target yet, " +
                    " total elapsed time: " + totalElapsedTimeInNanoSeconds + ", target time: " +
                    targetTimeInNanoSeconds);

        }
        resetElapsedTimeInNanoSeconds(totalElapsedTimeInNanoSeconds - targetTimeInNanoSeconds);
    }

    public boolean isTimerFinished() {
        return timerFinished;
    }

}

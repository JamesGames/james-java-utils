package org.jamesgames.jamesjavautils.time;

import org.jamesgames.jamesjavautils.general.ObserverSet;

/**
 * ObservableElapsedTimeTimer is much like an {@link org.jamesgames.jamesjavautils.time.ElapsedTimeTimer} where it is
 * used to time events based on a target time some amount of milliseconds or nanoseconds in the future. It's slightly
 * different where when the target time has passed it notifies registered observers {@link
 * org.jamesgames.jamesjavautils.time.ElapsedTimeTimerObserver} and resets it's target time (instead of having the
 * client code that uses the timer to do both actions manually).
 *
 * @author James Murphy
 */
public class ObservableElapsedTimeTimer extends ElapsedTimeTimer {
    private static enum LastTargetTimeSetMethod {random, specified}

    private final ObserverSet<ElapsedTimeTimerObserver> observers = new ObserverSet<>();

    private LastTargetTimeSetMethod lastTargetTimeSetMethod;
    private long lastSpecifiedTargetTimeInNanoSeconds;
    private long lastInclusiveRandomTimeInNanoSeconds;
    private long lastExclusiveRandomTimeInNanoSeconds;

    /**
     * Creates a ObservableElapsedTimeTimer with a specific target time.
     */
    public ObservableElapsedTimeTimer(long targetTimeInNanoSeconds) {
        super(targetTimeInNanoSeconds);
        recordSpecifiedTargetTime(targetTimeInNanoSeconds);
    }

    private void recordSpecifiedTargetTime(long targetTimeInNanoSeconds) {
        lastTargetTimeSetMethod = LastTargetTimeSetMethod.specified;
        lastSpecifiedTargetTimeInNanoSeconds = targetTimeInNanoSeconds;
    }

    /**
     * Creates a ObservableElapsedTimeTimer with a target time randomly between two times.
     */
    public ObservableElapsedTimeTimer(long inclusiveLowerBoundInNanoseconds,
            long exclusiveUpperBoundInNanoseconds) {
        super(inclusiveLowerBoundInNanoseconds, exclusiveUpperBoundInNanoseconds);
        recordRandomTargetTime(inclusiveLowerBoundInNanoseconds, exclusiveUpperBoundInNanoseconds);
    }

    private void recordRandomTargetTime(long inclusiveLowerBoundInNanoseconds,
            long exclusiveUpperBoundInNanoseconds) {
        lastTargetTimeSetMethod = LastTargetTimeSetMethod.random;
        lastInclusiveRandomTimeInNanoSeconds = inclusiveLowerBoundInNanoseconds;
        lastExclusiveRandomTimeInNanoSeconds = exclusiveUpperBoundInNanoseconds;
    }

    public final void addElapsedTimeTimerObserver(ElapsedTimeTimerObserver observer) {
        observers.addObserver(observer);
    }

    public final void removeElapsedTimeTimerObserver(ElapsedTimeTimerObserver observer) {
        observers.removeObserver(observer);
    }

    @Override
    public final void setTargetTimeInMilliseconds(long targetTimeInMilliseconds) {
        super.setTargetTimeInMilliseconds(targetTimeInMilliseconds);
        recordSpecifiedTargetTime(millisecondsToNanoSeconds(targetTimeInMilliseconds));
    }

    @Override
    public final void setTargetTimeInNanoSeconds(long targetTimeInNanoSeconds) {
        super.setTargetTimeInNanoSeconds(targetTimeInNanoSeconds);
        recordSpecifiedTargetTime(targetTimeInNanoSeconds);
    }

    @Override
    public final void setTargetTimeToRandomTimeSpecifiedInNanoSeconds(long inclusiveLowerBound,
            long exclusiveUpperBound) {
        super.setTargetTimeToRandomTimeSpecifiedInNanoSeconds(inclusiveLowerBound, exclusiveUpperBound);
        recordRandomTargetTime(inclusiveLowerBound, exclusiveUpperBound);
    }

    @Override
    public final void resetTargetTimeToRandomTimeSpecifiedInMilliseconds(long inclusiveLowerBound,
            long exclusiveUpperBound) {
        super.resetTargetTimeToRandomTimeSpecifiedInMilliseconds(inclusiveLowerBound, exclusiveUpperBound);
        recordRandomTargetTime(millisecondsToNanoSeconds(inclusiveLowerBound),
                millisecondsToNanoSeconds(exclusiveUpperBound));
    }

    @Override
    protected final void onTimerFinished() {
        // Important that the timer is now set to unfinished before resetting the time elapsed, that way if we pass
        // the target time again during the elapsed time reset then the observers can pick up another target time event
        // occurring.
        setTimerUnfinished();
        resetElapsedTimeToTimePastCurrentTarget();
        // It's also important that the reset of target time is done before notifying observers of the event,
        // that way if the observer wants to change the target time to something else they can
        // without that change being undone by the automatic resetting here.
        // (Although, if multiple observers exist than of course it is possible for an earlier informed
        // observer's target time change to be ignored...)
        resetTimerToLastUsedTargetTimeSettings();
        observers.informObservers(observer -> observer.targetTimePassed(this));


    }

    private void resetTimerToLastUsedTargetTimeSettings() {
        switch (lastTargetTimeSetMethod) {
            case random:
                setTargetTimeToRandomTimeSpecifiedInNanoSeconds(lastInclusiveRandomTimeInNanoSeconds,
                        lastExclusiveRandomTimeInNanoSeconds);
                break;
            case specified:
                setTargetTimeInNanoSeconds(lastSpecifiedTargetTimeInNanoSeconds);
                break;
        }
    }
}

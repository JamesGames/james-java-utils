package org.jamesgames.jamesjavautils.time;

/**
 * ElapsedTimeTimerObserver is an interface that depicts an event when a {@link org.jamesgames.jamesjavautils.time.ObservableElapsedTimeTimer}
 * has reached a target time.
 *
 * @author James Murphy
 */
public interface ElapsedTimeTimerObserver {

    /**
     * To be called when the timer reaches its target timer. The timer before this event is called has set it's next
     * target time already from the last set settings for the target time. However if the target time needs to be
     * changed in anyway (such as from random to a static time) then the observer can do so with the passed reference of
     * the Timer.
     *
     * @param timer
     *         Reference to the ElapsedTimeTimerObserver that the event originated from.
     */
    public void targetTimePassed(ObservableElapsedTimeTimer timer);
}

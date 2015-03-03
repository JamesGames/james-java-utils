package org.jamesgames.jamesjavautils.general;

import net.jcip.annotations.ThreadSafe;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/**
 * ObserverSet is a class that helps implement an Observer pattern. The class holds a {@link java.util.Set} of some type
 * of object, which are assumed to be in this case a type that depicts events that one would want to observe (listen
 * to). ObserverSet is thread safe.
 *
 * @author James Murphy
 */
@ThreadSafe
public class ObserverSet<Observer> {

    private final Set<Observer> observers = new HashSet<>();

    public synchronized void addObserver(Observer observer) {
        boolean listenerNotYetAdded = observers.add(Objects.requireNonNull(observer, "observer cannot be null"));
        if (!listenerNotYetAdded) {
            throw new IllegalArgumentException("Observer is already observing");
        }
    }

    public synchronized void removeObserver(Observer observer) {
        boolean listenerExisted = observers.remove(Objects.requireNonNull(observer, "observer cannot be null"));

        if (!listenerExisted) {
            throw new IllegalArgumentException("Observer was not observing");
        }
    }

    public synchronized void informObservers(Consumer<Observer> informAction) {
        observers.forEach(informAction);
    }
}

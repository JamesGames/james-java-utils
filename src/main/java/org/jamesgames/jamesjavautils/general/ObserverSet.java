package org.jamesgames.jamesjavautils.general;

import net.jcip.annotations.NotThreadSafe;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * ObserverSet is a class that helps implement an Observer pattern. The class holds a {@link java.util.Set} of some type
 * of object, which are assumed to be in this case a type that depicts events that one would want to observe (listen).
 * ObserverSet also implements {@link java.lang.Iterable} which iterates through all observers in the set. ObserverSet
 * is not thread safe, but add and remove are. Method iterator() does nto return an iterator of a copy of the underlying
 * set of observers because it's assumed it's an action that will be usually done on one thread, by an object which owns
 * the ObserverSet more than any other object. Method getThreadSafeIterator() can be used for a thread safe iterator.
 *
 * @author James Murphy
 */
@NotThreadSafe
public class ObserverSet<Observer> implements Iterable<Observer> {

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

    @Override
    public Iterator<Observer> iterator() {
        return observers.iterator();
    }

    public synchronized Iterator<Observer> getThreadSafeIterator() {
        return new HashSet<>(observers).iterator();
    }
}

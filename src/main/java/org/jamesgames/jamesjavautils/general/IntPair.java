package org.jamesgames.jamesjavautils.general;

import net.jcip.annotations.Immutable;

/**
 * Represents a pair of integer values. Useful for 2d coordinate representation and for being returned from {@link
 * java.util.function.Function}s.
 *
 * @author James Murphy
 */
@Immutable
public class IntPair {
    private final int x;
    private final int y;

    public IntPair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

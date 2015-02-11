package org.jamesgames.jamesjavautils.general;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class IntPairTest {

    private final IntPair pair =
            new IntPair(5, 9);

    @Test
    public void testXRetrieval() throws Exception {
        assertEquals(5, pair.getX());
    }

    @Test
    public void testYRetrieval() throws Exception {
        assertEquals(9, pair.getY());
    }
}

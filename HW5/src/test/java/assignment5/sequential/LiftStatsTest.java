package assignment5.sequential;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LiftStatsTest {

    @Test
    public void testConstructorAndGetters() {
        LiftStats stats = new LiftStats(7);
        assertEquals(7, stats.getLiftId());
        assertEquals(0, stats.getTotalRides()); // initial value
    }

    @Test
    public void testIncrementRides() {
        LiftStats stats = new LiftStats(3);

        stats.incrementRides();
        stats.incrementRides();
        stats.incrementRides();

        assertEquals(3, stats.getTotalRides());
    }

    @Test
    public void testMultipleInstancesIndependent() {
        LiftStats s1 = new LiftStats(1);
        LiftStats s2 = new LiftStats(2);

        s1.incrementRides();
        s1.incrementRides();
        s2.incrementRides();

        assertEquals(2, s1.getTotalRides());
        assertEquals(1, s2.getTotalRides());

        assertEquals(1, s1.getLiftId());
        assertEquals(2, s2.getLiftId());
    }
}

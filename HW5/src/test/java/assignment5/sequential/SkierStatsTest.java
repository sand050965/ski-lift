package assignment5.sequential;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SkierStatsTest {

    @Test
    public void testConstructorAndGetters() {
        SkierStats stats = new SkierStats("Skier-123");

        assertEquals("Skier-123", stats.getSkierId());
        assertEquals(0, stats.getTotalRides());
        assertEquals(0, stats.getTotalVertical());
    }

    @Test
    public void testConstructorRejectsNull() {
        assertThrows(IllegalArgumentException.class, () -> new SkierStats(null));
    }

    @Test
    public void testConstructorRejectsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new SkierStats(""));
    }

    @Test
    public void testAddRide() {
        SkierStats stats = new SkierStats("A");

        stats.addRide(300);
        stats.addRide(200);

        assertEquals(2, stats.getTotalRides());
        assertEquals(500, stats.getTotalVertical());
    }

    @Test
    public void testAddRideRejectsNegativeVertical() {
        SkierStats stats = new SkierStats("A");
        assertThrows(IllegalArgumentException.class, () -> stats.addRide(-5));
    }

    @Test
    public void testIndependentInstances() {
        SkierStats s1 = new SkierStats("X");
        SkierStats s2 = new SkierStats("Y");

        s1.addRide(100);
        s1.addRide(50);

        s2.addRide(10);

        assertEquals(2, s1.getTotalRides());
        assertEquals(150, s1.getTotalVertical());

        assertEquals(1, s2.getTotalRides());
        assertEquals(10, s2.getTotalVertical());
    }
}
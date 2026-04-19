package assignment5.sequential;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


public class HourlyLiftStatsTest {

    @Test
    public void testInitialState() {
        HourlyLiftStats stats = new HourlyLiftStats(3);

        assertEquals(3, stats.getHourNumber());
        assertEquals(0, stats.getRidesPerLift().size());
    }

    @Test
    public void testAddRideForSingleLift() {
        HourlyLiftStats stats = new HourlyLiftStats(1);

        stats.addRideForLift(5);
        stats.addRideForLift(5);

        Map<Integer, Integer> rides = stats.getRidesPerLift();
        assertEquals(1, rides.size());
        assertEquals(Integer.valueOf(2), rides.get(5));
    }

    @Test
    public void testAddRideForMultipleLifts() {
        HourlyLiftStats stats = new HourlyLiftStats(2);

        stats.addRideForLift(1);
        stats.addRideForLift(2);
        stats.addRideForLift(1);

        Map<Integer, Integer> rides = stats.getRidesPerLift();
        assertEquals(2, rides.size());
        assertEquals(Integer.valueOf(2), rides.get(1));
        assertEquals(Integer.valueOf(1), rides.get(2));
    }

    @Test
    public void testGetRidesPerLiftReturnsCopy() {
        HourlyLiftStats stats = new HourlyLiftStats(4);
        stats.addRideForLift(10);

        Map<Integer, Integer> copy = stats.getRidesPerLift();
        copy.put(99, 100);

        Map<Integer, Integer> original = stats.getRidesPerLift();
        assertFalse(original.containsKey(99));
        assertEquals(1, original.size());
        assertEquals(Integer.valueOf(1), original.get(10));
    }
}

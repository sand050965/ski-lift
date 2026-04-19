package assignment5.sequential;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class LiftVerticalMappingTest {

    @Test
    public void testValidLiftIdsRange1To10() {
        for (int id = 1; id <= 10; id++) {
            assertEquals(200, LiftVerticalMapping.getVerticalForLift(id));
        }
    }

    @Test
    public void testValidLiftIdsRange11To20() {
        for (int id = 11; id <= 20; id++) {
            assertEquals(300, LiftVerticalMapping.getVerticalForLift(id));
        }
    }

    @Test
    public void testValidLiftIdsRange21To30() {
        for (int id = 21; id <= 30; id++) {
            assertEquals(400, LiftVerticalMapping.getVerticalForLift(id));
        }
    }

    @Test
    public void testValidLiftIdsRange31To40() {
        for (int id = 31; id <= 40; id++) {
            assertEquals(500, LiftVerticalMapping.getVerticalForLift(id));
        }
    }

    @Test
    public void testInvalidLiftIdTooLow() {
        assertThrows(IllegalArgumentException.class, () -> LiftVerticalMapping.getVerticalForLift(0));
    }

    @Test
    public void testInvalidLiftIdTooHigh() {
        assertThrows(IllegalArgumentException.class, () -> LiftVerticalMapping.getVerticalForLift(41));
    }
}

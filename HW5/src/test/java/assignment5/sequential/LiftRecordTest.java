package assignment5.sequential;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class LiftRecordTest {

    @Test
    public void testConstructorAndGetters() {
        LiftRecord record = new LiftRecord(
                "Resort-1",
                12,
                150,
                "Skier-100",
                7
        );

        assertEquals("Resort-1", record.getResortId());
        assertEquals(12, record.getDayNumber());
        assertEquals(150, record.getTimestamp());
        assertEquals("Skier-100", record.getSkierId());
        assertEquals(7, record.getLiftId());
    }

    @Test
    public void testDifferentValues() {
        LiftRecord record = new LiftRecord(
                "ABC",
                3,
                0,
                "X999",
                1
        );

        assertEquals("ABC", record.getResortId());
        assertEquals(3, record.getDayNumber());
        assertEquals(0, record.getTimestamp());
        assertEquals("X999", record.getSkierId());
        assertEquals(1, record.getLiftId());
    }
}

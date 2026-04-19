package assignment5.sequential;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class LiftRecordParserTest {

    @Test
    public void testValidLine() {
        String line = "Resort-1, 10, Skier-99, 5, 120";

        LiftRecord record = LiftRecordParser.parse(line);

        assertEquals("Resort-1", record.getResortId());
        assertEquals(10, record.getDayNumber());
        assertEquals("Skier-99", record.getSkierId());
        assertEquals(5, record.getLiftId());
        assertEquals(120, record.getTimestamp());
    }

    @Test
    public void testValidLineWithSpaces() {
        String line = "  ABC , 2 , X1 , 3 , 0  ";

        LiftRecord record = LiftRecordParser.parse(line);

        assertEquals("ABC", record.getResortId());
        assertEquals(2, record.getDayNumber());
        assertEquals("X1", record.getSkierId());
        assertEquals(3, record.getLiftId());
        assertEquals(0, record.getTimestamp());
    }

    @Test
    public void testNullLine() {
        assertThrows(IllegalArgumentException.class, () -> {
            LiftRecordParser.parse(null);
        });
    }

    @Test
    public void testEmptyLine() {
        assertThrows(IllegalArgumentException.class, () -> {
            LiftRecordParser.parse("   ");
        });
    }

    @Test
    public void testTooFewFields() {
        assertThrows(IllegalArgumentException.class, () -> {
            LiftRecordParser.parse("A,B,C");
        });
    }

    @Test
    public void testInvalidDayNumber() {
        assertThrows(NumberFormatException.class, () -> {
            LiftRecordParser.parse("Resort,x,Skier,3,100");
        });
    }

    @Test
    public void testInvalidLiftId() {
        assertThrows(NumberFormatException.class, () -> {
            LiftRecordParser.parse("Resort,3,Skier,y,100");
        });
    }

    @Test
    public void testInvalidTimestamp() {
        assertThrows(NumberFormatException.class, () -> {
            LiftRecordParser.parse("Resort,3,Skier,3,z");
        });
    }
}

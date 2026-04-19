package assignment5.concurrent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RecordsTest {

    @Test
    public void testSkierRecordFields() {
        Records.SkierRecord r = new Records.SkierRecord("101", 5);

        assertEquals("101", r.skierId());
        assertEquals(5, r.liftId());
    }

    @Test
    public void testSkierRecordPoison() {
        Records.SkierRecord poison = Records.SkierRecord.POISON;

        assertNull(poison.skierId());
        assertEquals(-1, poison.liftId());
    }

    @Test
    public void testLiftRecordFields() {
        Records.LiftRecord r = new Records.LiftRecord(7);

        assertEquals(7, r.liftId());
    }

    @Test
    public void testLiftRecordPoison() {
        Records.LiftRecord poison = Records.LiftRecord.POISON;

        assertEquals(-1, poison.liftId());
    }

    @Test
    public void testHourRecordFields() {
        Records.HourRecord r = new Records.HourRecord(3, 12);

        assertEquals(3, r.hour());
        assertEquals(12, r.liftId());
    }

    @Test
    public void testHourRecordPoison() {
        Records.HourRecord poison = Records.HourRecord.POISON;

        assertEquals(-1, poison.hour());
        assertEquals(-1, poison.liftId());
    }

    @Test
    public void testRecordEquality() {
        Records.SkierRecord a = new Records.SkierRecord("10", 4);
        Records.SkierRecord b = new Records.SkierRecord("10", 4);

        assertEquals(a, b);  // record auto-generates equals()
        assertEquals(a.hashCode(), b.hashCode());
    }
}
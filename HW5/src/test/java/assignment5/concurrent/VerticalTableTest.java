package assignment5.concurrent;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VerticalTableTest {

    @Test
    public void testVerticalForLifts1to10() {
        for (int i = 1; i <= 10; i++) {
            assertEquals(200, VerticalTable.getVertical(i));
        }
    }

    @Test
    public void testVerticalForLifts11to20() {
        for (int i = 11; i <= 20; i++) {
            assertEquals(300, VerticalTable.getVertical(i));
        }
    }

    @Test
    public void testVerticalForLifts21to30() {
        for (int i = 21; i <= 30; i++) {
            assertEquals(400, VerticalTable.getVertical(i));
        }
    }

    @Test
    public void testVerticalForLifts31to40() {
        for (int i = 31; i <= 40; i++) {
            assertEquals(500, VerticalTable.getVertical(i));
        }
    }

    @Test
    public void testInvalidLiftThrows() {
        assertThrows(IllegalArgumentException.class, () -> VerticalTable.getVertical(0));
        assertThrows(IllegalArgumentException.class, () -> VerticalTable.getVertical(41));
        assertThrows(IllegalArgumentException.class, () -> VerticalTable.getVertical(-3));
    }
}

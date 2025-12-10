package ui;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class InputUtilsTest {

    private Scanner mockScanner(String input) {
        return new Scanner(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    void testReadIntValid() {
        Scanner sc = mockScanner("10\n");
        int result = InputUtils.readInt(sc, "Enter int: ");
        assertEquals(10, result);
    }

    @Test
    void testReadIntInvalidThenValid() {
        Scanner sc = mockScanner("abc\n15\n");
        int result = InputUtils.readInt(sc, "Enter int: ");
        assertEquals(15, result);
    }

    @Test
    void testReadStringValid() {
        Scanner sc = mockScanner("Hello World\n");
        String result = InputUtils.readString(sc, "Enter string: ");
        assertEquals("Hello World", result);
    }

    @Test
    void testReadStringEmptyThenValid() {
        Scanner sc = mockScanner("\n\nResult\n");
        String result = InputUtils.readString(sc, "Enter string: ");
        assertEquals("Result", result);
    }

    @Test
    void testReadMoneyValid() {
        Scanner sc = mockScanner("100.50\n");
        BigDecimal result = InputUtils.readMoney(sc, "Enter money: ");
        assertEquals(new BigDecimal("100.50"), result);
    }

    @Test
    void testReadMoneyWithComma() {
        Scanner sc = mockScanner("200,99\n");
        BigDecimal result = InputUtils.readMoney(sc, "Enter money: ");
        assertEquals(new BigDecimal("200.99"), result);
    }

    @Test
    void testReadMoneyNegativeThenValid() {
        Scanner sc = mockScanner("-100\n50\n");
        BigDecimal result = InputUtils.readMoney(sc, "Enter money: ");
        assertEquals(new BigDecimal("50"), result);
    }

    @Test
    void testReadMoneyInvalidTextThenValid() {
        Scanner sc = mockScanner("abc\n10\n");
        BigDecimal result = InputUtils.readMoney(sc, "Enter money: ");
        assertEquals(new BigDecimal("10"), result);
    }

    @Test
    void testConfirmYes() {
        Scanner sc = mockScanner("y\n");
        assertTrue(InputUtils.confirm(sc, "Confirm?"));
    }

    @Test
    void testConfirmYesFull() {
        Scanner sc = mockScanner("yes\n");
        assertTrue(InputUtils.confirm(sc, "Confirm?"));
    }

    @Test
    void testConfirmNo() {
        Scanner sc = mockScanner("n\n");
        assertFalse(InputUtils.confirm(sc, "Confirm?"));
    }

    @Test
    void testConfirmOther() {
        Scanner sc = mockScanner("abc\n");
        assertFalse(InputUtils.confirm(sc, "Confirm?"));
    }

    @Test
    void testReadDateValid() {
        Scanner sc = mockScanner("2025-12-31\n");
        LocalDate result = InputUtils.readDate(sc, "Enter date: ");
        assertEquals(LocalDate.of(2025, 12, 31), result);
    }

    @Test
    void testReadDateEmptyIsNow() {
        Scanner sc = mockScanner("\n");
        LocalDate result = InputUtils.readDate(sc, "Enter date: ");
        assertEquals(LocalDate.now(), result);
    }

    @Test
    void testReadDateInvalidThenValid() {
        Scanner sc = mockScanner("bad-date\n2024-01-01\n");
        LocalDate result = InputUtils.readDate(sc, "Enter date: ");
        assertEquals(LocalDate.of(2024, 1, 1), result);
    }
}
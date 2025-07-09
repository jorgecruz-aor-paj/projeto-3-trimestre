package aor.proj3t.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple test class to verify the JUnit setup works correctly.
 */
public class BasicTest {

    @Test
    @DisplayName("Simple assertion test to verify JUnit is working")
    public void testJUnitSetup() {
        // Simple assertion to verify that JUnit is working
        assertTrue(true, "JUnit setup is working if this test passes");
        assertEquals("expected", "expected", "String equality should work");
        assertNotEquals("expected", "actual", "Different strings should not be equal");
    }

    @Test
    @DisplayName("Math operations test")
    public void testMathOperations() {
        assertEquals(4, 2 + 2, "Addition should work correctly");
        assertEquals(0, 2 - 2, "Subtraction should work correctly");
        assertEquals(4, 2 * 2, "Multiplication should work correctly");
        assertEquals(1, 2 / 2, "Division should work correctly");
    }
}

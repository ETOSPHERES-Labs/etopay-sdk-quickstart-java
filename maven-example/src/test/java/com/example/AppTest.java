package com.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void appHasBuildInfo() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest.getBuildInfo(), "app should have build info");
    }
}

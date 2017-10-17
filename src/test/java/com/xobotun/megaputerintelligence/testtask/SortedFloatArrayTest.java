package com.xobotun.megaputerintelligence.testtask;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class SortedFloatArrayTest {
    public static SortedFloatArray Data;
    public static float[] UnwrappedData;

    @BeforeClass
    public static void Setup() {
        Data = new SortedFloatArray();
        UnwrappedData = Data.GetData();
    }

    // Должен быть только один такой элемент, у которого индекс равен целой части.
    @Test
    public void TestNumberOfDesireElementsIsOnlyOne() {
        int desiredElementsCounter = 0;
        for (int i = 0; i < SortedFloatArray.ARRAY_SIZE; ++i) {
            if (ElementFinder.IsElementDesired(i, UnwrappedData[i])) {
                desiredElementsCounter++;
            }
        }
        assertEquals("There should not be more than one desired element.",1, desiredElementsCounter);
    }

    // Каждый следующий элемент должен быть больше предыдущего.
    @Test
    public void TestIsSequenceNondecreasing() {
        for (int i = 0; i < SortedFloatArray.ARRAY_SIZE - 1; ++i) {
            assertTrue("Next element should be greater than previous.", UnwrappedData[i] < UnwrappedData[i+1]);
        }
    }

    // `_desiredElementLocation` должен возвращать индекс действительно требуемого элемента.
    @Test
    public void TestDesiredElementLocationPointsToDesiredElement() {
        assertTrue("GetDesiredItemLocation should return desired element location, not something else.", ElementFinder.IsElementDesired(Data.GetDesiredItemLocation(), UnwrappedData[Data.GetDesiredItemLocation()]));
    }
}

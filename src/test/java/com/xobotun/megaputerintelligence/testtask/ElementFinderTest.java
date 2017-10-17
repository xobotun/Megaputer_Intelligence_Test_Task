package com.xobotun.megaputerintelligence.testtask;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ElementFinderTest {
    public static ElementFinder Finder;
    public static SortedFloatArray Data;
    public static float[] UnwrappedData;

    @BeforeClass
    public static void Setup() {
        Finder = new ElementFinder();
        Data = new SortedFloatArray();
        UnwrappedData = Data.GetData();
    }

    // `GetIndexOfDesiredElement` должен возвращать позицию желаемого элемента, а не что-либо ещё.
    @Test
    public void TestGetIndexOfDesiredElementReturnsDesiredElementLocation() {
        int position = Finder.GetIndexOfDesiredElement(Data);
        assertTrue("Element at GetIndexOfDesiredElement should satisgy requirements.", position == (int) UnwrappedData[position]);
    }

    // Перегрузки `GetIndexOfDesiredElement` должены возвращать одинаковое значение.
    @Test
    public void TestGetIndexOfDesiredElementOverloadsRetunSame() {
        int position1 = Finder.GetIndexOfDesiredElement(Data);
        int position2 = Finder.GetIndexOfDesiredElement(Data.GetData());
        assertEquals("GetIndexOfDesiredElement should return same value in both overloads", position2, position1);
    }

}
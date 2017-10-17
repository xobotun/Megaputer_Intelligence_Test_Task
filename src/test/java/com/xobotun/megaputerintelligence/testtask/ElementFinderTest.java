package com.xobotun.megaputerintelligence.testtask;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ElementFinderTest {
    public static ElementFinder Finder;
    public static SortedFloatArray Data;
    public static float[] UnwrappedData;

    @BeforeClass
    public static void Setup() {
        Finder = new ElementFinder();
        // Зачем гонять тесты на тяжелых данных, когда можно взять лёгкие и фальшивые?
        Data = mock(SortedFloatArray.class);
        when(Data.GetData()).thenReturn(new float[] {-1f, 0.5f, 2f, 2.5f, 3f});
        //                                           0    1     2   3     4
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
    public void TestGetIndexOfDesiredElementOverloadsReturnSame() {
        int position1 = Finder.GetIndexOfDesiredElement(Data);
        int position2 = Finder.GetIndexOfDesiredElement(Data.GetData());
        assertEquals("GetIndexOfDesiredElement should return same value in both overloads", position2, position1);
    }

}
package com.xobotun.megaputerintelligence.testtask;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(value = Parameterized.class)
public class ElementFinderTest {
    public static ElementFinder Finder;
    public static SortedFloatArray Data;
    public static float[] UnwrappedData;

    @Parameterized.Parameters
    public static Collection GetDifferentImplementations() {
        ArrayList implementations = new ArrayList(1);

        implementations.add(SimpleElementFinder.class);
        implementations.add(ThreadPoolElementFinder.class);
        implementations.add(CustomThreadPoolElementFinder.class);

        return implementations;
    }

    public ElementFinderTest(Class implementation) throws Exception {
        Finder = (ElementFinder) implementation.newInstance();  // Этих исключений быть не должно. Если они есть, то что-то действително пошло не так.
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
        assertTrue("Element at GetIndexOfDesiredElement should satisfy requirements.", ElementFinder.IsElementDesired(position, UnwrappedData[position]));
    }

    // Перегрузки `GetIndexOfDesiredElement` должены возвращать одинаковое значение.
    @Test
    public void TestGetIndexOfDesiredElementOverloadsReturnSame() {
        int originalPosition = Finder.GetIndexOfDesiredElement(Data.GetData());
        int overloadedPosition1 = Finder.GetIndexOfDesiredElement(Data);
        assertEquals("GetIndexOfDesiredElement should return same value in both overloads", originalPosition, overloadedPosition1);
    }

    // `GetIndexOfDesiredElement` должен реализовывать проверку именно на то, что мы ищем.
    @Test
    public void TestIsElementDesiredShouldImplementBusinessLogic() {
        assertTrue("IsElementDesired should treat 3f at position 3 as desired element.", ElementFinder.IsElementDesired(3, 3f));
        assertFalse("IsElementDesired should not treat 4f at position 2 as desired element.", ElementFinder.IsElementDesired(2, 4f));
    }

}
package com.xobotun.megaputerintelligence.testtask;

public interface ElementFinder {
    // Функция, реализующая суть класса.
    // @param `data` – входной массив из миллиона элементов.
    // @return Позиция первого элемента, у которого целая часть равна индексу.
    int GetIndexOfDesiredElement(float[] data);

    // Перегруженная версия `GetIndexOfDesiredElement`. Принимает неразвёрнутый контейнер массива из миллиона
    // элементов.
    // @see ElementFinder#GetIndexOfDesiredElement(float[] data)
    int GetIndexOfDesiredElement(SortedFloatArray data);

    // Функция, проверяющая, является ли конкретный элемент искомым.
    // @param `position` – индекс элемента в массиве.
    // @param `data` – элемент массива, расположенный по этому индексу. /*@contract*/
    // @return `true`, если целая часть равна индексу. `false`, целая часть не равна индексу.
    default boolean IsElementDesired(int position, float data) {
        return position == (int) data;
    }
}

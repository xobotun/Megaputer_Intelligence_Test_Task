package com.xobotun.megaputerintelligence.testtask;

// В интерфейс у меня не получилось его переделать. :(
public abstract class ElementFinder {
    // Функция, реализующая суть класса.
    // @param `data` – входной массив из миллиона элементов.
    // @return Позиция первого элемента, у которого целая часть равна индексу.
    abstract public int GetIndexOfDesiredElement(float[] data);

    // Перегруженная версия `GetIndexOfDesiredElement`. Принимает неразвёрнутый контейнер массива из миллиона
    // элементов.
    // @see ElementFinder#GetIndexOfDesiredElement(float[] data)
    abstract public int GetIndexOfDesiredElement(SortedFloatArray data);

    // Функция, проверяющая, является ли конкретный элемент искомым.
    // @param `position` – индекс элемента в массиве.
    // @param `data` – элемент массива, расположенный по этому индексу. /*@contract*/
    // @return `true`, если целая часть равна индексу. `false`, целая часть не равна индексу.
    static boolean IsElementDesired(int position, float data) {
        return position == (int) data;
    }
}

package com.xobotun.megaputerintelligence.testtask;

// Класс, инкапсулирующий алгоритм поиска требуемого элемента. Сейчас это бессиысленно, но потом здесь будет
// многопоточный поиск.
public class SimpleElementFinder implements ElementFinder {
    // Функция, реализующая суть класса.
    // @param `data` – входной массив из миллиона элементов.
    // @return Позиция первого элемента, у которого целая часть равна индексу. Если такого элемента нет,
    // возвращает `-1`.
    public int GetIndexOfDesiredElement(float[] data) {
        for (int i = 0; i < data.length; ++i) {
            if (IsElementDesired(i, data[i])) {
                return i;
            }
        }
        return -1;
    }

    // Перенруженная версия `GetIndexOfDesiredElement`. Принимает неразвёрнутый контейнер массива из миллиона
    // элементов.
    // @see ElementFinder#GetIndexOfDesiredElement(float[] data)
    public int GetIndexOfDesiredElement(SortedFloatArray data) {
        return GetIndexOfDesiredElement(data.GetData());
    }
}

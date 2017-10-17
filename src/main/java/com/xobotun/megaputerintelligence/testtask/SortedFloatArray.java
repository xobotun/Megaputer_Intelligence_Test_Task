package com.xobotun.megaputerintelligence.testtask;


import java.util.Random;

// Класс-контейнер для float[].
//
// Я пришёл к такому решению после некоторого обдумывания задачи. Мне кажется,  что использование небольшого количества
// дополнительной памяти O(1) не страшно, по сравнению с использованием `float[]` везде, где только можно.
// Кроме того, он содержит некоторые константы и вспомогательные значения.
//
public class SortedFloatArray {
    // Использование константы для размера массива позволяет легче модифицировать код, изменяя значение только в одном
    // месте, а не в десятках различных мест по всему коду. ИМХО, доступ к константе быстрее, чем к свойству
    // `length` объекта. Она может быть ещё и inline.
    public static final int ARRAY_SIZE = 1_000_000;

    // Сами данные, над которыми производится операция. Специально используется `float`, а не `Float`, чтобы сэкономить
    // память. `float[]` займёт чуть меньше 3.9МБ памяти, а `Float[]` будет больше вплоть до 5-7 раз. Цифры взяты из
    // комментариев к ответу https://stackoverflow.com/a/16528236.
    private float[] _data = new float[ARRAY_SIZE];
    // Сгенерируем случайный индекс требуемого элемента. Да и тестировать удоно.
    private int _desiredItemLocation = Math.abs(new Random().nextInt()) % ARRAY_SIZE;

    // Конструктор генерирует случайные числа, соответсвующие условию задачи. За правило построения массива принята
    // прямая `y = kx + b`, при условии, что `k := i` и `b := 0`.
    public SortedFloatArray() {
        // Генерируем числа на 1 ниже, чем прямая.
        for (int i = 0; i < _desiredItemLocation; ++i) {
            _data[i] = i - 1;
        }

        // Искомый элемент с целой частью, равной индексу.
        _data[_desiredItemLocation] = _desiredItemLocation;

        // Генерируем числа на 1 выше, чем прямая. Как первый цикл, только чуть иначе.
        for (int i = _desiredItemLocation + 1; i < ARRAY_SIZE; ++i) {
            _data[i] = i + 1;
        }
    }

    // Геттеры
    public float[] GetData() {
        return _data;
    }
    public int GetDesiredItemLocation() {
        return _desiredItemLocation;
    }
}

package com.xobotun.megaputerintelligence.testtask;

public class Main
{
    public static final int BENCHMARK_TIMES = 100;
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException
    {
        // Начинаем замерять время исполнения
        long totalTimeTook = 0;

        // Усредним результат выполнения тестовых прогонов.
        for (int i = 0; i < BENCHMARK_TIMES; ++i){
            // Объект, содержащий в себе миллион элементов, заданных по определённому правилу.
            SortedFloatArray array = new SortedFloatArray();

            // Создаём искатель
            ElementFinder finder = new CustomThreadPoolElementFinder();

            // Замеряем время.
            long started = System.nanoTime();
            int desiredElementLocation = finder.GetIndexOfDesiredElement(array);
            long finished = System.nanoTime();
            totalTimeTook += finished - started;

            // Печать найденного элемента.
            System.out.println("Found: " + desiredElementLocation);
        }

        // Сколько времени заняло в среднем.
        long averageTimeTook = totalTimeTook / BENCHMARK_TIMES;

        System.out.println("Average time spent computing: " + averageTimeTook / 1_000 + " us.");
        System.out.println("Total time spent computing: " + totalTimeTook / 1_000 + " us.");
    }
}

// Core i3-7300 @ 4 Ghz
// SimpleElementFinder обрабатывает 1_677_000_0 элементов за 6079 мкс в IDE и за 7814 мкс в Powershell Win10.
// ThreadPoolElementFinder обрабатывает 1_677_000_0 элементов за 12863 мкс в IDE и за 14539 мкс в Powershell Win10. Но это с volatile.
// ThreadPoolElementFinder обрабатывает 1_677_000_0 элементов за 6743 мкс в IDE и за 9237 мкс в Powershell Win10. Это с выстрелом в ногу.
// CustomThreadPoolElementFinder обрабатывает 1_677_000_0 элементов за 14040 мкс в IDE и за 20423 мкс в Powershell Win10. Это печально.
//
// Вывод: я не умею в многопоточную джаву.
//
//  SimpleElementFinder 1_000_000 за 517 мкс.
//  Слегка поправленный ThreadPoolElementFinder 1_000_000 за 2349 мкс.
//  Жутко кривой CustomThreadPoolElementFinder 1_000_000 за 2833 мкс.
//  С каждым шагом всё хуже.
//  Буду доделывать потом...

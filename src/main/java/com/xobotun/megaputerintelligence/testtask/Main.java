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
            ElementFinder finder = new ThreadPoolElementFinder();

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


// SimpleElementFinder обрабатывает 1_677_000_0 элементов за 6079 мкс.
// ThreadPoolElementFinder обрабатывает 1_677_000_0 элементов за 12863 мкс. Но это с volatile.
//

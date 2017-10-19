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
// ThreadPoolElementFinder (А) обрабатывает 1_677_000_0 элементов за 12863 мкс в IDE и за 14539 мкс в Powershell Win10. Но это с volatile.
// ThreadPoolElementFinder (Б) обрабатывает 1_677_000_0 элементов за 6743 мкс в IDE и за 9237 мкс в Powershell Win10. Это с выстрелом в ногу.
// CustomThreadPoolElementFinder (А) обрабатывает 1_677_000_0 элементов за 14040 мкс в IDE и за 20423 мкс в Powershell Win10. Это печально.
// CustomThreadPoolElementFinder (Б) обрабатывает 1_677_000_0 элементов за 3293 мкс в IDE и за 6265 мкс в Powershell Win10. Это вин!
//
// SimpleElementFinder – простейший однопоточный поисковик. 100% КПД от однопоточного. Выигрыш от распараллеливания должен составить 200% КПД.
// ThreadPoolElementFinder (А) – попытка использовать Java 7 и volatile для остановки. Бегаем в RAM, 50% КПД от однопоточного.
// ThreadPoolElementFinder (Б) – попытка использовать Java 7 и Thread::stop для остановки. Не бегаем в RAM за флагом, но общие данные и скачем по памяти. 100% КПД от однопоточного.
// CustomThreadPoolElementFinder (А) – кривая версия ThreadPoolElementFinder (Б), запускающая лямюда-Runnable в потоках. Общие данные и скачем по памяти. 40% КПД от однопоточного.
// CustomThreadPoolElementFinder (Б) – полностью переписанная версия на своих собственных потоках. Данные у каждого потока свои, остановка через локальные переменнные. 200% КПД от однопоточного. Ура!


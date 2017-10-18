package com.xobotun.megaputerintelligence.testtask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// Класс, инкапсулирующий алгоритм поиска требуемого элемента. Сейчас это бессиысленно, но потом здесь будет
// многопоточный поиск.
public class ThreadPoolElementFinder extends ElementFinder {
    // Количество потоков, которые будут одновременно работать. Возможно, на одинаковых данных
    // гипертрединг здесь сможет помочь.
    private int _numberOfThreads = Runtime.getRuntime().availableProcessors();

    // Потоки, которые будут работать за нас.
    private ExecutorService _threadPool;

    // Будем хранить состояние объекта. Возможно, потом потребуется создать ElementFinderFactory.
    private float[] _data;

    // Глобальная переменная на несколько потоков, чтобы когда один из потоков нашёл искомый элемент, остальные
    // прекратили свою работу. Тут стоит вопрос, как сильно повлияет бегание каждый такт в оперативку или L3 кеш
    // за значением переменной.
    volatile private boolean _hasDesiredElementBeenFound = false;

    // А почему бы его индекс не вынести сюда? И да, мне не нравится слово Location, но я его уже использую.
    // Да и комментарии надо было через /** */ сделать. :)
    private int _elementLocation = -1;

    // Функция, реализующая суть класса.
    // @param `data` – входной массив из миллиона элементов.
    // @return Позиция первого элемента, у которого целая часть равна индексу. Если такого элемента нет,
    // возвращает `-1`.
    public int GetIndexOfDesiredElement(float[] data) {
        if (data == _data && _hasDesiredElementBeenFound) {
            return _elementLocation;
        } else {
            _threadPool = Executors.newFixedThreadPool(_numberOfThreads);
        }

        _data = data;

        // Назначем потокам количество элемментов для обработки.
        List<Integer> numberOfElementsPerThread = SplitDataIntoChunks(/*data*/);
        List<Future<Integer>> results = new ArrayList<>(_numberOfThreads);

        // Смещение в массиве для поиска элементов.
        int offset = 0;
        // Назначем потоку задание.
        for (Integer numberOfElements : numberOfElementsPerThread) {
            // Замыкание offset на конкретный поток. Но я всё ещё не уверен в кеш-промахах.
            int localOffset = offset;

            Future<Integer> taskResult = _threadPool.submit(() -> {
                // Сам поисковый цикл.
                for (int i = 0; i < numberOfElements; ++i) {
                    // Сама суть
                    if (IsElementDesired(localOffset + i, data[localOffset + i])) {
                        _elementLocation = localOffset + i;
                        _threadPool.shutdownNow();
                        return localOffset + i;
                    }
                }
                // Если искомый элемент находится не здесь.
                return -1;
            });
            results.add(taskResult);

            // Увеличиваем смещение для следующего потока.
            offset += numberOfElements;
        }

        // Выключаем пул, чтобы программа могла завершиться.
        _threadPool.shutdown();

        // Возвращаем значение из всех потоков. Ну, может, и блокирующий вызов.
        int result = 0xDEADBEEF;
        for (Future<Integer> task : results) {
            try {
                // Получаем значение от одного из выполнившихся тасков.
                int taskResult = task.get();
                // Если корректное значение, то точно нашли.
                if (taskResult != -1) {
                    result = taskResult;
                    break;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    // Перенруженная версия `GetIndexOfDesiredElement`. Принимает неразвёрнутый контейнер массива из миллиона
    // элементов.
    // @see ElementFinder#GetIndexOfDesiredElement(float[] data)
    public int GetIndexOfDesiredElement(SortedFloatArray data) {
        return GetIndexOfDesiredElement(data.GetData());
    }

    // Функция, распределяющая элементы массива для поиска между потоками.
    // @param `_data` – входные данные.
    // @returns список, в котором в равной пропорции распределены количество элементов массива для каждого потока.
    private List<Integer> SplitDataIntoChunks() {
        // Сколько элементов предстоит обработать одному потоку.
        final int fraction = _data.length % _numberOfThreads;
        // Переменная-счётчик, описывающая, скольким элементам ещё не назначен поток.
        int elementsLeft = _data.length;
        // Отображение "индекс потока" → "количество элементов".
        final ArrayList<Integer> result = new ArrayList<>(_numberOfThreads);

        for (int i = 0; i < _numberOfThreads - 1; ++i) {
            result.add(fraction);
            elementsLeft -= fraction;
        }

        // На случай если осталось большее число элементов, чем fraction. Например, 100 элементов и 3 потока.
        // поток №1 – 33 элемента, №2 – 33, а №3 – 34.
        result.add(elementsLeft);

        return result;
    }
}

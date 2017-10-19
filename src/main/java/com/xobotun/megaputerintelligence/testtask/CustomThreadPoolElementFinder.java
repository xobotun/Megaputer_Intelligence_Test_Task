package com.xobotun.megaputerintelligence.testtask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomThreadPoolElementFinder extends ElementFinder {
    private int _numberOfThreads = Runtime.getRuntime().availableProcessors();
    private List<ElementFindingThread> _threadPool;

    private float[] _data;
    private int _elementLocation = -1;
    volatile boolean _hasDesiredElementBeenFound = false;

    public int GetIndexOfDesiredElement(float[] data) {
        if (data == _data && _hasDesiredElementBeenFound) {
            return _elementLocation;
        }
        _data = data;
        _threadPool = new ArrayList<>(_numberOfThreads);
        AssignThreads();

        synchronized (_threadPool) {
            for (Thread thread : _threadPool)
                thread.start();

            try {
                _threadPool.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return _elementLocation;
    }

    public int GetIndexOfDesiredElement(SortedFloatArray data) {
        return GetIndexOfDesiredElement(data.GetData());
    }

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

    private void AssignThreads() {
        int offset = 0;
        List<Integer> numberOfElementsPerThread = SplitDataIntoChunks();
        for (Integer numberOfElements : numberOfElementsPerThread) {
            _threadPool.add(new ElementFindingThread(_data, offset, numberOfElements));
            offset += numberOfElements;
        }
    }

    private void StopThreads() {
        for (ElementFindingThread thread : _threadPool) {
            thread._enabled = false;
        }
    }

    class ElementFindingThread extends Thread {
        private float[] _localData;
        private int _offset;
        private int _size;
        public boolean _enabled = true;

        ElementFindingThread(float[] dataToCopy, int offset, int size) {
            _offset = offset;
            _size = size;
            _localData = Arrays.copyOfRange(dataToCopy, offset, offset + size);
        }

        @Override
        public void run() {
            for (int i = 0; i < _size; ++i) {
                if (IsElementDesired(_offset + i, _localData[i])) {
                    synchronized (_threadPool) {
                        _elementLocation = _offset + i;
                        _hasDesiredElementBeenFound = true;
                        _threadPool.notify();
                        CustomThreadPoolElementFinder.this.StopThreads();
                    }
                }

                if (!_enabled)
                    break;
            }
        }

    }
}

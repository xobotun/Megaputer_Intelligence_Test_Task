package com.xobotun.megaputerintelligence.testtask;

public class Main
{
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException
    {
        // Объект, содержащий в себе миллион элементов, заданных по определённому правилу.
        SortedFloatArray array = new SortedFloatArray();

        // Печать найденного элемента.
        System.out.println("Found:  " + new ThreadPoolElementFinder().GetIndexOfDesiredElement(array));
    }
}

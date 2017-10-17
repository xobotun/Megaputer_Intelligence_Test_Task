package com.xobotun.megaputerintelligence.testtask;

public class Main
{
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException
    {
        // Объект, содержащий в себе миллион элементов, заданных по определённому правилу.
        SortedFloatArray array = new SortedFloatArray();

        // Печать найденного элемента.
        System.out.println("Found:  " + new ElementFinder().GetIndexOfDesiredElement(array));

        // С интроспекцией у меня нее вышло, держите public-поле.
        System.out.println("Actual: " + array._desiredItemLocation);
    }
}

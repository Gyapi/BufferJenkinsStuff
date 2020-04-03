package com.epam.training.danielvekas.genericbuffer.buffer;

import java.util.Comparator;
import java.util.List;

public interface GenericCircularBufferInterface<T> {

    void writeElementIntoTheBuffer(T elementToAdd) throws GenericCircularBufferException;
    void writeArrayIntoTheBuffer(T[] arrayToAdd) throws GenericCircularBufferException;
    void writeListIntoTheBuffer(List<? extends T> listToAdd) throws GenericCircularBufferException;

    T readNextElementFromTheBuffer() throws GenericCircularBufferException;
    T[] readBufferContentsAsArray() throws GenericCircularBufferException;
    List<T> readBufferContentsAsList() throws GenericCircularBufferException;

    T[] readBufferContentsAsSortedArray(Comparator<? super T> comparator) throws GenericCircularBufferException;
    List<T> readBufferContentsAsSortedList(Comparator<? super T> comparator) throws GenericCircularBufferException;

    void wipeBufferData();

    boolean isEmpty();
    boolean isFull();
    int getFreeSpace();
    int getOccupiedSpace();
}

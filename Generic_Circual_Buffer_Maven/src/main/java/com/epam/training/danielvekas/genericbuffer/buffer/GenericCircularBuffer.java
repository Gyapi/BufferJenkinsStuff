package com.epam.training.danielvekas.genericbuffer.buffer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class GenericCircularBuffer<T> implements GenericCircularBufferInterface<T> {

    private final T[] buffer;
    private int writeIndex = 0, readIndex = 0;
    private Class<T> bufferTypeParameter;


    /**
     * @param bufferTypeParameter :  the type of the buffer, given as a class literal (Type.class); example: Integer.class;
     * @param bufferSize : the size of the buffer, can't be changed later;
     */
    @SuppressWarnings("unchecked")
    public GenericCircularBuffer(Class<T> bufferTypeParameter, int bufferSize) {
        this.bufferTypeParameter = bufferTypeParameter;
        buffer = (T[]) Array.newInstance(bufferTypeParameter, bufferSize);
    }


    /**
     * @param elementToWrite : element to be stored in the buffer;
     * @throws GenericCircularBufferException : thrown if Buffer is full, or the given element has a wrong type;
     */
    @Override
    public void writeElementIntoTheBuffer(T elementToWrite) throws GenericCircularBufferException {
        if (isBufferFull())
            throw new GenericCircularBufferException("The buffer is full. Please data from it, to free up space.");
        else {
            addElementToBufferArray(elementToWrite);
        }
    }

    private boolean isBufferFull(){
        return readIndex == writeIndex && buffer[readIndex] != null;
    }

    private void addElementToBufferArray(T elementToWrite) throws GenericCircularBufferException {
        try {
            buffer[writeIndex] = elementToWrite;
            writeIndex = nextIndex(writeIndex);
        } catch (ArrayStoreException e){
            throw new GenericCircularBufferException("Wrong element type", e);
        }
    }

    private int nextIndex(int indexToChange){
        return (indexToChange + 1) % buffer.length;
    }

    /**
     * @param listToWrite : List<T> to write into the buffer;
     * @throws GenericCircularBufferException : thrown if the buffer is full, or if List<T>.size > free space in the buffer;
     */
    @Override
    public void writeListIntoTheBuffer(List<? extends T> listToWrite) throws GenericCircularBufferException{
        writeListIntoBufferArray(listToWrite);
    }

    private void writeListIntoBufferArray(List<? extends T> listToWrite) throws GenericCircularBufferException {
        if (!isBufferFull()) {
            if (calculateFreeSpaceInTheBuffer() >= listToWrite.size()) {
                for (T elementToAdd : listToWrite) {
                    addElementToBufferArray(elementToAdd);
                }
            }
            else throw new GenericCircularBufferException("Not enough space in the buffer");
        } else {
            throw new GenericCircularBufferException("The buffer is full.");
        }
    }

    private int calculateFreeSpaceInTheBuffer() {
        if (!isBufferEmpty() && !isBufferFull()) {
            return writeIndex < readIndex ? (readIndex - writeIndex) : (buffer.length - (writeIndex - readIndex));
        } else if (isBufferEmpty()) {
            return buffer.length;
        } else {
            return 0;
        }
    }

    private boolean isBufferEmpty(){
        return readIndex == writeIndex && buffer[readIndex] == null;
    }

    /**
     * @param arrayToWrite : T[] to write into the buffer;
     * @throws GenericCircularBufferException : thrown if the buffer is full, or if T[].length > free space in the buffer;
     */
    @Override
    public void writeArrayIntoTheBuffer(T[] arrayToWrite) throws GenericCircularBufferException {
        writeListIntoBufferArray(Arrays.asList(arrayToWrite));
    }


    /**
     * @return : the element read from the Buffer
     * @throws GenericCircularBufferException : thrown if there is no data to read;
     */
    @Override
    public T readNextElementFromTheBuffer() throws GenericCircularBufferException {
        if (isBufferEmpty())
            throw new RuntimeException("The buffer is empty. Please write data into it, before trying to read from it.");
        else {
            return readNextElementInTheBuffer();
        }
    }

    private T readNextElementInTheBuffer(){
        T returnData = buffer[readIndex];

        if (returnData != null) {
            buffer[readIndex] = null;
            readIndex = nextIndex(readIndex);
        }
        return returnData;
    }

    /**
     * @return : the contents of the buffer as a List<T>;
     * @throws GenericCircularBufferException : thrown  if the buffer is empty;
     */
    @Override
    public List<T> readBufferContentsAsList() throws GenericCircularBufferException {
        return readListFromBuffer();
    }

    private List<T> readListFromBuffer() throws GenericCircularBufferException {
        if (!isBufferEmpty()) {
            ArrayList returnList = new ArrayList<T>();
            T nextElement;
            nextElement = readNextElementInTheBuffer();
            while (nextElement != null){
                returnList.add(nextElement);
                nextElement = readNextElementInTheBuffer();
            }
            return returnList;
        } else {
            throw new GenericCircularBufferException("The buffer is empty.");
        }
    }

    /**
     * @return : the contents of the buffer as a T[];
     * @throws GenericCircularBufferException : thrown if the buffer is empty;
     */
    @SuppressWarnings("unchecked")
    @Override
    public T[] readBufferContentsAsArray() throws GenericCircularBufferException {
        List<T> returnList = readListFromBuffer();
        return returnList.toArray((T[]) Array.newInstance(bufferTypeParameter, returnList.size()));
    }

    /**
     * @param comparator : comparator to sort the list  with;
     * @return : contents of the buffer as a sorted List<T>, sorted with the given comparator;
     * @throws GenericCircularBufferException : thrown if the comparator has the wrong type, or if the buffer is empty;
     */
    @Override
    public List<T> readBufferContentsAsSortedList(Comparator<? super T> comparator) throws GenericCircularBufferException {
        return readAsSortedListFromBuffer(comparator);
    }

    private List<T> readAsSortedListFromBuffer(Comparator<? super T> comparator) throws GenericCircularBufferException {
        List<T> returnedSortedList = readListFromBuffer();
        try {
            returnedSortedList.sort(comparator);
        } catch (ClassCastException e){
            throw new GenericCircularBufferException("Wrong comparator type", e);
        }
        return returnedSortedList;
    }

    /**
     * @param comparator : comparator to sort the array  with;
     * @return : the contents of the buffer as a sorted T[], sorted with the given comparator;
     * @throws GenericCircularBufferException : thrown if the comparator has the wrong type, or if the buffer is empty;
     */
    @SuppressWarnings("unchecked")
    @Override
    public T[] readBufferContentsAsSortedArray(Comparator<? super T> comparator) throws GenericCircularBufferException {
        List<T> returnList = readAsSortedListFromBuffer(comparator);
        return returnList.toArray((T[]) Array.newInstance(bufferTypeParameter, returnList.size()));
    }


    @Override
    public void wipeBufferData(){
        if (!isBufferEmpty()){
            cleanBuffer();
        }
    }

    private void cleanBuffer(){
        Arrays.fill(buffer, null);
    }



    @Override
    public boolean isEmpty() {
        return isBufferEmpty();
    }

    @Override
    public boolean isFull() {
        return isBufferFull();
    }

    @Override
    public int getFreeSpace() {
        return calculateFreeSpaceInTheBuffer();
    }

    @Override
    public int getOccupiedSpace(){
        return buffer.length - calculateFreeSpaceInTheBuffer();
    }
}

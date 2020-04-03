package com.epam.training.danielvekas.genericbuffer.buffer;

import org.junit.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;

public class GenericCircularBufferTest {

    private GenericCircularBuffer testBuffer;
    private final int testBufferSize = 10;
    private final Class<Integer> classTypeParameter = Integer.class;
    private final Comparator<Integer> testComparator = (i, j) -> i - j;
    private final Comparator<String> testStringComparator = (o1, o2) -> {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        return o1.compareTo(o2);
    };
    private final Integer[] testIntArray = new Integer[]{10, 11, 12, 14, 13, 15};
    private final Integer[] sortedTestIntArray = new Integer[]{10, 11, 12, 13, 14, 15};
    private final String[] testStringArray = new String[]{"1", "2", "3"};
    private final List<Integer> testIntegerList = Arrays.asList(10, 12, 14, 13);
    private final List<Integer> sortedTestIntegerList = Arrays.asList(10, 12, 13, 14);
    private final List<String> testStringList = Arrays.asList("10", "12", "13", "14");


    @Before
    @SuppressWarnings("unchecked")
    public void createBuffer(){
        testBuffer = new GenericCircularBuffer(classTypeParameter, testBufferSize);
    }

    @SuppressWarnings("unchecked")
    private void bufferFiller(int elementNumber) throws GenericCircularBufferException {
        for (int i = 0; i < elementNumber; i++){
            testBuffer.writeElementIntoTheBuffer(i);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void canWriteAndRead() throws GenericCircularBufferException {
        //when
        testBuffer.writeElementIntoTheBuffer(10);
        testBuffer.writeElementIntoTheBuffer(15);
        testBuffer.writeElementIntoTheBuffer(32);

        //then
        assertEquals(10, testBuffer.readNextElementFromTheBuffer());
        assertEquals(15, testBuffer.readNextElementFromTheBuffer());
        assertEquals(32, testBuffer.readNextElementFromTheBuffer());
    }

    @Test(expected = GenericCircularBufferException.class)
    @SuppressWarnings("unchecked")
    public void cantWriteIntoFullBuffer() throws GenericCircularBufferException {
        bufferFiller(testBufferSize + 1);
    }

    @Test(expected = GenericCircularBufferException.class)
    @SuppressWarnings("unchecked")
    public void cantWriteWrongType() throws GenericCircularBufferException {
        testBuffer.writeElementIntoTheBuffer("ASD");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void canWriteAndReadArray() throws GenericCircularBufferException {

        testBuffer.writeArrayIntoTheBuffer(testIntArray);

        assertArrayEquals(testIntArray, testBuffer.readBufferContentsAsArray());
    }

    @Test(expected = GenericCircularBufferException.class)
    @SuppressWarnings("unchecked")
    public void cantWriteWrongArrayType() throws GenericCircularBufferException {
        testBuffer.writeArrayIntoTheBuffer(testStringArray);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void canReadAndWriteList() throws GenericCircularBufferException {
        testBuffer.writeListIntoTheBuffer(testIntegerList);

        assertEquals(testIntegerList, testBuffer.readBufferContentsAsList());
    }

    @Test(expected = GenericCircularBufferException.class)
    @SuppressWarnings("unchecked")
    public void canTWriteWrongListType() throws GenericCircularBufferException {
        testBuffer.writeListIntoTheBuffer(testStringList);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void canSortArray() throws GenericCircularBufferException {
        testBuffer.writeArrayIntoTheBuffer(testIntArray);

        assertArrayEquals(sortedTestIntArray, testBuffer.readBufferContentsAsSortedArray(testComparator));
    }

    @Test(expected = GenericCircularBufferException.class)
    @SuppressWarnings("unchecked")
    public void cantSortWithWrongComparator() throws GenericCircularBufferException {
        testBuffer.writeArrayIntoTheBuffer(testIntArray);

       testBuffer.readBufferContentsAsSortedArray(testStringComparator);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void canSortList() throws GenericCircularBufferException {
        testBuffer.writeListIntoTheBuffer(testIntegerList);

        assertEquals(sortedTestIntegerList, testBuffer.readBufferContentsAsSortedList(testComparator));
    }

    @Test(expected = GenericCircularBufferException.class)
    @SuppressWarnings("unchecked")
    public void cantSortWithWrongListComparator() throws GenericCircularBufferException {
        testBuffer.writeListIntoTheBuffer(testIntegerList);

        testBuffer.readBufferContentsAsSortedArray(testStringComparator);
    }

    @Test
    public void isTheBufferTrulyEmpty(){
        assertTrue(testBuffer.isEmpty());
        assertFalse(testBuffer.isFull());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void isTheBufferTrulyFull() throws GenericCircularBufferException {
        bufferFiller(testBufferSize);

        assertFalse(testBuffer.isEmpty());
        assertTrue(testBuffer.isFull());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void bufferShouldBeNotEmptyOrFull() throws GenericCircularBufferException {
        bufferFiller(testBufferSize  -2);

        assertFalse(testBuffer.isEmpty());
        assertFalse(testBuffer.isFull());

    }

    @Test
    @SuppressWarnings("unchecked")
    public void bufferSpaceCalculationTest() throws GenericCircularBufferException {
        bufferFiller(testBufferSize - 2);

        assertEquals(2, testBuffer.getFreeSpace());
        assertEquals(8, testBuffer.getOccupiedSpace());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void canBufferCleared() throws GenericCircularBufferException {
        bufferFiller(testBufferSize);

        testBuffer.wipeBufferData();

        assertTrue(testBuffer.isEmpty());
    }
}
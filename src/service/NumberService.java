package service;

import fileReader.FileReader;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class NumberService {

    private final FileReader fileReader;
    boolean firstNumber = true;

    public NumberService(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    public List<String> getData(Path path) {

        List<String> response = new ArrayList<>();

        final int[] resultInt = {Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0}; // max, min, maxSeq, minSeq
        final double[] resultDouble = {0.0, 0.0}; // median, avr

        final int[] sum = {0};
        final int[] seqData = {0, 0, 0}; // previousNumber, currentMaxSeq, currentMinSeq

        List<Integer> integers = new ArrayList<>();

        Stream<String> stringStream = fileReader.readFile(path);
        stringStream.mapToInt(Integer::valueOf).forEach(number -> {

            getMin(resultInt, number);
            getMax(resultInt, number);
            getSeq(seqData, number, resultInt);

            integers.add(number);
            sum[0] = sum[0] + number;
        });

        Collections.sort(integers);
        getMedian(integers, resultDouble);
        getAvr(integers, resultDouble, sum);
        flush(seqData, resultInt);

        createResponse(response, resultInt, resultDouble);

        return response;
    }

    private void createResponse(List<String> response, int[] resultInt, double[] resultDouble) {
        response.add("Max = " + resultInt[0]);
        response.add("Min = " + resultInt[1]);
        response.add("Median = " + resultDouble[0]);
        response.add("Avg = " + resultDouble[1]);
        response.add("MaxSeq = " + resultInt[2]);
        response.add("MinSeq = " + resultInt[3]);
    }

    private void getMin(int[] resultInt, int current) {
        if (resultInt[1] > current) {
            resultInt[1] = current;
        }
    }

    private void getMax(int[] resultInt, int current) {
        if (resultInt[0] < current) {
            resultInt[0] = current;
        }
    }

    /**
     * @param integers  Collection MUST be sorted.
     */
    private void getMedian(List<Integer> integers, double[] resultDouble) {
        int size = integers.size();

        if(size == 0) {
            throw new RuntimeException("Empty collection/file");
        }

        int half = size / 2;
        if (size % 2 == 0) {
            resultDouble[0] = (integers.get(half) + integers.get(half - 1)) / 2.0;
        } else {
            resultDouble[0] = integers.get(half);
        }
    }

    private void getAvr(List<Integer> integers, double[] resultDouble, int[] dataInt) {
        resultDouble[1] = (double) dataInt[0] / integers.size();
    }

    /**
     * At the end use flush to update resultInt because we don't know when the file ends
     * therefore there can be different data in seqData
     */
    private void getSeq(int[] seqData, int number, int[] resultInt) {

        if (firstNumber) {
            seqData[0] = number;
            seqData[1] = 1;
            seqData[2] = 1;
            firstNumber = false;
            return;
        }

        if (number > seqData[0]) {
            seqData[1] = seqData[1] + 1;
        } else {
            if (seqData[1] > resultInt[2]) {
                resultInt[2] = seqData[1];
            }
            seqData[1] = 1;
        }

        if (number < seqData[0]) {
            seqData[2] = seqData[2] + 1;
        } else {
            if (seqData[2] > resultInt[3]) {
                resultInt[3] = seqData[2];
            }
            seqData[2] = 1;
        }

        seqData[0] = number;
    }

    private void flush(int[] seqData, int[] resultInt) {
        if (seqData[1] > resultInt[2]) {
            resultInt[2] = seqData[1];
        }

        if (seqData[2] > resultInt[3]) {
            resultInt[3] = seqData[2];
        }
    }
}

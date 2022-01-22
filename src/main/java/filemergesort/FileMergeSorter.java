package filemergesort;

import filehandler.FileHandler;
import filemergesort.exception.FileNotSortedException;
import filemergesort.exception.IllegalFileDataStructException;
import stringtransformer.S2ITransformer;
import util.optionsparser.MergeSortParameters;
import util.optionsparser.SortMode;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileMergeSorter implements FileMergeSorterInterface {
    private void integerDataMergeSort(MergeSortParameters mergeSortParameters) throws IOException {

        // Comparing function and start value for compare
        int startValue = mergeSortParameters.getSortMode() == SortMode.ASCEND ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        Comparator<Integer> comparator = mergeSortParameters.getSortMode() == SortMode.ASCEND ? (o1, o2) -> o2 - o1 : Comparator.comparingInt(o -> o);

        // Creating handler for each input file
        List<FileHandler<Integer>> fileHandlers = new ArrayList<>();
        S2ITransformer s2iTransformer = new S2ITransformer();
        try {
            for (var fileName : mergeSortParameters.getInputFileNames()) {
                fileHandlers.add(new FileHandler<>(fileName, s2iTransformer, comparator));
            }
        } catch (IllegalFileDataStructException e) {
            System.err.println("[Edrror]" + e.getMessage());
        }

        // Removing empty files handlers
        fileHandlers.removeIf(fileHandler -> fileHandler.getCurElem() == null);

        // While all input files hasn't been handled
        PrintStream outputFile = new PrintStream(new FileOutputStream(mergeSortParameters.getOutputFileName()));
        while (fileHandlers.size() > 0) {
            // Find min or max element (it depends on the sort mode)
            int fileHandlerNextElemIndex = 0;
            int searchedElem = startValue;
            for (int handlerIndex = 0; handlerIndex < fileHandlers.size(); ++handlerIndex) {
                Integer curElem = fileHandlers.get(handlerIndex).getCurElem();
                if (comparator.compare(searchedElem, curElem) <= 0) {
                    fileHandlerNextElemIndex = handlerIndex;
                    searchedElem = curElem;
                }
            }

            // Printing in output file next element
            outputFile.println(searchedElem);

            // If all rows in file has handled
            try {
                if (fileHandlers.get(fileHandlerNextElemIndex).readNextElem() == null) {
                    fileHandlers.remove(fileHandlerNextElemIndex);
                }
            } catch (FileNotSortedException | IllegalFileDataStructException e) {
                System.err.println("[Error] " + e.getMessage());
                fileHandlers.remove(fileHandlerNextElemIndex);
            }
        }

    }

    private void stringDataMergeSort(MergeSortParameters mergeSortParameters) throws FileNotFoundException {

    }

    @Override
    public void sort(MergeSortParameters mergeSortParameters) throws FileNotFoundException, IOException {
        switch (mergeSortParameters.getDataType()) {
            case INTEGER -> integerDataMergeSort(mergeSortParameters);
            case STRING -> stringDataMergeSort(mergeSortParameters);
            default -> throw new IllegalArgumentException("Unknown file data type");
        }
    }
}

package filemergesort;

import filemergesort.comparator.IntegerComparator;
import filemergesort.comparator.StringComparator;
import filemergesort.filehandler.FileHandler;
import filemergesort.exception.FileNotSortedException;
import filemergesort.exception.IllegalFileDataStructException;
import filemergesort.stringtransformer.S2ITransformer;
import filemergesort.stringtransformer.STSTransformer;
import filemergesort.stringtransformer.Transformer;
import optionsparser.MergeSortParameters;
import optionsparser.SortMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileMergeSorter implements SorterInterface {
    private final static Logger logger = LoggerFactory.getLogger(FileMergeSorter.class);

    private <T> void mergeSort(String outputFileName, List<String> inputFileNames, Comparator<T> comparator, Transformer<String, T> transformer) {

        // Creating handler for each input file
        List<FileHandler<T>> fileHandlers = new ArrayList<>();

        for (var fileName : inputFileNames) {
            try {
                fileHandlers.add(new FileHandler<>(fileName, transformer, comparator));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }

        // Removing empty files handlers
        fileHandlers.removeIf(fileHandler -> fileHandler.getCurElem() == null);

        // While all input files hasn't been handled
        try (PrintWriter outputFile = new PrintWriter(outputFileName)) {
            Path outputFilePath = Paths.get(outputFileName);
            if (!Files.exists(outputFilePath)) {
                Files.createFile(outputFilePath);
            }

            while (fileHandlers.size() > 0) {
                // Find min or max element (it depends on the sort mode)
                int fileHandlerNextElemIndex = 0;
                T searchedElem = fileHandlers.get(0).getCurElem();
                for (int handlerIndex = 0; handlerIndex < fileHandlers.size(); ++handlerIndex) {
                    T curElem = fileHandlers.get(handlerIndex).getCurElem();
                    if (comparator.compare(curElem, searchedElem) >= 0) {
                        fileHandlerNextElemIndex = handlerIndex;
                        searchedElem = curElem;
                    }
                }

                // Printing in output file next element
                outputFile.println(searchedElem);

                // If all rows in file has handled
                try {
                    var fileHandler = fileHandlers.get(fileHandlerNextElemIndex);
                    T nextElem = fileHandler.nextElem();
                    if (nextElem == null) {
                        fileHandlers.remove(fileHandlerNextElemIndex);
                    }
                } catch (IOException e) {
                    logger.warn(e.getMessage());
                    fileHandlers.remove(fileHandlerNextElemIndex);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void sort(MergeSortParameters mergeSortParameters) {
        switch (mergeSortParameters.getDataType()) {
            case INTEGER -> {
                Comparator<Integer> comparator = mergeSortParameters.getSortMode() == SortMode.ASCEND ? new IntegerComparator() : new IntegerComparator().reversed();
                mergeSort(mergeSortParameters.getOutputFileName(), mergeSortParameters.getInputFileNames(), comparator, new S2ITransformer());
            }
            case STRING -> {
                Comparator<String> comparator = mergeSortParameters.getSortMode() == SortMode.ASCEND ? new StringComparator() : new StringComparator().reversed();
                mergeSort(mergeSortParameters.getOutputFileName(), mergeSortParameters.getInputFileNames(), comparator, new STSTransformer());
            }
            default -> throw new IllegalArgumentException("Unknown file data type");
        }
    }
}

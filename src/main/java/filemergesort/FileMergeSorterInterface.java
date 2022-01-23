package filemergesort;

import optionsparser.MergeSortParameters;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileMergeSorterInterface {
    void sort(MergeSortParameters mergeSortParameters) throws IOException;
}

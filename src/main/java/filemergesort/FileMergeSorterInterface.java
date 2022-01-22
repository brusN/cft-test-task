package filemergesort;

import util.optionsparser.MergeSortParameters;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileMergeSorterInterface {
    void sort(MergeSortParameters mergeSortParameters) throws FileNotFoundException, IOException;
}

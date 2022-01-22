package util.optionsparser;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MergeSortParameters {
    private SortDataType dataType;
    private SortMode sortMode = SortMode.ASCEND;
    private String outputFileName = "output.txt";
    private List<String> inputFileNames;
}

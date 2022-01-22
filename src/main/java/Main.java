import filemergesort.FileMergeSorter;
import org.apache.commons.cli.ParseException;
import util.optionsparser.InputArgsParser;
import util.optionsparser.MergeSortParameters;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        InputArgsParser inputArgsParser = InputArgsParser.getInstance();
        try {
            MergeSortParameters mergeSortParameters = inputArgsParser.parseInputArgs(args);
            FileMergeSorter mergeSorter = new FileMergeSorter();
            mergeSorter.sort(mergeSortParameters);
        } catch (IllegalArgumentException | ParseException | FileNotFoundException e) {
            System.err.println("[Error] " + e.getMessage());
            inputArgsParser.printUsage();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

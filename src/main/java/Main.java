import filemergesort.FileMergeSorter;
import org.apache.commons.cli.ParseException;
import optionsparser.InputArgsParser;
import optionsparser.MergeSortParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    private final static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        InputArgsParser inputArgsParser = InputArgsParser.getInstance();
        try {
            MergeSortParameters mergeSortParameters = inputArgsParser.parseInputArgs(args);
            FileMergeSorter mergeSorter = new FileMergeSorter();
            mergeSorter.sort(mergeSortParameters);
        } catch (IllegalArgumentException | ParseException | FileNotFoundException e) {
            logger.error(e.getMessage());
            inputArgsParser.printUsage();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

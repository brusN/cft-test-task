package util.optionsparser;

import org.apache.commons.cli.*;

public class InputArgsParser implements OptionsParserInterface {
    private static InputArgsParser inputArgsParser;
    private final Options options;

    private InputArgsParser() {
        options = new Options();
        options.addOption(OptionName.SORT_MODE_ASCEND.getOptionName(), false, "Sets ascending sort mode [Default: ASCENDING]");
        options.addOption(OptionName.SORT_MODE_DESCEND.getOptionName(), false, "Sets descending sort mode [Default: ASCENDING]");
        options.addOption(OptionName.SORT_DATA_TYPE_STRING.getOptionName(), false, "Sets string sort data type");
        options.addOption(OptionName.SORT_DATA_TYPE_INTEGER.getOptionName(), false, "Sets integer sort data type");
        options.addOption(OptionName.OUTPUT_FILENAME.getOptionName(), true, "Sets output file name [Default: output.txt]");
        options.addOption("h", "help", false, "Prints help");
    }

    public static InputArgsParser getInstance() {
        if (inputArgsParser == null) {
            synchronized (InputArgsParser.class) {
                if (inputArgsParser == null) {
                    inputArgsParser = new InputArgsParser();
                }
            }
        }
        return inputArgsParser;
    }

    private MergeSortParameters buildSortAttributes(CommandLine parsedOptions) {
        MergeSortParameters mergeSortParameters = new MergeSortParameters();

        // Setting data type
        if (parsedOptions.hasOption(OptionName.SORT_DATA_TYPE_INTEGER.getOptionName())) {
            mergeSortParameters.setDataType(SortDataType.INTEGER);
        } else {
            mergeSortParameters.setDataType(SortDataType.STRING);
        }

        // Setting sort mode
        if (parsedOptions.hasOption(OptionName.SORT_MODE_DESCEND.getOptionName())) {
            mergeSortParameters.setSortMode(SortMode.DESCEND);
        } else {
            mergeSortParameters.setSortMode(SortMode.ASCEND);
        }

        // Setting output file name
        if (parsedOptions.hasOption(OptionName.OUTPUT_FILENAME.getOptionName())) {
            mergeSortParameters.setOutputFileName(parsedOptions.getOptionValue(OptionName.OUTPUT_FILENAME.getOptionName()));
        } else {
            mergeSortParameters.setOutputFileName("output.txt");
        }

        // Setting input files list for merge sort
        mergeSortParameters.setInputFileNames(parsedOptions.getArgList());

        return mergeSortParameters;
    }
    
    private void validateParsedOptions(CommandLine parsedOptions) throws IllegalArgumentException {
        // Not allowed ASCENDING and DESCENDING sort mode in one time - should be only one define or default: ascending
        if (parsedOptions.hasOption(OptionName.SORT_MODE_ASCEND.getOptionName()) && parsedOptions.hasOption(OptionName.SORT_MODE_DESCEND.getOptionName())) {
            throw new IllegalArgumentException("Redefined sort mode option");
        }

        // Not allowed STRING and INTEGER sort data type in one time and should be defined one of these types
        // There is simplified expression: !(A v B) v (A & B) ~ A <-> B
        if (parsedOptions.hasOption(OptionName.SORT_DATA_TYPE_INTEGER.getOptionName()) == parsedOptions.hasOption(OptionName.SORT_DATA_TYPE_STRING.getOptionName())) {
            throw new IllegalArgumentException("Redefined or not specified sort data type option");
        }

        // No input files for merge sort
        if (parsedOptions.getArgs().length < 1) {
            throw new IllegalArgumentException("No input files for merge sort");
        }
    }

    @Override
    public MergeSortParameters parseInputArgs(String[] args) throws ParseException, IllegalArgumentException {
        CommandLineParser commandLineParser = new DefaultParser();
        CommandLine parsedOptions = commandLineParser.parse(options, args);
        if (parsedOptions.hasOption(OptionName.HELP.getOptionName())) {
            return null;
        }
        validateParsedOptions(parsedOptions);
        return buildSortAttributes(parsedOptions);
    }

    @Override
    public void printUsage() {
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("help", options);
    }
}

package simplemapreduce;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {

    private static final String FILENAME1_ARGUMENT_NAME = "file1";
    private static final String FILENAME2_ARGUMENT_NAME = "file2";
    private static final String OUTPUT_ARGUMENT_NAME = "output";
    private static final Logger LOGGER = LoggerFactory.getLogger( Main.class.getName() );

    public static void main(final String args[]) {
        final Options options = createOptions();

        try {
            final CommandLine commandLine = parseArguments(args, options);
            final String filename1 = commandLine.getOptionValue(FILENAME1_ARGUMENT_NAME);
            final String filename2 = commandLine.getOptionValue(FILENAME2_ARGUMENT_NAME);
            final String output = commandLine.getOptionValue(OUTPUT_ARGUMENT_NAME, "stdout");

            LOGGER.info("Simple Map Reduce: Word count");
            LOGGER.info(String.format("Files: \"%s\" and \"%s\"", filename1, filename2));
            LOGGER.info(String.format("Output will be: \"%s\"", output));
        }
        catch (final ParseException parseException) {
            LOGGER.error(parseException.getMessage());
            showHelp(options);

            System.exit(-1);
        }
    }

    private static Options createOptions() {
        final Options options = new Options();
        options.addOption(OUTPUT_ARGUMENT_NAME, true, "Specify output format: stdout (default), csv");
        options.addOption(OptionBuilder.withLongOpt(FILENAME1_ARGUMENT_NAME).withArgName(FILENAME1_ARGUMENT_NAME).hasArg().isRequired().withDescription("Name of the first file.").create());
        options.addOption(OptionBuilder.withLongOpt(FILENAME2_ARGUMENT_NAME).withArgName(FILENAME2_ARGUMENT_NAME).hasArg().isRequired().withDescription("Name of the second file.").create());

        return options;
    }

    private static void showHelp(final Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "SimpleMapReduce.jar -file1 <file1> -file2 <file2> [output]", options );
    }

    private static CommandLine parseArguments(final String arguments[], final Options options) throws ParseException {
        final CommandLineParser parser = new GnuParser();
        return parser.parse(options, arguments);
    }

}
